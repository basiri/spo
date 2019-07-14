package com.abs.spo.service;

import com.abs.spo.controller.dto.WorkforceRequestDTO;
import com.abs.spo.exception.NoSolutionNotFoundException;
import com.abs.spo.model.Gene;
import com.abs.spo.model.WorkforceAssignee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * According to the properties in application.properties this custom Implementation of GA algorithm
 * find the answer. the response time could be set according to depthLevel and revolutionCount
 */
@Service
public class WorkforceOptimizerServiceImpl implements WorkforceOptimizerService {
    private static final Logger logger = LoggerFactory.getLogger(WorkforceOptimizerServiceImpl.class);

    /**
     * there is a rate for finding the equivalent number of junior cleaners for a senior cleaner
     * it's assumed that by specifying the rate for seniors and juniors we can find the solution
     * the value could be set in application.properties
     */
    @Value("${SPO.srRate}")
    double srRate;
    @Value("${SPO.jrRate}")
    double jrRate;
    @Value("${SPO.depthLevel}")
    int depthLevel;
    @Value("${SPO.generationCount}")
    int revolutionCount;//number of generation
    @Value("${SPO.maxRoom}")
    int maxRoomSize;
    @Value("${SPO.minSeniorInStructure}")
    int minSenior;


    @Override
    public WorkforceAssignee[] customGASolution(WorkforceRequestDTO dto)
            throws NoSolutionNotFoundException {

        Gene bestGene= createNewGene(dto);
        int iteration= revolutionCount;//initiate number of revolution in GA algorithm
        while (iteration > 0) {
            //Creating a new gene
            Gene gene = createNewGene(dto);
            if (gene!=null) {
                if(bestGene==null){
                    bestGene=gene;//could not initialize
                }else{
                    //Comparing to bestGene and replacing if the bestGene is not the best
                    bestGene= (bestGene.getFitness() > gene.getFitness())?gene:bestGene;
                }
            }
            iteration--;
        }

        //at least there is one solution
        if (bestGene!=null) {
            logger.info(bestGene.toString());
            return bestGene.getAssignees();
        }else {
            logger.error("No solution found");
            throw new NoSolutionNotFoundException("no answer found");
        }
    }

    private Gene createNewGene(WorkforceRequestDTO dto) {
        int fitness = 0;//the variable for measuring fitness value of each chromosome.
        int remainingS = dto.getSenior();
        int remainingJ = dto.getJunior();
        boolean validGene = true;
        WorkforceAssignee[] assignees = new WorkforceAssignee[dto.getRooms().length];
        for (int i = 0; i < dto.getRooms().length; i++) {
            assignees[i] = getPossibleSeniorJunior(dto.getRooms()[i], remainingS, remainingJ, depthLevel);
            // Fitness calculation of the answer
            if(assignees[i]==null){
                validGene=false;
            }else{
                fitness+=geneFitnessCalculator(assignees[i]);
                remainingS = remainingS - assignees[i].getSenior();
                remainingJ = remainingJ - assignees[i].getJunior();
            }
        }
        if (validGene){
            return new Gene(assignees,fitness);
        }else{
            return null;
        }

    }

    /**
     * Calculates fitness based on minimum cleaners in the structure
     */
    // for future use for changing the fitness calculation and adding more factors
    private int geneFitnessCalculator(WorkforceAssignee result) {
        return result.getSenior()+result.getJunior();
    }

    private WorkforceAssignee getPossibleSeniorJunior(int room, int maxSenior,
                                                      int maxJunior, int cntLoopMax) {
        if (maxSenior < minSenior) {//violation of the rule one
            return null; //No possible answer
        }
        if (Math.round(room / srRate) < maxSenior) {
            maxSenior = (int) Math.ceil(Double.valueOf(room) / srRate);
        }
        while (cntLoopMax>0) {
            int s = (int) (Math.random() * (maxSenior)) + 1;
            int j = (int) Math.ceil(Double.valueOf(room - s * srRate) / jrRate);
            if (j > maxJunior || j < 0) {
                j = 0;
            }
            if (s * srRate + j * jrRate >= room) {
                return new WorkforceAssignee(s, j);
            }
            cntLoopMax--;
        }
        return null;
    }
}
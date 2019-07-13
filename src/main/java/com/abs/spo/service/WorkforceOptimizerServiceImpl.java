package com.abs.spo.service;

import com.abs.spo.controller.dto.WorkforceRequestDTO;
import com.abs.spo.exception.NoSolutionNotFoundException;
import com.abs.spo.model.WorkforceAssignee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WorkforceOptimizerServiceImpl implements WorkforceOptimizerService {
    private static final Logger logger = LoggerFactory.getLogger(WorkforceOptimizerServiceImpl.class);

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
    public WorkforceAssignee[] getSolution(WorkforceRequestDTO dto)
            throws NoSolutionNotFoundException {
        //TODO Log

        // population initialization
        WorkforceAssignee[] results = new WorkforceAssignee[dto.getRooms().length];
        WorkforceAssignee[] bestResult = new WorkforceAssignee[dto.getRooms().length];

        int bestFitness=maxRoomSize;//initial the worst answer
        int iteration= revolutionCount;
        while (iteration > 0) {
            int fitness = 0;//the variable for measuring fitness value of each chromosome.
            int remainingS = dto.getSenior();
            int remainingJ = dto.getJunior();
            boolean validGene = true;

            for (int i = 0; i < dto.getRooms().length; i++) {
                results[i] = getPossibleSeniorJunior(dto.getRooms()[i], remainingS, remainingJ, depthLevel);
                if (results[i] != null) {
                    remainingS = remainingS - results[i].getSenior();
                    remainingJ = remainingJ - results[i].getJunior();
                    fitness += results[i].getSenior()+results[i].getJunior();

                } else {
                    validGene = false;
                }
            }

            if (fitness < bestFitness && validGene) {
                bestFitness = fitness;
                for (int i = 0; i < dto.getRooms().length; i++) {
                    bestResult[i] = new WorkforceAssignee(results[i].getSenior(), results[i].getJunior());
                }
                for (WorkforceAssignee result : bestResult) {

                }

            }
            iteration--;
        }

        //at least there is one solution
        if (bestFitness < maxRoomSize) {
            for (WorkforceAssignee result : bestResult) {
                logger.info("{senior:" + result.getSenior() + " junior:" + result.getJunior()
                        + " Fitness: " + result.getSenior()+result.getJunior() + "}, ");
            }
            return bestResult;
        }
        throw new NoSolutionNotFoundException("no answer found");
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
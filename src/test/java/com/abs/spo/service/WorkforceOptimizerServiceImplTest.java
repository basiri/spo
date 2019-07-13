package com.abs.spo.service;

import com.abs.spo.controller.dto.WorkforceRequestDTO;
import com.abs.spo.exception.NoSolutionNotFoundException;
import com.abs.spo.model.WorkforceAssignee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.*;


import static org.springframework.test.util.ReflectionTestUtils.*;

@RunWith(SpringRunner.class)
@TestPropertySource("/application.properties")
public class WorkforceOptimizerServiceImplTest {
    private static final Logger logger = LoggerFactory.getLogger(WorkforceOptimizerServiceImplTest.class);

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


    private WorkforceOptimizerService optimizerService ;

    @Before
    public void init() {
        optimizerService= new WorkforceOptimizerServiceImpl();
        setField(optimizerService,"srRate",srRate);
        setField(optimizerService,"jrRate",jrRate);
        setField(optimizerService,"depthLevel",depthLevel);
        setField(optimizerService,"revolutionCount",revolutionCount);
        setField(optimizerService,"maxRoomSize",maxRoomSize);
        setField(optimizerService,"minSenior",minSenior);

    }


    //test for 3 structures and enough workers
    // Should be at least one senior in each structure
    @Test
    public void getSolutionWithTwoStructureAndExactNumberOfWorker() {
        int[] rooms= {(int)(srRate*3+jrRate*2),(int)(srRate*2+jrRate*1)};
        WorkforceRequestDTO dto = new WorkforceRequestDTO(rooms,5,3);
        WorkforceAssignee[] assignees = null;
        try {
             assignees= optimizerService.getSolution(dto);
        } catch (NoSolutionNotFoundException e) {

            e.printStackTrace();
        }
        assertEquals(assignees.length,2);
        assertEquals(assignees[0].getSenior()+assignees[0].getJunior(),5);
        assertEquals(assignees[1].getSenior()+assignees[1].getJunior(),3);
        for (WorkforceAssignee assignee:assignees
        ) {
            assertTrue(assignee.getSenior()>0);

        }
    }
    //test for 3 structures and enough workers
    // Should be at least one senior in each structure
    @Test
    public void getSolutionWithThreeStructureAndEnoughWorker() {
        int[] rooms= {35,21,17};
        WorkforceRequestDTO dto = new WorkforceRequestDTO(rooms,10,6);
        WorkforceAssignee[] assignees = null;
        try {
             assignees= optimizerService.getSolution(dto);
        } catch (NoSolutionNotFoundException e) {
            //e.printStackTrace();
            logger.info("State =" + this.getClass().getName()+", Message: "+ e.getMessage());
        }
        assertEquals(assignees.length,3);
        assertEquals(assignees[0].getSenior()+assignees[0].getJunior(),4);
        assertEquals(assignees[1].getSenior()+assignees[1].getJunior(),3);
        assertEquals(assignees[2].getSenior()+assignees[2].getJunior(),2);
        for (WorkforceAssignee assignee:assignees
        ) {
            assertTrue(assignee.getSenior()>0);

        }
    }

    //test for 2 structures and enough workers
    // Should be at least one senior in each structure
    @Test
    public void getSolutionWithTwoStructureAndEnoughWorker() {
        int[] rooms= {24,28};
        WorkforceRequestDTO dto = new WorkforceRequestDTO(rooms,11,6);
        WorkforceAssignee[] assignees = null;
        try {
            assignees= optimizerService.getSolution(dto);
        } catch (NoSolutionNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(assignees.length,2);
        assertEquals(assignees[0].getSenior()+assignees[0].getJunior(),3);
        assertEquals(assignees[1].getSenior()+assignees[1].getJunior(),3);
        for (WorkforceAssignee assignee:assignees
        ) {
            assertTrue(assignee.getSenior()>0);

        }
    }

    //test for 1 structures and enough workers
    // Should be at least one senior in each structure
    @Test
    public void getSolutionWithOneStructureAndEnoughWorker() {
        int[] rooms= {36};
        WorkforceRequestDTO dto = new WorkforceRequestDTO(rooms,2,2);
        WorkforceAssignee[] assignees = null;
        try {
            assignees= optimizerService.getSolution(dto);
        } catch (NoSolutionNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(assignees.length,1);
        assertEquals(assignees[0].getSenior()+assignees[0].getJunior(),4);
        for (WorkforceAssignee assignee:assignees
        ) {
            assertTrue(assignee.getSenior()>0);

        }
    }

    //test for 1 structure and Not enough workers
    // Should be at least one senior in each structure
    @Test //(expected = NoSolutionNotFoundException.class)
    public void getSolutionWithOneStructureAndNotEnoughWorker() {
        int[] rooms = {37};
        WorkforceRequestDTO dto = new WorkforceRequestDTO(rooms, 2, 2);

        assertThatThrownBy(() -> optimizerService.getSolution(dto))
        .isInstanceOf(NoSolutionNotFoundException.class);
    }
    //test for some structures and Not enough workers
    // Should be at least one senior in each structure
    @Test //(expected = NoSolutionNotFoundException.class)
    public void getSolutionWithNotEnoughSenior() {
        int[] rooms = {37,16};
        WorkforceRequestDTO dto = new WorkforceRequestDTO(rooms, 1, 20);
        final WorkforceAssignee[][] assignees = {null};
        assertThatThrownBy(() -> optimizerService.getSolution(dto))
        .isInstanceOf(NoSolutionNotFoundException.class);
    }
}

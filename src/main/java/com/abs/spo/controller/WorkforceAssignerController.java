package com.abs.spo.controller;

import com.abs.spo.exception.ApiError;
import com.abs.spo.exception.NoSolutionNotFoundException;
import com.abs.spo.model.WorkforceAssignee;
import com.abs.spo.controller.dto.WorkforceRequestDTO;
import com.abs.spo.service.WorkforceOptimizerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

/**
 * Is the main and only controller accepting JSON
 * Has  path /test GET for validating running server
 * Has  path /assign POST with json request body
 *
 * @author Ali Bassiri
 *
 */
@RestController
public class WorkforceAssignerController {
    private static final Logger logger = LoggerFactory.getLogger(WorkforceAssignerController.class);


    private WorkforceOptimizerService optimizerService;

    public WorkforceAssignerController(WorkforceOptimizerService optimizerService) {
        this.optimizerService = optimizerService;
    }

    @GetMapping("/test")
    public @ResponseBody  String isRunning(){
        System.out.println("OK!!!");
        return "OK!!!";
    }

    @PostMapping(path = "/assign", consumes = "application/json", produces = "application/json")
    public WorkforceAssignee[] workforceAssign(@RequestBody @Valid WorkforceRequestDTO dto ){

        try {
            return optimizerService.customGASolution( dto);
        } catch (NoSolutionNotFoundException e) {
            //e.printStackTrace();
            logger.info("State:" + this.getClass().getName() +",for the imput"+ dto.toString()+","+ e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "No solution could be generated.");
        }
    }

//    ExceptionHandlers
    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException exception){
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest()
                .body(new ApiError(HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value(),101,"Invalid input data."));

    }
    @ResponseBody
    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<ApiError> handleSolutionException(ResponseStatusException exception){
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest()
                .body(new ApiError(HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value(),102,"No solution could be generated."));

    }

}

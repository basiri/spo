package com.abs.spo.controller;

import com.abs.spo.controller.dto.WorkforceRequestDTO;
import com.abs.spo.exception.ApiError;
import com.abs.spo.exception.NoSolutionNotFoundException;
import com.abs.spo.model.WorkforceAssignee;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WorkforceAssignerControllerTest {


    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;


    @Test
    public void isRunning() throws URISyntaxException {
        final String baseUrl = "http://localhost:"+randomServerPort+"/test";
        URI uri = new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        //HttpEntity<Employee> request = new HttpEntity<>(employee, headers);

        ResponseEntity<String> result = this.restTemplate.getForEntity(uri, String.class);

        //Verify bad request and missing header
        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertTrue(result.getBody().contains("OK!!!"));
    }


    @Test
    public void workforceAssign() throws URISyntaxException, NoSolutionNotFoundException {
        final String baseUrl = "http://localhost:"+randomServerPort+"/assign";
        URI uri = new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        int[] rooms= {24,28};
        WorkforceRequestDTO dto= new WorkforceRequestDTO(rooms,11,6);
        HttpEntity<WorkforceRequestDTO> request = new HttpEntity<>(dto, headers);

        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request,String.class);

        //Verify bad request and missing header
        Assert.assertEquals(200, result.getStatusCodeValue());
        Gson gson = new Gson();
        WorkforceAssignee[] assignees = gson.fromJson(result.getBody(),WorkforceAssignee[].class);
        Assert.assertEquals(3, assignees[0].getSenior()+assignees[0].getJunior());
        Assert.assertEquals(3, assignees[1].getSenior()+assignees[1].getJunior());
    }
    @Test
    public void handleValidationExceptionRoomsNegative() throws URISyntaxException {
        final String baseUrl = "http://localhost:"+randomServerPort+"/assign";
        URI uri = new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        int[] rooms= {-24,28};
        WorkforceRequestDTO dto= new WorkforceRequestDTO(rooms,11,6);
        HttpEntity<WorkforceRequestDTO> request = new HttpEntity<>(dto, headers);

        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request,String.class);

        //Verify bad request and missing header
        Assert.assertEquals(400, result.getStatusCodeValue());
        Gson gson = new Gson();
        ApiError apiError = gson.fromJson(result.getBody(),ApiError.class);
        Assert.assertEquals(101, apiError.getInternalErrorCode());
    }

    @Test
    public void handleValidationExceptionRoomsNotInRange() throws URISyntaxException {
        final String baseUrl = "http://localhost:"+randomServerPort+"/assign";
        URI uri = new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        int[] rooms= {124,28};
        WorkforceRequestDTO dto= new WorkforceRequestDTO(rooms,11,6);
        HttpEntity<WorkforceRequestDTO> request = new HttpEntity<>(dto, headers);

        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request,String.class);

        //Verify bad request and missing header
        Assert.assertEquals(400, result.getStatusCodeValue());
        Gson gson = new Gson();
        ApiError apiError = gson.fromJson(result.getBody(),ApiError.class);
        Assert.assertEquals(101, apiError.getInternalErrorCode());
    }
    @Test
    public void handleValidationExceptionSenorNotInRange() throws URISyntaxException {
        final String baseUrl = "http://localhost:"+randomServerPort+"/assign";
        URI uri = new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        int[] rooms= {24,28};
        WorkforceRequestDTO dto= new WorkforceRequestDTO(rooms,0,166);
        HttpEntity<WorkforceRequestDTO> request = new HttpEntity<>(dto, headers);

        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request,String.class);

        //Verify bad request and missing header
        Assert.assertEquals(400, result.getStatusCodeValue());
        Gson gson = new Gson();
        ApiError apiError = gson.fromJson(result.getBody(),ApiError.class);
        Assert.assertEquals(101, apiError.getInternalErrorCode());
    }

    @Test
    public void handleSolutionException() throws URISyntaxException{
        final String baseUrl = "http://localhost:"+randomServerPort+"/assign";
        URI uri = new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        int[] rooms= {24,28};
        WorkforceRequestDTO dto= new WorkforceRequestDTO(rooms,1,166);
        HttpEntity<WorkforceRequestDTO> request = new HttpEntity<>(dto, headers);

        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request,String.class);

        //Verify bad request and missing header
        Assert.assertEquals(400, result.getStatusCodeValue());
        Gson gson = new Gson();
        ApiError apiError = gson.fromJson(result.getBody(),ApiError.class);
        Assert.assertEquals(102, apiError.getInternalErrorCode());
    }
}
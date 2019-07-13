package com.abs.spo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Could not find any solution .")
public class NoSolutionNotFoundException extends Exception
{
    public NoSolutionNotFoundException(String message)
    {
        super(message);
    }

}

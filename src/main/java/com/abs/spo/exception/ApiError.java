package com.abs.spo.exception;


import org.springframework.http.HttpStatus;
/**
 * ApiError is for converting internal error to appropriate Error for
 * clients
 *
 *
 *
 */
public class ApiError {
    private HttpStatus httpStatus;
    private int httpStatusCode;
    /**
     * The meaningful error code based on agreement with Api Client
     */
    private int internalErrorCode;
    private String message;

    public ApiError(HttpStatus httpStatus,int httpStatusCode, int internalErrorcode, String message) {
        this.httpStatus = httpStatus;
        this.httpStatusCode= httpStatusCode;
        this.internalErrorCode = internalErrorcode;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public int getInternalErrorCode() {
        return internalErrorCode;
    }

    public String getMessage() {
        return message;
    }
}

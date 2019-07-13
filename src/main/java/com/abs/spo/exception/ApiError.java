package com.abs.spo.exception;


import org.springframework.http.HttpStatus;
/**
 * ApiError is for converting internal error to appropriate Error for
 * clients
 *
 *
 * @author Ali Bassiri
 *
 */
public class ApiError {
    HttpStatus httpStatus;
    int httpStatusCode;
    /**
     * The meaningful error code based on agreement with Api Client
     */
    int internalErrorCode;
    String message;

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

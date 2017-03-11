package com.jpmc.technicalStudy.trade.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vikas on 3/8/17.
 */
public class TradeException extends RuntimeException {

    private Integer statusCode;
    private String errorCode;
    private Map<String, Object> properties = new HashMap<String, Object>();

    public TradeException(String message, Integer statusCode, String errorCode, Map<String, Object> properties) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.properties = properties;
    }

    public TradeException(String message) {
        super(message);
    }

    public TradeException(String message, Throwable cause, Integer statusCode, String errorCode, Map<String, Object> properties) {
        super(message, cause);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.properties = properties;
    }

    public TradeException(Throwable cause, Integer statusCode, String errorCode, Map<String, Object> properties) {
        super(cause);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.properties = properties;
    }


    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}

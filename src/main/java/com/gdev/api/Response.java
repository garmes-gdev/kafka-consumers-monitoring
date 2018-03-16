package com.gdev.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {

    @JsonProperty
    int error;
    @JsonProperty
    String message = "";

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package com.dvir.rsa.model.request;

import javax.validation.constraints.NotNull;

/**
 * Sign request params wrapper
 */
public class SignRequest {
    @NotNull
    private String data;

    public SignRequest() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

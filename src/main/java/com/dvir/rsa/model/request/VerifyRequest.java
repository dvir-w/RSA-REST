package com.dvir.rsa.model.request;

import javax.validation.constraints.NotNull;

/**
 * Verify request params wrapper
 */
public class VerifyRequest {
    @NotNull
    private String data;
    @NotNull
    private String signature;

    public VerifyRequest() {
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}

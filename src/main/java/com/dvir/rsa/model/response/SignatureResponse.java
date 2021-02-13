package com.dvir.rsa.model.response;

/**
 * Signature response wrapper
 */
public class SignatureResponse extends RSABaseResponse{
    String signature;

    public SignatureResponse(String signature) {
        super(signature != null);
        this.signature = signature;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}

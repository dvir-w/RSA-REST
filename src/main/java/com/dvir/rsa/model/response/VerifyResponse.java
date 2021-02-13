package com.dvir.rsa.model.response;

/**
 * Verify response wrapper
 */
public class VerifyResponse extends RSABaseResponse {
    Boolean isVerified;

    public VerifyResponse(Boolean isVerified) {
        super(isVerified != null);
        this.isVerified = isVerified;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }
}

package com.dvir.rsa.model.response;

/**
 * Key ID response wrapper
 */
public class KeyResponse {
    Long keyId;

    public KeyResponse(Long keyId) {
        this.keyId = keyId;
    }

    public Long getKeyId() {
        return keyId;
    }

    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }
}

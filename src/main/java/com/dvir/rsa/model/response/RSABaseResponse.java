package com.dvir.rsa.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * basic response wrapper
 */
public abstract class RSABaseResponse {

    Boolean isFound = true;

    public RSABaseResponse(Boolean isFound) {
        this.isFound = isFound;
    }

    @JsonIgnore
    public Boolean getFound() {
        return isFound;
    }

    public void setFound(Boolean found) {
        isFound = found;
    }
}

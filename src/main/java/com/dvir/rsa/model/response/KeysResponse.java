package com.dvir.rsa.model.response;

import java.util.Set;

/**
 * List of keys response wrapper
 */
public class KeysResponse {
    Set<Long> keys;

    public KeysResponse(Set<Long> keys) {
        this.keys = keys;
    }

    public Set<Long> getKeys() {
        return keys;
    }

    public void setKeys(Set<Long> keys) {
        this.keys = keys;
    }
}

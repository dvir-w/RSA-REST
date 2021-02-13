package com.dvir.rsa.controller;

import com.dvir.rsa.component.RSAService;
import com.dvir.rsa.model.request.SignRequest;
import com.dvir.rsa.model.request.VerifyRequest;
import com.dvir.rsa.model.response.KeyResponse;
import com.dvir.rsa.model.response.KeysResponse;
import com.dvir.rsa.model.response.SignatureResponse;
import com.dvir.rsa.model.response.VerifyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;


/**
 * RSA rest controller
 */
@RestController
public class RSAController {
    private final static Logger logger = LoggerFactory.getLogger(RSAController.class);

    public static final String KEY_NOT_FOUND = "Key not found";

    private RSAService rsaService;

    public RSAController(RSAService rsaService) {
        this.rsaService = rsaService;
    }

    /**
     * app health check
     *
     * @return
     */
    @GetMapping("/greeting")
    public String greeting() {
        logger.info("greeting");
        return "Welcome to RSA by Rest example";
    }

    /**
     * Generate new RSA key pair
     *
     * @return result key id wrapper
     */
    @PostMapping("/keys")
    @ResponseStatus(HttpStatus.CREATED)
    public KeyResponse generateKey() throws NoSuchAlgorithmException {
        logger.info("generateKey ");
        return rsaService.generateKey();
    }

    /**
     * Delete an existing RSA key pair
     *
     * @param keyId key ID
     */
    @DeleteMapping("/keys/{keyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteKey(@PathVariable Long keyId) {
        logger.info("deleteKey " + keyId);
        Boolean removed = rsaService.deleteKey(keyId);
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, KEY_NOT_FOUND);
        }
    }

    /**
     * Sign data using the given key
     */
    @PostMapping("/keys/{keyId}/sign")
    public SignatureResponse signKey(@PathVariable Long keyId, @Valid @RequestBody SignRequest signRequest) throws Exception {
        logger.info("sign " + keyId);

        SignatureResponse signatureResponse = rsaService.signKey(keyId, signRequest);
        if (!signatureResponse.getFound()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, KEY_NOT_FOUND);
        }
        return signatureResponse;
    }

    /**
     * Verify signature of data by key id
     */
    @PostMapping("/keys/{keyId}/verify")
    public VerifyResponse verifyKey(@PathVariable Long keyId, @Valid @RequestBody VerifyRequest verifyRequest) throws Exception {
        logger.info("verify " + keyId);

        VerifyResponse verifyResponse = rsaService.verifyKey(keyId, verifyRequest);
        if (!verifyResponse.getFound()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, KEY_NOT_FOUND);
        }
        return verifyResponse;

    }

    /**
     * List all existing keys IDs
     */
    @GetMapping("/keys")
    public KeysResponse getKeys() {
        logger.info("getKeys ");
        return rsaService.getKeys();
    }

}

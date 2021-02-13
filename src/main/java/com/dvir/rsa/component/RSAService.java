package com.dvir.rsa.component;

import com.dvir.rsa.model.request.SignRequest;
import com.dvir.rsa.model.request.VerifyRequest;
import com.dvir.rsa.model.response.KeyResponse;
import com.dvir.rsa.model.response.KeysResponse;
import com.dvir.rsa.model.response.SignatureResponse;
import com.dvir.rsa.model.response.VerifyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.*;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * manages RSA operations
 */
@Service
public class RSAService {

    private final static Logger logger = LoggerFactory.getLogger(RSAService.class);


    public final static long ID_INIT_VALUE = 1000l;
    private static AtomicLong idCounter;


    private final int KEY_SIZE = 2048;
    private final String RSA = "RSA";
    private final String UTF8 = "UTF8";
    private final String SHA256_WITH_RSA = "SHA256withRSA";


    private ConcurrentHashMap<Long, KeyPair> keyIdToKeyPair;

    @PostConstruct
    public void init() throws NoSuchAlgorithmException {
        logger.info("RSAService#init");
        keyIdToKeyPair = new ConcurrentHashMap<>();
        idCounter = new AtomicLong(ID_INIT_VALUE);
    }

    /**
     * Generate new RSA key pair
     *
     * @return result key id wrapper
     */
    public KeyResponse generateKey() throws NoSuchAlgorithmException {
        logger.info("RSAService#generateKey");
        KeyPair keyPair = generateKeyPair();
        Long keyId = createID();
        KeyResponse keyResponse = createKeyResponse(keyId);
        keyIdToKeyPair.put(keyId, keyPair);
        return keyResponse;
    }

    /**
     * delete key pair of key id
     *
     * @param keyId key id
     * @return true when key found and deleted
     */
    public boolean deleteKey(Long keyId) {
        logger.info("RSAService#deleteKey " + keyId);
        KeyPair removedKeyPair = keyIdToKeyPair.remove(keyId);
        return removedKeyPair != null;
    }

    /**
     * creates key response
     *
     * @param keyId key ID
     * @return KeyResponse
     */
    private KeyResponse createKeyResponse(Long keyId) {
        return new KeyResponse(keyId);
    }

    /**
     * generate new key ID
     *
     * @return new key ID
     */
    public static Long createID() {
        return idCounter.getAndIncrement();
    }

    /**
     * sign data by key id
     *
     * @param keyId       key id
     * @param signRequest data wrapper
     * @return sign result wrapper
     * @throws Exception
     */
    public SignatureResponse signKey(Long keyId, SignRequest signRequest) throws Exception {
        String data = signRequest.getData();
        KeyPair keyPair = keyIdToKeyPair.get(keyId);
        String signature = null;
        if (keyPair != null) {
            signature = sign(data, keyPair.getPrivate());
        }
        return new SignatureResponse(signature);
    }

    /**
     * Verify signature of data by key id
     *
     * @param keyId         key id
     * @param verifyRequest signature and data wrapper
     * @return verification result wrapper
     * @throws Exception
     */
    public VerifyResponse verifyKey(Long keyId, VerifyRequest verifyRequest) throws Exception {
        String data = verifyRequest.getData();
        KeyPair keyPair = keyIdToKeyPair.get(keyId);
        String signature = verifyRequest.getSignature();
        Boolean isVerified = null;
        if (keyPair != null) {
            isVerified = verify(data, signature, keyPair.getPublic());
        }
        return new VerifyResponse(isVerified);
    }

    /**
     * verify data is signed correctly by key id
     *
     * @param plainText data
     * @param signature signed data
     * @param publicKey RSA public key
     * @return true when data is signed by key correctly
     * @throws Exception
     */
    public boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance(SHA256_WITH_RSA);
        publicSignature.initVerify(publicKey);
        publicSignature.update(plainText.getBytes(UTF8));

        byte[] signatureBytes = Base64.getDecoder().decode(signature);

        return publicSignature.verify(signatureBytes);
    }

    /**
     * generate new RSA signature for data
     *
     * @param plainText  data
     * @param privateKey RSA private key
     * @return generated RSA signature encoded in base 64
     * @throws Exception
     */
    public String sign(String plainText, PrivateKey privateKey) throws Exception {
        Signature privateSignature = Signature.getInstance(SHA256_WITH_RSA);
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes(UTF8));

        byte[] signature = privateSignature.sign();

        return Base64.getEncoder().encodeToString(signature);
    }

    /**
     * generate RSA key pair
     *
     * @return new KeyPair
     * @throws NoSuchAlgorithmException
     */
    public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA);
        generator.initialize(KEY_SIZE, new SecureRandom());
        return generator.generateKeyPair();
    }

    /**
     * List all existing keys IDs
     *
     * @return wrapper for all existing keys IDs
     */
    public KeysResponse getKeys() {
        Set<Long> keys = keyIdToKeyPair == null ? new HashSet<>(0) : keyIdToKeyPair.keySet();
        return new KeysResponse(keys);
    }
}

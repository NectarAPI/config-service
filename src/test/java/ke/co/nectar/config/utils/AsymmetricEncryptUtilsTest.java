package ke.co.nectar.config.utils;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.security.PublicKey;

public class AsymmetricEncryptUtilsTest {

    private final String PUBLIC_KEY = // "-----BEGIN PUBLIC KEY-----\n" +
            "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAtwof5P+1S8MQP00fosZ9vd2iPLsU5y5bbiN8SmBqOi/1iZvfNmJ095cL4Bx1DpHnQR1XrWrndYofLWvNeC2QtUCMASCuXE0XtQhjJmGc4jOt5228FjR7pP/I6WrJgiYBy+hrDm+8ZLqwHwFNI7F+7awY27fPgzfOd0jgl2HB/CAPYHNPTmzQmLTfE7uuw4FdkC+YuPVm8vPOuTFzOLNTzTCjND0UGnHESMK1BcI6/rY/lDvUyoZnCLefj5pgMu4Almam/pxTfUZQim+keBMHr7vr80TtZ+5EGpB4fzIBCqlNmq2DPWFWf3aoK+9JCDeaGZHx0f9WS/Y7HSy94A2N5FHongQ5Mr05Cji9H44O3wyFxZRRwbokNYNkGlXEZuRcch2spGoOEh63n2r9O/fEgPjd7x5VuEVDBRzsIiyn8hqEprTcn7CR/4p54L7RzpZEUVqpJJxLRZ0MOI4xmPOnZNCjMFErdzpD9NISQDwmUM3gVE6/NQsRcBCHpiLDzyukPT/YWlE6NF5ldK8PX6uwfXdp4iTjh0rowPntD9qVoS1aUL4itw7liJc+4FpIXr+bWHIOfNWqqRwip9JRbFQf5kqExyPZdwFGOredSGYm3ehOIIJpPsKlD17IQMfJM8DDuR/t2dgfDNQIeWkCOUsPBTREMpOyHnN1HUWDe4LfGfcCAwEAAQ==" ;
            // "-----END PUBLIC KEY-----";

    private final String PRIVATE_KEY = // "-----BEGIN PRIVATE KEY-----\n" +
            "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" ;
            // "-----END PRIVATE KEY-----";

    @Test
    public void testEncryptionUsingPublicKey() throws Exception {
        final String ENCRYPT_STRING = "String to encrypt";
        PublicKey publicKey = AsymmetricEncryptUtils.getPublicKeyFromString(PUBLIC_KEY);
        PrivateKey privateKey = AsymmetricEncryptUtils.getPrivateKeyFromString(PRIVATE_KEY);
        String encryptedString = AsymmetricEncryptUtils.encrypt(ENCRYPT_STRING, publicKey);
        Assert.assertEquals(ENCRYPT_STRING, AsymmetricEncryptUtils.decrypt(encryptedString, privateKey));
    }

    @Test
    public void testEncryptionUsingPrivateKey() throws Exception {
        final String ENCRYPT_STRING = "String to encrypt";
        PrivateKey privateKey = AsymmetricEncryptUtils.getPrivateKeyFromString(PRIVATE_KEY);
        PublicKey publicKey = AsymmetricEncryptUtils.getPublicKeyFromString(PUBLIC_KEY);
        String encryptedString = AsymmetricEncryptUtils.encrypt(ENCRYPT_STRING, privateKey);
        Assert.assertEquals(ENCRYPT_STRING, AsymmetricEncryptUtils.decrypt(encryptedString, publicKey));
    }

    @Test
    public void testCanEncryptWithoutBase64Encoding() throws Exception {
        final String ENCRYPT_STRING = "v8y/B?E(H+MbQeTh";
        PublicKey publicKey = AsymmetricEncryptUtils.getPublicKeyFromString(PUBLIC_KEY);
        PrivateKey privateKey = AsymmetricEncryptUtils.getPrivateKeyFromString(PRIVATE_KEY);
        byte[] encrypted = AsymmetricEncryptUtils.encrypt(ENCRYPT_STRING.getBytes(), publicKey);
        byte[] decrypted = AsymmetricEncryptUtils.decrypt(encrypted, privateKey);
        Assert.assertEquals(ENCRYPT_STRING, new String(decrypted, Charset.defaultCharset()));
    }
}

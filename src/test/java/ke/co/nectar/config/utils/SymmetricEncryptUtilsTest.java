package ke.co.nectar.config.utils;

import org.junit.Assert;
import org.junit.Test;

public class SymmetricEncryptUtilsTest {

    private final String AES_KEY = "RfUjXn2r4u7x!A%D";

    @Test
    public void testThatCanEncryptAndDecrypt() throws Exception {
        final String DATA = "This is the test string";

        String encrypted = SymmetricEncryptUtils.encrypt(DATA, AES_KEY);
        Assert.assertEquals(DATA, SymmetricEncryptUtils.decrypt(encrypted, AES_KEY));
    }
}

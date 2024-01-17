package ke.co.nectar.config.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Arrays;

public class EncryptExampleWorkflow {

    /**
     *
     * ENCRYPTION
     * ==========
     *
     * message -> symmetric key -> message output -> base64 message output
     *
     * digest -> user private key -> digest output -> base64 digest output
     *
     * symmetric key -> nectar public key -> key output -> base64 key output
     *
     * DECRYPTION
     * ==========
     *
     * base64 key output -> key output -> nectar private key -> symmetric key
     *
     * base64 message output -> message output -> symmetric key -> message
     *
     * message -> digest
     *
     * base64 digest output -> decrypt user public key -> digest output
     *
     * compare with digest output and digest
     *
     */
    public static void main(String[] args) {
        try {
            // 4096 bit RSA keys
            final String NECTAR_PUBLIC_KEY = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAvgJOsQeQ63PT/hu50eKqz7gRTe6X9re+Hy5vQdAbna4cbuzmxQ5scBYBW/tDXVoS40MRQYdqdoxJValoAT76U/xgV0yTlAYihU13Mvh61ZUyOU/QBD+w3LI/1mHQhMW6sQJ3TzSEKfH+6xzTsRktTywXQqHAMgtvhq+XO+b+7jBi4NRxZWgYhSMuGetdLh9rG/GUcyCOO8Oqix6paX5wMKsGUXVbA5VTsmycTsaHgI7NQKNnY46BmWcZB1XMnx+9PY7FgDJ7bsDIsfKtQj9sEIpnuD2leU5ZNJM/VBkJ3JfsBZ/UV3Q0kWbcPyoXhl0a4R2ywyA94BN6FSYrqq1NtdsxUATSHYu0dc/kwyTKAWVaatcLzmLRKF/Xacle0Kwkn1rGakwu1wB0YTIjUMsFGOlbffnAUOB5GmvYDWmliax2p5sG6Ar7NUABISZEwiw1SwHXhGdFxVHlcOna5LBZ9xvMfJM4L7vESKmvy5ChL/IoszIkOgPE5XUOox32DOsE+SU4Gjf5kwCFWRNPokB/bIssty3/4y/7FnNgGJsyBwSgq+/rAwHBFjwawVHbqWzhMiuEulEvukk2CjcbdgYW5az/Ikc/BBsxUlKJkWa0m7wlmMxbXngPpOmFg4qfDtqMflVfPw48Rfx2XOttXjFvFpkxrYVOzg9nP0llktJNpe8CAwEAAQ==";
            final String NECTAR_PRIVATE_KEY = "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";

            // 4096 bit RSA Keys
            final String MOCK_USER_PRIVATE_KEY = "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";
            final String MOCK_USER_PUBLIC_KEY = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAkSdBJVJ6JTTfs2PSisrmqkiwDiiTyeRvQcRoOW8pd4LqgcGbXnUrlRfunB2s1FUweshYO9h7OFWLaqDJxrEGoTioN1MveNj4ZWvQGd2gfotCZUiFXTAhe3mUffMMwq0GeOWFgauW8FswW0+PS07gb7Tb5S7pDdLl8GI9Ph+hFv+ZRuPNtdHeZ6dr7UIbgXJuvcZtL3Suvz9Qqe0QJ2kRNXV4Ougq6Bvm8XiE5bcms65eV1m0DU3Mt/mddprswAPDDLD/4ZoPdKiPqoqrZ88VYgtY+yIt7hk3YWY7xuGomeIH9vEWgkBGweo34LuOUN8bz8Hz/xfWGEjTbCrE9u5IgeEdyHJytL0wjgQ9EOXjilr44BNr3hLwoSWpwlkc8X6p0RbGHCgmZjXX5MZjouFwhZED2l7QTDG+uQ3w4pvw3bq9NUOOyQULu8hlvLfrTLrXt5zasyfeQHmKNp9+f2mNMeBLwJdyusr8dAUIIlOa9nrxL1RQ0sANu6WxWS3tjU5xm2DhKCDSeOSoFdbdwG4N86Ozozq5X7PedZWQbCV6kZCIR9zoim5VXAUeqhPxMj8N7TLhnDgMLhuSF+8Tq90mSDuMjbyL0ZAADzPfQnlIy6N0baQ/a19mgDZRt374iZEFhm054Xb7crgiDldKAlwsYu9rPEtERi6a7QjW9OTQFn8CAwEAAQ==";

            // 128 bit AES Key
            final String MOCK_USER_SYMMETRIC_KEY  = "v8y/B?E(H+MbQeTh";

            // -- Config encryption

            final String YAML = "---\n" +
                    "name: example_config\n" +
                    "key_expiry_no: 255\n" +
                    "encryption_algorithm: sta\n" +
                    "token_carrier_type: numeric\n" +
                    "decoder_key_generation_algorithm: 04\n" +
                    "tariff_index: 01\n" +
                    "key_revision_no: 1\n" +
                    "vending_key: 0123456789abcdef\n" +
                    "supply_group_code: 123456\n" +
                    "key_type: 3\n" +
                    "base_date: 2035\n" +
                    "issuer_identification_no: 600727";

            // 1. Encrypt data using symmetric key
            byte[] cipherTextAfterMockUserSymmetricKeyEncryption = SymmetricEncryptUtils.encrypt(YAML.getBytes(), MOCK_USER_SYMMETRIC_KEY);

            // 2. Base64 encode symmetric key text
            String cipherTextAfterMockUserSymmetricKeyEncryptionBase64 = AsymmetricEncryptUtils.encodeBASE64(cipherTextAfterMockUserSymmetricKeyEncryption);

            // 3. Generate message digest of the data
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(YAML.getBytes());
            byte[] digest = md.digest();

            // 4. Encrypt the digest with the user private key
            byte[] digestAfterMockUserPrivateKeyEncryption = AsymmetricEncryptUtils.encrypt(digest, AsymmetricEncryptUtils.getPrivateKeyFromString(MOCK_USER_PRIVATE_KEY));

            // 5. Base64 encode encrypted digest
            String digestAfterMockUserPrivateKeyEncryptionBase64 = AsymmetricEncryptUtils.encodeBASE64(digestAfterMockUserPrivateKeyEncryption);

            // 6. Encrypt symmetric key with nectar public key
            byte[] symmetricKeyAfterNectarPublicKeyEncryption = AsymmetricEncryptUtils.encrypt(MOCK_USER_SYMMETRIC_KEY.getBytes(), AsymmetricEncryptUtils.getPublicKeyFromString(NECTAR_PUBLIC_KEY));

            // 7. Base64 symmetric key output
            String symmetricKeyAfterNectarPublicKeyEncryptionBase64 = AsymmetricEncryptUtils.encodeBASE64(symmetricKeyAfterNectarPublicKeyEncryption);

            System.out.println(String.format("\nYAML file: \n%s", YAML));
            System.out.println(String.format("\nEncrypted Config Data: %s", cipherTextAfterMockUserSymmetricKeyEncryptionBase64));
            System.out.println(String.format("Encrypted Digest: %s", digestAfterMockUserPrivateKeyEncryptionBase64));
            System.out.println(String.format("Encrypted Symmetric Key: %s", symmetricKeyAfterNectarPublicKeyEncryptionBase64));

            // -- Config decryption

            // 1. Base64 decode symmetric key
            byte[] symmetricKeyAfterNectarPublicKeyEncryptionBase64Decoded = AsymmetricEncryptUtils.decodeBASE64(symmetricKeyAfterNectarPublicKeyEncryptionBase64);

            // 2. Decrypt base64 symmetric key using nectar private key
            byte[] symmetricKeyAfterNectarPrivateKeyDecryption = AsymmetricEncryptUtils.decrypt(symmetricKeyAfterNectarPublicKeyEncryptionBase64Decoded, AsymmetricEncryptUtils.getPrivateKeyFromString(NECTAR_PRIVATE_KEY));

            // 3. Base64 decode message
            byte[] messageBase64Decoded = AsymmetricEncryptUtils.decodeBASE64(cipherTextAfterMockUserSymmetricKeyEncryptionBase64);

            // 4. Decrypt message using symmetric key using symmetric key
            byte[] decryptedData = SymmetricEncryptUtils.decrypt(messageBase64Decoded, symmetricKeyAfterNectarPrivateKeyDecryption);

            // 5. Run message digest
            MessageDigest mdDec = MessageDigest.getInstance("SHA-256");
            mdDec.update(decryptedData);
            byte[] digestEnc = mdDec.digest();

            // 6. Base64 decode message digest
            byte[] encryptedMessageDigest = AsymmetricEncryptUtils.decodeBASE64(digestAfterMockUserPrivateKeyEncryptionBase64);

            // 7. Decrypt message digest using user public key
            byte[] decryptedMessageDigestAfterMockUserPublicKey = AsymmetricEncryptUtils.decrypt(encryptedMessageDigest, AsymmetricEncryptUtils.getPublicKeyFromString(MOCK_USER_PUBLIC_KEY));

            // 8. Compare generated digest and captured digest
            System.out.println(String.format("\ndigestEnc == decryptedMessageDigestAfterMockUserPublicKey: %b", Arrays.equals(digestEnc, decryptedMessageDigestAfterMockUserPublicKey)));

            System.out.println(String.format("\nDecrypted YAML file: \n%s", new String(decryptedData, Charset.forName("UTF-8"))));

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}

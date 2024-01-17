package ke.co.nectar.config.utils;

public class EncryptedYAMLConfig {

    private String data;
    private String digest;
    private String key;

    public EncryptedYAMLConfig() {}

    public EncryptedYAMLConfig(String data, String digest, String key) {
        setData(data);
        setDigest(digest);
        setKey(key);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data.trim();
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest.trim();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key.trim();
    }
}

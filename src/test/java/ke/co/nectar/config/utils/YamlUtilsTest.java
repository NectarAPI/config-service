package ke.co.nectar.config.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class YamlUtilsTest {

    @Test
    public void testThatYamlFileIsLoadedCorrectly() throws Exception {

        final String YAML_EXAMPLE_STRING = "---\n" +
                "key_expiry_no: 255\n" +
                "encryption_algorithm: sta\n" +
                "token_carrier_type: numeric";

        final String[] KEYS = {"key_expiry_no", "encryption_algorithm", "token_carrier_type"};
        Map<String, String> yaml = YamlUtils.load(YAML_EXAMPLE_STRING);

        Assert.assertArrayEquals(KEYS, yaml.keySet().toArray());
        Assert.assertEquals("255", yaml.get("key_expiry_no"));
        Assert.assertEquals("sta", yaml.get("encryption_algorithm"));
        Assert.assertEquals("numeric", yaml.get("token_carrier_type"));
    }
}

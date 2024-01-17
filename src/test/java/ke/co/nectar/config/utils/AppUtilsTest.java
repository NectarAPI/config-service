package ke.co.nectar.config.utils;

import org.junit.Assert;
import org.junit.Test;

public class AppUtilsTest {

    @Test
    public void testThatAppUtilsGeneratesRef() {
        Assert.assertNotNull(AppUtils.generateRef());
    }
}

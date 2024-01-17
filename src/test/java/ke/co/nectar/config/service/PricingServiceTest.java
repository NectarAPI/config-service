package ke.co.nectar.config.service;

import ke.co.nectar.config.NectarConfigServiceApplication;
import ke.co.nectar.config.entity.Pricing;
import ke.co.nectar.config.service.pricing.PricingService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NectarConfigServiceApplication.class)
@FixMethodOrder(MethodSorters.JVM)
@Transactional
public class PricingServiceTest {

    @Autowired
    private PricingService pricingService;

    private final long EPOCH_TIME = 1606754076302l;

    @Before
    public void setup() throws Exception {
        Pricing pricing = new Pricing(10.0, Instant.ofEpochMilli(EPOCH_TIME), Instant.ofEpochMilli(EPOCH_TIME));
        pricingService.add(pricing);
    }

    @Test
    public void testThatCorrectPricingIsReturned() throws Exception {
        List<Pricing> pricingList = pricingService.findAll();

        Assert.assertEquals(1, pricingList.size());
        Assert.assertEquals((Double) 10.0, pricingList.get(0).getCredits());
        Assert.assertEquals(EPOCH_TIME, pricingList.get(0).getCreatedAt().toEpochMilli());
        Assert.assertEquals(EPOCH_TIME, pricingList.get(0).getUpdatedAt().toEpochMilli());
    }
}

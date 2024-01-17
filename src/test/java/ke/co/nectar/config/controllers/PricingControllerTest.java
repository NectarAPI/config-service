package ke.co.nectar.config.controllers;

import ke.co.nectar.config.NectarConfigServiceApplication;
import ke.co.nectar.config.controllers.pricing.PricingController;
import ke.co.nectar.config.entity.Pricing;
import ke.co.nectar.config.service.pricing.PricingService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NectarConfigServiceApplication.class)
@AutoConfigureMockMvc
public class PricingControllerTest {

    @InjectMocks
    private PricingController pricingController;

    @MockBean
    private PricingService pricingService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contextLoads() {
        Assert.assertNotNull(pricingController);
    }

    @Test
    public void testThatPricingIsReturned() throws Exception {
        final long EPOCH_TIME = 1606754076302l;
        Pricing pricing = new Pricing(10.0, Instant.ofEpochMilli(EPOCH_TIME), Instant.ofEpochMilli(EPOCH_TIME));

        List<Pricing> pricingList = new ArrayList<>();
        pricingList.add(pricing);

        when(pricingService.findAll()).thenReturn(pricingList);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/pricing")
                .param("request_id", "requestid")
                .with(httpBasic("config_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Obtained pricing','request_id':'requestid'},'data':{'pricing':{'credits':10.0,'created_at':'2020-11-30T16:34:36.302Z','updated_at':'2020-11-30T16:34:36.302Z'}}}"));
    }
}

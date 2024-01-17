package ke.co.nectar.config.controllers;

import ke.co.nectar.config.NectarConfigServiceApplication;
import ke.co.nectar.config.annotation.NotificationProcessor;
import ke.co.nectar.config.entity.Config;
import ke.co.nectar.config.entity.NativeConfig;
import ke.co.nectar.config.entity.STSConfig;
import ke.co.nectar.config.repository.PublicKeyRepository;
import ke.co.nectar.config.service.config.ConfigService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NectarConfigServiceApplication.class)
@AutoConfigureMockMvc
public class ConfigControllerTest {

    @InjectMocks
    private ConfigController configController;

    @MockBean
    private ConfigService configService;

    @MockBean
    private PublicKeyRepository publicKeyRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationProcessor notificationProcessor;

    @Test
    public void contextLoads() {
        Assert.assertNotNull(configController);
    }

    @Test
    public void testThatNativeConfigIsReturned() throws Exception {
        final long EPOCH_TIME = 1606754076302l;
        Config config = new Config("Config", "ref", "user-ref", Config.ConfigType.NATIVE,
                        true, Instant.ofEpochMilli(EPOCH_TIME));
        NativeConfig nativeConfig = new NativeConfig( config, "255", "sta", "numeric", "02",
                            "01", "1", "0abc12def3456789", "000046", "0", "1993", "600727");

        when(configService.find(any(), anyBoolean())).thenReturn(nativeConfig);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/config?ref=ref")
                .param("request_id", "requestid")
                .with(httpBasic("config_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Obtained config','request_id':'requestid'},'data':{'config':{'config':{'name':'Config','configType':'NATIVE','ref':'ref','user_ref':'user-ref','activated':true,'created_at':'2020-11-30T16:34:36.302Z'},'key_expiry_no':'255','encryption_algorithm':'sta','token_carrier_type':'numeric','decoder_key_generation_algorithm':'02','tariff_index':'01','key_revision_no':'1','vending_key':'0abc12def3456789','supply_group_code':'000046','key_type':'0','base_date':'1993','issuer_identification_no':'600727'}}}"));
    }

    @Test
    public void testThatConfigForRefAndUserRefIsReturned() throws Exception {
        final long EPOCH_TIME = 1606754076302l;
        Config config = new Config("Config", "ref", "user-ref", Config.ConfigType.NATIVE,
                true, Instant.ofEpochMilli(EPOCH_TIME));
        NativeConfig nativeConfig = new NativeConfig( config, "255", "sta", "numeric", "02",
                "01", "1", "0abc12def3456789", "000046", "0", "1993", "600727");

        when(configService.find(any(), anyBoolean())).thenReturn(nativeConfig);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/config?user_ref=user-ref")
                .param("request_id", "requestid")
                .param("ref","ref")
                .with(httpBasic("config_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Obtained config','request_id':'requestid'},'data':{'data':{'name':'Config','activated':true,'ref':null,'key_expiry_no':'255','encryption_algorithm':'sta','token_carrier_type':'numeric','decoder_key_generation_algorithm':'02','tariff_index':'01','key_revision_no':'1','vending_key':'0abc12def3456789','supply_group_code':'000046','key_type':'0','base_date':'1993','issuer_identification_no':'600727','user_ref':'user-ref','created_at':'2020-11-30T16:34:36.302Z'}}}"));
    }

    @Test
    public void testThatConfigForUserRefIsReturned() throws Exception {
        final long EPOCH_TIME = 1606754076302l;
        Config config = new Config("Config", "ref", "user-ref", Config.ConfigType.NATIVE,
                true, Instant.ofEpochMilli(EPOCH_TIME));
        NativeConfig nativeConfig = new NativeConfig( config, "255", "sta", "numeric", "02",
                "01", "1", "0abc12def3456789", "000046", "0", "1993", "600727");

        when(configService.find(any(), anyBoolean())).thenReturn(nativeConfig);

        ArrayList<STSConfig> configs = new ArrayList<>();
        configs.add(nativeConfig);

        when(configService.findByUserRef(anyString(),anyBoolean())).thenReturn(configs);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/config?user_ref=user-ref")
                .param("request_id", "requestid")
                .with(httpBasic("config_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Obtained configs','request_id':'requestid'},'data':{'data':[{'config':{'name':'Config','configType':'NATIVE','ref':'ref','user_ref':'user-ref','activated':true,'created_at':'2020-11-30T16:34:36.302Z'},'key_expiry_no':'255','encryption_algorithm':'sta','token_carrier_type':'numeric','decoder_key_generation_algorithm':'02','tariff_index':'01','key_revision_no':'1','vending_key':'0abc12def3456789','supply_group_code':'000046','key_type':'0','base_date':'1993','issuer_identification_no':'600727'}]}}"));
    }

    @Test
    public void testThatConfigIsCreated() throws Throwable {
        final long EPOCH_TIME = 1606754076302L;

        String configStr = "{\"data\":\"data\",\"digest\":\"digest\",\"key\":\"key\"}";

        Config config = new Config("Config", "ref", "user-ref", Config.ConfigType.NATIVE,
                true, Instant.ofEpochMilli(EPOCH_TIME));
        NativeConfig nativeConfig = new NativeConfig( config, "255", "sta", "numeric", "02",
                "01", "1", "0abc12def3456789", "000046", "0", "1993", "600727");

        when(configService.extractAndAdd(anyString(), any())).thenReturn(nativeConfig);
        when(notificationProcessor.process(any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/config")
                .param("user_ref", "user-ref")
                .param("request_id", "requestid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(configStr)
                .with(httpBasic("config_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Successfully saved config','request_id':'requestid'},'data':{'config':{'config':{'name':'Config','configType':'NATIVE','ref':'ref','user_ref':'user-ref','activated':true,'created_at':'2020-11-30T16:34:36.302Z'},'key_expiry_no':'255','encryption_algorithm':'sta','token_carrier_type':'numeric','decoder_key_generation_algorithm':'02','tariff_index':'01','key_revision_no':'1','vending_key':'0abc12def3456789','supply_group_code':'000046','key_type':'0','base_date':'1993','issuer_identification_no':'600727'}}}"))
                .andReturn();
    }

    @Test
    public void testThatConfigIsActivated() throws Exception {

        when(configService.activateConfig(anyString(), anyString())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/config?config_ref=ref")
                .param("request_id", "requestid")
                .param("user_ref", "userref")
                .with(httpBasic("config_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Successfully activated configuration','request_id':'requestid'}}"));
    }

    @Test
    public void testThatConfigIsDeactivated() throws Exception {

        when(configService.deactivateConfig(anyString(), anyString())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/config?config_ref=ref")
                .param("request_id", "requestid")
                .param("user_ref", "userref")
                .with(httpBasic("config_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Successfully deactivated configuration','request_id':'requestid'}}"));
    }

}

package ke.co.nectar.config.controllers;

import ke.co.nectar.config.NectarConfigServiceApplication;
import ke.co.nectar.config.annotation.NotificationProcessor;
import ke.co.nectar.config.entity.PublicKey;
import ke.co.nectar.config.service.publickey.PublicKeyService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NectarConfigServiceApplication.class)
@AutoConfigureMockMvc
public class PublicKeyControllerTest {

    @InjectMocks
    private PublicKeyController publicKeyController;

    @MockBean
    private PublicKeyService publicKeyService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationProcessor notificationProcessor;

    @Test
    public void contextLoads() {
        Assert.assertNotNull(publicKeyController);
    }

    @Test
    public void testThatCanGetPublicKeysUsingUserRef() throws Exception {
        final long EPOCH_TIME = 1606754076302l;

        PublicKey publicKey = new PublicKey("Public  Key", "key", "user-ref", true,
                                            "key-ref", Instant.ofEpochMilli(EPOCH_TIME),
                                                Instant.ofEpochMilli(EPOCH_TIME));

        List<PublicKey> mockResults = new ArrayList<>();
        mockResults.add(publicKey);

        when(publicKeyService.findByUserRef(any())).thenReturn(mockResults);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/public-key?user_ref=user-ref")
                .param("request_id", "requestid")
                .with(httpBasic("config_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Found public keys for user ref user-ref','request_id':'requestid'},'data':{'data':[{'name':'key','key':'Public  Key','activated':true,'ref':'key-ref','user_ref':'user-ref','created_at':'2020-11-30T16:34:36.302Z','updated_at':'2020-11-30T16:34:36.302Z'}]}}"));
    }

    @Test
    public void testThatCanGetPublicKeysUsingUserRefActivatedFalse() throws Exception {

        when(publicKeyService.findByUserRefActivated(any())).thenReturn(new ArrayList<PublicKey>());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/public-key?user_ref=user-ref&activated=true")
                .param("request_id", "requestid")
                .with(httpBasic("config_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Found public keys for user ref user-ref','request_id':'requestid'},'data':{'data':[]}}"));
    }

    @Test
    public void testThatCanGetPublicKeyUsingRef() throws Exception {
        final long EPOCH_TIME = 1606754076302l;

        PublicKey publicKey = new PublicKey("Public  Key", "key", "user-ref", true,
                                        "key-ref", Instant.ofEpochMilli(EPOCH_TIME),
                                            Instant.ofEpochMilli(EPOCH_TIME));

        when(publicKeyService.findByRef(any())).thenReturn(publicKey);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/public-key?ref=key-ref")
                .param("request_id", "requestid")
                .with(httpBasic("config_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Found public key with ref key-ref','request_id':'requestid'},'data':{'data':{'name':'key','key':'Public  Key','activated':true,'ref':'key-ref','user_ref':'user-ref','created_at':'2020-11-30T16:34:36.302Z','updated_at':'2020-11-30T16:34:36.302Z'}}}"));
    }

    @Test
    public void testThatCanCreateNewPublicKey() throws Exception {

        final long EPOCH_TIME = 1606754076302l;
        final String NAME = "public key";
        final String USER_REF = "user-ref";
        final String PUBLIC_KEY = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAgifJ5Qzu9P1dqPjyH0T/LuBTC4Rmo2Yk9W/dKSgRhN0d0qlKpzjjcIAe3Rws3Ksg8atsmg5fencCQaIIj9cqoeAWpTFwvcpgXy7wc8Ecs6HmdIe2wESDwfe+c+5tOz+q9mpm81vANxF3g8yY3PNqD7Rj4XNGmGTyvil18sM5oBPqN1qE6m3TBnOWQmBHCND/JRKYZkK4FjXXhNUhXrV9llfs/mtXkaqW/239DxJy5C0brYXDUuccZYpdLCdHFZIHuOi3i1Q6JFmhMo2fZnNp1cY34UjnyS1ceWP2ej74YdIZYNj+zqLGmsD71V+nXMxFXaIPPsbCnWGvsCrUmrDdlRtUPPvXJsKA+6on6Pk2+R28V5w/fxhErukG0uMLlffxHwcF0MAi2V6gi5bgFe9SX9k4Ih6xsn2DKCeRK5gHS+UZPTsWIwiMkUhOuA2s4xowxS3d2FFdYSJnizKO75Q85vfPQxJZQ1HHI89pzIXtRQDDi/O+KVIyc6OPgrI2ZCG8zA3hf3xpx47WRBhvqeo9wqEocbTHg++puKlfRdBpbBMhnneM53cry9JRVH6IBPq/JL6SNl7Rn346buyi/QdAXEwIziNoYV9x92g5drQ75DeglUQKsxlzHYwTUESeLMNlVpJg3eWBGdFND0gFAejxv9h91tcum9wdPCEcDStXfEMCAwEAAQ==";

        String userJson = String.format("{\"name\": \"%s\",\"user_ref\": \"%s\",\"public_key\":\"%s\",\"activated\":true}", NAME, USER_REF, PUBLIC_KEY);

        PublicKey publicKey = new PublicKey();
        publicKey.setId(1l);
        publicKey.setName("Public Key");
        publicKey.setKey(PUBLIC_KEY);
        publicKey.setActivated(true);
        publicKey.setRef("c0e221d8-6ea3-475e-9bec-31259dc67677");
        publicKey.setUserRef(USER_REF);
        publicKey.setCreatedAt(Instant.ofEpochMilli(EPOCH_TIME));

        when(publicKeyService.add(any(), any(), any(), anyBoolean())).thenReturn(publicKey);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/public-key")
                .param("request_id", "requestid")
                .param("user_ref", "userref")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
                .with(httpBasic("config_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Public key saved successfully','request_id':'requestid'},'data':{'public_key':{'name':'Public Key','key':'MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAgifJ5Qzu9P1dqPjyH0T/LuBTC4Rmo2Yk9W/dKSgRhN0d0qlKpzjjcIAe3Rws3Ksg8atsmg5fencCQaIIj9cqoeAWpTFwvcpgXy7wc8Ecs6HmdIe2wESDwfe+c+5tOz+q9mpm81vANxF3g8yY3PNqD7Rj4XNGmGTyvil18sM5oBPqN1qE6m3TBnOWQmBHCND/JRKYZkK4FjXXhNUhXrV9llfs/mtXkaqW/239DxJy5C0brYXDUuccZYpdLCdHFZIHuOi3i1Q6JFmhMo2fZnNp1cY34UjnyS1ceWP2ej74YdIZYNj+zqLGmsD71V+nXMxFXaIPPsbCnWGvsCrUmrDdlRtUPPvXJsKA+6on6Pk2+R28V5w/fxhErukG0uMLlffxHwcF0MAi2V6gi5bgFe9SX9k4Ih6xsn2DKCeRK5gHS+UZPTsWIwiMkUhOuA2s4xowxS3d2FFdYSJnizKO75Q85vfPQxJZQ1HHI89pzIXtRQDDi/O+KVIyc6OPgrI2ZCG8zA3hf3xpx47WRBhvqeo9wqEocbTHg++puKlfRdBpbBMhnneM53cry9JRVH6IBPq/JL6SNl7Rn346buyi/QdAXEwIziNoYV9x92g5drQ75DeglUQKsxlzHYwTUESeLMNlVpJg3eWBGdFND0gFAejxv9h91tcum9wdPCEcDStXfEMCAwEAAQ==','activated':true,'ref':'c0e221d8-6ea3-475e-9bec-31259dc67677','user_ref':'user-ref','created_at':'2020-11-30T16:34:36.302Z','updated_at':null}}}"))
                .andReturn();
    }

    @Test
    public void testThatCanActivatePublicKey() throws Exception {
        when(publicKeyService.activatePublicKey(any(), anyString())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/public-key?ref=ref")
                .param("request_id", "requestid")
                .param("user_ref", "userref")
                .with(httpBasic("config_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Successfully activated public key','request_id':'requestid'}}"));
    }

    @Test
    public void testThatCanActivatePublicKeyWithUserRef() throws Throwable {
        when(publicKeyService.activateAllPublicKeysForUserRef(any())).thenReturn(true);
        when(notificationProcessor.process(any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/public-key?user_ref=user-ref")
                .param("request_id", "requestid")
                .with(httpBasic("config_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Successfully activated public key for user ref user-ref','request_id':'requestid'}}"));
    }

    @Test
    public void testThatCanDeactivatePublicKey() throws Exception, Throwable {
        when(publicKeyService.deactivatePublicKey(anyString(), anyString())).thenReturn(true);
        when(notificationProcessor.process(any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/public-key?ref=ref&user_ref=user_ref")
                .param("request_id", "requestid")
                .with(httpBasic("config_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(" {'status':{'code':200,'message':'Successfully deactivated public key','request_id':'requestid'}}"));
    }

    @Test
    public void testThatCanDeactivatePublicKeyWithUserRef() throws Throwable {
        when(publicKeyService.deactivateAllPublicKeysForUserRef(any())).thenReturn(true);
        when(notificationProcessor.process(any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/public-key?user_ref=user-ref")
                .param("request_id", "requestid")
                .with(httpBasic("config_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Successfully deactivated public keys','request_id':'requestid'}}"));
    }

}

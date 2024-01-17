package ke.co.nectar.config.service;

import ke.co.nectar.config.NectarConfigServiceApplication;
import ke.co.nectar.config.entity.*;
import ke.co.nectar.config.repository.PublicKeyRepository;
import ke.co.nectar.config.service.config.ConfigService;
import ke.co.nectar.config.utils.EncryptedYAMLConfig;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NectarConfigServiceApplication.class)
@FixMethodOrder(MethodSorters.JVM)
@Transactional
public class ConfigServiceTest {

    @Autowired
    ConfigService configService;

    @MockBean
    private PublicKeyRepository publicKeyRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private NativeConfig nativeConfig;

    private PrismThriftConfig prismThriftConfig;

    private String nativeConfigRef;

    private String prismThriftConfigRef;

    private final long EPOCH_TIME = 1606754076302l;

    @Before
    public void setup() throws Exception {
        Config config = new Config("Config", "ref", "user-ref", Config.ConfigType.NATIVE,
                            true, Instant.ofEpochMilli(EPOCH_TIME));

        nativeConfig = new NativeConfig(config, "255", "sta", "numeric", "02",
                "01", "1", "0abc12def3456789", "000046", "0", "1993",
                "600727");

        saveNativeConfig();

        config.setConfigType(Config.ConfigType.PRISM_THRIFT);

        prismThriftConfig = new PrismThriftConfig(config, "host", "100", "realm",
                "username", "password", "sta", "numeric", "600675", "1", "255", "01");

        savePrismThriftConfig();
    }

    private void saveNativeConfig() throws Exception {
        /**
         * YAML config file used for this test
         *
         * ---
         * name: example_config
         * type: native
         * key_expiry_no: 255
         * encryption_algorithm: sta
         * token_carrier_type: numeric
         * decoder_key_generation_algorithm: 04
         * tariff_index: 01
         * key_revision_no: 1
         * vending_key: 0123456789abcdef
         * supply_group_code: 123456
         * key_type: 3
         * base_date: 2035
         * issuer_identification_no: 600727
         */

        // Keys generated from { ke.co.nectar.config.utils.EncryptExampleWorkflow }

        final String USER_REF = "user-ref";

        PublicKey pk = new PublicKey();
        pk.setKey("MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAvp+e0D/MhungECsexbm+gqttT+wsPg3xC7mIPG4kHEGRDzthZr3kTJrTjWtnUtiObokoLoS9JHV2ycDYJSDGWz83zejtHmRe0PT6oIXByxBxXY0snviY8qgOv7DTCGNasEAExCkn1FWDWL5Xp9L5n+yJ3OB46+gv+a0NcQwWtj1hUPwFrrt5rubYfWbGcZAL7uIa/l5/+Bsqo4UPCvPGPtH0smgKSEJ7rljUulnnO1SZzjG7A5QVPLc9KYI6B+7Qi3yn8pxxXMaVLTcQAOGiHkMF+HXGAY3m5kC10vvRG/MVKeviV2Ac+lIkzyRpZc3RlSia5KrWuMHEGzNsFfUBgPBIsiDjcyiw4wpiKWOxVUn1lgFj7BY2oEkmZjPl/jMDSSDUPJ2T8+w1D6oQF0tSENu7duBtZmpfaeQXwNMEfg91dtQIKNXARLnvCyKuwPGq1Rytt/uaSZRcImgpBiOPbbSlhwryH6ABdCXSruf55aHvkIr6xMp1JC0ekl3awiCvmNXxiDNKeERfoDTLHevvMsDEtCEmnEMhuO8fdridkTzp1ecVzn5YZODuEUoksLTEj8d1kBFPGzhUgL/VE5LXpxHAHhTd3pZ8WAuO+XXX/T7ijBpdzctPESO+pVEqkkD65CVo+PnTx1uyQaaM2kDb84u6utixvejks854O8/9ilMCAwEAAQ==");

        ArrayList<PublicKey> pks = new ArrayList<>();
        pks.add(pk);

        when(publicKeyRepository.findByUserRefActivated(anyString())).thenReturn(pks);

        EncryptedYAMLConfig YAMLconfig = new EncryptedYAMLConfig();
        YAMLconfig.setData("MTIzNDU2NzgxMjM0NTY3OEDhTi/ovC/g58wK+7zhJVfndJUf2N6P5GMYAHuuUzyqJcEaYDccfGUQvNNQ1ZyRMZh4mrYcCiVLx7NOy8OVakYEK7+IbFiP6xLv+dUbQGmNsOS2Itz7QXyrmRmcI8N0i5HzBXAXKq9IR3L4N0TucqU4Qsv1wNeFMIxbqYFoRucaozXozyBD5K7WqNUZ2d3UgfRUP6i+L+wSVMpwcSHwfqf78CcpWL/BlohXG9fDOQ6/+v9jFLQH1tK9IvHjpsWmN+Tljx7ZRuIMHelxYVw3BguMWDW7rcnVxqwER+jGjZRPqQ6T+3dL6FX+t8h+jyhhr6peyKEzJcXGvQfhfp/8fAdKvNN1uP8748WM7FjdqTc6axT+6uz2YOlM4OLx+L7cct+DCHZk5oNdnuycjzQkpOI=");
        YAMLconfig.setDigest("pyE4G5DMIg4UZHWK1qrlu8yZ+7VyOAoK++vF3WBwfmRAY+foIyDdr/IlYn2atwW2+t1NJyOQX0yz9jA1Qrj1YLTI1mCOCRtI5CDwq+eTLvejdRRE20zaEuM126dYJ0RyJ3H/dQ16cXsfNU6swX5NSE4uO6g4wQlyUglMv75yV7F5iMULQy/F4MBIlIN7zJX5hLD5ZLoxXJOZ+Qys6wtF4OZnsQ+pYbDFdsyvfa73sa4v7NpE21NBkTLkRtC3X+pTBOfx2LUxeoy9CEcB2cq+n7HzeIZyIPsTLtLhol9Tk6MmZ13jIzuV/85y7Up+kIyQGGCk8U43XMsaElONeHIXj2OPnryFQUGA10URPMQNgZyNjLXFiqvWku5YWmkq5F5HSBq5gg4KnIgaUZ9CG8MR9nJwf6OlNQHnaHIkNMM6TBB29UX0J9S2JtP4WFHCbPsk+am3f1IKr5/Ou9BW+2+HAuAAW+sIjOdDuttbC/r+26gNf3oVCWboUe7xrSXgLXxizQkuDcw9+HrDUfkoXUoqN9Fjh7e4Fgdj449rLVIpexCfiIAtKqi2AEYakPsw15gNHql6K7IDxBflhhOw8adDWLkA8bjBvF8GTBx3whrT5LiOXVtBTNdYQdYP3jXg36oepHVs7eeCDspgN7SbyMpul9ZrYP997tIpeD4Y9HmxfII=");
        YAMLconfig.setKey("fh6N2GhIFj0Oes0CCYzbz+90fq7sTENHbM3yQR/AR8i8J54pe+EtjoMSE82uyQBhaHCQJH+5abPK9XQ2IGyIgjMh4Vip22pupYngImS79KO4d3iKvcJQOoV0JaTLqvj1r152X6SkACpHCGO1Yav60NZqwCAP51eDBv3q/uEiNMpymg6NuDb35Bfz5JQns0zolaTIothN8gRuFP+M3blrotNGP+rHEkGk956EfHnHHhMbXVvyt4fIiyBcVLnAED36vmTLj/Tgx+zwyW8gXqyYCMzR5FoJjcsH5JezeJFNwGrkTcIcMymj+mxOvuSG2B8zOQmkurSZw+G/8UyScpiGv3eqXJjVLL1zCwX0z52Bf+8d1R0GQtF/ZceuTB1/tr7IIy106I4rNA7qOBR8spHc2i9z+Dy85wdPWSqFEHhuTMp9WErUzbEEkhTATxXi6u0z9KZZ5hRU9Q6ng3/Obv4EgBNv6UNzrUc2/aGkbG0mtXCXKIuJkVHES9Ee7DjVWv4l1JjXIBzaR1A99sNwC/3mjCtUv3Xi1VkINI07AZSQE9FoMD27gQETqC/ZFfMBoFAdTWGKKnmEEx2Syp7BQMbbolbGCscyPtBitxGjx6/V5nDwWtVyIIDe2wuFg5h0KrwRgaaGcVSNIi6+oo9/+/Rptf2wsl02mfu/QfN9qOa3Sf4=");

        NativeConfig generatedConfig = (NativeConfig) configService.extractAndAdd(USER_REF, YAMLconfig);
        nativeConfigRef = generatedConfig.getConfig().getRef();
    }

    private void savePrismThriftConfig() throws Exception {
        /**
         * YAML config file used for this test
         *
         * ---
         * name: example prism thrift config
         * type: prism-thrift
         * host: 192.0.0.2
         * port: 8080
         * realm: realm
         * username: username
         * password: password
         * encryption_algorithm: sta
         * token_carrier_type: numeric
         * supply_group_code: 600675
         * key_revision_no: 1
         * key_expiry_no: 255
         * tariff_index: 01
         */

        final String USER_REF = "user-ref";

        PublicKey pk = new PublicKey();
        pk.setKey("MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAvp+e0D/MhungECsexbm+gqttT+wsPg3xC7mIPG4kHEGRDzthZr3kTJrTjWtnUtiObokoLoS9JHV2ycDYJSDGWz83zejtHmRe0PT6oIXByxBxXY0snviY8qgOv7DTCGNasEAExCkn1FWDWL5Xp9L5n+yJ3OB46+gv+a0NcQwWtj1hUPwFrrt5rubYfWbGcZAL7uIa/l5/+Bsqo4UPCvPGPtH0smgKSEJ7rljUulnnO1SZzjG7A5QVPLc9KYI6B+7Qi3yn8pxxXMaVLTcQAOGiHkMF+HXGAY3m5kC10vvRG/MVKeviV2Ac+lIkzyRpZc3RlSia5KrWuMHEGzNsFfUBgPBIsiDjcyiw4wpiKWOxVUn1lgFj7BY2oEkmZjPl/jMDSSDUPJ2T8+w1D6oQF0tSENu7duBtZmpfaeQXwNMEfg91dtQIKNXARLnvCyKuwPGq1Rytt/uaSZRcImgpBiOPbbSlhwryH6ABdCXSruf55aHvkIr6xMp1JC0ekl3awiCvmNXxiDNKeERfoDTLHevvMsDEtCEmnEMhuO8fdridkTzp1ecVzn5YZODuEUoksLTEj8d1kBFPGzhUgL/VE5LXpxHAHhTd3pZ8WAuO+XXX/T7ijBpdzctPESO+pVEqkkD65CVo+PnTx1uyQaaM2kDb84u6utixvejks854O8/9ilMCAwEAAQ==");

        ArrayList<PublicKey> pks = new ArrayList<>();
        pks.add(pk);

        when(publicKeyRepository.findByUserRefActivated(anyString())).thenReturn(pks);

        EncryptedYAMLConfig YAMLconfig = new EncryptedYAMLConfig();
        YAMLconfig.setData("MTIzNDU2NzgxMjM0NTY3OEDhTi/ovC/g58wK+7zhJVdT/3X7DNjp/JS++mvUpcdv5Fivgi0vaMn72UKwXpvpgEiaMdH9iTdIqNEeUVCproFBful+XRB5tJaEQOEqZQOINt/PmCPJRfV7AjpqKcVsudS+p5sM7KnSanonD1Ve7Bfwma9bzv+oAfM5Yodhi5HgPH5dbaGOqqo1D5vcLaGKWvSE8GbPDIwp2Uqcrlw5VJO7uc8cqcXfb7HE4dgyXsXH/NNgV5OIl6nvfQFK09x+JOEi1wrcn3Er/a2FnduLCVP84B85RFSoF7YdnW8IX8sb4sjnRS+AqyU8qdVdUpjoyGz4F/XS1XafiLGgzxWZsEX3Rt6+GsbSQMiT4m2OZCc5KsjDgUEHG2B5ooUvpNITSQ==");
        YAMLconfig.setDigest("E8wG3bmqURR1e/NUg2g4YXMevz1NqOMxfFQOXQBiYViWmyWisn5DsNCvJ6t+hrjramdKq49owWcp3jLiWJLs8N2IFz1uLTg1/svVgadOVKQw5mMI1/kVGlh/u20SBh/WohgTqrk/xrZ5Yeukq77qaUBq+ZzS/wavtPqywf2HyGH8OwFRaBWOMb4I1KWn41NYiYp2N2/d6gIgXMxy+Cco3UP2ece7HYDmdxWuKb07BP0G0RyjA2bZQ7Q6OvQPnZdtA3VKJUtLmaQzNJEASqAyYcAnoHSb5BB0HYCgJNeHz4mo49AhGthOwwhh1K3DphMM6l1EMV72aezdMvfjelPSXo7HolfqiF7iHTeEsfA3v/zYpnaPE2KOL+6HuWJfjSyHqKC+j/JRcle7vXZVm93WUcsfbRuMdHVku8i+TEXocXbbSVgX9bIbdhu8hQfJn+E7eLZvelxDGQ/3/jcesQzFaPts5Pd+UDSFQqi14yBzQhSNF5JhkMAtV6yy3zdu7okDj3+r7LbeFruqpgljWt3zYF+nuZRIUutftzh1lVRnNSSYRMHlPoojTRXmsl/BBNE9tbnKfa+BK9ZUupoiFvkPu8Ktu1SroiQqbFT/akRC1p243Um79MFwH1+AUDJ/nQlkP2awEqGMPS54W0ZW6cF7knVZI5o7V4jNGtDq0aNNRBY=");
        YAMLconfig.setKey("fh6N2GhIFj0Oes0CCYzbz+90fq7sTENHbM3yQR/AR8i8J54pe+EtjoMSE82uyQBhaHCQJH+5abPK9XQ2IGyIgjMh4Vip22pupYngImS79KO4d3iKvcJQOoV0JaTLqvj1r152X6SkACpHCGO1Yav60NZqwCAP51eDBv3q/uEiNMpymg6NuDb35Bfz5JQns0zolaTIothN8gRuFP+M3blrotNGP+rHEkGk956EfHnHHhMbXVvyt4fIiyBcVLnAED36vmTLj/Tgx+zwyW8gXqyYCMzR5FoJjcsH5JezeJFNwGrkTcIcMymj+mxOvuSG2B8zOQmkurSZw+G/8UyScpiGv3eqXJjVLL1zCwX0z52Bf+8d1R0GQtF/ZceuTB1/tr7IIy106I4rNA7qOBR8spHc2i9z+Dy85wdPWSqFEHhuTMp9WErUzbEEkhTATxXi6u0z9KZZ5hRU9Q6ng3/Obv4EgBNv6UNzrUc2/aGkbG0mtXCXKIuJkVHES9Ee7DjVWv4l1JjXIBzaR1A99sNwC/3mjCtUv3Xi1VkINI07AZSQE9FoMD27gQETqC/ZFfMBoFAdTWGKKnmEEx2Syp7BQMbbolbGCscyPtBitxGjx6/V5nDwWtVyIIDe2wuFg5h0KrwRgaaGcVSNIi6+oo9/+/Rptf2wsl02mfu/QfN9qOa3Sf4=");

        PrismThriftConfig generatedConfig = (PrismThriftConfig) configService.extractAndAdd(USER_REF, YAMLconfig);
        prismThriftConfigRef = generatedConfig.getConfig().getRef();
    }

    @Test
    public void testThatNativeConfigIsExtractedAndAdded() throws Exception {

        NativeConfig obtainedConfig = (NativeConfig) configService.find(nativeConfigRef, true);

        Assert.assertEquals("example_config", obtainedConfig.getConfig().getName());
        Assert.assertEquals(Config.ConfigType.NATIVE, obtainedConfig.getConfig().getConfigType());
        Assert.assertEquals("255", obtainedConfig.getKeyExpiryNo());
        Assert.assertEquals("sta", obtainedConfig.getEncryptionAlgorithm());
        Assert.assertEquals("numeric", obtainedConfig.getTokenCarrierType());
        Assert.assertEquals("04", obtainedConfig.getDecoderKeyGenerationAlgorithm());
        Assert.assertEquals("01", obtainedConfig.getTariffIndex());
        Assert.assertEquals("1", obtainedConfig.getKeyRevisionNo());
        Assert.assertEquals("0123456789abcdef", obtainedConfig.getVendingKey());
        Assert.assertEquals("123456", obtainedConfig.getSupplyGroupCode());
        Assert.assertEquals("3", obtainedConfig.getKeyType());
        Assert.assertEquals("2035", obtainedConfig.getBaseDate());
        Assert.assertEquals("600727", obtainedConfig.getIssuerIdentificationNo());

        Assert.assertTrue(obtainedConfig.getConfig().isActivated());
        Assert.assertNotNull(obtainedConfig.getConfig().getCreatedAt());
    }

    @Test
    public void testThatPrismThriftConfigIsExtractedAndAdded() throws Exception {
        PrismThriftConfig obtainedConfig = (PrismThriftConfig) configService.find(prismThriftConfigRef, true);

        Assert.assertEquals("example prism thrift config", obtainedConfig.getConfig().getName());
        Assert.assertEquals(Config.ConfigType.PRISM_THRIFT, obtainedConfig.getConfig().getConfigType());
        Assert.assertEquals("197.156.230.188", obtainedConfig.getHost());
        Assert.assertEquals("9443", obtainedConfig.getPort());
        Assert.assertEquals("thrift_lagos", obtainedConfig.getUsername());
        Assert.assertEquals("z3WAnCHQ1", obtainedConfig.getPassword());
        Assert.assertEquals("local", obtainedConfig.getRealm());
        Assert.assertEquals("sta", obtainedConfig.getEncryptionAlgorithm());
        Assert.assertEquals("numeric", obtainedConfig.getTokenCarrierType());
        Assert.assertEquals("600675", obtainedConfig.getSupplyGroupCode());
        Assert.assertEquals("1", obtainedConfig.getKeyRevisionNo());
        Assert.assertEquals("255", obtainedConfig.getKeyExpiryNo());
        Assert.assertEquals("01", obtainedConfig.getTariffIndex());

        Assert.assertTrue(obtainedConfig.getConfig().isActivated());
        Assert.assertNotNull(obtainedConfig.getConfig().getCreatedAt());
    }

    @Test
    public void testThatNativeConfigIsReturned() throws Exception {
        NativeConfig obtainedConfig = (NativeConfig) configService.find(nativeConfigRef, true);

        Assert.assertNotNull(obtainedConfig);
        Assert.assertEquals("example_config", obtainedConfig.getConfig().getName());
        Assert.assertEquals(Config.ConfigType.NATIVE, obtainedConfig.getConfig().getConfigType());
        Assert.assertEquals("255", obtainedConfig.getKeyExpiryNo());
        Assert.assertEquals("sta", obtainedConfig.getEncryptionAlgorithm());
        Assert.assertEquals("numeric", obtainedConfig.getTokenCarrierType());
        Assert.assertEquals("04", obtainedConfig.getDecoderKeyGenerationAlgorithm());
        Assert.assertEquals("01", obtainedConfig.getTariffIndex());
        Assert.assertEquals("1", obtainedConfig.getKeyRevisionNo());
        Assert.assertEquals("0123456789abcdef", obtainedConfig.getVendingKey());
        Assert.assertEquals("123456", obtainedConfig.getSupplyGroupCode());
        Assert.assertEquals("3", obtainedConfig.getKeyType());
        Assert.assertEquals("2035", obtainedConfig.getBaseDate());
        Assert.assertEquals("user-ref", obtainedConfig.getConfig().getUserRef());
        Assert.assertEquals("600727", obtainedConfig.getIssuerIdentificationNo());

        Assert.assertTrue(obtainedConfig.getConfig().isActivated());
        Assert.assertNotNull(obtainedConfig.getConfig().getCreatedAt());
    }

    @Test
    public void testThatPrismThriftConfigIsReturned() throws Exception {
        PrismThriftConfig obtainedConfig = (PrismThriftConfig) configService.find(prismThriftConfigRef, true);

        Assert.assertNotNull(obtainedConfig);
        Assert.assertEquals("example prism thrift config", obtainedConfig.getConfig().getName());
        Assert.assertEquals(Config.ConfigType.PRISM_THRIFT, obtainedConfig.getConfig().getConfigType());
        Assert.assertEquals("197.156.230.188", obtainedConfig.getHost());
        Assert.assertEquals("9443", obtainedConfig.getPort());
        Assert.assertEquals("local", obtainedConfig.getRealm());
        Assert.assertEquals("thrift_lagos", obtainedConfig.getUsername());
        Assert.assertEquals("z3WAnCHQ1", obtainedConfig.getPassword());
        Assert.assertEquals("sta", obtainedConfig.getEncryptionAlgorithm());
        Assert.assertEquals("numeric", obtainedConfig.getTokenCarrierType());
        Assert.assertEquals("600675", obtainedConfig.getSupplyGroupCode());
        Assert.assertEquals("1", obtainedConfig.getKeyRevisionNo());
        Assert.assertEquals("255", obtainedConfig.getKeyExpiryNo());
        Assert.assertEquals("01", obtainedConfig.getTariffIndex());

        Assert.assertEquals("user-ref", obtainedConfig.getConfig().getUserRef());
        Assert.assertTrue(obtainedConfig.getConfig().isActivated());
        Assert.assertNotNull(obtainedConfig.getConfig().getCreatedAt());
    }

    @Test
    public void testThatConfigByRefAndUserRefForNativeConfigIsReturned() throws Exception {
        NativeConfig obtainedConfig = (NativeConfig) configService.findByRefAndUserRef(nativeConfigRef, "user-ref", true);

        Assert.assertNotNull(obtainedConfig);
        Assert.assertEquals("example_config", obtainedConfig.getConfig().getName());
        Assert.assertEquals(Config.ConfigType.NATIVE, obtainedConfig.getConfig().getConfigType());
        Assert.assertEquals("255", obtainedConfig.getKeyExpiryNo());
        Assert.assertEquals("sta", obtainedConfig.getEncryptionAlgorithm());
        Assert.assertEquals("numeric", obtainedConfig.getTokenCarrierType());
        Assert.assertEquals("04", obtainedConfig.getDecoderKeyGenerationAlgorithm());
        Assert.assertEquals("01", obtainedConfig.getTariffIndex());
        Assert.assertEquals("1", obtainedConfig.getKeyRevisionNo());
        Assert.assertEquals("0123456789abcdef", obtainedConfig.getVendingKey());
        Assert.assertEquals("123456", obtainedConfig.getSupplyGroupCode());
        Assert.assertEquals("3", obtainedConfig.getKeyType());
        Assert.assertEquals("2035", obtainedConfig.getBaseDate());
        Assert.assertEquals("user-ref", obtainedConfig.getConfig().getUserRef());
        Assert.assertEquals("600727", obtainedConfig.getIssuerIdentificationNo());
        Assert.assertTrue(obtainedConfig.getConfig().isActivated());
        Assert.assertNotNull(obtainedConfig.getConfig().getCreatedAt());
    }

    @Test
    public void testThatConfigByRefAndUserRefForPrismThriftConfigIsReturned() throws Exception {
        PrismThriftConfig obtainedConfig = (PrismThriftConfig) configService.findByRefAndUserRef(prismThriftConfigRef, "user-ref", true);

        Assert.assertNotNull(obtainedConfig);
        Assert.assertEquals("example prism thrift config", obtainedConfig.getConfig().getName());
        Assert.assertEquals(Config.ConfigType.PRISM_THRIFT, obtainedConfig.getConfig().getConfigType());
        Assert.assertEquals("197.156.230.188", obtainedConfig.getHost());
        Assert.assertEquals("9443", obtainedConfig.getPort());
        Assert.assertEquals("local", obtainedConfig.getRealm());
        Assert.assertEquals("thrift_lagos", obtainedConfig.getUsername());
        Assert.assertEquals("z3WAnCHQ1", obtainedConfig.getPassword());
        Assert.assertEquals("sta", obtainedConfig.getEncryptionAlgorithm());
        Assert.assertEquals("numeric", obtainedConfig.getTokenCarrierType());
        Assert.assertEquals("600675", obtainedConfig.getSupplyGroupCode());
        Assert.assertEquals("1", obtainedConfig.getKeyRevisionNo());
        Assert.assertEquals("255", obtainedConfig.getKeyExpiryNo());
        Assert.assertEquals("01", obtainedConfig.getTariffIndex());

        Assert.assertEquals("user-ref", obtainedConfig.getConfig().getUserRef());
        Assert.assertTrue(obtainedConfig.getConfig().isActivated());
        Assert.assertNotNull(obtainedConfig.getConfig().getCreatedAt());
    }

    @Test
    public void testThatConfigsForUserAreReturned() throws Exception {
        List<STSConfig> configs = configService.findByUserRef("user-ref", true);

        Assert.assertNotNull(configs);
        Assert.assertEquals(1, configs.size());
    }
}

package ke.co.nectar.config.service.config.impl;

import ke.co.nectar.config.constants.StringConstants;
import ke.co.nectar.config.entity.Config;
import ke.co.nectar.config.entity.NativeConfig;
import ke.co.nectar.config.entity.PrismThriftConfig;
import ke.co.nectar.config.entity.STSConfig;
import ke.co.nectar.config.service.config.impl.exceptions.InvalidConfigException;
import ke.co.nectar.config.utils.AppUtils;
import ke.co.nectar.config.utils.AsymmetricEncryptUtils;
import ke.co.nectar.config.utils.YamlUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.PrivateKey;
import java.time.Instant;
import java.util.Map;

import static ke.co.nectar.config.utils.SymmetricEncryptUtils.encrypt;


@Component
public class ConfigServiceUtils {

    public String getKey(String keyPath)
            throws Exception  {
        BufferedReader bufferedReader;
        FileReader fileReader;
        fileReader = new FileReader(keyPath);
        bufferedReader = new BufferedReader(fileReader);

        String line, result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }
        bufferedReader.close();
        fileReader.close();

        return result;
    }

    public PrivateKey getNectarPrivateKey(String privateKeyPath)
        throws Exception  {
        BufferedReader bufferedReader;
        FileReader fileReader;
        fileReader = new FileReader(privateKeyPath);
        bufferedReader = new BufferedReader(fileReader);

        String line, result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }
        bufferedReader.close();
        fileReader.close();

        return AsymmetricEncryptUtils.getPrivateKeyFromString(result);
    }

    public STSConfig processYAMLConfig(String yaml, String userRef, String dbKey)
        throws Exception {
        Map<String, String> configs = YamlUtils.load(yaml);

        try {
            if (configs.containsKey("type") &&
                    (configs.get("type").equals("prism-thrift") ||
                            configs.get("type").equals("native"))) {

                Config.ConfigType configType = null;

                if (configs.get("type").trim().toUpperCase().equals("NATIVE")) {
                    configType = Config.ConfigType.NATIVE;
                } else if (configs.get("type").trim().toUpperCase().equals("PRISM-THRIFT")) {
                    configType = Config.ConfigType.PRISM_THRIFT;
                }

                Config config = new Config();
                config.setName(configs.get("name"));
                config.setRef(AppUtils.generateRef());
                config.setUserRef(userRef);
                config.setConfigType(configType);
                config.setActivated(true);
                config.setCreatedAt(Instant.now());

                if (configType == Config.ConfigType.NATIVE) {
                    NativeConfig nativeConfig = new NativeConfig();
                    nativeConfig.setConfig(config);
                    nativeConfig.setKeyExpiryNo(encrypt(configs.get("key_expiry_no"), dbKey));
                    nativeConfig.setEncryptionAlgorithm(encrypt(configs.get("encryption_algorithm"), dbKey));
                    nativeConfig.setTokenCarrierType(encrypt(configs.get("token_carrier_type"), dbKey));
                    nativeConfig.setDecoderKeyGenerationAlgorithm(encrypt(configs.get("decoder_key_generation_algorithm"), dbKey));
                    nativeConfig.setTariffIndex(encrypt(configs.get("tariff_index"), dbKey));
                    nativeConfig.setKeyRevisionNo(encrypt(configs.get("key_revision_no"), dbKey));
                    nativeConfig.setVendingKey(encrypt(configs.get("vending_key"), dbKey));
                    nativeConfig.setSupplyGroupCode(encrypt(configs.get("supply_group_code"), dbKey));
                    nativeConfig.setKeyType(encrypt(configs.get("key_type"), dbKey));
                    nativeConfig.setBaseDate(encrypt(configs.get("base_date"), dbKey));
                    nativeConfig.setIssuerIdentificationNo(encrypt(configs.get("issuer_identification_no"), dbKey));
                    return nativeConfig;

                } else if (configType == Config.ConfigType.PRISM_THRIFT) {
                    PrismThriftConfig prismThriftConfig = new PrismThriftConfig();
                    prismThriftConfig.setConfig(config);
                    prismThriftConfig.setHost(encrypt(configs.get("host"), dbKey));
                    prismThriftConfig.setPort(encrypt(configs.get("port"), dbKey));
                    prismThriftConfig.setRealm(encrypt(configs.get("realm"), dbKey));
                    prismThriftConfig.setUsername(encrypt(configs.get("username"), dbKey));
                    prismThriftConfig.setPassword(encrypt(configs.get("password"), dbKey));
                    prismThriftConfig.setEncryptionAlgorithm(encrypt(configs.get("encryption_algorithm"), dbKey));
                    prismThriftConfig.setTokenCarrierType(encrypt(configs.get("token_carrier_type"), dbKey));
                    prismThriftConfig.setSupplyGroupCode(encrypt(configs.get("supply_group_code"), dbKey));
                    prismThriftConfig.setKeyRevisionNo(encrypt(configs.get("key_revision_no"), dbKey));
                    prismThriftConfig.setKeyExpiryNo(encrypt(configs.get("key_expiry_no"), dbKey));
                    prismThriftConfig.setTariffIndex(encrypt(configs.get("tariff_index"), dbKey));
                    return prismThriftConfig;

                }
            }
            throw new InvalidConfigException(StringConstants.INVALID_CONFIG_FILE);

        } catch (Exception e) {
            throw new InvalidConfigException(e.getMessage());
        }
    }
}

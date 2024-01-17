package ke.co.nectar.config.service.config.impl;

import ke.co.nectar.config.constants.StringConstants;
import ke.co.nectar.config.entity.Config;
import ke.co.nectar.config.entity.NativeConfig;
import ke.co.nectar.config.entity.PrismThriftConfig;
import ke.co.nectar.config.entity.STSConfig;
import ke.co.nectar.config.repository.ConfigRepository;
import ke.co.nectar.config.repository.NativeConfigRepository;
import ke.co.nectar.config.repository.PrismThriftConfigRepository;
import ke.co.nectar.config.repository.PublicKeyRepository;
import ke.co.nectar.config.repository.exceptions.NoPublicKeyForUserException;
import ke.co.nectar.config.service.config.ConfigService;
import ke.co.nectar.config.service.config.impl.exceptions.InvalidConfigRefException;
import ke.co.nectar.config.service.config.impl.exceptions.InvalidUserRefException;
import ke.co.nectar.config.service.config.impl.exceptions.MessageDigestValidationException;
import ke.co.nectar.config.utils.AsymmetricEncryptUtils;
import ke.co.nectar.config.utils.EncryptedYAMLConfig;
import ke.co.nectar.config.utils.SymmetricEncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ke.co.nectar.config.utils.SymmetricEncryptUtils.decrypt;

@Service
public class ConfigServiceImpl implements ConfigService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private NativeConfigRepository nativeConfigRepository;

    @Autowired
    private PrismThriftConfigRepository prismThriftConfigRepository;

    @Autowired
    private PublicKeyRepository publicKeyRepository;

    @Value("${encrypt.shared.private-key}")
    private String nectarPrivateKeyPath;

    @Value("${encrypt.db.key}")
    private String dbKeyPath;

    @Override
    public STSConfig extractAndAdd(String userRef, EncryptedYAMLConfig YAMLConfig) throws Exception {

        STSConfig config;

        PublicKey userPublicKey = getUserPublicKey(userRef);
        PrivateKey nectarPrivateKey = getNectarPrivateKey(nectarPrivateKeyPath);
        String yamlConfigData = readYAMLConfig(YAMLConfig, nectarPrivateKey);

        if (validateYamlConfigDigest(YAMLConfig, yamlConfigData, userPublicKey)) {
            config = getConfigFromYAML(yamlConfigData, userRef, getKey());

            if (config.getClass().getSimpleName().equals("NativeConfig")) {
                nativeConfigRepository.save((NativeConfig) config);
                config = decryptNativeConfig((NativeConfig) config, true);
            } else if(config.getClass().getSimpleName().equals("PrismThriftConfig")) {
                prismThriftConfigRepository.save((PrismThriftConfig) config);
                config = decryptPrismThriftConfig((PrismThriftConfig) config, true);
            }

        } else {
            throw new MessageDigestValidationException(StringConstants.DIGEST_PAYLOAD_MISMATCH);
        }

        return config;
    }

    @Override
    public STSConfig find(String ref, boolean detailed) throws Exception {
        Config config = configRepository.findByRef(ref);
        if (config != null) {
            if (config.getConfigType() == Config.ConfigType.NATIVE) {
                NativeConfig nativeConfig = nativeConfigRepository.findByConfig(config);
                return decryptNativeConfig(nativeConfig, detailed);
            } else if (config.getConfigType() == Config.ConfigType.PRISM_THRIFT) {
                PrismThriftConfig prismThriftConfig = prismThriftConfigRepository.findByConfig(config);
                return decryptPrismThriftConfig(prismThriftConfig, detailed);
            }
        }
        throw new InvalidConfigRefException(StringConstants.INVALID_CONFIG_REF);
    }

    @Override
    public STSConfig findByRefAndUserRef(String ref, String userRef, boolean detailed) throws Exception {
        Config config = configRepository.findByRefAndUserRef(ref, userRef);
        if (config != null) {
            if (config.getConfigType() == Config.ConfigType.NATIVE) {
                return decryptNativeConfig(nativeConfigRepository.findByConfig(config), detailed);
            } else if (config.getConfigType() == Config.ConfigType.PRISM_THRIFT) {
                return decryptPrismThriftConfig(prismThriftConfigRepository.findByConfig(config), detailed);
            }
        }
        throw new InvalidConfigRefException(StringConstants.INVALID_CONFIG_REF);
    }


    @Override
    public List<STSConfig> findByUserRef(String userRef, boolean detailed) throws Exception {
        List<Config> configs = configRepository.findByUserRef(userRef);
        List<STSConfig> extractedConfigs = new ArrayList<>();
        if (configs != null) {
            for (Config config : configs) {
                if (config.getConfigType() == Config.ConfigType.NATIVE) {
                    extractedConfigs.add(decryptNativeConfig(nativeConfigRepository.findByConfig(config), detailed));
                } else if (config.getConfigType() == Config.ConfigType.PRISM_THRIFT) {
                    extractedConfigs.add(decryptPrismThriftConfig(prismThriftConfigRepository.findByConfig(config), detailed));
                }
            }
            return extractedConfigs;
        }
        throw new InvalidConfigRefException(StringConstants.INVALID_CONFIG_REF);
    }

    @Override
    public boolean activateConfig(String userRef, String ref) throws Exception {
        Config config = configRepository.findByRef(ref);
        if (config != null) {
            if (config.getUserRef().equals(userRef)) {
                return configRepository.activateConfig(ref) > 0;
            }
            throw new InvalidUserRefException(StringConstants.INVALID_USER_REF);
        }
        throw new InvalidConfigRefException(StringConstants.INVALID_REF);
    }

    @Override
    public boolean deactivateConfig(String userRef, String ref) throws Exception {
        Config config = configRepository.findByRef(ref);
        if (config != null) {
            if (config.getUserRef().equals(userRef)) {
                return configRepository.deactivateConfig(ref) > 0;
            }
            throw new InvalidUserRefException(StringConstants.INVALID_USER_REF);
        }
        throw new InvalidConfigRefException(StringConstants.INVALID_REF);
    }

    private String readYAMLConfig(EncryptedYAMLConfig jsonYAMLConfig,
                                  PrivateKey nectarPrivateKey) throws Exception {

        byte[] symmetricKeyBase64 = AsymmetricEncryptUtils.decodeBASE64(jsonYAMLConfig.getKey());
        byte[] symmetricKey = AsymmetricEncryptUtils.decrypt(symmetricKeyBase64, nectarPrivateKey);

        byte[] messageBase64 = AsymmetricEncryptUtils.decodeBASE64(jsonYAMLConfig.getData());
        byte[] decryptedData = SymmetricEncryptUtils.decrypt(messageBase64, symmetricKey);

        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    private boolean validateYamlConfigDigest(EncryptedYAMLConfig jsonYAMLConfig,
                                             String yamlConfigData,
                                             PublicKey userPublicKey) throws Exception {
        MessageDigest mdDec = MessageDigest.getInstance("SHA-256");
        mdDec.update(yamlConfigData.getBytes());
        byte[] digestEnc = mdDec.digest();

        byte[] digestBase64 = AsymmetricEncryptUtils.decodeBASE64(jsonYAMLConfig.getDigest());
        byte[] digest = AsymmetricEncryptUtils.decrypt(digestBase64, userPublicKey);

        return Arrays.equals(digestEnc, digest);
    }

    private PrivateKey getNectarPrivateKey(String privateKeyPath) throws Exception {
        ConfigServiceUtils utils = new ConfigServiceUtils();
        return utils.getNectarPrivateKey(privateKeyPath);
    }

    private PublicKey getUserPublicKey(String userRef) throws Exception {
        List<ke.co.nectar.config.entity.PublicKey> publicKey = publicKeyRepository.findByUserRefActivated(userRef);
        if (publicKey != null) {
            return AsymmetricEncryptUtils.getPublicKeyFromString(publicKey.get(0).getKey());
        }
        throw new NoPublicKeyForUserException(String.format("No activated public key for user ref %d", userRef));
    }

    private STSConfig getConfigFromYAML(String yaml, String userRef, String dbKey) throws Exception {
        ConfigServiceUtils utils = new ConfigServiceUtils();
        return utils.processYAMLConfig(yaml, userRef, dbKey);
    }

    private NativeConfig decryptNativeConfig(NativeConfig config, boolean detailed) throws Exception {
        NativeConfig decryptedConfig = new NativeConfig();
        decryptedConfig.setConfig(config.getConfig());

        if (detailed) {
            decryptedConfig.setIssuerIdentificationNo(decrypt(config.getIssuerIdentificationNo(), getKey()));
            decryptedConfig.setBaseDate(decrypt(config.getBaseDate(), getKey()));
            decryptedConfig.setDecoderKeyGenerationAlgorithm(decrypt(config.getDecoderKeyGenerationAlgorithm(), getKey()));
            decryptedConfig.setEncryptionAlgorithm(decrypt(config.getEncryptionAlgorithm(), getKey()));
            decryptedConfig.setKeyExpiryNo(decrypt(config.getKeyExpiryNo(), getKey()));
            decryptedConfig.setKeyRevisionNo(decrypt(config.getKeyRevisionNo(), getKey()));
            decryptedConfig.setKeyType(decrypt(config.getKeyType(), getKey()));
            decryptedConfig.setSupplyGroupCode(decrypt(config.getSupplyGroupCode(), getKey()));
            decryptedConfig.setTariffIndex(decrypt(config.getTariffIndex(), getKey()));
            decryptedConfig.setTokenCarrierType(decrypt(config.getTokenCarrierType(), getKey()));
            decryptedConfig.setVendingKey(decrypt(config.getVendingKey(), getKey()));
        }
        return decryptedConfig;
    }

    private PrismThriftConfig decryptPrismThriftConfig(PrismThriftConfig config, boolean detailed) throws Exception {
        PrismThriftConfig decryptedConfig = new PrismThriftConfig();
        decryptedConfig.setConfig(config.getConfig());

        if (detailed) {
            decryptedConfig.setHost(decrypt(config.getHost(), getKey()));
            decryptedConfig.setPort(decrypt(config.getPort(), getKey()));
            decryptedConfig.setUsername(decrypt(config.getUsername(), getKey()));
            decryptedConfig.setPassword(decrypt(config.getPassword(), getKey()));
            decryptedConfig.setRealm(decrypt(config.getRealm(), getKey()));
            decryptedConfig.setEncryptionAlgorithm(decrypt(config.getEncryptionAlgorithm(), getKey()));
            decryptedConfig.setTokenCarrierType(decrypt(config.getTokenCarrierType(), getKey()));
            decryptedConfig.setSupplyGroupCode(decrypt(config.getSupplyGroupCode(), getKey()));
            decryptedConfig.setKeyRevisionNo(decrypt(config.getKeyRevisionNo(), getKey()));
            decryptedConfig.setKeyExpiryNo(decrypt(config.getKeyExpiryNo(), getKey()));
            decryptedConfig.setTariffIndex(decrypt(config.getTariffIndex(), getKey()));
        }

        return decryptedConfig;
    }

    private String getKey() throws Exception {
        ConfigServiceUtils utils = new ConfigServiceUtils();
        return utils.getKey(dbKeyPath);
    }
}

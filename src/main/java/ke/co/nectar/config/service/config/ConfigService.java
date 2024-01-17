package ke.co.nectar.config.service.config;

import ke.co.nectar.config.entity.STSConfig;
import ke.co.nectar.config.utils.EncryptedYAMLConfig;

import java.util.List;

public interface ConfigService {

    STSConfig extractAndAdd(String userRef, EncryptedYAMLConfig YAMLConfig) throws Exception;

    STSConfig find(String ref, boolean detailed) throws Exception;

    List<STSConfig> findByUserRef(String userRef, boolean detailed) throws Exception;

    boolean activateConfig(String userRef, String ref) throws Exception;

    boolean deactivateConfig(String userRef, String ref) throws Exception;

    STSConfig findByRefAndUserRef(String ref, String userRef, boolean detailed) throws Exception;
}

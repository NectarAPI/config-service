package ke.co.nectar.config.service.publickey;

import ke.co.nectar.config.entity.PublicKey;

import java.util.List;

public interface PublicKeyService {

    List<PublicKey> findByUserRef(String userRef) throws Exception;

    List<PublicKey> findByUserRefActivated(String userRef) throws Exception;

    PublicKey findByRef(String ref) throws Exception;

    PublicKey add(String name, String key, String userRef, boolean activated) throws Exception;

    boolean activatePublicKey(String ref, String userRef) throws Exception;

    boolean activateAllPublicKeysForUserRef(String userRef) throws Exception;

    boolean deactivatePublicKey(String ref, String userRef) throws Exception;

    boolean deactivateAllPublicKeysForUserRef(String userRef) throws Exception;
}

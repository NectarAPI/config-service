package ke.co.nectar.config.service.publickey.impl;

import ke.co.nectar.config.constants.StringConstants;
import ke.co.nectar.config.entity.PublicKey;
import ke.co.nectar.config.repository.PublicKeyRepository;
import ke.co.nectar.config.service.publickey.PublicKeyService;
import ke.co.nectar.config.service.publickey.impl.exceptions.InvalidPublicKeyRefException;
import ke.co.nectar.config.service.publickey.impl.exceptions.NoPublicKeyForUserException;
import ke.co.nectar.config.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.util.List;

@Service
public class PublicKeyServiceImpl implements PublicKeyService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PublicKeyRepository publicKeyRepository;

    @Override
    public List<PublicKey> findByUserRef(String userRef) throws Exception {
        List<PublicKey> publicKey = publicKeyRepository.findByUserRef(userRef);
        if (publicKey != null) {
            return publicKey;
        } else {
            throw new NoPublicKeyForUserException(StringConstants.NO_PUBLIC_KEY_BY_USER_ID);
        }
    }

    @Override
    public PublicKey findByRef(String ref) throws Exception {
        PublicKey publicKey = publicKeyRepository.findByRef(ref);
        if (publicKey != null) {
            return publicKey;
        } else {
            throw new InvalidPublicKeyRefException(StringConstants.INVALID_PUBLIC_KEY_REF);
        }
    }

    @Override
    public List<PublicKey> findByUserRefActivated(String userRef) throws Exception {
        List<PublicKey> publicKeys = publicKeyRepository.findByUserRefActivated(userRef);
        if (publicKeys != null) {
            return publicKeys;
        } else {
            throw new InvalidPublicKeyRefException(StringConstants.INVALID_PUBLIC_KEY_REF);
        }
    }

    public PublicKey add(String name, String key, String userRef, boolean activated) throws Exception {
        PublicKey publicKey = new PublicKey();
        String keyRef = AppUtils.generateRef();
        publicKey.setKey(key);
        publicKey.setName(name);
        publicKey.setRef(keyRef);
        publicKey.setUserRef(userRef);
        publicKey.setActivated(activated);
        publicKey.setCreatedAt(Instant.now());
        publicKeyRepository.save(publicKey);
        return publicKey;
    }

    @Override
    public boolean activatePublicKey(String ref, String userRef) throws Exception {
        return publicKeyRepository.activatePublicKey(ref, userRef) > 0;
    }

    @Override
    public boolean deactivatePublicKey(String ref, String userRef) throws Exception {
        return publicKeyRepository.deactivatePublicKey(ref, userRef) > 0;
    }

    public boolean activateAllPublicKeysForUserRef(String userRef) throws Exception {
        return publicKeyRepository.activateAllPublicKeysForUserRef(userRef) > 0;
    }

    public boolean deactivateAllPublicKeysForUserRef(String userRef) throws Exception {
        return publicKeyRepository.deactivateAllPublicKeysForUserRef(userRef) > 0;
    }
}

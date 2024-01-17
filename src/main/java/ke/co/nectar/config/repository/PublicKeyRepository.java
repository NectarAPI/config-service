package ke.co.nectar.config.repository;

import ke.co.nectar.config.entity.PublicKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface PublicKeyRepository extends JpaRepository<PublicKey, Long> {

    List<PublicKey> findByUserRef(String ref);

    PublicKey findByRef(String ref);

    @Query("Select pk from PublicKey pk where pk.userRef = :ref and pk.activated = true")
    List<PublicKey> findByUserRefActivated(@Param("ref") String ref);

    @Transactional
    @Modifying
    @Query("Update PublicKey pk set pk.activated = false where pk.ref = :ref and pk.userRef = :userRef")
    int deactivatePublicKey(@Param("ref") String ref, @Param("userRef") String userRef);

    @Transactional
    @Modifying
    @Query("Update PublicKey pk set pk.activated = true where pk.ref = :ref and pk.userRef = :userRef")
    int activatePublicKey(@Param("ref") String ref, @Param("userRef") String userRef);

    @Transactional
    @Modifying
    @Query("Update PublicKey pk set pk.activated = true where pk.userRef = :userRef")
    int activateAllPublicKeysForUserRef(@Param("userRef") String ref);

    @Transactional
    @Modifying
    @Query("Update PublicKey pk set pk.activated = false where pk.userRef = :userRef")
    int deactivateAllPublicKeysForUserRef(@Param("userRef") String ref);

}

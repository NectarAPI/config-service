package ke.co.nectar.config.repository;

import ke.co.nectar.config.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Long> {

    Config findByRef(String ref);

    Config findByRefAndUserRef(String ref, String userRef);

    List<Config> findByUserRef(String userRef);

    @Transactional
    @Modifying
    @Query("Update Configs c set c.activated = false where c.ref=:ref")
    int deactivateConfig(@Param("ref") String ref);

    @Transactional
    @Modifying
    @Query("Update Configs c set c.activated = true where c.ref=:ref")
    int activateConfig(@Param("ref") String ref);

}

package ke.co.nectar.config.repository;

import ke.co.nectar.config.entity.Config;
import ke.co.nectar.config.entity.NativeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NativeConfigRepository extends JpaRepository<NativeConfig, Long> {

    NativeConfig findByConfig(Config config);
}

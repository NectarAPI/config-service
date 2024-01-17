package ke.co.nectar.config.repository;

import ke.co.nectar.config.entity.Config;
import ke.co.nectar.config.entity.PrismThriftConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrismThriftConfigRepository extends JpaRepository<PrismThriftConfig, Long> {

    PrismThriftConfig findByConfig(Config config);
}

package ke.co.nectar.config.repository;

import ke.co.nectar.config.entity.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {

    List<Pricing> findAll();


}

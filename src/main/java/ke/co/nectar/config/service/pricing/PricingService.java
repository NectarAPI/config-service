package ke.co.nectar.config.service.pricing;

import ke.co.nectar.config.entity.Pricing;

import java.util.List;

public interface PricingService {

    List<Pricing> findAll() throws Exception;

    void add(Pricing pricing);
}

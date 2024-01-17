package ke.co.nectar.config.service.pricing.impl;

import ke.co.nectar.config.entity.Pricing;
import ke.co.nectar.config.repository.PricingRepository;
import ke.co.nectar.config.service.pricing.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Service
public class PricingServiceImpl implements PricingService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PricingRepository pricingRepository;

    @Override
    public List<Pricing> findAll() throws Exception {
        return pricingRepository.findAll();
    }

    @Override
    public void add(Pricing pricing) {
        pricingRepository.save(pricing);
    }
}

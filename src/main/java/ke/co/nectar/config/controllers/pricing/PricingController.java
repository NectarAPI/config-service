package ke.co.nectar.config.controllers.pricing;

import ke.co.nectar.config.constants.StringConstants;
import ke.co.nectar.config.controllers.pricing.exceptions.PricingNotFoundException;
import ke.co.nectar.config.entity.Pricing;
import ke.co.nectar.config.response.ApiResponse;
import ke.co.nectar.config.service.pricing.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class PricingController {

    @Autowired
    private PricingService pricingService;

    @GetMapping(value = "/pricing")
    public ApiResponse getPricing(@RequestParam(value = "request_id") @NotNull String requestId) {
        ApiResponse apiResponse;
        try {
            List<Pricing> pricing = pricingService.findAll();

            if (pricing.size() > 0) {
                Map<String, Object> output = new LinkedHashMap<>();
                output.put("pricing", pricing.get(0));
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                        StringConstants.SUCCESS_MSG_PRICING,
                        requestId,
                        output);
            } else {
                throw new PricingNotFoundException();
            }

        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.toString(),
                    requestId);
        }
        return apiResponse;
    }
}

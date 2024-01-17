package ke.co.nectar.config.controllers;

import ke.co.nectar.config.annotation.Notify;
import ke.co.nectar.config.constants.StringConstants;
import ke.co.nectar.config.entity.STSConfig;
import ke.co.nectar.config.response.ApiResponse;
import ke.co.nectar.config.service.config.ConfigService;
import ke.co.nectar.config.utils.EncryptedYAMLConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @GetMapping(value = "/config", params = "ref")
    public ApiResponse getConfig(@RequestParam(value = "request_id") @NotNull String requestId,
                                 @RequestParam(value = "ref") String ref,
                                 @RequestParam(value = "detailed", defaultValue = "false") boolean detailed) {
        ApiResponse apiResponse;
        try {
            if (ref != null && !ref.isBlank()) {
                STSConfig config = configService.find(ref, detailed);
                Map<String, Object> output = new LinkedHashMap<>();
                output.put("config", config);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                                StringConstants.SUCCESS_MSG_CONFIG_BY_REF,
                                                requestId,
                                                output);
            } else {
                apiResponse = new ApiResponse(StringConstants.BAD_REQUEST,
                                                StringConstants.EMPTY_REF_VALUE,
                                                requestId);
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.toString(),
                    requestId);
        }
        return apiResponse;
    }

    @GetMapping(value = "/config", params = { "ref","user_ref" })
    public ApiResponse getConfigByRefAndUserRef(@RequestParam(value = "request_id") @NotNull String requestId,
                                                @RequestParam(value = "ref") String ref,
                                                @RequestParam(value = "user_ref") String userRef,
                                                @RequestParam(value = "detailed", defaultValue = "false") boolean detailed) {
        ApiResponse apiResponse;
        try {
            if (ref != null && !ref.isBlank() &&
                    userRef != null && !userRef.isBlank()) {
                STSConfig config = configService.findByRefAndUserRef(ref, userRef, detailed);
                Map<String, Object> output = new LinkedHashMap<>();
                output.put("data", config);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                                StringConstants.SUCCESS_MSG_CONFIG_BY_REF,
                                                requestId,
                                                output);
            } else {
                apiResponse = new ApiResponse(StringConstants.BAD_REQUEST,
                                                StringConstants.EMPTY_REF_VALUE,
                                                requestId);
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.toString(),
                    requestId);
        }
        return apiResponse;
    }

    @GetMapping(value = "/config", params = "user_ref")
    public ApiResponse getConfigsByUserRef(@RequestParam(value = "request_id") @NotNull String requestId,
                                           @RequestParam(value = "user_ref", name = "user_ref") String userRef,
                                           @RequestParam(value = "detailed", defaultValue = "false") boolean detailed) {
        ApiResponse apiResponse;
        try {
            if (userRef != null && !userRef.isBlank()) {
                List<STSConfig> config = configService.findByUserRef(userRef, detailed);
                Map<String, Object> output = new LinkedHashMap<>();
                output.put("data", config);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                                StringConstants.SUCCESS_MSG_CONFIG_BY_USER_REF,
                                                requestId,
                                                output);
            } else {
                apiResponse = new ApiResponse(StringConstants.BAD_REQUEST,
                                                StringConstants.EMPTY_REF_VALUE,
                                                requestId);
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.toString(),
                    requestId);
        }
        return apiResponse;
    }

    @PostMapping("/config")
    @Notify(category = "ADD_CONFIG",
            description = "Added new config [Request-ID: {requestId}]")
    public ApiResponse addConfig(@RequestParam(value = "request_id") @NotNull String requestId,
                                 @RequestParam(value = "user_ref") @NotNull String userRef,
                                 @RequestBody @NotNull EncryptedYAMLConfig config) {
        try {
            STSConfig generatedConfig = configService.extractAndAdd(userRef, config);
            Map<String, Object> output = new LinkedHashMap<>();
            output.put("config", generatedConfig);
            return new ApiResponse(StringConstants.SUCCESS_CODE,
                                    StringConstants.SUCCESS_CONFIG_SAVED,
                                    requestId,
                                    output);
        } catch (Exception e) {
            return new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                                    e.getMessage(),
                                    requestId);
        }
    }

    @PutMapping("/config")
    @Notify(category = "ACTIVATE_CONFIG",
            description = "Activated config {ref} [Request-ID: {requestId}]")
    public ApiResponse activateConfig(@RequestParam(value = "request_id") @NotNull String requestId,
                                      @RequestParam(value = "user_ref") @NotNull String userRef,
                                      @RequestParam(value = "config_ref") @NotNull String ref) {
        ApiResponse apiResponse;
        try {
            boolean activated = configService.activateConfig(userRef, ref);
            if (activated) {
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                        StringConstants.ACTIVATED_MSG_CONFIG_BY_REF,
                        requestId);
            } else {
                apiResponse = new ApiResponse(StringConstants.INVALID_REQUEST,
                        StringConstants.INVALID_MSG_CONFIG_BY_REF,
                        requestId);
            }

        } catch (Exception e) {
            return new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
        return apiResponse;
    }

    @DeleteMapping("/config")
    @Notify(category = "DEACTIVATE_CONFIG",
            description = "Deactivated config {ref} [Request-ID: {requestId}]")
    public ApiResponse deactivateConfig(@RequestParam(value = "request_id") @NotNull String requestId,
                                        @RequestParam(value = "user_ref") @NotNull String userRef,
                                        @RequestParam(value = "config_ref") @NotNull  String ref) {
        ApiResponse apiResponse;
        try {
            if (ref != null && !ref.isBlank()) {
                boolean deactivated = configService.deactivateConfig(userRef, ref);

                if (deactivated) {
                    apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                            StringConstants.DELETED_MSG_CONFIGURATION_BY_REF,
                            requestId);
                } else {
                    apiResponse = new ApiResponse(StringConstants.INVALID_REQUEST,
                            StringConstants.INVALID_MSG_CONFIGURATION_BY_REF,
                            requestId);
                }

            } else {
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                        StringConstants.EMPTY_REF_VALUE,
                        requestId);
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
        return apiResponse;
    }
}
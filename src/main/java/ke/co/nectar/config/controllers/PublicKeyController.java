package ke.co.nectar.config.controllers;

import com.jayway.jsonpath.JsonPath;
import ke.co.nectar.config.annotation.Notify;
import ke.co.nectar.config.constants.StringConstants;
import ke.co.nectar.config.entity.PublicKey;
import ke.co.nectar.config.response.ApiResponse;
import ke.co.nectar.config.service.publickey.PublicKeyService;
import ke.co.nectar.config.utils.AsymmetricEncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class PublicKeyController {
    @Autowired
    private PublicKeyService publicKeyService;

    @GetMapping(value = "/public-key", params = "user_ref")
    public ApiResponse getPublicKeysForUser(@RequestParam(value = "request_id") @NotNull String requestId,
                                            @RequestParam(value = "user_ref") String userRef,
                                            @RequestParam(value = "activated", required = false)  boolean activated) {
        ApiResponse apiResponse;
        try {
            if (userRef != null && !userRef.isBlank()) {

                List<PublicKey> publicKeys;
                if (Boolean.TRUE.equals(activated))
                    publicKeys = publicKeyService.findByUserRefActivated(userRef);
                else
                    publicKeys = publicKeyService.findByUserRef(userRef);

                Map<String, Object> output = new LinkedHashMap<>();
                output.put("data", publicKeys);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                        String.format(StringConstants.SUCCESS_MSG_PUBLIC_KEYS_BY_USER_REF, userRef),
                        requestId,
                        output);
            } else {
                apiResponse = new ApiResponse(StringConstants.BAD_REQUEST,
                        StringConstants.EMPTY_USER_ID_VALUE,
                        requestId);
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.toString(),
                    requestId);
        }
        return apiResponse;
    }

    @GetMapping(value = "/public-key", params = "ref")
    public ApiResponse getPublicKeyByRef(@RequestParam(value = "request_id") @NotNull String requestId,
                                         @RequestParam(value = "ref") String ref) {
        ApiResponse apiResponse;
        try {
            if (ref != null && !ref.isBlank()) {
                PublicKey publicKey = publicKeyService.findByRef(ref);
                Map<String, Object> output = new LinkedHashMap<>();
                output.put("data", publicKey);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                        String.format(StringConstants.SUCCESS_MSG_PUBLIC_KEY_BY_REF, ref),
                        requestId,
                        output);
            } else {
                apiResponse = new ApiResponse(StringConstants.BAD_REQUEST,
                        StringConstants.EMPTY_USER_ID_VALUE,
                        requestId);
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.toString(),
                    requestId);
        }
        return apiResponse;
    }

    @PostMapping(value = "/public-key")
    @Notify(category = "ADD_PUBLIC_KEY",
            description = "Added Public Key [Request-ID: {requestId}]")
    public ApiResponse addPublicKey(@RequestParam(value = "request_id") @NotNull String requestId,
                                    @RequestParam(value = "user_ref") @NotNull String userRef,
                                    @RequestBody String payload) {
        try {
            String name = JsonPath.parse(payload).read("$.name");
            String publicKey = JsonPath.parse(payload).read("$.public_key");
            Boolean activated = JsonPath.parse(payload).read("$.activated");

            AsymmetricEncryptUtils.getPublicKeyFromString(publicKey);
            PublicKey createdPublicKey = publicKeyService.add(name, publicKey, userRef, activated);

            Map<String, Object> output = new LinkedHashMap<>();
            output.put("public_key", createdPublicKey);

            return new ApiResponse(StringConstants.SUCCESS_CODE,
                    StringConstants.SUCCESS_PUBLIC_KEY_SAVED,
                    requestId,
                    output);
        } catch (Exception e) {
            return new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
    }

    @PutMapping(value = "/public-key", params = {"request_id", "user_ref", "ref" })
    @Notify(category = "ACTIVATE_PUBLIC_KEY",
            description = "Activated Public Key {ref} [Request-ID: {requestId}]")
    public ApiResponse activatePublicKey(@RequestParam(value = "request_id") @NotNull String requestId,
                                         @RequestParam(value = "user_ref") @NotNull String userRef,
                                         @RequestParam(value = "ref") String ref) {
        ApiResponse apiResponse;
        try {
            if (ref != null && !ref.isBlank()) {
                boolean activated = publicKeyService.activatePublicKey(ref, userRef);

                if (activated) {
                    apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                            StringConstants.ACTIVATED_MSG_PUBLIC_KEY_BY_REF,
                            requestId);
                } else {
                    apiResponse = new ApiResponse(StringConstants.INVALID_REQUEST,
                            StringConstants.INVALID_MSG_PUBLIC_KEY_REF,
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

    @PutMapping(value = "/public-key", params = {"request_id", "user_ref"})
    @Notify(category = "ACTIVATE_PUBLIC_KEY",
            description = "Activated Public Keys for {userRef} [Request-ID: {requestId}]")
    public ApiResponse activatePublicKeysForUserRef(@RequestParam(value = "request_id") @NotNull String requestId,
                                                    @RequestParam(value = "user_ref") @NotNull String userRef) {
        ApiResponse apiResponse;
        try {
            if (userRef != null && !userRef.isBlank()) {
                boolean activated = publicKeyService.activateAllPublicKeysForUserRef(userRef);

                if (activated) {
                    apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                            String.format(StringConstants.ACTIVATED_MSG_PUBLIC_KEY_FOR_USER_REF, userRef),
                            requestId);
                } else {
                    apiResponse = new ApiResponse(StringConstants.INVALID_REQUEST,
                            StringConstants.INVALID_MSG_PUBLIC_KEY_REF,
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

    @DeleteMapping(value = "/public-key", params = {"request_id", "user_ref", "ref"})
    @Notify(category = "DEACTIVATE_PUBLIC_KEY",
            description = "Deactivated Public Key {ref} [Request-ID: {requestId}]")
    public ApiResponse deactivatePublicKeysForUserRef(@RequestParam(value = "request_id") @NotNull String requestId,
                                                      @RequestParam(value = "user_ref") @NotNull String userRef,
                                                      @RequestParam(value = "ref") String ref) {
        ApiResponse apiResponse;
        try {
            if (ref != null && !ref.isBlank()) {
                boolean deactivated = publicKeyService.deactivatePublicKey(ref, userRef);

                if (deactivated) {
                    apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                            StringConstants.DEACTIVATED_MSG_PUBLIC_KEY_BY_REF,
                            requestId);
                } else {
                    apiResponse = new ApiResponse(StringConstants.INVALID_REQUEST,
                            StringConstants.INVALID_MSG_PUBLIC_KEY_REF,
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

    @DeleteMapping(value = "/public-key", params = {"request_id", "user_ref"})
    @Notify(category = "DEACTIVATE_PUBLIC_KEY",
            description = "Deactivated Public Keys for user {userRef} [Request-ID: {requestId}]")
    public ApiResponse deactivatePublicKeys(@RequestParam(value = "request_id") @NotNull String requestId,
                                           @RequestParam(value = "user_ref") String userRef) {
        ApiResponse apiResponse;
        try {
            if (userRef != null && !userRef.isBlank()) {
                boolean deactivated = publicKeyService.deactivateAllPublicKeysForUserRef(userRef);

                if (deactivated) {
                    apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                            StringConstants.DEACTIVATED_MSG_PUBLIC_KEYS_BY_REF,
                            requestId);
                } else {
                    apiResponse = new ApiResponse(StringConstants.INVALID_REQUEST,
                            StringConstants.INVALID_MSG_PUBLIC_KEY_REF,
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

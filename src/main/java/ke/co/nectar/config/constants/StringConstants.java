package ke.co.nectar.config.constants;

public interface StringConstants {

    // -- Generic Error Codes
    int SUCCESS_CODE = 200;
    int BAD_REQUEST = 400;
    int INVALID_REQUEST = 405;
    int INTERNAL_SERVER_ERROR = 500;
    int EXCEPTION_CODE=600;
    int ALREADY_EXIST_CODE=700;
    int PHONE_NUMBER_MAX_LENGTH=10;

    // -- Specific Error Messages
    String EMPTY_REF_VALUE="Ref value should not be empty";
    String EMPTY_USER_NAME_VALUE="User Name should not be empty";
    String SUCCESS_MSG_CONFIG_BY_REF ="Obtained config";
    String SUCCESS_MSG_CONFIG_BY_USER_REF ="Obtained configs";
    String INVALID_MSG_CONFIGURATION_BY_REF ="Invalid configuration ref";
    String DELETED_MSG_CONFIGURATION_BY_REF = "Successfully deactivated configuration";
    String ACTIVATED_MSG_CONFIG_BY_REF = "Successfully activated configuration";
    String INVALID_MSG_CONFIG_BY_REF = "Successfully deactivated configuration";
    String INVALID_CONFIG_REF = "Invalid configuration ref";
    String INVALID_CONFIG_FILE = "Invalid configuration file";
    String SUCCESS_MSG_PUBLIC_KEYS_BY_USER_REF = "Found public keys for user ref %s";
    String EMPTY_USER_ID_VALUE = "Missing user id";
    String SUCCESS_PUBLIC_KEY_SAVED = "Public key saved successfully";
    String ACTIVATED_MSG_PUBLIC_KEY_BY_REF = "Successfully activated public key";
    String ACTIVATED_MSG_PUBLIC_KEY_FOR_USER_REF = "Successfully activated public key for user ref %s";
    String INVALID_MSG_PUBLIC_KEY_REF = "Invalid public key ref";
    String DEACTIVATED_MSG_PUBLIC_KEY_BY_REF = "Successfully deactivated public key";
    String DEACTIVATED_MSG_PUBLIC_KEYS_BY_REF = "Successfully deactivated public keys";
    String NO_PUBLIC_KEY_BY_USER_ID = "No public key found for user id";
    String INVALID_PUBLIC_KEY_REF = "Invalid public key ref";
    String SUCCESS_MSG_PUBLIC_KEY_BY_REF = "Found public key with ref %s";
    String SUCCESS_CONFIG_SAVED = "Successfully saved config";
    String DIGEST_PAYLOAD_MISMATCH = "Calculated and payload digests do not match!";
    String EMAIL_ALREADY_EXIST_BY_USER = "Email already exist";
    String USER_NAME_ALREADY_EXIST_BY_USER = "User name already exist";
    String INVALID_USER_DETAILS = "Invalid user details";
    String INVALID_PHONE_NUMBER = "Invalid phone no";
    String INVALID_REF = "Invalid ref";
    String SUCCESS_USER_SAVED= "Successfully created user";
    String SUCCESS_USER_DETAILS= "Successfully obtained user details";
    String INVALID_USER_REF= "Invalid user ref";
    String SUCCESS_USER_UPDATE = "Successfully updated user details";
    String DELETED_MSG_USER_BY_REF = "Successfully deleted user";
    String ERROR_CONFIG_STARTING = "Error with config starting sequence";
    String SUCCESS_MSG_PRICING = "Obtained pricing";

}

package ke.co.nectar.config.utils;

import java.util.UUID;

public class AppUtils {

    public static String generateRef(){
        return UUID.randomUUID().toString();
    }
}


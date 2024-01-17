package ke.co.nectar.config.utils;

import ke.co.nectar.config.constants.StringConstants;
import ke.co.nectar.config.utils.exceptions.InvalidYamlFileException;

import java.util.HashMap;
import java.util.Map;


public class YamlUtils {

    public static Map<String, String> load(String yaml)
        throws InvalidYamlFileException {
        Map<String, String> configs = new HashMap<>();

        String lines[] = yaml.split("\\r?\\n");
        String [] currLineParams;
        if (lines[0].equals("---")) {
            for (int count = 1; count < lines.length; count++) {
                currLineParams = lines[count].split(":");
                configs.put(currLineParams[0].trim(), currLineParams[1].trim());
            }
        } else
            throw new InvalidYamlFileException(StringConstants.ERROR_CONFIG_STARTING);

        return configs;
    }
}

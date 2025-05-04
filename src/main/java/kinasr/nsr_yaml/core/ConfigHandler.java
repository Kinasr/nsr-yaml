package kinasr.nsr_yaml.core;

import kinasr.nsr_yaml.exception.InvalidKeyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * A singleton class that handles the configurations by reading data from YAML files.
 */
class ConfigHandler {
    private static ConfigHandler instance;
    private final YAMLReader reader;
    private final ConfigRecord<String> dateConfigDatePattern = new ConfigRecord<>("date-config.date-pattern");
    private final ConfigRecord<String> dateConfigTimePattern = new ConfigRecord<>("date-config.time-pattern");
    private final ConfigRecord<String> dateConfigDateTimePattern = new ConfigRecord<>("date-config.date-time-pattern");
    private final ConfigRecord<String> dateConfigZonedPattern = new ConfigRecord<>("date-config.zoned-date-time-pattern");
    private final ConfigRecord<List<String>> environments = new ConfigRecord<>("environments");

    /**
     * Constructor for ConfigHandler class.
     */
    private ConfigHandler() {
        var file = findConfigFile();
        this.reader = file != null ? YAML.read(file, false) : null;
    }

    /**
     * Returns the singleton instance of the ConfigHandler class.
     *
     * @return ConfigHandler The singleton instance of the ConfigHandler class.
     */
    protected static ConfigHandler getInstance() {
        if (instance == null) {
            instance = new ConfigHandler();
        }
        return instance;
    }

    /**
     * Returns the date pattern string for the date configuration.
     */
    protected Optional<String> getDateConfigDatePattern() {
        return fetchData(dateConfigDatePattern, key -> reader.get(key).asString());
    }

    /**
     * Returns the time pattern string for the date configuration.
     */
    protected Optional<String> getDateConfigTimePattern() {
        return fetchData(dateConfigTimePattern, key -> reader.get(key).asString());
    }

    /**
     * Returns the date and time pattern string for the date configuration.
     */
    protected Optional<String> getDateConfigDateTimePattern() {
        return fetchData(dateConfigDateTimePattern, key -> reader.get(key).asString());
    }

    /**
     * Returns the zoned date and time pattern string for the date configuration.
     */
    protected Optional<String> getDateConfigZonedPattern() {
        return fetchData(dateConfigZonedPattern, key -> reader.get(key).asString());
    }

    /**
     * Returns the list of environments.
     */
    protected Optional<List<String>> getEnvironments() {
        var propertyEnv = System.getProperty(Helper.NSR_ENV);
        var envInConfig = fetchData(environments, key -> reader.get(key).asList(String.class))
                .orElse(new ArrayList<>());

        if (propertyEnv != null) {
            envInConfig.remove(propertyEnv);
            envInConfig.add(0, propertyEnv);
        }

        return Optional.of(envInConfig);
    }
    
    /**
     * Searches for the configuration file in the specified root path using possible file names.
     */
    private String findConfigFile() {
        var rootPath = "src/main/resources/";
        var possibleNames = List.of("nsr_config.yaml", "nsr_config.yml", "config.yaml", "config.yml");

        for (String name : possibleNames) {
            var filePath = rootPath + name;
            if (Helper.validateFilePath(filePath) != null) {
                return filePath;
            }
        }

        return null;
    }
    
    /**
     * Attempts to fetch data for the specified configuration key, using the provided function
     * to read the value.
     */
    <T> Optional<T> fetchData(ConfigRecord<T> config, Function<String, T> read) {
        if (reader != null && !config.isFetched) {
            try {
                config.value = read.apply(config.key);
            } catch (InvalidKeyException ignore) {
                // Ignore if the key does not exist
            } finally {
                config.isFetched = true;
            }
        }

        return Optional.ofNullable(config.value);
    }


    /**
     * A utility class that keeps track of the configuration keys and their values.
     */
    static class ConfigRecord<T> {
        private final String key;
        private boolean isFetched = false;
        private T value;

        private ConfigRecord(String key) {
            this.key = key;
        }
    }
}

package nsr_yaml;

import exception.InvalidKeyException;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static nsr_yaml.Helper.isFileExist;

/**
 * <body>
 * <h1>ConfigHandler</h1>
 * <p>The ConfigHandler is a singleton class that handles the configurations of an application by reading the data from the YAML files.</p>
 * <br/>
 * <h2>Constructors</h2>
 * <ul>
 * <li><code>private ConfigHandler()</code> A private constructor that sets the reader to a new instance of YAMLReader if a config file exists, otherwise sets the reader to null.</li>
 * </ul>
 * <body/>
 */
class ConfigHandler {
    private static final ConfigHandler instance = new ConfigHandler();
    private final YAMLReader reader;
    private final ConfigRecord<String> dateConfigDatePattern = new ConfigRecord<>("date-config.date-pattern");
    private final ConfigRecord<String> dateConfigTimePattern = new ConfigRecord<>("date-config.time-pattern");
    private final ConfigRecord<String> dateConfigDateTimePattern =
            new ConfigRecord<>("date-config.date-time-pattern");
    private final ConfigRecord<String> dateConfigZonedPattern =
            new ConfigRecord<>("date-config.zoned-date-time-pattern");
    private final ConfigRecord<List<String>> environments = new ConfigRecord<>("environments");

    /**
     * Constructor for ConfigHandler class.
     * <p>
     * Initializes the reader field with a YAMLReader instance.
     */
    private ConfigHandler() {
        var file = findFileName();
        YAMLReader r = null;

        if (file != null)
            r = YAML.read(file, false);

        this.reader = r;
    }

    /**
     * Returns the singleton instance of the ConfigHandler class.
     *
     * @return ConfigHandler The singleton instance of the ConfigHandler class.
     */
    protected static ConfigHandler getInstance() {
        return instance;
    }

    /**
     * Returns the date pattern string for the date configuration.
     *
     * @return Optional<String> The date pattern string for the date configuration,
     * or an empty Optional if the value is not present.
     */
    protected Optional<String> getDateConfigDatePattern() {
        return fetchData(dateConfigDatePattern, key -> reader.get(key).asString());
    }

    /**
     * Returns the time pattern string for the date configuration.
     *
     * @return Optional<String> The time pattern string for the date configuration,
     * or an empty Optional if the value is not present.
     */
    protected Optional<String> getDateConfigTimePattern() {
        return fetchData(dateConfigTimePattern, key -> reader.get(key).asString());
    }

    /**
     * Returns the date and time pattern string for the date configuration.
     *
     * @return Optional<String> The date and time pattern string for the date
     * configuration, or an empty Optional if the value is not present.
     */
    protected Optional<String> getDateConfigDateTimePattern() {
        return fetchData(dateConfigDateTimePattern, key -> reader.get(key).asString());
    }

    /**
     * Returns the zoned date and time pattern string for the date configuration.
     *
     * @return Optional<String> The zoned date and time pattern string for the date
     * configuration, or an empty Optional if the value is not present.
     */
    protected Optional<String> getDateConfigZonedPattern() {
        return fetchData(dateConfigZonedPattern, key -> reader.get(key).asString());
    }

    /**
     * Returns the list of environments.
     *
     * @return Optional<List<String>> The list of environments, or an empty Optional
     * if the value is not present.
     */
    protected Optional<List<String>> getEnvironments() {
        return fetchData(environments, key -> reader.get(key).asList(String.class));
    }

    /**
     * Searches for the configuration file in the specified root path using possible file names.
     *
     * @return String The path to the configuration file if it is found, otherwise null.
     */
    private String findFileName() {
        var rootPath = "src/main/resources/";
        var possibleNames = List.of("nsr_config.yaml", "nsr_config.yml", "config.yaml", "config.yml");

        for (String name : possibleNames) {
            var filePath = rootPath + name;
            if (isFileExist(filePath) != null)
                return filePath;
        }

        return null;
    }

    /**
     * Attempts to fetch data for the specified configuration key, using the provided function
     * to read the value.
     *
     * @param config The configuration record to fetch.
     * @param read The function to read the value of the key.
     * @return Optional<T> The value of the key, or an empty Optional if the value is not present.
     * @param <T> type
     */
    private <T> Optional<T> fetchData(ConfigRecord<T> config, Function<String, T> read) {
        if (reader != null && !config.isFetched)
            try {
                config.value = read.apply(config.key);
            } catch (InvalidKeyException ignore) {
                // Ignore if the key is not exist
            } finally {
                config.isFetched = true;
            }

        return Optional.ofNullable(config.value);
    }

    /**
     * ConfigRecord
     * <p>
     * A utility class that keeps track of the configuration keys and their values.
     *
     * @param <T> The type of the configuration value.
     */
    static class ConfigRecord<T> {
        private final String key;
        private Boolean isFetched = false;
        private T value;

        private ConfigRecord(String key) {
            this.key = key;
        }
    }
}

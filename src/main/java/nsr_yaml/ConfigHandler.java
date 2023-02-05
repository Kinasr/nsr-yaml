package nsr_yaml;

import exception.InvalidKeyException;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static nsr_yaml.Helper.isFileExist;

class ConfigHandler {
    private static final ConfigHandler instance = new ConfigHandler();
    private final YAMLReader reader;

    private final ConfigRecord<String> dateConfigZoneID= new ConfigRecord<>("date-config.zone-id");
    private final ConfigRecord<String> dateConfigDatePattern = new ConfigRecord<>("date-config.date-pattern");
    private final ConfigRecord<String> dateConfigTimePattern = new ConfigRecord<>("date-config.time-pattern");
    private final ConfigRecord<String> dateConfigDateTimePattern =
            new ConfigRecord<>("date-config.date-time-pattern");
    private final ConfigRecord<String> dateConfigZonedPattern =
            new ConfigRecord<>("date-config.zoned-date-time-pattern");
    private final ConfigRecord<List<String>> environments = new ConfigRecord<>("environments");

    private ConfigHandler() {
        var file = findFileName();
        YAMLReader r = null;

        if (file != null)
            r = YAML.read(file, false);

        this.reader = r;
    }

    protected static ConfigHandler getInstance() {
        return instance;
    }

    protected Optional<String> getDateConfigZoneId() {
        return fetchData(dateConfigZoneID, key -> reader.get(key).asString());
    }

    protected Optional<String> getDateConfigDatePattern() {
        return fetchData(dateConfigDatePattern, key -> reader.get(key).asString());
    }

    protected Optional<String> getDateConfigTimePattern() {
        return fetchData(dateConfigTimePattern, key -> reader.get(key).asString());
    }

    protected Optional<String> getDateConfigDateTimePattern() {
        return fetchData(dateConfigDateTimePattern, key -> reader.get(key).asString());
    }

    protected Optional<String> getDateConfigZonedPattern() {
        return fetchData(dateConfigZonedPattern, key -> reader.get(key).asString());
    }

    protected Optional<List<String>> getEnvironments() {
        return fetchData(environments, key -> reader.get(key).asList(String.class));
    }

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

    private <T> Optional<T> fetchData(ConfigRecord<T> config, Function<String, T> read) {
        if (reader != null && !config.isFetched)
            try {
                config.value = read.apply(config.key);
            } catch (InvalidKeyException ignore) {
            } finally {
                config.isFetched = true;
            }

        return Optional.ofNullable(config.value);
    }

    static class ConfigRecord<T> {
        private final String key;
        private Boolean isFetched = false;
        private T value;

        private ConfigRecord(String key) {
            this.key = key;
        }
    }
}

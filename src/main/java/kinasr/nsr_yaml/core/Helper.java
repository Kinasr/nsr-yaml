package kinasr.nsr_yaml.core;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Helper class contains utility methods for validating file paths and applying
 * environment-specific configurations to maps. This class is designed to be
 * non-instantiable.
 */
class Helper {
    public static final String NSR_ENV = "NSR_ENV";
    private static final String ENV_KEY_PATTERN = ".+@.+";
    
    private Helper() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Validates if the specified file exists at the given file path.
     *
     * @param filePath The file path to validate.
     * @return String The file path if the file exists, null otherwise.
     */
    protected static String validateFilePath(String filePath) {
        var file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            return filePath;
        }
        return null;
    }
    
    /**
     * Apply environment-specific configurations to the map.
     *
     * @param map The map to apply environment configurations to.
     * @return The map with environment-specific configurations applied.
     */
    protected static Map<String, Object> applyEnvironmentVariables(Map<String, Object> map) {
        if (map == null) {
            return map;
        }
        
        Optional<List<String>> environments = ConfigHandler.getInstance().getEnvironments();
        if (environments.isEmpty()) {
            return map;
        }
        
        List<String> environmentList = environments.get();
        List<String> keysWithEnv = findKeysWithEnvironmentSuffix(map);
        
        HashSet<String> changedKeys = new HashSet<>();
        
        for (String environment : environmentList) {
            applyEnvironmentOverrides(map, keysWithEnv, changedKeys, environment);
        }
        
        return map;
    }
    
    /**
     * Finds all keys in the map that have an environment suffix.
     *
     * @param map The map to search in.
     * @return List of keys with environment suffixes.
     */
    private static List<String> findKeysWithEnvironmentSuffix(Map<String, Object> map) {
        return map.keySet()
                .stream()
                .filter(k -> k.matches(ENV_KEY_PATTERN))
                .collect(Collectors.toList());
    }
    
    /**
     * Applies environment-specific overrides to the configuration map.
     *
     * @param map The configuration map to modify.
     * @param keysWithEnv List of keys with environment suffixes.
     * @param changedKeys Set of keys that have already been processed.
     * @param environment The current environment to apply.
     */
    private static void applyEnvironmentOverrides(
            Map<String, Object> map, 
            List<String> keysWithEnv, 
            HashSet<String> changedKeys, 
            String environment) {
        
        String envSuffix = "@" + environment;
        
        for (String key : keysWithEnv) {
            if (!key.endsWith(envSuffix)) {
                continue;
            }
            
            String baseKey = key.replace(envSuffix, "");
            if (changedKeys.contains(baseKey)) {
                continue;
            }
            
            map.put(baseKey, map.get(key));
            map.remove(key);
            changedKeys.add(baseKey);
        }
    }
}
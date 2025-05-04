package kinasr.nsr_yaml.core;

import kinasr.nsr_yaml.exception.InvalidKeyException;

/**
 * Reads YAML data and converts it into usable objects.
 * This class serves as a bridge between raw YAML data and the application's 
 * domain objects by providing convenient access methods.
 */
public class YAMLReader {
    private final Object yamlData;
    private final ObjMapper mapper;

    /**
     * Constructs a YAMLReader with the specified YAML data and Object Mapper.
     *
     * @param yamlData The YAML data to be read.
     * @param mapper   The Object Mapper used to convert the YAML data.
     */
    protected YAMLReader(Object yamlData, ObjMapper mapper) {
        this.yamlData = yamlData;
        this.mapper = mapper;
    }

    /**
     * Returns a YAMLObj representing the value associated with the specified key in the YAML data.
     *
     * @param key The key associated with the desired value in the YAML data.
     * @return A YAMLObj representing the value associated with the specified key in the YAML data.
     * @throws InvalidKeyException if the specified key is null or empty.
     */
    public YAMLObj get(String key) {
        validateKey(key);
        return new YAMLObj(mapper.get(yamlData, key));
    }

    /**
     * Returns a YAMLObject representation of the YAML data.
     *
     * @return A YAMLObject representation of the YAML data.
     */
    public YAMLObject get() {
        return new YAMLObject(yamlData);
    }
    
    /**
     * Validates that the key is not null or empty.
     * 
     * @param key The key to validate
     * @throws InvalidKeyException if the key is null or empty
     */
    private void validateKey(String key) {
        if (isNullOrEmpty(key)) {
            throw new InvalidKeyException("Key can't be null or empty");
        }
    }
    
    /**
     * Checks if a string is null or empty.
     * 
     * @param str The string to check
     * @return true if the string is null or empty, false otherwise
     */
    private boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
package nsr_yaml;

import exception.InvalidKeyException;

/**
 * Class YAMLReader
 * The YAMLReader class is used to read YAML data and convert it into usable objects.
 * <p>
 * Constructor
 * <p>
 * YAMLReader(Object yamlData, ObjMapper mapper)
 * Constructs a YAMLReader with the specified YAML data and Object Mapper.
 * <p>
 * Parameters:
 * <p>
 * yamlData - The YAML data to be read.
 * mapper - The Object Mapper used to convert the YAML data.
 * <p>
 * Methods
 * <p>
 * YAMLObj get(String key)
 * Returns a YAMLObj representing the value associated with the specified key in the YAML data.
 * Parameters:
 * key - The key associated with the desired value in the YAML data.
 * Returns:
 * A YAMLObj representing the value associated with the specified key in the YAML data.
 * Throws:
 * InvalidKeyException - if the specified key is null or empty.
 * <p>
 * YAMLObject get()
 * Returns a YAMLObject representation of the YAML data.
 * Returns:
 * A YAMLObject representation of the YAML data.
 */
public class YAMLReader {
    private final Object data;
    private final ObjMapper mapper;

    /**
     * Constructs a YAMLReader with the specified YAML data and Object Mapper.
     *
     * @param yamlData The YAML data to be read.
     * @param mapper   The Object Mapper used to convert the YAML data.
     */
    protected YAMLReader(Object yamlData, ObjMapper mapper) {
        this.data = yamlData;
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
        if (key == null || key.isEmpty())
            throw new InvalidKeyException("Key can't be null or empty");

        return new YAMLObj(mapper.get(data, key));
    }

    /**
     * Returns a YAMLObject representation of the YAML data.
     *
     * @return A YAMLObject representation of the YAML data.
     */
    public YAMLObject get() {
        return new YAMLObject(data);
    }
}

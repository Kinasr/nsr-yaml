package kinasr.nsr_yaml.core;
import kinasr.nsr_yaml.exception.ParsingException;
import java.util.List;
import java.util.Map;

/**
 * Represents a YAMLObject that encapsulates a data value. 
 * Provides various methods to interpret the encapsulated data in different formats.
 */
public class YAMLObject {
    private final Object data;

    /**
     * Constructor for YAMLObject class.
     *
     * @param data The data to be stored in the YAMLObject.
     */
    protected YAMLObject(Object data) {
        this.data = data;
    }

    /**
     * Returns the data stored in the YAMLObject as an Object.
     *
     * @return The data stored in the YAMLObject as an Object.
     */
    public Object asObject() {
        return data;
    }

    /**
     * Returns the data stored in the YAMLObject as a String.
     *
     * @return The data stored in the YAMLObject as a String, using the Parser class.
     */
    public String asString() {
        return Parser.toString(data);
    }

    /**
     * Returns the data stored in the YAMLObject as a List of Objects.
     *
     * @return The data stored in the YAMLObject as a List of Objects.
     */
    public List<Object> asList() {
        return asList(Object.class);
    }

    /**
     * Returns the data stored in the YAMLObject as a List of a specified type.
     *
     * @param clazz The Class representing the type of objects to be returned.
     * @param <T>   object type
     * @return The data stored in the YAMLObject as a List of the specified type.
     */
    public <T> List<T> asList(Class<T> clazz) {
        validateClazz(clazz);
        return Parser.toList(data, clazz);
    }

    /**
     * Returns the data stored in the YAMLObject as a Map with String keys and Object values.
     *
     * @return The data stored in the YAMLObject as a Map with String keys and Object values.
     */
    public Map<String, Object> asMap() {
        return asMap(Object.class);
    }

    /**
     * Returns the data stored in the YAMLObject as a Map with String keys and values of a specified type.
     *
     * @param clazz The Class representing the type of values to be returned.
     * @param <T>   object type
     * @return The data stored in the YAMLObject as a Map with String keys and values of the specified type.
     */
    public <T> Map<String, T> asMap(Class<T> clazz) {
        validateClazz(clazz);
        return Parser.toMap(data, clazz);
    }

    /**
     * Returns the data stored in the YAMLObject as an instance of the specified type.
     *
     * @param clazz The Class representing the type of object to be returned.
     * @param <T>   object type
     * @return The data stored in the YAMLObject as an instance of the specified type.
     */
    public <T> T as(Class<T> clazz) {
        validateClazz(clazz);
        return Parser.to(data, clazz, null);
    }

    /**
     * Validates the specified Class to ensure it is supported for conversion.
     *
     * @param clazz The Class to be validated.
     * @param <T>   object type
     * @throws ParsingException if the specified Class is an interface other than List or Map,
     *                          if it is a Record, or if it is an Array of unsupported components.
     */
    private <T> void validateClazz(Class<T> clazz) {
        if (isUnsupportedInterface(clazz)) {
            throw new ParsingException("Interfaces can not be initialized");
        }
        
        if (clazz.isRecord()) {
            throw new ParsingException("Records are not supported");
        }
        
        if (clazz.isArray()) {
            validateClazz(clazz.getComponentType());
        }
    }
    
    /**
     * Checks if the given class is an interface that is not supported for conversion.
     * 
     * @param clazz The class to check
     * @param <T> The type of the class
     * @return true if the class is an unsupported interface, false otherwise
     */
    private <T> boolean isUnsupportedInterface(Class<T> clazz) {
        return clazz.isInterface() && 
               !clazz.isAssignableFrom(List.class) && 
               !clazz.isAssignableFrom(Map.class);
    }
}
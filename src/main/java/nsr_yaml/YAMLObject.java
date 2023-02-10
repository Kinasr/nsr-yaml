package nsr_yaml;

import exception.ParsingException;

import java.util.List;
import java.util.Map;

import static nsr_yaml.Parser.*;

/**
 * <body>
 * <h1>Class YAMLObject</h1>
 * <p>The YAMLObject class provides a convenient way to access and manipulate data stored in YAML format.
 * It contains various methods to convert the stored data into different data structures like Object, String, List, Map, etc.</p>
 * <h2>Fields</h2>
 * <ul>
 *   <li>
 *     <strong>data:</strong> final Object - Represents the data stored in the YAMLObject.
 *   </li>
 * </ul>
 * <br/>
 * <h2>Constructor</h2>
 * <ul>
 *   <li>
 *     <strong>YAMLObject(Object data)</strong> - Creates a new YAMLObject with the specified data.
 *   </li>
 * </ul>
 * <body/>
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
     * @return The data stored in the YAMLObject as a List of the specified type.
     * @param <T> object type
     */
    public <T> List<T> asList(Class<T> clazz) {
        validateClazz(clazz);
        return toList(data, clazz);
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
     * @return The data stored in the YAMLObject as a Map with String keys and values of the specified type.
     * @param <T> object type
     */
    public <T> Map<String, T> asMap(Class<T> clazz) {
        validateClazz(clazz);
        return toMap(data, clazz);
    }

    /**
     * Returns the data stored in the YAMLObject as an instance of the specified type.
     *
     * @param clazz The Class representing the type of object to be returned.
     * @return The data stored in the YAMLObject as an instance of the specified type.
     * @param <T> object type
     */
    public <T> T as(Class<T> clazz) {
        validateClazz(clazz);
        return to(data, clazz, null);
    }

    /**
     * Validates the specified Class to ensure it is supported for conversion.
     *
     * @param clazz The Class to be validated.
     * @throws ParsingException if the specified Class is an interface other than List or Map,
     *         if it is a Record, or if it is an Array of unsupported components.
     * @param <T> object type
     */
    private <T> void validateClazz(Class<T> clazz) {
        var errorMsg = "";
        if (clazz.isInterface() && !clazz.isAssignableFrom(List.class) &&
                !clazz.isAssignableFrom(Map.class))
            errorMsg = "Interfaces can not be initialized";
        else if (clazz.isRecord())
            errorMsg = "Records are not supported";
        else if (clazz.isArray())
            validateClazz(clazz.getComponentType());

        if (!errorMsg.isEmpty())
            throw new ParsingException(errorMsg);
    }
}

package nsr_yaml;

import exception.InvalidKeyException;

/**
 * <body>
 *     <h1>Class YAMLReader</h1>
 *     <p>The YAMLReader class is used to read YAML data and convert it into usable objects.</p>
 *     <h2>Constructor</h2>
 *     <h3>YAMLReader(Object yamlData, ObjMapper mapper)</h3>
 *     <p>
 *       Constructs a YAMLReader with the specified YAML data and Object Mapper.
 *     </p>
 *     <h3>Parameters:</h3>
 *     <ul>
 *       <li>
 *         <code>yamlData</code> - The YAML data to be read.
 *       </li>
 *       <li>
 *         <code>mapper</code> - The Object Mapper used to convert the YAML data.
 *       </li>
 *     </ul>
 *     <h2>Methods</h2>
 *     <h3>YAMLObj get(String key)</h3>
 *     <p>
 *       Returns a YAMLObj representing the value associated with the specified key in the YAML data.
 *     </p>
 *     <h3>Parameters:</h3>
 *     <ul>
 *       <li>
 *         <code>key</code> - The key associated with the desired value in the YAML data.
 *       </li>
 *     </ul>
 *     <h3>Returns:</h3>
 *     <p>A YAMLObj representing the value associated with the specified key in the YAML data.</p>
 *     <h3>Throws:</h3>
 *     <ul>
 *       <li>
 *         <code>InvalidKeyException</code> - if the specified key is null or empty.
 *       </li>
 *     </ul>
 *     <h3>YAMLObject get()</h3>
 *     <p>
 *       Returns a YAMLObject representation of the YAML data.
 *     </p>
 *     <h3>Returns:</h3>
 *     <p>A YAMLObject representation of the YAML data.</p>
 *   </body>
 */
public class YAMLReader {
    private final Object data;
    private final ObjMapper mapper;

    /**
     * Constructs a YAMLReader with the specified YAML data and Object Mapper.
     *
     * @param yamlData The YAML data to be read.
     * @param mapper The Object Mapper used to convert the YAML data.
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

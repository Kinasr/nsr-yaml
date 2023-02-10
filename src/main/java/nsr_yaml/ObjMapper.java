package nsr_yaml;

import exception.InvalidKeyException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static nsr_yaml.Parser.toList;
import static nsr_yaml.Parser.toMap;

/**
 * <body>
 * <h1>ObjMapper Class Documentation</h1>
 * <h2>Class Overview</h2>
 * <p>The ObjMapper class provides the functionality of mapping an object with the key.</p>
 * <br/>
 * <h2>Fields</h2>
 * <ul>
 *   <li>
 *     <b>KEY_CONTAINS_LIST_REGEX:</b> The regular expression pattern that matches if a key contains a list index.
 *   </li>
 *   <li>
 *     <b>NUMBER_IN_SQUARE_BRACKETS_REGEX:</b> The regular expression pattern that matches the number in square brackets.
 *   </li>
 *   <li>
 *     <b>SQUARE_BRACKETS_REGEX:</b> The regular expression pattern that matches the square brackets.
 *   </li>
 *   <li>
 *     <b>KEY_SEPARATOR_REGEX:</b> The regular expression pattern that matches the dot separator in the key.
 *   </li>
 *   <li>
 *     <b>changeEnv:</b> A boolean value that indicates if the environment change is enabled or not.
 *   </li>
 * </ul>
 * </body>
 */
class ObjMapper {
    private final static String KEY_CONTAINS_LIST_REGEX = "^.*(\\[\\d+])+$";
    private final static String NUMBER_IN_SQUARE_BRACKETS_REGEX = "\\[\\d+]";
    private final static String SQUARE_BRACKETS_REGEX = "[\\[\\]]";
    private final static String KEY_SEPARATOR_REGEX = "\\.";

    private final Boolean changeEnv;

    /**
     * Constructor for the `ObjMapper` class.
     *
     * @param changeEnv A boolean value indicating whether the environment should be changed.
     */
    protected ObjMapper(Boolean changeEnv) {
        this.changeEnv = changeEnv;
    }

    /**
     * Retrieve the value of an object based on its key.
     *
     * @param obj The object to retrieve the value from.
     * @param key The key for the value to retrieve.
     * @return The value of the object for the specified key.
     */
    protected Object get(Object obj, String key) {
        var keys = splitKey(key);

        for (String k : keys) {
            if (isList(k))
                obj = getObjFromList(obj, k);
            else
                obj = getObjFromMap(obj, k);
        }

        return obj;
    }

    /**
     * Retrieve an object from a list.
     *
     * @param obj The list to retrieve the object from.
     * @param key The key for the object to retrieve.
     * @return The object for the specified key.
     * @throws InvalidKeyException If the specified index is out of the boundary of the list.
     */
    private Object getObjFromList(Object obj, String key) {
        var indexes = getIndexesFromKeyList(key);

        key = key.replaceAll(NUMBER_IN_SQUARE_BRACKETS_REGEX, "");

        if (!key.isEmpty())
            obj = getObjFromMap(obj, key);

        for (Integer index : indexes) {
            var list = toList(obj, Object.class);

            if (index >= list.size())
                throw new InvalidKeyException("This index [" + index + "] is out of the boundary of [" + list + "]");

            obj = list.get(index);
        }

        return obj;
    }

    /**
     * Retrieve an object from a map.
     *
     * @param obj The map to retrieve the object from.
     * @param key The key for the object to retrieve.
     * @return The object for the specified key.
     * @throws InvalidKeyException If the specified key does not exist in the map.
     */
    private Object getObjFromMap(Object obj, String key) {
        var m = toMap(obj, Object.class);
        var map = changeEnv ? changeEnv(m) : m;

        if (!map.containsKey(key))
            throw new InvalidKeyException("This key [" + key + "] does not exist in [" + map + "]");

        return map.get(key);
    }

    /**
     * Retrieve the indexes from a list key.
     *
     * @param key The list key to retrieve the indexes from.
     * @return A list of integers representing the indexes.
     */
    private List<Integer> getIndexesFromKeyList(String key) {
        var indexes = new ArrayList<Integer>();

        var matcher = Pattern.compile(NUMBER_IN_SQUARE_BRACKETS_REGEX).matcher(key);

        while (matcher.find()) {
            indexes.add(Integer.parseInt(
                    matcher
                            .group()
                            .replaceAll(SQUARE_BRACKETS_REGEX, "")
            ));
        }

        return indexes;
    }

    /**
     * Change the environment of the specified map.
     *
     * @param map The map to change the environment for.
     * @return The map with its environment changed.
     */
    private Map<String, Object> changeEnv(Map<String, Object> map) {
        var environments = ConfigHandler.getInstance().getEnvironments();

        if (map == null || environments.isEmpty() || !changeEnv)
            return map;

        var keysWithEnv = map.keySet()
                .stream()
                .filter(k -> k.matches(".+@.+"))
                .toList();

        environments.get().forEach(
                environment -> keysWithEnv.forEach(
                        key -> {
                            var env = "@" + environment;
                            if (key.endsWith(env)) {
                                var newKey = key.replace(env, "");
                                if (!map.containsKey(newKey)) {
                                    map.put(newKey, map.get(key));
                                    map.remove(key);
                                }
                            }
                        }
                )
        );

        return map;
    }

    /**
     * Split the specified key into a list of keys.
     *
     * @param key The key to split.
     * @return The list of keys resulting from splitting the specified key.
     */
    private List<String> splitKey(String key) {
        return Arrays.stream(key.split(KEY_SEPARATOR_REGEX)).toList();
    }

    /**
     * Determine if the specified key contains a list.
     *
     * @param key The key to check.
     * @return True if the specified key contains a list, false otherwise.
     */
    private Boolean isList(String key) {
        return key.matches(KEY_CONTAINS_LIST_REGEX);
    }
}

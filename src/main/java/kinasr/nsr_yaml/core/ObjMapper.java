package kinasr.nsr_yaml.core;

import kinasr.nsr_yaml.exception.InvalidKeyException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Class that provides methods for mapping objects with keys.
 */
class ObjMapper {
    private static final String KEY_CONTAINS_LIST_REGEX = "^.*[\\[\\d+]]+$";
    private static final String NUMBER_IN_SQUARE_BRACKETS_REGEX = "\\[\\d+]";
    private static final String SQUARE_BRACKETS_REGEX = "[\\[\\]]";
    private static final String KEY_SEPARATOR_REGEX = "\\.";

    private final Boolean shouldApplyEnvironmentVariables;

    /**
     * Constructor for the ObjMapper class.
     *
     * @param shouldApplyEnvironmentVariables Whether to apply environment variables to maps.
     */
    protected ObjMapper(Boolean shouldApplyEnvironmentVariables) {
        this.shouldApplyEnvironmentVariables = shouldApplyEnvironmentVariables;
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
            obj = isList(k) ? getObjFromList(obj, k) : getObjFromMap(obj, k);
        }

        return obj;
    }

    /**
     * Retrieve an object from a list.
     */
    private Object getObjFromList(Object obj, String key) {
        var indexes = getIndexesFromKeyList(key);
        key = key.replaceAll(NUMBER_IN_SQUARE_BRACKETS_REGEX, "");

        if (!key.isEmpty()) {
            obj = getObjFromMap(obj, key);
        }

        for (Integer index : indexes) {
            var list = Parser.toList(obj, Object.class);

            if (index >= list.size()) {
                throw new InvalidKeyException("This index [" + index + "] is out of the boundary of [" + list + "]");
            }

            obj = list.get(index);
        }

        return obj;
    }

    /**
     * Retrieve an object from a map.
     */
    private Object getObjFromMap(Object obj, String key) {
        var map = Parser.toMap(obj, Object.class);

        if (shouldApplyEnvironmentVariables) {
            map = Helper.applyEnvironmentVariables(map);
        }

        if (!map.containsKey(key)) {
            throw new InvalidKeyException("This key [" + key + "] does not exist in [" + map + "]");
        }

        return map.get(key);
    }

    /**
     * Retrieve the indexes from a list key.
     */
    private List<Integer> getIndexesFromKeyList(String key) {
        var indexes = new ArrayList<Integer>();
        var matcher = Pattern.compile(NUMBER_IN_SQUARE_BRACKETS_REGEX).matcher(key);

        while (matcher.find()) {
            indexes.add(Integer.parseInt(
                    matcher.group().replaceAll(SQUARE_BRACKETS_REGEX, "")
            ));
        }

        return indexes;
    }

    /**
     * Split the specified key into a list of keys.
     */
    private List<String> splitKey(String key) {
        return Arrays.stream(key.split(KEY_SEPARATOR_REGEX)).toList();
    }

    /**
     * Determine if the specified key contains a list.
     */
    private boolean isList(String key) {
        return key.matches(KEY_CONTAINS_LIST_REGEX);
    }
}

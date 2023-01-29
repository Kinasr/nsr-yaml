package nsr_yaml;

import exception.InvalidKeyException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static nsr_yaml.Parser.*;

class ObjMapper {
    private final static String KEY_CONTAINS_LIST_REGEX = "^.*(\\[\\d+])+$";
    private final static String NUMBER_IN_SQUARE_BRACKETS_REGEX = "\\[\\d+]";
    private final static String SQUARE_BRACKETS_REGEX = "[\\[\\]]";
    private final static String KEY_SEPARATOR_REGEX = "\\.";

    private final Boolean changeEnv;

    protected ObjMapper(Boolean changeEnv) {
        this.changeEnv = changeEnv;
    }

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

    private Object getObjFromMap(Object obj, String key) {
        var m = toMap(obj, Object.class);
        var map = changeEnv ? changeEnv(m) : m;

        if (!map.containsKey(key))
            throw new InvalidKeyException("This key [" + key + "] does not exist in [" + map + "]");

        return map.get(key);
    }

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

    private List<String> splitKey(String key) {
        return Arrays.stream(key.split(KEY_SEPARATOR_REGEX)).toList();
    }

    private Boolean isList(String key) {
        return key.matches(KEY_CONTAINS_LIST_REGEX);
    }
}

package nsr_yaml;

import exception.ParsingException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static nsr_yaml.Parser.*;

public class YAMLObject {
    private final Object data;

    protected YAMLObject(Object data) {
        this.data = data;
    }

    public Object asObject() {
        return data;
    }

    public String asString() {
        return Parser.toString(data);
    }

    public List<Object> asList() {
        return asList(Object.class);
    }

    public <T> List<T> asList(Class<T> clazz) {
        if (clazz.isArray())
            throw new ParsingException("Please use asArray method instead");

        return toList(data, clazz);
    }


    public Map<String, Object> asMap() {
        return asMap(Object.class);
    }

    public <T> Map<String, T> asMap(Class<T> clazz) {
        if (clazz.isArray())
            throw new ParsingException("Please use asArray method instead");

        return toMap(data, clazz);
    }

    public <T> T as(Class<T> clazz) {
        if (clazz.isArray())
            throw new ParsingException("Please use asArray method instead");

        return to(data, clazz, null);
    }

    public <T> T[] asArray(Class<T[]> clazz) {
        var coreClazz = clazz.getComponentType();
        var arr = toList(data, coreClazz).toArray();

        return Arrays.copyOf(arr, arr.length, clazz);
    }
}

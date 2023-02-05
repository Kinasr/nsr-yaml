package nsr_yaml;

import exception.ParsingException;

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
        validateClazz(clazz);
        return toList(data, clazz);
    }


    public Map<String, Object> asMap() {
        return asMap(Object.class);
    }

    public <T> Map<String, T> asMap(Class<T> clazz) {
        validateClazz(clazz);
        return toMap(data, clazz);
    }

    public <T> T as(Class<T> clazz) {
        validateClazz(clazz);
        return to(data, clazz, null);
    }

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

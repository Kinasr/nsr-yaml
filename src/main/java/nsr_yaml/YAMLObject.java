package nsr_yaml;

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
        return objToString(data);
    }

    public List<Object> asList() {
        return asList(Object.class);
    }

    public <T> List<T> asList(Class<T> clazz) {
        return toList(data, clazz);
    }


    public Map<String, Object> asMap() {
        return asMap(Object.class);
    }

    public <T> Map<String, T> asMap(Class<T> clazz) {
        return toMap(data, clazz);
    }

    public <T> T as(Class<T> clazz) {
        var asObj = to(data, clazz, null);

        return asObj.isCustomObj() ? toCustomObj(data, asObj.obj()) : asObj.obj();
    }
}

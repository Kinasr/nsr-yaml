package nsr_yaml;

import exception.InvalidKeyException;

public class YAMLReader {
    private final Object data;
    private final ObjMapper mapper;

    protected YAMLReader(Object yamlData, ObjMapper mapper) {
        this.data = yamlData;
        this.mapper = mapper;
    }

    public YAMLObject get(String key) {
        if (key == null || key.isEmpty())
            throw new InvalidKeyException("Key can't be null or empty");

        return new YAMLObject(mapper.get(data, key));
    }

    public YAMLObject get() {
        return new YAMLObject(data);
    }
}

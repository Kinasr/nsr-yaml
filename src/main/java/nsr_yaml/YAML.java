package nsr_yaml;

import exception.YAMLFileException;

public class YAML {

    public static YAMLReader read(String filePath) {
        return read(filePath, true);
    }

    protected static YAMLReader read(String filePath, Boolean changeEnv) {
        if (filePath == null || filePath.isEmpty() || filePath.isBlank())
            throw new YAMLFileException("File path can't be null or empty");

        return new YAMLReader(YAMLFileLoader.load(filePath), new ObjMapper(changeEnv));
    }
}

package nsr_yaml;

import exception.YAMLFileException;

/**
 * The entry class
 */
public class YAML {

    /**
     * Reading a YAML file
     * @param filePath the relative file path
     * @return new instance of YAMLReader class
     */
    public static YAMLReader read(String filePath) {
        return read(filePath, true);
    }

    /**
     * Reading a YAML file with availability to disable environment change (for internal use)
     * @param filePath the relative file path
     * @param changeEnv to disable environment change
     * @return new instance of YAMLReader class
     */
    protected static YAMLReader read(String filePath, Boolean changeEnv) {
        if (filePath == null || filePath.isEmpty() || filePath.isBlank())
            throw new YAMLFileException("File path can't be null or empty");

        return new YAMLReader(YAMLFileLoader.load(filePath), new ObjMapper(changeEnv));
    }
}

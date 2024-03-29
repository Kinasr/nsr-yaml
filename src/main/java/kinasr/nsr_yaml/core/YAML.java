package kinasr.nsr_yaml.core;

import kinasr.nsr_yaml.exception.YAMLFileException;

/**
 * YAML Class
 * This class provides methods for reading a YAML file and returning a YAMLReader instance.
 */
public class YAML {

    private YAML() {
    }

    /**
     * Reads a YAML file and returns a `YAMLReader` instance.
     *
     * @param filePath The file path of the YAML file.
     * @return A `YAMLReader` instance representing the contents of the YAML file.
     */
    public static YAMLReader read(String filePath) {
        return read(filePath, true);
    }

    /**
     * Reads a YAML file and returns a `YAMLReader` instance.
     *
     * @param filePath  The file path of the YAML file.
     * @param changeEnv Whether to perform environment variable substitution on the file contents.
     * @return A `YAMLReader` instance representing the contents of the YAML file.
     * @throws YAMLFileException If the file path is `null`, empty, or blank.
     */
    protected static YAMLReader read(String filePath, Boolean changeEnv) {
        if (filePath == null || filePath.isEmpty() || filePath.isBlank())
            throw new YAMLFileException("File path can't be null or empty");

        var fileData = YAMLFileLoader.load(filePath);
        if (fileData == null)
            throw new YAMLFileException("Can not read empty file at path: " + filePath);

        return new YAMLReader(YAMLFileLoader.load(filePath), new ObjMapper(changeEnv));
    }
}

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
     * Environment variable substitution is enabled by default.
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
     * @param filePath                       The file path of the YAML file.
     * @param substituteEnvironmentVariables Whether to perform environment variable substitution on the file contents.
     * @return A `YAMLReader` instance representing the contents of the YAML file.
     * @throws YAMLFileException If the file path is `null`, empty, or blank.
     */
    public static YAMLReader read(String filePath, boolean substituteEnvironmentVariables) {
        validateFilePath(filePath);

        var fileData = YAMLFileLoader.load(filePath);
        if (fileData == null) {
            throw new YAMLFileException("Cannot read empty file at path: " + filePath);
        }

        return new YAMLReader(fileData, new ObjMapper(substituteEnvironmentVariables));
    }

    /**
     * Validates that the file path is not null or blank.
     */
    private static void validateFilePath(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new YAMLFileException("File path can't be null or empty");
        }
    }
}
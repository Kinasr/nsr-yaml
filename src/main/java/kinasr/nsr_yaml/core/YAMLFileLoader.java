package kinasr.nsr_yaml.core;

import kinasr.nsr_yaml.exception.YAMLFileException;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class YAMLFileLoader
 * <p>
 * This class provides a way to load data from a YAML file. The loaded data will be stored in a hash map so that
 * future requests for the same file can be served from the hash map without having to load the file again.
 */
public class YAMLFileLoader {
    private static final Map<String, Object> LOADED_FILES = new HashMap<>();
    private static final String YAML_FILE_PATTERN = ".*\\.(yaml|yml)$";
    
    private final String filePath;
    private final Object data;

    /**
     * Constructs a new YAMLFileLoader object.
     *
     * @param filePath the file path of the YAML file
     */
    private YAMLFileLoader(String filePath) {
        this.filePath = filePath;
        this.data = parseYamlFile();
    }

    /**
     * Loads the data from a YAML file specified by the file path.
     *
     * @param filePath the file path of the YAML file
     * @return the loaded data in the form of an Object
     */
    protected static Object load(String filePath) {
        if (LOADED_FILES.containsKey(filePath)) {
            return LOADED_FILES.get(filePath);
        }

        validateFileExtension(filePath);

        Object newData = new YAMLFileLoader(filePath).data;
        LOADED_FILES.put(filePath, newData);
        return newData;
    }

    /**
     * Validates if the file path has a supported extension.
     *
     * @param filePath the file path of the YAML file
     * @throws YAMLFileException if the file path has an unsupported extension
     */
    private static void validateFileExtension(String filePath) {
        if (!filePath.matches(YAML_FILE_PATTERN)) {
            throw new YAMLFileException(".yaml and .yml are the only supported extensions");
        }
    }

    /**
     * Parses the YAML file and returns its content.
     *
     * @return the loaded data
     */
    private Object parseYamlFile() {
        try (FileInputStream fileStream = new FileInputStream(filePath)) {
            return new Yaml().load(fileStream);
        } catch (FileNotFoundException e) {
            throw new YAMLFileException("Can't find this file [" + filePath + "]", e);
        } catch (IOException e) {
            throw new YAMLFileException("Error reading YAML file: " + filePath, e);
        }
    }
}
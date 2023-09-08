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
 * The class has a private constructor and a protected static method "load" that returns the loaded data as an Object.
 * The class also has several private methods that handle the reading and parsing of the YAML file.
 * <p>
 * Constructor
 * private YAMLFileLoader(String filePath) Constructs a new YAMLFileLoader object.
 * <p>
 * Parameters
 * filePath - the file path of the YAML file
 */
public class YAMLFileLoader {
    private static final Map<String, Object> loadedFiles = new HashMap<>();
    private final String filePath;
    private final Object data;

    /**
     * Constructs a new YAMLFileLoader object.
     *
     * @param filePath the file path of the YAML file
     */
    private YAMLFileLoader(String filePath) {
        this.filePath = filePath;
        this.data = loadData();
    }

    /**
     * Loads the data from a YAML file specified by the file path.
     *
     * @param filePath the file path of the YAML file
     * @return the loaded data in the form of an Object
     */
    protected static Object load(String filePath) {
        if (loadedFiles.containsKey(filePath))
            return loadedFiles.get(filePath);

        checkFilePath(filePath);

        var newData = new YAMLFileLoader(filePath).data;
        loadedFiles.put(filePath, newData);
        return newData;
    }

    /**
     * Checks if the file path has a supported extension.
     *
     * @param filePath the file path of the YAML file
     * @throws YAMLFileException if the file path has an unsupported extension
     */
    private static void checkFilePath(String filePath) {
        if (!filePath.matches(".*.yaml$") && !filePath.matches(".*.yml$"))
            throw new YAMLFileException(".yaml and .yml are the only supported extensions");
    }

    /**
     * Loads the data from the YAML file.
     *
     * @return the loaded data in the form of an Object
     */
    private Object loadData() {
        var file = this.loadFile();
        var d = new Yaml().load(file);

        this.closeFile(file);
        return d;
    }

    /**
     * Loads the YAML file specified by the file path.
     *
     * @return a FileInputStream representing the loaded YAML file
     * @throws YAMLFileException if the file can't be found
     */
    private FileInputStream loadFile() {
        try {
            return new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            throw new YAMLFileException("Can't find this file [" + filePath + "]", e);
        }
    }

    /**
     * Closes the specified FileInputStream.
     *
     * @param in the FileInputStream to be closed
     * @throws YAMLFileException if there is an error closing the FileInputStream
     */
    private void closeFile(FileInputStream in) {
        try {
            in.close();
        } catch (IOException e) {
            throw new YAMLFileException(e);
        }
    }
}

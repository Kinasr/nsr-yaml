package nsr_yaml;

import exception.YAMLFileException;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class YAMLFileLoader {
    private final static Map<String, Object> loadedFiles = new HashMap<>();
    private final String filePath;
    private final Object data;

    private YAMLFileLoader(String filePath) {
        this.filePath = filePath;
        this.data = loadData();
    }

    public static Object load(String filePath) {
        if (loadedFiles.containsKey(filePath))
            return loadedFiles.get(filePath);

        checkFilePath(filePath);

        var newData = new YAMLFileLoader(filePath).data;
        loadedFiles.put(filePath, newData);
        return newData;
    }

    private static void checkFilePath(String filePath) {
        if (!filePath.matches(".*.yaml$") && !filePath.matches(".*.yml$"))
            throw new YAMLFileException(".yaml and .yml are the only supported extensions");
    }

    private Object loadData() {
        var file = this.loadFile();
        var d = new Yaml().load(file);

        this.closeFile(file);
        return d;
    }

    private FileInputStream loadFile() {
        try {
            return new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            throw new YAMLFileException("Can't find this file [" + filePath + "]", e);
        }
    }

    private void closeFile(FileInputStream in) {
        try {
            in.close();
        } catch (IOException e) {
            throw new YAMLFileException(e);
        }
    }
}

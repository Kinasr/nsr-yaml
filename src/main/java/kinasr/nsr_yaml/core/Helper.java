package kinasr.nsr_yaml.core;

import java.io.File;
import java.util.Map;

/**
 * <body>
 * <h1>Helper Class</h1>
 * <p>The Helper class contains a method that checks if a specified file exists at a given file path.</p>
 * <br/>
 * <h2>Constructor</h2>
 * <pre>
 *     private Helper() {}
 *     </pre>
 * <p>The constructor is private and not accessible from outside the class, making the Helper class a singleton.</p>
 * </body>
 */
class Helper {

    private Helper() {
    }

    /**
     * Checks if the specified file exists at the given file path.
     *
     * @param filePath The file path.
     * @return String The file path if the file exists, null otherwise.
     */
    protected static String isFileExist(String filePath) {
        var f = new File(filePath);
        if (f.exists() && !f.isDirectory())
            return filePath;

        return null;
    }

    /**
     * Change the environment of the specified map.
     *
     * @param map The map to change the environment for.
     * @return The map with its environment changed.
     */
    protected static Map<String, Object> changeEnv(Map<String, Object> map) {
        var environments = ConfigHandler.getInstance().getEnvironments();

        if (map == null || environments.isEmpty())
            return map;

        var keysWithEnv = map.keySet()
                .stream()
                .filter(k -> k.matches(".+@.+"))
                .toList();

        environments.get().forEach(
                environment -> keysWithEnv.forEach(
                        key -> {
                            var env = "@" + environment;
                            if (key.endsWith(env)) {
                                var newKey = key.replace(env, "");
                                if (!map.containsKey(newKey)) {
                                    map.put(newKey, map.get(key));
                                    map.remove(key);
                                }
                            }
                        }
                )
        );

        return map;
    }
}

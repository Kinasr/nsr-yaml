package nsr_yaml;

import java.io.File;

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
}

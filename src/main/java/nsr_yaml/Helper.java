package nsr_yaml;

import java.io.File;

class Helper {

    private Helper() {}

    protected static String isFileExist(String filePath) {
        var f = new File(filePath);
        if (f.exists() && !f.isDirectory())
            return filePath;

        return null;
    }
}

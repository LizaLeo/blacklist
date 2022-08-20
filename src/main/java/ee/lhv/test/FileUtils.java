package ee.lhv.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileUtils {

    private static final String FILE_PATH = ".\\";

    FileUtils() {
    }

    public static InputStream getFile(String fileName) {
        String fileUrl = FILE_PATH + fileName;
        InputStream is = getFileFromPath(fileUrl);
        return is == null ? getFileFromPath(fileName) : is;
    }

    public static InputStream getFileFromPath(String fileName) {
        try {
            return new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found! " + fileName);
        } else {
            return inputStream;
        }
    }

}

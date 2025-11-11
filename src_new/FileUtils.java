package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtils {
    public static byte[] readFileToBytes(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        int read = 0;
        while (read < data.length) {
            int r = fis.read(data, read, data.length - read);
            if (r < 0) break;
            read += r;
        }
        fis.close();
        return data;
    }
}

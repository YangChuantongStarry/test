package org.elastos.dma.base.util.ela;




import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author hb.nie
 * @description
 */
class FileUtils {
    public static String readFileToString(File file, String encoding) throws IOException {
        InputStream in = openInputStream(file);
        Throwable var3 = null;

        String var4;
        try {
            var4 = IOUtils.toString(in, Charset.forName(encoding));
        } catch (Throwable var13) {
            var3 = var13;
            throw var13;
        } finally {
            if (in != null) {
                if (var3 != null) {
                    try {
                        in.close();
                    } catch (Throwable var12) {
                        var3.addSuppressed(var12);
                    }
                } else {
                    in.close();
                }
            }

        }

        return var4;
    }

    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            } else if (!file.canRead()) {
                throw new IOException("File '" + file + "' cannot be read");
            } else {
                return new FileInputStream(file);
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
    }
}

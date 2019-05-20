package org.elastos.dma.base.util.ela;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * @author hb.nie
 * @description
 */
class IOUtils {
    public static String toString(InputStream input, Charset encoding) throws IOException {
        StringBuilderWriter sw = new StringBuilderWriter();
        Throwable var3 = null;

        String var4;
        try {
            copy((InputStream) input, (Writer) sw, (Charset) encoding);
            var4 = sw.toString();
        } catch (Throwable var13) {
            var3 = var13;
            throw var13;
        } finally {
            if (sw != null) {
                if (var3 != null) {
                    try {
                        sw.close();
                    } catch (Throwable var12) {
                        var3.addSuppressed(var12);
                    }
                } else {
                    sw.close();
                }
            }

        }

        return var4;
    }

    public static void copy(InputStream input, Writer output, Charset inputEncoding) throws IOException {
        InputStreamReader in = new InputStreamReader(input,inputEncoding);
        copy((Reader) in, (Writer) output);
    }


    public static int copy(Reader input, Writer output) throws IOException {
        long count = copyLarge(input, output);
        return count > 2147483647L ? -1 : (int) count;
    }

    public static long copyLarge(Reader input, Writer output) throws IOException {
        return copyLarge(input, output, new char[4096]);
    }

    public static long copyLarge(Reader input, Writer output, char[] buffer) throws IOException {
        long count;
        int n;
        for (count = 0L; -1 != (n = input.read(buffer)); count += (long) n) {
            output.write(buffer, 0, n);
        }

        return count;
    }

}

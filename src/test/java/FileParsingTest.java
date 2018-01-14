import org.apache.commons.io.input.CloseShieldInputStream;
import org.apache.tika.detect.AutoDetectReader;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.txt.TXTParser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.After;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Scanner;

public class FileParsingTest {

    private static final long MEGABYTE = 1024L * 1024L;

    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }

    private static final String fileName = "pagecounts-20160803-180000";

    @Test
    public void parseUsingCloseShieldCharBuffer() throws Exception {
        try (AutoDetectReader reader = new AutoDetectReader(new CloseShieldInputStream(FileParsingTest.class.getResourceAsStream(fileName)))) {
            char[] buffer = new char[4096];
            int n = reader.read(buffer);
            while (n != -1) {
                n = reader.read(buffer);
                //System.out.println(new String(buffer));
            }
        }
    }

    @Test
    public void parseUsingCharBufferXHTML() throws Exception {
        try (InputStream inputStream = FileParsingTest.class.getResourceAsStream(fileName)) {
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 4096)) {
                int n;
                while ((n = bufferedInputStream.read()) != -1) {
                    //System.out.print((char) n);
                }
            }
        }
    }

    @Test
    public void parseUsingCharBuffer() throws Exception {
        try (InputStream inputStream = FileParsingTest.class.getResourceAsStream(fileName)) {
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 4096)) {
                int n;
                while ((n = bufferedInputStream.read()) != -1) {
                    //System.out.print((char) n);
                }
            }
        }
    }

    @Test
    public void parseUsingScanner() throws Exception {
        try (InputStream stream = FileParsingTest.class.getResourceAsStream(fileName)) {
            try (Scanner sc = new Scanner(stream, "UTF-8")) {
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    //System.out.println(line);
                }
            }
        }
    }

    @Test
    public void parseSimpleFileTika() throws Exception {
        BodyContentHandler handler = new BodyContentHandler(-1);
        AutoDetectParser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();
        try (InputStream stream = FileParsingTest.class.getResourceAsStream(fileName)) {
            parser.parse(stream, handler, metadata);
            //System.out.println(handler.toString());
        }
    }

    @Test
    public void parseUsingTXTParser() throws Exception {
        TXTParser txtParser = new TXTParser();
        BodyContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();
        try (InputStream stream = FileParsingTest.class.getResourceAsStream(fileName)) {
            txtParser.parse(stream, handler, metadata, new ParseContext());
        }
        //System.out.println(handler.toString());
    }

    @After
    public void printMemoryUsed() {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used memory in megabytes: " + bytesToMegabytes(memory));
    }
}

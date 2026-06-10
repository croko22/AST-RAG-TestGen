import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataUtilTest {

    @Mock
    private Parser parser;

    @Mock
    private InputStream inputStream;

    @BeforeEach
    void setup() {
        // Setup mock parser and input stream
        when(parser.parseInput(any(), any())).thenReturn(mock(Document.class));
    }

    @Test
    void testLoadFile() throws IOException {
        // Given
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";

        // When
        Document document = org.jsoup.helper.DataUtil.load(file, charsetName, baseUri);

        // Then
        assertNotNull(document);
    }

    @Test
    void testLoadFileWithParser() throws IOException {
        // Given
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";

        // When
        Document document = org.jsoup.helper.DataUtil.load(file, charsetName, baseUri, parser);

        // Then
        assertNotNull(document);
    }

    @Test
    void testLoadPath() throws IOException {
        // Given
        Path path = Paths.get("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";

        // When
        Document document = org.jsoup.helper.DataUtil.load(path, charsetName, baseUri);

        // Then
        assertNotNull(document);
    }

    @Test
    void testLoadPathWithParser() throws IOException {
        // Given
        Path path = Paths.get("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";

        // When
        Document document = org.jsoup.helper.DataUtil.load(path, charsetName, baseUri, parser);

        // Then
        assertNotNull(document);
    }

    @Test
    void testStreamParser() throws IOException {
        // Given
        Path path = Paths.get("test.html");
        Charset charset = Charset.forName("UTF-8");
        String baseUri = "https://example.com";

        // When
        org.jsoup.parser.StreamParser streamParser = org.jsoup.helper.DataUtil.streamParser(path, charset, baseUri, parser);

        // Then
        assertNotNull(streamParser);
    }

    @Test
    void testLoadInputStream() throws IOException {
        // Given
        InputStream in = inputStream;
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";

        // When
        Document document = org.jsoup.helper.DataUtil.load(in, charsetName, baseUri);

        // Then
        assertNotNull(document);
    }

    @Test
    void testLoadInputStreamWithParser() throws IOException {
        // Given
        InputStream in = inputStream;
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";

        // When
        Document document = org.jsoup.helper.DataUtil.load(in, charsetName, baseUri, parser);

        // Then
        assertNotNull(document);
    }

    @Test
    void testReadToByteBuffer() throws IOException {
        // Given
        InputStream inStream = inputStream;
        int maxSize = 1024;

        // When
        ByteBuffer byteBuffer = org.jsoup.helper.DataUtil.readToByteBuffer(inStream, maxSize);

        // Then
        assertNotNull(byteBuffer);
    }
}
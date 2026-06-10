import org.jsoup.Connection;
import org.jsoup.internal.StringUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.UnsupportedEncodingException;
import java.net.IDN;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UrlBuilderTest {

    @Mock
    private Connection connection;

    private UrlBuilder urlBuilder;

    @BeforeEach
    public void setup() throws MalformedURLException {
        URL url = new URL("http://example.com");
        urlBuilder = new UrlBuilder(url);
    }

    @Test
    public void testBuild() throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        // Given
        URL url = new URL("http://example.com/path?query#fragment");
        urlBuilder = new UrlBuilder(url);

        // When
        URL builtUrl = urlBuilder.build();

        // Then
        assertEquals("http", builtUrl.getProtocol());
        assertEquals("example.com", builtUrl.getHost());
        assertEquals("/path", builtUrl.getPath());
        assertEquals("query", builtUrl.getQuery());
        assertEquals("fragment", builtUrl.getRef());
    }

    @Test
    public void testBuild_PunyCode() throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        // Given
        URL url = new URL("http://ünicode.example.com");
        urlBuilder = new UrlBuilder(url);

        // When
        URL builtUrl = urlBuilder.build();

        // Then
        assertEquals("http", builtUrl.getProtocol());
        assertEquals("xn--nicode-43a.example.com", builtUrl.getHost());
    }

    @Test
    public void testBuild_PathEncoding() throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        // Given
        URL url = new URL("http://example.com/ünicode/path");
        urlBuilder = new UrlBuilder(url);

        // When
        URL builtUrl = urlBuilder.build();

        // Then
        assertEquals("http", builtUrl.getProtocol());
        assertEquals("example.com", builtUrl.getHost());
        assertEquals("/%C3%BCnicode/path", builtUrl.getPath());
    }

    @Test
    public void testBuild_QueryEncoding() throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        // Given
        URL url = new URL("http://example.com?ünicode=query");
        urlBuilder = new UrlBuilder(url);

        // When
        URL builtUrl = urlBuilder.build();

        // Then
        assertEquals("http", builtUrl.getProtocol());
        assertEquals("example.com", builtUrl.getHost());
        assertEquals("%C3%BCnicode%3Dquery", builtUrl.getQuery());
    }

    @Test
    public void testBuild_FragmentEncoding() throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        // Given
        URL url = new URL("http://example.com#ünicode");
        urlBuilder = new UrlBuilder(url);

        // When
        URL builtUrl = urlBuilder.build();

        // Then
        assertEquals("http", builtUrl.getProtocol());
        assertEquals("example.com", builtUrl.getHost());
        assertEquals("%C3%BCnicode", builtUrl.getRef());
    }

    @Test
    public void testBuild_ExistingEscape() throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        // Given
        URL url = new URL("http://example.com/path%20with%20space");
        urlBuilder = new UrlBuilder(url);

        // When
        URL builtUrl = urlBuilder.build();

        // Then
        assertEquals("http", builtUrl.getProtocol());
        assertEquals("example.com", builtUrl.getHost());
        assertEquals("/path%20with%20space", builtUrl.getPath());
    }

    @Test
    public void testBuild_NewEscape() throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        // Given
        URL url = new URL("http://example.com/path with space");
        urlBuilder = new UrlBuilder(url);

        // When
        URL builtUrl = urlBuilder.build();

        // Then
        assertEquals("http", builtUrl.getProtocol());
        assertEquals("example.com", builtUrl.getHost());
        assertEquals("/path%20with%20space", builtUrl.getPath());
    }

    @Test
    public void testAppendKeyVal() throws UnsupportedEncodingException {
        // Given
        Connection.KeyVal kv = new Connection.KeyVal("key", "value");
        urlBuilder = new UrlBuilder(new URL("http://example.com"));

        // When
        urlBuilder.appendKeyVal(kv);

        // Then
        assertEquals("key=value", urlBuilder.q.toString());
    }

    @Test
    public void testAppendKeyVal_Multiple() throws UnsupportedEncodingException {
        // Given
        Connection.KeyVal kv1 = new Connection.KeyVal("key1", "value1");
        Connection.KeyVal kv2 = new Connection.KeyVal("key2", "value2");
        urlBuilder = new UrlBuilder(new URL("http://example.com"));

        // When
        urlBuilder.appendKeyVal(kv1);
        urlBuilder.appendKeyVal(kv2);

        // Then
        assertEquals("key1=value1&key2=value2", urlBuilder.q.toString());
    }

    @Test
    public void testDecodePart() {
        // Given
        String encoded = "ünicode";

        // When
        String decoded = UrlBuilder.decodePart(encoded);

        // Then
        assertEquals("ünicode", decoded);
    }

    @Test
    public void testAppendToAscii_Space() throws UnsupportedEncodingException {
        // Given
        StringBuilder sb = new StringBuilder();
        String input = "path with space";

        // When
        UrlBuilder.appendToAscii(input, false, sb);

        // Then
        assertEquals("path%20with%20space", sb.toString());
    }

    @Test
    public void testAppendToAscii_SpaceAsPlus() throws UnsupportedEncodingException {
        // Given
        StringBuilder sb = new StringBuilder();
        String input = "path with space";

        // When
        UrlBuilder.appendToAscii(input, true, sb);

        // Then
        assertEquals("path+with+space", sb.toString());
    }

    @Test
    public void testAppendToAscii_NonAscii() throws UnsupportedEncodingException {
        // Given
        StringBuilder sb = new StringBuilder();
        String input = "path with ünicode";

        // When
        UrlBuilder.appendToAscii(input, false, sb);

        // Then
        assertEquals("path%20with%20%C3%BCnicode", sb.toString());
    }

    @Test
    public void testAppendToAscii_ExistingEscape() throws UnsupportedEncodingException {
        // Given
        StringBuilder sb = new StringBuilder();
        String input = "path%20with%20space";

        // When
        UrlBuilder.appendToAscii(input, false, sb);

        // Then
        assertEquals("path%20with%20space", sb.toString());
    }

    @Test
    public void testIsHex() {
        // Given
        char c = 'A';

        // When
        boolean isHex = UrlBuilder.isHex(c);

        // Then
        assertTrue(isHex);
    }
}
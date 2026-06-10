import org.jsoup.Connection;
import org.jsoup.helper.UrlConnectionExecutor;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UrlConnectionExecutorTest {

    @Mock
    private Connection connection;

    @Mock
    private HttpURLConnection httpURLConnection;

    private UrlConnectionExecutor executor;

    @BeforeEach
    void setup() {
        executor = new UrlConnectionExecutor((HttpConnection.Request) connection, null);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(connection, httpURLConnection);
    }

    @Test
    void testExecute() throws IOException {
        // Given
        when(connection.url()).thenReturn(new URL("http://example.com"));
        when(connection.method()).thenReturn(Connection.Method.GET);
        when(connection.timeout()).thenReturn(1000);
        when(connection.proxy()).thenReturn(null);
        when(connection.sslContext()).thenReturn(null);
        when(connection.sslSocketFactory()).thenReturn(null);
        when(connection.authenticator()).thenReturn(null);
        when(connection.multiHeaders()).thenReturn(new HashMap<>());
        when(connection.data()).thenReturn(new HashMap<>());
        when(httpURLConnection.getResponseCode()).thenReturn(200);
        when(httpURLConnection.getResponseMessage()).thenReturn("OK");
        when(httpURLConnection.getContentType()).thenReturn("text/html");
        when(httpURLConnection.getContentLength()).thenReturn(100);
        when(httpURLConnection.getHeaderFieldKey(anyInt())).thenReturn("Content-Type");
        when(httpURLConnection.getHeaderField(anyInt())).thenReturn("text/html");
        when(httpURLConnection.getOutputStream()).thenReturn(null);

        // When
        HttpConnection.Response response = executor.execute();

        // Then
        assertNotNull(response);
        assertEquals(200, response.statusCode);
        assertEquals("OK", response.statusMessage);
        assertEquals("text/html", response.contentType);
        assertEquals(100, response.contentLength);
        verify(connection, times(1)).url();
        verify(connection, times(1)).method();
        verify(connection, times(1)).timeout();
        verify(connection, times(1)).proxy();
        verify(connection, times(1)).sslContext();
        verify(connection, times(1)).sslSocketFactory();
        verify(connection, times(1)).authenticator();
        verify(connection, times(1)).multiHeaders();
        verify(connection, times(1)).data();
        verify(httpURLConnection, times(1)).getResponseCode();
        verify(httpURLConnection, times(1)).getResponseMessage();
        verify(httpURLConnection, times(1)).getContentType();
        verify(httpURLConnection, times(1)).getContentLength();
        verify(httpURLConnection, times(1)).getHeaderFieldKey(anyInt());
        verify(httpURLConnection, times(1)).getHeaderField(anyInt());
        verify(httpURLConnection, times(1)).getOutputStream();
    }

    @Test
    void testExecuteIOException() throws IOException {
        // Given
        when(connection.url()).thenReturn(new URL("http://example.com"));
        when(connection.method()).thenReturn(Connection.Method.GET);
        when(connection.timeout()).thenReturn(1000);
        when(connection.proxy()).thenReturn(null);
        when(connection.sslContext()).thenReturn(null);
        when(connection.sslSocketFactory()).thenReturn(null);
        when(connection.authenticator()).thenReturn(null);
        when(connection.multiHeaders()).thenReturn(new HashMap<>());
        when(connection.data()).thenReturn(new HashMap<>());
        when(httpURLConnection.getResponseCode()).thenThrow(new IOException());

        // When and Then
        assertThrows(IOException.class, () -> executor.execute());
        verify(connection, times(1)).url();
        verify(connection, times(1)).method();
        verify(connection, times(1)).timeout();
        verify(connection, times(1)).proxy();
        verify(connection, times(1)).sslContext();
        verify(connection, times(1)).sslSocketFactory();
        verify(connection, times(1)).authenticator();
        verify(connection, times(1)).multiHeaders();
        verify(connection, times(1)).data();
        verify(httpURLConnection, times(1)).getResponseCode();
    }

    @Test
    void testResponseBody() throws IOException {
        // Given
        when(connection.url()).thenReturn(new URL("http://example.com"));
        when(connection.method()).thenReturn(Connection.Method.GET);
        when(connection.timeout()).thenReturn(1000);
        when(connection.proxy()).thenReturn(null);
        when(connection.sslContext()).thenReturn(null);
        when(connection.sslSocketFactory()).thenReturn(null);
        when(connection.authenticator()).thenReturn(null);
        when(connection.multiHeaders()).thenReturn(new HashMap<>());
        when(connection.data()).thenReturn(new HashMap<>());
        when(httpURLConnection.getResponseCode()).thenReturn(200);
        when(httpURLConnection.getResponseMessage()).thenReturn("OK");
        when(httpURLConnection.getContentType()).thenReturn("text/html");
        when(httpURLConnection.getContentLength()).thenReturn(100);
        when(httpURLConnection.getHeaderFieldKey(anyInt())).thenReturn("Content-Type");
        when(httpURLConnection.getHeaderField(anyInt())).thenReturn("text/html");
        when(httpURLConnection.getOutputStream()).thenReturn(null);
        when(httpURLConnection.getErrorStream()).thenReturn(null);
        when(httpURLConnection.getInputStream()).thenReturn(null);

        // When
        executor.execute();
        InputStream inputStream = executor.responseBody();

        // Then
        assertNotNull(inputStream);
        verify(connection, times(1)).url();
        verify(connection, times(1)).method();
        verify(connection, times(1)).timeout();
        verify(connection, times(1)).proxy();
        verify(connection, times(1)).sslContext();
        verify(connection, times(1)).sslSocketFactory();
        verify(connection, times(1)).authenticator();
        verify(connection, times(1)).multiHeaders();
        verify(connection, times(1)).data();
        verify(httpURLConnection, times(1)).getResponseCode();
        verify(httpURLConnection, times(1)).getResponseMessage();
        verify(httpURLConnection, times(1)).getContentType();
        verify(httpURLConnection, times(1)).getContentLength();
        verify(httpURLConnection, times(1)).getHeaderFieldKey(anyInt());
        verify(httpURLConnection, times(1)).getHeaderField(anyInt());
        verify(httpURLConnection, times(1)).getOutputStream();
        verify(httpURLConnection, times(1)).getErrorStream();
        verify(httpURLConnection, times(1)).getInputStream();
    }

    @Test
    void testResponseBodyIOException() throws IOException {
        // Given
        when(connection.url()).thenReturn(new URL("http://example.com"));
        when(connection.method()).thenReturn(Connection.Method.GET);
        when(connection.timeout()).thenReturn(1000);
        when(connection.proxy()).thenReturn(null);
        when(connection.sslContext()).thenReturn(null);
        when(connection.sslSocketFactory()).thenReturn(null);
        when(connection.authenticator()).thenReturn(null);
        when(connection.multiHeaders()).thenReturn(new HashMap<>());
        when(connection.data()).thenReturn(new HashMap<>());
        when(httpURLConnection.getResponseCode()).thenReturn(200);
        when(httpURLConnection.getResponseMessage()).thenReturn("OK");
        when(httpURLConnection.getContentType()).thenReturn("text/html");
        when(httpURLConnection.getContentLength()).thenReturn(100);
        when(httpURLConnection.getHeaderFieldKey(anyInt())).thenReturn("Content-Type");
        when(httpURLConnection.getHeaderField(anyInt())).thenReturn("text/html");
        when(httpURLConnection.getOutputStream()).thenReturn(null);
        when(httpURLConnection.getErrorStream()).thenThrow(new IOException());

        // When and Then
        executor.execute();
        assertThrows(IOException.class, () -> executor.responseBody());
        verify(connection, times(1)).url();
        verify(connection, times(1)).method();
        verify(connection, times(1)).timeout();
        verify(connection, times(1)).proxy();
        verify(connection, times(1)).sslContext();
        verify(connection, times(1)).sslSocketFactory();
        verify(connection, times(1)).authenticator();
        verify(connection, times(1)).multiHeaders();
        verify(connection, times(1)).data();
        verify(httpURLConnection, times(1)).getResponseCode();
        verify(httpURLConnection, times(1)).getResponseMessage();
        verify(httpURLConnection, times(1)).getContentType();
        verify(httpURLConnection, times(1)).getContentLength();
        verify(httpURLConnection, times(1)).getHeaderFieldKey(anyInt());
        verify(httpURLConnection, times(1)).getHeaderField(anyInt());
        verify(httpURLConnection, times(1)).getOutputStream();
        verify(httpURLConnection, times(1)).getErrorStream();
    }

    @Test
    void testSafeClose() {
        // Given
        executor = new UrlConnectionExecutor((HttpConnection.Request) connection, null);
        executor.conn = httpURLConnection;

        // When
        executor.safeClose();

        // Then
        verify(httpURLConnection, times(1)).disconnect();
        assertNull(executor.conn);
    }

    @Test
    void testCreateConnection() throws IOException {
        // Given
        when(connection.url()).thenReturn(new URL("http://example.com"));
        when(connection.method()).thenReturn(Connection.Method.GET);
        when(connection.timeout()).thenReturn(1000);
        when(connection.proxy()).thenReturn(null);
        when(connection.sslContext()).thenReturn(null);
        when(connection.sslSocketFactory()).thenReturn(null);
        when(connection.authenticator()).thenReturn(null);
        when(connection.multiHeaders()).thenReturn(new HashMap<>());
        when(connection.data()).thenReturn(new HashMap<>());

        // When
        HttpURLConnection conn = UrlConnectionExecutor.createConnection((HttpConnection.Request) connection);

        // Then
        assertNotNull(conn);
        verify(connection, times(1)).url();
        verify(connection, times(1)).method();
        verify(connection, times(1)).timeout();
        verify(connection, times(1)).proxy();
        verify(connection, times(1)).sslContext();
        verify(connection, times(1)).sslSocketFactory();
        verify(connection, times(1)).authenticator();
        verify(connection, times(1)).multiHeaders();
        verify(connection, times(1)).data();
    }

    @Test
    void testCreateHeaderMap() {
        // Given
        when(httpURLConnection.getHeaderFieldKey(anyInt())).thenReturn("Content-Type");
        when(httpURLConnection.getHeaderField(anyInt())).thenReturn("text/html");

        // When
        Map<String, List<String>> headers = UrlConnectionExecutor.createHeaderMap(httpURLConnection);

        // Then
        assertNotNull(headers);
        assertEquals(1, headers.size());
        verify(httpURLConnection, atLeast(1)).getHeaderFieldKey(anyInt());
        verify(httpURLConnection, atLeast(1)).getHeaderField(anyInt());
    }
}
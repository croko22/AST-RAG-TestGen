import org.jsoup.helper.Validate;
import org.jsoup.internal.SimpleStreamReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SimpleStreamReaderTest {

    @Mock
    private InputStream inputStream;

    @Mock
    private CharsetDecoder charsetDecoder;

    private SimpleStreamReader simpleStreamReader;

    @BeforeEach
    void setup() {
        simpleStreamReader = new SimpleStreamReader(inputStream, Charset.defaultCharset());
    }

    @AfterEach
    void tearDown() {
        try {
            simpleStreamReader.close();
        } catch (IOException e) {
            fail("Error closing SimpleStreamReader");
        }
    }

    @Test
    void testRead() throws IOException {
        // Given
        byte[] bytes = "Hello, World!".getBytes();
        when(inputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(bytes.length);

        // When
        char[] chars = new char[bytes.length];
        int read = simpleStreamReader.read(chars, 0, bytes.length);

        // Then
        assertEquals(bytes.length, read);
        assertEquals("Hello, World!", new String(chars));
    }

    @Test
    void testReadEmpty() throws IOException {
        // Given
        when(inputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(-1);

        // When
        char[] chars = new char[10];
        int read = simpleStreamReader.read(chars, 0, 10);

        // Then
        assertEquals(-1, read);
    }

    @Test
    void testReadPartial() throws IOException {
        // Given
        byte[] bytes = "Hello".getBytes();
        when(inputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(bytes.length);

        // When
        char[] chars = new char[10];
        int read = simpleStreamReader.read(chars, 0, 10);

        // Then
        assertEquals(bytes.length, read);
        assertEquals("Hello", new String(chars, 0, bytes.length));
    }

    @Test
    void testClose() throws IOException {
        // Given
        doNothing().when(inputStream).close();

        // When
        simpleStreamReader.close();

        // Then
        verify(inputStream, times(1)).close();
    }

    @Test
    void testCloseTwice() throws IOException {
        // Given
        doNothing().when(inputStream).close();

        // When
        simpleStreamReader.close();
        simpleStreamReader.close();

        // Then
        verify(inputStream, times(1)).close();
    }

    @Test
    void testReadAfterClose() {
        // Given
        try {
            simpleStreamReader.close();
        } catch (IOException e) {
            fail("Error closing SimpleStreamReader");
        }

        // When and Then
        assertThrows(NullPointerException.class, () -> simpleStreamReader.read(new char[10], 0, 10));
    }

    @Test
    void testValidateNotNull() {
        // Given
        InputStream nullInputStream = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> new SimpleStreamReader(nullInputStream, Charset.defaultCharset()));
    }
}
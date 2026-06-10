import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SimpleBufferedInputTest {

    @Mock
    private InputStream in;

    private SimpleBufferedInput simpleBufferedInput;

    @BeforeEach
    void setUp() {
        simpleBufferedInput = new SimpleBufferedInput(in);
    }

    @Test
    public void testRead() throws IOException {
        // Given
        byte[] bytes = "Hello World".getBytes();
        when(in.read(any(byte[].class))).thenReturn(bytes.length);
        doAnswer(invocation -> {
            byte[] buffer = invocation.getArgument(0);
            System.arraycopy(bytes, 0, buffer, 0, bytes.length);
            return bytes.length;
        }).when(in).read(any(byte[].class), anyInt(), anyInt());

        // When
        int result = simpleBufferedInput.read();

        // Then
        assertEquals(bytes[0], result);
    }

    @Test
    public void testReadByteArray() throws IOException {
        // Given
        byte[] bytes = "Hello World".getBytes();
        when(in.read(any(byte[].class))).thenReturn(bytes.length);
        doAnswer(invocation -> {
            byte[] buffer = invocation.getArgument(0);
            System.arraycopy(bytes, 0, buffer, 0, bytes.length);
            return bytes.length;
        }).when(in).read(any(byte[].class), anyInt(), anyInt());

        // When
        byte[] dest = new byte[bytes.length];
        int result = simpleBufferedInput.read(dest, 0, bytes.length);

        // Then
        assertEquals(bytes.length, result);
        assertArrayEquals(bytes, dest);
    }

    @Test
    public void testAvailable() throws IOException {
        // Given
        when(in.available()).thenReturn(10);

        // When
        int result = simpleBufferedInput.available();

        // Then
        assertEquals(10, result);
    }

    @Test
    public void testClose() throws IOException {
        // Given

        // When
        simpleBufferedInput.close();

        // Then
        verify(in).close();
    }

    @Test
    public void testBaseReadFully() {
        // Given
        simpleBufferedInput.resetFullyRead();

        // When
        boolean result = simpleBufferedInput.baseReadFully();

        // Then
        assertFalse(result);
    }

    @Test
    public void testResetFullyRead() {
        // Given
        simpleBufferedInput.inReadFully = true;

        // When
        simpleBufferedInput.resetFullyRead();

        // Then
        assertFalse(simpleBufferedInput.inReadFully);
    }

    @Test
    public void testCapRemaining() {
        // Given
        int newRemaining = 10;

        // When
        simpleBufferedInput.capRemaining(newRemaining);

        // Then
        assertEquals(newRemaining, simpleBufferedInput.capRemaining);
    }

    @Test
    public void testSetMark() {
        // Given

        // When
        simpleBufferedInput.setMark();

        // Then
        assertEquals(simpleBufferedInput.bufPos, simpleBufferedInput.bufMark);
    }

    @Test
    public void testRewindToMark() throws IOException {
        // Given
        simpleBufferedInput.setMark();

        // When
        simpleBufferedInput.rewindToMark();

        // Then
        assertEquals(simpleBufferedInput.bufMark, simpleBufferedInput.bufPos);
    }

    @Test
    public void testClearMark() {
        // Given
        simpleBufferedInput.setMark();

        // When
        simpleBufferedInput.clearMark();

        // Then
        assertEquals(-1, simpleBufferedInput.bufMark);
    }

    @Test
    public void testCompact() {
        // Given
        byte[] bytes = "Hello World".getBytes();
        simpleBufferedInput.byteBuf = bytes;
        simpleBufferedInput.bufPos = 0;
        simpleBufferedInput.bufLength = bytes.length;

        // When
        simpleBufferedInput.compact();

        // Then
        assertEquals(0, simpleBufferedInput.bufPos);
        assertEquals(bytes.length, simpleBufferedInput.bufLength);
    }

    @Test
    public void testFill() throws IOException {
        // Given
        byte[] bytes = "Hello World".getBytes();
        when(in.read(any(byte[].class))).thenReturn(bytes.length);
        doAnswer(invocation -> {
            byte[] buffer = invocation.getArgument(0);
            System.arraycopy(bytes, 0, buffer, 0, bytes.length);
            return bytes.length;
        }).when(in).read(any(byte[].class), anyInt(), anyInt());

        // When
        simpleBufferedInput.fill();

        // Then
        assertEquals(bytes.length, simpleBufferedInput.bufLength);
    }

    @Test
    public void testGetBuf() {
        // Given
        byte[] bytes = "Hello World".getBytes();
        simpleBufferedInput.byteBuf = bytes;

        // When
        byte[] result = simpleBufferedInput.getBuf();

        // Then
        assertArrayEquals(bytes, result);
    }

    @Test
    public void testSimpleBufferedInput() {
        // Given
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());

        // When
        SimpleBufferedInput simpleBufferedInput = new SimpleBufferedInput(inputStream);

        // Then
        assertNotNull(simpleBufferedInput);
    }
}
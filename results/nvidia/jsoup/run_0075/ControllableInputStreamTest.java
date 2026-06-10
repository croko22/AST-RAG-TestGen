import org.jsoup.internal.ControllableInputStream;
import org.jsoup.internal.SimpleBufferedInput;
import org.jsoup.helper.Validate;
import org.jsoup.Progress;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ControllableInputStreamTest {

    @Mock
    private SimpleBufferedInput simpleBufferedInput;

    @Mock
    private Progress<Object> progress;

    private ControllableInputStream controllableInputStream;

    @BeforeEach
    void setup() {
        controllableInputStream = new ControllableInputStream(simpleBufferedInput, 10);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(simpleBufferedInput, progress);
    }

    @Test
    void testWrapInputStream() {
        // Given
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());

        // When
        ControllableInputStream controllableInputStream = ControllableInputStream.wrap(inputStream, 10);

        // Then
        assertNotNull(controllableInputStream);
    }

    @Test
    void testWrapInputStreamBufferSize() {
        // Given
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());

        // When
        ControllableInputStream controllableInputStream = ControllableInputStream.wrap(inputStream, 5, 10);

        // Then
        assertNotNull(controllableInputStream);
    }

    @Test
    void testRead() throws IOException {
        // Given
        byte[] bytes = new byte[10];
        when(simpleBufferedInput.read(anyInt(), anyInt())).thenReturn(5);

        // When
        int read = controllableInputStream.read(bytes, 0, 10);

        // Then
        assertEquals(5, read);
        verify(simpleBufferedInput, times(1)).read(anyInt(), anyInt());
    }

    @Test
    void testMarkSupported() {
        // When
        boolean supported = controllableInputStream.markSupported();

        // Then
        assertTrue(supported);
    }

    @Test
    void testReadToByteBuffer() throws IOException {
        // Given
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());

        // When
        ByteBuffer byteBuffer = ControllableInputStream.readToByteBuffer(inputStream, 10);

        // Then
        assertNotNull(byteBuffer);
    }

    @Test
    void testReset() throws IOException {
        // Given
        controllableInputStream.mark(0);

        // When
        controllableInputStream.reset();

        // Then
        verify(simpleBufferedInput, times(1)).rewindToMark();
        verify(simpleBufferedInput, times(1)).clearMark();
    }

    @Test
    void testMark() {
        // When
        controllableInputStream.mark(0);

        // Then
        verify(simpleBufferedInput, times(1)).setMark();
    }

    @Test
    void testBaseReadFully() {
        // Given
        when(simpleBufferedInput.baseReadFully()).thenReturn(true);

        // When
        boolean fullyRead = controllableInputStream.baseReadFully();

        // Then
        assertTrue(fullyRead);
        verify(simpleBufferedInput, times(1)).baseReadFully();
    }

    @Test
    void testResetFullyRead() {
        // When
        controllableInputStream.resetFullyRead();

        // Then
        verify(simpleBufferedInput, times(1)).resetFullyRead();
    }

    @Test
    void testMax() {
        // When
        int max = controllableInputStream.max();

        // Then
        assertEquals(10, max);
    }

    @Test
    void testMaxNewMax() {
        // When
        controllableInputStream.max(20);

        // Then
        verify(simpleBufferedInput, times(1)).capRemaining(20);
    }

    @Test
    void testAllowClose() {
        // When
        controllableInputStream.allowClose(false);

        // Then
        assertFalse(controllableInputStream.allowClose);
    }

    @Test
    void testClose() throws IOException {
        // Given
        controllableInputStream.allowClose(true);

        // When
        controllableInputStream.close();

        // Then
        verify(simpleBufferedInput, times(1)).close();
    }

    @Test
    void testTimeout() {
        // When
        controllableInputStream.timeout(1000, 1000);

        // Then
        assertEquals(1000, controllableInputStream.startTime);
        assertEquals(1000000, controllableInputStream.timeout);
    }

    @Test
    void testOnProgress() {
        // Given
        Progress<Object> progress = mock(Progress.class);
        Object context = new Object();

        // When
        controllableInputStream.onProgress(100, progress, context);

        // Then
        assertEquals(100, controllableInputStream.contentLength);
        assertEquals(progress, controllableInputStream.progress);
        assertEquals(context, controllableInputStream.progressContext);
    }

    @Test
    void testInputSteam() {
        // When
        InputStream inputStream = controllableInputStream.inputStream();

        // Then
        assertNotNull(inputStream);
    }
}
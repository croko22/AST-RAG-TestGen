import org.ice4j.socket.MultiplexingSocket;
import org.ice4j.socket.TCPInputStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.DatagramPacket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TCPInputStreamTest {

    @Mock
    private MultiplexingSocket socket;

    private TCPInputStream tcpInputStream;

    @BeforeEach
    void setup() {
        tcpInputStream = new TCPInputStream(socket);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(socket);
    }

    @Test
    void testClose() throws IOException {
        // When
        tcpInputStream.close();

        // Then
        verify(socket, never()).close(any());
    }

    @Test
    void testRead() throws IOException {
        // Given
        byte[] data = new byte[]{1, 2, 3};
        DatagramPacket packet = new DatagramPacket(data, data.length);
        when(socket.receive(any())).thenReturn();
        when(socket.receive(any(DatagramPacket.class))).thenAnswer(invocation -> {
            DatagramPacket p = invocation.getArgument(0);
            p.setData(data);
            p.setLength(data.length);
            return;
        });

        // When
        int result = tcpInputStream.read();

        // Then
        assertEquals(1, result);
        verify(socket, times(1)).receive(any(DatagramPacket.class));
    }

    @Test
    void testReadByteArr() throws IOException {
        // Given
        byte[] data = new byte[]{1, 2, 3};
        DatagramPacket packet = new DatagramPacket(data, data.length);
        when(socket.receive(any())).thenReturn();
        when(socket.receive(any(DatagramPacket.class))).thenAnswer(invocation -> {
            DatagramPacket p = invocation.getArgument(0);
            p.setData(data);
            p.setLength(data.length);
            return;
        });

        // When
        byte[] buffer = new byte[10];
        int result = tcpInputStream.read(buffer, 0, 10);

        // Then
        assertEquals(3, result);
        assertArrayEquals(data, buffer, 0, 3);
        verify(socket, times(1)).receive(any(DatagramPacket.class));
    }

    @Test
    void testSkip() throws IOException {
        // Given
        long n = 10;

        // When
        long result = tcpInputStream.skip(n);

        // Then
        assertEquals(n, result);
    }

    @Test
    void testReadWithZeroLength() throws IOException {
        // When
        int result = tcpInputStream.read(new byte[10], 0, 0);

        // Then
        assertEquals(0, result);
        verify(socket, never()).receive(any(DatagramPacket.class));
    }

    @Test
    void testReadWithNegativeLength() throws IOException {
        // When and Then
        assertThrows(IllegalArgumentException.class, () -> tcpInputStream.read(new byte[10], 0, -1));
        verify(socket, never()).receive(any(DatagramPacket.class));
    }

    @Test
    void testReadWithNullBuffer() throws IOException {
        // When and Then
        assertThrows(NullPointerException.class, () -> tcpInputStream.read(null, 0, 10));
        verify(socket, never()).receive(any(DatagramPacket.class));
    }

    @Test
    void testReadWithOffsetGreaterThanBufferLength() throws IOException {
        // When and Then
        assertThrows(IndexOutOfBoundsException.class, () -> tcpInputStream.read(new byte[10], 15, 10));
        verify(socket, never()).receive(any(DatagramPacket.class));
    }
}
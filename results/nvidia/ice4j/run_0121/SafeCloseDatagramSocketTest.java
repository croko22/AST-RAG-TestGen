import org.ice4j.socket.SafeCloseDatagramSocket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SafeCloseDatagramSocketTest {

    @Mock
    private DatagramSocket delegate;

    private SafeCloseDatagramSocket safeCloseDatagramSocket;

    @BeforeEach
    void setup() throws SocketException {
        safeCloseDatagramSocket = new SafeCloseDatagramSocket(delegate);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(delegate);
    }

    @Test
    void testClose() {
        // When
        safeCloseDatagramSocket.close();

        // Then
        verify(delegate).close();
    }

    @Test
    void testReceive() throws IOException {
        // Given
        DatagramPacket packet = new DatagramPacket(new byte[10], 10);
        doNothing().when(delegate).receive(any(DatagramPacket.class));

        // When
        safeCloseDatagramSocket.receive(packet);

        // Then
        verify(delegate).receive(any(DatagramPacket.class));
    }

    @Test
    void testReceive_ThrowsIOException() throws IOException {
        // Given
        DatagramPacket packet = new DatagramPacket(new byte[10], 10);
        doThrow(new IOException()).when(delegate).receive(any(DatagramPacket.class));

        // When and Then
        assertThrows(IOException.class, () -> safeCloseDatagramSocket.receive(packet));
        verify(delegate).receive(any(DatagramPacket.class));
    }

    @Test
    void testConstructor_Default() throws SocketException {
        // When
        SafeCloseDatagramSocket socket = new SafeCloseDatagramSocket();

        // Then
        assertNotNull(socket);
    }

    @Test
    void testConstructor_WithDelegate() throws SocketException {
        // When
        SafeCloseDatagramSocket socket = new SafeCloseDatagramSocket(delegate);

        // Then
        assertNotNull(socket);
    }

    @Test
    void testConstructor_WithPort() throws SocketException {
        // When
        SafeCloseDatagramSocket socket = new SafeCloseDatagramSocket(1234);

        // Then
        assertNotNull(socket);
    }

    @Test
    void testConstructor_WithPortAndAddress() throws SocketException {
        // When
        InetAddress address = InetAddress.getByName("localhost");
        SafeCloseDatagramSocket socket = new SafeCloseDatagramSocket(1234, address);

        // Then
        assertNotNull(socket);
    }

    @Test
    void testConstructor_WithSocketAddress() throws SocketException {
        // When
        InetAddress address = InetAddress.getByName("localhost");
        SafeCloseDatagramSocket socket = new SafeCloseDatagramSocket(new java.net.InetSocketAddress(address, 1234));

        // Then
        assertNotNull(socket);
    }
}
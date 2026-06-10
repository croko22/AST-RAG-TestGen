import org.ice4j.socket.IceSocketWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IceSocketWrapperTest {

    @Mock
    private IceSocketWrapper iceSocketWrapper;

    @BeforeEach
    void setup() {
        // Initialize mock
    }

    @Test
    public void testSend() throws IOException {
        // Given
        DatagramPacket packet = new DatagramPacket(new byte[10], 10);
        doNothing().when(iceSocketWrapper).send(any(DatagramPacket.class));

        // When
        iceSocketWrapper.send(packet);

        // Then
        verify(iceSocketWrapper, times(1)).send(any(DatagramPacket.class));
    }

    @Test
    public void testSend_IOException() throws IOException {
        // Given
        DatagramPacket packet = new DatagramPacket(new byte[10], 10);
        doThrow(new IOException()).when(iceSocketWrapper).send(any(DatagramPacket.class));

        // When / Then
        assertThrows(IOException.class, () -> iceSocketWrapper.send(packet));
        verify(iceSocketWrapper, times(1)).send(any(DatagramPacket.class));
    }

    @Test
    public void testReceive() throws IOException {
        // Given
        DatagramPacket packet = new DatagramPacket(new byte[10], 10);
        doNothing().when(iceSocketWrapper).receive(any(DatagramPacket.class));

        // When
        iceSocketWrapper.receive(packet);

        // Then
        verify(iceSocketWrapper, times(1)).receive(any(DatagramPacket.class));
    }

    @Test
    public void testReceive_IOException() throws IOException {
        // Given
        DatagramPacket packet = new DatagramPacket(new byte[10], 10);
        doThrow(new IOException()).when(iceSocketWrapper).receive(any(DatagramPacket.class));

        // When / Then
        assertThrows(IOException.class, () -> iceSocketWrapper.receive(packet));
        verify(iceSocketWrapper, times(1)).receive(any(DatagramPacket.class));
    }

    @Test
    public void testClose() {
        // Given
        doNothing().when(iceSocketWrapper).close();

        // When
        iceSocketWrapper.close();

        // Then
        verify(iceSocketWrapper, times(1)).close();
    }

    @Test
    public void testGetLocalAddress() throws UnknownHostException {
        // Given
        InetAddress address = InetAddress.getLocalHost();
        when(iceSocketWrapper.getLocalAddress()).thenReturn(address);

        // When
        InetAddress result = iceSocketWrapper.getLocalAddress();

        // Then
        assertEquals(address, result);
        verify(iceSocketWrapper, times(1)).getLocalAddress();
    }

    @Test
    public void testGetLocalPort() {
        // Given
        int port = 8080;
        when(iceSocketWrapper.getLocalPort()).thenReturn(port);

        // When
        int result = iceSocketWrapper.getLocalPort();

        // Then
        assertEquals(port, result);
        verify(iceSocketWrapper, times(1)).getLocalPort();
    }

    @Test
    public void testGetLocalSocketAddress() {
        // Given
        SocketAddress address = mock(SocketAddress.class);
        when(iceSocketWrapper.getLocalSocketAddress()).thenReturn(address);

        // When
        SocketAddress result = iceSocketWrapper.getLocalSocketAddress();

        // Then
        assertEquals(address, result);
        verify(iceSocketWrapper, times(1)).getLocalSocketAddress();
    }

    @Test
    public void testGetTCPSocket() {
        // Given
        Socket socket = mock(Socket.class);
        when(iceSocketWrapper.getTCPSocket()).thenReturn(socket);

        // When
        Socket result = iceSocketWrapper.getTCPSocket();

        // Then
        assertEquals(socket, result);
        verify(iceSocketWrapper, times(1)).getTCPSocket();
    }

    @Test
    public void testGetUDPSocket() {
        // Given
        DatagramSocket socket = mock(DatagramSocket.class);
        when(iceSocketWrapper.getUDPSocket()).thenReturn(socket);

        // When
        DatagramSocket result = iceSocketWrapper.getUDPSocket();

        // Then
        assertEquals(socket, result);
        verify(iceSocketWrapper, times(1)).getUDPSocket();
    }
}
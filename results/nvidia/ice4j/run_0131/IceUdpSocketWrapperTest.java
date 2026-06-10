import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IceUdpSocketWrapperTest {

    @Mock
    private DatagramSocket socket;

    @Mock
    private DatagramPacket packet;

    private IceUdpSocketWrapper iceUdpSocketWrapper;

    @BeforeEach
    void setup() {
        iceUdpSocketWrapper = new IceUdpSocketWrapper(socket);
    }

    @Test
    public void testSend() throws IOException {
        // Given
        when(socket.send(any(DatagramPacket.class))).thenReturn();

        // When
        iceUdpSocketWrapper.send(packet);

        // Then
        verify(socket, times(1)).send(packet);
    }

    @Test
    public void testSend_IOException() throws IOException {
        // Given
        when(socket.send(any(DatagramPacket.class))).thenThrow(new SocketException());

        // When / Then
        assertThrows(IOException.class, () -> iceUdpSocketWrapper.send(packet));
        verify(socket, times(1)).send(packet);
    }

    @Test
    public void testReceive() throws IOException {
        // Given
        when(socket.receive(any(DatagramPacket.class))).thenReturn();

        // When
        iceUdpSocketWrapper.receive(packet);

        // Then
        verify(socket, times(1)).receive(packet);
    }

    @Test
    public void testReceive_IOException() throws IOException {
        // Given
        when(socket.receive(any(DatagramPacket.class))).thenThrow(new SocketException());

        // When / Then
        assertThrows(IOException.class, () -> iceUdpSocketWrapper.receive(packet));
        verify(socket, times(1)).receive(packet);
    }

    @Test
    public void testClose() {
        // When
        iceUdpSocketWrapper.close();

        // Then
        verify(socket, times(1)).close();
    }

    @Test
    public void testGetLocalAddress() {
        // Given
        InetAddress address = mock(InetAddress.class);
        when(socket.getLocalAddress()).thenReturn(address);

        // When
        InetAddress result = iceUdpSocketWrapper.getLocalAddress();

        // Then
        assertEquals(address, result);
        verify(socket, times(1)).getLocalAddress();
    }

    @Test
    public void testGetLocalPort() {
        // Given
        int port = 1234;
        when(socket.getLocalPort()).thenReturn(port);

        // When
        int result = iceUdpSocketWrapper.getLocalPort();

        // Then
        assertEquals(port, result);
        verify(socket, times(1)).getLocalPort();
    }

    @Test
    public void testGetLocalSocketAddress() {
        // Given
        SocketAddress address = mock(SocketAddress.class);
        when(socket.getLocalSocketAddress()).thenReturn(address);

        // When
        SocketAddress result = iceUdpSocketWrapper.getLocalSocketAddress();

        // Then
        assertEquals(address, result);
        verify(socket, times(1)).getLocalSocketAddress();
    }

    @Test
    public void testGetTCPSocket() {
        // When
        Socket result = iceUdpSocketWrapper.getTCPSocket();

        // Then
        assertNull(result);
    }

    @Test
    public void testGetUDPSocket() {
        // When
        DatagramSocket result = iceUdpSocketWrapper.getUDPSocket();

        // Then
        assertEquals(socket, result);
    }
}
import org.ice4j.ice.Component;
import org.ice4j.message.DatagramPacket;
import org.ice4j.socket.IceTcpServerSocketWrapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IceTcpServerSocketWrapperTest {

    @Mock
    private ServerSocket serverSocket;

    @Mock
    private Component component;

    private IceTcpServerSocketWrapper iceTcpServerSocketWrapper;

    @BeforeEach
    void setup() throws IOException {
        // Given: a ServerSocket and a Component
        when(serverSocket.getInetAddress()).thenReturn(InetAddress.getByName("localhost"));
        when(serverSocket.getLocalPort()).thenReturn(1234);
        when(serverSocket.getLocalSocketAddress()).thenReturn(new SocketAddress() {
            @Override
            public String toString() {
                return "localhost:1234";
            }
        });
        iceTcpServerSocketWrapper = new IceTcpServerSocketWrapper(serverSocket, component);
    }

    @AfterEach
    void tearDown() {
        // Clean up
        iceTcpServerSocketWrapper.close();
    }

    @Test
    void testSend() {
        // Given: a DatagramPacket
        DatagramPacket packet = new DatagramPacket(new byte[0], 0, InetAddress.getByName("localhost"), 1234);

        // When: send is called
        assertDoesNotThrow(() -> iceTcpServerSocketWrapper.send(packet));
    }

    @Test
    void testReceive() {
        // Given: a DatagramPacket
        DatagramPacket packet = new DatagramPacket(new byte[0], 0, InetAddress.getByName("localhost"), 1234);

        // When: receive is called
        assertDoesNotThrow(() -> iceTcpServerSocketWrapper.receive(packet));
    }

    @Test
    void testClose() {
        // When: close is called
        iceTcpServerSocketWrapper.close();

        // Then: the ServerSocket is closed
        verify(serverSocket, times(1)).close();
    }

    @Test
    void testGetLocalAddress() {
        // When: getLocalAddress is called
        InetAddress localAddress = iceTcpServerSocketWrapper.getLocalAddress();

        // Then: the local address is returned
        assertEquals(InetAddress.getByName("localhost"), localAddress);
    }

    @Test
    void testGetLocalPort() {
        // When: getLocalPort is called
        int localPort = iceTcpServerSocketWrapper.getLocalPort();

        // Then: the local port is returned
        assertEquals(1234, localPort);
    }

    @Test
    void testGetLocalSocketAddress() {
        // When: getLocalSocketAddress is called
        SocketAddress localSocketAddress = iceTcpServerSocketWrapper.getLocalSocketAddress();

        // Then: the local socket address is returned
        assertEquals("localhost:1234", localSocketAddress.toString());
    }

    @Test
    void testGetTCPSocket() {
        // Given: a Socket
        Socket socket = mock(Socket.class);

        // When: getTCPSocket is called
        Socket tcpSocket = iceTcpServerSocketWrapper.getTCPSocket();

        // Then: null is returned because no sockets are added
        assertNull(tcpSocket);
    }

    @Test
    void testGetUDPSocket() {
        // When: getUDPSocket is called
        DatagramSocket udpSocket = iceTcpServerSocketWrapper.getUDPSocket();

        // Then: null is returned
        assertNull(udpSocket);
    }
}
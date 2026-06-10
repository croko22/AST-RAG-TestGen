import org.ice4j.ice.HostCandidate;
import org.ice4j.ice.TcpHostCandidate;
import org.ice4j.socket.IceSocketWrapper;
import org.ice4j.stack.StunStack;
import org.ice4j.Component;
import org.ice4j.TransportAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TcpHostCandidateTest {

    @Mock
    private TransportAddress transportAddress;

    @Mock
    private Component parentComponent;

    @Mock
    private StunStack stunStack;

    @Mock
    private IceSocketWrapper iceSocketWrapper;

    private TcpHostCandidate tcpHostCandidate;

    @BeforeEach
    public void setup() {
        tcpHostCandidate = new TcpHostCandidate(transportAddress, parentComponent);
        when(tcpHostCandidate.getStunStack()).thenReturn(stunStack);
    }

    @Test
    public void testAddSocket() {
        // Given
        IceSocketWrapper socket = mock(IceSocketWrapper.class);

        // When
        tcpHostCandidate.addSocket(socket);

        // Then
        assertEquals(1, tcpHostCandidate.sockets.size());
        assertTrue(tcpHostCandidate.sockets.contains(socket));
    }

    @Test
    public void testGetCandidateIceSocketWrapper_Found() {
        // Given
        SocketAddress remoteAddress = new InetSocketAddress("localhost", 8080);
        IceSocketWrapper socket = mock(IceSocketWrapper.class);
        when(socket.getTCPSocket().getRemoteSocketAddress()).thenReturn(remoteAddress);
        tcpHostCandidate.addSocket(socket);

        // When
        IceSocketWrapper result = tcpHostCandidate.getCandidateIceSocketWrapper(remoteAddress);

        // Then
        assertNotNull(result);
        assertEquals(socket, result);
    }

    @Test
    public void testGetCandidateIceSocketWrapper_NotFound() {
        // Given
        SocketAddress remoteAddress = new InetSocketAddress("localhost", 8080);

        // When
        IceSocketWrapper result = tcpHostCandidate.getCandidateIceSocketWrapper(remoteAddress);

        // Then
        assertNull(result);
    }

    @Test
    public void testFree() {
        // Given
        IceSocketWrapper socket = mock(IceSocketWrapper.class);
        Socket tcpSocket = mock(java.net.Socket.class);
        when(socket.getTCPSocket()).thenReturn(tcpSocket);
        when(tcpSocket.getInetAddress()).thenReturn(java.net.InetAddress.getByName("localhost"));
        when(tcpSocket.getPort()).thenReturn(8080);
        tcpHostCandidate.addSocket(socket);

        // When
        tcpHostCandidate.free();

        // Then
        verify(stunStack, times(1)).removeSocket(any(TransportAddress.class), any(TransportAddress.class));
        verify(socket, times(1)).close();
    }
}
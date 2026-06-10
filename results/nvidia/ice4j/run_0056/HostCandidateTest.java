import org.ice4j.ice.HostCandidate;
import org.ice4j.ice.Component;
import org.ice4j.ice.CandidateType;
import org.ice4j.ice.CandidateExtendedType;
import org.ice4j.ice.Transport;
import org.ice4j.ice.TransportAddress;
import org.ice4j.socket.IceSocketWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HostCandidateTest {

    @Mock
    private IceSocketWrapper socket;

    @Mock
    private Component parentComponent;

    @Mock
    private TransportAddress transportAddress;

    private HostCandidate hostCandidate;

    @BeforeEach
    public void setup() {
        hostCandidate = new HostCandidate(socket, parentComponent);
    }

    @Test
    public void testGetCandidateIceSocketWrapper() {
        // When
        IceSocketWrapper result = hostCandidate.getCandidateIceSocketWrapper();

        // Then
        assertEquals(socket, result);
    }

    @Test
    public void testCreateHostCandidateWithSocket() {
        // Given
        when(socket.getLocalAddress()).thenReturn(InetAddress.getLoopbackAddress());
        when(socket.getLocalPort()).thenReturn(1234);

        // When
        HostCandidate hostCandidate = new HostCandidate(socket, parentComponent);

        // Then
        assertNotNull(hostCandidate);
        assertEquals(socket, hostCandidate.getCandidateIceSocketWrapper());
    }

    @Test
    public void testCreateHostCandidateWithTransportAddress() {
        // Given
        when(transportAddress.getAddress()).thenReturn(InetAddress.getLoopbackAddress());
        when(transportAddress.getPort()).thenReturn(1234);

        // When
        HostCandidate hostCandidate = new HostCandidate(transportAddress, parentComponent);

        // Then
        assertNotNull(hostCandidate);
        assertNull(hostCandidate.getCandidateIceSocketWrapper());
    }

    @Test
    public void testCreateHostCandidateWithSocketAndTransport() {
        // Given
        when(socket.getLocalAddress()).thenReturn(InetAddress.getLoopbackAddress());
        when(socket.getLocalPort()).thenReturn(1234);

        // When
        HostCandidate hostCandidate = new HostCandidate(socket, parentComponent, Transport.UDP);

        // Then
        assertNotNull(hostCandidate);
        assertEquals(socket, hostCandidate.getCandidateIceSocketWrapper());
    }

    @Test
    public void testCreateStunDatagramPacketFilter() {
        // Given
        TransportAddress serverAddress = mock(TransportAddress.class);

        // When
        StunDatagramPacketFilter filter = hostCandidate.createStunDatagramPacketFilter(serverAddress);

        // Then
        assertNotNull(filter);
        assertTrue(filter instanceof TurnDatagramPacketFilter);
    }
}
import org.ice4j.ice.GoogleRelayedCandidate;
import org.ice4j.ice.harvest.GoogleTurnCandidateHarvest;
import org.ice4j.socket.GoogleRelayedCandidateDatagramSocket;
import org.ice4j.socket.GoogleRelayedCandidateSocket;
import org.ice4j.socket.IceSocketWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GoogleRelayedCandidateTest {

    @Mock
    private GoogleTurnCandidateHarvest turnCandidateHarvest;

    @Mock
    private GoogleRelayedCandidateDatagramSocket relayedCandidateDatagramSocket;

    @Mock
    private GoogleRelayedCandidateSocket relayedCandidateSocket;

    @Mock
    private IceSocketWrapper iceSocketWrapper;

    private GoogleRelayedCandidate googleRelayedCandidate;

    @BeforeEach
    void setup() throws SocketException {
        googleRelayedCandidate = new GoogleRelayedCandidate(
                new TransportAddress(InetAddress.getByName("127.0.0.1"), 1234, Transport.UDP),
                turnCandidateHarvest,
                new TransportAddress(InetAddress.getByName("127.0.0.1"), 1234, Transport.UDP),
                "username",
                "password"
        );
    }

    @Test
    void testGetCandidateIceSocketWrapper() {
        // Given
        when(turnCandidateHarvest.getTransport()).thenReturn(Transport.UDP);
        when(relayedCandidateDatagramSocket.getLocalAddress()).thenReturn(InetAddress.getByName("127.0.0.1"));
        when(relayedCandidateDatagramSocket.getLocalPort()).thenReturn(1234);

        // When
        IceSocketWrapper iceSocketWrapper = googleRelayedCandidate.getCandidateIceSocketWrapper();

        // Then
        assertNotNull(iceSocketWrapper);
        verify(relayedCandidateDatagramSocket, times(1)).getLocalAddress();
        verify(relayedCandidateDatagramSocket, times(1)).getLocalPort();
    }

    @Test
    void testGetCandidateIceSocketWrapperTcp() {
        // Given
        googleRelayedCandidate = new GoogleRelayedCandidate(
                new TransportAddress(InetAddress.getByName("127.0.0.1"), 1234, Transport.TCP),
                turnCandidateHarvest,
                new TransportAddress(InetAddress.getByName("127.0.0.1"), 1234, Transport.TCP),
                "username",
                "password"
        );
        when(turnCandidateHarvest.getTransport()).thenReturn(Transport.TCP);
        when(relayedCandidateSocket.getLocalAddress()).thenReturn(InetAddress.getByName("127.0.0.1"));
        when(relayedCandidateSocket.getLocalPort()).thenReturn(1234);

        // When
        IceSocketWrapper iceSocketWrapper = googleRelayedCandidate.getCandidateIceSocketWrapper();

        // Then
        assertNotNull(iceSocketWrapper);
        verify(relayedCandidateSocket, times(1)).getLocalAddress();
        verify(relayedCandidateSocket, times(1)).getLocalPort();
    }

    @Test
    void testGetPassword() {
        // Given
        String password = "password";

        // When
        String actualPassword = googleRelayedCandidate.getPassword();

        // Then
        assertEquals(password, actualPassword);
    }

    @Test
    void testGetRelayedCandidateDatagramSocket() throws SocketException {
        // Given
        whenNew(GoogleRelayedCandidateDatagramSocket.class).withAnyArguments().thenReturn(relayedCandidateDatagramSocket);

        // When
        GoogleRelayedCandidateDatagramSocket relayedCandidateDatagramSocket = googleRelayedCandidate.getRelayedCandidateDatagramSocket();

        // Then
        assertNotNull(relayedCandidateDatagramSocket);
        verify(relayedCandidateDatagramSocket, times(1)).getLocalAddress();
        verify(relayedCandidateDatagramSocket, times(1)).getLocalPort();
    }

    @Test
    void testGetRelayedCandidateSocket() throws SocketException {
        // Given
        whenNew(GoogleRelayedCandidateSocket.class).withAnyArguments().thenReturn(relayedCandidateSocket);

        // When
        GoogleRelayedCandidateSocket relayedCandidateSocket = googleRelayedCandidate.getRelayedCandidateSocket();

        // Then
        assertNotNull(relayedCandidateSocket);
        verify(relayedCandidateSocket, times(1)).getLocalAddress();
        verify(relayedCandidateSocket, times(1)).getLocalPort();
    }
}
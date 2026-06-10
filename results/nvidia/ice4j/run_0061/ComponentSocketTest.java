import org.ice4j.ice.Component;
import org.ice4j.ice.ComponentSocket;
import org.ice4j.ice.CandidatePair;
import org.ice4j.ice.CandidatePairState;
import org.ice4j.ice.IceMediaStream;
import org.ice4j.ice.LocalCandidate;
import org.ice4j.ice.RemoteCandidate;
import org.ice4j.ice.TransportAddress;
import org.ice4j.message.PropertyChangeEvent;
import org.ice4j.socket.IceSocketWrapper;
import org.ice4j.util.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.SocketAddress;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComponentSocketTest {

    @Mock
    private Component component;

    @Mock
    private IceMediaStream iceMediaStream;

    @Mock
    private Logger logger;

    @Mock
    private LocalCandidate localCandidate;

    @Mock
    private RemoteCandidate remoteCandidate;

    @Mock
    private CandidatePair candidatePair;

    @Mock
    private IceSocketWrapper iceSocketWrapper;

    private ComponentSocket componentSocket;

    @BeforeEach
    void setup() throws Exception {
        when(component.getParentStream()).thenReturn(iceMediaStream);
        componentSocket = new ComponentSocket(component, logger);
    }

    @Test
    void testPropertyChange_PairStateChanged() {
        // Given
        PropertyChangeEvent event = new PropertyChangeEvent(candidatePair, IceMediaStream.PROPERTY_PAIR_STATE_CHANGED, null, CandidatePairState.SUCCEEDED);
        when(candidatePair.getParentComponent()).thenReturn(component);
        when(candidatePair.getRemoteCandidate()).thenReturn(remoteCandidate);
        when(remoteCandidate.getTransportAddress()).thenReturn(mock(TransportAddress.class));

        // When
        componentSocket.propertyChange(event);

        // Then
        verify(componentSocket, times(1)).addAuthorizedAddress(any(TransportAddress.class));
    }

    @Test
    void testPropertyChange_PairNominated() {
        // Given
        PropertyChangeEvent event = new PropertyChangeEvent(candidatePair, IceMediaStream.PROPERTY_PAIR_NOMINATED, null, null);
        when(candidatePair.getParentComponent()).thenReturn(component);
        when(localCandidate.getBase()).thenReturn(null);
        when(candidatePair.getLocalCandidate()).thenReturn(localCandidate);
        when(candidatePair.getRemoteCandidate()).thenReturn(remoteCandidate);
        when(remoteCandidate.getTransportAddress()).thenReturn(mock(TransportAddress.class));
        when(localCandidate.getCandidateIceSocketWrapper(any(TransportAddress.class))).thenReturn(iceSocketWrapper);

        // When
        componentSocket.propertyChange(event);

        // Then
        verify(componentSocket, times(1)).initializeActive(iceSocketWrapper, any(TransportAddress.class));
    }

    @Test
    void testClose() {
        // When
        componentSocket.close();

        // Then
        verify(iceMediaStream, times(1)).removePairStateChangeListener(componentSocket);
        assertNull(componentSocket.component);
    }

    @Test
    void testAccept_AuthorizedAddress() {
        // Given
        DatagramPacket packet = mock(DatagramPacket.class);
        SocketAddress address = mock(SocketAddress.class);
        when(packet.getSocketAddress()).thenReturn(address);
        componentSocket.addAuthorizedAddress(address);

        // When
        boolean result = componentSocket.accept(packet);

        // Then
        assertTrue(result);
    }

    @Test
    void testAccept_UnauthorizedAddress() {
        // Given
        DatagramPacket packet = mock(DatagramPacket.class);
        SocketAddress address = mock(SocketAddress.class);
        when(packet.getSocketAddress()).thenReturn(address);

        // When
        boolean result = componentSocket.accept(packet);

        // Then
        assertFalse(result);
    }

    @Test
    void testAddAuthorizedAddress_NewAddress() {
        // Given
        TransportAddress address = mock(TransportAddress.class);
        Set<SocketAddress> authorizedAddresses = new HashSet<>();
        componentSocket.authorizedAddresses = authorizedAddresses;

        // When
        componentSocket.addAuthorizedAddress(address);

        // Then
        assertTrue(authorizedAddresses.contains(address));
    }

    @Test
    void testAddAuthorizedAddress_ExistingAddress() {
        // Given
        TransportAddress address = mock(TransportAddress.class);
        Set<SocketAddress> authorizedAddresses = new HashSet<>();
        authorizedAddresses.add(address);
        componentSocket.authorizedAddresses = authorizedAddresses;

        // When
        componentSocket.addAuthorizedAddress(address);

        // Then
        assertEquals(1, authorizedAddresses.size());
    }
}
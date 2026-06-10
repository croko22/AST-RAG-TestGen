import org.ice4j.TransportAddress;
import org.ice4j.socket.IceSocketWrapper;
import org.ice4j.socket.IceUdpSocketWrapper;
import org.ice4j.stunclient.SimpleAddressDetector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StunMappingCandidateHarvesterTest {

    @Mock
    private TransportAddress localAddress;

    @Mock
    private TransportAddress stunServerAddress;

    @Mock
    private SimpleAddressDetector simpleAddressDetector;

    @Mock
    private IceSocketWrapper iceSocketWrapper;

    @Mock
    private DatagramSocket datagramSocket;

    @InjectMocks
    private StunMappingCandidateHarvester stunMappingCandidateHarvester;

    @BeforeEach
    void setup() {
        stunMappingCandidateHarvester = new StunMappingCandidateHarvester(localAddress, stunServerAddress);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(localAddress, stunServerAddress, simpleAddressDetector, iceSocketWrapper, datagramSocket);
    }

    @Test
    public void testDiscover_PublicAddressDiscovered() {
        // Given
        when(simpleAddressDetector.getMappingFor(any(IceSocketWrapper.class))).thenReturn(new TransportAddress());
        when(localAddress.getTransport()).thenReturn(Transport.UDP);
        when(iceSocketWrapper.getLocalSocketAddress()).thenReturn(new InetSocketAddress("localhost", 1234));

        // When
        stunMappingCandidateHarvester.discover();

        // Then
        assertNotNull(stunMappingCandidateHarvester.getMask());
        verify(simpleAddressDetector, times(1)).getMappingFor(any(IceSocketWrapper.class));
        verify(iceSocketWrapper, times(1)).getLocalSocketAddress();
    }

    @Test
    public void testDiscover_PublicAddressNotDiscovered() {
        // Given
        when(simpleAddressDetector.getMappingFor(any(IceSocketWrapper.class))).thenReturn(null);

        // When
        stunMappingCandidateHarvester.discover();

        // Then
        assertNull(stunMappingCandidateHarvester.getMask());
        verify(simpleAddressDetector, times(1)).getMappingFor(any(IceSocketWrapper.class));
    }

    @Test
    public void testDiscover_ThrowsException() {
        // Given
        when(simpleAddressDetector.getMappingFor(any(IceSocketWrapper.class))).thenThrow(new RuntimeException());

        // When
        assertDoesNotThrow(() -> stunMappingCandidateHarvester.discover());

        // Then
        verify(simpleAddressDetector, times(1)).getMappingFor(any(IceSocketWrapper.class));
    }

    @Test
    public void testGetFace() {
        // Given

        // When
        TransportAddress face = stunMappingCandidateHarvester.getFace();

        // Then
        assertEquals(localAddress, face);
    }

    @Test
    public void testGetMask_PublicAddressDiscovered() {
        // Given
        when(simpleAddressDetector.getMappingFor(any(IceSocketWrapper.class))).thenReturn(new TransportAddress());
        stunMappingCandidateHarvester.discover();

        // When
        TransportAddress mask = stunMappingCandidateHarvester.getMask();

        // Then
        assertNotNull(mask);
    }

    @Test
    public void testGetMask_PublicAddressNotDiscovered() {
        // Given

        // When
        TransportAddress mask = stunMappingCandidateHarvester.getMask();

        // Then
        assertNull(mask);
    }
}
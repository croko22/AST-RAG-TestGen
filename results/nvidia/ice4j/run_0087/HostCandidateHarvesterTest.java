import org.ice4j.ice.Component;
import org.ice4j.ice.Transport;
import org.ice4j.ice.harvest.HostCandidateHarvester;
import org.ice4j.ice.harvest.HarvestStatistics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HostCandidateHarvesterTest {

    @Mock
    private Component component;

    @Mock
    private NetworkInterface networkInterface;

    private HostCandidateHarvester hostCandidateHarvester;

    @BeforeEach
    void setUp() {
        hostCandidateHarvester = new HostCandidateHarvester();
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(component, networkInterface);
    }

    @Test
    void testGetAllAllowedAddresses() {
        // Given
        when(networkInterface.getInetAddresses()).thenReturn(null);

        // When
        List<InetAddress> addresses = HostCandidateHarvester.getAllAllowedAddresses();

        // Then
        assertNotNull(addresses);
        assertTrue(addresses.isEmpty());
    }

    @Test
    void testHarvest() throws Exception {
        // Given
        when(component.getComponentSocket()).thenReturn(null);
        when(networkInterface.isLoopback()).thenReturn(false);
        when(networkInterface.isUp()).thenReturn(true);
        when(networkInterface.getInetAddresses()).thenReturn(null);

        // When
        hostCandidateHarvester.harvest(component, 1024, 1024, 65535, Transport.UDP);

        // Then
        verify(component, times(1)).getComponentSocket();
    }

    @Test
    void testHarvest_InvalidTransport() {
        // Given
        when(component.getComponentSocket()).thenReturn(null);
        when(networkInterface.isLoopback()).thenReturn(false);
        when(networkInterface.isUp()).thenReturn(true);
        when(networkInterface.getInetAddresses()).thenReturn(null);

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> hostCandidateHarvester.harvest(component, 1024, 1024, 65535, null));
    }

    @Test
    void testIsInterfaceAllowed() throws SocketException {
        // Given
        when(networkInterface.isLoopback()).thenReturn(false);
        when(networkInterface.isUp()).thenReturn(true);

        // When
        boolean allowed = HostCandidateHarvester.isInterfaceAllowed(networkInterface);

        // Then
        assertTrue(allowed);
    }

    @Test
    void testIsInterfaceAllowed_Loopback() throws SocketException {
        // Given
        when(networkInterface.isLoopback()).thenReturn(true);
        when(networkInterface.isUp()).thenReturn(true);

        // When
        boolean allowed = HostCandidateHarvester.isInterfaceAllowed(networkInterface);

        // Then
        assertFalse(allowed);
    }

    @Test
    void testIsInterfaceAllowed_Down() throws SocketException {
        // Given
        when(networkInterface.isLoopback()).thenReturn(false);
        when(networkInterface.isUp()).thenReturn(false);

        // When
        boolean allowed = HostCandidateHarvester.isInterfaceAllowed(networkInterface);

        // Then
        assertFalse(allowed);
    }

    @Test
    void testGetAllowedInterfaces() throws SocketException {
        // Given
        when(networkInterface.isLoopback()).thenReturn(false);
        when(networkInterface.isUp()).thenReturn(true);

        // When
        List<NetworkInterface> interfaces = HostCandidateHarvester.getAllowedInterfaces();

        // Then
        assertNotNull(interfaces);
        assertTrue(interfaces.isEmpty());
    }

    @Test
    void testGetHarvestStatistics() {
        // When
        HarvestStatistics statistics = hostCandidateHarvester.getHarvestStatistics();

        // Then
        assertNotNull(statistics);
    }
}
import org.ice4j.ice.UPNPCandidate;
import org.ice4j.message.Component;
import org.ice4j.socket.IceSocketWrapper;
import org.bitlet.weupnp.GatewayDevice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UPNPCandidateTest {

    @Mock
    private TransportAddress transportAddress;

    @Mock
    private LocalCandidate base;

    @Mock
    private Component parentComponent;

    @Mock
    private GatewayDevice device;

    @Mock
    private IceSocketWrapper iceSocketWrapper;

    private UPNPCandidate upnpCandidate;

    @BeforeEach
    void setup() {
        when(base.getCandidateIceSocketWrapper()).thenReturn(iceSocketWrapper);
        when(base.getIceSocketWrapper()).thenReturn(iceSocketWrapper);
        upnpCandidate = new UPNPCandidate(transportAddress, base, parentComponent, device);
    }

    @Test
    public void testGetCandidateIceSocketWrapper() {
        // When
        IceSocketWrapper result = upnpCandidate.getCandidateIceSocketWrapper();

        // Then
        assertEquals(iceSocketWrapper, result);
        verify(base, times(1)).getCandidateIceSocketWrapper();
    }

    @Test
    public void testGetIceSocketWrapper() {
        // When
        IceSocketWrapper result = upnpCandidate.getIceSocketWrapper();

        // Then
        assertEquals(iceSocketWrapper, result);
        verify(base, times(1)).getIceSocketWrapper();
    }

    @Test
    public void testFree() {
        // Given
        doThrow(new Exception()).when(device).deletePortMapping(any(), any());

        // When
        upnpCandidate.free();

        // Then
        verify(device, times(1)).deletePortMapping(any(), any());
        verify(iceSocketWrapper, times(1)).close();
        assertNull(upnpCandidate.device);
    }

    @Test
    public void testFree_NoException() {
        // When
        upnpCandidate.free();

        // Then
        verify(device, times(1)).deletePortMapping(any(), any());
        verify(iceSocketWrapper, times(1)).close();
        assertNull(upnpCandidate.device);
    }
}
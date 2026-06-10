import org.ice4j.ice.Agent;
import org.ice4j.ice.Component;
import org.ice4j.ice.IceMediaStream;
import org.ice4j.ice.LocalCandidate;
import org.ice4j.ice.TransportAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sdp.Media;
import javax.sdp.MediaDescription;
import javax.sdp.SessionDescription;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IceSdpUtilsTest {

    @Mock
    private SessionDescription sessionDescription;

    @Mock
    private MediaDescription mediaDescription;

    @Mock
    private Agent agent;

    @Mock
    private IceMediaStream iceMediaStream;

    @Mock
    private Component component;

    @Mock
    private LocalCandidate localCandidate;

    @Mock
    private TransportAddress transportAddress;

    @BeforeEach
    public void setup() {
        // Initialize mocks
        when(sessionDescription.getAttributes(anyBoolean())).thenReturn(new Vector<>());
        when(mediaDescription.getAttributes(anyBoolean())).thenReturn(new Vector<>());
        when(agent.getLocalUfrag()).thenReturn("localUfrag");
        when(agent.getLocalPassword()).thenReturn("localPassword");
        when(iceMediaStream.getName()).thenReturn("streamName");
        when(component.getDefaultCandidate()).thenReturn(localCandidate);
        when(localCandidate.getTransportAddress()).thenReturn(transportAddress);
        when(transportAddress.getHostAddress()).thenReturn("hostAddress");
        when(transportAddress.getPort()).thenReturn(1234);
    }

    @Test
    public void testSetIceCredentials() {
        // Given
        String uFrag = "uFrag";
        String pwd = "pwd";

        // When
        IceSdpUtils.setIceCredentials(sessionDescription, uFrag, pwd);

        // Then
        verify(sessionDescription).getAttributes(true);
        verify(sessionDescription).setAttributes(any(Vector.class));
    }

    @Test
    public void testSetIceCredentials_NullSessionDescription() {
        // Given
        String uFrag = "uFrag";
        String pwd = "pwd";
        SessionDescription nullSessionDescription = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> IceSdpUtils.setIceCredentials(nullSessionDescription, uFrag, pwd));
    }

    @Test
    public void testSetIceCredentials_NullUFrag() {
        // Given
        String uFrag = null;
        String pwd = "pwd";

        // When and Then
        assertThrows(NullPointerException.class, () -> IceSdpUtils.setIceCredentials(sessionDescription, uFrag, pwd));
    }

    @Test
    public void testSetIceCredentials_NullPwd() {
        // Given
        String uFrag = "uFrag";
        String pwd = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> IceSdpUtils.setIceCredentials(sessionDescription, uFrag, pwd));
    }

    @Test
    public void testInitMediaDescription() {
        // Given

        // When
        IceSdpUtils.initMediaDescription(mediaDescription, iceMediaStream);

        // Then
        verify(mediaDescription).setAttribute(anyString(), anyString());
        verify(mediaDescription).getAttributes(true);
        verify(mediaDescription).setMediaPort(anyInt());
        verify(mediaDescription).setConnection(any());
    }

    @Test
    public void testInitSessionDescription() {
        // Given

        // When
        IceSdpUtils.initSessionDescription(sessionDescription, agent);

        // Then
        verify(sessionDescription).getAttributes(true);
        verify(sessionDescription).setAttributes(any(Vector.class));
        verify(sessionDescription).setOrigin(any());
        verify(sessionDescription).setMediaDescriptions(any(Vector.class));
    }

    @Test
    public void testCreateTrickleUpdate() {
        // Given
        List<LocalCandidate> localCandidates = new ArrayList<>();
        localCandidates.add(localCandidate);

        // When
        Collection<Attribute> trickleUpdate = IceSdpUtils.createTrickleUpdate(localCandidates);

        // Then
        assertNotNull(trickleUpdate);
        assertEquals(2, trickleUpdate.size());
    }

    @Test
    public void testCreateTrickleUpdate_EmptyLocalCandidates() {
        // Given
        List<LocalCandidate> localCandidates = new ArrayList<>();

        // When
        Collection<Attribute> trickleUpdate = IceSdpUtils.createTrickleUpdate(localCandidates);

        // Then
        assertNotNull(trickleUpdate);
        assertEquals(1, trickleUpdate.size());
    }

    @Test
    public void testCreateTrickleUpdate_NullLocalCandidates() {
        // Given
        List<LocalCandidate> nullLocalCandidates = null;

        // When
        Collection<Attribute> trickleUpdate = IceSdpUtils.createTrickleUpdate(nullLocalCandidates);

        // Then
        assertNotNull(trickleUpdate);
        assertEquals(1, trickleUpdate.size());
    }
}
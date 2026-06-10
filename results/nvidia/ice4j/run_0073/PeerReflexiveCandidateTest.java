import org.ice4j.ice.PeerReflexiveCandidate;
import org.ice4j.socket.TransportAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PeerReflexiveCandidateTest {

    @Mock
    private TransportAddress transportAddress;

    @Mock
    private Component parentComponent;

    @Mock
    private LocalCandidate base;

    @Mock
    private IceSocketWrapper iceSocketWrapper;

    private PeerReflexiveCandidate peerReflexiveCandidate;

    @BeforeEach
    public void setup() {
        when(base.getCandidateIceSocketWrapper()).thenReturn(iceSocketWrapper);
        peerReflexiveCandidate = new PeerReflexiveCandidate(transportAddress, parentComponent, base, 10);
    }

    @Test
    public void testGetCandidateIceSocketWrapper() {
        // When
        IceSocketWrapper result = peerReflexiveCandidate.getCandidateIceSocketWrapper();

        // Then
        assertEquals(iceSocketWrapper, result);
        verify(base, times(1)).getCandidateIceSocketWrapper();
    }

    @Test
    public void testGetCandidateIceSocketWrapper_BaseReturnsNull() {
        // Given
        when(base.getCandidateIceSocketWrapper()).thenReturn(null);

        // When
        IceSocketWrapper result = peerReflexiveCandidate.getCandidateIceSocketWrapper();

        // Then
        assertNull(result);
        verify(base, times(1)).getCandidateIceSocketWrapper();
    }
}
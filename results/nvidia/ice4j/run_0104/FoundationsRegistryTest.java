import org.ice4j.ice.Candidate;
import org.ice4j.ice.TransportAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Hashtable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FoundationsRegistryTest {

    @Mock
    private Candidate<?> candidate;

    private FoundationsRegistry foundationsRegistry;

    @BeforeEach
    void setup() {
        foundationsRegistry = new FoundationsRegistry();
    }

    @Test
    public void testAssignFoundation_FirstTime() {
        // Given
        when(candidate.getType()).thenReturn(CandidateType.HOST_CANDIDATE);
        when(candidate.getBase()).thenReturn(new TransportAddress("192.168.1.1", 1234));
        when(candidate.getTransport()).thenReturn(Transport.TCP);

        // When
        foundationsRegistry.assignFoundation(candidate);

        // Then
        assertNotNull(candidate.getFoundation());
        assertEquals("1", candidate.getFoundation());
        assertEquals(1, foundationsRegistry.size());
    }

    @Test
    public void testAssignFoundation_SecondTime_SameFoundation() {
        // Given
        when(candidate.getType()).thenReturn(CandidateType.HOST_CANDIDATE);
        when(candidate.getBase()).thenReturn(new TransportAddress("192.168.1.1", 1234));
        when(candidate.getTransport()).thenReturn(Transport.TCP);
        foundationsRegistry.assignFoundation(candidate);

        Candidate<?> newCandidate = mock(Candidate.class);
        when(newCandidate.getType()).thenReturn(CandidateType.HOST_CANDIDATE);
        when(newCandidate.getBase()).thenReturn(new TransportAddress("192.168.1.1", 1234));
        when(newCandidate.getTransport()).thenReturn(Transport.TCP);

        // When
        foundationsRegistry.assignFoundation(newCandidate);

        // Then
        assertNotNull(newCandidate.getFoundation());
        assertEquals("1", newCandidate.getFoundation());
        assertEquals(1, foundationsRegistry.size());
    }

    @Test
    public void testAssignFoundation_SecondTime_DifferentFoundation() {
        // Given
        when(candidate.getType()).thenReturn(CandidateType.HOST_CANDIDATE);
        when(candidate.getBase()).thenReturn(new TransportAddress("192.168.1.1", 1234));
        when(candidate.getTransport()).thenReturn(Transport.TCP);
        foundationsRegistry.assignFoundation(candidate);

        Candidate<?> newCandidate = mock(Candidate.class);
        when(newCandidate.getType()).thenReturn(CandidateType.SERVER_REFLEXIVE_CANDIDATE);
        when(newCandidate.getBase()).thenReturn(new TransportAddress("192.168.1.2", 1234));
        when(newCandidate.getTransport()).thenReturn(Transport.UDP);

        // When
        foundationsRegistry.assignFoundation(newCandidate);

        // Then
        assertNotNull(newCandidate.getFoundation());
        assertEquals("2", newCandidate.getFoundation());
        assertEquals(2, foundationsRegistry.size());
    }

    @Test
    public void testObtainFoundationForPeerReflexiveCandidate() {
        // When
        String foundation = foundationsRegistry.obtainFoundationForPeerReflexiveCandidate();

        // Then
        assertNotNull(foundation);
        assertEquals("10000", foundation);
    }

    @Test
    public void testObtainFoundationForPeerReflexiveCandidate_MultipleTimes() {
        // When
        String foundation1 = foundationsRegistry.obtainFoundationForPeerReflexiveCandidate();
        String foundation2 = foundationsRegistry.obtainFoundationForPeerReflexiveCandidate();
        String foundation3 = foundationsRegistry.obtainFoundationForPeerReflexiveCandidate();

        // Then
        assertNotNull(foundation1);
        assertNotNull(foundation2);
        assertNotNull(foundation3);
        assertEquals("10000", foundation1);
        assertEquals("10001", foundation2);
        assertEquals("10002", foundation3);
    }

    @Test
    public void testSize() {
        // Given
        when(candidate.getType()).thenReturn(CandidateType.HOST_CANDIDATE);
        when(candidate.getBase()).thenReturn(new TransportAddress("192.168.1.1", 1234));
        when(candidate.getTransport()).thenReturn(Transport.TCP);
        foundationsRegistry.assignFoundation(candidate);

        // When
        int size = foundationsRegistry.size();

        // Then
        assertEquals(1, size);
    }

    @Test
    public void testSize_MultipleFoundations() {
        // Given
        when(candidate.getType()).thenReturn(CandidateType.HOST_CANDIDATE);
        when(candidate.getBase()).thenReturn(new TransportAddress("192.168.1.1", 1234));
        when(candidate.getTransport()).thenReturn(Transport.TCP);
        foundationsRegistry.assignFoundation(candidate);

        Candidate<?> newCandidate = mock(Candidate.class);
        when(newCandidate.getType()).thenReturn(CandidateType.SERVER_REFLEXIVE_CANDIDATE);
        when(newCandidate.getBase()).thenReturn(new TransportAddress("192.168.1.2", 1234));
        when(newCandidate.getTransport()).thenReturn(Transport.UDP);
        foundationsRegistry.assignFoundation(newCandidate);

        // When
        int size = foundationsRegistry.size();

        // Then
        assertEquals(2, size);
    }
}
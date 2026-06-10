import org.ice4j.ice.CandidateType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CandidateTypeTest {

    @Test
    public void testParse_PeerReflexiveCandidate() {
        // Given
        String candidateTypeName = "prflx";

        // When
        CandidateType candidateType = CandidateType.parse(candidateTypeName);

        // Then
        assertEquals(CandidateType.PEER_REFLEXIVE_CANDIDATE, candidateType);
    }

    @Test
    public void testParse_ServerReflexiveCandidate() {
        // Given
        String candidateTypeName = "srflx";

        // When
        CandidateType candidateType = CandidateType.parse(candidateTypeName);

        // Then
        assertEquals(CandidateType.SERVER_REFLEXIVE_CANDIDATE, candidateType);
    }

    @Test
    public void testParse_RelayedCandidate() {
        // Given
        String candidateTypeName = "relay";

        // When
        CandidateType candidateType = CandidateType.parse(candidateTypeName);

        // Then
        assertEquals(CandidateType.RELAYED_CANDIDATE, candidateType);
    }

    @Test
    public void testParse_HostCandidate() {
        // Given
        String candidateTypeName = "host";

        // When
        CandidateType candidateType = CandidateType.parse(candidateTypeName);

        // Then
        assertEquals(CandidateType.HOST_CANDIDATE, candidateType);
    }

    @Test
    public void testParse_LocalCandidate_OldName() {
        // Given
        String candidateTypeName = "local";

        // When
        CandidateType candidateType = CandidateType.parse(candidateTypeName);

        // Then
        assertEquals(CandidateType.HOST_CANDIDATE, candidateType);
    }

    @Test
    public void testParse_StunCandidate_OldName() {
        // Given
        String candidateTypeName = "stun";

        // When
        CandidateType candidateType = CandidateType.parse(candidateTypeName);

        // Then
        assertEquals(CandidateType.SERVER_REFLEXIVE_CANDIDATE, candidateType);
    }

    @Test
    public void testParse_InvalidCandidateType() {
        // Given
        String candidateTypeName = "invalid";

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            CandidateType.parse(candidateTypeName);
        });
    }

    @Test
    public void testToString_PeerReflexiveCandidate() {
        // Given
        CandidateType candidateType = CandidateType.PEER_REFLEXIVE_CANDIDATE;

        // When
        String typeName = candidateType.toString();

        // Then
        assertEquals("prflx", typeName);
    }

    @Test
    public void testToString_ServerReflexiveCandidate() {
        // Given
        CandidateType candidateType = CandidateType.SERVER_REFLEXIVE_CANDIDATE;

        // When
        String typeName = candidateType.toString();

        // Then
        assertEquals("srflx", typeName);
    }

    @Test
    public void testToString_RelayedCandidate() {
        // Given
        CandidateType candidateType = CandidateType.RELAYED_CANDIDATE;

        // When
        String typeName = candidateType.toString();

        // Then
        assertEquals("relay", typeName);
    }

    @Test
    public void testToString_HostCandidate() {
        // Given
        CandidateType candidateType = CandidateType.HOST_CANDIDATE;

        // When
        String typeName = candidateType.toString();

        // Then
        assertEquals("host", typeName);
    }

    @Test
    public void testToString_LocalCandidate() {
        // Given
        CandidateType candidateType = CandidateType.LOCAL_CANDIDATE;

        // When
        String typeName = candidateType.toString();

        // Then
        assertEquals("local", typeName);
    }

    @Test
    public void testToString_StunCandidate() {
        // Given
        CandidateType candidateType = CandidateType.STUN_CANDIDATE;

        // When
        String typeName = candidateType.toString();

        // Then
        assertEquals("stun", typeName);
    }
}
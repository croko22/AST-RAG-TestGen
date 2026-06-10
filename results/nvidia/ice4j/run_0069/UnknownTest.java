import org.ice4j.ice.CandidateExtendedType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CandidateExtendedTypeTest {

    @Test
    public void testToString_HostCandidate() {
        // Given: HOST_CANDIDATE
        CandidateExtendedType candidateExtendedType = CandidateExtendedType.HOST_CANDIDATE;

        // When: toString is called
        String result = candidateExtendedType.toString();

        // Then: result is "host"
        assertEquals("host", result);
    }

    @Test
    public void testToString_UpnpCandidate() {
        // Given: UPNP_CANDIDATE
        CandidateExtendedType candidateExtendedType = CandidateExtendedType.UPNP_CANDIDATE;

        // When: toString is called
        String result = candidateExtendedType.toString();

        // Then: result is "upnp"
        assertEquals("upnp", result);
    }

    @Test
    public void testToString_StunPeerReflexiveCandidate() {
        // Given: STUN_PEER_REFLEXIVE_CANDIDATE
        CandidateExtendedType candidateExtendedType = CandidateExtendedType.STUN_PEER_REFLEXIVE_CANDIDATE;

        // When: toString is called
        String result = candidateExtendedType.toString();

        // Then: result is "stun peer reflexive"
        assertEquals("stun peer reflexive", result);
    }

    @Test
    public void testToString_StunServerReflexiveCandidate() {
        // Given: STUN_SERVER_REFLEXIVE_CANDIDATE
        CandidateExtendedType candidateExtendedType = CandidateExtendedType.STUN_SERVER_REFLEXIVE_CANDIDATE;

        // When: toString is called
        String result = candidateExtendedType.toString();

        // Then: result is "stun server reflexive"
        assertEquals("stun server reflexive", result);
    }

    @Test
    public void testToString_TurnRelayedCandidate() {
        // Given: TURN_RELAYED_CANDIDATE
        CandidateExtendedType candidateExtendedType = CandidateExtendedType.TURN_RELAYED_CANDIDATE;

        // When: toString is called
        String result = candidateExtendedType.toString();

        // Then: result is "turn relayed"
        assertEquals("turn relayed", result);
    }

    @Test
    public void testToString_GoogleTurnRelayedCandidate() {
        // Given: GOOGLE_TURN_RELAYED_CANDIDATE
        CandidateExtendedType candidateExtendedType = CandidateExtendedType.GOOGLE_TURN_RELAYED_CANDIDATE;

        // When: toString is called
        String result = candidateExtendedType.toString();

        // Then: result is "google turn relayed"
        assertEquals("google turn relayed", result);
    }

    @Test
    public void testToString_GoogleTcpTurnRelayedCandidate() {
        // Given: GOOGLE_TCP_TURN_RELAYED_CANDIDATE
        CandidateExtendedType candidateExtendedType = CandidateExtendedType.GOOGLE_TCP_TURN_RELAYED_CANDIDATE;

        // When: toString is called
        String result = candidateExtendedType.toString();

        // Then: result is "google tcp turn relayed"
        assertEquals("google tcp turn relayed", result);
    }

    @Test
    public void testToString_JingleNodeCandidate() {
        // Given: JINGLE_NODE_CANDIDATE
        CandidateExtendedType candidateExtendedType = CandidateExtendedType.JINGLE_NODE_CANDIDATE;

        // When: toString is called
        String result = candidateExtendedType.toString();

        // Then: result is "jingle node"
        assertEquals("jingle node", result);
    }

    @Test
    public void testParse_HostCandidate() {
        // Given: "host"
        String extendedTypeName = "host";

        // When: parse is called
        CandidateExtendedType result = CandidateExtendedType.parse(extendedTypeName);

        // Then: result is HOST_CANDIDATE
        assertEquals(CandidateExtendedType.HOST_CANDIDATE, result);
    }

    @Test
    public void testParse_UpnpCandidate() {
        // Given: "upnp"
        String extendedTypeName = "upnp";

        // When: parse is called
        CandidateExtendedType result = CandidateExtendedType.parse(extendedTypeName);

        // Then: result is UPNP_CANDIDATE
        assertEquals(CandidateExtendedType.UPNP_CANDIDATE, result);
    }

    @Test
    public void testParse_StunPeerReflexiveCandidate() {
        // Given: "stun peer reflexive"
        String extendedTypeName = "stun peer reflexive";

        // When: parse is called
        CandidateExtendedType result = CandidateExtendedType.parse(extendedTypeName);

        // Then: result is STUN_PEER_REFLEXIVE_CANDIDATE
        assertEquals(CandidateExtendedType.STUN_PEER_REFLEXIVE_CANDIDATE, result);
    }

    @Test
    public void testParse_StunServerReflexiveCandidate() {
        // Given: "stun server reflexive"
        String extendedTypeName = "stun server reflexive";

        // When: parse is called
        CandidateExtendedType result = CandidateExtendedType.parse(extendedTypeName);

        // Then: result is STUN_SERVER_REFLEXIVE_CANDIDATE
        assertEquals(CandidateExtendedType.STUN_SERVER_REFLEXIVE_CANDIDATE, result);
    }

    @Test
    public void testParse_TurnRelayedCandidate() {
        // Given: "turn relayed"
        String extendedTypeName = "turn relayed";

        // When: parse is called
        CandidateExtendedType result = CandidateExtendedType.parse(extendedTypeName);

        // Then: result is TURN_RELAYED_CANDIDATE
        assertEquals(CandidateExtendedType.TURN_RELAYED_CANDIDATE, result);
    }

    @Test
    public void testParse_GoogleTurnRelayedCandidate() {
        // Given: "google turn relayed"
        String extendedTypeName = "google turn relayed";

        // When: parse is called
        CandidateExtendedType result = CandidateExtendedType.parse(extendedTypeName);

        // Then: result is GOOGLE_TURN_RELAYED_CANDIDATE
        assertEquals(CandidateExtendedType.GOOGLE_TURN_RELAYED_CANDIDATE, result);
    }

    @Test
    public void testParse_GoogleTcpTurnRelayedCandidate() {
        // Given: "google tcp turn relayed"
        String extendedTypeName = "google tcp turn relayed";

        // When: parse is called
        CandidateExtendedType result = CandidateExtendedType.parse(extendedTypeName);

        // Then: result is GOOGLE_TCP_TURN_RELAYED_CANDIDATE
        assertEquals(CandidateExtendedType.GOOGLE_TCP_TURN_RELAYED_CANDIDATE, result);
    }

    @Test
    public void testParse_JingleNodeCandidate() {
        // Given: "jingle node"
        String extendedTypeName = "jingle node";

        // When: parse is called
        CandidateExtendedType result = CandidateExtendedType.parse(extendedTypeName);

        // Then: result is JINGLE_NODE_CANDIDATE
        assertEquals(CandidateExtendedType.JINGLE_NODE_CANDIDATE, result);
    }

    @Test
    public void testParse_InvalidExtendedTypeName() {
        // Given: "invalid"
        String extendedTypeName = "invalid";

        // When: parse is called
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CandidateExtendedType.parse(extendedTypeName);
        });

        // Then: exception is thrown
        assertNotNull(exception);
        assertEquals(extendedTypeName + " is not a currently supported CandidateExtendedType", exception.getMessage());
    }
}
import org.ice4j.ice.Candidate;
import org.ice4j.ice.CandidateType;
import org.ice4j.ice.Component;
import org.ice4j.ice.LocalCandidate;
import org.ice4j.ice.RemoteCandidate;
import org.ice4j.message.Request;
import org.ice4j.transport.Transport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CandidateTest {

    @Mock
    private Component component;

    @Mock
    private TransportAddress transportAddress;

    private Candidate<Candidate<?>> candidate;

    @BeforeEach
    public void setup() {
        candidate = new LocalCandidate(transportAddress, component, CandidateType.HOST_CANDIDATE, null);
    }

    @Test
    public void testGetType() {
        // Given
        when(component.getComponentID()).thenReturn(1);
        when(transportAddress.toString()).thenReturn("192.168.1.1:1234");

        // When
        CandidateType type = candidate.getType();

        // Then
        assertEquals(CandidateType.HOST_CANDIDATE, type);
    }

    @Test
    public void testSetCandidateType() {
        // Given
        CandidateType newType = CandidateType.SERVER_REFLEXIVE_CANDIDATE;

        // When
        candidate.setCandidateType(newType);

        // Then
        assertEquals(newType, candidate.getType());
    }

    @Test
    public void testGetFoundation() {
        // Given
        String foundation = "foundation";

        // When
        candidate.setFoundation(foundation);

        // Then
        assertEquals(foundation, candidate.getFoundation());
    }

    @Test
    public void testSetFoundation() {
        // Given
        String newFoundation = "newFoundation";

        // When
        candidate.setFoundation(newFoundation);

        // Then
        assertEquals(newFoundation, candidate.getFoundation());
    }

    @Test
    public void testGetBase() {
        // Given
        Candidate<?> base = mock(Candidate.class);

        // When
        candidate.setBase(base);

        // Then
        assertEquals(base, candidate.getBase());
    }

    @Test
    public void testSetBase() {
        // Given
        Candidate<?> newBase = mock(Candidate.class);

        // When
        candidate.setBase(newBase);

        // Then
        assertEquals(newBase, candidate.getBase());
    }

    @Test
    public void testGetPriority() {
        // Given
        long priority = 100;

        // When
        candidate.computePriority();

        // Then
        assertNotEquals(0, candidate.getPriority());
    }

    @Test
    public void testGetTransportAddress() {
        // Given

        // When

        // Then
        assertEquals(transportAddress, candidate.getTransportAddress());
    }

    @Test
    public void testEquals() {
        // Given
        Candidate<?> other = new LocalCandidate(transportAddress, component, CandidateType.HOST_CANDIDATE, null);

        // When
        boolean equals = candidate.equals(other);

        // Then
        assertTrue(equals);
    }

    @Test
    public void testHashCode() {
        // Given

        // When
        int hashCode = candidate.hashCode();

        // Then
        assertNotEquals(0, hashCode);
    }

    @Test
    public void testGetParentComponent() {
        // Given

        // When
        Component parent = candidate.getParentComponent();

        // Then
        assertEquals(component, parent);
    }

    @Test
    public void testComputePriority() {
        // Given

        // When
        long priority = candidate.computePriority();

        // Then
        assertNotEquals(0, priority);
    }

    @Test
    public void testComputeGTalkPriority() {
        // Given

        // When
        long priority = candidate.computeGTalkPriority();

        // Then
        assertNotEquals(0, priority);
    }

    @Test
    public void testComputePriorityForType() {
        // Given
        CandidateType type = CandidateType.SERVER_REFLEXIVE_CANDIDATE;

        // When
        long priority = candidate.computePriorityForType(type);

        // Then
        assertNotEquals(0, priority);
    }

    @Test
    public void testComputeGTalkPriorityForType() {
        // Given
        CandidateType type = CandidateType.SERVER_REFLEXIVE_CANDIDATE;

        // When
        long priority = candidate.computeGTalkPriorityForType(type);

        // Then
        assertNotEquals(0, priority);
    }

    @Test
    public void testIsVirtual() {
        // Given
        candidate.setVirtual(true);

        // When
        boolean isVirtual = candidate.isVirtual();

        // Then
        assertTrue(isVirtual);
    }

    @Test
    public void testSetVirtual() {
        // Given
        boolean virtual = true;

        // When
        candidate.setVirtual(virtual);

        // Then
        assertTrue(candidate.isVirtual());
    }

    @Test
    public void testGetStunServerAddress() {
        // Given
        TransportAddress stunServerAddress = mock(TransportAddress.class);
        candidate.setStunServerAddress(stunServerAddress);

        // When
        TransportAddress address = candidate.getStunServerAddress();

        // Then
        assertEquals(stunServerAddress, address);
    }

    @Test
    public void testGetRelayServerAddress() {
        // Given
        TransportAddress relayServerAddress = mock(TransportAddress.class);
        candidate.setRelayServerAddress(relayServerAddress);

        // When
        TransportAddress address = candidate.getRelayServerAddress();

        // Then
        assertEquals(relayServerAddress, address);
    }

    @Test
    public void testGetMappedAddress() {
        // Given
        TransportAddress mappedAddress = mock(TransportAddress.class);
        candidate.setMappedAddress(mappedAddress);

        // When
        TransportAddress address = candidate.getMappedAddress();

        // Then
        assertEquals(mappedAddress, address);
    }

    @Test
    public void testGetTransport() {
        // Given
        Transport transport = Transport.UDP;

        // When
        Transport transport1 = candidate.getTransport();

        // Then
        assertEquals(transport, transport1);
    }

    @Test
    public void testGetRelatedAddress() {
        // Given
        TransportAddress relatedAddress = mock(TransportAddress.class);
        candidate.setRelatedCandidate(new LocalCandidate(relatedAddress, component, CandidateType.HOST_CANDIDATE, null));

        // When
        TransportAddress address = candidate.getRelatedAddress();

        // Then
        assertEquals(relatedAddress, address);
    }

    @Test
    public void testToString() {
        // Given

        // When
        String string = candidate.toString();

        // Then
        assertNotNull(string);
    }

    @Test
    public void testToRedactedString() {
        // Given

        // When
        String string = candidate.toRedactedString();

        // Then
        assertNotNull(string);
    }

    @Test
    public void testToShortString() {
        // Given

        // When
        String string = candidate.toShortString();

        // Then
        assertNotNull(string);
    }

    @Test
    public void testToRedactedShortString() {
        // Given

        // When
        String string = candidate.toRedactedShortString();

        // Then
        assertNotNull(string);
    }

    @Test
    public void testCanReach() {
        // Given
        Candidate<?> other = new LocalCandidate(transportAddress, component, CandidateType.HOST_CANDIDATE, null);

        // When
        boolean canReach = candidate.canReach(other);

        // Then
        assertTrue(canReach);
    }

    @Test
    public void testIsDefault() {
        // Given

        // When
        boolean isDefault = candidate.isDefault();

        // Then
        assertFalse(isDefault);
    }

    @Test
    public void testGetUfrag() {
        // Given

        // When
        String ufrag = candidate.getUfrag();

        // Then
        assertNotNull(ufrag);
    }

    @Test
    public void testGetHostAddress() {
        // Given

        // When
        TransportAddress address = candidate.getHostAddress();

        // Then
        assertEquals(transportAddress, address);
    }

    @Test
    public void testGetReflexiveAddress() {
        // Given

        // When
        TransportAddress address = candidate.getReflexiveAddress();

        // Then
        assertNull(address);
    }

    @Test
    public void testGetRelatedCandidate() {
        // Given
        Candidate<?> relatedCandidate = new LocalCandidate(transportAddress, component, CandidateType.HOST_CANDIDATE, null);
        candidate.setRelatedCandidate(relatedCandidate);

        // When
        Candidate<?> candidate1 = candidate.getRelatedCandidate();

        // Then
        assertEquals(relatedCandidate, candidate1);
    }

    @Test
    public void testCompareTo() {
        // Given
        Candidate<?> other = new LocalCandidate(transportAddress, component, CandidateType.HOST_CANDIDATE, null);

        // When
        int compare = candidate.compareTo(other);

        // Then
        assertEquals(0, compare);
    }

    @Test
    public void testGetTcpType() {
        // Given

        // When
        CandidateTcpType tcpType = candidate.getTcpType();

        // Then
        assertNull(tcpType);
    }

    @Test
    public void testSetTcpType() {
        // Given
        CandidateTcpType tcpType = CandidateTcpType.ACTIVE;

        // When
        candidate.setTcpType(tcpType);

        // Then
        assertEquals(tcpType, candidate.getTcpType());
    }
}
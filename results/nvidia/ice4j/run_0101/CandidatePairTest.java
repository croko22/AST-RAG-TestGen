import org.ice4j.ice.CandidatePair;
import org.ice4j.ice.CandidatePairState;
import org.ice4j.ice.Component;
import org.ice4j.ice.LocalCandidate;
import org.ice4j.ice.RemoteCandidate;
import org.ice4j.stack.TransactionID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CandidatePairTest {

    @Mock
    private LocalCandidate localCandidate;

    @Mock
    private RemoteCandidate remoteCandidate;

    @Mock
    private Component component;

    private CandidatePair candidatePair;

    @BeforeEach
    public void setup() {
        when(localCandidate.getParentComponent()).thenReturn(component);
        when(remoteCandidate.getParentComponent()).thenReturn(component);
        candidatePair = new CandidatePair(localCandidate, remoteCandidate);
    }

    @Test
    public void testGetFoundation() {
        // Given
        String localFoundation = "localFoundation";
        String remoteFoundation = "remoteFoundation";
        when(localCandidate.getFoundation()).thenReturn(localFoundation);
        when(remoteCandidate.getFoundation()).thenReturn(remoteFoundation);

        // When
        String foundation = candidatePair.getFoundation();

        // Then
        assertEquals(localFoundation + remoteFoundation, foundation);
    }

    @Test
    public void testGetLocalCandidate() {
        // When
        LocalCandidate localCandidateResult = candidatePair.getLocalCandidate();

        // Then
        assertEquals(localCandidate, localCandidateResult);
    }

    @Test
    public void testGetRemoteCandidate() {
        // When
        RemoteCandidate remoteCandidateResult = candidatePair.getRemoteCandidate();

        // Then
        assertEquals(remoteCandidate, remoteCandidateResult);
    }

    @Test
    public void testGetState() {
        // When
        CandidatePairState state = candidatePair.getState();

        // Then
        assertEquals(CandidatePairState.FROZEN, state);
    }

    @Test
    public void testSetStateFailed() {
        // When
        candidatePair.setStateFailed();

        // Then
        assertEquals(CandidatePairState.FAILED, candidatePair.getState());
    }

    @Test
    public void testSetStateFrozen() {
        // When
        candidatePair.setStateFrozen();

        // Then
        assertEquals(CandidatePairState.FROZEN, candidatePair.getState());
    }

    @Test
    public void testSetStateInProgress() {
        // Given
        TransactionID transactionID = mock(TransactionID.class);

        // When
        candidatePair.setStateInProgress(transactionID);

        // Then
        assertEquals(CandidatePairState.IN_PROGRESS, candidatePair.getState());
        assertEquals(transactionID, candidatePair.getConnectivityCheckTransaction());
    }

    @Test
    public void testSetStateSucceeded() {
        // When
        candidatePair.setStateSucceeded();

        // Then
        assertEquals(CandidatePairState.SUCCEEDED, candidatePair.getState());
    }

    @Test
    public void testSetStateWaiting() {
        // When
        candidatePair.setStateWaiting();

        // Then
        assertEquals(CandidatePairState.WAITING, candidatePair.getState());
    }

    @Test
    public void testIsFrozen() {
        // When
        boolean isFrozen = candidatePair.isFrozen();

        // Then
        assertTrue(isFrozen);
    }

    @Test
    public void testGetControllingAgentCandidate() {
        // Given
        when(component.getParentAgent().isControlling()).thenReturn(true);

        // When
        Candidate<?> controllingAgentCandidate = candidatePair.getControllingAgentCandidate();

        // Then
        assertEquals(localCandidate, controllingAgentCandidate);
    }

    @Test
    public void testGetControlledAgentCandidate() {
        // Given
        when(component.getParentAgent().isControlling()).thenReturn(true);

        // When
        Candidate<?> controlledAgentCandidate = candidatePair.getControlledAgentCandidate();

        // Then
        assertEquals(remoteCandidate, controlledAgentCandidate);
    }

    @Test
    public void testGetPriority() {
        // Given
        long localPriority = 10L;
        long remotePriority = 20L;
        when(localCandidate.getPriority()).thenReturn(localPriority);
        when(remoteCandidate.getPriority()).thenReturn(remotePriority);

        // When
        long priority = candidatePair.getPriority();

        // Then
        assertEquals(2L * 32L * Math.min(localPriority, remotePriority) + 2L * Math.max(localPriority, remotePriority) + (localPriority > remotePriority ? 1L : 0L), priority);
    }

    @Test
    public void testCompareTo() {
        // Given
        CandidatePair otherCandidatePair = mock(CandidatePair.class);
        when(otherCandidatePair.getPriority()).thenReturn(30L);

        // When
        int comparison = candidatePair.compareTo(otherCandidatePair);

        // Then
        assertEquals(1, comparison);
    }

    @Test
    public void testEquals() {
        // Given
        CandidatePair otherCandidatePair = new CandidatePair(localCandidate, remoteCandidate);

        // When
        boolean equals = candidatePair.equals(otherCandidatePair);

        // Then
        assertTrue(equals);
    }

    @Test
    public void testHashCode() {
        // When
        int hashCode = candidatePair.hashCode();

        // Then
        assertEquals(Objects.hash(localCandidate), hashCode);
    }

    @Test
    public void testToString() {
        // When
        String toString = candidatePair.toString();

        // Then
        assertNotNull(toString);
    }

    @Test
    public void testToRedactedString() {
        // When
        String toRedactedString = candidatePair.toRedactedString();

        // Then
        assertNotNull(toRedactedString);
    }

    @Test
    public void testToShortString() {
        // When
        String toShortString = candidatePair.toShortString();

        // Then
        assertNotNull(toShortString);
    }

    @Test
    public void testToRedactedShortString() {
        // When
        String toRedactedShortString = candidatePair.toRedactedShortString();

        // Then
        assertNotNull(toRedactedShortString);
    }

    @Test
    public void testGetParentComponent() {
        // When
        Component parentComponent = candidatePair.getParentComponent();

        // Then
        assertEquals(component, parentComponent);
    }

    @Test
    public void testGetConnectivityCheckTransaction() {
        // When
        TransactionID connectivityCheckTransaction = candidatePair.getConnectivityCheckTransaction();

        // Then
        assertNull(connectivityCheckTransaction);
    }

    @Test
    public void testSetUseCandidateSent() {
        // When
        candidatePair.setUseCandidateSent();

        // Then
        assertTrue(candidatePair.useCandidateSent());
    }

    @Test
    public void testUseCandidateSent() {
        // When
        boolean useCandidateSent = candidatePair.useCandidateSent();

        // Then
        assertFalse(useCandidateSent);
    }

    @Test
    public void testSetUseCandidateReceived() {
        // When
        candidatePair.setUseCandidateReceived();

        // Then
        assertTrue(candidatePair.useCandidateReceived());
    }

    @Test
    public void testUseCandidateReceived() {
        // When
        boolean useCandidateReceived = candidatePair.useCandidateReceived();

        // Then
        assertFalse(useCandidateReceived);
    }

    @Test
    public void testNominate() {
        // When
        candidatePair.nominate();

        // Then
        assertTrue(candidatePair.isNominated());
    }

    @Test
    public void testIsNominated() {
        // When
        boolean isNominated = candidatePair.isNominated();

        // Then
        assertFalse(isNominated);
    }

    @Test
    public void testIsValid() {
        // When
        boolean isValid = candidatePair.isValid();

        // Then
        assertFalse(isValid);
    }

    @Test
    public void testGetConsentFreshness() {
        // When
        long consentFreshness = candidatePair.getConsentFreshness();

        // Then
        assertEquals(CandidatePair.CONSENT_FRESHNESS_UNKNOWN, consentFreshness);
    }

    @Test
    public void testGetDatagramSocket() {
        // When
        DatagramSocket datagramSocket = candidatePair.getDatagramSocket();

        // Then
        assertNull(datagramSocket);
    }

    @Test
    public void testGetSocket() {
        // When
        Socket socket = candidatePair.getSocket();

        // Then
        assertNull(socket);
    }

    @Test
    public void testGetIceSocketWrapper() {
        // When
        IceSocketWrapper iceSocketWrapper = candidatePair.getIceSocketWrapper();

        // Then
        assertNull(iceSocketWrapper);
    }
}
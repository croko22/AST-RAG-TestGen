import org.ice4j.ice.RemoteCandidate;
import org.ice4j.message.Component;
import org.ice4j.message.CandidateType;
import org.ice4j.message.TransportAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RemoteCandidateTest {

    @Mock
    private TransportAddress transportAddress;

    @Mock
    private Component parentComponent;

    @Mock
    private RemoteCandidate relatedCandidate;

    private RemoteCandidate remoteCandidate;

    @BeforeEach
    void setup() {
        remoteCandidate = new RemoteCandidate(transportAddress, parentComponent, CandidateType.HOST_CANDIDATE, "foundation", 1, relatedCandidate);
    }

    @Test
    void testSetPriority() {
        // Given
        long priority = 10;

        // When
        remoteCandidate.setPriority(priority);

        // Then
        assertEquals(priority, remoteCandidate.priority);
    }

    @Test
    void testIsDefault_WithParentComponent() {
        // Given
        when(parentComponent.getDefaultRemoteCandidate()).thenReturn(remoteCandidate);

        // When
        boolean result = remoteCandidate.isDefault();

        // Then
        assertTrue(result);
        verify(parentComponent, times(1)).getDefaultRemoteCandidate();
    }

    @Test
    void testIsDefault_WithoutParentComponent() {
        // Given
        when(parentComponent.getDefaultRemoteCandidate()).thenReturn(null);
        remoteCandidate = new RemoteCandidate(transportAddress, null, CandidateType.HOST_CANDIDATE, "foundation", 1, relatedCandidate);

        // When
        boolean result = remoteCandidate.isDefault();

        // Then
        assertFalse(result);
    }

    @Test
    void testGetUfrag() {
        // Given
        String ufrag = "ufrag";
        remoteCandidate = new RemoteCandidate(transportAddress, parentComponent, CandidateType.HOST_CANDIDATE, "foundation", 1, relatedCandidate, ufrag);

        // When
        String result = remoteCandidate.getUfrag();

        // Then
        assertEquals(ufrag, result);
    }

    @Test
    void testFindRelatedCandidate() {
        // Given
        TransportAddress relatedAddress = mock(TransportAddress.class);
        RemoteCandidate relatedRemoteCandidate = mock(RemoteCandidate.class);
        when(parentComponent.findRemoteCandidate(relatedAddress)).thenReturn(relatedRemoteCandidate);

        // When
        RemoteCandidate result = remoteCandidate.findRelatedCandidate(relatedAddress);

        // Then
        assertEquals(relatedRemoteCandidate, result);
        verify(parentComponent, times(1)).findRemoteCandidate(relatedAddress);
    }

    @Test
    void testFindRelatedCandidate_NullRelatedAddress() {
        // Given
        TransportAddress relatedAddress = null;

        // When
        RemoteCandidate result = remoteCandidate.findRelatedCandidate(relatedAddress);

        // Then
        assertNotNull(result);
        verify(parentComponent, times(1)).findRemoteCandidate(relatedAddress);
    }
}
import org.ice4j.ice.CheckList;
import org.ice4j.ice.CheckListState;
import org.ice4j.ice.Component;
import org.ice4j.ice.CandidatePair;
import org.ice4j.ice.CandidatePairState;
import org.ice4j.ice.IceMediaStream;
import org.ice4j.ice.LocalCandidate;
import org.ice4j.ice.RemoteCandidate;
import org.ice4j.ice.TransportAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckListTest {

    @Mock
    private IceMediaStream parentStream;

    @Mock
    private LocalCandidate localCandidate;

    @Mock
    private RemoteCandidate remoteCandidate;

    @Mock
    private Component component;

    @Mock
    private PropertyChangeListener propertyChangeListener;

    private CheckList checkList;

    @BeforeEach
    public void setup() {
        checkList = new CheckList(parentStream);
    }

    @Test
    public void testGetState() {
        // Given
        CheckListState expectedState = CheckListState.RUNNING;

        // When
        CheckListState actualState = checkList.getState();

        // Then
        assertEquals(expectedState, actualState);
    }

    @Test
    public void testFindPairMatching() {
        // Given
        CandidatePair candidatePair = mock(CandidatePair.class);
        when(candidatePair.getLocalCandidate()).thenReturn(localCandidate);
        when(candidatePair.getRemoteCandidate()).thenReturn(remoteCandidate);
        checkList.add(candidatePair);

        // When
        CandidatePair actualPair = checkList.findPairMatching(localCandidate, remoteCandidate);

        // Then
        assertEquals(candidatePair, actualPair);
    }

    @Test
    public void testIsActive() {
        // Given
        CandidatePair candidatePair = mock(CandidatePair.class);
        when(candidatePair.getState()).thenReturn(CandidatePairState.WAITING);
        checkList.add(candidatePair);

        // When
        boolean isActive = checkList.isActive();

        // Then
        assertTrue(isActive);
    }

    @Test
    public void testAllChecksCompleted() {
        // Given
        CandidatePair candidatePair = mock(CandidatePair.class);
        when(candidatePair.getState()).thenReturn(CandidatePairState.SUCCEEDED);
        checkList.add(candidatePair);

        // When
        boolean allChecksCompleted = checkList.allChecksCompleted();

        // Then
        assertTrue(allChecksCompleted);
    }

    @Test
    public void testIsFrozen() {
        // Given
        CandidatePair candidatePair = mock(CandidatePair.class);
        when(candidatePair.getState()).thenReturn(CandidatePairState.FROZEN);
        checkList.add(candidatePair);

        // When
        boolean isFrozen = checkList.isFrozen();

        // Then
        assertTrue(isFrozen);
    }

    @Test
    public void testToString() {
        // Given
        CandidatePair candidatePair = mock(CandidatePair.class);
        checkList.add(candidatePair);

        // When
        String toString = checkList.toString();

        // Then
        assertNotNull(toString);
    }

    @Test
    public void testGetName() {
        // Given
        String expectedName = "expectedName";
        when(parentStream.getName()).thenReturn(expectedName);

        // When
        String actualName = checkList.getName();

        // Then
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testContainsNomineeForComponent() {
        // Given
        CandidatePair candidatePair = mock(CandidatePair.class);
        when(candidatePair.isNominated()).thenReturn(true);
        when(candidatePair.getParentComponent()).thenReturn(component);
        checkList.add(candidatePair);

        // When
        boolean containsNominee = checkList.containsNomineeForComponent(component);

        // Then
        assertTrue(containsNominee);
    }

    @Test
    public void testAddStateChangeListener() {
        // Given
        PropertyChangeListener listener = mock(PropertyChangeListener.class);

        // When
        checkList.addStateChangeListener(listener);

        // Then
        verify(checkList, times(1)).addStateChangeListener(listener);
    }

    @Test
    public void testRemoveStateChangeListener() {
        // Given
        PropertyChangeListener listener = mock(PropertyChangeListener.class);
        checkList.addStateChangeListener(listener);

        // When
        checkList.removeStateChangeListener(listener);

        // Then
        verify(checkList, times(1)).removeStateChangeListener(listener);
    }

    @Test
    public void testAddChecksListener() {
        // Given
        PropertyChangeListener listener = mock(PropertyChangeListener.class);

        // When
        checkList.addChecksListener(listener);

        // Then
        verify(checkList, times(1)).addChecksListener(listener);
    }

    @Test
    public void testRemoveChecksListener() {
        // Given
        PropertyChangeListener listener = mock(PropertyChangeListener.class);
        checkList.addChecksListener(listener);

        // When
        checkList.removeChecksListener(listener);

        // Then
        verify(checkList, times(1)).removeChecksListener(listener);
    }

    @Test
    public void testGetParentStream() {
        // Given
        IceMediaStream expectedParentStream = parentStream;

        // When
        IceMediaStream actualParentStream = checkList.getParentStream();

        // Then
        assertEquals(expectedParentStream, actualParentStream);
    }

    @Test
    public void testShouldStartPaceMaker() {
        // Given
        boolean expectedShouldStart = true;

        // When
        boolean actualShouldStart = checkList.shouldStartPaceMaker();

        // Then
        assertEquals(expectedShouldStart, actualShouldStart);
    }
}
import org.ice4j.ice.Component;
import org.ice4j.ice.LocalCandidate;
import org.ice4j.ice.TrickleCallback;
import org.ice4j.ice.harvest.CandidateHarvester;
import org.ice4j.ice.harvest.CandidateHarvesterSetElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CandidateHarvesterSetElementTest {

    @Mock
    private CandidateHarvester harvester;

    @Mock
    private Component component;

    @Mock
    private TrickleCallback trickleCallback;

    private CandidateHarvesterSetElement candidateHarvesterSetElement;

    @BeforeEach
    void setup() {
        candidateHarvesterSetElement = new CandidateHarvesterSetElement(harvester);
    }

    @Test
    void testHarvest_Enabled() {
        // Given
        Collection<LocalCandidate> candidates = new ArrayList<>();
        when(harvester.harvest(component)).thenReturn(candidates);

        // When
        candidateHarvesterSetElement.harvest(component, trickleCallback);

        // Then
        verify(harvester, times(1)).harvest(component);
        verify(trickleCallback, times(1)).onIceCandidates(candidates);
    }

    @Test
    void testHarvest_Disabled() {
        // Given
        candidateHarvesterSetElement.setEnabled(false);

        // When
        candidateHarvesterSetElement.harvest(component, trickleCallback);

        // Then
        verify(harvester, never()).harvest(component);
        verify(trickleCallback, never()).onIceCandidates(any());
    }

    @Test
    void testHarvest_NoCandidates() {
        // Given
        when(harvester.harvest(component)).thenReturn(null);

        // When
        candidateHarvesterSetElement.harvest(component, trickleCallback);

        // Then
        verify(harvester, times(1)).harvest(component);
        verify(trickleCallback, never()).onIceCandidates(any());
        assertFalse(candidateHarvesterSetElement.isEnabled());
    }

    @Test
    void testHarvesterEquals() {
        // Given
        CandidateHarvester otherHarvester = mock(CandidateHarvester.class);
        when(otherHarvester.equals(harvester)).thenReturn(true);

        // When
        boolean result = candidateHarvesterSetElement.harvesterEquals(otherHarvester);

        // Then
        assertTrue(result);
    }

    @Test
    void testHarvesterEquals_False() {
        // Given
        CandidateHarvester otherHarvester = mock(CandidateHarvester.class);
        when(otherHarvester.equals(harvester)).thenReturn(false);

        // When
        boolean result = candidateHarvesterSetElement.harvesterEquals(otherHarvester);

        // Then
        assertFalse(result);
    }

    @Test
    void testIsEnabled() {
        // Given
        candidateHarvesterSetElement.setEnabled(true);

        // When
        boolean result = candidateHarvesterSetElement.isEnabled();

        // Then
        assertTrue(result);
    }

    @Test
    void testSetEnabled() {
        // Given
        candidateHarvesterSetElement.setEnabled(false);

        // When
        candidateHarvesterSetElement.setEnabled(true);

        // Then
        assertTrue(candidateHarvesterSetElement.isEnabled());
    }

    @Test
    void testGetHarvester() {
        // When
        CandidateHarvester result = candidateHarvesterSetElement.getHarvester();

        // Then
        assertSame(harvester, result);
    }
}
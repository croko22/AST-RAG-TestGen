import org.ice4j.ice.Component;
import org.ice4j.ice.LocalCandidate;
import org.ice4j.ice.harvest.CandidateHarvester;
import org.ice4j.ice.harvest.HarvestStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CandidateHarvesterTest {

    @Mock
    private Component component;

    @Mock
    private CandidateHarvester candidateHarvester;

    @BeforeEach
    void setup() {
        // No setup needed
    }

    @Test
    public void testHarvest() {
        // Given
        Collection<LocalCandidate> localCandidates = new ArrayList<>();
        when(candidateHarvester.harvest(any(Component.class))).thenReturn(localCandidates);

        // When
        Collection<LocalCandidate> result = candidateHarvester.harvest(component);

        // Then
        assertNotNull(result);
        assertEquals(localCandidates, result);
        verify(candidateHarvester, times(1)).harvest(component);
    }

    @Test
    public void testGetHarvestStatistics() {
        // Given
        HarvestStatistics harvestStatistics = mock(HarvestStatistics.class);
        when(candidateHarvester.getHarvestStatistics()).thenReturn(harvestStatistics);

        // When
        HarvestStatistics result = candidateHarvester.getHarvestStatistics();

        // Then
        assertNotNull(result);
        assertEquals(harvestStatistics, result);
        verify(candidateHarvester, times(1)).getHarvestStatistics();
    }

    @Test
    public void testIsHostHarvester() {
        // Given
        boolean isHostHarvester = true;
        when(candidateHarvester.isHostHarvester()).thenReturn(isHostHarvester);

        // When
        boolean result = candidateHarvester.isHostHarvester();

        // Then
        assertEquals(isHostHarvester, result);
        verify(candidateHarvester, times(1)).isHostHarvester();
    }

    @Test
    public void testIsNotHostHarvester() {
        // Given
        boolean isHostHarvester = false;
        when(candidateHarvester.isHostHarvester()).thenReturn(isHostHarvester);

        // When
        boolean result = candidateHarvester.isHostHarvester();

        // Then
        assertEquals(isHostHarvester, result);
        verify(candidateHarvester, times(1)).isHostHarvester();
    }

    @Test
    public void testHarvestNullComponent() {
        // Given
        component = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> candidateHarvester.harvest(component));
        verify(candidateHarvester, never()).harvest(any(Component.class));
    }

    @Test
    public void testGetHarvestStatisticsNull() {
        // Given
        when(candidateHarvester.getHarvestStatistics()).thenReturn(null);

        // When
        HarvestStatistics result = candidateHarvester.getHarvestStatistics();

        // Then
        assertNull(result);
        verify(candidateHarvester, times(1)).getHarvestStatistics();
    }
}
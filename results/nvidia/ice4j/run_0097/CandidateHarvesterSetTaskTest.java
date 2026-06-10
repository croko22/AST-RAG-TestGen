import org.ice4j.ice.Component;
import org.ice4j.ice.LocalCandidate;
import org.ice4j.ice.harvest.CandidateHarvesterSetElement;
import org.ice4j.ice.harvest.CandidateHarvesterSetTask;
import org.ice4j.ice.harvest.TrickleCallback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CandidateHarvesterSetTaskTest {

    @Mock
    private CandidateHarvesterSetElement harvester;

    @Mock
    private TrickleCallback trickleCallback;

    @Mock
    private Component component;

    private Collection<Component> components;

    private CandidateHarvesterSetTask task;

    @BeforeEach
    public void setup() {
        components = new ArrayList<>();
        components.add(component);
        task = new CandidateHarvesterSetTask(harvester, components, trickleCallback);
    }

    @Test
    public void testGetHarvester() {
        // Given
        // When
        CandidateHarvesterSetElement result = task.getHarvester();
        // Then
        assertEquals(harvester, result);
    }

    @Test
    public void testRun_HarvesterNull() {
        // Given
        task = new CandidateHarvesterSetTask(null, components, trickleCallback);
        // When
        task.run();
        // Then
        verify(harvester, never()).harvest(any(), any());
    }

    @Test
    public void testRun_HarvesterDisabled() {
        // Given
        when(harvester.isEnabled()).thenReturn(false);
        // When
        task.run();
        // Then
        verify(harvester, never()).harvest(any(), any());
    }

    @Test
    public void testRun_HarvesterEnabled() {
        // Given
        when(harvester.isEnabled()).thenReturn(true);
        // When
        task.run();
        // Then
        verify(harvester, times(1)).harvest(component, trickleCallback);
    }

    @Test
    public void testRun_HarvesterThrowsException() {
        // Given
        when(harvester.isEnabled()).thenReturn(true);
        doThrow(new RuntimeException()).when(harvester).harvest(component, trickleCallback);
        // When
        task.run();
        // Then
        verify(harvester, times(1)).harvest(component, trickleCallback);
        verify(harvester, times(1)).setEnabled(false);
    }

    @Test
    public void testRun_HarvesterThrowsThreadDeath() {
        // Given
        when(harvester.isEnabled()).thenReturn(true);
        doThrow(new ThreadDeath()).when(harvester).harvest(component, trickleCallback);
        // When
        assertThrows(ThreadDeath.class, () -> task.run());
        // Then
        verify(harvester, times(1)).harvest(component, trickleCallback);
        verify(harvester, times(1)).setEnabled(false);
    }
}
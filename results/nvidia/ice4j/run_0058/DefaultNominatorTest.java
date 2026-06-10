import org.ice4j.ice.Agent;
import org.ice4j.ice.DefaultNominator;
import org.ice4j.ice.NominationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.beans.PropertyChangeEvent;
import java.util.EventObject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultNominatorTest {

    @Mock
    private Agent parentAgent;

    private DefaultNominator defaultNominator;

    @BeforeEach
    void setup() {
        defaultNominator = new DefaultNominator(parentAgent);
    }

    @Test
    void testGetStrategy() {
        // Given
        NominationStrategy expectedStrategy = NominationStrategy.NOMINATE_FIRST_VALID;

        // When
        NominationStrategy actualStrategy = defaultNominator.getStrategy();

        // Then
        assertEquals(expectedStrategy, actualStrategy);
    }

    @Test
    void testSetStrategy() {
        // Given
        NominationStrategy newStrategy = NominationStrategy.NOMINATE_HIGHEST_PRIO;

        // When
        defaultNominator.setStrategy(newStrategy);

        // Then
        assertEquals(newStrategy, defaultNominator.getStrategy());
    }

    @Test
    void testPropertyChange_ControlledAgent() {
        // Given
        when(parentAgent.isControlling()).thenReturn(false);
        PropertyChangeEvent event = new PropertyChangeEvent(this, "test", null, null);

        // When
        defaultNominator.propertyChange(event);

        // Then
        verify(parentAgent, never()).nominate(any());
    }

    @Test
    void testPropertyChange_NominateFirstValid() {
        // Given
        when(parentAgent.isControlling()).thenReturn(true);
        defaultNominator.setStrategy(NominationStrategy.NOMINATE_FIRST_VALID);
        PropertyChangeEvent event = mock(PropertyChangeEvent.class);
        when(event.getPropertyName()).thenReturn("test");
        when(event.getSource()).thenReturn(mock(Object.class));

        // When
        defaultNominator.propertyChange(event);

        // Then
        verify(parentAgent, never()).nominate(any());
    }

    @Test
    void testPropertyChange_NominateHighestPrio() {
        // Given
        when(parentAgent.isControlling()).thenReturn(true);
        defaultNominator.setStrategy(NominationStrategy.NOMINATE_HIGHEST_PRIO);
        PropertyChangeEvent event = mock(PropertyChangeEvent.class);
        when(event.getPropertyName()).thenReturn("test");
        when(event.getSource()).thenReturn(mock(Object.class));

        // When
        defaultNominator.propertyChange(event);

        // Then
        verify(parentAgent, never()).nominate(any());
    }

    @Test
    void testPropertyChange_NominateFirstHostOrReflexiveValid() {
        // Given
        when(parentAgent.isControlling()).thenReturn(true);
        defaultNominator.setStrategy(NominationStrategy.NOMINATE_FIRST_HOST_OR_REFLEXIVE_VALID);
        PropertyChangeEvent event = mock(PropertyChangeEvent.class);
        when(event.getPropertyName()).thenReturn("test");
        when(event.getSource()).thenReturn(mock(Object.class));

        // When
        defaultNominator.propertyChange(event);

        // Then
        verify(parentAgent, never()).nominate(any());
    }
}
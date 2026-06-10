import net.hydromatic.morel.eval.Discrete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DiscreteTest {

    @Mock
    private Discrete<Integer> discrete;

    @BeforeEach
    void setup() {
        // No setup needed for this test class
    }

    @Test
    public void testComparator() {
        // Given: a discrete type with a comparator
        Comparator<Object> comparator = mock(Comparator.class);
        when(discrete.comparator()).thenReturn(comparator);

        // When: the comparator is retrieved
        Comparator<Object> result = discrete.comparator();

        // Then: the comparator is returned
        assertEquals(comparator, result);
        verify(discrete, times(1)).comparator();
    }

    @Test
    public void testNext() {
        // Given: a discrete type with a next value
        Integer nextValue = 2;
        when(discrete.next(1)).thenReturn(nextValue);

        // When: the next value is retrieved
        Integer result = discrete.next(1);

        // Then: the next value is returned
        assertEquals(nextValue, result);
        verify(discrete, times(1)).next(1);
    }

    @Test
    public void testNext_Null() {
        // Given: a discrete type with no next value (max value)
        when(discrete.next(1)).thenReturn(null);

        // When: the next value is retrieved
        Integer result = discrete.next(1);

        // Then: null is returned
        assertNull(result);
        verify(discrete, times(1)).next(1);
    }

    @Test
    public void testPrev() {
        // Given: a discrete type with a previous value
        Integer prevValue = 0;
        when(discrete.prev(1)).thenReturn(prevValue);

        // When: the previous value is retrieved
        Integer result = discrete.prev(1);

        // Then: the previous value is returned
        assertEquals(prevValue, result);
        verify(discrete, times(1)).prev(1);
    }

    @Test
    public void testPrev_Null() {
        // Given: a discrete type with no previous value (min value)
        when(discrete.prev(1)).thenReturn(null);

        // When: the previous value is retrieved
        Integer result = discrete.prev(1);

        // Then: null is returned
        assertNull(result);
        verify(discrete, times(1)).prev(1);
    }

    @Test
    public void testMinValue() {
        // Given: a discrete type with a minimum value
        Integer minValue = 0;
        when(discrete.minValue()).thenReturn(minValue);

        // When: the minimum value is retrieved
        Integer result = discrete.minValue();

        // Then: the minimum value is returned
        assertEquals(minValue, result);
        verify(discrete, times(1)).minValue();
    }

    @Test
    public void testMaxValue() {
        // Given: a discrete type with a maximum value
        Integer maxValue = 1;
        when(discrete.maxValue()).thenReturn(maxValue);

        // When: the maximum value is retrieved
        Integer result = discrete.maxValue();

        // Then: the maximum value is returned
        assertEquals(maxValue, result);
        verify(discrete, times(1)).maxValue();
    }
}
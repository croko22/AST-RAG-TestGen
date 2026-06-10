import org.apache.commons.collections4.SortedBidiMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.SortedMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SortedBidiMapTest {

    @Mock
    private SortedBidiMap<String, Integer> sortedBidiMap;

    @BeforeEach
    void setup() {
        // Initialize the mock object
        sortedBidiMap = mock(SortedBidiMap.class);
    }

    @Test
    void testInverseBidiMap() {
        // Given: a sorted bidirectional map
        SortedBidiMap<String, Integer> inverseMap = mock(SortedBidiMap.class);
        when(sortedBidiMap.inverseBidiMap()).thenReturn(inverseMap);

        // When: the inverse map is retrieved
        SortedBidiMap<Integer, String> result = sortedBidiMap.inverseBidiMap();

        // Then: the result is the expected inverse map
        assertEquals(inverseMap, result);
        verify(sortedBidiMap, times(1)).inverseBidiMap();
    }

    @Test
    void testValueComparator() {
        // Given: a comparator for the values
        Comparator<Integer> comparator = mock(Comparator.class);
        when(sortedBidiMap.valueComparator()).thenReturn(comparator);

        // When: the value comparator is retrieved
        Comparator<Integer> result = sortedBidiMap.valueComparator();

        // Then: the result is the expected comparator
        assertEquals(comparator, result);
        verify(sortedBidiMap, times(1)).valueComparator();
    }

    @Test
    void testInverseBidiMapNull() {
        // Given: the inverse map is not initialized
        when(sortedBidiMap.inverseBidiMap()).thenReturn(null);

        // When / Then: a NullPointerException is expected
        assertThrows(NullPointerException.class, () -> sortedBidiMap.inverseBidiMap().inverseBidiMap());
        verify(sortedBidiMap, times(1)).inverseBidiMap();
    }

    @Test
    void testValueComparatorNull() {
        // Given: the value comparator is not initialized
        when(sortedBidiMap.valueComparator()).thenReturn(null);

        // When / Then: a NullPointerException is expected
        assertThrows(NullPointerException.class, () -> sortedBidiMap.valueComparator().compare(any(), any()));
        verify(sortedBidiMap, times(1)).valueComparator();
    }
}
import org.apache.commons.collections4.OrderedMapIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderedMapIteratorTest {

    @Mock
    private OrderedMapIterator<String, String> orderedMapIterator;

    @BeforeEach
    void setup() {
        // Initialize the mock
        orderedMapIterator = mock(OrderedMapIterator.class);
    }

    @Test
    void testHasPrevious_ReturnsTrue() {
        // Given: the iterator has a previous element
        when(orderedMapIterator.hasPrevious()).thenReturn(true);

        // When: hasPrevious is called
        boolean result = orderedMapIterator.hasPrevious();

        // Then: the result is true
        assertTrue(result);
        verify(orderedMapIterator, times(1)).hasPrevious();
    }

    @Test
    void testHasPrevious_ReturnsFalse() {
        // Given: the iterator does not have a previous element
        when(orderedMapIterator.hasPrevious()).thenReturn(false);

        // When: hasPrevious is called
        boolean result = orderedMapIterator.hasPrevious();

        // Then: the result is false
        assertFalse(result);
        verify(orderedMapIterator, times(1)).hasPrevious();
    }

    @Test
    void testPrevious_WithPreviousElement() {
        // Given: the iterator has a previous element
        String previousKey = "previousKey";
        when(orderedMapIterator.hasPrevious()).thenReturn(true);
        when(orderedMapIterator.previous()).thenReturn(previousKey);

        // When: previous is called
        String result = orderedMapIterator.previous();

        // Then: the previous key is returned
        assertEquals(previousKey, result);
        verify(orderedMapIterator, times(1)).hasPrevious();
        verify(orderedMapIterator, times(1)).previous();
    }

    @Test
    void testPrevious_WithoutPreviousElement() {
        // Given: the iterator does not have a previous element
        when(orderedMapIterator.hasPrevious()).thenReturn(false);

        // When / Then: a NoSuchElementException is thrown
        assertThrows(java.util.NoSuchElementException.class, () -> orderedMapIterator.previous());
        verify(orderedMapIterator, times(1)).hasPrevious();
        verify(orderedMapIterator, never()).previous();
    }

    @Test
    void testPrevious_MultipleCalls() {
        // Given: the iterator has multiple previous elements
        String[] previousKeys = {"previousKey1", "previousKey2", "previousKey3"};
        when(orderedMapIterator.hasPrevious()).thenReturn(true, true, true, false);
        when(orderedMapIterator.previous()).thenReturn(previousKeys[0], previousKeys[1], previousKeys[2]);

        // When: previous is called multiple times
        String result1 = orderedMapIterator.previous();
        String result2 = orderedMapIterator.previous();
        String result3 = orderedMapIterator.previous();

        // Then: the previous keys are returned in order
        assertEquals(previousKeys[0], result1);
        assertEquals(previousKeys[1], result2);
        assertEquals(previousKeys[2], result3);
        verify(orderedMapIterator, times(4)).hasPrevious();
        verify(orderedMapIterator, times(3)).previous();
    }
}
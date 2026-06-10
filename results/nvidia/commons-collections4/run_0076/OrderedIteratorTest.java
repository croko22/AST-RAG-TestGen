import org.apache.commons.collections4.OrderedIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderedIteratorTest {

    @Mock
    private OrderedIterator<String> orderedIterator;

    @BeforeEach
    void setup() {
        // No setup needed for this test class
    }

    @Test
    public void testHasPrevious_ReturnsTrue() {
        // Given: the iterator has a previous element
        when(orderedIterator.hasPrevious()).thenReturn(true);

        // When: hasPrevious is called
        boolean result = orderedIterator.hasPrevious();

        // Then: the result is true
        assertTrue(result);
        verify(orderedIterator, times(1)).hasPrevious();
    }

    @Test
    public void testHasPrevious_ReturnsFalse() {
        // Given: the iterator does not have a previous element
        when(orderedIterator.hasPrevious()).thenReturn(false);

        // When: hasPrevious is called
        boolean result = orderedIterator.hasPrevious();

        // Then: the result is false
        assertFalse(result);
        verify(orderedIterator, times(1)).hasPrevious();
    }

    @Test
    public void testPrevious_ElementAvailable() {
        // Given: the iterator has a previous element
        String previousElement = "Previous Element";
        when(orderedIterator.previous()).thenReturn(previousElement);

        // When: previous is called
        String result = orderedIterator.previous();

        // Then: the previous element is returned
        assertEquals(previousElement, result);
        verify(orderedIterator, times(1)).previous();
    }

    @Test
    public void testPrevious_NoSuchElementException() {
        // Given: the iterator does not have a previous element
        when(orderedIterator.previous()).thenThrow(NoSuchElementException.class);

        // When / Then: calling previous throws an exception
        assertThrows(NoSuchElementException.class, () -> orderedIterator.previous());
        verify(orderedIterator, times(1)).previous();
    }
}
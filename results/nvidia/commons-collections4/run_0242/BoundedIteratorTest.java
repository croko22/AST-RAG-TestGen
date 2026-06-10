import org.apache.commons.collections4.iterators.BoundedIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoundedIteratorTest {

    @Mock
    private Iterator<Integer> iterator;

    private BoundedIterator<Integer> boundedIterator;

    @BeforeEach
    void setup() {
        boundedIterator = new BoundedIterator<>(iterator, 0, 10);
    }

    @Test
    void testHasNext_WhenIteratorHasNext_ReturnsTrue() {
        // Given
        when(iterator.hasNext()).thenReturn(true);

        // When
        boolean result = boundedIterator.hasNext();

        // Then
        assertTrue(result);
    }

    @Test
    void testHasNext_WhenIteratorDoesNotHaveNext_ReturnsFalse() {
        // Given
        when(iterator.hasNext()).thenReturn(false);

        // When
        boolean result = boundedIterator.hasNext();

        // Then
        assertFalse(result);
    }

    @Test
    void testHasNext_WhenOffsetIsGreaterThanMax_ReturnsFalse() {
        // Given
        boundedIterator = new BoundedIterator<>(iterator, 10, 5);

        // When
        boolean result = boundedIterator.hasNext();

        // Then
        assertFalse(result);
    }

    @Test
    void testNext_WhenIteratorHasNext_ReturnsNextElement() {
        // Given
        when(iterator.hasNext()).thenReturn(true);
        when(iterator.next()).thenReturn(1);

        // When
        int result = boundedIterator.next();

        // Then
        assertEquals(1, result);
    }

    @Test
    void testNext_WhenIteratorDoesNotHaveNext_ThrowsNoSuchElementException() {
        // Given
        when(iterator.hasNext()).thenReturn(false);

        // When
        assertThrows(NoSuchElementException.class, () -> boundedIterator.next());
    }

    @Test
    void testNext_WhenOffsetIsGreaterThanMax_ThrowsNoSuchElementException() {
        // Given
        boundedIterator = new BoundedIterator<>(iterator, 10, 5);

        // When
        assertThrows(NoSuchElementException.class, () -> boundedIterator.next());
    }

    @Test
    void testRemove_WhenNextHasBeenCalled_RemovesElement() {
        // Given
        when(iterator.hasNext()).thenReturn(true);
        when(iterator.next()).thenReturn(1);

        // When
        boundedIterator.next();
        boundedIterator.remove();

        // Then
        verify(iterator).remove();
    }

    @Test
    void testRemove_WhenNextHasNotBeenCalled_ThrowsIllegalStateException() {
        // Given

        // When
        assertThrows(IllegalStateException.class, () -> boundedIterator.remove());
    }

    @Test
    void testConstructor_WhenOffsetIsNegative_ThrowsIllegalArgumentException() {
        // Given

        // When
        assertThrows(IllegalArgumentException.class, () -> new BoundedIterator<>(iterator, -1, 10));
    }

    @Test
    void testConstructor_WhenMaxIsNegative_ThrowsIllegalArgumentException() {
        // Given

        // When
        assertThrows(IllegalArgumentException.class, () -> new BoundedIterator<>(iterator, 0, -1));
    }

    @Test
    void testConstructor_WhenIteratorIsNull_ThrowsNullPointerException() {
        // Given

        // When
        assertThrows(NullPointerException.class, () -> new BoundedIterator<>(null, 0, 10));
    }

    @Test
    void testInit_WhenOffsetIsGreaterThanZero_AdvancesIterator() {
        // Given
        boundedIterator = new BoundedIterator<>(iterator, 5, 10);
        when(iterator.hasNext()).thenReturn(true, true, true, true, true, false);

        // When
        boundedIterator.init();

        // Then
        verify(iterator, times(5)).next();
    }
}
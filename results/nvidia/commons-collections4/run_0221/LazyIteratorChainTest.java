import org.apache.commons.collections4.iterators.LazyIteratorChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LazyIteratorChainTest {

    @Mock
    private Iterator<String> iterator1;

    @Mock
    private Iterator<String> iterator2;

    private LazyIteratorChain<String> lazyIteratorChain;

    @BeforeEach
    void setup() {
        lazyIteratorChain = new LazyIteratorChain<String>() {
            @Override
            protected Iterator<? extends String> nextIterator(int count) {
                if (count == 1) {
                    return iterator1;
                } else if (count == 2) {
                    return iterator2;
                } else {
                    return null;
                }
            }
        };
    }

    @Test
    void testHasNext_WithElements_ReturnsTrue() {
        // Given
        when(iterator1.hasNext()).thenReturn(true);

        // When
        boolean result = lazyIteratorChain.hasNext();

        // Then
        assertTrue(result);
        verify(iterator1, times(1)).hasNext();
    }

    @Test
    void testHasNext_WithoutElements_ReturnsFalse() {
        // Given
        when(iterator1.hasNext()).thenReturn(false);
        when(iterator2.hasNext()).thenReturn(false);

        // When
        boolean result = lazyIteratorChain.hasNext();

        // Then
        assertFalse(result);
        verify(iterator1, times(1)).hasNext();
        verify(iterator2, times(1)).hasNext();
    }

    @Test
    void testNext_WithElements_ReturnsElement() {
        // Given
        when(iterator1.hasNext()).thenReturn(true);
        when(iterator1.next()).thenReturn("Element");

        // When
        String result = lazyIteratorChain.next();

        // Then
        assertEquals("Element", result);
        verify(iterator1, times(1)).next();
    }

    @Test
    void testNext_WithoutElements_ThrowsException() {
        // Given
        when(iterator1.hasNext()).thenReturn(false);
        when(iterator2.hasNext()).thenReturn(false);

        // When
        assertThrows(NoSuchElementException.class, () -> lazyIteratorChain.next());
    }

    @Test
    void testRemove_WithElements_RemovesElement() {
        // Given
        when(iterator1.hasNext()).thenReturn(true);
        when(iterator1.next()).thenReturn("Element");

        // When
        lazyIteratorChain.next();
        lazyIteratorChain.remove();

        // Then
        verify(iterator1, times(1)).remove();
    }

    @Test
    void testRemove_WithoutElements_ThrowsException() {
        // Given
        when(iterator1.hasNext()).thenReturn(false);
        when(iterator2.hasNext()).thenReturn(false);

        // When
        assertThrows(NoSuchElementException.class, () -> lazyIteratorChain.remove());
    }

    @Test
    void testRemove_MultipleTimes_ThrowsException() {
        // Given
        when(iterator1.hasNext()).thenReturn(true);
        when(iterator1.next()).thenReturn("Element");

        // When
        lazyIteratorChain.next();
        lazyIteratorChain.remove();
        assertThrows(IllegalStateException.class, () -> lazyIteratorChain.remove());
    }
}
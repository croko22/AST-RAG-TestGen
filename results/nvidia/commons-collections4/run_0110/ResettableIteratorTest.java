import org.apache.commons.collections4.ResettableIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResettableIteratorTest {

    @Mock
    private ResettableIterator<String> resettableIterator;

    @BeforeEach
    void setup() {
        // Initialize the mock iterator
        reset(resettableIterator);
    }

    @Test
    public void testReset() {
        // Given: the iterator has been used
        when(resettableIterator.hasNext()).thenReturn(true);
        when(resettableIterator.next()).thenReturn("Element");

        // When: reset is called
        resettableIterator.reset();

        // Then: the iterator should be reset
        verify(resettableIterator, times(1)).reset();
    }

    @Test
    public void testHasNext() {
        // Given: the iterator has elements
        when(resettableIterator.hasNext()).thenReturn(true);

        // When: hasNext is called
        boolean result = resettableIterator.hasNext();

        // Then: the result should be true
        assertTrue(result);
        verify(resettableIterator, times(1)).hasNext();
    }

    @Test
    public void testHasNext_NoElements() {
        // Given: the iterator has no elements
        when(resettableIterator.hasNext()).thenReturn(false);

        // When: hasNext is called
        boolean result = resettableIterator.hasNext();

        // Then: the result should be false
        assertFalse(result);
        verify(resettableIterator, times(1)).hasNext();
    }

    @Test
    public void testNext() {
        // Given: the iterator has elements
        when(resettableIterator.hasNext()).thenReturn(true);
        when(resettableIterator.next()).thenReturn("Element");

        // When: next is called
        String result = resettableIterator.next();

        // Then: the result should be the next element
        assertEquals("Element", result);
        verify(resettableIterator, times(1)).next();
    }

    @Test
    public void testNext_NoElements() {
        // Given: the iterator has no elements
        when(resettableIterator.hasNext()).thenReturn(false);

        // When: next is called
        assertThrows(NoSuchElementException.class, () -> resettableIterator.next());
        verify(resettableIterator, times(1)).hasNext();
    }

    @Test
    public void testRemove() {
        // Given: the iterator has elements
        when(resettableIterator.hasNext()).thenReturn(true);
        when(resettableIterator.next()).thenReturn("Element");

        // When: remove is called
        assertThrows(UnsupportedOperationException.class, () -> resettableIterator.remove());
        verify(resettableIterator, times(1)).hasNext();
        verify(resettableIterator, times(1)).next();
    }
}
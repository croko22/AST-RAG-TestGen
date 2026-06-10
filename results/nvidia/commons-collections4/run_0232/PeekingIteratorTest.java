import org.apache.commons.collections4.iterators.PeekingIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PeekingIteratorTest {

    @Mock
    private Iterator<String> iterator;

    private PeekingIterator<String> peekingIterator;

    @BeforeEach
    void setup() {
        peekingIterator = PeekingIterator.peekingIterator(iterator);
    }

    @Test
    public void testPeekingIterator() {
        // Given
        List<String> elements = new ArrayList<>();
        elements.add("Element1");
        elements.add("Element2");
        elements.add("Element3");

        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.next()).thenReturn("Element1", "Element2", "Element3");

        // When
        PeekingIterator<String> peekingIterator = PeekingIterator.peekingIterator(iterator);

        // Then
        assertTrue(peekingIterator.hasNext());
        assertEquals("Element1", peekingIterator.next());
        assertTrue(peekingIterator.hasNext());
        assertEquals("Element2", peekingIterator.next());
        assertTrue(peekingIterator.hasNext());
        assertEquals("Element3", peekingIterator.next());
        assertFalse(peekingIterator.hasNext());
    }

    @Test
    public void testElement() {
        // Given
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn("Element1");

        // When
        String element = peekingIterator.element();

        // Then
        assertEquals("Element1", element);
    }

    @Test
    public void testElement_ThrowsNoSuchElementException() {
        // Given
        when(iterator.hasNext()).thenReturn(false);

        // When and Then
        assertThrows(NoSuchElementException.class, () -> peekingIterator.element());
    }

    @Test
    public void testHasNext() {
        // Given
        when(iterator.hasNext()).thenReturn(true, false);

        // When and Then
        assertTrue(peekingIterator.hasNext());
        verify(iterator, times(1)).hasNext();
        when(iterator.hasNext()).thenReturn(false);
        assertFalse(peekingIterator.hasNext());
        verify(iterator, times(2)).hasNext();
    }

    @Test
    public void testNext() {
        // Given
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn("Element1");

        // When
        String next = peekingIterator.next();

        // Then
        assertEquals("Element1", next);
    }

    @Test
    public void testNext_ThrowsNoSuchElementException() {
        // Given
        when(iterator.hasNext()).thenReturn(false);

        // When and Then
        assertThrows(NoSuchElementException.class, () -> peekingIterator.next());
    }

    @Test
    public void testPeek() {
        // Given
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn("Element1");

        // When
        String peeked = peekingIterator.peek();

        // Then
        assertEquals("Element1", peeked);
    }

    @Test
    public void testPeek_ReturnsNull() {
        // Given
        when(iterator.hasNext()).thenReturn(false);

        // When
        String peeked = peekingIterator.peek();

        // Then
        assertNull(peeked);
    }

    @Test
    public void testRemove() {
        // Given
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn("Element1");

        // When
        peekingIterator.next();
        peekingIterator.remove();

        // Then
        verify(iterator, times(1)).remove();
    }

    @Test
    public void testRemove_ThrowsIllegalStateException() {
        // Given
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn("Element1");

        // When
        peekingIterator.peek();
        assertThrows(IllegalStateException.class, () -> peekingIterator.remove());
    }
}
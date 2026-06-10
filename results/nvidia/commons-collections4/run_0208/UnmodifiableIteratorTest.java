import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;
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
public class UnmodifiableIteratorTest {

    @Mock
    private Iterator<String> iterator;

    private UnmodifiableIterator<String> unmodifiableIterator;

    @BeforeEach
    void setup() {
        unmodifiableIterator = UnmodifiableIterator.unmodifiableIterator(iterator);
    }

    @Test
    public void testUnmodifiableIterator Creation() {
        // Given: an iterator
        // When: creating an unmodifiable iterator
        // Then: the unmodifiable iterator is created
        assertNotNull(unmodifiableIterator);
    }

    @Test
    public void testUnmodifiableIterator_HasNext() {
        // Given: the iterator has a next element
        when(iterator.hasNext()).thenReturn(true);
        // When: checking if the unmodifiable iterator has a next element
        boolean hasNext = unmodifiableIterator.hasNext();
        // Then: the result is the same as the original iterator
        assertTrue(hasNext);
        verify(iterator, times(1)).hasNext();
    }

    @Test
    public void testUnmodifiableIterator_HasNext_NoNextElement() {
        // Given: the iterator does not have a next element
        when(iterator.hasNext()).thenReturn(false);
        // When: checking if the unmodifiable iterator has a next element
        boolean hasNext = unmodifiableIterator.hasNext();
        // Then: the result is the same as the original iterator
        assertFalse(hasNext);
        verify(iterator, times(1)).hasNext();
    }

    @Test
    public void testUnmodifiableIterator_Next() {
        // Given: the iterator has a next element
        when(iterator.hasNext()).thenReturn(true);
        String nextElement = "nextElement";
        when(iterator.next()).thenReturn(nextElement);
        // When: getting the next element from the unmodifiable iterator
        String element = unmodifiableIterator.next();
        // Then: the result is the same as the original iterator
        assertEquals(nextElement, element);
        verify(iterator, times(1)).next();
    }

    @Test
    public void testUnmodifiableIterator_Next_NoNextElement() {
        // Given: the iterator does not have a next element
        when(iterator.hasNext()).thenReturn(false);
        // When: getting the next element from the unmodifiable iterator
        assertThrows(NoSuchElementException.class, () -> unmodifiableIterator.next());
        verify(iterator, times(1)).hasNext();
    }

    @Test
    public void testUnmodifiableIterator_Remove() {
        // When: removing an element from the unmodifiable iterator
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableIterator.remove());
        verify(iterator, never()).remove();
    }

    @Test
    public void testUnmodifiableIterator_UnmodifiableIteratorAlready() {
        // Given: an unmodifiable iterator
        Iterator<String> unmodifiableIteratorMock = mock(Iterator.class, withSettings().extraInterfaces(Unmodifiable.class));
        // When: creating an unmodifiable iterator from the existing one
        UnmodifiableIterator<String> result = UnmodifiableIterator.unmodifiableIterator(unmodifiableIteratorMock);
        // Then: the same iterator is returned
        assertSame(unmodifiableIteratorMock, result);
    }

    @Test
    public void testUnmodifiableIterator_NullIterator() {
        // Given: a null iterator
        Iterator<String> nullIterator = null;
        // When: creating an unmodifiable iterator from the null iterator
        assertThrows(NullPointerException.class, () -> UnmodifiableIterator.unmodifiableIterator(nullIterator));
    }
}
import org.apache.commons.collections4.iterators.LoopingIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoopingIteratorTest {

    @Mock
    private Collection<String> collection;

    private LoopingIterator<String> loopingIterator;

    @BeforeEach
    public void setup() {
        loopingIterator = new LoopingIterator<>(collection);
    }

    @Test
    public void testHasNext_EmptyCollection() {
        // Given: an empty collection
        when(collection.isEmpty()).thenReturn(true);

        // When: hasNext is called
        boolean result = loopingIterator.hasNext();

        // Then: hasNext returns false
        assertFalse(result);
    }

    @Test
    public void testHasNext_NonEmptyCollection() {
        // Given: a non-empty collection
        when(collection.isEmpty()).thenReturn(false);

        // When: hasNext is called
        boolean result = loopingIterator.hasNext();

        // Then: hasNext returns true
        assertTrue(result);
    }

    @Test
    public void testNext_EmptyCollection() {
        // Given: an empty collection
        when(collection.isEmpty()).thenReturn(true);

        // When: next is called
        assertThrows(NoSuchElementException.class, () -> loopingIterator.next());
    }

    @Test
    public void testNext_NonEmptyCollection() {
        // Given: a non-empty collection
        when(collection.isEmpty()).thenReturn(false);
        Iterator<String> iterator = mock(Iterator.class);
        when(collection.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn("Element1");

        // When: next is called
        String result = loopingIterator.next();

        // Then: next returns the first element
        assertEquals("Element1", result);
    }

    @Test
    public void testNext_Looping() {
        // Given: a non-empty collection
        when(collection.isEmpty()).thenReturn(false);
        Iterator<String> iterator = mock(Iterator.class);
        when(collection.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn("Element1");

        // When: next is called twice
        loopingIterator.next();
        when(collection.iterator()).thenReturn(iterator);
        String result = loopingIterator.next();

        // Then: next returns the first element again
        assertEquals("Element1", result);
    }

    @Test
    public void testRemove() {
        // Given: a non-empty collection
        when(collection.isEmpty()).thenReturn(false);
        Iterator<String> iterator = mock(Iterator.class);
        when(collection.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn("Element1");

        // When: next and remove are called
        loopingIterator.next();
        loopingIterator.remove();

        // Then: remove is called on the underlying iterator
        verify(iterator).remove();
    }

    @Test
    public void testReset() {
        // Given: a non-empty collection
        when(collection.isEmpty()).thenReturn(false);
        Iterator<String> iterator = mock(Iterator.class);
        when(collection.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn("Element1");

        // When: next and reset are called
        loopingIterator.next();
        loopingIterator.reset();

        // Then: the iterator is reset
        verify(collection).iterator();
    }

    @Test
    public void testSize() {
        // Given: a collection with a size
        when(collection.size()).thenReturn(10);

        // When: size is called
        int result = loopingIterator.size();

        // Then: size returns the collection size
        assertEquals(10, result);
    }

    @Test
    public void testConstructor_NullCollection() {
        // Given: a null collection
        Collection<String> nullCollection = null;

        // When: the constructor is called with a null collection
        assertThrows(NullPointerException.class, () -> new LoopingIterator<>(nullCollection));
    }

    @Test
    public void testConstructor_NonNullCollection() {
        // Given: a non-null collection
        Collection<String> nonNullCollection = new ArrayList<>();

        // When: the constructor is called with a non-null collection
        LoopingIterator<String> loopingIterator = new LoopingIterator<>(nonNullCollection);

        // Then: the constructor does not throw an exception
        assertNotNull(loopingIterator);
    }
}
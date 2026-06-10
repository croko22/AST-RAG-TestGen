import org.apache.commons.collections4.set.UnmodifiableSet;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnmodifiableSetTest {

    @Mock
    private Set<String> set;

    @Mock
    private Collection<String> collection;

    @Mock
    private Predicate<String> predicate;

    private UnmodifiableSet<String> unmodifiableSet;

    @BeforeEach
    public void setup() {
        unmodifiableSet = UnmodifiableSet.unmodifiableSet(set);
    }

    @Test
    public void testUnmodifiableSet() {
        // Given
        when(set.isEmpty()).thenReturn(false);

        // When
        Set<String> result = UnmodifiableSet.unmodifiableSet(set);

        // Then
        assertNotNull(result);
        assertSame(set, result);
    }

    @Test
    public void testUnmodifiableSet_Unmodifiable() {
        // Given
        UnmodifiableSet<String> unmodifiableSet = new UnmodifiableSet<>(set);
        when(set instanceof Unmodifiable).thenReturn(true);

        // When
        Set<String> result = UnmodifiableSet.unmodifiableSet(unmodifiableSet);

        // Then
        assertNotNull(result);
        assertSame(unmodifiableSet, result);
    }

    @Test
    public void testAdd() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSet.add("Test"));
    }

    @Test
    public void testAddAll() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSet.addAll(collection));
    }

    @Test
    public void testClear() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSet.clear());
    }

    @Test
    public void testIterator() {
        // Given
        Iterator<String> iterator = mock(Iterator.class);
        when(set.iterator()).thenReturn(iterator);

        // When
        Iterator<String> result = unmodifiableSet.iterator();

        // Then
        assertNotNull(result);
        assertNotSame(iterator, result);
    }

    @Test
    public void testRemove() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSet.remove("Test"));
    }

    @Test
    public void testRemoveAll() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSet.removeAll(collection));
    }

    @Test
    public void testRemoveIf() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSet.removeIf(predicate));
    }

    @Test
    public void testRetainAll() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSet.retainAll(collection));
    }

    @Test
    public void testUnmodifiableIterator() {
        // Given
        Iterator<String> iterator = mock(Iterator.class);
        when(set.iterator()).thenReturn(iterator);

        // When
        Iterator<String> result = UnmodifiableIterator.unmodifiableIterator(iterator);

        // Then
        assertNotNull(result);
        assertNotSame(iterator, result);
    }

    @Test
    public void testUnmodifiableIterator_HasNext() {
        // Given
        Iterator<String> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true);
        Iterator<String> unmodifiableIterator = UnmodifiableIterator.unmodifiableIterator(iterator);

        // When
        boolean result = unmodifiableIterator.hasNext();

        // Then
        assertTrue(result);
        verify(iterator, times(1)).hasNext();
    }

    @Test
    public void testUnmodifiableIterator_Next() {
        // Given
        Iterator<String> iterator = mock(Iterator.class);
        when(iterator.next()).thenReturn("Test");
        Iterator<String> unmodifiableIterator = UnmodifiableIterator.unmodifiableIterator(iterator);

        // When
        String result = unmodifiableIterator.next();

        // Then
        assertNotNull(result);
        assertEquals("Test", result);
        verify(iterator, times(1)).next();
    }

    @Test
    public void testUnmodifiableIterator_Remove() {
        // Given
        Iterator<String> iterator = mock(Iterator.class);
        Iterator<String> unmodifiableIterator = UnmodifiableIterator.unmodifiableIterator(iterator);

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableIterator.remove());
    }
}
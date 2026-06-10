import org.apache.commons.collections4.BoundedCollection;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.collection.UnmodifiableBoundedCollection;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnmodifiableBoundedCollectionTest {

    @Mock
    private BoundedCollection<String> boundedCollection;

    @Mock
    private Collection<String> collection;

    private UnmodifiableBoundedCollection<String> unmodifiableBoundedCollection;

    @BeforeEach
    public void setup() {
        unmodifiableBoundedCollection = new UnmodifiableBoundedCollection<>(boundedCollection);
    }

    @Test
    public void testUnmodifiableBoundedCollection_BoundedCollection() {
        // Given
        BoundedCollection<String> boundedCollection = mock(BoundedCollection.class);

        // When
        UnmodifiableBoundedCollection<String> result = UnmodifiableBoundedCollection.unmodifiableBoundedCollection(boundedCollection);

        // Then
        assertNotNull(result);
        assertSame(boundedCollection, result.decorated());
    }

    @Test
    public void testUnmodifiableBoundedCollection_BoundedCollection_Null() {
        // Given
        BoundedCollection<String> boundedCollection = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> UnmodifiableBoundedCollection.unmodifiableBoundedCollection(boundedCollection));
    }

    @Test
    public void testUnmodifiableBoundedCollection_Collection() {
        // Given
        Collection<String> collection = mock(Collection.class);
        when(collection instanceof BoundedCollection).thenReturn(true);
        BoundedCollection<String> boundedCollection = mock(BoundedCollection.class);
        when(((BoundedCollection<String>) collection).maxSize()).thenReturn(10);
        when(((BoundedCollection<String>) collection).isFull()).thenReturn(false);

        // When
        UnmodifiableBoundedCollection<String> result = UnmodifiableBoundedCollection.unmodifiableBoundedCollection(collection);

        // Then
        assertNotNull(result);
        assertSame(boundedCollection, result.decorated());
    }

    @Test
    public void testUnmodifiableBoundedCollection_Collection_Null() {
        // Given
        Collection<String> collection = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> UnmodifiableBoundedCollection.unmodifiableBoundedCollection(collection));
    }

    @Test
    public void testUnmodifiableBoundedCollection_Collection_NotBounded() {
        // Given
        Collection<String> collection = mock(Collection.class);
        when(collection instanceof BoundedCollection).thenReturn(false);

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> UnmodifiableBoundedCollection.unmodifiableBoundedCollection(collection));
    }

    @Test
    public void testAdd() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableBoundedCollection.add("Test"));
    }

    @Test
    public void testAddAll() {
        // Given
        Collection<String> collection = new ArrayList<>();

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableBoundedCollection.addAll(collection));
    }

    @Test
    public void testClear() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableBoundedCollection.clear());
    }

    @Test
    public void testIsFull() {
        // Given
        when(boundedCollection.isFull()).thenReturn(true);

        // When
        boolean result = unmodifiableBoundedCollection.isFull();

        // Then
        assertTrue(result);
        verify(boundedCollection, times(1)).isFull();
    }

    @Test
    public void testIterator() {
        // Given
        Iterator<String> iterator = mock(Iterator.class);

        // When
        Iterator<String> result = unmodifiableBoundedCollection.iterator();

        // Then
        assertNotNull(result);
        assertNotSame(iterator, result);
    }

    @Test
    public void testMaxSize() {
        // Given
        when(boundedCollection.maxSize()).thenReturn(10);

        // When
        int result = unmodifiableBoundedCollection.maxSize();

        // Then
        assertEquals(10, result);
        verify(boundedCollection, times(1)).maxSize();
    }

    @Test
    public void testRemove() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableBoundedCollection.remove("Test"));
    }

    @Test
    public void testRemoveAll() {
        // Given
        Collection<String> collection = new ArrayList<>();

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableBoundedCollection.removeAll(collection));
    }

    @Test
    public void testRemoveIf() {
        // Given
        Predicate<String> predicate = mock(Predicate.class);

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableBoundedCollection.removeIf(predicate));
    }

    @Test
    public void testRetainAll() {
        // Given
        Collection<String> collection = new ArrayList<>();

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableBoundedCollection.retainAll(collection));
    }
}
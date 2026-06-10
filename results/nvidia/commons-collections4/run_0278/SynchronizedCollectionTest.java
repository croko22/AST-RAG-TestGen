import org.apache.commons.collections4.collection.SynchronizedCollection;
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
public class SynchronizedCollectionTest {

    @Mock
    private Collection<String> collection;

    private SynchronizedCollection<String> synchronizedCollection;

    @BeforeEach
    void setup() {
        synchronizedCollection = new SynchronizedCollection<>(collection);
    }

    @Test
    void testSynchronizedCollection() {
        // Given
        Collection<String> coll = mock(Collection.class);
        // When
        SynchronizedCollection<String> synchronizedColl = SynchronizedCollection.synchronizedCollection(coll);
        // Then
        assertNotNull(synchronizedColl);
    }

    @Test
    void testAdd() {
        // Given
        String object = "object";
        when(collection.add(object)).thenReturn(true);
        // When
        boolean result = synchronizedCollection.add(object);
        // Then
        assertTrue(result);
        verify(collection, times(1)).add(object);
    }

    @Test
    void testAddAll() {
        // Given
        Collection<String> coll = new ArrayList<>();
        coll.add("object1");
        coll.add("object2");
        when(collection.addAll(coll)).thenReturn(true);
        // When
        boolean result = synchronizedCollection.addAll(coll);
        // Then
        assertTrue(result);
        verify(collection, times(1)).addAll(coll);
    }

    @Test
    void testClear() {
        // Given
        // When
        synchronizedCollection.clear();
        // Then
        verify(collection, times(1)).clear();
    }

    @Test
    void testContains() {
        // Given
        String object = "object";
        when(collection.contains(object)).thenReturn(true);
        // When
        boolean result = synchronizedCollection.contains(object);
        // Then
        assertTrue(result);
        verify(collection, times(1)).contains(object);
    }

    @Test
    void testContainsAll() {
        // Given
        Collection<String> coll = new ArrayList<>();
        coll.add("object1");
        coll.add("object2");
        when(collection.containsAll(coll)).thenReturn(true);
        // When
        boolean result = synchronizedCollection.containsAll(coll);
        // Then
        assertTrue(result);
        verify(collection, times(1)).containsAll(coll);
    }

    @Test
    void testEquals() {
        // Given
        Object object = new Object();
        when(collection.equals(object)).thenReturn(true);
        // When
        boolean result = synchronizedCollection.equals(object);
        // Then
        assertTrue(result);
        verify(collection, times(1)).equals(object);
    }

    @Test
    void testHashCode() {
        // Given
        when(collection.hashCode()).thenReturn(1);
        // When
        int result = synchronizedCollection.hashCode();
        // Then
        assertEquals(1, result);
        verify(collection, times(1)).hashCode();
    }

    @Test
    void testIsEmpty() {
        // Given
        when(collection.isEmpty()).thenReturn(true);
        // When
        boolean result = synchronizedCollection.isEmpty();
        // Then
        assertTrue(result);
        verify(collection, times(1)).isEmpty();
    }

    @Test
    void testIterator() {
        // Given
        Iterator<String> iterator = mock(Iterator.class);
        when(collection.iterator()).thenReturn(iterator);
        // When
        Iterator<String> result = synchronizedCollection.iterator();
        // Then
        assertEquals(iterator, result);
        verify(collection, times(1)).iterator();
    }

    @Test
    void testRemove() {
        // Given
        String object = "object";
        when(collection.remove(object)).thenReturn(true);
        // When
        boolean result = synchronizedCollection.remove(object);
        // Then
        assertTrue(result);
        verify(collection, times(1)).remove(object);
    }

    @Test
    void testRemoveAll() {
        // Given
        Collection<String> coll = new ArrayList<>();
        coll.add("object1");
        coll.add("object2");
        when(collection.removeAll(coll)).thenReturn(true);
        // When
        boolean result = synchronizedCollection.removeAll(coll);
        // Then
        assertTrue(result);
        verify(collection, times(1)).removeAll(coll);
    }

    @Test
    void testRemoveIf() {
        // Given
        Predicate<String> filter = mock(Predicate.class);
        when(collection.removeIf(filter)).thenReturn(true);
        // When
        boolean result = synchronizedCollection.removeIf(filter);
        // Then
        assertTrue(result);
        verify(collection, times(1)).removeIf(filter);
    }

    @Test
    void testRetainAll() {
        // Given
        Collection<String> coll = new ArrayList<>();
        coll.add("object1");
        coll.add("object2");
        when(collection.retainAll(coll)).thenReturn(true);
        // When
        boolean result = synchronizedCollection.retainAll(coll);
        // Then
        assertTrue(result);
        verify(collection, times(1)).retainAll(coll);
    }

    @Test
    void testSize() {
        // Given
        when(collection.size()).thenReturn(10);
        // When
        int result = synchronizedCollection.size();
        // Then
        assertEquals(10, result);
        verify(collection, times(1)).size();
    }

    @Test
    void testToString() {
        // Given
        when(collection.toString()).thenReturn("collection");
        // When
        String result = synchronizedCollection.toString();
        // Then
        assertEquals("collection", result);
        verify(collection, times(1)).toString();
    }
}
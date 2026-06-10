import org.apache.commons.collections4.collection.CompositeCollection;
import org.apache.commons.collections4.collection.CompositeCollection.CollectionMutator;
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
public class CompositeCollectionTest {

    @Mock
    private Collection<String> collection1;

    @Mock
    private Collection<String> collection2;

    private CompositeCollection<String> compositeCollection;

    @BeforeEach
    public void setup() {
        compositeCollection = new CompositeCollection<>();
        compositeCollection.addComposited(collection1, collection2);
    }

    @Test
    public void testAdd() {
        // Given
        CollectionMutator<String> mutator = mock(CollectionMutator.class);
        when(mutator.add(any(), any(), any())).thenReturn(true);
        compositeCollection.setMutator(mutator);

        // When
        boolean result = compositeCollection.add("element");

        // Then
        assertTrue(result);
        verify(mutator, times(1)).add(compositeCollection, compositeCollection.getCollections(), "element");
    }

    @Test
    public void testAddAll() {
        // Given
        Collection<String> elements = new ArrayList<>();
        elements.add("element1");
        elements.add("element2");
        CollectionMutator<String> mutator = mock(CollectionMutator.class);
        when(mutator.addAll(any(), any(), any())).thenReturn(true);
        compositeCollection.setMutator(mutator);

        // When
        boolean result = compositeCollection.addAll(elements);

        // Then
        assertTrue(result);
        verify(mutator, times(1)).addAll(compositeCollection, compositeCollection.getCollections(), elements);
    }

    @Test
    public void testAddComposited() {
        // Given
        Collection<String> collection3 = mock(Collection.class);

        // When
        compositeCollection.addComposited(collection3);

        // Then
        assertEquals(3, compositeCollection.getCollections().size());
        assertTrue(compositeCollection.getCollections().contains(collection3));
    }

    @Test
    public void testClear() {
        // Given
        when(collection1.isEmpty()).thenReturn(false);
        when(collection2.isEmpty()).thenReturn(false);

        // When
        compositeCollection.clear();

        // Then
        verify(collection1, times(1)).clear();
        verify(collection2, times(1)).clear();
    }

    @Test
    public void testContains() {
        // Given
        when(collection1.contains("element")).thenReturn(false);
        when(collection2.contains("element")).thenReturn(true);

        // When
        boolean result = compositeCollection.contains("element");

        // Then
        assertTrue(result);
    }

    @Test
    public void testContainsAll() {
        // Given
        Collection<String> elements = new ArrayList<>();
        elements.add("element1");
        elements.add("element2");
        when(collection1.containsAll(elements)).thenReturn(true);
        when(collection2.containsAll(elements)).thenReturn(true);

        // When
        boolean result = compositeCollection.containsAll(elements);

        // Then
        assertTrue(result);
    }

    @Test
    public void testGetCollections() {
        // When
        List<Collection<String>> collections = compositeCollection.getCollections();

        // Then
        assertEquals(2, collections.size());
        assertTrue(collections.contains(collection1));
        assertTrue(collections.contains(collection2));
    }

    @Test
    public void testIsEmpty() {
        // Given
        when(collection1.isEmpty()).thenReturn(true);
        when(collection2.isEmpty()).thenReturn(true);

        // When
        boolean result = compositeCollection.isEmpty();

        // Then
        assertTrue(result);
    }

    @Test
    public void testIterator() {
        // When
        Iterator<String> iterator = compositeCollection.iterator();

        // Then
        assertNotNull(iterator);
    }

    @Test
    public void testRemove() {
        // Given
        CollectionMutator<String> mutator = mock(CollectionMutator.class);
        when(mutator.remove(any(), any(), any())).thenReturn(true);
        compositeCollection.setMutator(mutator);

        // When
        boolean result = compositeCollection.remove("element");

        // Then
        assertTrue(result);
        verify(mutator, times(1)).remove(compositeCollection, compositeCollection.getCollections(), "element");
    }

    @Test
    public void testRemoveAll() {
        // Given
        Collection<String> elements = new ArrayList<>();
        elements.add("element1");
        elements.add("element2");

        // When
        boolean result = compositeCollection.removeAll(elements);

        // Then
        assertTrue(result);
        verify(collection1, times(1)).removeAll(elements);
        verify(collection2, times(1)).removeAll(elements);
    }

    @Test
    public void testRemoveComposited() {
        // When
        compositeCollection.removeComposited(collection1);

        // Then
        assertEquals(1, compositeCollection.getCollections().size());
        assertFalse(compositeCollection.getCollections().contains(collection1));
    }

    @Test
    public void testRemoveIf() {
        // Given
        Predicate<String> predicate = mock(Predicate.class);
        when(predicate.test(any())).thenReturn(true);

        // When
        boolean result = compositeCollection.removeIf(predicate);

        // Then
        assertTrue(result);
        verify(collection1, times(1)).removeIf(predicate);
        verify(collection2, times(1)).removeIf(predicate);
    }

    @Test
    public void testRetainAll() {
        // Given
        Collection<String> elements = new ArrayList<>();
        elements.add("element1");
        elements.add("element2");

        // When
        boolean result = compositeCollection.retainAll(elements);

        // Then
        assertTrue(result);
        verify(collection1, times(1)).retainAll(elements);
        verify(collection2, times(1)).retainAll(elements);
    }

    @Test
    public void testSetMutator() {
        // Given
        CollectionMutator<String> mutator = mock(CollectionMutator.class);

        // When
        compositeCollection.setMutator(mutator);

        // Then
        assertEquals(mutator, compositeCollection.getMutator());
    }

    @Test
    public void testSize() {
        // Given
        when(collection1.size()).thenReturn(1);
        when(collection2.size()).thenReturn(2);

        // When
        int result = compositeCollection.size();

        // Then
        assertEquals(3, result);
    }

    @Test
    public void testToCollection() {
        // When
        Collection<String> collection = compositeCollection.toCollection();

        // Then
        assertNotNull(collection);
        assertEquals(compositeCollection.size(), collection.size());
    }
}
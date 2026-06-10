import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.collection.IndexedCollection;
import org.apache.commons.collections4.map.MultiValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IndexedCollectionTest {

    @Mock
    private Transformer<String, Integer> transformer;

    @Mock
    private Collection<String> collection;

    private IndexedCollection<Integer, String> indexedCollection;

    @BeforeEach
    void setup() {
        when(transformer.apply(any())).thenAnswer(invocation -> 1);
        indexedCollection = IndexedCollection.nonUniqueIndexedCollection(collection, transformer);
    }

    @Test
    void testNonUniqueIndexedCollection() {
        // Given
        Collection<String> coll = new ArrayList<>();
        Transformer<String, Integer> keyTransformer = transformer;

        // When
        IndexedCollection<Integer, String> result = IndexedCollection.nonUniqueIndexedCollection(coll, keyTransformer);

        // Then
        assertNotNull(result);
    }

    @Test
    void testUniqueIndexedCollection() {
        // Given
        Collection<String> coll = new ArrayList<>();
        Transformer<String, Integer> keyTransformer = transformer;

        // When
        IndexedCollection<Integer, String> result = IndexedCollection.uniqueIndexedCollection(coll, keyTransformer);

        // Then
        assertNotNull(result);
    }

    @Test
    void testAdd() {
        // Given
        String object = "object";

        // When
        boolean result = indexedCollection.add(object);

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

        // When
        boolean result = indexedCollection.addAll(coll);

        // Then
        assertTrue(result);
        verify(collection, times(1)).addAll(coll);
    }

    @Test
    void testClear() {
        // When
        indexedCollection.clear();

        // Then
        verify(collection, times(1)).clear();
    }

    @Test
    void testContains() {
        // Given
        String object = "object";

        // When
        boolean result = indexedCollection.contains(object);

        // Then
        assertTrue(result);
    }

    @Test
    void testContainsAll() {
        // Given
        Collection<String> coll = new ArrayList<>();
        coll.add("object1");
        coll.add("object2");

        // When
        boolean result = indexedCollection.containsAll(coll);

        // Then
        assertTrue(result);
    }

    @Test
    void testGet() {
        // Given
        Integer key = 1;

        // When
        String result = indexedCollection.get(key);

        // Then
        assertNotNull(result);
    }

    @Test
    void testReindex() {
        // When
        indexedCollection.reindex();

        // Then
        verify(collection, times(1)).clear();
    }

    @Test
    void testRemove() {
        // Given
        String object = "object";

        // When
        boolean result = indexedCollection.remove(object);

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

        // When
        boolean result = indexedCollection.removeAll(coll);

        // Then
        assertTrue(result);
        verify(collection, times(1)).removeAll(coll);
    }

    @Test
    void testRemoveIf() {
        // Given
        Predicate<String> filter = string -> true;

        // When
        boolean result = indexedCollection.removeIf(filter);

        // Then
        assertTrue(result);
    }

    @Test
    void testRetainAll() {
        // Given
        Collection<String> coll = new ArrayList<>();
        coll.add("object1");
        coll.add("object2");

        // When
        boolean result = indexedCollection.retainAll(coll);

        // Then
        assertTrue(result);
    }

    @Test
    void testValues() {
        // Given
        Integer key = 1;

        // When
        Collection<String> result = indexedCollection.values(key);

        // Then
        assertNotNull(result);
    }
}
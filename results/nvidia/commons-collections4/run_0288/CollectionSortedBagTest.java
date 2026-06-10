import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.bag.CollectionSortedBag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CollectionSortedBagTest {

    @Mock
    private SortedBag<String> sortedBag;

    private CollectionSortedBag<String> collectionSortedBag;

    @BeforeEach
    void setup() {
        collectionSortedBag = new CollectionSortedBag<>(sortedBag);
    }

    @Test
    void testCollectionSortedBag() {
        // Given
        SortedBag<String> bag = mock(SortedBag.class);

        // When
        CollectionSortedBag<String> result = CollectionSortedBag.collectionSortedBag(bag);

        // Then
        assertNotNull(result);
    }

    @Test
    void testAdd() {
        // Given
        String object = "Test Object";

        // When
        boolean result = collectionSortedBag.add(object);

        // Then
        assertTrue(result);
        verify(sortedBag).add(object, 1);
    }

    @Test
    void testAddWithCount() {
        // Given
        String object = "Test Object";
        int count = 2;

        // When
        boolean result = collectionSortedBag.add(object, count);

        // Then
        assertTrue(result);
        verify(sortedBag).add(object, count);
    }

    @Test
    void testAddAll() {
        // Given
        Collection<String> collection = new ArrayList<>();
        collection.add("Test Object 1");
        collection.add("Test Object 2");

        // When
        boolean result = collectionSortedBag.addAll(collection);

        // Then
        assertTrue(result);
        verify(sortedBag, times(2)).add(any(), eq(1));
    }

    @Test
    void testContainsAll() {
        // Given
        Collection<String> collection = new ArrayList<>();
        collection.add("Test Object 1");
        collection.add("Test Object 2");

        // When
        when(sortedBag.contains(any())).thenReturn(true);
        boolean result = collectionSortedBag.containsAll(collection);

        // Then
        assertTrue(result);
        verify(sortedBag, times(2)).contains(any());
    }

    @Test
    void testRemove() {
        // Given
        String object = "Test Object";

        // When
        boolean result = collectionSortedBag.remove(object);

        // Then
        assertTrue(result);
        verify(sortedBag).add(object, -1);
    }

    @Test
    void testRemoveAll() {
        // Given
        Collection<String> collection = new ArrayList<>();
        collection.add("Test Object 1");
        collection.add("Test Object 2");

        // When
        boolean result = collectionSortedBag.removeAll(collection);

        // Then
        assertTrue(result);
        verify(sortedBag, times(2)).add(any(), anyInt());
    }

    @Test
    void testRetainAll() {
        // Given
        Collection<String> collection = new ArrayList<>();
        collection.add("Test Object 1");
        collection.add("Test Object 2");

        // When
        boolean result = collectionSortedBag.retainAll(collection);

        // Then
        assertTrue(result);
        verify(sortedBag).iterator();
    }

    @Test
    void testCollectionSortedBagNull() {
        // Given
        SortedBag<String> bag = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> CollectionSortedBag.collectionSortedBag(bag));
    }
}
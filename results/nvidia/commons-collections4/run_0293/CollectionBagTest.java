import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.CollectionBag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CollectionBagTest {

    @Mock
    private Bag<String> bag;

    private CollectionBag<String> collectionBag;

    @BeforeEach
    public void setup() {
        collectionBag = new CollectionBag<>(bag);
    }

    @Test
    public void testCollectionBag() {
        // Given
        Bag<String> inputBag = mock(Bag.class);

        // When
        CollectionBag<String> result = CollectionBag.collectionBag(inputBag);

        // Then
        assertNotNull(result);
        assertNotSame(inputBag, result);
    }

    @Test
    public void testAdd() {
        // Given
        String object = "Test Object";

        // When
        boolean result = collectionBag.add(object);

        // Then
        assertTrue(result);
        verify(bag, times(1)).add(object, 1);
    }

    @Test
    public void testAddWithCount() {
        // Given
        String object = "Test Object";
        int count = 5;

        // When
        boolean result = collectionBag.add(object, count);

        // Then
        assertTrue(result);
        verify(bag, times(1)).add(object, count);
    }

    @Test
    public void testAddAll() {
        // Given
        Collection<String> collection = new ArrayList<>();
        collection.add("Test Object 1");
        collection.add("Test Object 2");

        // When
        boolean result = collectionBag.addAll(collection);

        // Then
        assertTrue(result);
        verify(bag, times(1)).add("Test Object 1", 1);
        verify(bag, times(1)).add("Test Object 2", 1);
    }

    @Test
    public void testContainsAll() {
        // Given
        Collection<String> collection = new ArrayList<>();
        collection.add("Test Object 1");
        collection.add("Test Object 2");

        // When
        when(bag.containsAll(collection)).thenReturn(true);
        boolean result = collectionBag.containsAll(collection);

        // Then
        assertTrue(result);
        verify(bag, times(1)).containsAll(collection);
    }

    @Test
    public void testRemove() {
        // Given
        String object = "Test Object";

        // When
        boolean result = collectionBag.remove(object);

        // Then
        assertTrue(result);
        verify(bag, times(1)).remove(object, 1);
    }

    @Test
    public void testRemoveAll() {
        // Given
        Collection<String> collection = new ArrayList<>();
        collection.add("Test Object 1");
        collection.add("Test Object 2");

        // When
        boolean result = collectionBag.removeAll(collection);

        // Then
        assertTrue(result);
        verify(bag, times(1)).remove("Test Object 1", 1);
        verify(bag, times(1)).remove("Test Object 2", 1);
    }

    @Test
    public void testRetainAll() {
        // Given
        Collection<String> collection = new ArrayList<>();
        collection.add("Test Object 1");
        collection.add("Test Object 2");

        // When
        boolean result = collectionBag.retainAll(collection);

        // Then
        assertTrue(result);
        verify(bag, times(1)).retainAll(collection);
    }
}
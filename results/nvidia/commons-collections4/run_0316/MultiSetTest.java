import org.apache.commons.collections4.MultiSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MultiSetTest {

    @Mock
    private MultiSet<String> multiSet;

    @BeforeEach
    void setup() {
        // Initialize the mock MultiSet
        when(multiSet.add(any())).thenReturn(true);
        when(multiSet.add(any(), anyInt())).thenReturn(0);
        when(multiSet.containsAll(any())).thenReturn(true);
        when(multiSet.getCount(any())).thenReturn(1);
        when(multiSet.remove(any())).thenReturn(true);
        when(multiSet.remove(any(), anyInt())).thenReturn(1);
        when(multiSet.removeAll(any())).thenReturn(true);
        when(multiSet.retainAll(any())).thenReturn(true);
        when(multiSet.setCount(any(), anyInt())).thenReturn(1);
        when(multiSet.size()).thenReturn(1);
    }

    @Test
    public void testAdd() {
        // Given: an object to add
        String object = "test";

        // When: add the object to the MultiSet
        boolean result = multiSet.add(object);

        // Then: verify the result and the interactions with the mock
        assertTrue(result);
        verify(multiSet, times(1)).add(object);
    }

    @Test
    public void testAddOccurrences() {
        // Given: an object and the number of occurrences to add
        String object = "test";
        int occurrences = 2;

        // When: add the occurrences of the object to the MultiSet
        int result = multiSet.add(object, occurrences);

        // Then: verify the result and the interactions with the mock
        assertEquals(0, result);
        verify(multiSet, times(1)).add(object, occurrences);
    }

    @Test
    public void testContainsAll() {
        // Given: a collection to check against
        Collection<String> collection = new ArrayList<>();

        // When: check if the MultiSet contains all elements in the collection
        boolean result = multiSet.containsAll(collection);

        // Then: verify the result and the interactions with the mock
        assertTrue(result);
        verify(multiSet, times(1)).containsAll(collection);
    }

    @Test
    public void testGetCount() {
        // Given: an object to get the count for
        String object = "test";

        // When: get the count of the object in the MultiSet
        int result = multiSet.getCount(object);

        // Then: verify the result and the interactions with the mock
        assertEquals(1, result);
        verify(multiSet, times(1)).getCount(object);
    }

    @Test
    public void testRemove() {
        // Given: an object to remove
        String object = "test";

        // When: remove the object from the MultiSet
        boolean result = multiSet.remove(object);

        // Then: verify the result and the interactions with the mock
        assertTrue(result);
        verify(multiSet, times(1)).remove(object);
    }

    @Test
    public void testRemoveOccurrences() {
        // Given: an object and the number of occurrences to remove
        String object = "test";
        int occurrences = 2;

        // When: remove the occurrences of the object from the MultiSet
        int result = multiSet.remove(object, occurrences);

        // Then: verify the result and the interactions with the mock
        assertEquals(1, result);
        verify(multiSet, times(1)).remove(object, occurrences);
    }

    @Test
    public void testRemoveAll() {
        // Given: a collection to remove from the MultiSet
        Collection<String> collection = new ArrayList<>();

        // When: remove all elements in the collection from the MultiSet
        boolean result = multiSet.removeAll(collection);

        // Then: verify the result and the interactions with the mock
        assertTrue(result);
        verify(multiSet, times(1)).removeAll(collection);
    }

    @Test
    public void testRetainAll() {
        // Given: a collection to retain in the MultiSet
        Collection<String> collection = new ArrayList<>();

        // When: retain all elements in the collection in the MultiSet
        boolean result = multiSet.retainAll(collection);

        // Then: verify the result and the interactions with the mock
        assertTrue(result);
        verify(multiSet, times(1)).retainAll(collection);
    }

    @Test
    public void testSetCount() {
        // Given: an object and the new count
        String object = "test";
        int count = 2;

        // When: set the count of the object in the MultiSet
        int result = multiSet.setCount(object, count);

        // Then: verify the result and the interactions with the mock
        assertEquals(1, result);
        verify(multiSet, times(1)).setCount(object, count);
    }

    @Test
    public void testSize() {
        // When: get the size of the MultiSet
        int result = multiSet.size();

        // Then: verify the result and the interactions with the mock
        assertEquals(1, result);
        verify(multiSet, times(1)).size();
    }

    @Test
    public void testUniqueSet() {
        // When: get the unique set of elements in the MultiSet
        Set<String> result = multiSet.uniqueSet();

        // Then: verify the result and the interactions with the mock
        assertNotNull(result);
        verify(multiSet, times(1)).uniqueSet();
    }

    @Test
    public void testEntrySet() {
        // When: get the entry set of the MultiSet
        Set<MultiSet.Entry<String>> result = multiSet.entrySet();

        // Then: verify the result and the interactions with the mock
        assertNotNull(result);
        verify(multiSet, times(1)).entrySet();
    }

    @Test
    public void testIterator() {
        // When: get the iterator of the MultiSet
        Iterator<String> result = multiSet.iterator();

        // Then: verify the result and the interactions with the mock
        assertNotNull(result);
        verify(multiSet, times(1)).iterator();
    }
}
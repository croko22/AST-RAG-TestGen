import org.apache.commons.collections4.Bag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BagTest {

    @Mock
    private Bag<String> bag;

    @BeforeEach
    void setup() {
        // Initialize the bag with some elements
        when(bag.add("apple")).thenReturn(true);
        when(bag.add("banana")).thenReturn(true);
        when(bag.add("orange")).thenReturn(true);
    }

    @Test
    public void testAdd() {
        // Given: an element to add
        String element = "grape";

        // When: add the element to the bag
        boolean result = bag.add(element);

        // Then: verify the result and the interactions with the bag
        assertTrue(result);
        verify(bag, times(1)).add(element);
    }

    @Test
    public void testAddExistingElement() {
        // Given: an existing element in the bag
        String element = "apple";

        // When: add the existing element to the bag
        boolean result = bag.add(element);

        // Then: verify the result and the interactions with the bag
        assertFalse(result);
        verify(bag, times(1)).add(element);
    }

    @Test
    public void testAddMultipleCopies() {
        // Given: an element and the number of copies to add
        String element = "grape";
        int nCopies = 3;

        // When: add multiple copies of the element to the bag
        boolean result = bag.add(element, nCopies);

        // Then: verify the result and the interactions with the bag
        assertTrue(result);
        verify(bag, times(1)).add(element, nCopies);
    }

    @Test
    public void testContainsAll() {
        // Given: a collection of elements to check
        Collection<String> collection = Set.of("apple", "banana");

        // When: check if the bag contains all elements in the collection
        boolean result = bag.containsAll(collection);

        // Then: verify the result and the interactions with the bag
        assertTrue(result);
        verify(bag, times(1)).containsAll(collection);
    }

    @Test
    public void testGetCount() {
        // Given: an element to get the count for
        String element = "apple";

        // When: get the count of the element in the bag
        int count = bag.getCount(element);

        // Then: verify the count and the interactions with the bag
        assertEquals(1, count);
        verify(bag, times(1)).getCount(element);
    }

    @Test
    public void testGetCountNonExistingElement() {
        // Given: a non-existing element in the bag
        String element = "grape";

        // When: get the count of the non-existing element in the bag
        int count = bag.getCount(element);

        // Then: verify the count and the interactions with the bag
        assertEquals(0, count);
        verify(bag, times(1)).getCount(element);
    }

    @Test
    public void testIterator() {
        // When: get an iterator over the bag
        Iterator<String> iterator = bag.iterator();

        // Then: verify the iterator and the interactions with the bag
        assertNotNull(iterator);
        verify(bag, times(1)).iterator();
    }

    @Test
    public void testRemove() {
        // Given: an element to remove
        String element = "apple";

        // When: remove the element from the bag
        boolean result = bag.remove(element);

        // Then: verify the result and the interactions with the bag
        assertTrue(result);
        verify(bag, times(1)).remove(element);
    }

    @Test
    public void testRemoveNonExistingElement() {
        // Given: a non-existing element in the bag
        String element = "grape";

        // When: remove the non-existing element from the bag
        boolean result = bag.remove(element);

        // Then: verify the result and the interactions with the bag
        assertFalse(result);
        verify(bag, times(1)).remove(element);
    }

    @Test
    public void testRemoveMultipleCopies() {
        // Given: an element and the number of copies to remove
        String element = "apple";
        int nCopies = 1;

        // When: remove multiple copies of the element from the bag
        boolean result = bag.remove(element, nCopies);

        // Then: verify the result and the interactions with the bag
        assertTrue(result);
        verify(bag, times(1)).remove(element, nCopies);
    }

    @Test
    public void testRemoveAll() {
        // Given: a collection of elements to remove
        Collection<String> collection = Set.of("apple", "banana");

        // When: remove all elements in the collection from the bag
        boolean result = bag.removeAll(collection);

        // Then: verify the result and the interactions with the bag
        assertTrue(result);
        verify(bag, times(1)).removeAll(collection);
    }

    @Test
    public void testRetainAll() {
        // Given: a collection of elements to retain
        Collection<String> collection = Set.of("apple", "orange");

        // When: retain all elements in the collection in the bag
        boolean result = bag.retainAll(collection);

        // Then: verify the result and the interactions with the bag
        assertTrue(result);
        verify(bag, times(1)).retainAll(collection);
    }

    @Test
    public void testSize() {
        // When: get the size of the bag
        int size = bag.size();

        // Then: verify the size and the interactions with the bag
        assertEquals(3, size);
        verify(bag, times(1)).size();
    }

    @Test
    public void testUniqueSet() {
        // When: get the unique set of elements in the bag
        Set<String> uniqueSet = bag.uniqueSet();

        // Then: verify the unique set and the interactions with the bag
        assertNotNull(uniqueSet);
        verify(bag, times(1)).uniqueSet();
    }
}
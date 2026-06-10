import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.AbstractMapBag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractMapBagTest {

    @Mock
    private Map<String, AbstractMapBag.MutableInteger> map;

    private AbstractMapBag<String> bag;

    @BeforeEach
    void setup() {
        bag = new AbstractMapBag<>(map);
    }

    @Test
    void testAdd() {
        // Given
        String object = "Test Object";
        when(map.get(object)).thenReturn(null);

        // When
        boolean result = bag.add(object);

        // Then
        assertTrue(result);
        verify(map).put(object, new AbstractMapBag.MutableInteger(1));
    }

    @Test
    void testAddExistingObject() {
        // Given
        String object = "Test Object";
        AbstractMapBag.MutableInteger mutableInteger = new AbstractMapBag.MutableInteger(1);
        when(map.get(object)).thenReturn(mutableInteger);

        // When
        boolean result = bag.add(object);

        // Then
        assertFalse(result);
        assertEquals(2, mutableInteger.value);
    }

    @Test
    void testAddWithNCopies() {
        // Given
        String object = "Test Object";
        int nCopies = 5;
        when(map.get(object)).thenReturn(null);

        // When
        boolean result = bag.add(object, nCopies);

        // Then
        assertTrue(result);
        verify(map).put(object, new AbstractMapBag.MutableInteger(nCopies));
    }

    @Test
    void testAddAll() {
        // Given
        Collection<String> collection = new ArrayList<>();
        collection.add("Test Object 1");
        collection.add("Test Object 2");
        when(map.get("Test Object 1")).thenReturn(null);
        when(map.get("Test Object 2")).thenReturn(null);

        // When
        boolean result = bag.addAll(collection);

        // Then
        assertTrue(result);
        verify(map).put("Test Object 1", new AbstractMapBag.MutableInteger(1));
        verify(map).put("Test Object 2", new AbstractMapBag.MutableInteger(1));
    }

    @Test
    void testClear() {
        // Given
        when(map.isEmpty()).thenReturn(false);

        // When
        bag.clear();

        // Then
        verify(map).clear();
    }

    @Test
    void testContains() {
        // Given
        String object = "Test Object";
        when(map.containsKey(object)).thenReturn(true);

        // When
        boolean result = bag.contains(object);

        // Then
        assertTrue(result);
    }

    @Test
    void testContainsAll() {
        // Given
        Collection<String> collection = new ArrayList<>();
        collection.add("Test Object 1");
        collection.add("Test Object 2");
        when(map.containsKey("Test Object 1")).thenReturn(true);
        when(map.containsKey("Test Object 2")).thenReturn(true);

        // When
        boolean result = bag.containsAll(collection);

        // Then
        assertTrue(result);
    }

    @Test
    void testGetCount() {
        // Given
        String object = "Test Object";
        AbstractMapBag.MutableInteger mutableInteger = new AbstractMapBag.MutableInteger(5);
        when(map.get(object)).thenReturn(mutableInteger);

        // When
        int result = bag.getCount(object);

        // Then
        assertEquals(5, result);
    }

    @Test
    void testIsEmpty() {
        // Given
        when(map.isEmpty()).thenReturn(true);

        // When
        boolean result = bag.isEmpty();

        // Then
        assertTrue(result);
    }

    @Test
    void testIterator() {
        // Given
        Map<String, AbstractMapBag.MutableInteger> map = new HashMap<>();
        map.put("Test Object 1", new AbstractMapBag.MutableInteger(2));
        map.put("Test Object 2", new AbstractMapBag.MutableInteger(3));
        AbstractMapBag<String> bag = new AbstractMapBag<>(map);
        Iterator<String> iterator = bag.iterator();

        // When
        StringBuilder result = new StringBuilder();
        while (iterator.hasNext()) {
            result.append(iterator.next()).append(",");
        }

        // Then
        assertEquals("Test Object 1,Test Object 1,Test Object 2,Test Object 2,Test Object 2,", result.toString());
    }

    @Test
    void testRemove() {
        // Given
        String object = "Test Object";
        AbstractMapBag.MutableInteger mutableInteger = new AbstractMapBag.MutableInteger(1);
        when(map.get(object)).thenReturn(mutableInteger);

        // When
        boolean result = bag.remove(object);

        // Then
        assertTrue(result);
        verify(map).remove(object);
    }

    @Test
    void testRemoveWithNCopies() {
        // Given
        String object = "Test Object";
        AbstractMapBag.MutableInteger mutableInteger = new AbstractMapBag.MutableInteger(5);
        when(map.get(object)).thenReturn(mutableInteger);

        // When
        boolean result = bag.remove(object, 3);

        // Then
        assertTrue(result);
        assertEquals(2, mutableInteger.value);
    }

    @Test
    void testRemoveAll() {
        // Given
        Collection<String> collection = new ArrayList<>();
        collection.add("Test Object 1");
        collection.add("Test Object 2");
        when(map.containsKey("Test Object 1")).thenReturn(true);
        when(map.containsKey("Test Object 2")).thenReturn(true);

        // When
        boolean result = bag.removeAll(collection);

        // Then
        assertTrue(result);
        verify(map).remove("Test Object 1");
        verify(map).remove("Test Object 2");
    }

    @Test
    void testRetainAll() {
        // Given
        Collection<String> collection = new ArrayList<>();
        collection.add("Test Object 1");
        collection.add("Test Object 2");
        when(map.containsKey("Test Object 1")).thenReturn(true);
        when(map.containsKey("Test Object 2")).thenReturn(true);

        // When
        boolean result = bag.retainAll(collection);

        // Then
        assertTrue(result);
    }

    @Test
    void testSize() {
        // Given
        when(map.size()).thenReturn(5);

        // When
        int result = bag.size();

        // Then
        assertEquals(5, result);
    }

    @Test
    void testToString() {
        // Given
        Map<String, AbstractMapBag.MutableInteger> map = new HashMap<>();
        map.put("Test Object 1", new AbstractMapBag.MutableInteger(2));
        map.put("Test Object 2", new AbstractMapBag.MutableInteger(3));
        AbstractMapBag<String> bag = new AbstractMapBag<>(map);

        // When
        String result = bag.toString();

        // Then
        assertEquals("[2:Test Object 1,3:Test Object 2]", result);
    }

    @Test
    void testUniqueSet() {
        // Given
        Map<String, AbstractMapBag.MutableInteger> map = new HashMap<>();
        map.put("Test Object 1", new AbstractMapBag.MutableInteger(2));
        map.put("Test Object 2", new AbstractMapBag.MutableInteger(3));
        AbstractMapBag<String> bag = new AbstractMapBag<>(map);

        // When
        Set<String> result = bag.uniqueSet();

        // Then
        assertEquals(2, result.size());
    }
}
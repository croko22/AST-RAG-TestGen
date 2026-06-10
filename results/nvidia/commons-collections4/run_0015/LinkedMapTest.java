import org.apache.commons.collections4.map.LinkedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LinkedMapTest {

    @Mock
    private LinkedMap<String, String> linkedMap;

    @BeforeEach
    public void setup() {
        linkedMap = new LinkedMap<>();
    }

    @Test
    public void testClear() {
        // Given
        linkedMap.put("key1", "value1");
        linkedMap.put("key2", "value2");

        // When
        linkedMap.clear();

        // Then
        assertTrue(linkedMap.isEmpty());
    }

    @Test
    public void testContains() {
        // Given
        linkedMap.put("key1", "value1");

        // When
        boolean result = linkedMap.containsKey("key1");

        // Then
        assertTrue(result);
    }

    @Test
    public void testContainsAll() {
        // Given
        linkedMap.put("key1", "value1");
        linkedMap.put("key2", "value2");
        Collection<String> keys = new ArrayList<>();
        keys.add("key1");
        keys.add("key2");

        // When
        boolean result = linkedMap.keySet().containsAll(keys);

        // Then
        assertTrue(result);
    }

    @Test
    public void testGet() {
        // Given
        linkedMap.put("key1", "value1");

        // When
        String result = linkedMap.get(0);

        // Then
        assertEquals("key1", result);
    }

    @Test
    public void testIndexOf() {
        // Given
        linkedMap.put("key1", "value1");
        linkedMap.put("key2", "value2");

        // When
        int result = linkedMap.indexOf("key1");

        // Then
        assertEquals(0, result);
    }

    @Test
    public void testIterator() {
        // Given
        linkedMap.put("key1", "value1");
        linkedMap.put("key2", "value2");

        // When
        Iterator<String> iterator = linkedMap.keySet().iterator();

        // Then
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testLastIndexOf() {
        // Given
        linkedMap.put("key1", "value1");
        linkedMap.put("key2", "value2");

        // When
        int result = linkedMap.lastIndexOf("key1");

        // Then
        assertEquals(0, result);
    }

    @Test
    public void testListIterator() {
        // Given
        linkedMap.put("key1", "value1");
        linkedMap.put("key2", "value2");

        // When
        ListIterator<String> listIterator = linkedMap.asList().listIterator();

        // Then
        assertNotNull(listIterator);
        assertTrue(listIterator.hasNext());
    }

    @Test
    public void testListIteratorWithIndex() {
        // Given
        linkedMap.put("key1", "value1");
        linkedMap.put("key2", "value2");

        // When
        ListIterator<String> listIterator = linkedMap.asList().listIterator(1);

        // Then
        assertNotNull(listIterator);
        assertTrue(listIterator.hasNext());
    }

    @Test
    public void testRemove() {
        // Given
        linkedMap.put("key1", "value1");
        linkedMap.put("key2", "value2");

        // When
        String result = linkedMap.remove(0);

        // Then
        assertEquals("value1", result);
    }

    @Test
    public void testRemoveAll() {
        // Given
        linkedMap.put("key1", "value1");
        linkedMap.put("key2", "value2");
        Collection<String> keys = new ArrayList<>();
        keys.add("key1");

        // When
        boolean result = linkedMap.keySet().removeAll(keys);

        // Then
        assertTrue(result);
    }

    @Test
    public void testRemoveIf() {
        // Given
        linkedMap.put("key1", "value1");
        linkedMap.put("key2", "value2");

        // When
        boolean result = linkedMap.keySet().removeIf(key -> key.equals("key1"));

        // Then
        assertTrue(result);
    }

    @Test
    public void testRetainAll() {
        // Given
        linkedMap.put("key1", "value1");
        linkedMap.put("key2", "value2");
        Collection<String> keys = new ArrayList<>();
        keys.add("key1");

        // When
        boolean result = linkedMap.keySet().retainAll(keys);

        // Then
        assertTrue(result);
    }

    @Test
    public void testSize() {
        // Given
        linkedMap.put("key1", "value1");
        linkedMap.put("key2", "value2");

        // When
        int result = linkedMap.size();

        // Then
        assertEquals(2, result);
    }

    @Test
    public void testSubList() {
        // Given
        linkedMap.put("key1", "value1");
        linkedMap.put("key2", "value2");

        // When
        List<String> result = linkedMap.asList().subList(0, 1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testAsList() {
        // Given
        linkedMap.put("key1", "value1");
        linkedMap.put("key2", "value2");

        // When
        List<String> result = linkedMap.asList();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testClone() {
        // Given
        linkedMap.put("key1", "value1");
        linkedMap.put("key2", "value2");

        // When
        LinkedMap<String, String> result = linkedMap.clone();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetValue() {
        // Given
        linkedMap.put("key1", "value1");
        linkedMap.put("key2", "value2");

        // When
        String result = linkedMap.getValue(0);

        // Then
        assertEquals("value1", result);
    }
}
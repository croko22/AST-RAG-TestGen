import org.apache.commons.collections4.OrderedBidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TreeBidiMapTest {

    private TreeBidiMap<String, Integer> treeBidiMap;

    @BeforeEach
    public void setup() {
        treeBidiMap = new TreeBidiMap<>();
    }

    @Test
    public void testClear() {
        // Given
        treeBidiMap.put("A", 1);
        treeBidiMap.put("B", 2);

        // When
        treeBidiMap.clear();

        // Then
        assertTrue(treeBidiMap.isEmpty());
        assertEquals(0, treeBidiMap.size());
    }

    @Test
    public void testContainsKey() {
        // Given
        treeBidiMap.put("A", 1);

        // When
        boolean result = treeBidiMap.containsKey("A");

        // Then
        assertTrue(result);
    }

    @Test
    public void testContainsValue() {
        // Given
        treeBidiMap.put("A", 1);

        // When
        boolean result = treeBidiMap.containsValue(1);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEntrySet() {
        // Given
        treeBidiMap.put("A", 1);
        treeBidiMap.put("B", 2);

        // When
        Set<Map.Entry<String, Integer>> entrySet = treeBidiMap.entrySet();

        // Then
        assertEquals(2, entrySet.size());
    }

    @Test
    public void testEquals() {
        // Given
        treeBidiMap.put("A", 1);
        treeBidiMap.put("B", 2);
        TreeBidiMap<String, Integer> otherMap = new TreeBidiMap<>();
        otherMap.put("A", 1);
        otherMap.put("B", 2);

        // When
        boolean result = treeBidiMap.equals(otherMap);

        // Then
        assertTrue(result);
    }

    @Test
    public void testFirstKey() {
        // Given
        treeBidiMap.put("A", 1);
        treeBidiMap.put("B", 2);

        // When
        String result = treeBidiMap.firstKey();

        // Then
        assertEquals("A", result);
    }

    @Test
    public void testGet() {
        // Given
        treeBidiMap.put("A", 1);

        // When
        Integer result = treeBidiMap.get("A");

        // Then
        assertEquals(1, result);
    }

    @Test
    public void testGetKey() {
        // Given
        treeBidiMap.put("A", 1);

        // When
        String result = treeBidiMap.getKey(1);

        // Then
        assertEquals("A", result);
    }

    @Test
    public void testHashCode() {
        // Given
        treeBidiMap.put("A", 1);
        treeBidiMap.put("B", 2);

        // When
        int result = treeBidiMap.hashCode();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testInverseBidiMap() {
        // Given
        treeBidiMap.put("A", 1);

        // When
        OrderedBidiMap<Integer, String> inverseMap = treeBidiMap.inverseBidiMap();

        // Then
        assertNotNull(inverseMap);
    }

    @Test
    public void testIsEmpty() {
        // Given
        treeBidiMap.clear();

        // When
        boolean result = treeBidiMap.isEmpty();

        // Then
        assertTrue(result);
    }

    @Test
    public void testKeySet() {
        // Given
        treeBidiMap.put("A", 1);
        treeBidiMap.put("B", 2);

        // When
        Set<String> keySet = treeBidiMap.keySet();

        // Then
        assertEquals(2, keySet.size());
    }

    @Test
    public void testLastKey() {
        // Given
        treeBidiMap.put("A", 1);
        treeBidiMap.put("B", 2);

        // When
        String result = treeBidiMap.lastKey();

        // Then
        assertEquals("B", result);
    }

    @Test
    public void testMapIterator() {
        // Given
        treeBidiMap.put("A", 1);
        treeBidiMap.put("B", 2);

        // When
        OrderedMapIterator<String, Integer> mapIterator = treeBidiMap.mapIterator();

        // Then
        assertNotNull(mapIterator);
    }

    @Test
    public void testNextKey() {
        // Given
        treeBidiMap.put("A", 1);
        treeBidiMap.put("B", 2);

        // When
        String result = treeBidiMap.nextKey("A");

        // Then
        assertEquals("B", result);
    }

    @Test
    public void testPreviousKey() {
        // Given
        treeBidiMap.put("A", 1);
        treeBidiMap.put("B", 2);

        // When
        String result = treeBidiMap.previousKey("B");

        // Then
        assertEquals("A", result);
    }

    @Test
    public void testPut() {
        // Given
        treeBidiMap.put("A", 1);

        // When
        Integer result = treeBidiMap.put("A", 2);

        // Then
        assertEquals(1, result);
    }

    @Test
    public void testPutAll() {
        // Given
        Map<String, Integer> otherMap = new HashMap<>();
        otherMap.put("A", 1);
        otherMap.put("B", 2);

        // When
        treeBidiMap.putAll(otherMap);

        // Then
        assertEquals(2, treeBidiMap.size());
    }

    @Test
    public void testRemove() {
        // Given
        treeBidiMap.put("A", 1);

        // When
        Integer result = treeBidiMap.remove("A");

        // Then
        assertEquals(1, result);
    }

    @Test
    public void testRemoveValue() {
        // Given
        treeBidiMap.put("A", 1);

        // When
        String result = treeBidiMap.removeValue(1);

        // Then
        assertEquals("A", result);
    }

    @Test
    public void testSize() {
        // Given
        treeBidiMap.put("A", 1);
        treeBidiMap.put("B", 2);

        // When
        int result = treeBidiMap.size();

        // Then
        assertEquals(2, result);
    }

    @Test
    public void testToString() {
        // Given
        treeBidiMap.put("A", 1);
        treeBidiMap.put("B", 2);

        // When
        String result = treeBidiMap.toString();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testValues() {
        // Given
        treeBidiMap.put("A", 1);
        treeBidiMap.put("B", 2);

        // When
        Set<Integer> values = treeBidiMap.values();

        // Then
        assertEquals(2, values.size());
    }
}
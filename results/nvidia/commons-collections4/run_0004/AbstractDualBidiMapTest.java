import org.apache.commons.collections4.bidimap.AbstractDualBidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AbstractDualBidiMapTest {

    @Mock
    private Map<String, Integer> normalMap;

    @Mock
    private Map<Integer, String> reverseMap;

    private AbstractDualBidiMap<String, Integer> abstractDualBidiMap;

    @BeforeEach
    void setup() {
        abstractDualBidiMap = new DualHashBidiMap<>();
        abstractDualBidiMap.normalMap = normalMap;
        abstractDualBidiMap.reverseMap = reverseMap;
    }

    @Test
    void testClear() {
        // Given
        abstractDualBidiMap.put("key1", 1);
        abstractDualBidiMap.put("key2", 2);

        // When
        abstractDualBidiMap.clear();

        // Then
        assertTrue(abstractDualBidiMap.isEmpty());
    }

    @Test
    void testContainsKey() {
        // Given
        abstractDualBidiMap.put("key1", 1);

        // When
        boolean result = abstractDualBidiMap.containsKey("key1");

        // Then
        assertTrue(result);
    }

    @Test
    void testContainsValue() {
        // Given
        abstractDualBidiMap.put("key1", 1);

        // When
        boolean result = abstractDualBidiMap.containsValue(1);

        // Then
        assertTrue(result);
    }

    @Test
    void testEntrySet() {
        // Given
        abstractDualBidiMap.put("key1", 1);
        abstractDualBidiMap.put("key2", 2);

        // When
        Set<Map.Entry<String, Integer>> entrySet = abstractDualBidiMap.entrySet();

        // Then
        assertEquals(2, entrySet.size());
    }

    @Test
    void testEquals() {
        // Given
        AbstractDualBidiMap<String, Integer> otherMap = new DualHashBidiMap<>();
        otherMap.put("key1", 1);
        otherMap.put("key2", 2);

        abstractDualBidiMap.put("key1", 1);
        abstractDualBidiMap.put("key2", 2);

        // When
        boolean result = abstractDualBidiMap.equals(otherMap);

        // Then
        assertTrue(result);
    }

    @Test
    void testGet() {
        // Given
        abstractDualBidiMap.put("key1", 1);

        // When
        Integer result = abstractDualBidiMap.get("key1");

        // Then
        assertEquals(1, result);
    }

    @Test
    void testGetKey() {
        // Given
        abstractDualBidiMap.put("key1", 1);

        // When
        String result = abstractDualBidiMap.getKey(1);

        // Then
        assertEquals("key1", result);
    }

    @Test
    void testHashCode() {
        // Given
        abstractDualBidiMap.put("key1", 1);
        abstractDualBidiMap.put("key2", 2);

        // When
        int result = abstractDualBidiMap.hashCode();

        // Then
        assertNotNull(result);
    }

    @Test
    void testInverseBidiMap() {
        // Given
        abstractDualBidiMap.put("key1", 1);
        abstractDualBidiMap.put("key2", 2);

        // When
        BidiMap<Integer, String> inverseBidiMap = abstractDualBidiMap.inverseBidiMap();

        // Then
        assertEquals(2, inverseBidiMap.size());
    }

    @Test
    void testIsEmpty() {
        // Given

        // When
        boolean result = abstractDualBidiMap.isEmpty();

        // Then
        assertTrue(result);
    }

    @Test
    void testKeySet() {
        // Given
        abstractDualBidiMap.put("key1", 1);
        abstractDualBidiMap.put("key2", 2);

        // When
        Set<String> keySet = abstractDualBidiMap.keySet();

        // Then
        assertEquals(2, keySet.size());
    }

    @Test
    void testMapIterator() {
        // Given
        abstractDualBidiMap.put("key1", 1);
        abstractDualBidiMap.put("key2", 2);

        // When
        MapIterator<String, Integer> mapIterator = abstractDualBidiMap.mapIterator();

        // Then
        assertNotNull(mapIterator);
    }

    @Test
    void testPut() {
        // Given

        // When
        abstractDualBidiMap.put("key1", 1);

        // Then
        assertEquals(1, abstractDualBidiMap.get("key1"));
    }

    @Test
    void testPutAll() {
        // Given
        Map<String, Integer> otherMap = new HashMap<>();
        otherMap.put("key1", 1);
        otherMap.put("key2", 2);

        // When
        abstractDualBidiMap.putAll(otherMap);

        // Then
        assertEquals(2, abstractDualBidiMap.size());
    }

    @Test
    void testRemove() {
        // Given
        abstractDualBidiMap.put("key1", 1);

        // When
        abstractDualBidiMap.remove("key1");

        // Then
        assertTrue(abstractDualBidiMap.isEmpty());
    }

    @Test
    void testRemoveValue() {
        // Given
        abstractDualBidiMap.put("key1", 1);

        // When
        abstractDualBidiMap.removeValue(1);

        // Then
        assertTrue(abstractDualBidiMap.isEmpty());
    }

    @Test
    void testSize() {
        // Given
        abstractDualBidiMap.put("key1", 1);
        abstractDualBidiMap.put("key2", 2);

        // When
        int result = abstractDualBidiMap.size();

        // Then
        assertEquals(2, result);
    }

    @Test
    void testToString() {
        // Given
        abstractDualBidiMap.put("key1", 1);
        abstractDualBidiMap.put("key2", 2);

        // When
        String result = abstractDualBidiMap.toString();

        // Then
        assertNotNull(result);
    }

    @Test
    void testValues() {
        // Given
        abstractDualBidiMap.put("key1", 1);
        abstractDualBidiMap.put("key2", 2);

        // When
        Set<Integer> values = abstractDualBidiMap.values();

        // Then
        assertEquals(2, values.size());
    }
}
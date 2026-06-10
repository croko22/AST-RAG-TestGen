import org.apache.commons.collections4.iterators.EmptyIterator;
import org.apache.commons.collections4.iterators.EmptyMapIterator;
import org.apache.commons.collections4.map.AbstractHashedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AbstractHashedMapTest {

    @Mock
    private AbstractHashedMap<String, String> map;

    @BeforeEach
    public void setup() {
        map = new AbstractHashedMap<>();
    }

    @Test
    public void testClear() {
        // Given
        map.put("key1", "value1");
        map.put("key2", "value2");

        // When
        map.clear();

        // Then
        assertTrue(map.isEmpty());
    }

    @Test
    public void testContainsKey() {
        // Given
        map.put("key1", "value1");

        // When
        boolean result = map.containsKey("key1");

        // Then
        assertTrue(result);
    }

    @Test
    public void testContainsValue() {
        // Given
        map.put("key1", "value1");

        // When
        boolean result = map.containsValue("value1");

        // Then
        assertTrue(result);
    }

    @Test
    public void testEntrySet() {
        // Given
        map.put("key1", "value1");
        map.put("key2", "value2");

        // When
        Set<Map.Entry<String, String>> entrySet = map.entrySet();

        // Then
        assertEquals(2, entrySet.size());
    }

    @Test
    public void testEquals() {
        // Given
        map.put("key1", "value1");
        AbstractHashedMap<String, String> otherMap = new AbstractHashedMap<>();
        otherMap.put("key1", "value1");

        // When
        boolean result = map.equals(otherMap);

        // Then
        assertTrue(result);
    }

    @Test
    public void testGet() {
        // Given
        map.put("key1", "value1");

        // When
        String result = map.get("key1");

        // Then
        assertEquals("value1", result);
    }

    @Test
    public void testHashCode() {
        // Given
        map.put("key1", "value1");

        // When
        int result = map.hashCode();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testIsEmpty() {
        // Given

        // When
        boolean result = map.isEmpty();

        // Then
        assertTrue(result);
    }

    @Test
    public void testKeySet() {
        // Given
        map.put("key1", "value1");
        map.put("key2", "value2");

        // When
        Set<String> keySet = map.keySet();

        // Then
        assertEquals(2, keySet.size());
    }

    @Test
    public void testMapIterator() {
        // Given
        map.put("key1", "value1");
        map.put("key2", "value2");

        // When
        MapIterator<String, String> iterator = map.mapIterator();

        // Then
        assertNotNull(iterator);
    }

    @Test
    public void testPut() {
        // Given

        // When
        map.put("key1", "value1");

        // Then
        assertEquals("value1", map.get("key1"));
    }

    @Test
    public void testPutAll() {
        // Given
        Map<String, String> otherMap = new HashMap<>();
        otherMap.put("key1", "value1");
        otherMap.put("key2", "value2");

        // When
        map.putAll(otherMap);

        // Then
        assertEquals(2, map.size());
    }

    @Test
    public void testRemove() {
        // Given
        map.put("key1", "value1");

        // When
        map.remove("key1");

        // Then
        assertTrue(map.isEmpty());
    }

    @Test
    public void testSize() {
        // Given
        map.put("key1", "value1");
        map.put("key2", "value2");

        // When
        int result = map.size();

        // Then
        assertEquals(2, result);
    }

    @Test
    public void testToString() {
        // Given
        map.put("key1", "value1");

        // When
        String result = map.toString();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testValues() {
        // Given
        map.put("key1", "value1");
        map.put("key2", "value2");

        // When
        Collection<String> values = map.values();

        // Then
        assertEquals(2, values.size());
    }
}
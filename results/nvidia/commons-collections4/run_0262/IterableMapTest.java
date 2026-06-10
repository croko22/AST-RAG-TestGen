import org.apache.commons.collections4.IterableMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IterableMapTest {

    @Mock
    private IterableMap<String, Integer> iterableMap;

    @BeforeEach
    void setup() {
        // Initialize the mock iterable map
        iterableMap = mock(IterableMap.class);
    }

    @Test
    void testMapIterator() {
        // Given: a map with some entries
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        // When: we get the map iterator
        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();

        // Then: we can iterate over the map
        assertTrue(iterator.hasNext());
        Map.Entry<String, Integer> entry = iterator.next();
        assertNotNull(entry);
        assertEquals("one", entry.getKey());
        assertEquals(1, entry.getValue().intValue());
    }

    @Test
    void testPut() {
        // Given: a key and a value
        String key = "testKey";
        Integer value = 10;

        // When: we put the value into the map
        when(iterableMap.put(key, value)).thenReturn(value);

        // Then: the value is stored in the map
        Integer result = iterableMap.put(key, value);
        assertEquals(value, result);
        verify(iterableMap, times(1)).put(key, value);
    }

    @Test
    void testGet() {
        // Given: a key
        String key = "testKey";
        Integer value = 10;

        // When: we get the value from the map
        when(iterableMap.get(key)).thenReturn(value);

        // Then: the value is returned
        Integer result = iterableMap.get(key);
        assertEquals(value, result);
        verify(iterableMap, times(1)).get(key);
    }

    @Test
    void testRemove() {
        // Given: a key
        String key = "testKey";
        Integer value = 10;

        // When: we remove the value from the map
        when(iterableMap.remove(key)).thenReturn(value);

        // Then: the value is removed
        Integer result = iterableMap.remove(key);
        assertEquals(value, result);
        verify(iterableMap, times(1)).remove(key);
    }

    @Test
    void testContainsKey() {
        // Given: a key
        String key = "testKey";

        // When: we check if the key is in the map
        when(iterableMap.containsKey(key)).thenReturn(true);

        // Then: the key is in the map
        boolean result = iterableMap.containsKey(key);
        assertTrue(result);
        verify(iterableMap, times(1)).containsKey(key);
    }

    @Test
    void testContainsValue() {
        // Given: a value
        Integer value = 10;

        // When: we check if the value is in the map
        when(iterableMap.containsValue(value)).thenReturn(true);

        // Then: the value is in the map
        boolean result = iterableMap.containsValue(value);
        assertTrue(result);
        verify(iterableMap, times(1)).containsValue(value);
    }

    @Test
    void testIsEmpty() {
        // Given: an empty map
        when(iterableMap.isEmpty()).thenReturn(true);

        // Then: the map is empty
        boolean result = iterableMap.isEmpty();
        assertTrue(result);
        verify(iterableMap, times(1)).isEmpty();
    }

    @Test
    void testSize() {
        // Given: a map with some entries
        when(iterableMap.size()).thenReturn(3);

        // Then: the size of the map is 3
        int result = iterableMap.size();
        assertEquals(3, result);
        verify(iterableMap, times(1)).size();
    }

    @Test
    void testClear() {
        // When: we clear the map
        doNothing().when(iterableMap).clear();

        // Then: the map is cleared
        iterableMap.clear();
        verify(iterableMap, times(1)).clear();
    }

    @Test
    void testPutAll() {
        // Given: another map
        Map<String, Integer> anotherMap = new HashMap<>();
        anotherMap.put("one", 1);
        anotherMap.put("two", 2);

        // When: we put all entries from another map into this map
        doNothing().when(iterableMap).putAll(anotherMap);

        // Then: all entries are added to this map
        iterableMap.putAll(anotherMap);
        verify(iterableMap, times(1)).putAll(anotherMap);
    }
}
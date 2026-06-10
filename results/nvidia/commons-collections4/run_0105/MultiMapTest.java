import org.apache.commons.collections4.MultiMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MultiMapTest {

    @Mock
    private Map<Object, Collection<Object>> map;

    @InjectMocks
    private MultiMap<String, String> multiMap;

    @BeforeEach
    void setup() {
        // Initialize the map with some values
        map = new HashMap<>();
        map.put("key1", Collection.of("value1", "value2"));
        map.put("key2", Collection.of("value3", "value4"));
    }

    @Test
    void testContainsValue() {
        // Given: a value that exists in the map
        Object value = "value1";

        // When: containsValue is called
        boolean result = multiMap.containsValue(value);

        // Then: the result should be true
        assertTrue(result);
    }

    @Test
    void testContainsValue_NotFound() {
        // Given: a value that does not exist in the map
        Object value = "non-existent-value";

        // When: containsValue is called
        boolean result = multiMap.containsValue(value);

        // Then: the result should be false
        assertFalse(result);
    }

    @Test
    void testGet() {
        // Given: a key that exists in the map
        Object key = "key1";

        // When: get is called
        Object result = multiMap.get(key);

        // Then: the result should be a collection of values
        assertNotNull(result);
        assertTrue(result instanceof Collection);
        assertEquals(2, ((Collection<?>) result).size());
    }

    @Test
    void testGet_NotFound() {
        // Given: a key that does not exist in the map
        Object key = "non-existent-key";

        // When: get is called
        Object result = multiMap.get(key);

        // Then: the result should be null or an empty collection
        assertNull(result);
    }

    @Test
    void testPut() {
        // Given: a key and a value
        String key = "new-key";
        Object value = "new-value";

        // When: put is called
        Object result = multiMap.put(key, value);

        // Then: the result should be the value added
        assertEquals(value, result);
        verify(map, times(1)).put(key, Collection.of(value));
    }

    @Test
    void testRemove() {
        // Given: a key that exists in the map
        Object key = "key1";

        // When: remove is called
        Object result = multiMap.remove(key);

        // Then: the result should be the collection of values removed
        assertNotNull(result);
        assertTrue(result instanceof Collection);
        assertEquals(2, ((Collection<?>) result).size());
        verify(map, times(1)).remove(key);
    }

    @Test
    void testRemove_NotFound() {
        // Given: a key that does not exist in the map
        Object key = "non-existent-key";

        // When: remove is called
        Object result = multiMap.remove(key);

        // Then: the result should be null or an empty collection
        assertNull(result);
    }

    @Test
    void testRemoveMapping() {
        // Given: a key and a value that exist in the map
        String key = "key1";
        String item = "value1";

        // When: removeMapping is called
        boolean result = multiMap.removeMapping(key, item);

        // Then: the result should be true
        assertTrue(result);
        verify(map, times(1)).get(key);
    }

    @Test
    void testRemoveMapping_NotFound() {
        // Given: a key and a value that do not exist in the map
        String key = "key1";
        String item = "non-existent-value";

        // When: removeMapping is called
        boolean result = multiMap.removeMapping(key, item);

        // Then: the result should be false
        assertFalse(result);
    }

    @Test
    void testSize() {
        // When: size is called
        int result = multiMap.size();

        // Then: the result should be the number of key-collection mappings
        assertEquals(2, result);
    }

    @Test
    void testValues() {
        // When: values is called
        Collection<Object> result = multiMap.values();

        // Then: the result should be a collection view of the values
        assertNotNull(result);
        assertEquals(4, result.size());
    }
}
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.LinkedHashSetValuedLinkedHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LinkedHashSetValuedLinkedHashMapTest {

    private LinkedHashSetValuedLinkedHashMap<String, String> linkedHashSetValuedLinkedHashMap;

    @BeforeEach
    void setup() {
        linkedHashSetValuedLinkedHashMap = new LinkedHashSetValuedLinkedHashMap<>();
    }

    @Test
    void testPut() {
        // Given
        String key = "key";
        String value = "value";

        // When
        boolean result = linkedHashSetValuedLinkedHashMap.put(key, value);

        // Then
        assertTrue(result);
        assertEquals(1, linkedHashSetValuedLinkedHashMap.size());
    }

    @Test
    void testPutAll() {
        // Given
        String key = "key";
        List<String> values = Arrays.asList("value1", "value2", "value3");

        // When
        boolean result = linkedHashSetValuedLinkedHashMap.putAll(key, values);

        // Then
        assertTrue(result);
        assertEquals(1, linkedHashSetValuedLinkedHashMap.size());
        assertEquals(3, linkedHashSetValuedLinkedHashMap.get(key).size());
    }

    @Test
    void testPutAllMap() {
        // Given
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        // When
        boolean result = linkedHashSetValuedLinkedHashMap.putAll(map);

        // Then
        assertTrue(result);
        assertEquals(2, linkedHashSetValuedLinkedHashMap.size());
    }

    @Test
    void testPutAllMultiValuedMap() {
        // Given
        MultiValuedMap<String, String> multiValuedMap = new LinkedHashSetValuedLinkedHashMap<>();
        multiValuedMap.put("key1", "value1");
        multiValuedMap.put("key1", "value2");
        multiValuedMap.put("key2", "value3");

        // When
        boolean result = linkedHashSetValuedLinkedHashMap.putAll(multiValuedMap);

        // Then
        assertTrue(result);
        assertEquals(2, linkedHashSetValuedLinkedHashMap.size());
        assertEquals(2, linkedHashSetValuedLinkedHashMap.get("key1").size());
        assertEquals(1, linkedHashSetValuedLinkedHashMap.get("key2").size());
    }

    @Test
    void testGet() {
        // Given
        String key = "key";
        String value = "value";
        linkedHashSetValuedLinkedHashMap.put(key, value);

        // When
        Collection<String> result = linkedHashSetValuedLinkedHashMap.get(key);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(value));
    }

    @Test
    void testRemove() {
        // Given
        String key = "key";
        String value = "value";
        linkedHashSetValuedLinkedHashMap.put(key, value);

        // When
        Collection<String> result = linkedHashSetValuedLinkedHashMap.remove(key);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(value));
        assertTrue(linkedHashSetValuedLinkedHashMap.isEmpty());
    }

    @Test
    void testRemoveMapping() {
        // Given
        String key = "key";
        String value = "value";
        linkedHashSetValuedLinkedHashMap.put(key, value);

        // When
        boolean result = linkedHashSetValuedLinkedHashMap.removeMapping(key, value);

        // Then
        assertTrue(result);
        assertTrue(linkedHashSetValuedLinkedHashMap.isEmpty());
    }

    @Test
    void testClear() {
        // Given
        String key = "key";
        String value = "value";
        linkedHashSetValuedLinkedHashMap.put(key, value);

        // When
        linkedHashSetValuedLinkedHashMap.clear();

        // Then
        assertTrue(linkedHashSetValuedLinkedHashMap.isEmpty());
    }

    @Test
    void testContainsKey() {
        // Given
        String key = "key";
        String value = "value";
        linkedHashSetValuedLinkedHashMap.put(key, value);

        // When
        boolean result = linkedHashSetValuedLinkedHashMap.containsKey(key);

        // Then
        assertTrue(result);
    }

    @Test
    void testContainsMapping() {
        // Given
        String key = "key";
        String value = "value";
        linkedHashSetValuedLinkedHashMap.put(key, value);

        // When
        boolean result = linkedHashSetValuedLinkedHashMap.containsMapping(key, value);

        // Then
        assertTrue(result);
    }

    @Test
    void testContainsValue() {
        // Given
        String key = "key";
        String value = "value";
        linkedHashSetValuedLinkedHashMap.put(key, value);

        // When
        boolean result = linkedHashSetValuedLinkedHashMap.containsValue(value);

        // Then
        assertTrue(result);
    }

    @Test
    void testIsEmpty() {
        // Given

        // When
        boolean result = linkedHashSetValuedLinkedHashMap.isEmpty();

        // Then
        assertTrue(result);
    }

    @Test
    void testSize() {
        // Given
        String key = "key";
        String value = "value";
        linkedHashSetValuedLinkedHashMap.put(key, value);

        // When
        int result = linkedHashSetValuedLinkedHashMap.size();

        // Then
        assertEquals(1, result);
    }

    @Test
    void testKeys() {
        // Given
        String key = "key";
        String value = "value";
        linkedHashSetValuedLinkedHashMap.put(key, value);

        // When
        MultiSet<String> result = linkedHashSetValuedLinkedHashMap.keys();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(key));
    }

    @Test
    void testKeySet() {
        // Given
        String key = "key";
        String value = "value";
        linkedHashSetValuedLinkedHashMap.put(key, value);

        // When
        Set<String> result = linkedHashSetValuedLinkedHashMap.keySet();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(key));
    }

    @Test
    void testMapIterator() {
        // Given
        String key = "key";
        String value = "value";
        linkedHashSetValuedLinkedHashMap.put(key, value);

        // When
        MapIterator<String, String> result = linkedHashSetValuedLinkedHashMap.mapIterator();

        // Then
        assertNotNull(result);
        assertTrue(result.hasNext());
        Map.Entry<String, String> entry = result.next();
        assertEquals(key, entry.getKey());
        assertEquals(value, entry.getValue());
    }

    @Test
    void testValues() {
        // Given
        String key = "key";
        String value = "value";
        linkedHashSetValuedLinkedHashMap.put(key, value);

        // When
        Collection<String> result = linkedHashSetValuedLinkedHashMap.values();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(value));
    }

    @Test
    void testEntries() {
        // Given
        String key = "key";
        String value = "value";
        linkedHashSetValuedLinkedHashMap.put(key, value);

        // When
        Collection<Map.Entry<String, String>> result = linkedHashSetValuedLinkedHashMap.entries();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        Map.Entry<String, String> entry = result.iterator().next();
        assertEquals(key, entry.getKey());
        assertEquals(value, entry.getValue());
    }

    @Test
    void testAsMap() {
        // Given
        String key = "key";
        String value = "value";
        linkedHashSetValuedLinkedHashMap.put(key, value);

        // When
        Map<String, Collection<String>> result = linkedHashSetValuedLinkedHashMap.asMap();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        Collection<String> values = result.get(key);
        assertNotNull(values);
        assertEquals(1, values.size());
        assertTrue(values.contains(value));
    }
}
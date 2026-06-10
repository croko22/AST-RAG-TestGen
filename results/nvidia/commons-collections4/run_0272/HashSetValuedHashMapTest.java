import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HashSetValuedHashMapTest {

    @Mock
    private Map<String, String> map;

    private HashSetValuedHashMap<String, String> hashSetValuedHashMap;

    @BeforeEach
    void setup() {
        hashSetValuedHashMap = new HashSetValuedHashMap<>();
    }

    @Test
    void testConstructor() {
        // Given
        int initialMapCapacity = 10;
        int initialSetCapacity = 5;

        // When
        HashSetValuedHashMap<String, String> hashSetValuedHashMap = new HashSetValuedHashMap<>(initialMapCapacity, initialSetCapacity);

        // Then
        assertNotNull(hashSetValuedHashMap);
    }

    @Test
    void testPut() {
        // Given
        String key = "key";
        String value = "value";

        // When
        boolean result = hashSetValuedHashMap.put(key, value);

        // Then
        assertTrue(result);
        assertTrue(hashSetValuedHashMap.containsKey(key));
        assertTrue(hashSetValuedHashMap.containsMapping(key, value));
    }

    @Test
    void testPutAll() {
        // Given
        String key = "key";
        String value1 = "value1";
        String value2 = "value2";

        // When
        hashSetValuedHashMap.put(key, value1);
        boolean result = hashSetValuedHashMap.putAll(key, new HashSet<>(java.util.Arrays.asList(value2)));

        // Then
        assertTrue(result);
        assertTrue(hashSetValuedHashMap.containsKey(key));
        assertTrue(hashSetValuedHashMap.containsMapping(key, value1));
        assertTrue(hashSetValuedHashMap.containsMapping(key, value2));
    }

    @Test
    void testGet() {
        // Given
        String key = "key";
        String value = "value";
        hashSetValuedHashMap.put(key, value);

        // When
        java.util.Collection<String> result = hashSetValuedHashMap.get(key);

        // Then
        assertNotNull(result);
        assertTrue(result.contains(value));
    }

    @Test
    void testRemove() {
        // Given
        String key = "key";
        String value = "value";
        hashSetValuedHashMap.put(key, value);

        // When
        java.util.Collection<String> result = hashSetValuedHashMap.remove(key);

        // Then
        assertNotNull(result);
        assertTrue(result.contains(value));
        assertFalse(hashSetValuedHashMap.containsKey(key));
    }

    @Test
    void testRemoveMapping() {
        // Given
        String key = "key";
        String value = "value";
        hashSetValuedHashMap.put(key, value);

        // When
        boolean result = hashSetValuedHashMap.removeMapping(key, value);

        // Then
        assertTrue(result);
        assertFalse(hashSetValuedHashMap.containsMapping(key, value));
    }

    @Test
    void testClear() {
        // Given
        String key = "key";
        String value = "value";
        hashSetValuedHashMap.put(key, value);

        // When
        hashSetValuedHashMap.clear();

        // Then
        assertFalse(hashSetValuedHashMap.containsKey(key));
    }

    @Test
    void testContainsKey() {
        // Given
        String key = "key";
        String value = "value";
        hashSetValuedHashMap.put(key, value);

        // When
        boolean result = hashSetValuedHashMap.containsKey(key);

        // Then
        assertTrue(result);
    }

    @Test
    void testContainsMapping() {
        // Given
        String key = "key";
        String value = "value";
        hashSetValuedHashMap.put(key, value);

        // When
        boolean result = hashSetValuedHashMap.containsMapping(key, value);

        // Then
        assertTrue(result);
    }

    @Test
    void testContainsValue() {
        // Given
        String key = "key";
        String value = "value";
        hashSetValuedHashMap.put(key, value);

        // When
        boolean result = hashSetValuedHashMap.containsValue(value);

        // Then
        assertTrue(result);
    }

    @Test
    void testIsEmpty() {
        // Given

        // When
        boolean result = hashSetValuedHashMap.isEmpty();

        // Then
        assertTrue(result);
    }

    @Test
    void testSize() {
        // Given
        String key = "key";
        String value = "value";
        hashSetValuedHashMap.put(key, value);

        // When
        int result = hashSetValuedHashMap.size();

        // Then
        assertEquals(1, result);
    }

    @Test
    void testKeys() {
        // Given
        String key = "key";
        String value = "value";
        hashSetValuedHashMap.put(key, value);

        // When
        MultiValuedMap<String, String>.MultiSet<String> result = hashSetValuedHashMap.keys();

        // Then
        assertNotNull(result);
        assertTrue(result.contains(key));
    }

    @Test
    void testKeySet() {
        // Given
        String key = "key";
        String value = "value";
        hashSetValuedHashMap.put(key, value);

        // When
        java.util.Set<String> result = hashSetValuedHashMap.keySet();

        // Then
        assertNotNull(result);
        assertTrue(result.contains(key));
    }

    @Test
    void testMapIterator() {
        // Given
        String key = "key";
        String value = "value";
        hashSetValuedHashMap.put(key, value);

        // When
        MultiValuedMap<String, String>.MapIterator<String, String> result = hashSetValuedHashMap.mapIterator();

        // Then
        assertNotNull(result);
    }

    @Test
    void testValues() {
        // Given
        String key = "key";
        String value = "value";
        hashSetValuedHashMap.put(key, value);

        // When
        java.util.Collection<String> result = hashSetValuedHashMap.values();

        // Then
        assertNotNull(result);
        assertTrue(result.contains(value));
    }

    @Test
    void testAsMap() {
        // Given
        String key = "key";
        String value = "value";
        hashSetValuedHashMap.put(key, value);

        // When
        java.util.Map<String, java.util.Collection<String>> result = ((MultiValuedMap<String, String>) hashSetValuedHashMap).asMap();

        // Then
        assertNotNull(result);
        assertTrue(result.containsKey(key));
    }
}
import org.apache.commons.collections4.MultiValuedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MultiValuedMapTest {

    @Mock
    private MultiValuedMap<String, String> multiValuedMap;

    @BeforeEach
    void setup() {
        // Initialize the mock object
        multiValuedMap = mock(MultiValuedMap.class);
    }

    @Test
    void testAsMap() {
        // Given
        Map<String, Collection<String>> map = new HashMap<>();
        when(multiValuedMap.asMap()).thenReturn(map);

        // When
        Map<String, Collection<String>> result = multiValuedMap.asMap();

        // Then
        assertEquals(map, result);
        verify(multiValuedMap, times(1)).asMap();
    }

    @Test
    void testClear() {
        // Given
        doNothing().when(multiValuedMap).clear();

        // When
        multiValuedMap.clear();

        // Then
        verify(multiValuedMap, times(1)).clear();
    }

    @Test
    void testContainsKey() {
        // Given
        when(multiValuedMap.containsKey(any())).thenReturn(true);

        // When
        boolean result = multiValuedMap.containsKey("key");

        // Then
        assertTrue(result);
        verify(multiValuedMap, times(1)).containsKey("key");
    }

    @Test
    void testContainsMapping() {
        // Given
        when(multiValuedMap.containsMapping(any(), any())).thenReturn(true);

        // When
        boolean result = multiValuedMap.containsMapping("key", "value");

        // Then
        assertTrue(result);
        verify(multiValuedMap, times(1)).containsMapping("key", "value");
    }

    @Test
    void testContainsValue() {
        // Given
        when(multiValuedMap.containsValue(any())).thenReturn(true);

        // When
        boolean result = multiValuedMap.containsValue("value");

        // Then
        assertTrue(result);
        verify(multiValuedMap, times(1)).containsValue("value");
    }

    @Test
    void testEntries() {
        // Given
        Collection<Map.Entry<String, String>> entries = new ArrayList<>();
        when(multiValuedMap.entries()).thenReturn(entries);

        // When
        Collection<Map.Entry<String, String>> result = multiValuedMap.entries();

        // Then
        assertEquals(entries, result);
        verify(multiValuedMap, times(1)).entries();
    }

    @Test
    void testGet() {
        // Given
        Collection<String> values = new ArrayList<>();
        when(multiValuedMap.get(any())).thenReturn(values);

        // When
        Collection<String> result = multiValuedMap.get("key");

        // Then
        assertEquals(values, result);
        verify(multiValuedMap, times(1)).get("key");
    }

    @Test
    void testIsEmpty() {
        // Given
        when(multiValuedMap.isEmpty()).thenReturn(true);

        // When
        boolean result = multiValuedMap.isEmpty();

        // Then
        assertTrue(result);
        verify(multiValuedMap, times(1)).isEmpty();
    }

    @Test
    void testKeys() {
        // Given
        org.apache.commons.collections4.MultiSet<String> keys = mock(org.apache.commons.collections4.MultiSet.class);
        when(multiValuedMap.keys()).thenReturn(keys);

        // When
        org.apache.commons.collections4.MultiSet<String> result = multiValuedMap.keys();

        // Then
        assertEquals(keys, result);
        verify(multiValuedMap, times(1)).keys();
    }

    @Test
    void testKeySet() {
        // Given
        java.util.Set<String> keySet = new java.util.HashSet<>();
        when(multiValuedMap.keySet()).thenReturn(keySet);

        // When
        java.util.Set<String> result = multiValuedMap.keySet();

        // Then
        assertEquals(keySet, result);
        verify(multiValuedMap, times(1)).keySet();
    }

    @Test
    void testMapIterator() {
        // Given
        org.apache.commons.collections4.MapIterator<String, String> mapIterator = mock(org.apache.commons.collections4.MapIterator.class);
        when(multiValuedMap.mapIterator()).thenReturn(mapIterator);

        // When
        org.apache.commons.collections4.MapIterator<String, String> result = multiValuedMap.mapIterator();

        // Then
        assertEquals(mapIterator, result);
        verify(multiValuedMap, times(1)).mapIterator();
    }

    @Test
    void testPut() {
        // Given
        doNothing().when(multiValuedMap).put(any(), any());

        // When
        multiValuedMap.put("key", "value");

        // Then
        verify(multiValuedMap, times(1)).put("key", "value");
    }

    @Test
    void testPutAll() {
        // Given
        doNothing().when(multiValuedMap).putAll(any(), any());

        // When
        multiValuedMap.putAll("key", java.util.Arrays.asList("value1", "value2"));

        // Then
        verify(multiValuedMap, times(1)).putAll("key", java.util.Arrays.asList("value1", "value2"));
    }

    @Test
    void testPutAllMap() {
        // Given
        doNothing().when(multiValuedMap).putAll(any());

        // When
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        multiValuedMap.putAll(map);

        // Then
        verify(multiValuedMap, times(1)).putAll(map);
    }

    @Test
    void testRemove() {
        // Given
        Collection<String> values = new ArrayList<>();
        when(multiValuedMap.remove(any())).thenReturn(values);

        // When
        Collection<String> result = multiValuedMap.remove("key");

        // Then
        assertEquals(values, result);
        verify(multiValuedMap, times(1)).remove("key");
    }

    @Test
    void testRemoveMapping() {
        // Given
        when(multiValuedMap.removeMapping(any(), any())).thenReturn(true);

        // When
        boolean result = multiValuedMap.removeMapping("key", "value");

        // Then
        assertTrue(result);
        verify(multiValuedMap, times(1)).removeMapping("key", "value");
    }

    @Test
    void testSize() {
        // Given
        when(multiValuedMap.size()).thenReturn(10);

        // When
        int result = multiValuedMap.size();

        // Then
        assertEquals(10, result);
        verify(multiValuedMap, times(1)).size();
    }

    @Test
    void testValues() {
        // Given
        Collection<String> values = new ArrayList<>();
        when(multiValuedMap.values()).thenReturn(values);

        // When
        Collection<String> result = multiValuedMap.values();

        // Then
        assertEquals(values, result);
        verify(multiValuedMap, times(1)).values();
    }
}
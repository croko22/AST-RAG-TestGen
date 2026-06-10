import org.apache.commons.collections4.map.MultiValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MultiValueMapTest {

    @Mock
    private Map<String, Collection<String>> map;

    private MultiValueMap<String, String> multiValueMap;

    @BeforeEach
    void setup() {
        multiValueMap = new MultiValueMap<>(map, new ArrayList<>());
    }

    @Test
    void testClear() {
        // Given
        when(map.clear()).thenReturn(null);

        // When
        multiValueMap.clear();

        // Then
        verify(map, times(1)).clear();
    }

    @Test
    void testContainsValue() {
        // Given
        when(map.containsValue(any())).thenReturn(true);

        // When
        boolean result = multiValueMap.containsValue("value");

        // Then
        assertTrue(result);
        verify(map, times(1)).containsValue(any());
    }

    @Test
    void testContainsValueWithKey() {
        // Given
        when(map.get(any())).thenReturn(new ArrayList<>(Arrays.asList("value1", "value2")));

        // When
        boolean result = multiValueMap.containsValue("key", "value1");

        // Then
        assertTrue(result);
        verify(map, times(1)).get(any());
    }

    @Test
    void testEntrySet() {
        // Given
        when(map.entrySet()).thenReturn(new HashSet<>());

        // When
        Set<Map.Entry<String, Object>> result = multiValueMap.entrySet();

        // Then
        assertNotNull(result);
        verify(map, times(1)).entrySet();
    }

    @Test
    void testGetCollection() {
        // Given
        when(map.get(any())).thenReturn(new ArrayList<>(Arrays.asList("value1", "value2")));

        // When
        Collection<String> result = multiValueMap.getCollection("key");

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(map, times(1)).get(any());
    }

    @Test
    void testIterator() {
        // Given
        when(map.keySet()).thenReturn(new HashSet<>(Arrays.asList("key1", "key2")));

        // When
        Iterator<Map.Entry<String, String>> result = multiValueMap.iterator();

        // Then
        assertNotNull(result);
        verify(map, times(1)).keySet();
    }

    @Test
    void testIteratorWithKey() {
        // Given
        when(map.get(any())).thenReturn(new ArrayList<>(Arrays.asList("value1", "value2")));

        // When
        Iterator<String> result = multiValueMap.iterator("key");

        // Then
        assertNotNull(result);
        verify(map, times(1)).get(any());
    }

    @Test
    void testPut() {
        // Given
        when(map.put(any(), any())).thenReturn(null);

        // When
        Object result = multiValueMap.put("key", "value");

        // Then
        assertNull(result);
        verify(map, times(1)).put(any(), any());
    }

    @Test
    void testPutAll() {
        // Given
        when(map.put(any(), any())).thenReturn(null);

        // When
        boolean result = multiValueMap.putAll("key", new ArrayList<>(Arrays.asList("value1", "value2")));

        // Then
        assertTrue(result);
        verify(map, times(1)).put(any(), any());
    }

    @Test
    void testPutAllMap() {
        // Given
        when(map.putAll(any())).thenReturn(null);

        // When
        multiValueMap.putAll(new HashMap<>());

        // Then
        verify(map, times(1)).putAll(any());
    }

    @Test
    void testRemoveMapping() {
        // Given
        when(map.get(any())).thenReturn(new ArrayList<>(Arrays.asList("value1", "value2")));

        // When
        boolean result = multiValueMap.removeMapping("key", "value1");

        // Then
        assertTrue(result);
        verify(map, times(1)).get(any());
    }

    @Test
    void testSize() {
        // Given
        when(map.get(any())).thenReturn(new ArrayList<>(Arrays.asList("value1", "value2")));

        // When
        int result = multiValueMap.size("key");

        // Then
        assertEquals(2, result);
        verify(map, times(1)).get(any());
    }

    @Test
    void testTotalSize() {
        // Given
        when(map.values()).thenReturn(new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("value1", "value2")),
                new ArrayList<>(Arrays.asList("value3", "value4"))
        )));

        // When
        int result = multiValueMap.totalSize();

        // Then
        assertEquals(4, result);
        verify(map, times(1)).values();
    }

    @Test
    void testValues() {
        // Given
        when(map.values()).thenReturn(new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("value1", "value2")),
                new ArrayList<>(Arrays.asList("value3", "value4"))
        )));

        // When
        Collection<Object> result = multiValueMap.values();

        // Then
        assertNotNull(result);
        assertEquals(4, result.size());
        verify(map, times(1)).values();
    }
}
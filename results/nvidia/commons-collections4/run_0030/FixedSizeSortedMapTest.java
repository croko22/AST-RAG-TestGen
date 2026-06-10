import org.apache.commons.collections4.map.FixedSizeSortedMap;
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
public class FixedSizeSortedMapTest {

    @Mock
    private SortedMap<String, String> sortedMap;

    private FixedSizeSortedMap<String, String> fixedSizeSortedMap;

    @BeforeEach
    void setup() {
        fixedSizeSortedMap = FixedSizeSortedMap.fixedSizeSortedMap(sortedMap);
    }

    @Test
    void testFixedSizeSortedMapCreation() {
        // Given
        SortedMap<String, String> map = new TreeMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        // When
        FixedSizeSortedMap<String, String> fixedSizeSortedMap = FixedSizeSortedMap.fixedSizeSortedMap(map);

        // Then
        assertNotNull(fixedSizeSortedMap);
        assertEquals(2, fixedSizeSortedMap.size());
    }

    @Test
    void testClear() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> fixedSizeSortedMap.clear());
    }

    @Test
    void testEntrySet() {
        // Given
        Set<Map.Entry<String, String>> entrySet = new HashSet<>();
        entrySet.add(new AbstractMap.SimpleEntry<>("key1", "value1"));
        entrySet.add(new AbstractMap.SimpleEntry<>("key2", "value2"));
        when(sortedMap.entrySet()).thenReturn(entrySet);

        // When
        Set<Map.Entry<String, String>> result = fixedSizeSortedMap.entrySet();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(sortedMap, times(1)).entrySet();
    }

    @Test
    void testHeadMap() {
        // Given
        SortedMap<String, String> headMap = new TreeMap<>();
        headMap.put("key1", "value1");
        when(sortedMap.headMap(any())).thenReturn(headMap);

        // When
        SortedMap<String, String> result = fixedSizeSortedMap.headMap("key2");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sortedMap, times(1)).headMap(any());
    }

    @Test
    void testIsFull() {
        // When
        boolean result = fixedSizeSortedMap.isFull();

        // Then
        assertTrue(result);
    }

    @Test
    void testKeySet() {
        // Given
        Set<String> keySet = new HashSet<>();
        keySet.add("key1");
        keySet.add("key2");
        when(sortedMap.keySet()).thenReturn(keySet);

        // When
        Set<String> result = fixedSizeSortedMap.keySet();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(sortedMap, times(1)).keySet();
    }

    @Test
    void testMaxSize() {
        // Given
        when(sortedMap.size()).thenReturn(2);

        // When
        int result = fixedSizeSortedMap.maxSize();

        // Then
        assertEquals(2, result);
        verify(sortedMap, times(1)).size();
    }

    @Test
    void testPutExistingKey() {
        // Given
        when(sortedMap.containsKey(any())).thenReturn(true);
        when(sortedMap.put(any(), any())).thenReturn("value1");

        // When
        String result = fixedSizeSortedMap.put("key1", "newValue1");

        // Then
        assertNotNull(result);
        assertEquals("newValue1", result);
        verify(sortedMap, times(1)).containsKey(any());
        verify(sortedMap, times(1)).put(any(), any());
    }

    @Test
    void testPutNewKey() {
        // Given
        when(sortedMap.containsKey(any())).thenReturn(false);

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> fixedSizeSortedMap.put("key3", "value3"));
        verify(sortedMap, times(1)).containsKey(any());
    }

    @Test
    void testPutAll() {
        // Given
        Map<String, String> mapToCopy = new HashMap<>();
        mapToCopy.put("key1", "newValue1");
        mapToCopy.put("key2", "newValue2");
        when(sortedMap.containsKey(any())).thenReturn(true);
        when(sortedMap.putAll(any())).thenReturn(null);

        // When
        fixedSizeSortedMap.putAll(mapToCopy);

        // Then
        verify(sortedMap, times(1)).putAll(any());
    }

    @Test
    void testPutAllNewKey() {
        // Given
        Map<String, String> mapToCopy = new HashMap<>();
        mapToCopy.put("key3", "value3");
        when(sortedMap.containsKey(any())).thenReturn(false);

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> fixedSizeSortedMap.putAll(mapToCopy));
        verify(sortedMap, times(1)).containsKey(any());
    }

    @Test
    void testRemove() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> fixedSizeSortedMap.remove("key1"));
    }

    @Test
    void testSubMap() {
        // Given
        SortedMap<String, String> subMap = new TreeMap<>();
        subMap.put("key1", "value1");
        when(sortedMap.subMap(any(), any())).thenReturn(subMap);

        // When
        SortedMap<String, String> result = fixedSizeSortedMap.subMap("key1", "key2");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sortedMap, times(1)).subMap(any(), any());
    }

    @Test
    void testTailMap() {
        // Given
        SortedMap<String, String> tailMap = new TreeMap<>();
        tailMap.put("key2", "value2");
        when(sortedMap.tailMap(any())).thenReturn(tailMap);

        // When
        SortedMap<String, String> result = fixedSizeSortedMap.tailMap("key2");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sortedMap, times(1)).tailMap(any());
    }

    @Test
    void testValues() {
        // Given
        Collection<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        when(sortedMap.values()).thenReturn(values);

        // When
        Collection<String> result = fixedSizeSortedMap.values();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(sortedMap, times(1)).values();
    }
}
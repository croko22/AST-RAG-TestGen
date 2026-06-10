import org.apache.commons.collections4.IterableSortedMap;
import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.SortedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IterableSortedMapTest {

    @Mock
    private IterableSortedMap<String, Integer> iterableSortedMap;

    @BeforeEach
    void setup() {
        // Initialize the mock object
        iterableSortedMap = mock(IterableSortedMap.class);
    }

    @Test
    public void testComparator() {
        // Given: a comparator
        Comparator<String> comparator = mock(Comparator.class);

        // When: getComparator is called
        when(iterableSortedMap.comparator()).thenReturn(comparator);

        // Then: verify the result
        assertEquals(comparator, iterableSortedMap.comparator());

        // Verify the interaction with the mock object
        verify(iterableSortedMap, times(1)).comparator();
    }

    @Test
    public void testSubMap() {
        // Given: a fromKey and a toKey
        String fromKey = "fromKey";
        String toKey = "toKey";

        // When: subMap is called
        SortedMap<String, Integer> subMap = mock(SortedMap.class);
        when(iterableSortedMap.subMap(fromKey, toKey)).thenReturn(subMap);

        // Then: verify the result
        assertEquals(subMap, iterableSortedMap.subMap(fromKey, toKey));

        // Verify the interaction with the mock object
        verify(iterableSortedMap, times(1)).subMap(fromKey, toKey);
    }

    @Test
    public void testHeadMap() {
        // Given: a toKey
        String toKey = "toKey";

        // When: headMap is called
        SortedMap<String, Integer> headMap = mock(SortedMap.class);
        when(iterableSortedMap.headMap(toKey)).thenReturn(headMap);

        // Then: verify the result
        assertEquals(headMap, iterableSortedMap.headMap(toKey));

        // Verify the interaction with the mock object
        verify(iterableSortedMap, times(1)).headMap(toKey);
    }

    @Test
    public void testTailMap() {
        // Given: a fromKey
        String fromKey = "fromKey";

        // When: tailMap is called
        SortedMap<String, Integer> tailMap = mock(SortedMap.class);
        when(iterableSortedMap.tailMap(fromKey)).thenReturn(tailMap);

        // Then: verify the result
        assertEquals(tailMap, iterableSortedMap.tailMap(fromKey));

        // Verify the interaction with the mock object
        verify(iterableSortedMap, times(1)).tailMap(fromKey);
    }

    @Test
    public void testFirstKey() {
        // Given: a first key
        String firstKey = "firstKey";

        // When: firstKey is called
        when(iterableSortedMap.firstKey()).thenReturn(firstKey);

        // Then: verify the result
        assertEquals(firstKey, iterableSortedMap.firstKey());

        // Verify the interaction with the mock object
        verify(iterableSortedMap, times(1)).firstKey();
    }

    @Test
    public void testLastKey() {
        // Given: a last key
        String lastKey = "lastKey";

        // When: lastKey is called
        when(iterableSortedMap.lastKey()).thenReturn(lastKey);

        // Then: verify the result
        assertEquals(lastKey, iterableSortedMap.lastKey());

        // Verify the interaction with the mock object
        verify(iterableSortedMap, times(1)).lastKey();
    }

    @Test
    public void testGet() {
        // Given: a key
        String key = "key";
        Integer value = 1;

        // When: get is called
        when(iterableSortedMap.get(key)).thenReturn(value);

        // Then: verify the result
        assertEquals(value, iterableSortedMap.get(key));

        // Verify the interaction with the mock object
        verify(iterableSortedMap, times(1)).get(key);
    }

    @Test
    public void testPut() {
        // Given: a key and a value
        String key = "key";
        Integer value = 1;

        // When: put is called
        when(iterableSortedMap.put(key, value)).thenReturn(value);

        // Then: verify the result
        assertEquals(value, iterableSortedMap.put(key, value));

        // Verify the interaction with the mock object
        verify(iterableSortedMap, times(1)).put(key, value);
    }

    @Test
    public void testRemove() {
        // Given: a key
        String key = "key";
        Integer value = 1;

        // When: remove is called
        when(iterableSortedMap.remove(key)).thenReturn(value);

        // Then: verify the result
        assertEquals(value, iterableSortedMap.remove(key));

        // Verify the interaction with the mock object
        verify(iterableSortedMap, times(1)).remove(key);
    }

    @Test
    public void testContainsKey() {
        // Given: a key
        String key = "key";

        // When: containsKey is called
        when(iterableSortedMap.containsKey(key)).thenReturn(true);

        // Then: verify the result
        assertTrue(iterableSortedMap.containsKey(key));

        // Verify the interaction with the mock object
        verify(iterableSortedMap, times(1)).containsKey(key);
    }

    @Test
    public void testContainsValue() {
        // Given: a value
        Integer value = 1;

        // When: containsValue is called
        when(iterableSortedMap.containsValue(value)).thenReturn(true);

        // Then: verify the result
        assertTrue(iterableSortedMap.containsValue(value));

        // Verify the interaction with the mock object
        verify(iterableSortedMap, times(1)).containsValue(value);
    }

    @Test
    public void testIsEmpty() {
        // When: isEmpty is called
        when(iterableSortedMap.isEmpty()).thenReturn(true);

        // Then: verify the result
        assertTrue(iterableSortedMap.isEmpty());

        // Verify the interaction with the mock object
        verify(iterableSortedMap, times(1)).isEmpty();
    }

    @Test
    public void testSize() {
        // When: size is called
        when(iterableSortedMap.size()).thenReturn(1);

        // Then: verify the result
        assertEquals(1, iterableSortedMap.size());

        // Verify the interaction with the mock object
        verify(iterableSortedMap, times(1)).size();
    }

    @Test
    public void testClear() {
        // When: clear is called
        doNothing().when(iterableSortedMap).clear();

        // Then: verify the interaction with the mock object
        iterableSortedMap.clear();
        verify(iterableSortedMap, times(1)).clear();
    }

    @Test
    public void testPutAll() {
        // Given: a map
        Map<String, Integer> map = mock(Map.class);

        // When: putAll is called
        doNothing().when(iterableSortedMap).putAll(map);

        // Then: verify the interaction with the mock object
        iterableSortedMap.putAll(map);
        verify(iterableSortedMap, times(1)).putAll(map);
    }

    @Test
    public void testKeySet() {
        // When: keySet is called
        when(iterableSortedMap.keySet()).thenReturn(mock(java.util.Set.class));

        // Then: verify the result
        assertNotNull(iterableSortedMap.keySet());

        // Verify the interaction with the mock object
        verify(iterableSortedMap, times(1)).keySet();
    }

    @Test
    public void testValues() {
        // When: values is called
        when(iterableSortedMap.values()).thenReturn(mock(java.util.Collection.class));

        // Then: verify the result
        assertNotNull(iterableSortedMap.values());

        // Verify the interaction with the mock object
        verify(iterableSortedMap, times(1)).values();
    }

    @Test
    public void testEntrySet() {
        // When: entrySet is called
        when(iterableSortedMap.entrySet()).thenReturn(mock(java.util.Set.class));

        // Then: verify the result
        assertNotNull(iterableSortedMap.entrySet());

        // Verify the interaction with the mock object
        verify(iterableSortedMap, times(1)).entrySet();
    }
}
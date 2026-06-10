import org.apache.commons.collections4.map.ListOrderedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ListOrderedMapTest {

    @Mock
    private Map<String, String> map;

    private ListOrderedMap<String, String> listOrderedMap;

    @BeforeEach
    void setup() {
        listOrderedMap = ListOrderedMap.listOrderedMap(map);
    }

    @Test
    void testClear() {
        // Given
        when(map.containsKey("key1")).thenReturn(true);
        when(map.containsKey("key2")).thenReturn(true);

        // When
        listOrderedMap.clear();

        // Then
        verify(map).clear();
    }

    @Test
    void testContains() {
        // Given
        when(map.containsKey("key1")).thenReturn(true);

        // When
        boolean result = listOrderedMap.containsKey("key1");

        // Then
        assertTrue(result);
    }

    @Test
    void testContainsAll() {
        // Given
        when(map.containsKey("key1")).thenReturn(true);
        when(map.containsKey("key2")).thenReturn(true);

        // When
        boolean result = listOrderedMap.containsKey("key1") && listOrderedMap.containsKey("key2");

        // Then
        assertTrue(result);
    }

    @Test
    void testEquals() {
        // Given
        ListOrderedMap<String, String> otherListOrderedMap = ListOrderedMap.listOrderedMap(map);

        // When
        boolean result = listOrderedMap.equals(otherListOrderedMap);

        // Then
        assertTrue(result);
    }

    @Test
    void testHashCode() {
        // Given
        int hashCode = listOrderedMap.hashCode();

        // Then
        assertNotNull(hashCode);
    }

    @Test
    void testIsEmpty() {
        // Given
        when(map.isEmpty()).thenReturn(true);

        // When
        boolean result = listOrderedMap.isEmpty();

        // Then
        assertTrue(result);
    }

    @Test
    void testIterator() {
        // Given
        when(map.entrySet()).thenReturn(new HashSet<>());

        // When
        Iterator<Map.Entry<String, String>> iterator = listOrderedMap.entrySet().iterator();

        // Then
        assertNotNull(iterator);
    }

    @Test
    void testRemove() {
        // Given
        when(map.containsKey("key1")).thenReturn(true);

        // When
        listOrderedMap.remove("key1");

        // Then
        verify(map).remove("key1");
    }

    @Test
    void testSize() {
        // Given
        when(map.size()).thenReturn(2);

        // When
        int size = listOrderedMap.size();

        // Then
        assertEquals(2, size);
    }

    @Test
    void testToString() {
        // Given

        // When
        String result = listOrderedMap.toString();

        // Then
        assertNotNull(result);
    }

    @Test
    void testAsList() {
        // Given

        // When
        List<String> list = listOrderedMap.asList();

        // Then
        assertNotNull(list);
    }

    @Test
    void testEntrySet() {
        // Given

        // When
        Set<Map.Entry<String, String>> entrySet = listOrderedMap.entrySet();

        // Then
        assertNotNull(entrySet);
    }

    @Test
    void testFirstKey() {
        // Given
        when(map.keySet()).thenReturn(new HashSet<>(Arrays.asList("key1", "key2")));

        // When
        String firstKey = listOrderedMap.firstKey();

        // Then
        assertEquals("key1", firstKey);
    }

    @Test
    void testGet() {
        // Given
        when(map.get("key1")).thenReturn("value1");

        // When
        String value = listOrderedMap.get("key1");

        // Then
        assertEquals("value1", value);
    }

    @Test
    void testGetValue() {
        // Given
        when(map.get("key1")).thenReturn("value1");

        // When
        String value = listOrderedMap.getValue(0);

        // Then
        assertEquals("value1", value);
    }

    @Test
    void testIndexOf() {
        // Given
        when(map.containsKey("key1")).thenReturn(true);

        // When
        int index = listOrderedMap.indexOf("key1");

        // Then
        assertEquals(0, index);
    }

    @Test
    void testKeyList() {
        // Given

        // When
        List<String> keyList = listOrderedMap.keyList();

        // Then
        assertNotNull(keyList);
    }

    @Test
    void testKeySet() {
        // Given

        // When
        Set<String> keySet = listOrderedMap.keySet();

        // Then
        assertNotNull(keySet);
    }

    @Test
    void testLastKey() {
        // Given
        when(map.keySet()).thenReturn(new HashSet<>(Arrays.asList("key1", "key2")));

        // When
        String lastKey = listOrderedMap.lastKey();

        // Then
        assertEquals("key2", lastKey);
    }

    @Test
    void testMapIterator() {
        // Given

        // When
        OrderedMapIterator<String, String> mapIterator = listOrderedMap.mapIterator();

        // Then
        assertNotNull(mapIterator);
    }

    @Test
    void testNextKey() {
        // Given
        when(map.containsKey("key1")).thenReturn(true);
        when(map.containsKey("key2")).thenReturn(true);

        // When
        String nextKey = listOrderedMap.nextKey("key1");

        // Then
        assertEquals("key2", nextKey);
    }

    @Test
    void testPreviousKey() {
        // Given
        when(map.containsKey("key1")).thenReturn(true);
        when(map.containsKey("key2")).thenReturn(true);

        // When
        String previousKey = listOrderedMap.previousKey("key2");

        // Then
        assertEquals("key1", previousKey);
    }

    @Test
    void testPut() {
        // Given
        when(map.put("key1", "value1")).thenReturn("value1");

        // When
        String value = listOrderedMap.put("key1", "value1");

        // Then
        assertEquals("value1", value);
    }

    @Test
    void testPutAll() {
        // Given
        Map<String, String> otherMap = new HashMap<>();
        otherMap.put("key2", "value2");

        // When
        listOrderedMap.putAll(otherMap);

        // Then
        verify(map).putAll(otherMap);
    }

    @Test
    void testRemoveIndex() {
        // Given
        when(map.remove("key1")).thenReturn("value1");

        // When
        String value = listOrderedMap.remove(0);

        // Then
        assertEquals("value1", value);
    }

    @Test
    void testRemoveKey() {
        // Given
        when(map.remove("key1")).thenReturn("value1");

        // When
        String value = listOrderedMap.remove("key1");

        // Then
        assertEquals("value1", value);
    }

    @Test
    void testSetValue() {
        // Given
        when(map.put("key1", "value1")).thenReturn("value1");

        // When
        String value = listOrderedMap.setValue(0, "value1");

        // Then
        assertEquals("value1", value);
    }

    @Test
    void testValueList() {
        // Given

        // When
        List<String> valueList = listOrderedMap.valueList();

        // Then
        assertNotNull(valueList);
    }

    @Test
    void testValues() {
        // Given

        // When
        Collection<String> values = listOrderedMap.values();

        // Then
        assertNotNull(values);
    }
}
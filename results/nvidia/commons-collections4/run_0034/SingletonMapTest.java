import org.apache.commons.collections4.map.SingletonMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SingletonMapTest {

    @Mock
    private String key;

    @Mock
    private String value;

    private SingletonMap<String, String> singletonMap;

    @BeforeEach
    public void setup() {
        singletonMap = new SingletonMap<>(key, value);
    }

    @Test
    public void testGetKey() {
        // Given
        // When
        String result = singletonMap.getKey();
        // Then
        assertEquals(key, result);
    }

    @Test
    public void testGetValue() {
        // Given
        // When
        String result = singletonMap.getValue();
        // Then
        assertEquals(value, result);
    }

    @Test
    public void testHasNext() {
        // Given
        SingletonMap.SingletonMapIterator<String, String> iterator = singletonMap.mapIterator();
        // When
        boolean result = iterator.hasNext();
        // Then
        assertTrue(result);
    }

    @Test
    public void testHasPrevious() {
        // Given
        SingletonMap.SingletonMapIterator<String, String> iterator = singletonMap.mapIterator();
        // When
        boolean result = iterator.hasPrevious();
        // Then
        assertFalse(result);
    }

    @Test
    public void testNext() {
        // Given
        SingletonMap.SingletonMapIterator<String, String> iterator = singletonMap.mapIterator();
        // When
        String result = iterator.next();
        // Then
        assertEquals(key, result);
    }

    @Test
    public void testPrevious() {
        // Given
        SingletonMap.SingletonMapIterator<String, String> iterator = singletonMap.mapIterator();
        iterator.next();
        // When
        String result = iterator.previous();
        // Then
        assertEquals(key, result);
    }

    @Test
    public void testRemove() {
        // Given
        SingletonMap.SingletonMapIterator<String, String> iterator = singletonMap.mapIterator();
        // When and Then
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    public void testReset() {
        // Given
        SingletonMap.SingletonMapIterator<String, String> iterator = singletonMap.mapIterator();
        iterator.next();
        // When
        iterator.reset();
        // Then
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testSetValue() {
        // Given
        String newValue = "new value";
        // When
        String result = singletonMap.setValue(newValue);
        // Then
        assertEquals(value, result);
        assertEquals(newValue, singletonMap.getValue());
    }

    @Test
    public void testToString() {
        // Given
        // When
        String result = singletonMap.toString();
        // Then
        assertNotNull(result);
    }

    @Test
    public void testClear() {
        // Given
        // When and Then
        assertThrows(UnsupportedOperationException.class, singletonMap::clear);
    }

    @Test
    public void testContains() {
        // Given
        // When
        boolean result = singletonMap.containsValue(value);
        // Then
        assertTrue(result);
    }

    @Test
    public void testIsEmpty() {
        // Given
        // When
        boolean result = singletonMap.isEmpty();
        // Then
        assertFalse(result);
    }

    @Test
    public void testIterator() {
        // Given
        // When
        Iterator<String> result = singletonMap.values().iterator();
        // Then
        assertNotNull(result);
    }

    @Test
    public void testSize() {
        // Given
        // When
        int result = singletonMap.size();
        // Then
        assertEquals(1, result);
    }

    @Test
    public void testClone() {
        // Given
        // When
        SingletonMap<String, String> result = singletonMap.clone();
        // Then
        assertNotNull(result);
        assertEquals(singletonMap.getKey(), result.getKey());
        assertEquals(singletonMap.getValue(), result.getValue());
    }

    @Test
    public void testContainsKey() {
        // Given
        // When
        boolean result = singletonMap.containsKey(key);
        // Then
        assertTrue(result);
    }

    @Test
    public void testContainsValue() {
        // Given
        // When
        boolean result = singletonMap.containsValue(value);
        // Then
        assertTrue(result);
    }

    @Test
    public void testEntrySet() {
        // Given
        // When
        Set<Map.Entry<String, String>> result = singletonMap.entrySet();
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testEquals() {
        // Given
        SingletonMap<String, String> other = new SingletonMap<>(key, value);
        // When
        boolean result = singletonMap.equals(other);
        // Then
        assertTrue(result);
    }

    @Test
    public void testFirstKey() {
        // Given
        // When
        String result = singletonMap.firstKey();
        // Then
        assertEquals(key, result);
    }

    @Test
    public void testGet() {
        // Given
        // When
        String result = singletonMap.get(key);
        // Then
        assertEquals(value, result);
    }

    @Test
    public void testGetKey() {
        // Given
        // When
        String result = singletonMap.getKey();
        // Then
        assertEquals(key, result);
    }

    @Test
    public void testGetValue() {
        // Given
        // When
        String result = singletonMap.getValue();
        // Then
        assertEquals(value, result);
    }

    @Test
    public void testHashCode() {
        // Given
        // When
        int result = singletonMap.hashCode();
        // Then
        assertNotNull(result);
    }

    @Test
    public void testIsEmpty() {
        // Given
        // When
        boolean result = singletonMap.isEmpty();
        // Then
        assertFalse(result);
    }

    @Test
    public void testIsFull() {
        // Given
        // When
        boolean result = singletonMap.isFull();
        // Then
        assertTrue(result);
    }

    @Test
    public void testKeySet() {
        // Given
        // When
        Set<String> result = singletonMap.keySet();
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testLastKey() {
        // Given
        // When
        String result = singletonMap.lastKey();
        // Then
        assertEquals(key, result);
    }

    @Test
    public void testMapIterator() {
        // Given
        // When
        OrderedMapIterator<String, String> result = singletonMap.mapIterator();
        // Then
        assertNotNull(result);
    }

    @Test
    public void testMaxSize() {
        // Given
        // When
        int result = singletonMap.maxSize();
        // Then
        assertEquals(1, result);
    }

    @Test
    public void testNextKey() {
        // Given
        // When
        String result = singletonMap.nextKey(key);
        // Then
        assertNull(result);
    }

    @Test
    public void testPreviousKey() {
        // Given
        // When
        String result = singletonMap.previousKey(key);
        // Then
        assertNull(result);
    }

    @Test
    public void testPut() {
        // Given
        String newKey = "new key";
        String newValue = "new value";
        // When and Then
        assertThrows(IllegalArgumentException.class, () -> singletonMap.put(newKey, newValue));
    }

    @Test
    public void testPutAll() {
        // Given
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        // When
        singletonMap.putAll(map);
        // Then
        assertEquals(value, singletonMap.getValue());
    }

    @Test
    public void testRemove() {
        // Given
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> singletonMap.remove(key));
    }

    @Test
    public void testSetValue() {
        // Given
        String newValue = "new value";
        // When
        String result = singletonMap.setValue(newValue);
        // Then
        assertEquals(value, result);
        assertEquals(newValue, singletonMap.getValue());
    }

    @Test
    public void testSize() {
        // Given
        // When
        int result = singletonMap.size();
        // Then
        assertEquals(1, result);
    }

    @Test
    public void testToString() {
        // Given
        // When
        String result = singletonMap.toString();
        // Then
        assertNotNull(result);
    }

    @Test
    public void testValues() {
        // Given
        // When
        Collection<String> result = singletonMap.values();
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
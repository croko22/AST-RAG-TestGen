import org.apache.commons.collections4.map.AbstractMapDecorator;
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
public class AbstractMapDecoratorTest {

    @Mock
    private Map<String, String> map;

    private AbstractMapDecorator<String, String> abstractMapDecorator;

    @BeforeEach
    public void setup() {
        abstractMapDecorator = new AbstractMapDecorator<String, String>(map) {
        };
    }

    @Test
    public void testClear() {
        // Given
        when(map.isEmpty()).thenReturn(false);

        // When
        abstractMapDecorator.clear();

        // Then
        verify(map, times(1)).clear();
    }

    @Test
    public void testContainsKey_Present() {
        // Given
        String key = "key";
        when(map.containsKey(key)).thenReturn(true);

        // When
        boolean result = abstractMapDecorator.containsKey(key);

        // Then
        assertTrue(result);
        verify(map, times(1)).containsKey(key);
    }

    @Test
    public void testContainsKey_NotPresent() {
        // Given
        String key = "key";
        when(map.containsKey(key)).thenReturn(false);

        // When
        boolean result = abstractMapDecorator.containsKey(key);

        // Then
        assertFalse(result);
        verify(map, times(1)).containsKey(key);
    }

    @Test
    public void testContainsValue_Present() {
        // Given
        String value = "value";
        when(map.containsValue(value)).thenReturn(true);

        // When
        boolean result = abstractMapDecorator.containsValue(value);

        // Then
        assertTrue(result);
        verify(map, times(1)).containsValue(value);
    }

    @Test
    public void testContainsValue_NotPresent() {
        // Given
        String value = "value";
        when(map.containsValue(value)).thenReturn(false);

        // When
        boolean result = abstractMapDecorator.containsValue(value);

        // Then
        assertFalse(result);
        verify(map, times(1)).containsValue(value);
    }

    @Test
    public void testEntrySet() {
        // Given
        Set<Map.Entry<String, String>> entrySet = new HashSet<>();
        when(map.entrySet()).thenReturn(entrySet);

        // When
        Set<Map.Entry<String, String>> result = abstractMapDecorator.entrySet();

        // Then
        assertEquals(entrySet, result);
        verify(map, times(1)).entrySet();
    }

    @Test
    public void testEquals_SameObject() {
        // Given
        Object object = abstractMapDecorator;

        // When
        boolean result = abstractMapDecorator.equals(object);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_SameValues() {
        // Given
        Map<String, String> differentMap = new HashMap<>();
        differentMap.putAll(map);
        Object object = new AbstractMapDecorator<String, String>(differentMap) {
        };
        when(map.equals(any())).thenReturn(true);

        // When
        boolean result = abstractMapDecorator.equals(object);

        // Then
        assertTrue(result);
        verify(map, times(1)).equals(any());
    }

    @Test
    public void testEquals_DifferentObject_DifferentValues() {
        // Given
        Map<String, String> differentMap = new HashMap<>();
        differentMap.putAll(map);
        Object object = new AbstractMapDecorator<String, String>(differentMap) {
        };
        when(map.equals(any())).thenReturn(false);

        // When
        boolean result = abstractMapDecorator.equals(object);

        // Then
        assertFalse(result);
        verify(map, times(1)).equals(any());
    }

    @Test
    public void testGet_Present() {
        // Given
        String key = "key";
        String value = "value";
        when(map.get(key)).thenReturn(value);

        // When
        String result = abstractMapDecorator.get(key);

        // Then
        assertEquals(value, result);
        verify(map, times(1)).get(key);
    }

    @Test
    public void testGet_NotPresent() {
        // Given
        String key = "key";
        when(map.get(key)).thenReturn(null);

        // When
        String result = abstractMapDecorator.get(key);

        // Then
        assertNull(result);
        verify(map, times(1)).get(key);
    }

    @Test
    public void testHashCode() {
        // Given
        int hashCode = 123;
        when(map.hashCode()).thenReturn(hashCode);

        // When
        int result = abstractMapDecorator.hashCode();

        // Then
        assertEquals(hashCode, result);
        verify(map, times(1)).hashCode();
    }

    @Test
    public void testIsEmpty_Empty() {
        // Given
        when(map.isEmpty()).thenReturn(true);

        // When
        boolean result = abstractMapDecorator.isEmpty();

        // Then
        assertTrue(result);
        verify(map, times(1)).isEmpty();
    }

    @Test
    public void testIsEmpty_NotEmpty() {
        // Given
        when(map.isEmpty()).thenReturn(false);

        // When
        boolean result = abstractMapDecorator.isEmpty();

        // Then
        assertFalse(result);
        verify(map, times(1)).isEmpty();
    }

    @Test
    public void testKeySet() {
        // Given
        Set<String> keySet = new HashSet<>();
        when(map.keySet()).thenReturn(keySet);

        // When
        Set<String> result = abstractMapDecorator.keySet();

        // Then
        assertEquals(keySet, result);
        verify(map, times(1)).keySet();
    }

    @Test
    public void testPut() {
        // Given
        String key = "key";
        String value = "value";
        when(map.put(key, value)).thenReturn(value);

        // When
        String result = abstractMapDecorator.put(key, value);

        // Then
        assertEquals(value, result);
        verify(map, times(1)).put(key, value);
    }

    @Test
    public void testPutAll() {
        // Given
        Map<String, String> mapToCopy = new HashMap<>();

        // When
        abstractMapDecorator.putAll(mapToCopy);

        // Then
        verify(map, times(1)).putAll(mapToCopy);
    }

    @Test
    public void testRemove_Present() {
        // Given
        String key = "key";
        String value = "value";
        when(map.remove(key)).thenReturn(value);

        // When
        String result = abstractMapDecorator.remove(key);

        // Then
        assertEquals(value, result);
        verify(map, times(1)).remove(key);
    }

    @Test
    public void testRemove_NotPresent() {
        // Given
        String key = "key";
        when(map.remove(key)).thenReturn(null);

        // When
        String result = abstractMapDecorator.remove(key);

        // Then
        assertNull(result);
        verify(map, times(1)).remove(key);
    }

    @Test
    public void testSize() {
        // Given
        int size = 10;
        when(map.size()).thenReturn(size);

        // When
        int result = abstractMapDecorator.size();

        // Then
        assertEquals(size, result);
        verify(map, times(1)).size();
    }

    @Test
    public void testToString() {
        // Given
        String toString = "toString";
        when(map.toString()).thenReturn(toString);

        // When
        String result = abstractMapDecorator.toString();

        // Then
        assertEquals(toString, result);
        verify(map, times(1)).toString();
    }

    @Test
    public void testValues() {
        // Given
        Collection<String> values = new ArrayList<>();
        when(map.values()).thenReturn(values);

        // When
        Collection<String> result = abstractMapDecorator.values();

        // Then
        assertEquals(values, result);
        verify(map, times(1)).values();
    }
}
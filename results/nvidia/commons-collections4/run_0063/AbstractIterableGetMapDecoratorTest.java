import org.apache.commons.collections4.IterableGet;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.map.EntrySetToMapIteratorAdapter;
import org.apache.commons.collections4.splitmap.AbstractIterableGetMapDecorator;
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
public class AbstractIterableGetMapDecoratorTest {

    @Mock
    private Map<String, String> map;

    private AbstractIterableGetMapDecorator<String, String> decorator;

    @BeforeEach
    void setup() {
        decorator = new AbstractIterableGetMapDecorator<>(map);
    }

    @Test
    void testContainsKey_KeyPresent_ReturnsTrue() {
        // Given
        String key = "key";
        when(map.containsKey(key)).thenReturn(true);

        // When
        boolean result = decorator.containsKey(key);

        // Then
        assertTrue(result);
        verify(map, times(1)).containsKey(key);
    }

    @Test
    void testContainsKey_KeyNotPresent_ReturnsFalse() {
        // Given
        String key = "key";
        when(map.containsKey(key)).thenReturn(false);

        // When
        boolean result = decorator.containsKey(key);

        // Then
        assertFalse(result);
        verify(map, times(1)).containsKey(key);
    }

    @Test
    void testContainsValue_ValuePresent_ReturnsTrue() {
        // Given
        String value = "value";
        when(map.containsValue(value)).thenReturn(true);

        // When
        boolean result = decorator.containsValue(value);

        // Then
        assertTrue(result);
        verify(map, times(1)).containsValue(value);
    }

    @Test
    void testContainsValue_ValueNotPresent_ReturnsFalse() {
        // Given
        String value = "value";
        when(map.containsValue(value)).thenReturn(false);

        // When
        boolean result = decorator.containsValue(value);

        // Then
        assertFalse(result);
        verify(map, times(1)).containsValue(value);
    }

    @Test
    void testEntrySet_ReturnsEntrySet() {
        // Given
        Set<Map.Entry<String, String>> entrySet = new HashSet<>();
        when(map.entrySet()).thenReturn(entrySet);

        // When
        Set<Map.Entry<String, String>> result = decorator.entrySet();

        // Then
        assertEquals(entrySet, result);
        verify(map, times(1)).entrySet();
    }

    @Test
    void testEquals_SameObject_ReturnsTrue() {
        // Given
        Object object = decorator;

        // When
        boolean result = decorator.equals(object);

        // Then
        assertTrue(result);
    }

    @Test
    void testEquals_DifferentObject_ReturnsFalse() {
        // Given
        Object object = new Object();

        // When
        boolean result = decorator.equals(object);

        // Then
        assertFalse(result);
    }

    @Test
    void testEquals_MapEquals_ReturnsTrue() {
        // Given
        Object object = new AbstractIterableGetMapDecorator<>(map);
        when(map.equals(any())).thenReturn(true);

        // When
        boolean result = decorator.equals(object);

        // Then
        assertTrue(result);
        verify(map, times(1)).equals(any());
    }

    @Test
    void testEquals_MapNotEquals_ReturnsFalse() {
        // Given
        Object object = new AbstractIterableGetMapDecorator<>(map);
        when(map.equals(any())).thenReturn(false);

        // When
        boolean result = decorator.equals(object);

        // Then
        assertFalse(result);
        verify(map, times(1)).equals(any());
    }

    @Test
    void testGet_KeyPresent_ReturnsValue() {
        // Given
        String key = "key";
        String value = "value";
        when(map.get(key)).thenReturn(value);

        // When
        String result = decorator.get(key);

        // Then
        assertEquals(value, result);
        verify(map, times(1)).get(key);
    }

    @Test
    void testGet_KeyNotPresent_ReturnsNull() {
        // Given
        String key = "key";
        when(map.get(key)).thenReturn(null);

        // When
        String result = decorator.get(key);

        // Then
        assertNull(result);
        verify(map, times(1)).get(key);
    }

    @Test
    void testHashCode_ReturnsHashCode() {
        // Given
        int hashCode = 123;
        when(map.hashCode()).thenReturn(hashCode);

        // When
        int result = decorator.hashCode();

        // Then
        assertEquals(hashCode, result);
        verify(map, times(1)).hashCode();
    }

    @Test
    void testIsEmpty_MapEmpty_ReturnsTrue() {
        // Given
        when(map.isEmpty()).thenReturn(true);

        // When
        boolean result = decorator.isEmpty();

        // Then
        assertTrue(result);
        verify(map, times(1)).isEmpty();
    }

    @Test
    void testIsEmpty_MapNotEmpty_ReturnsFalse() {
        // Given
        when(map.isEmpty()).thenReturn(false);

        // When
        boolean result = decorator.isEmpty();

        // Then
        assertFalse(result);
        verify(map, times(1)).isEmpty();
    }

    @Test
    void testKeySet_ReturnsKeySet() {
        // Given
        Set<String> keySet = new HashSet<>();
        when(map.keySet()).thenReturn(keySet);

        // When
        Set<String> result = decorator.keySet();

        // Then
        assertEquals(keySet, result);
        verify(map, times(1)).keySet();
    }

    @Test
    void testMapIterator_ReturnsMapIterator() {
        // Given
        MapIterator<String, String> mapIterator = new EntrySetToMapIteratorAdapter<>(new HashSet<>());
        when(map.entrySet()).thenReturn(new HashSet<>());

        // When
        MapIterator<String, String> result = decorator.mapIterator();

        // Then
        assertNotNull(result);
        verify(map, times(1)).entrySet();
    }

    @Test
    void testRemove_KeyPresent_ReturnsValue() {
        // Given
        String key = "key";
        String value = "value";
        when(map.remove(key)).thenReturn(value);

        // When
        String result = decorator.remove(key);

        // Then
        assertEquals(value, result);
        verify(map, times(1)).remove(key);
    }

    @Test
    void testRemove_KeyNotPresent_ReturnsNull() {
        // Given
        String key = "key";
        when(map.remove(key)).thenReturn(null);

        // When
        String result = decorator.remove(key);

        // Then
        assertNull(result);
        verify(map, times(1)).remove(key);
    }

    @Test
    void testSize_ReturnsSize() {
        // Given
        int size = 123;
        when(map.size()).thenReturn(size);

        // When
        int result = decorator.size();

        // Then
        assertEquals(size, result);
        verify(map, times(1)).size();
    }

    @Test
    void testToString_ReturnsToString() {
        // Given
        String toString = "toString";
        when(map.toString()).thenReturn(toString);

        // When
        String result = decorator.toString();

        // Then
        assertEquals(toString, result);
        verify(map, times(1)).toString();
    }

    @Test
    void testValues_ReturnsValues() {
        // Given
        Collection<String> values = new ArrayList<>();
        when(map.values()).thenReturn(values);

        // When
        Collection<String> result = decorator.values();

        // Then
        assertEquals(values, result);
        verify(map, times(1)).values();
    }
}
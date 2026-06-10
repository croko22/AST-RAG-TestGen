import org.apache.commons.collections4.iterators.EntrySetMapIterator;
import org.apache.commons.collections4.iterators.UnmodifiableMapIterator;
import org.apache.commons.collections4.map.UnmodifiableMap;
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
public class UnmodifiableMapTest {

    @Mock
    private Map<String, String> map;

    private UnmodifiableMap<String, String> unmodifiableMap;

    @BeforeEach
    void setup() {
        unmodifiableMap = UnmodifiableMap.unmodifiableMap(map);
    }

    @Test
    void testUnmodifiableMap() {
        // Given
        when(map.isEmpty()).thenReturn(false);

        // When
        boolean isEmpty = unmodifiableMap.isEmpty();

        // Then
        assertFalse(isEmpty);
        verify(map, times(1)).isEmpty();
    }

    @Test
    void testClear() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMap.clear());
    }

    @Test
    void testEntrySet() {
        // Given
        Set<Map.Entry<String, String>> entrySet = new HashSet<>();
        when(map.entrySet()).thenReturn(entrySet);

        // When
        Set<Map.Entry<String, String>> result = unmodifiableMap.entrySet();

        // Then
        assertNotNull(result);
        assertNotSame(entrySet, result);
        verify(map, times(1)).entrySet();
    }

    @Test
    void testKeySet() {
        // Given
        Set<String> keySet = new HashSet<>();
        when(map.keySet()).thenReturn(keySet);

        // When
        Set<String> result = unmodifiableMap.keySet();

        // Then
        assertNotNull(result);
        assertNotSame(keySet, result);
        verify(map, times(1)).keySet();
    }

    @Test
    void testMapIterator() {
        // Given
        MapIterator<String, String> mapIterator = mock(MapIterator.class);
        when(map instanceof IterableMap).thenReturn(true);
        when(((IterableMap<String, String>) map).mapIterator()).thenReturn(mapIterator);

        // When
        MapIterator<String, String> result = unmodifiableMap.mapIterator();

        // Then
        assertNotNull(result);
        assertNotSame(mapIterator, result);
        verify(map, times(1)).mapIterator();
    }

    @Test
    void testPut() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMap.put("key", "value"));
    }

    @Test
    void testPutAll() {
        // Given
        Map<String, String> mapToCopy = new HashMap<>();

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMap.putAll(mapToCopy));
    }

    @Test
    void testRemove() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMap.remove("key"));
    }

    @Test
    void testValues() {
        // Given
        Collection<String> values = new ArrayList<>();
        when(map.values()).thenReturn(values);

        // When
        Collection<String> result = unmodifiableMap.values();

        // Then
        assertNotNull(result);
        assertNotSame(values, result);
        verify(map, times(1)).values();
    }

    @Test
    void testUnmodifiableMapFactoryMethod() {
        // Given
        Map<String, String> map = new HashMap<>();

        // When
        Map<String, String> result = UnmodifiableMap.unmodifiableMap(map);

        // Then
        assertNotNull(result);
        assertNotSame(map, result);
    }

    @Test
    void testUnmodifiableMapFactoryMethodWithUnmodifiableMap() {
        // Given
        Map<String, String> map = UnmodifiableMap.unmodifiableMap(new HashMap<>());

        // When
        Map<String, String> result = UnmodifiableMap.unmodifiableMap(map);

        // Then
        assertSame(map, result);
    }
}
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.UnmodifiableBidiMap;
import org.apache.commons.collections4.iterators.UnmodifiableMapIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnmodifiableBidiMapTest {

    @Mock
    private BidiMap<String, String> bidiMap;

    private UnmodifiableBidiMap<String, String> unmodifiableBidiMap;

    @BeforeEach
    public void setup() {
        unmodifiableBidiMap = new UnmodifiableBidiMap<>(bidiMap);
    }

    @Test
    public void testUnmodifiableBidiMapFactoryMethod() {
        // Given
        BidiMap<String, String> map = new HashMap<>();

        // When
        BidiMap<String, String> unmodifiableMap = UnmodifiableBidiMap.unmodifiableBidiMap(map);

        // Then
        assertNotNull(unmodifiableMap);
        assertNotSame(map, unmodifiableMap);
    }

    @Test
    public void testClear() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableBidiMap.clear());
    }

    @Test
    public void testEntrySet() {
        // Given
        Set<Map.Entry<String, String>> entrySet = mock(Set.class);
        when(bidiMap.entrySet()).thenReturn(entrySet);

        // When
        Set<Map.Entry<String, String>> result = unmodifiableBidiMap.entrySet();

        // Then
        assertNotNull(result);
        assertNotSame(entrySet, result);
    }

    @Test
    public void testInverseBidiMap() {
        // Given
        BidiMap<String, String> inverseMap = mock(BidiMap.class);
        when(bidiMap.inverseBidiMap()).thenReturn(inverseMap);

        // When
        BidiMap<String, String> result = unmodifiableBidiMap.inverseBidiMap();

        // Then
        assertNotNull(result);
        assertNotSame(inverseMap, result);
    }

    @Test
    public void testKeySet() {
        // Given
        Set<String> keySet = mock(Set.class);
        when(bidiMap.keySet()).thenReturn(keySet);

        // When
        Set<String> result = unmodifiableBidiMap.keySet();

        // Then
        assertNotNull(result);
        assertNotSame(keySet, result);
    }

    @Test
    public void testMapIterator() {
        // Given
        UnmodifiableMapIterator<String, String> iterator = mock(UnmodifiableMapIterator.class);
        when(bidiMap.mapIterator()).thenReturn(iterator);

        // When
        UnmodifiableMapIterator<String, String> result = unmodifiableBidiMap.mapIterator();

        // Then
        assertNotNull(result);
        assertNotSame(iterator, result);
    }

    @Test
    public void testPut() {
        // Given
        String key = "key";
        String value = "value";

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableBidiMap.put(key, value));
    }

    @Test
    public void testPutAll() {
        // Given
        Map<String, String> map = new HashMap<>();

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableBidiMap.putAll(map));
    }

    @Test
    public void testRemove() {
        // Given
        String key = "key";

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableBidiMap.remove(key));
    }

    @Test
    public void testRemoveValue() {
        // Given
        String value = "value";

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableBidiMap.removeValue(value));
    }

    @Test
    public void testValues() {
        // Given
        Set<String> values = mock(Set.class);
        when(bidiMap.values()).thenReturn(values);

        // When
        Set<String> result = unmodifiableBidiMap.values();

        // Then
        assertNotNull(result);
        assertNotSame(values, result);
    }
}
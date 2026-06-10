import org.apache.commons.collections4.bidimap.UnmodifiableOrderedBidiMap;
import org.apache.commons.collections4.OrderedBidiMap;
import org.apache.commons.collections4.iterators.UnmodifiableOrderedMapIterator;
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
public class UnmodifiableOrderedBidiMapTest {

    @Mock
    private OrderedBidiMap<String, String> orderedBidiMap;

    private UnmodifiableOrderedBidiMap<String, String> unmodifiableOrderedBidiMap;

    @BeforeEach
    void setup() {
        unmodifiableOrderedBidiMap = new UnmodifiableOrderedBidiMap<>(orderedBidiMap);
    }

    @Test
    void testUnmodifiableOrderedBidiMap() {
        // Given
        when(orderedBidiMap.inverseBidiMap()).thenReturn(orderedBidiMap);

        // When
        UnmodifiableOrderedBidiMap<String, String> result = UnmodifiableOrderedBidiMap.unmodifiableOrderedBidiMap(orderedBidiMap);

        // Then
        assertNotNull(result);
        assertSame(unmodifiableOrderedBidiMap.getClass(), result.getClass());
    }

    @Test
    void testClear() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableOrderedBidiMap.clear());
    }

    @Test
    void testEntrySet() {
        // Given
        Set<Map.Entry<String, String>> entrySet = new HashSet<>();
        when(orderedBidiMap.entrySet()).thenReturn(entrySet);

        // When
        Set<Map.Entry<String, String>> result = unmodifiableOrderedBidiMap.entrySet();

        // Then
        assertNotNull(result);
        assertNotSame(entrySet, result);
    }

    @Test
    void testInverseBidiMap() {
        // Given
        when(orderedBidiMap.inverseBidiMap()).thenReturn(orderedBidiMap);

        // When
        OrderedBidiMap<String, String> result = unmodifiableOrderedBidiMap.inverseBidiMap();

        // Then
        assertNotNull(result);
        assertSame(unmodifiableOrderedBidiMap.getClass(), result.getClass());
    }

    @Test
    void testInverseOrderedBidiMap() {
        // Given
        when(orderedBidiMap.inverseBidiMap()).thenReturn(orderedBidiMap);

        // When
        OrderedBidiMap<String, String> result = unmodifiableOrderedBidiMap.inverseOrderedBidiMap();

        // Then
        assertNotNull(result);
        assertSame(unmodifiableOrderedBidiMap.getClass(), result.getClass());
    }

    @Test
    void testKeySet() {
        // Given
        Set<String> keySet = new HashSet<>();
        when(orderedBidiMap.keySet()).thenReturn(keySet);

        // When
        Set<String> result = unmodifiableOrderedBidiMap.keySet();

        // Then
        assertNotNull(result);
        assertNotSame(keySet, result);
    }

    @Test
    void testMapIterator() {
        // Given
        UnmodifiableOrderedMapIterator<String, String> iterator = mock(UnmodifiableOrderedMapIterator.class);
        when(orderedBidiMap.mapIterator()).thenReturn(iterator);

        // When
        OrderedMapIterator<String, String> result = unmodifiableOrderedBidiMap.mapIterator();

        // Then
        assertNotNull(result);
        assertSame(iterator.getClass(), result.getClass());
    }

    @Test
    void testPut() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableOrderedBidiMap.put("key", "value"));
    }

    @Test
    void testPutAll() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableOrderedBidiMap.putAll(new HashMap<>()));
    }

    @Test
    void testRemove() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableOrderedBidiMap.remove("key"));
    }

    @Test
    void testRemoveValue() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableOrderedBidiMap.removeValue("value"));
    }

    @Test
    void testValues() {
        // Given
        Set<String> values = new HashSet<>();
        when(orderedBidiMap.values()).thenReturn(values);

        // When
        Set<String> result = unmodifiableOrderedBidiMap.values();

        // Then
        assertNotNull(result);
        assertNotSame(values, result);
    }
}
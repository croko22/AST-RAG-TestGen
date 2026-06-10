import org.apache.commons.collections4.bidimap.UnmodifiableSortedBidiMap;
import org.apache.commons.collections4.bidimap.SortedBidiMap;
import org.apache.commons.collections4.iterators.OrderedMapIterator;
import org.apache.commons.collections4.map.UnmodifiableSortedMap;
import org.apache.commons.collections4.set.UnmodifiableSet;
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
public class UnmodifiableSortedBidiMapTest {

    @Mock
    private SortedBidiMap<String, String> sortedBidiMap;

    private UnmodifiableSortedBidiMap<String, String> unmodifiableSortedBidiMap;

    @BeforeEach
    void setup() {
        unmodifiableSortedBidiMap = UnmodifiableSortedBidiMap.unmodifiableSortedBidiMap(sortedBidiMap);
    }

    @Test
    void testUnmodifiableSortedBidiMap() {
        // Given
        when(sortedBidiMap.entrySet()).thenReturn(new HashSet<>());

        // When
        Set<Map.Entry<String, String>> entrySet = unmodifiableSortedBidiMap.entrySet();

        // Then
        assertNotNull(entrySet);
        assertTrue(entrySet instanceof UnmodifiableSet);
    }

    @Test
    void testClear() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedBidiMap.clear());
        verify(sortedBidiMap, never()).clear();
    }

    @Test
    void testHeadMap() {
        // Given
        when(sortedBidiMap.headMap(any())).thenReturn(new TreeMap<>());

        // When
        SortedMap<String, String> headMap = unmodifiableSortedBidiMap.headMap("key");

        // Then
        assertNotNull(headMap);
        assertTrue(headMap instanceof UnmodifiableSortedMap);
    }

    @Test
    void testInverseBidiMap() {
        // Given
        when(sortedBidiMap.inverseBidiMap()).thenReturn(mock(SortedBidiMap.class));

        // When
        SortedBidiMap<String, String> inverseBidiMap = unmodifiableSortedBidiMap.inverseBidiMap();

        // Then
        assertNotNull(inverseBidiMap);
        assertTrue(inverseBidiMap instanceof UnmodifiableSortedBidiMap);
    }

    @Test
    void testKeySet() {
        // Given
        when(sortedBidiMap.keySet()).thenReturn(new HashSet<>());

        // When
        Set<String> keySet = unmodifiableSortedBidiMap.keySet();

        // Then
        assertNotNull(keySet);
        assertTrue(keySet instanceof UnmodifiableSet);
    }

    @Test
    void testMapIterator() {
        // Given
        when(sortedBidiMap.mapIterator()).thenReturn(mock(OrderedMapIterator.class));

        // When
        OrderedMapIterator<String, String> mapIterator = unmodifiableSortedBidiMap.mapIterator();

        // Then
        assertNotNull(mapIterator);
    }

    @Test
    void testPut() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedBidiMap.put("key", "value"));
        verify(sortedBidiMap, never()).put(any(), any());
    }

    @Test
    void testPutAll() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedBidiMap.putAll(new HashMap<>()));
        verify(sortedBidiMap, never()).putAll(any());
    }

    @Test
    void testRemove() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedBidiMap.remove("key"));
        verify(sortedBidiMap, never()).remove(any());
    }

    @Test
    void testRemoveValue() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedBidiMap.removeValue("value"));
        verify(sortedBidiMap, never()).removeValue(any());
    }

    @Test
    void testSubMap() {
        // Given
        when(sortedBidiMap.subMap(any(), any())).thenReturn(new TreeMap<>());

        // When
        SortedMap<String, String> subMap = unmodifiableSortedBidiMap.subMap("fromKey", "toKey");

        // Then
        assertNotNull(subMap);
        assertTrue(subMap instanceof UnmodifiableSortedMap);
    }

    @Test
    void testTailMap() {
        // Given
        when(sortedBidiMap.tailMap(any())).thenReturn(new TreeMap<>());

        // When
        SortedMap<String, String> tailMap = unmodifiableSortedBidiMap.tailMap("fromKey");

        // Then
        assertNotNull(tailMap);
        assertTrue(tailMap instanceof UnmodifiableSortedMap);
    }

    @Test
    void testValues() {
        // Given
        when(sortedBidiMap.values()).thenReturn(new HashSet<>());

        // When
        Set<String> values = unmodifiableSortedBidiMap.values();

        // Then
        assertNotNull(values);
        assertTrue(values instanceof UnmodifiableSet);
    }
}
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.collection.UnmodifiableCollection;
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
public class UnmodifiableSortedMapTest {

    @Mock
    private SortedMap<String, String> sortedMap;

    private UnmodifiableSortedMap<String, String> unmodifiableSortedMap;

    @BeforeEach
    void setup() {
        unmodifiableSortedMap = new UnmodifiableSortedMap<>(sortedMap);
    }

    @Test
    public void testUnmodifiableSortedMapFactoryMethod() {
        // Given
        SortedMap<String, String> map = new TreeMap<>();

        // When
        SortedMap<String, String> unmodifiableMap = UnmodifiableSortedMap.unmodifiableSortedMap(map);

        // Then
        assertNotNull(unmodifiableMap);
        assertNotSame(map, unmodifiableMap);
    }

    @Test
    public void testClear() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedMap.clear());
    }

    @Test
    public void testComparator() {
        // Given
        Comparator<String> comparator = mock(Comparator.class);
        when(sortedMap.comparator()).thenReturn(comparator);

        // When
        Comparator<String> result = unmodifiableSortedMap.comparator();

        // Then
        assertSame(comparator, result);
    }

    @Test
    public void testEntrySet() {
        // Given
        Set<Map.Entry<String, String>> entrySet = mock(Set.class);
        when(sortedMap.entrySet()).thenReturn(entrySet);

        // When
        Set<Map.Entry<String, String>> result = unmodifiableSortedMap.entrySet();

        // Then
        assertNotNull(result);
        assertNotSame(entrySet, result);
    }

    @Test
    public void testFirstKey() {
        // Given
        String firstKey = "firstKey";
        when(sortedMap.firstKey()).thenReturn(firstKey);

        // When
        String result = unmodifiableSortedMap.firstKey();

        // Then
        assertEquals(firstKey, result);
    }

    @Test
    public void testHeadMap() {
        // Given
        String toKey = "toKey";
        SortedMap<String, String> headMap = mock(SortedMap.class);
        when(sortedMap.headMap(toKey)).thenReturn(headMap);

        // When
        SortedMap<String, String> result = unmodifiableSortedMap.headMap(toKey);

        // Then
        assertNotNull(result);
        assertNotSame(headMap, result);
    }

    @Test
    public void testKeySet() {
        // Given
        Set<String> keySet = mock(Set.class);
        when(sortedMap.keySet()).thenReturn(keySet);

        // When
        Set<String> result = unmodifiableSortedMap.keySet();

        // Then
        assertNotNull(result);
        assertNotSame(keySet, result);
    }

    @Test
    public void testLastKey() {
        // Given
        String lastKey = "lastKey";
        when(sortedMap.lastKey()).thenReturn(lastKey);

        // When
        String result = unmodifiableSortedMap.lastKey();

        // Then
        assertEquals(lastKey, result);
    }

    @Test
    public void testPut() {
        // Given
        String key = "key";
        String value = "value";

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedMap.put(key, value));
    }

    @Test
    public void testPutAll() {
        // Given
        Map<String, String> mapToCopy = mock(Map.class);

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedMap.putAll(mapToCopy));
    }

    @Test
    public void testRemove() {
        // Given
        Object key = "key";

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedMap.remove(key));
    }

    @Test
    public void testSubMap() {
        // Given
        String fromKey = "fromKey";
        String toKey = "toKey";
        SortedMap<String, String> subMap = mock(SortedMap.class);
        when(sortedMap.subMap(fromKey, toKey)).thenReturn(subMap);

        // When
        SortedMap<String, String> result = unmodifiableSortedMap.subMap(fromKey, toKey);

        // Then
        assertNotNull(result);
        assertNotSame(subMap, result);
    }

    @Test
    public void testTailMap() {
        // Given
        String fromKey = "fromKey";
        SortedMap<String, String> tailMap = mock(SortedMap.class);
        when(sortedMap.tailMap(fromKey)).thenReturn(tailMap);

        // When
        SortedMap<String, String> result = unmodifiableSortedMap.tailMap(fromKey);

        // Then
        assertNotNull(result);
        assertNotSame(tailMap, result);
    }

    @Test
    public void testValues() {
        // Given
        Collection<String> values = mock(Collection.class);
        when(sortedMap.values()).thenReturn(values);

        // When
        Collection<String> result = unmodifiableSortedMap.values();

        // Then
        assertNotNull(result);
        assertNotSame(values, result);
    }
}
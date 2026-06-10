import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.map.LazySortedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LazySortedMapTest {

    @Mock
    private SortedMap<String, String> sortedMap;

    @Mock
    private Factory<String> factory;

    @Mock
    private Transformer<String, String> transformer;

    private LazySortedMap<String, String> lazySortedMap;

    @BeforeEach
    void setup() {
        lazySortedMap = LazySortedMap.lazySortedMap(sortedMap, factory);
    }

    @Test
    public void testLazySortedMapFactory() {
        // Given
        SortedMap<String, String> map = new TreeMap<>();
        Factory<String> factory = mock(Factory.class);
        when(factory.create()).thenReturn("value");

        // When
        LazySortedMap<String, String> lazySortedMap = LazySortedMap.lazySortedMap(map, factory);

        // Then
        assertNotNull(lazySortedMap);
    }

    @Test
    public void testLazySortedMapTransformer() {
        // Given
        SortedMap<String, String> map = new TreeMap<>();
        Transformer<String, String> transformer = mock(Transformer.class);
        when(transformer.apply(any())).thenReturn("value");

        // When
        LazySortedMap<String, String> lazySortedMap = LazySortedMap.lazySortedMap(map, transformer);

        // Then
        assertNotNull(lazySortedMap);
    }

    @Test
    public void testComparator() {
        // Given
        Comparator<String> comparator = mock(Comparator.class);
        when(sortedMap.comparator()).thenReturn(comparator);

        // When
        Comparator<String> result = lazySortedMap.comparator();

        // Then
        assertEquals(comparator, result);
    }

    @Test
    public void testFirstKey() {
        // Given
        String key = "key";
        when(sortedMap.firstKey()).thenReturn(key);

        // When
        String result = lazySortedMap.firstKey();

        // Then
        assertEquals(key, result);
    }

    @Test
    public void testHeadMap() {
        // Given
        String toKey = "toKey";
        SortedMap<String, String> headMap = mock(SortedMap.class);
        when(sortedMap.headMap(toKey)).thenReturn(headMap);

        // When
        SortedMap<String, String> result = lazySortedMap.headMap(toKey);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testLastKey() {
        // Given
        String key = "key";
        when(sortedMap.lastKey()).thenReturn(key);

        // When
        String result = lazySortedMap.lastKey();

        // Then
        assertEquals(key, result);
    }

    @Test
    public void testSubMap() {
        // Given
        String fromKey = "fromKey";
        String toKey = "toKey";
        SortedMap<String, String> subMap = mock(SortedMap.class);
        when(sortedMap.subMap(fromKey, toKey)).thenReturn(subMap);

        // When
        SortedMap<String, String> result = lazySortedMap.subMap(fromKey, toKey);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testTailMap() {
        // Given
        String fromKey = "fromKey";
        SortedMap<String, String> tailMap = mock(SortedMap.class);
        when(sortedMap.tailMap(fromKey)).thenReturn(tailMap);

        // When
        SortedMap<String, String> result = lazySortedMap.tailMap(fromKey);

        // Then
        assertNotNull(result);
    }
}
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.map.TransformedSortedMap;
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
public class TransformedSortedMapTest {

    @Mock
    private Transformer<String, String> keyTransformer;

    @Mock
    private Transformer<String, String> valueTransformer;

    @Mock
    private SortedMap<String, String> sortedMap;

    private TransformedSortedMap<String, String> transformedSortedMap;

    @BeforeEach
    public void setup() {
        transformedSortedMap = TransformedSortedMap.transformingSortedMap(sortedMap, keyTransformer, valueTransformer);
    }

    @Test
    public void testTransformedSortedMap() {
        // Given
        when(sortedMap.isEmpty()).thenReturn(true);

        // When
        TransformedSortedMap<String, String> result = TransformedSortedMap.transformedSortedMap(sortedMap, keyTransformer, valueTransformer);

        // Then
        assertNotNull(result);
        assertSame(sortedMap, result.getSortedMap());
    }

    @Test
    public void testTransformingSortedMap() {
        // Given
        when(sortedMap.isEmpty()).thenReturn(true);

        // When
        TransformedSortedMap<String, String> result = TransformedSortedMap.transformingSortedMap(sortedMap, keyTransformer, valueTransformer);

        // Then
        assertNotNull(result);
        assertSame(sortedMap, result.getSortedMap());
    }

    @Test
    public void testComparator() {
        // Given
        Comparator<String> comparator = mock(Comparator.class);
        when(sortedMap.comparator()).thenReturn(comparator);

        // When
        Comparator<String> result = transformedSortedMap.comparator();

        // Then
        assertSame(comparator, result);
    }

    @Test
    public void testFirstKey() {
        // Given
        String firstKey = "firstKey";
        when(sortedMap.firstKey()).thenReturn(firstKey);

        // When
        String result = transformedSortedMap.firstKey();

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
        SortedMap<String, String> result = transformedSortedMap.headMap(toKey);

        // Then
        assertNotNull(result);
        assertNotSame(headMap, result);
    }

    @Test
    public void testLastKey() {
        // Given
        String lastKey = "lastKey";
        when(sortedMap.lastKey()).thenReturn(lastKey);

        // When
        String result = transformedSortedMap.lastKey();

        // Then
        assertEquals(lastKey, result);
    }

    @Test
    public void testSubMap() {
        // Given
        String fromKey = "fromKey";
        String toKey = "toKey";
        SortedMap<String, String> subMap = mock(SortedMap.class);
        when(sortedMap.subMap(fromKey, toKey)).thenReturn(subMap);

        // When
        SortedMap<String, String> result = transformedSortedMap.subMap(fromKey, toKey);

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
        SortedMap<String, String> result = transformedSortedMap.tailMap(fromKey);

        // Then
        assertNotNull(result);
        assertNotSame(tailMap, result);
    }

    @Test
    public void testTransformedSortedMap_NullMap() {
        // Given
        SortedMap<String, String> nullMap = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> TransformedSortedMap.transformedSortedMap(nullMap, keyTransformer, valueTransformer));
    }

    @Test
    public void testTransformingSortedMap_NullMap() {
        // Given
        SortedMap<String, String> nullMap = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> TransformedSortedMap.transformingSortedMap(nullMap, keyTransformer, valueTransformer));
    }
}
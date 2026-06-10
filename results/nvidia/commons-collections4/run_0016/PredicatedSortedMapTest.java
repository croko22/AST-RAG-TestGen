import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NotNullPredicate;
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
public class PredicatedSortedMapTest {

    @Mock
    private SortedMap<String, String> sortedMap;

    @Mock
    private Predicate<String> keyPredicate;

    @Mock
    private Predicate<String> valuePredicate;

    private PredicatedSortedMap<String, String> predicatedSortedMap;

    @BeforeEach
    void setup() {
        predicatedSortedMap = PredicatedSortedMap.predicatedSortedMap(sortedMap, keyPredicate, valuePredicate);
    }

    @Test
    public void testPredicatedSortedMap() {
        // Given
        when(sortedMap.isEmpty()).thenReturn(true);

        // When
        PredicatedSortedMap<String, String> result = PredicatedSortedMap.predicatedSortedMap(sortedMap, keyPredicate, valuePredicate);

        // Then
        assertNotNull(result);
        assertSame(sortedMap, result.getSortedMap());
        assertSame(keyPredicate, result.getKeyPredicate());
        assertSame(valuePredicate, result.getValuePredicate());
    }

    @Test
    public void testComparator() {
        // Given
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };
        when(sortedMap.comparator()).thenReturn(comparator);

        // When
        Comparator<String> result = predicatedSortedMap.comparator();

        // Then
        assertSame(comparator, result);
    }

    @Test
    public void testFirstKey() {
        // Given
        String firstKey = "firstKey";
        when(sortedMap.firstKey()).thenReturn(firstKey);

        // When
        String result = predicatedSortedMap.firstKey();

        // Then
        assertEquals(firstKey, result);
    }

    @Test
    public void testHeadMap() {
        // Given
        String toKey = "toKey";
        SortedMap<String, String> headMap = new TreeMap<>();
        when(sortedMap.headMap(toKey)).thenReturn(headMap);

        // When
        SortedMap<String, String> result = predicatedSortedMap.headMap(toKey);

        // Then
        assertNotSame(headMap, result);
        assertTrue(result instanceof PredicatedSortedMap);
    }

    @Test
    public void testLastKey() {
        // Given
        String lastKey = "lastKey";
        when(sortedMap.lastKey()).thenReturn(lastKey);

        // When
        String result = predicatedSortedMap.lastKey();

        // Then
        assertEquals(lastKey, result);
    }

    @Test
    public void testSubMap() {
        // Given
        String fromKey = "fromKey";
        String toKey = "toKey";
        SortedMap<String, String> subMap = new TreeMap<>();
        when(sortedMap.subMap(fromKey, toKey)).thenReturn(subMap);

        // When
        SortedMap<String, String> result = predicatedSortedMap.subMap(fromKey, toKey);

        // Then
        assertNotSame(subMap, result);
        assertTrue(result instanceof PredicatedSortedMap);
    }

    @Test
    public void testTailMap() {
        // Given
        String fromKey = "fromKey";
        SortedMap<String, String> tailMap = new TreeMap<>();
        when(sortedMap.tailMap(fromKey)).thenReturn(tailMap);

        // When
        SortedMap<String, String> result = predicatedSortedMap.tailMap(fromKey);

        // Then
        assertNotSame(tailMap, result);
        assertTrue(result instanceof PredicatedSortedMap);
    }

    @Test
    public void testPredicatedSortedMap_NullMap() {
        // Given
        SortedMap<String, String> nullMap = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> PredicatedSortedMap.predicatedSortedMap(nullMap, keyPredicate, valuePredicate));
    }

    @Test
    public void testPredicatedSortedMap_NullKeyPredicate() {
        // Given
        Predicate<String> nullKeyPredicate = null;

        // When
        PredicatedSortedMap<String, String> result = PredicatedSortedMap.predicatedSortedMap(sortedMap, nullKeyPredicate, valuePredicate);

        // Then
        assertNotNull(result);
        assertSame(sortedMap, result.getSortedMap());
        assertSame(nullKeyPredicate, result.getKeyPredicate());
        assertSame(valuePredicate, result.getValuePredicate());
    }

    @Test
    public void testPredicatedSortedMap_NullValuePredicate() {
        // Given
        Predicate<String> nullValuePredicate = null;

        // When
        PredicatedSortedMap<String, String> result = PredicatedSortedMap.predicatedSortedMap(sortedMap, keyPredicate, nullValuePredicate);

        // Then
        assertNotNull(result);
        assertSame(sortedMap, result.getSortedMap());
        assertSame(keyPredicate, result.getKeyPredicate());
        assertSame(nullValuePredicate, result.getValuePredicate());
    }

    @Test
    public void testPredicatedSortedMap_NotNullPredicate() {
        // Given
        Predicate<String> notNullPredicate = NotNullPredicate.notNullPredicate();

        // When
        PredicatedSortedMap<String, String> result = PredicatedSortedMap.predicatedSortedMap(sortedMap, notNullPredicate, notNullPredicate);

        // Then
        assertNotNull(result);
        assertSame(sortedMap, result.getSortedMap());
        assertSame(notNullPredicate, result.getKeyPredicate());
        assertSame(notNullPredicate, result.getValuePredicate());
    }
}
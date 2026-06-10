import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.OrderedBidiMap;
import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.OrderedMapIterator;
import org.apache.commons.collections4.ResettableIterator;
import org.apache.commons.collections4.SortedBidiMap;
import org.apache.commons.collections4.bidimap.DualTreeBidiMap;
import org.apache.commons.collections4.map.AbstractSortedMapDecorator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DualTreeBidiMapTest {

    @Mock
    private Comparator<String> keyComparator;

    @Mock
    private Comparator<String> valueComparator;

    private DualTreeBidiMap<String, String> dualTreeBidiMap;

    @BeforeEach
    void setup() {
        dualTreeBidiMap = new DualTreeBidiMap<>(keyComparator, valueComparator);
    }

    @AfterEach
    void tearDown() {
        dualTreeBidiMap.clear();
    }

    @Test
    void testFirstKey() {
        // Given
        dualTreeBidiMap.put("key1", "value1");
        dualTreeBidiMap.put("key2", "value2");

        // When
        String firstKey = dualTreeBidiMap.firstKey();

        // Then
        assertNotNull(firstKey);
        assertEquals("key1", firstKey);
    }

    @Test
    void testLastKey() {
        // Given
        dualTreeBidiMap.put("key1", "value1");
        dualTreeBidiMap.put("key2", "value2");

        // When
        String lastKey = dualTreeBidiMap.lastKey();

        // Then
        assertNotNull(lastKey);
        assertEquals("key2", lastKey);
    }

    @Test
    void testNextKey() {
        // Given
        dualTreeBidiMap.put("key1", "value1");
        dualTreeBidiMap.put("key2", "value2");

        // When
        String nextKey = dualTreeBidiMap.nextKey("key1");

        // Then
        assertNotNull(nextKey);
        assertEquals("key2", nextKey);
    }

    @Test
    void testPreviousKey() {
        // Given
        dualTreeBidiMap.put("key1", "value1");
        dualTreeBidiMap.put("key2", "value2");

        // When
        String previousKey = dualTreeBidiMap.previousKey("key2");

        // Then
        assertNotNull(previousKey);
        assertEquals("key1", previousKey);
    }

    @Test
    void testHeadMap() {
        // Given
        dualTreeBidiMap.put("key1", "value1");
        dualTreeBidiMap.put("key2", "value2");

        // When
        Map<String, String> headMap = dualTreeBidiMap.headMap("key2");

        // Then
        assertNotNull(headMap);
        assertEquals(1, headMap.size());
        assertTrue(headMap.containsKey("key1"));
    }

    @Test
    void testSubMap() {
        // Given
        dualTreeBidiMap.put("key1", "value1");
        dualTreeBidiMap.put("key2", "value2");
        dualTreeBidiMap.put("key3", "value3");

        // When
        Map<String, String> subMap = dualTreeBidiMap.subMap("key1", "key3");

        // Then
        assertNotNull(subMap);
        assertEquals(2, subMap.size());
        assertTrue(subMap.containsKey("key1"));
        assertTrue(subMap.containsKey("key2"));
    }

    @Test
    void testTailMap() {
        // Given
        dualTreeBidiMap.put("key1", "value1");
        dualTreeBidiMap.put("key2", "value2");

        // When
        Map<String, String> tailMap = dualTreeBidiMap.tailMap("key1");

        // Then
        assertNotNull(tailMap);
        assertEquals(2, tailMap.size());
        assertTrue(tailMap.containsKey("key1"));
        assertTrue(tailMap.containsKey("key2"));
    }

    @Test
    void testComparator() {
        // Given
        dualTreeBidiMap.put("key1", "value1");
        dualTreeBidiMap.put("key2", "value2");

        // When
        Comparator<String> comparator = dualTreeBidiMap.comparator();

        // Then
        assertNotNull(comparator);
    }

    @Test
    void testValueComparator() {
        // Given
        dualTreeBidiMap.put("key1", "value1");
        dualTreeBidiMap.put("key2", "value2");

        // When
        Comparator<String> valueComparator = dualTreeBidiMap.valueComparator();

        // Then
        assertNotNull(valueComparator);
    }

    @Test
    void testMapIterator() {
        // Given
        dualTreeBidiMap.put("key1", "value1");
        dualTreeBidiMap.put("key2", "value2");

        // When
        OrderedMapIterator<String, String> mapIterator = dualTreeBidiMap.mapIterator();

        // Then
        assertNotNull(mapIterator);
    }

    @Test
    void testInverseBidiMap() {
        // Given
        dualTreeBidiMap.put("key1", "value1");
        dualTreeBidiMap.put("key2", "value2");

        // When
        SortedBidiMap<String, String> inverseBidiMap = dualTreeBidiMap.inverseBidiMap();

        // Then
        assertNotNull(inverseBidiMap);
    }

    @Test
    void testContainsValue() {
        // Given
        dualTreeBidiMap.put("key1", "value1");
        dualTreeBidiMap.put("key2", "value2");

        // When
        boolean containsValue = dualTreeBidiMap.containsValue("value1");

        // Then
        assertTrue(containsValue);
    }

    @Test
    void testClear() {
        // Given
        dualTreeBidiMap.put("key1", "value1");
        dualTreeBidiMap.put("key2", "value2");

        // When
        dualTreeBidiMap.clear();

        // Then
        assertTrue(dualTreeBidiMap.isEmpty());
    }
}
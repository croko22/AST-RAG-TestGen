import net.hydromatic.morel.util.Static;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StaticTest {

    @Mock
    private List<String> list;

    @Mock
    private Map<String, String> map;

    @BeforeEach
    void setup() {
        // Initialize mocks if necessary
    }

    @Test
    void testShorterThan() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertTrue(Static.shorterThan(list, 4));
        assertFalse(Static.shorterThan(list, 2));
    }

    @Test
    void testLast() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertEquals(3, Static.last(list));
    }

    @Test
    void testSkip() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> skippedList = Static.skip(list);
        assertEquals(Arrays.asList(2, 3), skippedList);
    }

    @Test
    void testSkipWithCount() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> skippedList = Static.skip(list, 2);
        assertEquals(Arrays.asList(3), skippedList);
    }

    @Test
    void testSkipLast() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> skippedList = Static.skipLast(list);
        assertEquals(Arrays.asList(1, 2), skippedList);
    }

    @Test
    void testSkipLastWithCount() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> skippedList = Static.skipLast(list, 2);
        assertEquals(Arrays.asList(1), skippedList);
    }

    @Test
    void testAppend() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> appendedList = Static.append(list, 4);
        assertEquals(Arrays.asList(1, 2, 3, 4), appendedList);
    }

    @Test
    void testPlus() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> plusList = Static.plus(0, list);
        assertEquals(Arrays.asList(0, 1, 2, 3), plusList);
    }

    @Test
    void testMinus() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> minusList = Static.minus(list, 2);
        assertEquals(Arrays.asList(1, 3), minusList);
    }

    @Test
    void testPlusMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        Map<String, String> plusMap = Static.plus(map, "key2", "value2");
        assertEquals(2, plusMap.size());
    }

    @Test
    void testPlusSortedMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        Map<String, String> plusMap = Static.plus(new java.util.TreeMap<>(map), "key2", "value2");
        assertEquals(2, plusMap.size());
    }

    @Test
    void testNextPowerOfTwo() {
        assertEquals(2, Static.nextPowerOfTwo(1));
        assertEquals(4, Static.nextPowerOfTwo(2));
        assertEquals(8, Static.nextPowerOfTwo(3));
    }

    @Test
    void testAllMatch() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertTrue(Static.allMatch(list, x -> x > 0));
        assertFalse(Static.allMatch(list, x -> x > 1));
    }

    @Test
    void testAnyMatch() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertTrue(Static.anyMatch(list, x -> x > 1));
        assertFalse(Static.anyMatch(list, x -> x > 3));
    }

    @Test
    void testNoneMatch() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertTrue(Static.noneMatch(list, x -> x > 3));
        assertFalse(Static.noneMatch(list, x -> x > 1));
    }

    @Test
    void testTransform() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<String> transformedList = Static.transform(list, x -> x.toString());
        assertEquals(Arrays.asList("1", "2", "3"), transformedList);
    }

    @Test
    void testTransformEager() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<String> transformedList = Static.transformEager(list, x -> x.toString());
        assertEquals(Arrays.asList("1", "2", "3"), transformedList);
    }

    @Test
    void testFilterEager() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> filteredList = Static.filterEager(list, x -> x > 1);
        assertEquals(Arrays.asList(2, 3), filteredList);
    }

    @Test
    void testTransformValuesEager() {
        Map<String, Integer> map = new HashMap<>();
        map.put("key1", 1);
        map.put("key2", 2);
        Map<String, String> transformedMap = Static.transformValuesEager(map, x -> x.toString());
        assertEquals(2, transformedMap.size());
    }

    @Test
    void testTransformToMap() {
        List<String> list = Arrays.asList("key1", "key2");
        Map<String, String> transformedMap = Static.transformToMap(list, (x, builder) -> builder.put(x, x));
        assertEquals(2, transformedMap.size());
    }

    @Test
    void testFind() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        int index = Static.find(list, x -> x > 1);
        assertEquals(1, index);
    }

    @Test
    void testIntersect() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(2, 3, 4);
        List<Integer> intersectList = Static.intersect(list1, list2);
        assertEquals(Arrays.asList(2, 3), intersectList);
    }

    @Test
    void testForEachInIntersection() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(2, 3, 4);
        List<Integer> intersectList = new ArrayList<>();
        Static.forEachInIntersection(list1, list2, intersectList::add);
        assertEquals(Arrays.asList(2, 3), intersectList);
    }

    @Test
    void testStr() {
        StringBuilder builder = new StringBuilder("Hello");
        String str = Static.str(builder);
        assertEquals("Hello", str);
        assertEquals(0, builder.length());
    }

    @Test
    void testEndsWith() {
        StringBuilder builder = new StringBuilder("Hello World");
        assertTrue(Static.endsWith(builder, "World"));
        assertFalse(Static.endsWith(builder, "Universe"));
    }

    @Test
    void testUnmodifiable() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> unmodifiableList = Static.unmodifiable(list);
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.add(4));
    }

    @Test
    void testSplitQuoted() {
        String str = "a,'b,c',d";
        List<String> splitList = Static.splitQuoted(str, ',', '\'');
        assertEquals(Arrays.asList("a", "b,c", "d"), splitList);
    }

    @Test
    void testJoinQuoted() {
        List<String> list = Arrays.asList("a", "b,c", "d");
        String joinedStr = Static.joinQuoted(list, ',', '\'');
        assertEquals("a,'b,c',d", joinedStr);
    }
}
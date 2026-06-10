import net.hydromatic.morel.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PairTest {

    @InjectMocks
    private Pair<String, Integer> pair;

    @Mock
    private BiConsumer<String, Integer> biConsumer;

    @Mock
    private BiPredicate<String, Integer> biPredicate;

    @BeforeEach
    void setup() {
        pair = new Pair<>("test", 1);
    }

    @Test
    void testOf() {
        Pair<String, Integer> pair = Pair.of("test", 1);
        assertEquals("test", pair.getKey());
        assertEquals(1, pair.getValue());
    }

    @Test
    void testOfMapEntry() {
        Map.Entry<String, Integer> entry = new HashMap.SimpleEntry<>("test", 1);
        Pair<String, Integer> pair = Pair.of(entry);
        assertEquals("test", pair.getKey());
        assertEquals(1, pair.getValue());
    }

    @Test
    void testEquals() {
        Pair<String, Integer> pair1 = new Pair<>("test", 1);
        Pair<String, Integer> pair2 = new Pair<>("test", 1);
        assertTrue(pair1.equals(pair2));
    }

    @Test
    void testHashCode() {
        Pair<String, Integer> pair1 = new Pair<>("test", 1);
        Pair<String, Integer> pair2 = new Pair<>("test", 1);
        assertEquals(pair1.hashCode(), pair2.hashCode());
    }

    @Test
    void testCompareTo() {
        Pair<String, Integer> pair1 = new Pair<>("test", 1);
        Pair<String, Integer> pair2 = new Pair<>("test", 1);
        assertEquals(0, pair1.compareTo(pair2));
    }

    @Test
    void testToString() {
        Pair<String, Integer> pair = new Pair<>("test", 1);
        assertEquals("test=1", pair.toString());
    }

    @Test
    void testGetKey() {
        Pair<String, Integer> pair = new Pair<>("test", 1);
        assertEquals("test", pair.getKey());
    }

    @Test
    void testGetValue() {
        Pair<String, Integer> pair = new Pair<>("test", 1);
        assertEquals(1, pair.getValue());
    }

    @Test
    void testSetValue() {
        assertThrows(UnsupportedOperationException.class, () -> pair.setValue(2));
    }

    @Test
    void testToMap() {
        List<Pair<String, Integer>> pairs = new ArrayList<>();
        pairs.add(new Pair<>("test1", 1));
        pairs.add(new Pair<>("test2", 2));
        Map<String, Integer> map = Pair.toMap(pairs);
        assertEquals(2, map.size());
        assertEquals(1, map.get("test1"));
        assertEquals(2, map.get("test2"));
    }

    @Test
    void testZip() {
        List<String> keys = new ArrayList<>();
        keys.add("test1");
        keys.add("test2");
        List<Integer> values = new ArrayList<>();
        values.add(1);
        values.add(2);
        List<Pair<String, Integer>> pairs = Pair.zip(keys, values);
        assertEquals(2, pairs.size());
        assertEquals("test1", pairs.get(0).getKey());
        assertEquals(1, pairs.get(0).getValue());
        assertEquals("test2", pairs.get(1).getKey());
        assertEquals(2, pairs.get(1).getValue());
    }

    @Test
    void testZipStrict() {
        List<String> keys = new ArrayList<>();
        keys.add("test1");
        keys.add("test2");
        List<Integer> values = new ArrayList<>();
        values.add(1);
        values.add(2);
        List<Pair<String, Integer>> pairs = Pair.zip(keys, values, true);
        assertEquals(2, pairs.size());
        assertEquals("test1", pairs.get(0).getKey());
        assertEquals(1, pairs.get(0).getValue());
        assertEquals("test2", pairs.get(1).getKey());
        assertEquals(2, pairs.get(1).getValue());
    }

    @Test
    void testAllMatch() {
        List<String> keys = new ArrayList<>();
        keys.add("test1");
        keys.add("test2");
        List<Integer> values = new ArrayList<>();
        values.add(1);
        values.add(2);
        BiPredicate<String, Integer> predicate = (k, v) -> k.startsWith("test") && v > 0;
        assertTrue(Pair.allMatch(keys, values, predicate));
    }

    @Test
    void testNoneMatch() {
        List<String> keys = new ArrayList<>();
        keys.add("test1");
        keys.add("test2");
        List<Integer> values = new ArrayList<>();
        values.add(1);
        values.add(2);
        BiPredicate<String, Integer> predicate = (k, v) -> k.startsWith("wrong") && v > 0;
        assertTrue(Pair.noneMatch(keys, values, predicate));
    }

    @Test
    void testAnyMatch() {
        List<String> keys = new ArrayList<>();
        keys.add("test1");
        keys.add("test2");
        List<Integer> values = new ArrayList<>();
        values.add(1);
        values.add(2);
        BiPredicate<String, Integer> predicate = (k, v) -> k.startsWith("test") && v > 0;
        assertTrue(Pair.anyMatch(keys, values, predicate));
    }

    @Test
    void testFirstMatch() {
        List<String> keys = new ArrayList<>();
        keys.add("test1");
        keys.add("test2");
        List<Integer> values = new ArrayList<>();
        values.add(1);
        values.add(2);
        BiPredicate<String, Integer> predicate = (k, v) -> k.startsWith("test") && v > 0;
        assertEquals(0, Pair.firstMatch(keys, values, predicate));
    }

    @Test
    void testForEach() {
        List<String> keys = new ArrayList<>();
        keys.add("test1");
        keys.add("test2");
        List<Integer> values = new ArrayList<>();
        values.add(1);
        values.add(2);
        BiConsumer<String, Integer> consumer = (k, v) -> {
            // do something
        };
        Pair.forEach(keys, values, consumer);
        verify(biConsumer, times(2)).accept(any(), any());
    }

    @Test
    void testForEachIndexed() {
        List<String> keys = new ArrayList<>();
        keys.add("test1");
        keys.add("test2");
        List<Integer> values = new ArrayList<>();
        values.add(1);
        values.add(2);
        PairWithOrdinalConsumer<String, Integer> consumer = (i, k, v) -> {
            // do something
        };
        Pair.forEachIndexed(keys, values, consumer);
    }

    @Test
    void testZipIterable() {
        List<String> keys = new ArrayList<>();
        keys.add("test1");
        keys.add("test2");
        List<Integer> values = new ArrayList<>();
        values.add(1);
        values.add(2);
        Iterable<Pair<String, Integer>> pairs = Pair.zip(keys, values);
        // do something with pairs
    }

    @Test
    void testLeft() {
        List<Pair<String, Integer>> pairs = new ArrayList<>();
        pairs.add(new Pair<>("test1", 1));
        pairs.add(new Pair<>("test2", 2));
        List<String> left = Pair.left(pairs);
        assertEquals(2, left.size());
        assertEquals("test1", left.get(0));
        assertEquals("test2", left.get(1));
    }

    @Test
    void testRight() {
        List<Pair<String, Integer>> pairs = new ArrayList<>();
        pairs.add(new Pair<>("test1", 1));
        pairs.add(new Pair<>("test2", 2));
        List<Integer> right = Pair.right(pairs);
        assertEquals(2, right.size());
        assertEquals(1, right.get(0).intValue());
        assertEquals(2, right.get(1).intValue());
    }

    @Test
    void testAdjacents() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        Iterable<Pair<Integer, Integer>> pairs = Pair.adjacents(list);
        // do something with pairs
    }

    @Test
    void testFirstAnd() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        Iterable<Pair<Integer, Integer>> pairs = Pair.firstAnd(list);
        // do something with pairs
    }
}
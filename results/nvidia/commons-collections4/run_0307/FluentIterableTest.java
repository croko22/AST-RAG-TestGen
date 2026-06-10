import org.apache.commons.collections4.FluentIterable;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.ComparatorUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FluentIterableTest {

    @Mock
    private Predicate<Integer> predicate;

    @Mock
    private Transformer<Integer, String> transformer;

    @Mock
    private Closure<Integer> closure;

    @Mock
    private Comparator<Integer> comparator;

    private List<Integer> list;

    @BeforeEach
    public void setup() {
        list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
    }

    @AfterEach
    public void tearDown() {
        list.clear();
    }

    @Test
    public void testEmpty() {
        FluentIterable<Integer> emptyIterable = FluentIterable.empty();
        assertTrue(emptyIterable.isEmpty());
        assertEquals(0, emptyIterable.size());
    }

    @Test
    public void testOfIterable() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        assertEquals(list.size(), iterable.size());
        Iterator<Integer> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            assertTrue(list.contains(iterator.next()));
        }
    }

    @Test
    public void testOfSingleton() {
        FluentIterable<Integer> singletonIterable = FluentIterable.of(1);
        assertEquals(1, singletonIterable.size());
        Iterator<Integer> iterator = singletonIterable.iterator();
        assertEquals(1, (int) iterator.next());
    }

    @Test
    public void testAllMatch() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        when(predicate.evaluate(1)).thenReturn(true);
        when(predicate.evaluate(2)).thenReturn(true);
        when(predicate.evaluate(3)).thenReturn(true);
        when(predicate.evaluate(4)).thenReturn(true);
        when(predicate.evaluate(5)).thenReturn(true);
        assertTrue(iterable.allMatch(predicate));
    }

    @Test
    public void testAnyMatch() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        when(predicate.evaluate(1)).thenReturn(true);
        when(predicate.evaluate(2)).thenReturn(false);
        when(predicate.evaluate(3)).thenReturn(false);
        when(predicate.evaluate(4)).thenReturn(false);
        when(predicate.evaluate(5)).thenReturn(false);
        assertTrue(iterable.anyMatch(predicate));
    }

    @Test
    public void testAppend() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        FluentIterable<Integer> appendedIterable = iterable.append(6, 7, 8);
        assertEquals(8, appendedIterable.size());
    }

    @Test
    public void testAsEnumeration() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        Enumeration<Integer> enumeration = iterable.asEnumeration();
        while (enumeration.hasMoreElements()) {
            assertTrue(list.contains(enumeration.nextElement()));
        }
    }

    @Test
    public void testCollate() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        FluentIterable<Integer> otherIterable = FluentIterable.of(Arrays.asList(6, 7, 8));
        FluentIterable<Integer> collatedIterable = iterable.collate(otherIterable);
        assertEquals(10, collatedIterable.size());
    }

    @Test
    public void testCollateWithComparator() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        FluentIterable<Integer> otherIterable = FluentIterable.of(Arrays.asList(6, 7, 8));
        FluentIterable<Integer> collatedIterable = iterable.collate(otherIterable, comparator);
        assertEquals(10, collatedIterable.size());
    }

    @Test
    public void testContains() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        assertTrue(iterable.contains(1));
        assertFalse(iterable.contains(6));
    }

    @Test
    public void testCopyInto() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        List<Integer> destinationList = new ArrayList<>();
        iterable.copyInto(destinationList);
        assertEquals(list.size(), destinationList.size());
        assertTrue(destinationList.containsAll(list));
    }

    @Test
    public void testEval() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        FluentIterable<Integer> evaluatedIterable = iterable.eval();
        assertEquals(list.size(), evaluatedIterable.size());
    }

    @Test
    public void testFilter() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        when(predicate.evaluate(1)).thenReturn(true);
        when(predicate.evaluate(2)).thenReturn(false);
        when(predicate.evaluate(3)).thenReturn(true);
        when(predicate.evaluate(4)).thenReturn(false);
        when(predicate.evaluate(5)).thenReturn(true);
        FluentIterable<Integer> filteredIterable = iterable.filter(predicate);
        assertEquals(3, filteredIterable.size());
    }

    @Test
    public void testForEach() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        iterable.forEach(closure);
        verify(closure, times(5)).execute(any(Integer.class));
    }

    @Test
    public void testGet() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        assertEquals(1, (int) iterable.get(0));
        assertEquals(2, (int) iterable.get(1));
        assertEquals(3, (int) iterable.get(2));
        assertEquals(4, (int) iterable.get(3));
        assertEquals(5, (int) iterable.get(4));
    }

    @Test
    public void testIsEmpty() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        assertFalse(iterable.isEmpty());
        FluentIterable<Integer> emptyIterable = FluentIterable.empty();
        assertTrue(emptyIterable.isEmpty());
    }

    @Test
    public void testIterator() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        Iterator<Integer> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            assertTrue(list.contains(iterator.next()));
        }
    }

    @Test
    public void testLimit() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        FluentIterable<Integer> limitedIterable = iterable.limit(3);
        assertEquals(3, limitedIterable.size());
    }

    @Test
    public void testLoop() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        FluentIterable<Integer> loopedIterable = iterable.loop();
        Iterator<Integer> iterator = loopedIterable.iterator();
        for (int i = 0; i < 10; i++) {
            assertTrue(list.contains(iterator.next()));
        }
    }

    @Test
    public void testReverse() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        FluentIterable<Integer> reversedIterable = iterable.reverse();
        Iterator<Integer> iterator = reversedIterable.iterator();
        assertEquals(5, (int) iterator.next());
        assertEquals(4, (int) iterator.next());
        assertEquals(3, (int) iterator.next());
        assertEquals(2, (int) iterator.next());
        assertEquals(1, (int) iterator.next());
    }

    @Test
    public void testSize() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        assertEquals(list.size(), iterable.size());
    }

    @Test
    public void testSkip() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        FluentIterable<Integer> skippedIterable = iterable.skip(2);
        assertEquals(3, skippedIterable.size());
    }

    @Test
    public void testToArray() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        Integer[] array = iterable.toArray(Integer.class);
        assertEquals(list.size(), array.length);
        assertTrue(Arrays.asList(array).containsAll(list));
    }

    @Test
    public void testToList() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        List<Integer> listFromIterable = iterable.toList();
        assertEquals(list.size(), listFromIterable.size());
        assertTrue(listFromIterable.containsAll(list));
    }

    @Test
    public void testToString() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        String toString = iterable.toString();
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("2"));
        assertTrue(toString.contains("3"));
        assertTrue(toString.contains("4"));
        assertTrue(toString.contains("5"));
    }

    @Test
    public void testTransform() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        when(transformer.transform(1)).thenReturn("1");
        when(transformer.transform(2)).thenReturn("2");
        when(transformer.transform(3)).thenReturn("3");
        when(transformer.transform(4)).thenReturn("4");
        when(transformer.transform(5)).thenReturn("5");
        FluentIterable<String> transformedIterable = iterable.transform(transformer);
        assertEquals(5, transformedIterable.size());
    }

    @Test
    public void testUnique() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        FluentIterable<Integer> uniqueIterable = iterable.unique();
        assertEquals(list.size(), uniqueIterable.size());
    }

    @Test
    public void testUnmodifiable() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        FluentIterable<Integer> unmodifiableIterable = iterable.unmodifiable();
        assertEquals(list.size(), unmodifiableIterable.size());
    }

    @Test
    public void testZip() {
        FluentIterable<Integer> iterable = FluentIterable.of(list);
        FluentIterable<Integer> otherIterable = FluentIterable.of(Arrays.asList(6, 7, 8));
        FluentIterable<Integer> zippedIterable = iterable.zip(otherIterable);
        assertEquals(8, zippedIterable.size());
    }
}
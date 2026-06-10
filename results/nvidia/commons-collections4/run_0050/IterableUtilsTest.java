import org.apache.commons.collections4.FluentIterable;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IterableUtilsTest {

    @Mock
    private Iterable<String> iterable;

    @Mock
    private Predicate<String> predicate;

    @Mock
    private Comparator<String> comparator;

    @BeforeEach
    public void setup() {
        // Initialize mocks
        iterable = new ArrayList<>();
        predicate = s -> true;
        comparator = (s1, s2) -> s1.compareTo(s2);
    }

    @Test
    public void testBoundedIterable() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c");
        when(iterable.iterator()).thenReturn(list.iterator());
        int maxSize = 2;

        // When
        Iterable<String> boundedIterable = IterableUtils.boundedIterable(iterable, maxSize);

        // Then
        Iterator<String> iterator = boundedIterable.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testChainedIterable() {
        // Given
        List<String> list1 = Arrays.asList("a", "b");
        List<String> list2 = Arrays.asList("c", "d");
        when(iterable.iterator()).thenReturn(list1.iterator());

        // When
        Iterable<String> chainedIterable = IterableUtils.chainedIterable(iterable, list2);

        // Then
        Iterator<String> iterator = chainedIterable.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("c", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("d", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testCollatedIterable() {
        // Given
        List<String> list1 = Arrays.asList("a", "c");
        List<String> list2 = Arrays.asList("b", "d");
        when(iterable.iterator()).thenReturn(list1.iterator());

        // When
        Iterable<String> collatedIterable = IterableUtils.collatedIterable(comparator, iterable, list2);

        // Then
        Iterator<String> iterator = collatedIterable.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("c", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("d", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testContains() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c");
        when(iterable.iterator()).thenReturn(list.iterator());
        String object = "b";

        // When
        boolean contains = IterableUtils.contains(iterable, object);

        // Then
        assertTrue(contains);
    }

    @Test
    public void testCountMatches() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c", "b");
        when(iterable.iterator()).thenReturn(list.iterator());
        when(predicate.test(any())).thenReturn(true);

        // When
        long countMatches = IterableUtils.countMatches(iterable, predicate);

        // Then
        assertEquals(4, countMatches);
    }

    @Test
    public void testDuplicateList() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c", "b");
        when(iterable.iterator()).thenReturn(list.iterator());

        // When
        List<String> duplicateList = IterableUtils.duplicateList(iterable);

        // Then
        assertEquals(Arrays.asList("b"), duplicateList);
    }

    @Test
    public void testEmptyIfNull() {
        // Given
        when(iterable.iterator()).thenReturn(null);

        // When
        Iterable<String> emptyIfNull = IterableUtils.emptyIfNull(iterable);

        // Then
        assertNotNull(emptyIfNull);
    }

    @Test
    public void testFilteredIterable() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c");
        when(iterable.iterator()).thenReturn(list.iterator());
        when(predicate.test(any())).thenReturn(true);

        // When
        Iterable<String> filteredIterable = IterableUtils.filteredIterable(iterable, predicate);

        // Then
        Iterator<String> iterator = filteredIterable.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("c", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testFind() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c");
        when(iterable.iterator()).thenReturn(list.iterator());
        when(predicate.test(any())).thenReturn(true);

        // When
        String find = IterableUtils.find(iterable, predicate);

        // Then
        assertEquals("a", find);
    }

    @Test
    public void testFirst() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c");
        when(iterable.iterator()).thenReturn(list.iterator());

        // When
        String first = IterableUtils.first(iterable);

        // Then
        assertEquals("a", first);
    }

    @Test
    public void testForEach() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c");
        when(iterable.iterator()).thenReturn(list.iterator());

        // When
        IterableUtils.forEach(iterable, s -> {
            // Then
            assertNotNull(s);
        });
    }

    @Test
    public void testForEachButLast() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c");
        when(iterable.iterator()).thenReturn(list.iterator());

        // When
        String forEachButLast = IterableUtils.forEachButLast(iterable, s -> {
            // Then
            assertNotNull(s);
        });

        // Then
        assertEquals("c", forEachButLast);
    }

    @Test
    public void testFrequency() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c", "b");
        when(iterable.iterator()).thenReturn(list.iterator());
        String obj = "b";

        // When
        int frequency = IterableUtils.frequency(iterable, obj);

        // Then
        assertEquals(2, frequency);
    }

    @Test
    public void testGet() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c");
        when(iterable.iterator()).thenReturn(list.iterator());
        int index = 1;

        // When
        String get = IterableUtils.get(iterable, index);

        // Then
        assertEquals("b", get);
    }

    @Test
    public void testIndexOf() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c");
        when(iterable.iterator()).thenReturn(list.iterator());
        when(predicate.test(any())).thenReturn(true);

        // When
        int indexOf = IterableUtils.indexOf(iterable, predicate);

        // Then
        assertEquals(0, indexOf);
    }

    @Test
    public void testIsEmpty() {
        // Given
        when(iterable.iterator()).thenReturn(null);

        // When
        boolean isEmpty = IterableUtils.isEmpty(iterable);

        // Then
        assertTrue(isEmpty);
    }

    @Test
    public void testLoopingIterable() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c");
        when(iterable.iterator()).thenReturn(list.iterator());

        // When
        Iterable<String> loopingIterable = IterableUtils.loopingIterable(iterable);

        // Then
        Iterator<String> iterator = loopingIterable.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("c", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next());
    }

    @Test
    public void testMatchesAll() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c");
        when(iterable.iterator()).thenReturn(list.iterator());
        when(predicate.test(any())).thenReturn(true);

        // When
        boolean matchesAll = IterableUtils.matchesAll(iterable, predicate);

        // Then
        assertTrue(matchesAll);
    }

    @Test
    public void testMatchesAny() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c");
        when(iterable.iterator()).thenReturn(list.iterator());
        when(predicate.test(any())).thenReturn(true);

        // When
        boolean matchesAny = IterableUtils.matchesAny(iterable, predicate);

        // Then
        assertTrue(matchesAny);
    }

    @Test
    public void testPartition() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c");
        when(iterable.iterator()).thenReturn(list.iterator());
        when(predicate.test(any())).thenReturn(true);

        // When
        List<List<String>> partition = IterableUtils.partition(iterable, predicate);

        // Then
        assertNotNull(partition);
    }

    @Test
    public void testReversedIterable() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c");
        when(iterable.iterator()).thenReturn(list.iterator());

        // When
        Iterable<String> reversedIterable = IterableUtils.reversedIterable(iterable);

        // Then
        Iterator<String> iterator = reversedIterable.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("c", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSize() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c");
        when(iterable.iterator()).thenReturn(list.iterator());

        // When
        int size = IterableUtils.size(iterable);

        // Then
        assertEquals(3, size);
    }

    @Test
    public void testSkippingIterable() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c");
        when(iterable.iterator()).thenReturn(list.iterator());
        long elementsToSkip = 1;

        // When
        Iterable<String> skippingIterable = IterableUtils.skippingIterable(iterable, elementsToSkip);

        // Then
        Iterator<String> iterator = skippingIterable.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("c", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testToList() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c");
        when(iterable.iterator()).thenReturn(list.iterator());

        // When
        List<String> toList = IterableUtils.toList(iterable);

        // Then
        assertNotNull(toList);
    }

    @Test
    public void testToString() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c");
        when(iterable.iterator()).thenReturn(list.iterator());

        // When
        String toString = IterableUtils.toString(iterable);

        // Then
        assertNotNull(toString);
    }

    @Test
    public void testTransformedIterable() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c");
        when(iterable.iterator()).thenReturn(list.iterator());

        // When
        Iterable<String> transformedIterable = IterableUtils.transformedIterable(iterable, s -> s + "1");

        // Then
        Iterator<String> iterator = transformedIterable.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("a1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("b1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("c1", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testUniqueIterable() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c", "b");
        when(iterable.iterator()).thenReturn(list.iterator());

        // When
        Iterable<String> uniqueIterable = IterableUtils.uniqueIterable(iterable);

        // Then
        Iterator<String> iterator = uniqueIterable.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("c", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testUnmodifiableIterable() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c");
        when(iterable.iterator()).thenReturn(list.iterator());

        // When
        Iterable<String> unmodifiableIterable = IterableUtils.unmodifiableIterable(iterable);

        // Then
        assertNotNull(unmodifiableIterable);
    }

    @Test
    public void testZippingIterable() {
        // Given
        List<String> list1 = Arrays.asList("a", "b", "c");
        List<String> list2 = Arrays.asList("1", "2", "3");
        when(iterable.iterator()).thenReturn(list1.iterator());

        // When
        Iterable<String> zippingIterable = IterableUtils.zippingIterable(iterable, list2);

        // Then
        Iterator<String> iterator = zippingIterable.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("c", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("3", iterator.next());
        assertFalse(iterator.hasNext());
    }
}
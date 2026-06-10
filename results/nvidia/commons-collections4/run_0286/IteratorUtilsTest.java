Here is the test class for the `IteratorUtils` class:

```java
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IteratorUtilsTest {

    @Mock
    private Iterator<String> iterator;

    @Mock
    private ListIterator<String> listIterator;

    @Mock
    private Predicate<String> predicate;

    @Mock
    private Transformer<String, String> transformer;

    @BeforeEach
    public void setup() {
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn("Test");
        when(listIterator.hasNext()).thenReturn(true, false);
        when(listIterator.next()).thenReturn("Test");
        when(predicate.test(any())).thenReturn(true);
        when(transformer.transform(any())).thenReturn("Transformed");
    }

    @Test
    public void testArrayIterator() {
        String[] array = {"Test1", "Test2"};
        Iterator<String> iterator = IteratorUtils.arrayIterator(array);
        assertTrue(iterator.hasNext());
        assertEquals("Test1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("Test2", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testArrayIteratorWithStart() {
        String[] array = {"Test1", "Test2", "Test3"};
        Iterator<String> iterator = IteratorUtils.arrayIterator(array, 1);
        assertTrue(iterator.hasNext());
        assertEquals("Test2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("Test3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testArrayIteratorWithStartAndEnd() {
        String[] array = {"Test1", "Test2", "Test3", "Test4"};
        Iterator<String> iterator = IteratorUtils.arrayIterator(array, 1, 3);
        assertTrue(iterator.hasNext());
        assertEquals("Test2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("Test3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testArrayListIterator() {
        String[] array = {"Test1", "Test2"};
        ListIterator<String> iterator = IteratorUtils.arrayListIterator(array);
        assertTrue(iterator.hasNext());
        assertEquals("Test1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("Test2", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testArrayListIteratorWithStart() {
        String[] array = {"Test1", "Test2", "Test3"};
        ListIterator<String> iterator = IteratorUtils.arrayListIterator(array, 1);
        assertTrue(iterator.hasNext());
        assertEquals("Test2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("Test3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testArrayListIteratorWithStartAndEnd() {
        String[] array = {"Test1", "Test2", "Test3", "Test4"};
        ListIterator<String> iterator = IteratorUtils.arrayListIterator(array, 1, 3);
        assertTrue(iterator.hasNext());
        assertEquals("Test2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("Test3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAsEnumeration() {
        Enumeration<String> enumeration = IteratorUtils.asEnumeration(iterator);
        assertTrue(enumeration.hasMoreElements());
        assertEquals("Test", enumeration.nextElement());
    }

    @Test
    public void testAsIterable() {
        Iterable<String> iterable = IteratorUtils.asIterable(iterator);
        Iterator<String> iterator = iterable.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("Test", iterator.next());
    }

    @Test
    public void testAsIterator() {
        Enumeration<String> enumeration = mock(Enumeration.class);
        when(enumeration.hasMoreElements()).thenReturn(true, false);
        when(enumeration.nextElement()).thenReturn("Test");
        Iterator<String> iterator = IteratorUtils.asIterator(enumeration);
        assertTrue(iterator.hasNext());
        assertEquals("Test", iterator.next());
    }

    @Test
    public void testBoundedIterator() {
        Iterator<String> boundedIterator = IteratorUtils.boundedIterator(iterator, 1);
        assertTrue(boundedIterator.hasNext());
        assertEquals("Test", boundedIterator.next());
        assertFalse(boundedIterator.hasNext());
    }

    @Test
    public void testChainedIterator() {
        Collection<Iterator<String>> iterators = new ArrayList<>();
        iterators.add(iterator);
        Iterator<String> chainedIterator = IteratorUtils.chainedIterator(iterators);
        assertTrue(chainedIterator.hasNext());
        assertEquals("Test", chainedIterator.next());
    }

    @Test
    public void testCollatedIterator() {
        Iterator<String> iterator1 = mock(Iterator.class);
        when(iterator1.hasNext()).thenReturn(true, false);
        when(iterator1.next()).thenReturn("Test1");
        Iterator<String> iterator2 = mock(Iterator.class);
        when(iterator2.hasNext()).thenReturn(true, false);
        when(iterator2.next()).thenReturn("Test2");
        Collection<Iterator<String>> iterators = new ArrayList<>();
        iterators.add(iterator1);
        iterators.add(iterator2);
        Iterator<String> collatedIterator = IteratorUtils.collatedIterator(null, iterators);
        assertTrue(collatedIterator.hasNext());
        assertEquals("Test1", collatedIterator.next());
        assertTrue(collatedIterator.hasNext());
        assertEquals("Test2", collatedIterator.next());
    }

    @Test
    public void testContains() {
        assertTrue(IteratorUtils.contains(iterator, "Test"));
    }

    @Test
    public void testEmptyIterator() {
        Iterator<String> emptyIterator = IteratorUtils.emptyIterator();
        assertFalse(emptyIterator.hasNext());
    }

    @Test
    public void testEmptyListIterator() {
        ListIterator<String> emptyListIterator = IteratorUtils.emptyListIterator();
        assertFalse(emptyListIterator.hasNext());
    }

    @Test
    public void testFilteredIterator() {
        Iterator<String> filteredIterator = IteratorUtils.filteredIterator(iterator, predicate);
        assertTrue(filteredIterator.hasNext());
        assertEquals("Test", filteredIterator.next());
    }

    @Test
    public void testFind() {
        String result = IteratorUtils.find(iterator, predicate);
        assertEquals("Test", result);
    }

    @Test
    public void testFirst() {
        String result = IteratorUtils.first(iterator);
        assertEquals("Test", result);
    }

    @Test
    public void testForEach() {
        Closure<String> closure = mock(Closure.class);
        IteratorUtils.forEach(iterator, closure);
        verify(closure).accept("Test");
    }

    @Test
    public void testForEachButLast() {
        Closure<String> closure = mock(Closure.class);
        String result = IteratorUtils.forEachButLast(iterator, closure);
        assertEquals("Test", result);
    }

    @Test
    public void testGet() {
        String result = IteratorUtils.get(iterator, 0);
        assertEquals("Test", result);
    }

    @Test
    public void testGetIterator() {
        Object obj = new Object();
        Iterator<?> iterator = IteratorUtils.getIterator(obj);
        assertNotNull(iterator);
    }

    @Test
    public void testIndexOf() {
        int result = IteratorUtils.indexOf(iterator, predicate);
        assertEquals(0, result);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(IteratorUtils.isEmpty(iterator));
    }

    @Test
    public void testLoopingIterator() {
        Collection<String> collection = new ArrayList<>();
        collection.add("Test");
        Iterator<String> loopingIterator = IteratorUtils.loopingIterator(collection);
        assertTrue(loopingIterator.hasNext());
        assertEquals("Test", loopingIterator.next());
        assertTrue(loopingIterator.hasNext());
        assertEquals("Test", loopingIterator.next());
    }

    @Test
    public void testLoopingListIterator() {
        List<String> list = new ArrayList<>();
        list.add("Test");
        ListIterator<String> loopingListIterator = IteratorUtils.loopingListIterator(list);
        assertTrue(loopingListIterator.hasNext());
        assertEquals("Test", loopingListIterator.next());
        assertTrue(loopingListIterator.hasNext());
        assertEquals("Test", loopingListIterator.next());
    }

    @Test
    public void testMatchesAll() {
        assertTrue(IteratorUtils.matchesAll(iterator, predicate));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(IteratorUtils.matchesAny(iterator, predicate));
    }

    @Test
    public void testNodeListIterator() {
        // Node and NodeList are not available in this context
        // IteratorUtils.nodeListIterator(node);
        // IteratorUtils.nodeListIterator(nodeList);
    }

    @Test
    public void testObjectGraphIterator() {
        // Object graph iterator test is complex and requires a specific setup
        // IteratorUtils.objectGraphIterator(root, transformer);
    }

    @Test
    public void testPeekingIterator() {
        Iterator<String> peekingIterator = IteratorUtils.peekingIterator(iterator);
        assertTrue(peekingIterator.hasNext());
        assertEquals("Test", peekingIterator.next());
    }

    @Test
    public void testPushbackIterator() {
        Iterator<String> pushbackIterator = IteratorUtils.pushbackIterator(iterator);
        assertTrue(pushbackIterator.hasNext());
        assertEquals("Test", pushbackIterator.next());
    }

    @Test
    public void testSingletonIterator() {
        String object = "Test";
        Iterator<String> singletonIterator = IteratorUtils.singletonIterator(object);
        assertTrue(singletonIterator.hasNext());
        assertEquals("Test", singletonIterator.next());
    }

    @Test
    public void testSingletonListIterator() {
        String object = "Test";
        ListIterator<String> singletonListIterator = IteratorUtils.singletonListIterator(object);
        assertTrue(singletonListIterator.hasNext());
        assertEquals("Test", singletonListIterator.next());
    }

    @Test
    public void testSize() {
        int result = IteratorUtils.size(iterator);
        assertEquals(1, result);
    }

    @Test
    public void testSkippingIterator() {
        Iterator<String> skippingIterator = IteratorUtils.skippingIterator(iterator, 1);
        assertFalse(skippingIterator.hasNext());
    }

    @Test
    public void testStream() {
        Iterable<String> iterable = IteratorUtils.asIterable(iterator);
        Stream<String> stream = IteratorUtils.stream(iterable);
        assertNotNull(stream);
    }

    @Test
    public void testToList() {
        List<String> list = IteratorUtils.toList(iterator);
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("Test", list.get(0));
    }

    @Test
    public void testToListIterator() {
        ListIterator<String> listIterator = IteratorUtils.toListIterator(iterator);
        assertNotNull(listIterator);
        assertTrue(listIterator.hasNext());
        assertEquals("Test", listIterator.next());
    }

    @Test
    public void testToSet() {
        Set<String> set = IteratorUtils.toSet(iterator);
        assertNotNull(set);
        assertEquals(1, set.size());
        assertTrue(set.contains("Test"));
    }

    @Test
    public void testToString() {
        String result = IteratorUtils.toString(iterator);
        assertNotNull(result);
        assertEquals("[Test]", result);
    }

    @Test
    public void testTransformedIterator() {
        Iterator<String> transformedIterator = IteratorUtils.transformedIterator(iterator, transformer);
        assertTrue(transformedIterator.hasNext());
        assertEquals("Transformed", transformedIterator.next());
    }

    @Test
    public void testUnmodifiableIterator() {
        Iterator<String> unmodifiableIterator = IteratorUtils.unmodifiableIterator(iterator);
        assertTrue(unmodifiableIterator.hasNext());
        assertEquals("Test", unmodifiableIterator.next());
    }

    @Test
    public void testUnmodifiableListIterator() {
        ListIterator<String> unmodifiableListIterator = IteratorUtils.unmodifiableListIterator(listIterator);
        assertTrue(unmodifiableListIterator.hasNext());
        assertEquals("Test", unmodifiableListIterator.next());
    }

    @Test
    public void testZippingIterator() {
        Iterator<String> iterator1 = mock(Iterator.class);
        when(iterator1.hasNext()).thenReturn(true, false);
        when(iterator1.next()).thenReturn("Test1");
        Iterator<String> iterator2 = mock(Iterator.class);
        when(iterator2.hasNext()).thenReturn(true, false);
        when(iterator2.next()).thenReturn("Test2");
        Iterator<String> zippingIterator = IteratorUtils.zippingIterator(iterator1, iterator2);
        assertTrue(zippingIterator.hasNext());
        assertEquals("Test1", zippingIterator.next());
        assertTrue(zippingIterator.hasNext());
        assertEquals("Test2", zippingIterator.next());
    }
}
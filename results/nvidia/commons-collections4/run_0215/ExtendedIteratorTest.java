import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.iterators.ExtendedIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExtendedIteratorTest {

    @Mock
    private Iterator<String> iterator;

    @Mock
    private Consumer<String> consumer;

    @Mock
    private Predicate<String> predicate;

    @Mock
    private Function<String, String> function;

    private List<String> list;

    @BeforeEach
    void setup() {
        list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.next()).thenReturn("a", "b", "c");
    }

    @Test
    void testCreate() {
        // Given
        Iterator<String> it = list.iterator();

        // When
        ExtendedIterator<String> extendedIterator = ExtendedIterator.create(it);

        // Then
        assertNotNull(extendedIterator);
        assertTrue(extendedIterator.hasNext());
        assertEquals("a", extendedIterator.next());
    }

    @Test
    void testCreateStream() {
        // Given
        Stream<String> stream = list.stream();

        // When
        ExtendedIterator<String> extendedIterator = ExtendedIterator.create(stream);

        // Then
        assertNotNull(extendedIterator);
        assertTrue(extendedIterator.hasNext());
        assertEquals("a", extendedIterator.next());
    }

    @Test
    void testCreateNoRemove() {
        // Given
        Iterator<String> it = list.iterator();

        // When
        ExtendedIterator<String> extendedIterator = ExtendedIterator.createNoRemove(it);

        // Then
        assertNotNull(extendedIterator);
        assertTrue(extendedIterator.hasNext());
        assertEquals("a", extendedIterator.next());
        assertThrows(UnsupportedOperationException.class, extendedIterator::remove);
    }

    @Test
    void testEmptyIterator() {
        // When
        ExtendedIterator<?> extendedIterator = ExtendedIterator.emptyIterator();

        // Then
        assertNotNull(extendedIterator);
        assertFalse(extendedIterator.hasNext());
    }

    @Test
    void testFlatten() {
        // Given
        Iterator<Iterator<String>> iterators = IteratorUtils.chainedIterator(list.iterator(), list.iterator());

        // When
        ExtendedIterator<String> extendedIterator = ExtendedIterator.flatten(iterators);

        // Then
        assertNotNull(extendedIterator);
        assertTrue(extendedIterator.hasNext());
        assertEquals("a", extendedIterator.next());
    }

    @Test
    void testAndThen() {
        // Given
        Iterator<String> other = list.iterator();

        // When
        ExtendedIterator<String> extendedIterator = ExtendedIterator.create(list.iterator());
        extendedIterator = extendedIterator.andThen(other);

        // Then
        assertNotNull(extendedIterator);
        assertTrue(extendedIterator.hasNext());
        assertEquals("a", extendedIterator.next());
    }

    @Test
    void testFilter() {
        // Given
        when(predicate.test(any())).thenReturn(true, false, true);

        // When
        ExtendedIterator<String> extendedIterator = ExtendedIterator.create(list.iterator());
        extendedIterator = extendedIterator.filter(predicate);

        // Then
        assertNotNull(extendedIterator);
        assertTrue(extendedIterator.hasNext());
        assertEquals("a", extendedIterator.next());
        assertTrue(extendedIterator.hasNext());
        assertEquals("c", extendedIterator.next());
    }

    @Test
    void testForEachRemaining() {
        // Given
        doAnswer(invocation -> {
            Consumer<String> action = invocation.getArgument(0);
            action.accept("a");
            action.accept("b");
            action.accept("c");
        }).when(consumer).accept(any());

        // When
        ExtendedIterator<String> extendedIterator = ExtendedIterator.create(list.iterator());
        extendedIterator.forEachRemaining(consumer);

        // Then
        verify(consumer, times(3)).accept(any());
    }

    @Test
    void testHasNext() {
        // Given
        when(iterator.hasNext()).thenReturn(true, false);

        // When
        ExtendedIterator<String> extendedIterator = ExtendedIterator.create(iterator);

        // Then
        assertTrue(extendedIterator.hasNext());
        assertFalse(extendedIterator.hasNext());
    }

    @Test
    void testMap() {
        // Given
        when(function.apply(any())).thenReturn("x");

        // When
        ExtendedIterator<String> extendedIterator = ExtendedIterator.create(list.iterator());
        ExtendedIterator<String> mappedIterator = extendedIterator.map(function);

        // Then
        assertNotNull(mappedIterator);
        assertTrue(mappedIterator.hasNext());
        assertEquals("x", mappedIterator.next());
    }

    @Test
    void testNext() {
        // Given
        when(iterator.next()).thenReturn("a");

        // When
        ExtendedIterator<String> extendedIterator = ExtendedIterator.create(iterator);

        // Then
        assertEquals("a", extendedIterator.next());
    }

    @Test
    void testRemove() {
        // Given
        doThrow(UnsupportedOperationException.class).when(iterator).remove();

        // When
        ExtendedIterator<String> extendedIterator = ExtendedIterator.createNoRemove(iterator);

        // Then
        assertThrows(UnsupportedOperationException.class, extendedIterator::remove);
    }
}
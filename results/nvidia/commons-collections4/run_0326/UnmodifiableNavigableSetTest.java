import org.apache.commons.collections4.set.UnmodifiableNavigableSet;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnmodifiableNavigableSetTest {

    @Mock
    private NavigableSet<String> navigableSet;

    private UnmodifiableNavigableSet<String> unmodifiableNavigableSet;

    @BeforeEach
    void setup() {
        unmodifiableNavigableSet = new UnmodifiableNavigableSet<>(navigableSet);
    }

    @Test
    public void testUnmodifiableNavigableSet() {
        // Given
        NavigableSet<String> set = mock(NavigableSet.class);
        UnmodifiableNavigableSet<String> unmodifiableSet = UnmodifiableNavigableSet.unmodifiableNavigableSet(set);

        // Then
        assertSame(set, ((UnmodifiableNavigableSet<String>) unmodifiableSet).decorated());
    }

    @Test
    public void testAdd() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableNavigableSet.add("test"));
    }

    @Test
    public void testAddAll() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableNavigableSet.addAll(Collections.singleton("test")));
    }

    @Test
    public void testClear() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableNavigableSet.clear());
    }

    @Test
    public void testDescendingIterator() {
        // Given
        Iterator<String> iterator = mock(Iterator.class);
        when(navigableSet.descendingIterator()).thenReturn(iterator);

        // When
        Iterator<String> result = unmodifiableNavigableSet.descendingIterator();

        // Then
        assertNotSame(iterator, result);
        assertTrue(result instanceof UnmodifiableIterator);
    }

    @Test
    public void testDescendingSet() {
        // Given
        NavigableSet<String> set = mock(NavigableSet.class);
        when(navigableSet.descendingSet()).thenReturn(set);

        // When
        NavigableSet<String> result = unmodifiableNavigableSet.descendingSet();

        // Then
        assertNotSame(set, result);
        assertTrue(result instanceof UnmodifiableNavigableSet);
    }

    @Test
    public void testHeadSet() {
        // Given
        SortedSet<String> set = mock(SortedSet.class);
        when(navigableSet.headSet("test")).thenReturn(set);

        // When
        SortedSet<String> result = unmodifiableNavigableSet.headSet("test");

        // Then
        assertNotSame(set, result);
        assertTrue(result instanceof Unmodifiable);
    }

    @Test
    public void testHeadSetInclusive() {
        // Given
        NavigableSet<String> set = mock(NavigableSet.class);
        when(navigableSet.headSet("test", true)).thenReturn(set);

        // When
        NavigableSet<String> result = unmodifiableNavigableSet.headSet("test", true);

        // Then
        assertNotSame(set, result);
        assertTrue(result instanceof UnmodifiableNavigableSet);
    }

    @Test
    public void testIterator() {
        // Given
        Iterator<String> iterator = mock(Iterator.class);
        when(navigableSet.iterator()).thenReturn(iterator);

        // When
        Iterator<String> result = unmodifiableNavigableSet.iterator();

        // Then
        assertNotSame(iterator, result);
        assertTrue(result instanceof UnmodifiableIterator);
    }

    @Test
    public void testPollFirst() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableNavigableSet.pollFirst());
    }

    @Test
    public void testPollLast() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableNavigableSet.pollLast());
    }

    @Test
    public void testRemove() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableNavigableSet.remove("test"));
    }

    @Test
    public void testRemoveAll() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableNavigableSet.removeAll(Collections.singleton("test")));
    }

    @Test
    public void testRemoveIf() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableNavigableSet.removeIf(any()));
    }

    @Test
    public void testRetainAll() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableNavigableSet.retainAll(Collections.singleton("test")));
    }

    @Test
    public void testSubSet() {
        // Given
        NavigableSet<String> set = mock(NavigableSet.class);
        when(navigableSet.subSet("from", true, "to", true)).thenReturn(set);

        // When
        NavigableSet<String> result = unmodifiableNavigableSet.subSet("from", true, "to", true);

        // Then
        assertNotSame(set, result);
        assertTrue(result instanceof UnmodifiableNavigableSet);
    }

    @Test
    public void testSubSetRange() {
        // Given
        SortedSet<String> set = mock(SortedSet.class);
        when(navigableSet.subSet("from", "to")).thenReturn(set);

        // When
        SortedSet<String> result = unmodifiableNavigableSet.subSet("from", "to");

        // Then
        assertNotSame(set, result);
        assertTrue(result instanceof Unmodifiable);
    }

    @Test
    public void testTailSet() {
        // Given
        SortedSet<String> set = mock(SortedSet.class);
        when(navigableSet.tailSet("from")).thenReturn(set);

        // When
        SortedSet<String> result = unmodifiableNavigableSet.tailSet("from");

        // Then
        assertNotSame(set, result);
        assertTrue(result instanceof Unmodifiable);
    }

    @Test
    public void testTailSetInclusive() {
        // Given
        NavigableSet<String> set = mock(NavigableSet.class);
        when(navigableSet.tailSet("from", true)).thenReturn(set);

        // When
        NavigableSet<String> result = unmodifiableNavigableSet.tailSet("from", true);

        // Then
        assertNotSame(set, result);
        assertTrue(result instanceof UnmodifiableNavigableSet);
    }
}
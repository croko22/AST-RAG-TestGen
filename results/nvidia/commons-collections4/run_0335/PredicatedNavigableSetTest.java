import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.set.PredicatedNavigableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.NavigableSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PredicatedNavigableSetTest {

    @Mock
    private Predicate<Integer> predicate;

    private NavigableSet<Integer> set;

    @BeforeEach
    void setup() {
        set = new TreeSet<>();
        set.add(1);
        set.add(2);
        set.add(3);
    }

    @Test
    void testPredicatedNavigableSet() {
        // Given
        when(predicate.evaluate(any())).thenReturn(true);

        // When
        PredicatedNavigableSet<Integer> predicatedSet = PredicatedNavigableSet.predicatedNavigableSet(set, predicate);

        // Then
        assertNotNull(predicatedSet);
    }

    @Test
    void testPredicatedNavigableSetNullSet() {
        // Given
        when(predicate.evaluate(any())).thenReturn(true);

        // When / Then
        assertThrows(NullPointerException.class, () -> PredicatedNavigableSet.predicatedNavigableSet(null, predicate));
    }

    @Test
    void testPredicatedNavigableSetNullPredicate() {
        // Given

        // When / Then
        assertThrows(NullPointerException.class, () -> PredicatedNavigableSet.predicatedNavigableSet(set, null));
    }

    @Test
    void testPredicatedNavigableSetInvalidElements() {
        // Given
        when(predicate.evaluate(any())).thenReturn(false);

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> PredicatedNavigableSet.predicatedNavigableSet(set, predicate));
    }

    @Test
    void testCeiling() {
        // Given
        when(predicate.evaluate(any())).thenReturn(true);
        PredicatedNavigableSet<Integer> predicatedSet = PredicatedNavigableSet.predicatedNavigableSet(set, predicate);

        // When
        Integer ceiling = predicatedSet.ceiling(2);

        // Then
        assertEquals(2, ceiling);
    }

    @Test
    void testDescendingIterator() {
        // Given
        when(predicate.evaluate(any())).thenReturn(true);
        PredicatedNavigableSet<Integer> predicatedSet = PredicatedNavigableSet.predicatedNavigableSet(set, predicate);

        // When
        Iterator<Integer> iterator = predicatedSet.descendingIterator();

        // Then
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals(3, iterator.next());
        assertEquals(2, iterator.next());
        assertEquals(1, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testDescendingSet() {
        // Given
        when(predicate.evaluate(any())).thenReturn(true);
        PredicatedNavigableSet<Integer> predicatedSet = PredicatedNavigableSet.predicatedNavigableSet(set, predicate);

        // When
        NavigableSet<Integer> descendingSet = predicatedSet.descendingSet();

        // Then
        assertNotNull(descendingSet);
        assertEquals(3, descendingSet.first());
        assertEquals(1, descendingSet.last());
    }

    @Test
    void testFloor() {
        // Given
        when(predicate.evaluate(any())).thenReturn(true);
        PredicatedNavigableSet<Integer> predicatedSet = PredicatedNavigableSet.predicatedNavigableSet(set, predicate);

        // When
        Integer floor = predicatedSet.floor(2);

        // Then
        assertEquals(2, floor);
    }

    @Test
    void testHeadSet() {
        // Given
        when(predicate.evaluate(any())).thenReturn(true);
        PredicatedNavigableSet<Integer> predicatedSet = PredicatedNavigableSet.predicatedNavigableSet(set, predicate);

        // When
        NavigableSet<Integer> headSet = predicatedSet.headSet(2, true);

        // Then
        assertNotNull(headSet);
        assertEquals(1, headSet.first());
        assertEquals(2, headSet.last());
    }

    @Test
    void testHigher() {
        // Given
        when(predicate.evaluate(any())).thenReturn(true);
        PredicatedNavigableSet<Integer> predicatedSet = PredicatedNavigableSet.predicatedNavigableSet(set, predicate);

        // When
        Integer higher = predicatedSet.higher(2);

        // Then
        assertEquals(3, higher);
    }

    @Test
    void testLower() {
        // Given
        when(predicate.evaluate(any())).thenReturn(true);
        PredicatedNavigableSet<Integer> predicatedSet = PredicatedNavigableSet.predicatedNavigableSet(set, predicate);

        // When
        Integer lower = predicatedSet.lower(2);

        // Then
        assertEquals(1, lower);
    }

    @Test
    void testPollFirst() {
        // Given
        when(predicate.evaluate(any())).thenReturn(true);
        PredicatedNavigableSet<Integer> predicatedSet = PredicatedNavigableSet.predicatedNavigableSet(set, predicate);

        // When
        Integer first = predicatedSet.pollFirst();

        // Then
        assertEquals(1, first);
        assertEquals(2, predicatedSet.first());
    }

    @Test
    void testPollLast() {
        // Given
        when(predicate.evaluate(any())).thenReturn(true);
        PredicatedNavigableSet<Integer> predicatedSet = PredicatedNavigableSet.predicatedNavigableSet(set, predicate);

        // When
        Integer last = predicatedSet.pollLast();

        // Then
        assertEquals(3, last);
        assertEquals(2, predicatedSet.last());
    }

    @Test
    void testSubSet() {
        // Given
        when(predicate.evaluate(any())).thenReturn(true);
        PredicatedNavigableSet<Integer> predicatedSet = PredicatedNavigableSet.predicatedNavigableSet(set, predicate);

        // When
        NavigableSet<Integer> subSet = predicatedSet.subSet(1, true, 2, true);

        // Then
        assertNotNull(subSet);
        assertEquals(1, subSet.first());
        assertEquals(2, subSet.last());
    }

    @Test
    void testTailSet() {
        // Given
        when(predicate.evaluate(any())).thenReturn(true);
        PredicatedNavigableSet<Integer> predicatedSet = PredicatedNavigableSet.predicatedNavigableSet(set, predicate);

        // When
        NavigableSet<Integer> tailSet = predicatedSet.tailSet(2, true);

        // Then
        assertNotNull(tailSet);
        assertEquals(2, tailSet.first());
        assertEquals(3, tailSet.last());
    }
}
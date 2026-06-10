import org.apache.commons.collections4.SetUtils;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.commons.collections4.set.PredicatedNavigableSet;
import org.apache.commons.collections4.set.PredicatedSet;
import org.apache.commons.collections4.set.PredicatedSortedSet;
import org.apache.commons.collections4.set.TransformedNavigableSet;
import org.apache.commons.collections4.set.TransformedSet;
import org.apache.commons.collections4.set.TransformedSortedSet;
import org.apache.commons.collections4.set.UnmodifiableNavigableSet;
import org.apache.commons.collections4.set.UnmodifiableSet;
import org.apache.commons.collections4.set.UnmodifiableSortedSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SetUtilsTest {

    @Mock
    private Set<String> setA;

    @Mock
    private Set<String> setB;

    @BeforeEach
    void setup() {
        // Initialize mock sets
        when(setA.contains("A")).thenReturn(true);
        when(setA.contains("B")).thenReturn(false);
        when(setB.contains("A")).thenReturn(false);
        when(setB.contains("B")).thenReturn(true);
    }

    @Test
    void testDifference() {
        // Given
        SetUtils.SetView<String> difference = SetUtils.difference(setA, setB);

        // When
        boolean containsA = difference.contains("A");
        boolean containsB = difference.contains("B");

        // Then
        assertTrue(containsA);
        assertFalse(containsB);
    }

    @Test
    void testDisjunction() {
        // Given
        SetUtils.SetView<String> disjunction = SetUtils.disjunction(setA, setB);

        // When
        boolean containsA = disjunction.contains("A");
        boolean containsB = disjunction.contains("B");

        // Then
        assertTrue(containsA);
        assertTrue(containsB);
    }

    @Test
    void testEmptyIfNull() {
        // Given
        Set<String> nullSet = null;
        Set<String> nonNullSet = new HashSet<>();

        // When
        Set<String> emptyIfNullSet = SetUtils.emptyIfNull(nullSet);
        Set<String> emptyIfNonNullSet = SetUtils.emptyIfNull(nonNullSet);

        // Then
        assertNotNull(emptyIfNullSet);
        assertNotSame(nullSet, emptyIfNullSet);
        assertSame(nonNullSet, emptyIfNonNullSet);
    }

    @Test
    void testEmptySet() {
        // Given
        Set<String> emptySet = SetUtils.emptySet();

        // When
        boolean isEmpty = emptySet.isEmpty();

        // Then
        assertTrue(isEmpty);
    }

    @Test
    void testEmptySortedSet() {
        // Given
        SortedSet<String> emptySortedSet = SetUtils.emptySortedSet();

        // When
        boolean isEmpty = emptySortedSet.isEmpty();

        // Then
        assertTrue(isEmpty);
    }

    @Test
    void testHashCodeForSet() {
        // Given
        Set<String> set = new HashSet<>();
        set.add("A");
        set.add("B");

        // When
        int hashCode = SetUtils.hashCodeForSet(set);

        // Then
        assertNotNull(hashCode);
    }

    @Test
    void testHashSet() {
        // Given
        Set<String> hashSet = SetUtils.hashSet("A", "B");

        // When
        boolean containsA = hashSet.contains("A");
        boolean containsB = hashSet.contains("B");

        // Then
        assertTrue(containsA);
        assertTrue(containsB);
    }

    @Test
    void testIntersection() {
        // Given
        when(setA.contains("A")).thenReturn(true);
        when(setA.contains("B")).thenReturn(true);
        when(setB.contains("A")).thenReturn(true);
        when(setB.contains("B")).thenReturn(false);
        SetUtils.SetView<String> intersection = SetUtils.intersection(setA, setB);

        // When
        boolean containsA = intersection.contains("A");
        boolean containsB = intersection.contains("B");

        // Then
        assertTrue(containsA);
        assertFalse(containsB);
    }

    @Test
    void testIsEqualSet() {
        // Given
        Set<String> set1 = new HashSet<>();
        set1.add("A");
        set1.add("B");
        Set<String> set2 = new HashSet<>();
        set2.add("A");
        set2.add("B");

        // When
        boolean isEqual = SetUtils.isEqualSet(set1, set2);

        // Then
        assertTrue(isEqual);
    }

    @Test
    void testNewIdentityHashSet() {
        // Given
        Set<String> newIdentityHashSet = SetUtils.newIdentityHashSet();

        // When
        boolean isEmpty = newIdentityHashSet.isEmpty();

        // Then
        assertTrue(isEmpty);
    }

    @Test
    void testOrderedSet() {
        // Given
        Set<String> orderedSet = SetUtils.orderedSet(setA);

        // When
        boolean containsA = orderedSet.contains("A");

        // Then
        assertTrue(containsA);
    }

    @Test
    void testPredicatedNavigableSet() {
        // Given
        NavigableSet<String> navigableSet = new TreeSet<>();
        navigableSet.add("A");
        navigableSet.add("B");
        Predicate<String> predicate = s -> s.equals("A");
        SortedSet<String> predicatedNavigableSet = SetUtils.predicatedNavigableSet(navigableSet, predicate);

        // When
        boolean containsA = predicatedNavigableSet.contains("A");
        boolean containsB = predicatedNavigableSet.contains("B");

        // Then
        assertTrue(containsA);
        assertFalse(containsB);
    }

    @Test
    void testPredicatedSet() {
        // Given
        Set<String> set = new HashSet<>();
        set.add("A");
        set.add("B");
        Predicate<String> predicate = s -> s.equals("A");
        Set<String> predicatedSet = SetUtils.predicatedSet(set, predicate);

        // When
        boolean containsA = predicatedSet.contains("A");
        boolean containsB = predicatedSet.contains("B");

        // Then
        assertTrue(containsA);
        assertFalse(containsB);
    }

    @Test
    void testPredicatedSortedSet() {
        // Given
        SortedSet<String> sortedSet = new TreeSet<>();
        sortedSet.add("A");
        sortedSet.add("B");
        Predicate<String> predicate = s -> s.equals("A");
        SortedSet<String> predicatedSortedSet = SetUtils.predicatedSortedSet(sortedSet, predicate);

        // When
        boolean containsA = predicatedSortedSet.contains("A");
        boolean containsB = predicatedSortedSet.contains("B");

        // Then
        assertTrue(containsA);
        assertFalse(containsB);
    }

    @Test
    void testSynchronizedSet() {
        // Given
        Set<String> set = new HashSet<>();
        set.add("A");
        set.add("B");
        Set<String> synchronizedSet = SetUtils.synchronizedSet(set);

        // When
        boolean containsA = synchronizedSet.contains("A");
        boolean containsB = synchronizedSet.contains("B");

        // Then
        assertTrue(containsA);
        assertTrue(containsB);
    }

    @Test
    void testSynchronizedSortedSet() {
        // Given
        SortedSet<String> sortedSet = new TreeSet<>();
        sortedSet.add("A");
        sortedSet.add("B");
        SortedSet<String> synchronizedSortedSet = SetUtils.synchronizedSortedSet(sortedSet);

        // When
        boolean containsA = synchronizedSortedSet.contains("A");
        boolean containsB = synchronizedSortedSet.contains("B");

        // Then
        assertTrue(containsA);
        assertTrue(containsB);
    }

    @Test
    void testTransformedNavigableSet() {
        // Given
        NavigableSet<String> navigableSet = new TreeSet<>();
        navigableSet.add("A");
        navigableSet.add("B");
        Transformer<String, String> transformer = s -> s + " transformed";
        SortedSet<String> transformedNavigableSet = SetUtils.transformedNavigableSet(navigableSet, transformer);

        // When
        boolean containsATransformed = transformedNavigableSet.contains("A transformed");
        boolean containsBTransformed = transformedNavigableSet.contains("B transformed");

        // Then
        assertTrue(containsATransformed);
        assertTrue(containsBTransformed);
    }

    @Test
    void testTransformedSet() {
        // Given
        Set<String> set = new HashSet<>();
        set.add("A");
        set.add("B");
        Transformer<String, String> transformer = s -> s + " transformed";
        Set<String> transformedSet = SetUtils.transformedSet(set, transformer);

        // When
        boolean containsATransformed = transformedSet.contains("A transformed");
        boolean containsBTransformed = transformedSet.contains("B transformed");

        // Then
        assertTrue(containsATransformed);
        assertTrue(containsBTransformed);
    }

    @Test
    void testTransformedSortedSet() {
        // Given
        SortedSet<String> sortedSet = new TreeSet<>();
        sortedSet.add("A");
        sortedSet.add("B");
        Transformer<String, String> transformer = s -> s + " transformed";
        SortedSet<String> transformedSortedSet = SetUtils.transformedSortedSet(sortedSet, transformer);

        // When
        boolean containsATransformed = transformedSortedSet.contains("A transformed");
        boolean containsBTransformed = transformedSortedSet.contains("B transformed");

        // Then
        assertTrue(containsATransformed);
        assertTrue(containsBTransformed);
    }

    @Test
    void testUnion() {
        // Given
        SetUtils.SetView<String> union = SetUtils.union(setA, setB);

        // When
        boolean containsA = union.contains("A");
        boolean containsB = union.contains("B");

        // Then
        assertTrue(containsA);
        assertTrue(containsB);
    }

    @Test
    void testUnmodifiableNavigableSet() {
        // Given
        NavigableSet<String> navigableSet = new TreeSet<>();
        navigableSet.add("A");
        navigableSet.add("B");
        SortedSet<String> unmodifiableNavigableSet = SetUtils.unmodifiableNavigableSet(navigableSet);

        // When
        boolean containsA = unmodifiableNavigableSet.contains("A");
        boolean containsB = unmodifiableNavigableSet.contains("B");

        // Then
        assertTrue(containsA);
        assertTrue(containsB);
    }

    @Test
    void testUnmodifiableSet() {
        // Given
        Set<String> set = new HashSet<>();
        set.add("A");
        set.add("B");
        Set<String> unmodifiableSet = SetUtils.unmodifiableSet(set);

        // When
        boolean containsA = unmodifiableSet.contains("A");
        boolean containsB = unmodifiableSet.contains("B");

        // Then
        assertTrue(containsA);
        assertTrue(containsB);
    }

    @Test
    void testUnmodifiableSortedSet() {
        // Given
        SortedSet<String> sortedSet = new TreeSet<>();
        sortedSet.add("A");
        sortedSet.add("B");
        SortedSet<String> unmodifiableSortedSet = SetUtils.unmodifiableSortedSet(sortedSet);

        // When
        boolean containsA = unmodifiableSortedSet.contains("A");
        boolean containsB = unmodifiableSortedSet.contains("B");

        // Then
        assertTrue(containsA);
        assertTrue(containsB);
    }
}
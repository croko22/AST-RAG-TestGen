import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.bag.UnmodifiableSortedBag;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;
import org.apache.commons.collections4.set.UnmodifiableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnmodifiableSortedBagTest {

    @Mock
    private SortedBag<String> sortedBag;

    private UnmodifiableSortedBag<String> unmodifiableSortedBag;

    @BeforeEach
    void setup() {
        unmodifiableSortedBag = new UnmodifiableSortedBag<>(sortedBag);
    }

    @Test
    void testUnmodifiableSortedBagFactoryMethod() {
        // Given
        when(sortedBag instanceof Unmodifiable).thenReturn(false);

        // When
        SortedBag<String> result = UnmodifiableSortedBag.unmodifiableSortedBag(sortedBag);

        // Then
        assertNotSame(sortedBag, result);
        assertTrue(result instanceof UnmodifiableSortedBag);
    }

    @Test
    void testUnmodifiableSortedBagFactoryMethod_AlreadyUnmodifiable() {
        // Given
        when(sortedBag instanceof Unmodifiable).thenReturn(true);

        // When
        SortedBag<String> result = UnmodifiableSortedBag.unmodifiableSortedBag(sortedBag);

        // Then
        assertSame(sortedBag, result);
    }

    @Test
    void testAdd() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedBag.add("element"));
    }

    @Test
    void testAddWithCount() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedBag.add("element", 1));
    }

    @Test
    void testAddAll() {
        // Given
        Collection<String> collection = new ArrayList<>();

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedBag.addAll(collection));
    }

    @Test
    void testClear() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedBag.clear());
    }

    @Test
    void testIterator() {
        // Given
        Iterator<String> iterator = mock(Iterator.class);

        when(sortedBag.iterator()).thenReturn(iterator);

        // When
        Iterator<String> result = unmodifiableSortedBag.iterator();

        // Then
        assertNotSame(iterator, result);
        assertTrue(result instanceof UnmodifiableIterator);
    }

    @Test
    void testRemove() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedBag.remove("element"));
    }

    @Test
    void testRemoveWithCount() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedBag.remove("element", 1));
    }

    @Test
    void testRemoveAll() {
        // Given
        Collection<String> collection = new ArrayList<>();

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedBag.removeAll(collection));
    }

    @Test
    void testRemoveIf() {
        // Given
        Predicate<String> predicate = mock(Predicate.class);

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedBag.removeIf(predicate));
    }

    @Test
    void testRetainAll() {
        // Given
        Collection<String> collection = new ArrayList<>();

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedBag.retainAll(collection));
    }

    @Test
    void testUniqueSet() {
        // Given
        Set<String> set = mock(Set.class);

        when(sortedBag.uniqueSet()).thenReturn(set);

        // When
        Set<String> result = unmodifiableSortedBag.uniqueSet();

        // Then
        assertNotSame(set, result);
        assertTrue(result instanceof UnmodifiableSet);
    }
}
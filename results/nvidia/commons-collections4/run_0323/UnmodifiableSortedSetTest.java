import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;
import org.apache.commons.collections4.set.UnmodifiableSortedSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnmodifiableSortedSetTest {

    @Mock
    private SortedSet<String> sortedSet;

    private UnmodifiableSortedSet<String> unmodifiableSortedSet;

    @BeforeEach
    public void setup() {
        unmodifiableSortedSet = UnmodifiableSortedSet.unmodifiableSortedSet(sortedSet);
    }

    @Test
    public void testUnmodifiableSortedSet() {
        // Given
        when(sortedSet.isEmpty()).thenReturn(true);

        // When
        boolean result = unmodifiableSortedSet.isEmpty();

        // Then
        assertTrue(result);
        verify(sortedSet, times(1)).isEmpty();
    }

    @Test
    public void testAdd() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedSet.add("element"));
    }

    @Test
    public void testAddAll() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedSet.addAll(new ArrayList<>()));
    }

    @Test
    public void testClear() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedSet.clear());
    }

    @Test
    public void testHeadSet() {
        // Given
        when(sortedSet.headSet(any())).thenReturn(new TreeSet<>());

        // When
        SortedSet<String> result = unmodifiableSortedSet.headSet("element");

        // Then
        assertNotNull(result);
        verify(sortedSet, times(1)).headSet(any());
    }

    @Test
    public void testIterator() {
        // Given
        when(sortedSet.iterator()).thenReturn(new ArrayList<>().iterator());

        // When
        Iterator<String> result = unmodifiableSortedSet.iterator();

        // Then
        assertNotNull(result);
        verify(sortedSet, times(1)).iterator();
    }

    @Test
    public void testRemove() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedSet.remove("element"));
    }

    @Test
    public void testRemoveAll() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedSet.removeAll(new ArrayList<>()));
    }

    @Test
    public void testRemoveIf() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedSet.removeIf(element -> true));
    }

    @Test
    public void testRetainAll() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableSortedSet.retainAll(new ArrayList<>()));
    }

    @Test
    public void testSubSet() {
        // Given
        when(sortedSet.subSet(any(), any())).thenReturn(new TreeSet<>());

        // When
        SortedSet<String> result = unmodifiableSortedSet.subSet("from", "to");

        // Then
        assertNotNull(result);
        verify(sortedSet, times(1)).subSet(any(), any());
    }

    @Test
    public void testTailSet() {
        // Given
        when(sortedSet.tailSet(any())).thenReturn(new TreeSet<>());

        // When
        SortedSet<String> result = unmodifiableSortedSet.tailSet("from");

        // Then
        assertNotNull(result);
        verify(sortedSet, times(1)).tailSet(any());
    }
}
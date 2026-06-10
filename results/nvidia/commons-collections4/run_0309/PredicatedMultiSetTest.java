import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.collection.PredicatedCollection;
import org.apache.commons.collections4.multiset.PredicatedMultiSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PredicatedMultiSetTest {

    @Mock
    private MultiSet<String> multiset;

    @Mock
    private Predicate<String> predicate;

    private PredicatedMultiSet<String> predicatedMultiSet;

    @BeforeEach
    void setup() {
        predicatedMultiSet = PredicatedMultiSet.predicatedMultiSet(multiset, predicate);
    }

    @Test
    void testPredicatedMultiSet() {
        // Given
        String object = "test";
        int count = 1;

        // When
        when(predicate.evaluate(any())).thenReturn(true);
        int result = predicatedMultiSet.add(object, count);

        // Then
        verify(predicate, times(1)).evaluate(object);
        verify(multiset, times(1)).add(object, count);
    }

    @Test
    void testPredicatedMultiSet_InvalidObject() {
        // Given
        String object = "test";
        int count = 1;

        // When
        when(predicate.evaluate(any())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> predicatedMultiSet.add(object, count));

        // Then
        verify(predicate, times(1)).evaluate(object);
        verify(multiset, never()).add(object, count);
    }

    @Test
    void testEntrySet() {
        // Given
        Set<MultiSet.Entry<String>> entrySet = mock(Set.class);

        // When
        when(multiset.entrySet()).thenReturn(entrySet);
        Set<MultiSet.Entry<String>> result = predicatedMultiSet.entrySet();

        // Then
        assertEquals(entrySet, result);
        verify(multiset, times(1)).entrySet();
    }

    @Test
    void testEquals() {
        // Given
        Object object = new Object();

        // When
        when(multiset.equals(object)).thenReturn(true);
        boolean result = predicatedMultiSet.equals(object);

        // Then
        assertTrue(result);
        verify(multiset, times(1)).equals(object);
    }

    @Test
    void testGetCount() {
        // Given
        Object object = new Object();
        int count = 1;

        // When
        when(multiset.getCount(object)).thenReturn(count);
        int result = predicatedMultiSet.getCount(object);

        // Then
        assertEquals(count, result);
        verify(multiset, times(1)).getCount(object);
    }

    @Test
    void testHashCode() {
        // Given
        int hashCode = 1;

        // When
        when(multiset.hashCode()).thenReturn(hashCode);
        int result = predicatedMultiSet.hashCode();

        // Then
        assertEquals(hashCode, result);
        verify(multiset, times(1)).hashCode();
    }

    @Test
    void testRemove() {
        // Given
        Object object = new Object();
        int count = 1;
        int resultCount = 1;

        // When
        when(multiset.remove(object, count)).thenReturn(resultCount);
        int result = predicatedMultiSet.remove(object, count);

        // Then
        assertEquals(resultCount, result);
        verify(multiset, times(1)).remove(object, count);
    }

    @Test
    void testSetCount() {
        // Given
        String object = "test";
        int count = 1;

        // When
        when(predicate.evaluate(any())).thenReturn(true);
        when(multiset.setCount(object, count)).thenReturn(count);
        int result = predicatedMultiSet.setCount(object, count);

        // Then
        verify(predicate, times(1)).evaluate(object);
        verify(multiset, times(1)).setCount(object, count);
        assertEquals(count, result);
    }

    @Test
    void testSetCount_InvalidObject() {
        // Given
        String object = "test";
        int count = 1;

        // When
        when(predicate.evaluate(any())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> predicatedMultiSet.setCount(object, count));

        // Then
        verify(predicate, times(1)).evaluate(object);
        verify(multiset, never()).setCount(object, count);
    }

    @Test
    void testUniqueSet() {
        // Given
        Set<String> uniqueSet = mock(Set.class);

        // When
        when(multiset.uniqueSet()).thenReturn(uniqueSet);
        Set<String> result = predicatedMultiSet.uniqueSet();

        // Then
        assertEquals(uniqueSet, result);
        verify(multiset, times(1)).uniqueSet();
    }
}
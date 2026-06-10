import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.bag.PredicatedBag;
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
public class PredicatedBagTest {

    @Mock
    private Bag<String> bag;

    @Mock
    private Predicate<String> predicate;

    private PredicatedBag<String> predicatedBag;

    @BeforeEach
    public void setup() {
        predicatedBag = PredicatedBag.predicatedBag(bag, predicate);
    }

    @Test
    public void testPredicatedBag() {
        // Given
        Bag<String> bagToDecorate = mock(Bag.class);
        Predicate<String> predicateToUse = mock(Predicate.class);

        // When
        PredicatedBag<String> result = PredicatedBag.predicatedBag(bagToDecorate, predicateToUse);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testAdd() {
        // Given
        String object = "object";
        int count = 1;
        when(predicate.evaluate(any())).thenReturn(true);
        when(bag.add(any(), anyInt())).thenReturn(true);

        // When
        boolean result = predicatedBag.add(object, count);

        // Then
        assertTrue(result);
        verify(predicate, times(1)).evaluate(object);
        verify(bag, times(1)).add(object, count);
    }

    @Test
    public void testAdd_InvalidObject() {
        // Given
        String object = "object";
        int count = 1;
        when(predicate.evaluate(any())).thenReturn(false);

        // When
        assertThrows(IllegalArgumentException.class, () -> predicatedBag.add(object, count));

        // Then
        verify(predicate, times(1)).evaluate(object);
        verify(bag, never()).add(object, count);
    }

    @Test
    public void testEquals() {
        // Given
        Object object = new Object();

        // When
        boolean result = predicatedBag.equals(object);

        // Then
        assertEquals(bag.equals(object), result);
    }

    @Test
    public void testGetCount() {
        // Given
        Object object = new Object();
        when(bag.getCount(any())).thenReturn(1);

        // When
        int result = predicatedBag.getCount(object);

        // Then
        assertEquals(1, result);
        verify(bag, times(1)).getCount(object);
    }

    @Test
    public void testHashCode() {
        // Given
        when(bag.hashCode()).thenReturn(1);

        // When
        int result = predicatedBag.hashCode();

        // Then
        assertEquals(1, result);
        verify(bag, times(1)).hashCode();
    }

    @Test
    public void testRemove() {
        // Given
        Object object = new Object();
        int count = 1;
        when(bag.remove(any(), anyInt())).thenReturn(true);

        // When
        boolean result = predicatedBag.remove(object, count);

        // Then
        assertTrue(result);
        verify(bag, times(1)).remove(object, count);
    }

    @Test
    public void testUniqueSet() {
        // Given
        Set<String> set = mock(Set.class);
        when(bag.uniqueSet()).thenReturn(set);

        // When
        Set<String> result = predicatedBag.uniqueSet();

        // Then
        assertEquals(set, result);
        verify(bag, times(1)).uniqueSet();
    }
}
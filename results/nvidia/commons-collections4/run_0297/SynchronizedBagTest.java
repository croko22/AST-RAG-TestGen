import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.SynchronizedBag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SynchronizedBagTest {

    @Mock
    private Bag<String> bag;

    private SynchronizedBag<String> synchronizedBag;

    @BeforeEach
    void setup() {
        synchronizedBag = SynchronizedBag.synchronizedBag(bag);
    }

    @Test
    void testSynchronizedBag() {
        // Given
        Bag<String> inputBag = mock(Bag.class);

        // When
        SynchronizedBag<String> result = SynchronizedBag.synchronizedBag(inputBag);

        // Then
        assertNotNull(result);
    }

    @Test
    void testAdd() {
        // Given
        String object = "object";
        int count = 1;
        when(bag.add(object, count)).thenReturn(true);

        // When
        boolean result = synchronizedBag.add(object, count);

        // Then
        assertTrue(result);
        verify(bag, times(1)).add(object, count);
    }

    @Test
    void testEquals() {
        // Given
        Object object = new Object();
        when(bag.equals(object)).thenReturn(true);

        // When
        boolean result = synchronizedBag.equals(object);

        // Then
        assertTrue(result);
        verify(bag, times(1)).equals(object);
    }

    @Test
    void testGetCount() {
        // Given
        Object object = new Object();
        when(bag.getCount(object)).thenReturn(1);

        // When
        int result = synchronizedBag.getCount(object);

        // Then
        assertEquals(1, result);
        verify(bag, times(1)).getCount(object);
    }

    @Test
    void testHashCode() {
        // Given
        when(bag.hashCode()).thenReturn(1);

        // When
        int result = synchronizedBag.hashCode();

        // Then
        assertEquals(1, result);
        verify(bag, times(1)).hashCode();
    }

    @Test
    void testRemove() {
        // Given
        Object object = new Object();
        int count = 1;
        when(bag.remove(object, count)).thenReturn(true);

        // When
        boolean result = synchronizedBag.remove(object, count);

        // Then
        assertTrue(result);
        verify(bag, times(1)).remove(object, count);
    }

    @Test
    void testUniqueSet() {
        // Given
        Set<String> set = mock(Set.class);
        when(bag.uniqueSet()).thenReturn(set);

        // When
        Set<String> result = synchronizedBag.uniqueSet();

        // Then
        assertNotNull(result);
        verify(bag, times(1)).uniqueSet();
    }

    @Test
    void testAddNullObject() {
        // Given
        String object = null;
        int count = 1;

        // When and Then
        assertThrows(NullPointerException.class, () -> synchronizedBag.add(object, count));
    }

    @Test
    void testAddNegativeCount() {
        // Given
        String object = "object";
        int count = -1;

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> synchronizedBag.add(object, count));
    }

    @Test
    void testRemoveNullObject() {
        // Given
        Object object = null;
        int count = 1;

        // When and Then
        assertThrows(NullPointerException.class, () -> synchronizedBag.remove(object, count));
    }

    @Test
    void testRemoveNegativeCount() {
        // Given
        Object object = new Object();
        int count = -1;

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> synchronizedBag.remove(object, count));
    }
}
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.multiset.SynchronizedMultiSet;
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
public class SynchronizedMultiSetTest {

    @Mock
    private MultiSet<String> multiset;

    private SynchronizedMultiSet<String> synchronizedMultiSet;

    @BeforeEach
    public void setup() {
        synchronizedMultiSet = SynchronizedMultiSet.synchronizedMultiSet(multiset);
    }

    @Test
    public void testSynchronizedMultiSet() {
        // Given
        when(multiset.add(any(), anyInt())).thenReturn(1);

        // When
        int result = synchronizedMultiSet.add("object", 1);

        // Then
        assertEquals(1, result);
        verify(multiset, times(1)).add("object", 1);
    }

    @Test
    public void testEntrySet() {
        // Given
        Set<MultiSet.Entry<String>> entrySet = new HashSet<>();
        when(multiset.entrySet()).thenReturn(entrySet);

        // When
        Set<MultiSet.Entry<String>> result = synchronizedMultiSet.entrySet();

        // Then
        assertSame(entrySet, result);
        verify(multiset, times(1)).entrySet();
    }

    @Test
    public void testEquals() {
        // Given
        when(multiset.equals(any())).thenReturn(true);

        // When
        boolean result = synchronizedMultiSet.equals(multiset);

        // Then
        assertTrue(result);
        verify(multiset, times(1)).equals(multiset);
    }

    @Test
    public void testGetCount() {
        // Given
        when(multiset.getCount(any())).thenReturn(1);

        // When
        int result = synchronizedMultiSet.getCount("object");

        // Then
        assertEquals(1, result);
        verify(multiset, times(1)).getCount("object");
    }

    @Test
    public void testHashCode() {
        // Given
        when(multiset.hashCode()).thenReturn(1);

        // When
        int result = synchronizedMultiSet.hashCode();

        // Then
        assertEquals(1, result);
        verify(multiset, times(1)).hashCode();
    }

    @Test
    public void testRemove() {
        // Given
        when(multiset.remove(any(), anyInt())).thenReturn(1);

        // When
        int result = synchronizedMultiSet.remove("object", 1);

        // Then
        assertEquals(1, result);
        verify(multiset, times(1)).remove("object", 1);
    }

    @Test
    public void testSetCount() {
        // Given
        when(multiset.setCount(any(), anyInt())).thenReturn(1);

        // When
        int result = synchronizedMultiSet.setCount("object", 1);

        // Then
        assertEquals(1, result);
        verify(multiset, times(1)).setCount("object", 1);
    }

    @Test
    public void testUniqueSet() {
        // Given
        Set<String> uniqueSet = new HashSet<>();
        when(multiset.uniqueSet()).thenReturn(uniqueSet);

        // When
        Set<String> result = synchronizedMultiSet.uniqueSet();

        // Then
        assertSame(uniqueSet, result);
        verify(multiset, times(1)).uniqueSet();
    }

    @Test
    public void testSynchronizedMultiSetFactoryMethod() {
        // Given
        MultiSet<String> multiset = mock(MultiSet.class);

        // When
        SynchronizedMultiSet<String> result = SynchronizedMultiSet.synchronizedMultiSet(multiset);

        // Then
        assertNotNull(result);
        assertSame(multiset, result.decorated());
    }

    @Test
    public void testSynchronizedMultiSetConstructor() {
        // Given
        MultiSet<String> multiset = mock(MultiSet.class);

        // When
        SynchronizedMultiSet<String> result = new SynchronizedMultiSet<>(multiset);

        // Then
        assertNotNull(result);
        assertSame(multiset, result.decorated());
    }
}
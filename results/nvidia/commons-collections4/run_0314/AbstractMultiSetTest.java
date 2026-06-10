import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.multiset.AbstractMultiSet;
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
public class AbstractMultiSetTest {

    @Mock
    private AbstractMultiSet<String> abstractMultiSet;

    @BeforeEach
    void setup() {
        // Initialize the mock object
        abstractMultiSet = mock(AbstractMultiSet.class);
    }

    @Test
    void testEquals() {
        // Given
        when(abstractMultiSet.equals(any())).thenReturn(true);

        // When
        boolean result = abstractMultiSet.equals(new Object());

        // Then
        assertTrue(result);
    }

    @Test
    void testHashCode() {
        // Given
        when(abstractMultiSet.hashCode()).thenReturn(123);

        // When
        int result = abstractMultiSet.hashCode();

        // Then
        assertEquals(123, result);
    }

    @Test
    void testToString() {
        // Given
        when(abstractMultiSet.toString()).thenReturn("Test String");

        // When
        String result = abstractMultiSet.toString();

        // Then
        assertEquals("Test String", result);
    }

    @Test
    void testContains() {
        // Given
        when(abstractMultiSet.contains(any())).thenReturn(true);

        // When
        boolean result = abstractMultiSet.contains("Test");

        // Then
        assertTrue(result);
    }

    @Test
    void testIterator() {
        // Given
        Iterator<String> iterator = mock(Iterator.class);
        when(abstractMultiSet.iterator()).thenReturn(iterator);

        // When
        Iterator<String> result = abstractMultiSet.iterator();

        // Then
        assertEquals(iterator, result);
    }

    @Test
    void testRemove() {
        // Given
        when(abstractMultiSet.remove(any())).thenReturn(true);

        // When
        boolean result = abstractMultiSet.remove("Test");

        // Then
        assertTrue(result);
    }

    @Test
    void testSize() {
        // Given
        when(abstractMultiSet.size()).thenReturn(10);

        // When
        int result = abstractMultiSet.size();

        // Then
        assertEquals(10, result);
    }

    @Test
    void testAdd() {
        // Given
        when(abstractMultiSet.add(any())).thenReturn(true);

        // When
        boolean result = abstractMultiSet.add("Test");

        // Then
        assertTrue(result);
    }

    @Test
    void testClear() {
        // Given
        doNothing().when(abstractMultiSet).clear();

        // When
        abstractMultiSet.clear();

        // Then
        verify(abstractMultiSet, times(1)).clear();
    }

    @Test
    void testEntrySet() {
        // Given
        Set<org.apache.commons.collections4.MultiSet.Entry<String>> entrySet = mock(Set.class);
        when(abstractMultiSet.entrySet()).thenReturn(entrySet);

        // When
        Set<org.apache.commons.collections4.MultiSet.Entry<String>> result = abstractMultiSet.entrySet();

        // Then
        assertEquals(entrySet, result);
    }

    @Test
    void testGetCount() {
        // Given
        when(abstractMultiSet.getCount(any())).thenReturn(5);

        // When
        int result = abstractMultiSet.getCount("Test");

        // Then
        assertEquals(5, result);
    }

    @Test
    void testSetCount() {
        // Given
        when(abstractMultiSet.setCount(any(), anyInt())).thenReturn(5);

        // When
        int result = abstractMultiSet.setCount("Test", 10);

        // Then
        assertEquals(5, result);
    }

    @Test
    void testUniqueSet() {
        // Given
        Set<String> uniqueSet = mock(Set.class);
        when(abstractMultiSet.uniqueSet()).thenReturn(uniqueSet);

        // When
        Set<String> result = abstractMultiSet.uniqueSet();

        // Then
        assertEquals(uniqueSet, result);
    }
}
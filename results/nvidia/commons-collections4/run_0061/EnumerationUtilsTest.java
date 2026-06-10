import org.apache.commons.collections4.EnumerationUtils;
import org.apache.commons.collections4.iterators.EnumerationIterator;
import org.apache.commons.collections4.iterators.IteratorIterable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnumerationUtilsTest {

    @Mock
    private Enumeration<String> enumeration;

    @Mock
    private EnumerationIterator<String> enumerationIterator;

    @Mock
    private IteratorIterable<String> iteratorIterable;

    @BeforeEach
    void setup() {
        // Setup mock behavior
        when(enumeration.hasMoreElements()).thenReturn(true, false);
        when(enumeration.nextElement()).thenReturn("Element1", "Element2");
        when(enumerationIterator.getEnumeration()).thenReturn(enumeration);
        when(iteratorIterable.iterator()).thenReturn(enumerationIterator);
    }

    @Test
    public void testAsIterable() {
        // Given
        Enumeration<String> enumeration = mock(Enumeration.class);
        when(enumeration.hasMoreElements()).thenReturn(true, false);
        when(enumeration.nextElement()).thenReturn("Element1");

        // When
        Iterable<String> iterable = EnumerationUtils.asIterable(enumeration);

        // Then
        assertNotNull(iterable);
        Iterator<String> iterator = iterable.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("Element1", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGet() {
        // Given
        Enumeration<String> enumeration = mock(Enumeration.class);
        when(enumeration.hasMoreElements()).thenReturn(true, true, false);
        when(enumeration.nextElement()).thenReturn("Element1", "Element2");

        // When
        String result = EnumerationUtils.get(enumeration, 1);

        // Then
        assertEquals("Element2", result);
    }

    @Test
    public void testGet_IndexOutOfBoundsException() {
        // Given
        Enumeration<String> enumeration = mock(Enumeration.class);
        when(enumeration.hasMoreElements()).thenReturn(true, true, false);
        when(enumeration.nextElement()).thenReturn("Element1", "Element2");

        // When and Then
        assertThrows(IndexOutOfBoundsException.class, () -> EnumerationUtils.get(enumeration, 2));
    }

    @Test
    public void testToList() {
        // Given
        Enumeration<String> enumeration = mock(Enumeration.class);
        when(enumeration.hasMoreElements()).thenReturn(true, true, false);
        when(enumeration.nextElement()).thenReturn("Element1", "Element2");

        // When
        List<String> list = EnumerationUtils.toList(enumeration);

        // Then
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals("Element1", list.get(0));
        assertEquals("Element2", list.get(1));
    }

    @Test
    public void testToList_StringTokenizer() {
        // Given
        StringTokenizer stringTokenizer = new StringTokenizer("Element1,Element2", ",");

        // When
        List<String> list = EnumerationUtils.toList(stringTokenizer);

        // Then
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals("Element1", list.get(0));
        assertEquals("Element2", list.get(1));
    }

    @Test
    public void testToSet() {
        // Given
        Enumeration<String> enumeration = mock(Enumeration.class);
        when(enumeration.hasMoreElements()).thenReturn(true, true, false);
        when(enumeration.nextElement()).thenReturn("Element1", "Element2");

        // When
        Set<String> set = EnumerationUtils.toSet(enumeration);

        // Then
        assertNotNull(set);
        assertEquals(2, set.size());
        assertTrue(set.contains("Element1"));
        assertTrue(set.contains("Element2"));
    }
}
import org.apache.commons.collections4.iterators.EnumerationIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnumerationIteratorTest {

    @Mock
    private Enumeration<String> enumeration;

    @Mock
    private List<String> collection;

    private EnumerationIterator<String> enumerationIterator;

    @BeforeEach
    public void setup() {
        enumerationIterator = new EnumerationIterator<>(enumeration, collection);
    }

    @Test
    public void testGetEnumeration() {
        // Given
        Enumeration<String> expectedEnumeration = enumeration;

        // When
        Enumeration<String> actualEnumeration = enumerationIterator.getEnumeration();

        // Then
        assertEquals(expectedEnumeration, actualEnumeration);
    }

    @Test
    public void testHasNext_True() {
        // Given
        when(enumeration.hasMoreElements()).thenReturn(true);

        // When
        boolean result = enumerationIterator.hasNext();

        // Then
        assertTrue(result);
        verify(enumeration, times(1)).hasMoreElements();
    }

    @Test
    public void testHasNext_False() {
        // Given
        when(enumeration.hasMoreElements()).thenReturn(false);

        // When
        boolean result = enumerationIterator.hasNext();

        // Then
        assertFalse(result);
        verify(enumeration, times(1)).hasMoreElements();
    }

    @Test
    public void testHasNext_EnumerationIsNull() {
        // Given
        enumerationIterator = new EnumerationIterator<>();

        // When and Then
        assertThrows(NullPointerException.class, () -> enumerationIterator.hasNext());
    }

    @Test
    public void testNext() {
        // Given
        String expectedElement = "Element";
        when(enumeration.nextElement()).thenReturn(expectedElement);

        // When
        String actualElement = enumerationIterator.next();

        // Then
        assertEquals(expectedElement, actualElement);
        verify(enumeration, times(1)).nextElement();
    }

    @Test
    public void testNext_EnumerationIsNull() {
        // Given
        enumerationIterator = new EnumerationIterator<>();

        // When and Then
        assertThrows(NullPointerException.class, () -> enumerationIterator.next());
    }

    @Test
    public void testRemove() {
        // Given
        String element = "Element";
        when(enumeration.nextElement()).thenReturn(element);
        enumerationIterator.next();

        // When
        enumerationIterator.remove();

        // Then
        verify(collection, times(1)).remove(element);
    }

    @Test
    public void testRemove_NoCollection() {
        // Given
        enumerationIterator = new EnumerationIterator<>(enumeration);

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> enumerationIterator.remove());
    }

    @Test
    public void testRemove_NoNextCalled() {
        // Given

        // When and Then
        assertThrows(IllegalStateException.class, () -> enumerationIterator.remove());
    }

    @Test
    public void testSetEnumeration() {
        // Given
        Enumeration<String> newEnumeration = mock(Enumeration.class);

        // When
        enumerationIterator.setEnumeration(newEnumeration);

        // Then
        assertEquals(newEnumeration, enumerationIterator.getEnumeration());
    }
}
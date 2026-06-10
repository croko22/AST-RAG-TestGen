import org.apache.commons.collections4.iterators.IteratorEnumeration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IteratorEnumerationTest {

    @Mock
    private Iterator<String> iteratorMock;

    private IteratorEnumeration<String> iteratorEnumeration;

    @BeforeEach
    void setup() {
        iteratorEnumeration = new IteratorEnumeration<>();
    }

    @Test
    void testGetIterator_WithoutSettingIterator_ReturnsNull() {
        // Given: no iterator set
        // When: get iterator
        Iterator<? extends String> result = iteratorEnumeration.getIterator();
        // Then: returns null
        assertNull(result);
    }

    @Test
    void testGetIterator_WithSettingIterator_ReturnsSameIterator() {
        // Given: set iterator
        iteratorEnumeration.setIterator(iteratorMock);
        // When: get iterator
        Iterator<? extends String> result = iteratorEnumeration.getIterator();
        // Then: returns same iterator
        assertSame(iteratorMock, result);
    }

    @Test
    void testHasMoreElements_WithoutSettingIterator_ThrowsNullPointerException() {
        // Given: no iterator set
        // When / Then: has more elements throws NullPointerException
        assertThrows(NullPointerException.class, () -> iteratorEnumeration.hasMoreElements());
    }

    @Test
    void testHasMoreElements_WithSettingIterator_ReturnsTrue() {
        // Given: set iterator and has next
        iteratorEnumeration.setIterator(iteratorMock);
        when(iteratorMock.hasNext()).thenReturn(true);
        // When: has more elements
        boolean result = iteratorEnumeration.hasMoreElements();
        // Then: returns true
        assertTrue(result);
        verify(iteratorMock, times(1)).hasNext();
    }

    @Test
    void testHasMoreElements_WithSettingIterator_ReturnsFalse() {
        // Given: set iterator and no next
        iteratorEnumeration.setIterator(iteratorMock);
        when(iteratorMock.hasNext()).thenReturn(false);
        // When: has more elements
        boolean result = iteratorEnumeration.hasMoreElements();
        // Then: returns false
        assertFalse(result);
        verify(iteratorMock, times(1)).hasNext();
    }

    @Test
    void testNextElement_WithoutSettingIterator_ThrowsNullPointerException() {
        // Given: no iterator set
        // When / Then: next element throws NullPointerException
        assertThrows(NullPointerException.class, () -> iteratorEnumeration.nextElement());
    }

    @Test
    void testNextElement_WithSettingIterator_AndHasNext_ReturnsNextElement() {
        // Given: set iterator and has next
        iteratorEnumeration.setIterator(iteratorMock);
        when(iteratorMock.hasNext()).thenReturn(true);
        String nextElement = "nextElement";
        when(iteratorMock.next()).thenReturn(nextElement);
        // When: next element
        String result = iteratorEnumeration.nextElement();
        // Then: returns next element
        assertEquals(nextElement, result);
        verify(iteratorMock, times(1)).hasNext();
        verify(iteratorMock, times(1)).next();
    }

    @Test
    void testNextElement_WithSettingIterator_AndNoNext_ThrowsNoSuchElementException() {
        // Given: set iterator and no next
        iteratorEnumeration.setIterator(iteratorMock);
        when(iteratorMock.hasNext()).thenReturn(false);
        // When / Then: next element throws NoSuchElementException
        assertThrows(NoSuchElementException.class, () -> iteratorEnumeration.nextElement());
        verify(iteratorMock, times(1)).hasNext();
    }

    @Test
    void testSetIterator_NullIterator_ThrowsNullPointerException() {
        // Given: null iterator
        // When / Then: set iterator throws NullPointerException
        assertThrows(NullPointerException.class, () -> iteratorEnumeration.setIterator(null));
    }

    @Test
    void testSetIterator_NonNullIterator_SetsIterator() {
        // Given: non-null iterator
        // When: set iterator
        iteratorEnumeration.setIterator(iteratorMock);
        // Then: sets iterator
        assertSame(iteratorMock, iteratorEnumeration.getIterator());
    }
}
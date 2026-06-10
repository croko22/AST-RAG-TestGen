import org.apache.commons.collections4.iterators.IteratorChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IteratorChainTest {

    @Mock
    private Iterator<String> iteratorMock1;

    @Mock
    private Iterator<String> iteratorMock2;

    private IteratorChain<String> iteratorChain;

    @BeforeEach
    void setup() {
        iteratorChain = new IteratorChain<>();
    }

    @Test
    void testAddIterator() {
        // Given
        when(iteratorMock1.hasNext()).thenReturn(true);
        when(iteratorMock1.next()).thenReturn("Element1");

        // When
        iteratorChain.addIterator(iteratorMock1);

        // Then
        assertEquals(1, iteratorChain.size());
        verify(iteratorMock1, never()).hasNext();
        verify(iteratorMock1, never()).next();
    }

    @Test
    void testAddIterator_MultipleIterators() {
        // Given
        when(iteratorMock1.hasNext()).thenReturn(true);
        when(iteratorMock1.next()).thenReturn("Element1");
        when(iteratorMock2.hasNext()).thenReturn(true);
        when(iteratorMock2.next()).thenReturn("Element2");

        // When
        iteratorChain.addIterator(iteratorMock1);
        iteratorChain.addIterator(iteratorMock2);

        // Then
        assertEquals(2, iteratorChain.size());
        verify(iteratorMock1, never()).hasNext();
        verify(iteratorMock1, never()).next();
        verify(iteratorMock2, never()).hasNext();
        verify(iteratorMock2, never()).next();
    }

    @Test
    void testHasNext() {
        // Given
        when(iteratorMock1.hasNext()).thenReturn(true);
        iteratorChain.addIterator(iteratorMock1);

        // When
        boolean result = iteratorChain.hasNext();

        // Then
        assertTrue(result);
        verify(iteratorMock1).hasNext();
    }

    @Test
    void testHasNext_NoElements() {
        // Given
        when(iteratorMock1.hasNext()).thenReturn(false);
        iteratorChain.addIterator(iteratorMock1);

        // When
        boolean result = iteratorChain.hasNext();

        // Then
        assertFalse(result);
        verify(iteratorMock1).hasNext();
    }

    @Test
    void testHasNext_MultipleIterators() {
        // Given
        when(iteratorMock1.hasNext()).thenReturn(true);
        when(iteratorMock2.hasNext()).thenReturn(true);
        iteratorChain.addIterator(iteratorMock1);
        iteratorChain.addIterator(iteratorMock2);

        // When
        boolean result = iteratorChain.hasNext();

        // Then
        assertTrue(result);
        verify(iteratorMock1).hasNext();
        verify(iteratorMock2, never()).hasNext();
    }

    @Test
    void testNext() {
        // Given
        when(iteratorMock1.hasNext()).thenReturn(true);
        when(iteratorMock1.next()).thenReturn("Element1");
        iteratorChain.addIterator(iteratorMock1);

        // When
        String result = iteratorChain.next();

        // Then
        assertEquals("Element1", result);
        verify(iteratorMock1).hasNext();
        verify(iteratorMock1).next();
    }

    @Test
    void testNext_NoElements() {
        // Given
        when(iteratorMock1.hasNext()).thenReturn(false);
        iteratorChain.addIterator(iteratorMock1);

        // When and Then
        assertThrows(NoSuchElementException.class, () -> iteratorChain.next());
        verify(iteratorMock1).hasNext();
    }

    @Test
    void testNext_MultipleIterators() {
        // Given
        when(iteratorMock1.hasNext()).thenReturn(true);
        when(iteratorMock1.next()).thenReturn("Element1");
        when(iteratorMock2.hasNext()).thenReturn(true);
        when(iteratorMock2.next()).thenReturn("Element2");
        iteratorChain.addIterator(iteratorMock1);
        iteratorChain.addIterator(iteratorMock2);

        // When
        String result1 = iteratorChain.next();
        String result2 = iteratorChain.next();

        // Then
        assertEquals("Element1", result1);
        assertEquals("Element2", result2);
        verify(iteratorMock1).hasNext();
        verify(iteratorMock1).next();
        verify(iteratorMock2).hasNext();
        verify(iteratorMock2).next();
    }

    @Test
    void testRemove() {
        // Given
        when(iteratorMock1.hasNext()).thenReturn(true);
        when(iteratorMock1.next()).thenReturn("Element1");
        doThrow(new UnsupportedOperationException()).when(iteratorMock1).remove();
        iteratorChain.addIterator(iteratorMock1);

        // When and Then
        iteratorChain.next();
        assertThrows(UnsupportedOperationException.class, () -> iteratorChain.remove());
        verify(iteratorMock1).hasNext();
        verify(iteratorMock1).next();
        verify(iteratorMock1).remove();
    }

    @Test
    void testSize() {
        // Given
        iteratorChain.addIterator(iteratorMock1);
        iteratorChain.addIterator(iteratorMock2);

        // When
        int result = iteratorChain.size();

        // Then
        assertEquals(2, result);
    }

    @Test
    void testIsLocked() {
        // Given
        assertFalse(iteratorChain.isLocked());

        // When
        iteratorChain.hasNext();

        // Then
        assertTrue(iteratorChain.isLocked());
    }

    @Test
    void testAddIterator_AfterLocking() {
        // Given
        iteratorChain.hasNext();

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> iteratorChain.addIterator(iteratorMock1));
    }
}
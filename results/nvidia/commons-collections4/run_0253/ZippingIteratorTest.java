import org.apache.commons.collections4.FluentIterable;
import org.apache.commons.collections4.iterators.ZippingIterator;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ZippingIteratorTest {

    @Mock
    private Iterator<String> iterator1;

    @Mock
    private Iterator<String> iterator2;

    @Mock
    private Iterator<String> iterator3;

    private ZippingIterator<String> zippingIterator;

    @BeforeEach
    public void setup() {
        zippingIterator = new ZippingIterator<>(iterator1, iterator2, iterator3);
    }

    @Test
    public void testHasNext_NoElements() {
        // Given
        when(iterator1.hasNext()).thenReturn(false);
        when(iterator2.hasNext()).thenReturn(false);
        when(iterator3.hasNext()).thenReturn(false);

        // When
        boolean result = zippingIterator.hasNext();

        // Then
        assertFalse(result);
        verify(iterator1, times(1)).hasNext();
        verify(iterator2, times(1)).hasNext();
        verify(iterator3, times(1)).hasNext();
    }

    @Test
    public void testHasNext_HasElements() {
        // Given
        when(iterator1.hasNext()).thenReturn(true);
        when(iterator2.hasNext()).thenReturn(false);
        when(iterator3.hasNext()).thenReturn(false);

        // When
        boolean result = zippingIterator.hasNext();

        // Then
        assertTrue(result);
        verify(iterator1, times(1)).hasNext();
        verify(iterator2, times(1)).hasNext();
        verify(iterator3, times(1)).hasNext();
    }

    @Test
    public void testNext_NoElements() {
        // Given
        when(iterator1.hasNext()).thenReturn(false);
        when(iterator2.hasNext()).thenReturn(false);
        when(iterator3.hasNext()).thenReturn(false);

        // When and Then
        assertThrows(NoSuchElementException.class, () -> zippingIterator.next());
        verify(iterator1, times(1)).hasNext();
        verify(iterator2, times(1)).hasNext();
        verify(iterator3, times(1)).hasNext();
    }

    @Test
    public void testNext_HasElements() {
        // Given
        when(iterator1.hasNext()).thenReturn(true);
        when(iterator2.hasNext()).thenReturn(false);
        when(iterator3.hasNext()).thenReturn(false);
        when(iterator1.next()).thenReturn("Element1");

        // When
        String result = zippingIterator.next();

        // Then
        assertEquals("Element1", result);
        verify(iterator1, times(1)).hasNext();
        verify(iterator2, times(1)).hasNext();
        verify(iterator3, times(1)).hasNext();
        verify(iterator1, times(1)).next();
    }

    @Test
    public void testRemove_NoElements() {
        // Given
        when(iterator1.hasNext()).thenReturn(false);
        when(iterator2.hasNext()).thenReturn(false);
        when(iterator3.hasNext()).thenReturn(false);

        // When and Then
        assertThrows(NoSuchElementException.class, () -> zippingIterator.next());
        assertThrows(IllegalStateException.class, () -> zippingIterator.remove());
        verify(iterator1, times(1)).hasNext();
        verify(iterator2, times(1)).hasNext();
        verify(iterator3, times(1)).hasNext();
    }

    @Test
    public void testRemove_HasElements() {
        // Given
        when(iterator1.hasNext()).thenReturn(true);
        when(iterator2.hasNext()).thenReturn(false);
        when(iterator3.hasNext()).thenReturn(false);
        when(iterator1.next()).thenReturn("Element1");

        // When
        zippingIterator.next();
        zippingIterator.remove();

        // Then
        verify(iterator1, times(1)).remove();
    }

    @Test
    public void testConstructor_NullIterator() {
        // Given
        Iterator<String> nullIterator = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> new ZippingIterator<>(nullIterator));
    }

    @Test
    public void testConstructor_MultipleIterators() {
        // Given
        Iterator<String> iterator4 = mock(Iterator.class);
        Iterator<String> iterator5 = mock(Iterator.class);

        // When
        ZippingIterator<String> zippingIterator = new ZippingIterator<>(iterator1, iterator2, iterator3, iterator4, iterator5);

        // Then
        assertNotNull(zippingIterator);
    }

    @Test
    public void testConstructor_TwoIterators() {
        // Given
        Iterator<String> iterator4 = mock(Iterator.class);

        // When
        ZippingIterator<String> zippingIterator = new ZippingIterator<>(iterator1, iterator4);

        // Then
        assertNotNull(zippingIterator);
    }

    @Test
    public void testConstructor_ThreeIterators() {
        // Given
        Iterator<String> iterator4 = mock(Iterator.class);
        Iterator<String> iterator5 = mock(Iterator.class);

        // When
        ZippingIterator<String> zippingIterator = new ZippingIterator<>(iterator1, iterator4, iterator5);

        // Then
        assertNotNull(zippingIterator);
    }
}
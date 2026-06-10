import org.apache.commons.collections4.iterators.SkippingIterator;
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
public class SkippingIteratorTest {

    @Mock
    private Iterator<String> iterator;

    private SkippingIterator<String> skippingIterator;

    @BeforeEach
    void setup() {
        skippingIterator = new SkippingIterator<>(iterator, 0);
    }

    @Test
    void testConstructor_OffsetZero() {
        // Given
        Iterator<String> iterator = mock(Iterator.class);
        long offset = 0;

        // When
        SkippingIterator<String> skippingIterator = new SkippingIterator<>(iterator, offset);

        // Then
        assertNotNull(skippingIterator);
    }

    @Test
    void testConstructor_OffsetPositive() {
        // Given
        Iterator<String> iterator = mock(Iterator.class);
        long offset = 5;

        // When
        SkippingIterator<String> skippingIterator = new SkippingIterator<>(iterator, offset);

        // Then
        assertNotNull(skippingIterator);
    }

    @Test
    void testConstructor_NullIterator() {
        // Given
        Iterator<String> iterator = null;
        long offset = 0;

        // When and Then
        assertThrows(NullPointerException.class, () -> new SkippingIterator<>(iterator, offset));
    }

    @Test
    void testConstructor_NegativeOffset() {
        // Given
        Iterator<String> iterator = mock(Iterator.class);
        long offset = -1;

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> new SkippingIterator<>(iterator, offset));
    }

    @Test
    void testNext_NoElements() {
        // Given
        when(iterator.hasNext()).thenReturn(false);

        // When and Then
        assertThrows(NoSuchElementException.class, () -> skippingIterator.next());
    }

    @Test
    void testNext_OneElement() {
        // Given
        List<String> elements = new ArrayList<>();
        elements.add("Element1");
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn("Element1");

        // When
        String next = skippingIterator.next();

        // Then
        assertEquals("Element1", next);
    }

    @Test
    void testNext_MultipleElements() {
        // Given
        List<String> elements = new ArrayList<>();
        elements.add("Element1");
        elements.add("Element2");
        elements.add("Element3");
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.next()).thenReturn("Element1", "Element2", "Element3");

        // When
        String next1 = skippingIterator.next();
        String next2 = skippingIterator.next();
        String next3 = skippingIterator.next();

        // Then
        assertEquals("Element1", next1);
        assertEquals("Element2", next2);
        assertEquals("Element3", next3);
    }

    @Test
    void testRemove_BeforeNext() {
        // Given

        // When and Then
        assertThrows(IllegalStateException.class, () -> skippingIterator.remove());
    }

    @Test
    void testRemove_AfterNext() {
        // Given
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn("Element1");

        // When
        skippingIterator.next();
        skippingIterator.remove();

        // Then
        verify(iterator, times(1)).remove();
    }

    @Test
    void testSkippingIterator_Offset() {
        // Given
        List<String> elements = new ArrayList<>();
        elements.add("Element1");
        elements.add("Element2");
        elements.add("Element3");
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.next()).thenReturn("Element1", "Element2", "Element3");
        SkippingIterator<String> skippingIterator = new SkippingIterator<>(iterator, 1);

        // When
        String next1 = skippingIterator.next();
        String next2 = skippingIterator.next();

        // Then
        assertEquals("Element2", next1);
        assertEquals("Element3", next2);
    }
}
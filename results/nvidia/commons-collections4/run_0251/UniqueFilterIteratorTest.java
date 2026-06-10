import org.apache.commons.collections4.functors.UniquePredicate;
import org.apache.commons.collections4.iterators.FilterIterator;
import org.apache.commons.collections4.iterators.UniqueFilterIterator;
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
public class UniqueFilterIteratorTest {

    @Mock
    private Iterator<String> iterator;

    private UniqueFilterIterator<String> uniqueFilterIterator;

    @BeforeEach
    void setup() {
        uniqueFilterIterator = new UniqueFilterIterator<>(iterator);
    }

    @Test
    void testConstructor() {
        // Given
        Iterator<String> testIterator = mock(Iterator.class);

        // When
        UniqueFilterIterator<String> uniqueFilterIterator = new UniqueFilterIterator<>(testIterator);

        // Then
        assertNotNull(uniqueFilterIterator);
    }

    @Test
    void testHasNext_NoElements() {
        // Given
        when(iterator.hasNext()).thenReturn(false);

        // When
        boolean result = uniqueFilterIterator.hasNext();

        // Then
        assertFalse(result);
        verify(iterator, times(1)).hasNext();
    }

    @Test
    void testHasNext_UniqueElements() {
        // Given
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn("element1");

        // When
        boolean result = uniqueFilterIterator.hasNext();

        // Then
        assertTrue(result);
        verify(iterator, times(1)).hasNext();
    }

    @Test
    void testNext_UniqueElements() {
        // Given
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn("element1");

        // When
        String result = uniqueFilterIterator.next();

        // Then
        assertEquals("element1", result);
        verify(iterator, times(1)).next();
    }

    @Test
    void testNext_DuplicateElements() {
        // Given
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.next()).thenReturn("element1", "element1");

        // When
        String result = uniqueFilterIterator.next();

        // Then
        assertEquals("element1", result);
        verify(iterator, times(2)).next();
        verify(iterator, times(2)).hasNext();
    }

    @Test
    void testNext_NoElements() {
        // Given
        when(iterator.hasNext()).thenReturn(false);

        // When and Then
        assertThrows(NoSuchElementException.class, () -> uniqueFilterIterator.next());
        verify(iterator, times(1)).hasNext();
    }

    @Test
    void testRemove() {
        // Given

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> uniqueFilterIterator.remove());
    }

    @Test
    void testUniquePredicate() {
        // Given
        UniquePredicate<String> uniquePredicate = UniquePredicate.uniquePredicate();

        // When
        boolean result1 = uniquePredicate.test("element1");
        boolean result2 = uniquePredicate.test("element1");
        boolean result3 = uniquePredicate.test("element2");

        // Then
        assertTrue(result1);
        assertFalse(result2);
        assertTrue(result3);
    }

    @Test
    void testUniqueFilterIterator_IteratorWithDuplicates() {
        // Given
        List<String> elements = new ArrayList<>();
        elements.add("element1");
        elements.add("element1");
        elements.add("element2");
        elements.add("element2");
        elements.add("element2");
        Iterator<String> testIterator = elements.iterator();

        UniqueFilterIterator<String> uniqueFilterIterator = new UniqueFilterIterator<>(testIterator);

        // When
        List<String> result = new ArrayList<>();
        while (uniqueFilterIterator.hasNext()) {
            result.add(uniqueFilterIterator.next());
        }

        // Then
        assertEquals(2, result.size());
        assertEquals("element1", result.get(0));
        assertEquals("element2", result.get(1));
    }
}
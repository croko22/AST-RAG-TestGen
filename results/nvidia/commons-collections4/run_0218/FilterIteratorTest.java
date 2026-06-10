import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.apache.commons.collections4.iterators.FilterIterator;
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
public class FilterIteratorTest {

    @Mock
    private Iterator<String> iterator;

    @Mock
    private Predicate<String> predicate;

    private FilterIterator<String> filterIterator;

    @BeforeEach
    public void setup() {
        filterIterator = new FilterIterator<>();
        filterIterator.setIterator(iterator);
        filterIterator.setPredicate(predicate);
    }

    @Test
    public void testGetIterator() {
        // Given
        Iterator<String> expectedIterator = iterator;

        // When
        Iterator<String> actualIterator = filterIterator.getIterator();

        // Then
        assertEquals(expectedIterator, actualIterator);
    }

    @Test
    public void testGetPredicate() {
        // Given
        Predicate<String> expectedPredicate = predicate;

        // When
        Predicate<String> actualPredicate = filterIterator.getPredicate();

        // Then
        assertEquals(expectedPredicate, actualPredicate);
    }

    @Test
    public void testHasNext_PredicateMatches() {
        // Given
        when(iterator.hasNext()).thenReturn(true);
        when(predicate.test(any())).thenReturn(true);

        // When
        boolean hasNext = filterIterator.hasNext();

        // Then
        assertTrue(hasNext);
    }

    @Test
    public void testHasNext_PredicateDoesNotMatch() {
        // Given
        when(iterator.hasNext()).thenReturn(true);
        when(predicate.test(any())).thenReturn(false);

        // When
        boolean hasNext = filterIterator.hasNext();

        // Then
        assertFalse(hasNext);
    }

    @Test
    public void testNext_PredicateMatches() {
        // Given
        String expectedElement = "Element";
        when(iterator.hasNext()).thenReturn(true);
        when(iterator.next()).thenReturn(expectedElement);
        when(predicate.test(any())).thenReturn(true);

        // When
        String actualElement = filterIterator.next();

        // Then
        assertEquals(expectedElement, actualElement);
    }

    @Test
    public void testNext_PredicateDoesNotMatch() {
        // Given
        when(iterator.hasNext()).thenReturn(true);
        when(iterator.next()).thenReturn("Element");
        when(predicate.test(any())).thenReturn(false);

        // When
        assertThrows(NoSuchElementException.class, () -> filterIterator.next());
    }

    @Test
    public void testRemove() {
        // Given
        doNothing().when(iterator).remove();

        // When
        filterIterator.next();
        filterIterator.remove();

        // Then
        verify(iterator).remove();
    }

    @Test
    public void testRemove_ThrowsException() {
        // Given
        doNothing().when(iterator).remove();

        // When and Then
        assertThrows(IllegalStateException.class, () -> filterIterator.remove());
    }

    @Test
    public void testSetIterator() {
        // Given
        Iterator<String> newIterator = mock(Iterator.class);

        // When
        filterIterator.setIterator(newIterator);

        // Then
        assertEquals(newIterator, filterIterator.getIterator());
    }

    @Test
    public void testSetPredicate() {
        // Given
        Predicate<String> newPredicate = mock(Predicate.class);

        // When
        filterIterator.setPredicate(newPredicate);

        // Then
        assertEquals(newPredicate, filterIterator.getPredicate());
    }

    @Test
    public void testSetPredicate_NullPredicate() {
        // Given
        Predicate<String> expectedPredicate = TruePredicate.truePredicate();

        // When
        filterIterator.setPredicate(null);

        // Then
        assertEquals(expectedPredicate, filterIterator.getPredicate());
    }

    @Test
    public void testFilterIterator_Constructor_NoArguments() {
        // Given
        FilterIterator<String> filterIterator = new FilterIterator<>();

        // When and Then
        assertNotNull(filterIterator);
    }

    @Test
    public void testFilterIterator_Constructor_WithIterator() {
        // Given
        Iterator<String> iterator = mock(Iterator.class);
        FilterIterator<String> filterIterator = new FilterIterator<>(iterator);

        // When and Then
        assertEquals(iterator, filterIterator.getIterator());
    }

    @Test
    public void testFilterIterator_Constructor_WithIteratorAndPredicate() {
        // Given
        Iterator<String> iterator = mock(Iterator.class);
        Predicate<String> predicate = mock(Predicate.class);
        FilterIterator<String> filterIterator = new FilterIterator<>(iterator, predicate);

        // When and Then
        assertEquals(iterator, filterIterator.getIterator());
        assertEquals(predicate, filterIterator.getPredicate());
    }
}
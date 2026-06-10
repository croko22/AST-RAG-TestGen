import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.iterators.FilterListIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilterListIteratorTest {

    @Mock
    private ListIterator<String> listIterator;

    @Mock
    private Predicate<String> predicate;

    private FilterListIterator<String> filterListIterator;

    @BeforeEach
    void setup() {
        filterListIterator = new FilterListIterator<>(listIterator, predicate);
    }

    @Test
    void testAdd_ThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> filterListIterator.add("test"));
    }

    @Test
    void testGetListIterator_ReturnsListIterator() {
        assertEquals(listIterator, filterListIterator.getListIterator());
    }

    @Test
    void testGetPredicate_ReturnsPredicate() {
        assertEquals(predicate, filterListIterator.getPredicate());
    }

    @Test
    void testHasNext_ReturnsTrue_WhenNextObjectSet() {
        filterListIterator.nextObject = "test";
        filterListIterator.nextObjectSet = true;
        assertTrue(filterListIterator.hasNext());
    }

    @Test
    void testHasNext_ReturnsTrue_WhenSetNextObjectReturnsTrue() {
        when(predicate.test(any())).thenReturn(true);
        when(listIterator.hasNext()).thenReturn(true);
        when(listIterator.next()).thenReturn("test");
        assertTrue(filterListIterator.hasNext());
    }

    @Test
    void testHasNext_ReturnsFalse_WhenSetNextObjectReturnsFalse() {
        when(predicate.test(any())).thenReturn(false);
        when(listIterator.hasNext()).thenReturn(false);
        assertFalse(filterListIterator.hasNext());
    }

    @Test
    void testHasPrevious_ReturnsTrue_WhenPreviousObjectSet() {
        filterListIterator.previousObject = "test";
        filterListIterator.previousObjectSet = true;
        assertTrue(filterListIterator.hasPrevious());
    }

    @Test
    void testHasPrevious_ReturnsTrue_WhenSetPreviousObjectReturnsTrue() {
        when(predicate.test(any())).thenReturn(true);
        when(listIterator.hasPrevious()).thenReturn(true);
        when(listIterator.previous()).thenReturn("test");
        assertTrue(filterListIterator.hasPrevious());
    }

    @Test
    void testHasPrevious_ReturnsFalse_WhenSetPreviousObjectReturnsFalse() {
        when(predicate.test(any())).thenReturn(false);
        when(listIterator.hasPrevious()).thenReturn(false);
        assertFalse(filterListIterator.hasPrevious());
    }

    @Test
    void testNext_ThrowsNoSuchElementException_WhenNextObjectNotSetAndSetNextObjectReturnsFalse() {
        filterListIterator.nextObjectSet = false;
        when(predicate.test(any())).thenReturn(false);
        when(listIterator.hasNext()).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> filterListIterator.next());
    }

    @Test
    void testNext_ReturnsNextObject_WhenNextObjectSet() {
        filterListIterator.nextObject = "test";
        filterListIterator.nextObjectSet = true;
        assertEquals("test", filterListIterator.next());
    }

    @Test
    void testNextIndex_ReturnsNextIndex() {
        filterListIterator.nextIndex = 10;
        assertEquals(10, filterListIterator.nextIndex());
    }

    @Test
    void testPrevious_ThrowsNoSuchElementException_WhenPreviousObjectNotSetAndSetPreviousObjectReturnsFalse() {
        filterListIterator.previousObjectSet = false;
        when(predicate.test(any())).thenReturn(false);
        when(listIterator.hasPrevious()).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> filterListIterator.previous());
    }

    @Test
    void testPrevious_ReturnsPreviousObject_WhenPreviousObjectSet() {
        filterListIterator.previousObject = "test";
        filterListIterator.previousObjectSet = true;
        assertEquals("test", filterListIterator.previous());
    }

    @Test
    void testPreviousIndex_ReturnsPreviousIndex() {
        filterListIterator.nextIndex = 10;
        assertEquals(9, filterListIterator.previousIndex());
    }

    @Test
    void testRemove_ThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> filterListIterator.remove());
    }

    @Test
    void testSet_ThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> filterListIterator.set("test"));
    }

    @Test
    void testSetListIterator_SetsListIterator() {
        ListIterator<String> newListIterator = mock(ListIterator.class);
        filterListIterator.setListIterator(newListIterator);
        assertEquals(newListIterator, filterListIterator.getListIterator());
    }

    @Test
    void testSetPredicate_SetsPredicate() {
        Predicate<String> newPredicate = mock(Predicate.class);
        filterListIterator.setPredicate(newPredicate);
        assertEquals(newPredicate, filterListIterator.getPredicate());
    }

    @Test
    void testFilterListIterator_ConstructorWithoutArguments() {
        FilterListIterator<String> filterListIterator = new FilterListIterator<>();
        assertNull(filterListIterator.iterator);
        assertNull(filterListIterator.predicate);
    }

    @Test
    void testFilterListIterator_ConstructorWithListIterator() {
        ListIterator<String> listIterator = mock(ListIterator.class);
        FilterListIterator<String> filterListIterator = new FilterListIterator<>(listIterator);
        assertEquals(listIterator, filterListIterator.iterator);
        assertNull(filterListIterator.predicate);
    }

    @Test
    void testFilterListIterator_ConstructorWithListIteratorAndPredicate() {
        ListIterator<String> listIterator = mock(ListIterator.class);
        Predicate<String> predicate = mock(Predicate.class);
        FilterListIterator<String> filterListIterator = new FilterListIterator<>(listIterator, predicate);
        assertEquals(listIterator, filterListIterator.iterator);
        assertEquals(predicate, filterListIterator.predicate);
    }

    @Test
    void testFilterListIterator_ConstructorWithPredicate() {
        Predicate<String> predicate = mock(Predicate.class);
        FilterListIterator<String> filterListIterator = new FilterListIterator<>(predicate);
        assertNull(filterListIterator.iterator);
        assertEquals(predicate, filterListIterator.predicate);
    }
}
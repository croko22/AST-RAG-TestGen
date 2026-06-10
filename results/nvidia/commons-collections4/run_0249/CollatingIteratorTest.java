import org.apache.commons.collections4.iterators.CollatingIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CollatingIteratorTest {

    @Mock
    private Iterator<String> iterator1;

    @Mock
    private Iterator<String> iterator2;

    private CollatingIterator<String> collatingIterator;

    @BeforeEach
    public void setup() {
        collatingIterator = new CollatingIterator<>(Comparator.naturalOrder());
        collatingIterator.addIterator(iterator1);
        collatingIterator.addIterator(iterator2);
    }

    @Test
    public void testAddIterator() {
        // Given
        Iterator<String> iterator3 = mock(Iterator.class);

        // When
        collatingIterator.addIterator(iterator3);

        // Then
        assertEquals(3, collatingIterator.getIterators().size());
    }

    @Test
    public void testAddIterator_NullIterator() {
        // Given
        Iterator<String> iterator3 = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> collatingIterator.addIterator(iterator3));
    }

    @Test
    public void testGetComparator() {
        // Given
        Comparator<String> comparator = Comparator.naturalOrder();

        // When
        collatingIterator.setComparator(comparator);

        // Then
        assertEquals(comparator, collatingIterator.getComparator());
    }

    @Test
    public void testGetIteratorIndex() {
        // Given
        doReturn(true).when(iterator1).hasNext();
        doReturn("A").when(iterator1).next();
        doReturn(false).when(iterator2).hasNext();

        // When
        collatingIterator.next();

        // Then
        assertEquals(0, collatingIterator.getIteratorIndex());
    }

    @Test
    public void testGetIteratorIndex_NoElementReturned() {
        // Given

        // When and Then
        assertThrows(IllegalStateException.class, () -> collatingIterator.getIteratorIndex());
    }

    @Test
    public void testGetIterators() {
        // Given

        // When
        List<Iterator<? extends String>> iterators = collatingIterator.getIterators();

        // Then
        assertEquals(2, iterators.size());
        assertTrue(iterators.contains(iterator1));
        assertTrue(iterators.contains(iterator2));
    }

    @Test
    public void testHasNext() {
        // Given
        doReturn(true).when(iterator1).hasNext();
        doReturn(false).when(iterator2).hasNext();

        // When
        boolean hasNext = collatingIterator.hasNext();

        // Then
        assertTrue(hasNext);
    }

    @Test
    public void testHasNext_NoMoreElements() {
        // Given
        doReturn(false).when(iterator1).hasNext();
        doReturn(false).when(iterator2).hasNext();

        // When
        boolean hasNext = collatingIterator.hasNext();

        // Then
        assertFalse(hasNext);
    }

    @Test
    public void testNext() {
        // Given
        doReturn(true).when(iterator1).hasNext();
        doReturn("A").when(iterator1).next();
        doReturn(false).when(iterator2).hasNext();

        // When
        String next = collatingIterator.next();

        // Then
        assertEquals("A", next);
    }

    @Test
    public void testNext_NoMoreElements() {
        // Given
        doReturn(false).when(iterator1).hasNext();
        doReturn(false).when(iterator2).hasNext();

        // When and Then
        assertThrows(NoSuchElementException.class, () -> collatingIterator.next());
    }

    @Test
    public void testRemove() {
        // Given
        doReturn(true).when(iterator1).hasNext();
        doReturn("A").when(iterator1).next();
        doReturn(false).when(iterator2).hasNext();
        collatingIterator.next();

        // When
        collatingIterator.remove();

        // Then
        verify(iterator1).remove();
    }

    @Test
    public void testRemove_NoElementReturned() {
        // Given

        // When and Then
        assertThrows(IllegalStateException.class, () -> collatingIterator.remove());
    }

    @Test
    public void testSetComparator() {
        // Given
        Comparator<String> comparator = Comparator.naturalOrder();

        // When
        collatingIterator.setComparator(comparator);

        // Then
        assertEquals(comparator, collatingIterator.getComparator());
    }

    @Test
    public void testSetIterator() {
        // Given
        Iterator<String> iterator3 = mock(Iterator.class);

        // When
        collatingIterator.setIterator(0, iterator3);

        // Then
        assertEquals(iterator3, collatingIterator.getIterators().get(0));
    }

    @Test
    public void testSetIterator_NullIterator() {
        // Given
        Iterator<String> iterator3 = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> collatingIterator.setIterator(0, iterator3));
    }

    @Test
    public void testSetIterator_InvalidIndex() {
        // Given
        Iterator<String> iterator3 = mock(Iterator.class);

        // When and Then
        assertThrows(IndexOutOfBoundsException.class, () -> collatingIterator.setIterator(2, iterator3));
    }
}
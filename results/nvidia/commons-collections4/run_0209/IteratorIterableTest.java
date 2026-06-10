import org.apache.commons.collections4.iterators.IteratorIterable;
import org.apache.commons.collections4.iterators.ResettableIterator;
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
public class IteratorIterableTest {

    @Mock
    private Iterator<Integer> iteratorMock;

    @Mock
    private ResettableIterator<Integer> resettableIteratorMock;

    private IteratorIterable<Integer> iteratorIterable;

    @BeforeEach
    void setup() {
        iteratorIterable = new IteratorIterable<>(iteratorMock);
    }

    @Test
    void testIterator() {
        // Given
        when(iteratorMock.hasNext()).thenReturn(true, false);
        when(iteratorMock.next()).thenReturn(1);

        // When
        Iterator<Integer> iterator = iteratorIterable.iterator();

        // Then
        assertTrue(iterator.hasNext());
        assertEquals(1, (int) iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testIterator_MultipleUse() {
        // Given
        iteratorIterable = new IteratorIterable<>(iteratorMock, true);
        when(iteratorMock.hasNext()).thenReturn(true, false);
        when(iteratorMock.next()).thenReturn(1);

        // When
        Iterator<Integer> iterator1 = iteratorIterable.iterator();
        Iterator<Integer> iterator2 = iteratorIterable.iterator();

        // Then
        assertTrue(iterator1.hasNext());
        assertEquals(1, (int) iterator1.next());
        assertFalse(iterator1.hasNext());

        assertTrue(iterator2.hasNext());
        assertEquals(1, (int) iterator2.next());
        assertFalse(iterator2.hasNext());
    }

    @Test
    void testIterator_Resettable() {
        // Given
        iteratorIterable = new IteratorIterable<>(resettableIteratorMock);
        when(resettableIteratorMock.hasNext()).thenReturn(true, false);
        when(resettableIteratorMock.next()).thenReturn(1);

        // When
        Iterator<Integer> iterator1 = iteratorIterable.iterator();
        Iterator<Integer> iterator2 = iteratorIterable.iterator();

        // Then
        assertTrue(iterator1.hasNext());
        assertEquals(1, (int) iterator1.next());
        assertFalse(iterator1.hasNext());

        assertTrue(iterator2.hasNext());
        assertEquals(1, (int) iterator2.next());
        assertFalse(iterator2.hasNext());

        verify(resettableIteratorMock, times(2)).reset();
    }

    @Test
    void testIterator_NoSuchElementException() {
        // Given
        when(iteratorMock.hasNext()).thenReturn(false);

        // When
        Iterator<Integer> iterator = iteratorIterable.iterator();

        // Then
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testIterator_RemoveUnsupported() {
        // Given
        when(iteratorMock.hasNext()).thenReturn(true);
        when(iteratorMock.next()).thenReturn(1);

        // When
        Iterator<Integer> iterator = iteratorIterable.iterator();
        iterator.next();

        // Then
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    void testCreateTypesafeIterator() {
        // Given
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        Iterator<Integer> iterator = list.iterator();

        // When
        Iterator<Integer> typesafeIterator = IteratorIterable.createTypesafeIterator(iterator);

        // Then
        assertTrue(typesafeIterator.hasNext());
        assertEquals(1, (int) typesafeIterator.next());
        assertTrue(typesafeIterator.hasNext());
        assertEquals(2, (int) typesafeIterator.next());
        assertTrue(typesafeIterator.hasNext());
        assertEquals(3, (int) typesafeIterator.next());
        assertFalse(typesafeIterator.hasNext());
    }
}
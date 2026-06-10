import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CircularFifoQueueTest {

    private CircularFifoQueue<String> queue;

    @BeforeEach
    void setup() {
        queue = new CircularFifoQueue<>();
    }

    @AfterEach
    void tearDown() {
        queue.clear();
    }

    @Test
    void testAdd_ElementAddedSuccessfully() {
        // Given
        String element = "Test Element";

        // When
        boolean result = queue.add(element);

        // Then
        assertTrue(result);
        assertEquals(1, queue.size());
    }

    @Test
    void testAdd_NullElement_ThrowsNullPointerException() {
        // Given
        String element = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> queue.add(element));
    }

    @Test
    void testClear_QueueClearedSuccessfully() {
        // Given
        queue.add("Test Element 1");
        queue.add("Test Element 2");

        // When
        queue.clear();

        // Then
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    @Test
    void testElement_EmptyQueue_ThrowsNoSuchElementException() {
        // Given
        assertTrue(queue.isEmpty());

        // When and Then
        assertThrows(NoSuchElementException.class, () -> queue.element());
    }

    @Test
    void testElement_NonEmptyQueue_ElementReturned() {
        // Given
        queue.add("Test Element");

        // When
        String result = queue.element();

        // Then
        assertEquals("Test Element", result);
    }

    @Test
    void testGet_IndexOutOfRange_ThrowsNoSuchElementException() {
        // Given
        queue.add("Test Element");

        // When and Then
        assertThrows(NoSuchElementException.class, () -> queue.get(1));
    }

    @Test
    void testGet_ValidIndex_ElementReturned() {
        // Given
        queue.add("Test Element");

        // When
        String result = queue.get(0);

        // Then
        assertEquals("Test Element", result);
    }

    @Test
    void testIsAtFullCapacity_EmptyQueue_ReturnsFalse() {
        // Given
        assertTrue(queue.isEmpty());

        // When
        boolean result = queue.isAtFullCapacity();

        // Then
        assertFalse(result);
    }

    @Test
    void testIsAtFullCapacity_FullQueue_ReturnsTrue() {
        // Given
        for (int i = 0; i < 32; i++) {
            queue.add("Test Element " + i);
        }

        // When
        boolean result = queue.isAtFullCapacity();

        // Then
        assertTrue(result);
    }

    @Test
    void testIsEmpty_EmptyQueue_ReturnsTrue() {
        // Given
        assertTrue(queue.isEmpty());

        // When
        boolean result = queue.isEmpty();

        // Then
        assertTrue(result);
    }

    @Test
    void testIsEmpty_NonEmptyQueue_ReturnsFalse() {
        // Given
        queue.add("Test Element");

        // When
        boolean result = queue.isEmpty();

        // Then
        assertFalse(result);
    }

    @Test
    void testIsFull_AlwaysReturnsFalse() {
        // Given

        // When
        boolean result = queue.isFull();

        // Then
        assertFalse(result);
    }

    @Test
    void testIterator_EmptyQueue_NoElementsReturned() {
        // Given
        assertTrue(queue.isEmpty());

        // When
        Iterator<String> iterator = queue.iterator();

        // Then
        assertFalse(iterator.hasNext());
    }

    @Test
    void testIterator_NonEmptyQueue_ElementsReturned() {
        // Given
        queue.add("Test Element 1");
        queue.add("Test Element 2");

        // When
        Iterator<String> iterator = queue.iterator();

        // Then
        assertTrue(iterator.hasNext());
        assertEquals("Test Element 1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("Test Element 2", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testMaxSize_DefaultSizeReturned() {
        // Given

        // When
        int result = queue.maxSize();

        // Then
        assertEquals(32, result);
    }

    @Test
    void testOffer_ElementAddedSuccessfully() {
        // Given
        String element = "Test Element";

        // When
        boolean result = queue.offer(element);

        // Then
        assertTrue(result);
        assertEquals(1, queue.size());
    }

    @Test
    void testOffer_NullElement_ThrowsNullPointerException() {
        // Given
        String element = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> queue.offer(element));
    }

    @Test
    void testPeek_EmptyQueue_NullReturned() {
        // Given
        assertTrue(queue.isEmpty());

        // When
        String result = queue.peek();

        // Then
        assertNull(result);
    }

    @Test
    void testPeek_NonEmptyQueue_ElementReturned() {
        // Given
        queue.add("Test Element");

        // When
        String result = queue.peek();

        // Then
        assertEquals("Test Element", result);
    }

    @Test
    void testPoll_EmptyQueue_NullReturned() {
        // Given
        assertTrue(queue.isEmpty());

        // When
        String result = queue.poll();

        // Then
        assertNull(result);
    }

    @Test
    void testPoll_NonEmptyQueue_ElementReturned() {
        // Given
        queue.add("Test Element");

        // When
        String result = queue.poll();

        // Then
        assertEquals("Test Element", result);
        assertTrue(queue.isEmpty());
    }

    @Test
    void testRemove_EmptyQueue_ThrowsNoSuchElementException() {
        // Given
        assertTrue(queue.isEmpty());

        // When and Then
        assertThrows(NoSuchElementException.class, () -> queue.remove());
    }

    @Test
    void testRemove_NonEmptyQueue_ElementReturned() {
        // Given
        queue.add("Test Element");

        // When
        String result = queue.remove();

        // Then
        assertEquals("Test Element", result);
        assertTrue(queue.isEmpty());
    }

    @Test
    void testSize_EmptyQueue_ZeroReturned() {
        // Given
        assertTrue(queue.isEmpty());

        // When
        int result = queue.size();

        // Then
        assertEquals(0, result);
    }

    @Test
    void testSize_NonEmptyQueue_SizeReturned() {
        // Given
        queue.add("Test Element 1");
        queue.add("Test Element 2");

        // When
        int result = queue.size();

        // Then
        assertEquals(2, result);
    }
}
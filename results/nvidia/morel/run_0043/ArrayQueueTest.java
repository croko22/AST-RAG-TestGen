import net.hydromatic.morel.util.ArrayQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ArrayQueueTest {

    private ArrayQueue<String> queue;

    @BeforeEach
    public void setup() {
        queue = new ArrayQueue<>();
    }

    @Test
    public void testToString() {
        // Given: an empty queue
        // When: toString is called
        String result = queue.toString();
        // Then: the result is an empty list
        assertEquals("[]", result);
    }

    @Test
    public void testPoll_EmptyQueue() {
        // Given: an empty queue
        // When: poll is called
        String result = queue.poll();
        // Then: the result is null
        assertNull(result);
    }

    @Test
    public void testPoll_NonEmptyQueue() {
        // Given: a queue with one element
        queue.add("element");
        // When: poll is called
        String result = queue.poll();
        // Then: the result is the element
        assertEquals("element", result);
    }

    @Test
    public void testSize_EmptyQueue() {
        // Given: an empty queue
        // When: size is called
        int result = queue.size();
        // Then: the result is 0
        assertEquals(0, result);
    }

    @Test
    public void testSize_NonEmptyQueue() {
        // Given: a queue with one element
        queue.add("element");
        // When: size is called
        int result = queue.size();
        // Then: the result is 1
        assertEquals(1, result);
    }

    @Test
    public void testIsEmpty_EmptyQueue() {
        // Given: an empty queue
        // When: isEmpty is called
        boolean result = queue.isEmpty();
        // Then: the result is true
        assertTrue(result);
    }

    @Test
    public void testIsEmpty_NonEmptyQueue() {
        // Given: a queue with one element
        queue.add("element");
        // When: isEmpty is called
        boolean result = queue.isEmpty();
        // Then: the result is false
        assertFalse(result);
    }

    @Test
    public void testGet_EmptyQueue() {
        // Given: an empty queue
        // When: get is called
        assertThrows(IndexOutOfBoundsException.class, () -> queue.get(0));
    }

    @Test
    public void testGet_NonEmptyQueue() {
        // Given: a queue with one element
        queue.add("element");
        // When: get is called
        String result = queue.get(0);
        // Then: the result is the element
        assertEquals("element", result);
    }

    @Test
    public void testSet_EmptyQueue() {
        // Given: an empty queue
        // When: set is called
        assertThrows(IndexOutOfBoundsException.class, () -> queue.set(0, "element"));
    }

    @Test
    public void testSet_NonEmptyQueue() {
        // Given: a queue with one element
        queue.add("element");
        // When: set is called
        String result = queue.set(0, "newElement");
        // Then: the result is the old element
        assertEquals("element", result);
    }

    @Test
    public void testAdd() {
        // Given: an empty queue
        // When: add is called
        queue.add("element");
        // Then: the queue has one element
        assertEquals(1, queue.size());
    }

    @Test
    public void testAsList() {
        // Given: a queue with one element
        queue.add("element");
        // When: asList is called
        List<String> result = queue.asList();
        // Then: the result is a list with one element
        assertEquals(Arrays.asList("element"), result);
    }

    @Test
    public void testForEach() {
        // Given: a queue with one element
        queue.add("element");
        // When: forEach is called
        StringBuilder result = new StringBuilder();
        queue.forEach(element -> result.append(element));
        // Then: the result is the element
        assertEquals("element", result.toString());
    }

    @Test
    public void testRemove_EmptyQueue() {
        // Given: an empty queue
        // When: remove is called
        assertThrows(IndexOutOfBoundsException.class, () -> queue.remove(0));
    }

    @Test
    public void testRemove_NonEmptyQueue() {
        // Given: a queue with one element
        queue.add("element");
        // When: remove is called
        String result = queue.remove(0);
        // Then: the result is the element
        assertEquals("element", result);
    }

    @Test
    public void testListIterator() {
        // Given: a queue with one element
        queue.add("element");
        // When: listIterator is called
        ListIterator<String> result = queue.listIterator();
        // Then: the result is a list iterator with one element
        assertTrue(result.hasNext());
        assertEquals("element", result.next());
        assertFalse(result.hasNext());
    }
}
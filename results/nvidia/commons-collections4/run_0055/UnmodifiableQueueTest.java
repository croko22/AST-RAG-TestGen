import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;
import org.apache.commons.collections4.queue.UnmodifiableQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnmodifiableQueueTest {

    @Mock
    private Queue<String> queue;

    @BeforeEach
    void setup() {
        // Initialize the queue with some elements
        when(queue.iterator()).thenReturn(new ArrayList<String>() {{
            add("Element1");
            add("Element2");
        }}.iterator());
    }

    @Test
    public void testUnmodifiableQueueFactoryMethod() {
        // Given: a queue
        Queue<String> unmodifiableQueue = UnmodifiableQueue.unmodifiableQueue(queue);

        // Then: the queue is unmodifiable
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableQueue.add("New Element"));
    }

    @Test
    public void testAdd() {
        // Given: an unmodifiable queue
        UnmodifiableQueue<String> unmodifiableQueue = new UnmodifiableQueue<>(queue);

        // When: trying to add an element
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableQueue.add("New Element"));
    }

    @Test
    public void testAddAll() {
        // Given: an unmodifiable queue
        UnmodifiableQueue<String> unmodifiableQueue = new UnmodifiableQueue<>(queue);

        // When: trying to add all elements from a collection
        Collection<String> collection = new ArrayList<>();
        collection.add("New Element1");
        collection.add("New Element2");
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableQueue.addAll(collection));
    }

    @Test
    public void testClear() {
        // Given: an unmodifiable queue
        UnmodifiableQueue<String> unmodifiableQueue = new UnmodifiableQueue<>(queue);

        // When: trying to clear the queue
        assertThrows(UnsupportedOperationException.class, unmodifiableQueue::clear);
    }

    @Test
    public void testIterator() {
        // Given: an unmodifiable queue
        UnmodifiableQueue<String> unmodifiableQueue = new UnmodifiableQueue<>(queue);

        // When: getting the iterator
        Iterator<String> iterator = unmodifiableQueue.iterator();

        // Then: the iterator is not null
        assertNotNull(iterator);
    }

    @Test
    public void testOffer() {
        // Given: an unmodifiable queue
        UnmodifiableQueue<String> unmodifiableQueue = new UnmodifiableQueue<>(queue);

        // When: trying to offer an element
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableQueue.offer("New Element"));
    }

    @Test
    public void testPoll() {
        // Given: an unmodifiable queue
        UnmodifiableQueue<String> unmodifiableQueue = new UnmodifiableQueue<>(queue);

        // When: trying to poll an element
        assertThrows(UnsupportedOperationException.class, unmodifiableQueue::poll);
    }

    @Test
    public void testRemove() {
        // Given: an unmodifiable queue
        UnmodifiableQueue<String> unmodifiableQueue = new UnmodifiableQueue<>(queue);

        // When: trying to remove an element
        assertThrows(UnsupportedOperationException.class, unmodifiableQueue::remove);
    }

    @Test
    public void testRemoveObject() {
        // Given: an unmodifiable queue
        UnmodifiableQueue<String> unmodifiableQueue = new UnmodifiableQueue<>(queue);

        // When: trying to remove an object
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableQueue.remove("Element1"));
    }

    @Test
    public void testRemoveAll() {
        // Given: an unmodifiable queue
        UnmodifiableQueue<String> unmodifiableQueue = new UnmodifiableQueue<>(queue);

        // When: trying to remove all elements from a collection
        Collection<String> collection = new ArrayList<>();
        collection.add("Element1");
        collection.add("Element2");
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableQueue.removeAll(collection));
    }

    @Test
    public void testRemoveIf() {
        // Given: an unmodifiable queue
        UnmodifiableQueue<String> unmodifiableQueue = new UnmodifiableQueue<>(queue);

        // When: trying to remove if a predicate is true
        Predicate<String> predicate = any();
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableQueue.removeIf(predicate));
    }

    @Test
    public void testRetainAll() {
        // Given: an unmodifiable queue
        UnmodifiableQueue<String> unmodifiableQueue = new UnmodifiableQueue<>(queue);

        // When: trying to retain all elements from a collection
        Collection<String> collection = new ArrayList<>();
        collection.add("Element1");
        collection.add("Element2");
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableQueue.retainAll(collection));
    }
}
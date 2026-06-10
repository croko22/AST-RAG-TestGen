import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.collection.PredicatedCollection;
import org.apache.commons.collections4.queue.PredicatedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PredicatedQueueTest {

    @Mock
    private Queue<String> queue;

    @Mock
    private Predicate<String> predicate;

    private PredicatedQueue<String> predicatedQueue;

    @BeforeEach
    public void setup() {
        predicatedQueue = new PredicatedQueue<>(queue, predicate);
    }

    @Test
    public void testPredicatedQueue() {
        // Given
        when(predicate.evaluate(any())).thenReturn(true);

        // When
        PredicatedQueue<String> result = PredicatedQueue.predicatedQueue(queue, predicate);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testPredicatedQueue_NullQueue() {
        // Given
        Queue<String> nullQueue = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> PredicatedQueue.predicatedQueue(nullQueue, predicate));
    }

    @Test
    public void testPredicatedQueue_NullPredicate() {
        // Given
        Predicate<String> nullPredicate = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> PredicatedQueue.predicatedQueue(queue, nullPredicate));
    }

    @Test
    public void testElement() {
        // Given
        String element = "element";
        when(queue.element()).thenReturn(element);

        // When
        String result = predicatedQueue.element();

        // Then
        assertEquals(element, result);
        verify(queue, times(1)).element();
    }

    @Test
    public void testOffer_ValidObject() {
        // Given
        String object = "object";
        when(predicate.evaluate(object)).thenReturn(true);
        when(queue.offer(object)).thenReturn(true);

        // When
        boolean result = predicatedQueue.offer(object);

        // Then
        assertTrue(result);
        verify(queue, times(1)).offer(object);
    }

    @Test
    public void testOffer_InvalidObject() {
        // Given
        String object = "object";
        when(predicate.evaluate(object)).thenReturn(false);

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> predicatedQueue.offer(object));
        verify(queue, never()).offer(object);
    }

    @Test
    public void testPeek() {
        // Given
        String peek = "peek";
        when(queue.peek()).thenReturn(peek);

        // When
        String result = predicatedQueue.peek();

        // Then
        assertEquals(peek, result);
        verify(queue, times(1)).peek();
    }

    @Test
    public void testPoll() {
        // Given
        String poll = "poll";
        when(queue.poll()).thenReturn(poll);

        // When
        String result = predicatedQueue.poll();

        // Then
        assertEquals(poll, result);
        verify(queue, times(1)).poll();
    }

    @Test
    public void testRemove() {
        // Given
        String remove = "remove";
        when(queue.remove()).thenReturn(remove);

        // When
        String result = predicatedQueue.remove();

        // Then
        assertEquals(remove, result);
        verify(queue, times(1)).remove();
    }
}
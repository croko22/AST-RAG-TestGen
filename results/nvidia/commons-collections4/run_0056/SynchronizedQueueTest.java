import org.apache.commons.collections4.collection.SynchronizedCollection;
import org.apache.commons.collections4.queue.SynchronizedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SynchronizedQueueTest {

    @Mock
    private Queue<String> queue;

    private SynchronizedQueue<String> synchronizedQueue;

    @BeforeEach
    void setup() {
        synchronizedQueue = SynchronizedQueue.synchronizedQueue(queue);
    }

    @Test
    public void testSynchronizedQueue() {
        // Given
        Queue<String> queue = new ConcurrentLinkedQueue<>();
        SynchronizedQueue<String> synchronizedQueue = SynchronizedQueue.synchronizedQueue(queue);

        // Then
        assertNotNull(synchronizedQueue);
    }

    @Test
    public void testElement() {
        // Given
        when(queue.element()).thenReturn("element");

        // When
        String element = synchronizedQueue.element();

        // Then
        assertEquals("element", element);
        verify(queue, times(1)).element();
    }

    @Test
    public void testElement_ThrowsException() {
        // Given
        when(queue.element()).thenThrow(new RuntimeException("Test exception"));

        // When / Then
        assertThrows(RuntimeException.class, () -> synchronizedQueue.element());
        verify(queue, times(1)).element();
    }

    @Test
    public void testEquals() {
        // Given
        when(queue.equals(any())).thenReturn(true);

        // When
        boolean equals = synchronizedQueue.equals(queue);

        // Then
        assertTrue(equals);
        verify(queue, times(1)).equals(any());
    }

    @Test
    public void testHashCode() {
        // Given
        when(queue.hashCode()).thenReturn(123);

        // When
        int hashCode = synchronizedQueue.hashCode();

        // Then
        assertEquals(123, hashCode);
        verify(queue, times(1)).hashCode();
    }

    @Test
    public void testOffer() {
        // Given
        when(queue.offer(any())).thenReturn(true);

        // When
        boolean offer = synchronizedQueue.offer("element");

        // Then
        assertTrue(offer);
        verify(queue, times(1)).offer(any());
    }

    @Test
    public void testPeek() {
        // Given
        when(queue.peek()).thenReturn("element");

        // When
        String peek = synchronizedQueue.peek();

        // Then
        assertEquals("element", peek);
        verify(queue, times(1)).peek();
    }

    @Test
    public void testPoll() {
        // Given
        when(queue.poll()).thenReturn("element");

        // When
        String poll = synchronizedQueue.poll();

        // Then
        assertEquals("element", poll);
        verify(queue, times(1)).poll();
    }

    @Test
    public void testRemove() {
        // Given
        when(queue.remove()).thenReturn("element");

        // When
        String remove = synchronizedQueue.remove();

        // Then
        assertEquals("element", remove);
        verify(queue, times(1)).remove();
    }

    @Test
    public void testSynchronizedQueue_NullQueue() {
        // Given
        Queue<String> nullQueue = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> SynchronizedQueue.synchronizedQueue(nullQueue));
    }

    @Test
    public void testSynchronizedQueue_WithLock() {
        // Given
        Queue<String> queue = new ConcurrentLinkedQueue<>();
        Object lock = new Object();

        // When
        SynchronizedQueue<String> synchronizedQueue = new SynchronizedQueue<>(queue, lock);

        // Then
        assertNotNull(synchronizedQueue);
    }
}
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.collection.TransformedCollection;
import org.apache.commons.collections4.queue.TransformedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransformedQueueTest {

    @Mock
    private Queue<String> queue;

    @Mock
    private Transformer<String, String> transformer;

    private TransformedQueue<String> transformedQueue;

    @BeforeEach
    void setup() {
        transformedQueue = TransformedQueue.transformingQueue(queue, transformer);
    }

    @Test
    public void testTransformedQueue() {
        // Given
        when(queue.isEmpty()).thenReturn(false);
        String[] values = {"value1", "value2"};
        when(queue.toArray()).thenReturn(values);

        // When
        TransformedQueue<String> result = TransformedQueue.transformedQueue(queue, transformer);

        // Then
        verify(queue, times(1)).isEmpty();
        verify(queue, times(1)).toArray();
        verify(transformer, times(2)).apply(any());
    }

    @Test
    public void testTransformingQueue() {
        // Given
        when(queue.isEmpty()).thenReturn(false);

        // When
        TransformedQueue<String> result = TransformedQueue.transformingQueue(queue, transformer);

        // Then
        verify(queue, times(1)).isEmpty();
        verify(transformer, never()).apply(any());
    }

    @Test
    public void testElement() {
        // Given
        String element = "element";
        when(queue.element()).thenReturn(element);

        // When
        String result = transformedQueue.element();

        // Then
        assertEquals(element, result);
        verify(queue, times(1)).element();
    }

    @Test
    public void testOffer() {
        // Given
        String obj = "obj";
        when(transformer.apply(obj)).thenReturn(obj);
        when(queue.offer(obj)).thenReturn(true);

        // When
        boolean result = transformedQueue.offer(obj);

        // Then
        assertTrue(result);
        verify(transformer, times(1)).apply(obj);
        verify(queue, times(1)).offer(obj);
    }

    @Test
    public void testPeek() {
        // Given
        String peek = "peek";
        when(queue.peek()).thenReturn(peek);

        // When
        String result = transformedQueue.peek();

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
        String result = transformedQueue.poll();

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
        String result = transformedQueue.remove();

        // Then
        assertEquals(remove, result);
        verify(queue, times(1)).remove();
    }

    @Test
    public void testTransformedQueueNullQueue() {
        // Given
        Queue<String> nullQueue = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> TransformedQueue.transformedQueue(nullQueue, transformer));
    }

    @Test
    public void testTransformedQueueNullTransformer() {
        // Given
        Transformer<String, String> nullTransformer = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> TransformedQueue.transformedQueue(queue, nullTransformer));
    }

    @Test
    public void testTransformingQueueNullQueue() {
        // Given
        Queue<String> nullQueue = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> TransformedQueue.transformingQueue(nullQueue, transformer));
    }

    @Test
    public void testTransformingQueueNullTransformer() {
        // Given
        Transformer<String, String> nullTransformer = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> TransformedQueue.transformingQueue(queue, nullTransformer));
    }
}
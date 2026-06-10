import org.apache.commons.collections4.QueueUtils;
import org.apache.commons.collections4.queue.PredicatedQueue;
import org.apache.commons.collections4.queue.SynchronizedQueue;
import org.apache.commons.collections4.queue.TransformedQueue;
import org.apache.commons.collections4.queue.UnmodifiableQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QueueUtilsTest {

    @Mock
    private Queue<String> queue;

    @Mock
    private Predicate<String> predicate;

    @Mock
    private org.apache.commons.collections4.Transformer<String, String> transformer;

    @BeforeEach
    void setup() {
        // Initialize mocks
    }

    @Test
    public void testEmptyQueue() {
        // Given
        Queue<String> result = QueueUtils.emptyQueue();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testPredicatedQueue() {
        // Given
        when(predicate.test(any())).thenReturn(true);

        // When
        Queue<String> result = QueueUtils.predicatedQueue(queue, predicate);

        // Then
        assertNotNull(result);
        assertSame(PredicatedQueue.class, result.getClass());
    }

    @Test
    public void testPredicatedQueue_NullQueue() {
        // Given
        when(predicate.test(any())).thenReturn(true);

        // When / Then
        assertThrows(NullPointerException.class, () -> QueueUtils.predicatedQueue(null, predicate));
    }

    @Test
    public void testPredicatedQueue_NullPredicate() {
        // Given

        // When / Then
        assertThrows(NullPointerException.class, () -> QueueUtils.predicatedQueue(queue, null));
    }

    @Test
    public void testSynchronizedQueue() {
        // Given

        // When
        Queue<String> result = QueueUtils.synchronizedQueue(queue);

        // Then
        assertNotNull(result);
        assertSame(SynchronizedQueue.class, result.getClass());
    }

    @Test
    public void testSynchronizedQueue_NullQueue() {
        // Given

        // When / Then
        assertThrows(NullPointerException.class, () -> QueueUtils.synchronizedQueue(null));
    }

    @Test
    public void testTransformingQueue() {
        // Given

        // When
        Queue<String> result = QueueUtils.transformingQueue(queue, transformer);

        // Then
        assertNotNull(result);
        assertSame(TransformedQueue.class, result.getClass());
    }

    @Test
    public void testTransformingQueue_NullQueue() {
        // Given

        // When / Then
        assertThrows(NullPointerException.class, () -> QueueUtils.transformingQueue(null, transformer));
    }

    @Test
    public void testTransformingQueue_NullTransformer() {
        // Given

        // When / Then
        assertThrows(NullPointerException.class, () -> QueueUtils.transformingQueue(queue, null));
    }

    @Test
    public void testUnmodifiableQueue() {
        // Given
        Queue<String> unmodifiableQueue = QueueUtils.unmodifiableQueue(queue);

        // Then
        assertNotNull(unmodifiableQueue);
        assertSame(UnmodifiableQueue.class, unmodifiableQueue.getClass());
    }

    @Test
    public void testUnmodifiableQueue_NullQueue() {
        // Given

        // When / Then
        assertThrows(NullPointerException.class, () -> QueueUtils.unmodifiableQueue(null));
    }
}
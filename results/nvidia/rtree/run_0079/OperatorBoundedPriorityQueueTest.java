import com.github.davidmoten.rtree.internal.operators.OperatorBoundedPriorityQueue;
import com.github.davidmoten.rtree.internal.util.BoundedPriorityQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rx.Subscriber;
import rx.exceptions.OnErrorNotImplementedException;
import rx.observers.TestSubscriber;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OperatorBoundedPriorityQueueTest {

    @Mock
    private Subscriber<Integer> child;

    @Mock
    private BoundedPriorityQueue<Integer> boundedPriorityQueue;

    private OperatorBoundedPriorityQueue<Integer> operatorBoundedPriorityQueue;

    @BeforeEach
    public void setup() {
        operatorBoundedPriorityQueue = new OperatorBoundedPriorityQueue<>(10, Comparator.naturalOrder());
    }

    @Test
    public void testCall() {
        // Given
        Subscriber<Integer> subscriber = operatorBoundedPriorityQueue.call(child);

        // When
        subscriber.onStart();

        // Then
        verify(child, times(1)).onStart();
    }

    @Test
    public void testOnNext() {
        // Given
        Subscriber<Integer> subscriber = operatorBoundedPriorityQueue.call(child);
        BoundedPriorityQueue<Integer> boundedPriorityQueue = mock(BoundedPriorityQueue.class);
        when(boundedPriorityQueue.asList()).thenReturn(new ArrayList<>());
        when(boundedPriorityQueue.asOrderedList()).thenReturn(new ArrayList<>());
        FieldUtils.writeField(operatorBoundedPriorityQueue, "q", boundedPriorityQueue, true);

        // When
        subscriber.onNext(1);

        // Then
        verify(boundedPriorityQueue, times(1)).add(1);
    }

    @Test
    public void testOnCompleted() {
        // Given
        Subscriber<Integer> subscriber = operatorBoundedPriorityQueue.call(child);
        BoundedPriorityQueue<Integer> boundedPriorityQueue = mock(BoundedPriorityQueue.class);
        when(boundedPriorityQueue.asList()).thenReturn(new ArrayList<>());
        when(boundedPriorityQueue.asOrderedList()).thenReturn(new ArrayList<>());
        FieldUtils.writeField(operatorBoundedPriorityQueue, "q", boundedPriorityQueue, true);

        // When
        subscriber.onCompleted();

        // Then
        verify(child, times(1)).onCompleted();
    }

    @Test
    public void testOnError() {
        // Given
        Subscriber<Integer> subscriber = operatorBoundedPriorityQueue.call(child);

        // When
        subscriber.onError(new OnErrorNotImplementedException());

        // Then
        verify(child, times(1)).onError(any(Throwable.class));
    }

    @Test
    public void testOnNextWithUnsubscribed() {
        // Given
        Subscriber<Integer> subscriber = operatorBoundedPriorityQueue.call(child);
        BoundedPriorityQueue<Integer> boundedPriorityQueue = mock(BoundedPriorityQueue.class);
        when(boundedPriorityQueue.asList()).thenReturn(new ArrayList<>());
        when(boundedPriorityQueue.asOrderedList()).thenReturn(new ArrayList<>());
        FieldUtils.writeField(operatorBoundedPriorityQueue, "q", boundedPriorityQueue, true);
        doReturn(true).when(subscriber).isUnsubscribed();

        // When
        subscriber.onNext(1);

        // Then
        verify(boundedPriorityQueue, never()).add(1);
    }

    @Test
    public void testOnCompletedWithUnsubscribed() {
        // Given
        Subscriber<Integer> subscriber = operatorBoundedPriorityQueue.call(child);
        BoundedPriorityQueue<Integer> boundedPriorityQueue = mock(BoundedPriorityQueue.class);
        when(boundedPriorityQueue.asList()).thenReturn(new ArrayList<>());
        when(boundedPriorityQueue.asOrderedList()).thenReturn(new ArrayList<>());
        FieldUtils.writeField(operatorBoundedPriorityQueue, "q", boundedPriorityQueue, true);
        doReturn(true).when(subscriber).isUnsubscribed();

        // When
        subscriber.onCompleted();

        // Then
        verify(child, never()).onCompleted();
    }
}
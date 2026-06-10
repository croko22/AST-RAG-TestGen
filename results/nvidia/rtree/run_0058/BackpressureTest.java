import com.github.davidmoten.rtree.Backpressure;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.internal.util.ImmutableStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import rx.Subscriber;
import rx.functions.Func1;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BackpressureTest {

    @Mock
    private Func1<Geometry, Boolean> condition;

    @Mock
    private Subscriber<Entry<String, Geometry>> subscriber;

    @Mock
    private Geometry geometry;

    @Mock
    private ImmutableStack<NodePosition<String, Geometry>> stack;

    @BeforeEach
    public void setup() {
        // Initialize mocks
        when(condition.call(any())).thenReturn(true);
        when(subscriber.isUnsubscribed()).thenReturn(false);
    }

    @Test
    public void testSearch() {
        // Given
        when(stack.isEmpty()).thenReturn(false);
        when(stack.peek()).thenReturn(new NodePosition<>(new Leaf<>("value", geometry), 0));

        // When
        ImmutableStack<NodePosition<String, Geometry>> result = Backpressure.search(condition, subscriber, stack, 1);

        // Then
        assertNotNull(result);
        verify(condition, times(1)).call(any());
        verify(subscriber, times(1)).onNext(any());
    }

    @Test
    public void testSearchAndReturnStack() {
        // Given
        when(stack.isEmpty()).thenReturn(false);
        when(stack.peek()).thenReturn(new NodePosition<>(new Leaf<>("value", geometry), 0));

        // When
        ImmutableStack<NodePosition<String, Geometry>> result = Backpressure.searchAndReturnStack(condition, subscriber, new StackAndRequest<>(stack, 1));

        // Then
        assertNotNull(result);
        verify(condition, times(1)).call(any());
        verify(subscriber, times(1)).onNext(any());
    }

    @Test
    public void testSearchLeaf() {
        // Given
        NodePosition<String, Geometry> nodePosition = new NodePosition<>(new Leaf<>("value", geometry), 0);
        StackAndRequest<NodePosition<String, Geometry>> state = new StackAndRequest<>(stack, 1);

        // When
        StackAndRequest<NodePosition<String, Geometry>> result = Backpressure.searchLeaf(condition, subscriber, state, nodePosition);

        // Then
        assertNotNull(result);
        verify(condition, times(1)).call(any());
        verify(subscriber, times(1)).onNext(any());
    }

    @Test
    public void testSearchNonLeaf() {
        // Given
        NodePosition<String, Geometry> nodePosition = new NodePosition<>(new NonLeaf<>("value", geometry), 0);

        // When
        ImmutableStack<NodePosition<String, Geometry>> result = Backpressure.searchNonLeaf(condition, stack, nodePosition);

        // Then
        assertNotNull(result);
        verify(condition, times(1)).call(any());
    }

    @Test
    public void testSearchAfterLastInNode() {
        // Given
        NodePosition<String, Geometry> nodePosition = new NodePosition<>(new Leaf<>("value", geometry), 0);

        // When
        ImmutableStack<NodePosition<String, Geometry>> result = Backpressure.searchAfterLastInNode(stack);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testSearch_Unsubscribed() {
        // Given
        when(subscriber.isUnsubscribed()).thenReturn(true);

        // When
        ImmutableStack<NodePosition<String, Geometry>> result = Backpressure.search(condition, subscriber, stack, 1);

        // Then
        assertNotNull(result);
        verify(condition, never()).call(any());
        verify(subscriber, never()).onNext(any());
    }

    @Test
    public void testSearch_NoRequest() {
        // Given
        when(stack.isEmpty()).thenReturn(false);
        when(stack.peek()).thenReturn(new NodePosition<>(new Leaf<>("value", geometry), 0));

        // When
        ImmutableStack<NodePosition<String, Geometry>> result = Backpressure.search(condition, subscriber, stack, 0);

        // Then
        assertNotNull(result);
        verify(condition, times(1)).call(any());
        verify(subscriber, times(1)).onNext(any());
    }

    private static class NodePosition<T, S extends Geometry> {
        private final Node<T, S> node;
        private final int position;

        public NodePosition(Node<T, S> node, int position) {
            this.node = node;
            this.position = position;
        }

        public Node<T, S> node() {
            return node;
        }

        public int position() {
            return position;
        }

        public int nextPosition() {
            return position + 1;
        }
    }

    private static class Node<T, S extends Geometry> {
        private final String value;
        private final Geometry geometry;

        public Node(String value, Geometry geometry) {
            this.value = value;
            this.geometry = geometry;
        }

        public Geometry geometry() {
            return geometry;
        }
    }

    private static class Leaf<T, S extends Geometry> extends Node<T, S> {
        public Leaf(String value, Geometry geometry) {
            super(value, geometry);
        }

        public Entry<T, S> entry(int position) {
            return new Entry<>(value, geometry);
        }
    }

    private static class NonLeaf<T, S extends Geometry> extends Node<T, S> {
        public NonLeaf(String value, Geometry geometry) {
            super(value, geometry);
        }

        public Node<T, S> child(int position) {
            return new Node<>(value, geometry);
        }
    }

    private static class Entry<T, S extends Geometry> {
        private final T value;
        private final Geometry geometry;

        public Entry(T value, Geometry geometry) {
            this.value = value;
            this.geometry = geometry;
        }

        public Geometry geometry() {
            return geometry;
        }
    }
}
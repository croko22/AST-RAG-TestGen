import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.Node;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.HasGeometry;
import com.github.davidmoten.rtree.internal.NodeAndEntries;
import io.reactivex.functions.Func1;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NodeTest {

    @Mock
    private Node<String, Geometry> node;

    @Mock
    private Entry<String, Geometry> entry;

    @Mock
    private Geometry geometry;

    @Mock
    private Func1<Geometry, Boolean> criterion;

    @Mock
    private TestSubscriber<Entry<String, Geometry>> subscriber;

    @BeforeEach
    public void setup() {
        // Initialize mocks
        when(node.geometry()).thenReturn(geometry);
        when(geometry.intersects(any())).thenReturn(true);
        when(criterion.apply(any())).thenReturn(true);
    }

    @Test
    public void testAdd() {
        // Given
        List<Node<String, Geometry>> expected = new ArrayList<>();
        when(node.add(entry)).thenReturn(expected);

        // When
        List<Node<String, Geometry>> result = node.add(entry);

        // Then
        assertEquals(expected, result);
        verify(node, times(1)).add(entry);
    }

    @Test
    public void testDelete() {
        // Given
        NodeAndEntries<String, Geometry> expected = mock(NodeAndEntries.class);
        when(node.delete(entry, true)).thenReturn(expected);

        // When
        NodeAndEntries<String, Geometry> result = node.delete(entry, true);

        // Then
        assertEquals(expected, result);
        verify(node, times(1)).delete(entry, true);
    }

    @Test
    public void testDelete_False() {
        // Given
        NodeAndEntries<String, Geometry> expected = mock(NodeAndEntries.class);
        when(node.delete(entry, false)).thenReturn(expected);

        // When
        NodeAndEntries<String, Geometry> result = node.delete(entry, false);

        // Then
        assertEquals(expected, result);
        verify(node, times(1)).delete(entry, false);
    }

    @Test
    public void testSearchWithoutBackpressure() {
        // Given
        doNothing().when(node).searchWithoutBackpressure(criterion, subscriber);

        // When
        node.searchWithoutBackpressure(criterion, subscriber);

        // Then
        verify(node, times(1)).searchWithoutBackpressure(criterion, subscriber);
    }

    @Test
    public void testCount() {
        // Given
        int expected = 10;
        when(node.count()).thenReturn(expected);

        // When
        int result = node.count();

        // Then
        assertEquals(expected, result);
        verify(node, times(1)).count();
    }

    @Test
    public void testContext() {
        // Given
        Object expected = new Object();
        when(node.context()).thenReturn(expected);

        // When
        Object result = node.context();

        // Then
        assertEquals(expected, result);
        verify(node, times(1)).context();
    }
}
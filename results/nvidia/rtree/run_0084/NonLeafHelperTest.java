import com.github.davidmoten.rtree.Context;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.Node;
import com.github.davidmoten.rtree.NonLeaf;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.ListPair;
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
public class NonLeafHelperTest {

    @Mock
    private NonLeaf<String, Geometry> nonLeaf;

    @Mock
    private Context<String, Geometry> context;

    @Mock
    private Entry<String, Geometry> entry;

    @Mock
    private Geometry geometry;

    @Mock
    private ListPair<Node<String, Geometry>> listPair;

    @Mock
    private Node<String, Geometry> node;

    @Mock
    private Node<String, Geometry> child;

    @BeforeEach
    void setup() {
        when(nonLeaf.context()).thenReturn(context);
        when(nonLeaf.children()).thenReturn(new ArrayList<>());
        when(nonLeaf.count()).thenReturn(0);
        when(entry.geometry()).thenReturn(geometry);
        when(listPair.group1()).thenReturn(new ListPair.Group<>());
        when(listPair.group2()).thenReturn(new ListPair.Group<>());
    }

    @Test
    void testSearch() {
        // Given
        Func1<Geometry, Boolean> criterion = geometry1 -> true;
        Subscriber<Entry<String, Geometry>> subscriber = mock(Subscriber.class);
        when(nonLeaf.geometry().mbr()).thenReturn(geometry);
        when(nonLeaf.child(0)).thenReturn(child);

        // When
        NonLeafHelper.search(criterion, subscriber, nonLeaf);

        // Then
        verify(child).searchWithoutBackpressure(criterion, subscriber);
    }

    @Test
    void testSearchUnsubscribed() {
        // Given
        Func1<Geometry, Boolean> criterion = geometry1 -> true;
        Subscriber<Entry<String, Geometry>> subscriber = mock(Subscriber.class);
        when(subscriber.isUnsubscribed()).thenReturn(true);
        when(nonLeaf.geometry().mbr()).thenReturn(geometry);
        when(nonLeaf.child(0)).thenReturn(child);

        // When
        NonLeafHelper.search(criterion, subscriber, nonLeaf);

        // Then
        verify(child, never()).searchWithoutBackpressure(criterion, subscriber);
    }

    @Test
    void testAdd() {
        // Given
        when(context.selector()).thenReturn(mock(Context.Selector.class));
        when(context.factory()).thenReturn(mock(Context.Factory.class));
        when(context.maxChildren()).thenReturn(10);
        when(context.minChildren()).thenReturn(5);
        when(context.splitter()).thenReturn(mock(Context.Splitter.class));
        when(nonLeaf.children()).thenReturn(new ArrayList<>());
        when(nonLeaf.context()).thenReturn(context);

        // When
        List<Node<String, Geometry>> result = NonLeafHelper.add(entry, nonLeaf);

        // Then
        assertNotNull(result);
    }

    @Test
    void testAddSplit() {
        // Given
        when(context.selector()).thenReturn(mock(Context.Selector.class));
        when(context.factory()).thenReturn(mock(Context.Factory.class));
        when(context.maxChildren()).thenReturn(5);
        when(context.minChildren()).thenReturn(5);
        when(context.splitter()).thenReturn(mock(Context.Splitter.class));
        when(nonLeaf.children()).thenReturn(new ArrayList<>());
        when(nonLeaf.context()).thenReturn(context);
        when(context.splitter().split(any(), any())).thenReturn(listPair);

        // When
        List<Node<String, Geometry>> result = NonLeafHelper.add(entry, nonLeaf);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testDelete() {
        // Given
        when(nonLeaf.children()).thenReturn(new ArrayList<>());
        when(nonLeaf.context()).thenReturn(context);
        when(context.factory()).thenReturn(mock(Context.Factory.class));
        when(context.maxChildren()).thenReturn(10);
        when(context.minChildren()).thenReturn(5);

        // When
        NonLeafHelper.NodeAndEntries<String, Geometry> result = NonLeafHelper.delete(entry, true, nonLeaf);

        // Then
        assertNotNull(result);
    }

    @Test
    void testDeleteRemoveChild() {
        // Given
        when(nonLeaf.children()).thenReturn(List.of(child));
        when(nonLeaf.context()).thenReturn(context);
        when(context.factory()).thenReturn(mock(Context.Factory.class));
        when(context.maxChildren()).thenReturn(10);
        when(context.minChildren()).thenReturn(5);
        when(child.delete(entry, true)).thenReturn(new NonLeafHelper.NodeAndEntries<>(Optional.empty(), new ArrayList<>(), 0));

        // When
        NonLeafHelper.NodeAndEntries<String, Geometry> result = NonLeafHelper.delete(entry, true, nonLeaf);

        // Then
        assertNotNull(result);
        assertTrue(result.node().isEmpty());
    }

    @Test
    void testDeleteUpdateChild() {
        // Given
        when(nonLeaf.children()).thenReturn(List.of(child));
        when(nonLeaf.context()).thenReturn(context);
        when(context.factory()).thenReturn(mock(Context.Factory.class));
        when(context.maxChildren()).thenReturn(10);
        when(context.minChildren()).thenReturn(5);
        when(child.delete(entry, true)).thenReturn(new NonLeafHelper.NodeAndEntries<>(Optional.of(node), new ArrayList<>(), 0));

        // When
        NonLeafHelper.NodeAndEntries<String, Geometry> result = NonLeafHelper.delete(entry, true, nonLeaf);

        // Then
        assertNotNull(result);
        assertTrue(result.node().isPresent());
    }
}
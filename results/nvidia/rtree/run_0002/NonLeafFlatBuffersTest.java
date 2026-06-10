import com.github.davidmoten.rtree.Context;
import com.github.davidmoten.rtree.Entries;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.NonLeaf;
import com.github.davidmoten.rtree.Node;
import com.github.davidmoten.rtree.fbs.FlatBuffersHelper;
import com.github.davidmoten.rtree.fbs.generated.Bounds_;
import com.github.davidmoten.rtree.fbs.generated.BoxDouble_;
import com.github.davidmoten.rtree.fbs.generated.BoxFloat_;
import com.github.davidmoten.rtree.fbs.generated.Entry_;
import com.github.davidmoten.rtree.fbs.generated.Geometry_;
import com.github.davidmoten.rtree.fbs.generated.Node_;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.internal.NodeAndEntries;
import com.github.davidmoten.rtree.internal.NonLeafHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NonLeafFlatBuffersTest {

    @Mock
    private Node_ node;

    @Mock
    private Context<String, Geometry> context;

    @Mock
    private Func1<byte[], String> deserializer;

    private NonLeafFlatBuffers<String, Geometry> nonLeafFlatBuffers;

    @BeforeEach
    void setup() {
        nonLeafFlatBuffers = new NonLeafFlatBuffers<>(node, context, deserializer);
    }

    @Test
    void testAdd() {
        // Given
        Entry<String, Geometry> entry = Entries.entry("object", Geometries.rectangle(0, 0, 1, 1));

        // When
        List<Node<String, Geometry>> result = nonLeafFlatBuffers.add(entry);

        // Then
        assertNotNull(result);
        verify(NonLeafHelper.class, times(1)).add(any(Entry.class), any(NonLeaf.class));
    }

    @Test
    void testDelete() {
        // Given
        Entry<String, Geometry> entry = Entries.entry("object", Geometries.rectangle(0, 0, 1, 1));

        // When
        NodeAndEntries<String, Geometry> result = nonLeafFlatBuffers.delete(entry, true);

        // Then
        assertNotNull(result);
        verify(NonLeafHelper.class, times(1)).delete(any(Entry.class), anyBoolean(), any(NonLeaf.class));
    }

    @Test
    void testSearchWithoutBackpressure() {
        // Given
        Func1<Geometry, Boolean> criterion = geometry -> true;
        Subscriber<Entry<String, Geometry>> subscriber = mock(Subscriber.class);

        // When
        nonLeafFlatBuffers.searchWithoutBackpressure(criterion, subscriber);

        // Then
        verify(NonLeafHelper.class, times(1)).search(any(Func1.class), any(Subscriber.class), any(NonLeaf.class));
    }

    @Test
    void testCount() {
        // Given
        when(node.childrenLength()).thenReturn(5);

        // When
        int result = nonLeafFlatBuffers.count();

        // Then
        assertEquals(5, result);
        verify(node, times(1)).childrenLength();
    }

    @Test
    void testContext() {
        // When
        Context<String, Geometry> result = nonLeafFlatBuffers.context();

        // Then
        assertSame(context, result);
    }

    @Test
    void testGeometry() {
        // Given
        when(node.mbb(any(Bounds_.class))).thenReturn(new Bounds_());

        // When
        Geometry result = nonLeafFlatBuffers.geometry();

        // Then
        assertNotNull(result);
        verify(node, times(1)).mbb(any(Bounds_.class));
    }

    @Test
    void testChild() {
        // Given
        when(node.children(anyInt())).thenReturn(new Node_());

        // When
        Node<String, Geometry> result = nonLeafFlatBuffers.child(0);

        // Then
        assertNotNull(result);
        verify(node, times(1)).children(anyInt());
    }

    @Test
    void testChildren() {
        // Given
        when(node.childrenLength()).thenReturn(5);
        when(node.children(anyInt())).thenReturn(new Node_());

        // When
        List<Node<String, Geometry>> result = nonLeafFlatBuffers.children();

        // Then
        assertNotNull(result);
        assertEquals(5, result.size());
        verify(node, times(1)).childrenLength();
        verify(node, times(5)).children(anyInt());
    }

    @Test
    void testToString() {
        // When
        String result = nonLeafFlatBuffers.toString();

        // Then
        assertNotNull(result);
    }
}
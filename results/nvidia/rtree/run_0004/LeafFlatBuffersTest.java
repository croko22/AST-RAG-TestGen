import com.github.davidmoten.rtree.Context;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.fbs.LeafFlatBuffers;
import com.github.davidmoten.rtree.fbs.generated.Bounds_;
import com.github.davidmoten.rtree.fbs.generated.BoxDouble_;
import com.github.davidmoten.rtree.fbs.generated.BoxFloat_;
import com.github.davidmoten.rtree.fbs.generated.Node_;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.internal.LeafHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LeafFlatBuffersTest {

    @Mock
    private Context<String, Geometry> context;

    @Mock
    private Function<byte[], String> deserializer;

    @Mock
    private Function<String, byte[]> serializer;

    private LeafFlatBuffers<String, Geometry> leafFlatBuffers;

    @BeforeEach
    void setup() {
        List<Entry<String, Geometry>> entries = new ArrayList<>();
        Entry<String, Geometry> entry = mock(Entry.class);
        Geometry geometry = Geometries.rectangle(0, 0, 10, 10);
        doReturn(geometry).when(entry).geometry();
        doReturn("value").when(entry).value();
        entries.add(entry);
        leafFlatBuffers = new LeafFlatBuffers<>(entries, context, serializer, deserializer);
    }

    @Test
    void testAdd() {
        Entry<String, Geometry> newEntry = mock(Entry.class);
        Geometry newGeometry = Geometries.rectangle(10, 10, 20, 20);
        doReturn(newGeometry).when(newEntry).geometry();
        doReturn("newValue").when(newEntry).value();
        List<com.github.davidmoten.rtree.Node<String, Geometry>> result = leafFlatBuffers.add(newEntry);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testDelete() {
        Entry<String, Geometry> entry = mock(Entry.class);
        Geometry geometry = Geometries.rectangle(0, 0, 10, 10);
        doReturn(geometry).when(entry).geometry();
        doReturn("value").when(entry).value();
        com.github.davidmoten.rtree.internal.NodeAndEntries<String, Geometry> result = leafFlatBuffers.delete(entry, true);
        assertNotNull(result);
    }

    @Test
    void testSearchWithoutBackpressure() {
        Function<Geometry, Boolean> condition = geometry -> true;
        rx.Subscriber<Entry<String, Geometry>> subscriber = mock(rx.Subscriber.class);
        leafFlatBuffers.searchWithoutBackpressure(condition, subscriber);
        verify(subscriber).onNext(any());
    }

    @Test
    void testCount() {
        int count = leafFlatBuffers.count();
        assertEquals(1, count);
    }

    @Test
    void testContext() {
        Context<String, Geometry> result = leafFlatBuffers.context();
        assertEquals(context, result);
    }

    @Test
    void testGeometry() {
        Geometry result = leafFlatBuffers.geometry();
        assertNotNull(result);
    }

    @Test
    void testEntries() {
        List<Entry<String, Geometry>> result = leafFlatBuffers.entries();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testEntry() {
        Entry<String, Geometry> result = leafFlatBuffers.entry(0);
        assertNotNull(result);
    }

    @Test
    void testCreateNode() {
        List<Entry<String, Geometry>> entries = new ArrayList<>();
        Entry<String, Geometry> entry = mock(Entry.class);
        Geometry geometry = Geometries.rectangle(0, 0, 10, 10);
        doReturn(geometry).when(entry).geometry();
        doReturn("value").when(entry).value();
        entries.add(entry);
        Node_ node = LeafFlatBuffers.createNode(entries, serializer);
        assertNotNull(node);
    }
}
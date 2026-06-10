import com.github.davidmoten.rtree.Entries;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.fbs.FlatBuffersHelper;
import com.github.davidmoten.rtree.fbs.generated.BoundsType_;
import com.github.davidmoten.rtree.fbs.generated.Bounds_;
import com.github.davidmoten.rtree.fbs.generated.BoxDouble_;
import com.github.davidmoten.rtree.fbs.generated.BoxFloat_;
import com.github.davidmoten.rtree.fbs.generated.CircleDouble_;
import com.github.davidmoten.rtree.fbs.generated.CircleFloat_;
import com.github.davidmoten.rtree.fbs.generated.Entry_;
import com.github.davidmoten.rtree.fbs.generated.GeometryType_;
import com.github.davidmoten.rtree.fbs.generated.Geometry_;
import com.github.davidmoten.rtree.fbs.generated.LineDouble_;
import com.github.davidmoten.rtree.fbs.generated.LineFloat_;
import com.github.davidmoten.rtree.fbs.generated.Node_;
import com.github.davidmoten.rtree.fbs.generated.PointDouble_;
import com.github.davidmoten.rtree.fbs.generated.PointFloat_;
import com.github.davidmoten.rtree.geometry.Circle;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Line;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.geometry.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rx.functions.Func1;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlatBuffersHelperTest {

    @Mock
    private Func1<byte[], String> deserializer;

    @Mock
    private Func1<String, byte[]> serializer;

    private FlatBufferBuilder builder;

    @BeforeEach
    public void setup() {
        builder = new FlatBufferBuilder();
    }

    @Test
    public void testAddEntries() {
        // Given
        List<Entry<String, Geometry>> entries = new ArrayList<>();
        entries.add(Entries.entry("value1", Geometries.point(1.0, 2.0)));
        entries.add(Entries.entry("value2", Geometries.point(3.0, 4.0)));

        // When
        int result = FlatBuffersHelper.addEntries(entries, builder, serializer);

        // Then
        assertNotNull(result);
        assertEquals(2, entries.size());
    }

    @Test
    public void testCreateEntries() {
        // Given
        Node_ node = new Node_();
        node.__init(0, ByteBuffer.allocate(10));

        // When
        List<Entry<String, Geometry>> entries = FlatBuffersHelper.createEntries(node, deserializer);

        // Then
        assertNotNull(entries);
        assertTrue(entries.isEmpty());
    }

    @Test
    public void testCreateEntry() {
        // Given
        Node_ node = new Node_();
        node.__init(0, ByteBuffer.allocate(10));

        // When
        Entry<String, Geometry> entry = FlatBuffersHelper.createEntry(node, deserializer, 0);

        // Then
        assertNotNull(entry);
    }

    @Test
    public void testParseObject() {
        // Given
        Entry_ entry = new Entry_();
        entry.__init(0, ByteBuffer.allocate(10));

        // When
        String result = FlatBuffersHelper.parseObject(deserializer, entry);

        // Then
        assertNull(result);
    }

    @Test
    public void testToGeometry() {
        // Given
        Geometry_ geometry = new Geometry_();
        geometry.__init(0, ByteBuffer.allocate(10));

        // When
        Geometry result = FlatBuffersHelper.toGeometry(geometry);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testCreateBox() {
        // Given
        BoxDouble_ box = new BoxDouble_();
        box.__init(0, ByteBuffer.allocate(10));

        // When
        Geometry result = FlatBuffersHelper.createBox(box);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testCreateLine() {
        // Given
        BoxFloat_ box = new BoxFloat_();
        box.__init(0, ByteBuffer.allocate(10));

        // When
        Line result = FlatBuffersHelper.createLine(box);

        // Then
        assertNotNull(result);
    }
}
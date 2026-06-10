import com.github.davidmoten.rtree.Context;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.Geometry;
import com.github.davidmoten.rtree.InternalStructure;
import com.github.davidmoten.rtree.Leaf;
import com.github.davidmoten.rtree.Node;
import com.github.davidmoten.rtree.NonLeaf;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.Serializer;
import com.github.davidmoten.rtree.fbs.SerializerFlatBuffers;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Rectangle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SerializerFlatBuffersTest {

    @Mock
    private Function<Object, byte[]> serializer;

    @Mock
    private Function<byte[], Object> deserializer;

    private SerializerFlatBuffers<Object, Geometry> serializerFlatBuffers;

    @BeforeEach
    public void setup() {
        serializerFlatBuffers = SerializerFlatBuffers.create(serializer, deserializer);
    }

    @AfterEach
    public void tearDown() {
        serializerFlatBuffers = null;
    }

    @Test
    public void testCreate() {
        // Given
        Function<Object, byte[]> serializer = mock(Function.class);
        Function<byte[], Object> deserializer = mock(Function.class);

        // When
        SerializerFlatBuffers<Object, Geometry> serializerFlatBuffers = SerializerFlatBuffers.create(serializer, deserializer);

        // Then
        assertNotNull(serializerFlatBuffers);
    }

    @Test
    public void testWrite() throws IOException {
        // Given
        RTree<Object, Geometry> tree = mock(RTree.class);
        OutputStream os = new ByteArrayOutputStream();

        doReturn(Optional.of(mock(Node.class))).when(tree).root();
        doReturn(mock(Context.class)).when(tree).context();
        doReturn(10).when(tree).size();

        // When
        serializerFlatBuffers.write(tree, os);

        // Then
        verify(serializer).apply(any());
        verify(deserializer).apply(any());
    }

    @Test
    public void testRead() throws IOException {
        // Given
        InputStream is = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        long sizeBytes = 5;
        InternalStructure structure = InternalStructure.SINGLE_ARRAY;

        // When
        RTree<Object, Geometry> tree = serializerFlatBuffers.read(is, sizeBytes, structure);

        // Then
        assertNotNull(tree);
    }

    @Test
    public void testRead_InvalidInput() {
        // Given
        InputStream is = null;
        long sizeBytes = 5;
        InternalStructure structure = InternalStructure.SINGLE_ARRAY;

        // When and Then
        assertThrows(NullPointerException.class, () -> serializerFlatBuffers.read(is, sizeBytes, structure));
    }

    @Test
    public void testToNodeDefault() {
        // Given
        Node_ node = mock(Node_.class);
        Context<Object, Geometry> context = mock(Context.class);
        Function<byte[], Object> deserializer = mock(Function.class);

        // When
        Node<Object, Geometry> result = serializerFlatBuffers.toNodeDefault(node, context, deserializer);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testToNodeDefault_NullNode() {
        // Given
        Node_ node = null;
        Context<Object, Geometry> context = mock(Context.class);
        Function<byte[], Object> deserializer = mock(Function.class);

        // When and Then
        assertThrows(NullPointerException.class, () -> serializerFlatBuffers.toNodeDefault(node, context, deserializer));
    }

    @Test
    public void testReadFully() throws IOException {
        // Given
        InputStream is = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        int numBytes = 5;

        // When
        byte[] result = serializerFlatBuffers.readFully(is, numBytes);

        // Then
        assertEquals(5, result.length);
    }

    @Test
    public void testReadFully_InvalidInput() {
        // Given
        InputStream is = null;
        int numBytes = 5;

        // When and Then
        assertThrows(NullPointerException.class, () -> serializerFlatBuffers.readFully(is, numBytes));
    }

    @Test
    public void testToBounds() {
        // Given
        FlatBufferBuilder builder = new FlatBufferBuilder();
        Rectangle rectangle = Geometries.rectangle(1, 2, 3, 4);

        // When
        int result = serializerFlatBuffers.toBounds(builder, rectangle);

        // Then
        assertEquals(0, result);
    }

    @Test
    public void testAddNode() {
        // Given
        Node<Object, Geometry> node = mock(Node.class);
        FlatBufferBuilder builder = new FlatBufferBuilder();
        Function<Object, byte[]> serializer = mock(Function.class);

        // When
        int result = serializerFlatBuffers.addNode(node, builder, serializer);

        // Then
        assertEquals(0, result);
    }

    @Test
    public void testAddNode_NullNode() {
        // Given
        Node<Object, Geometry> node = null;
        FlatBufferBuilder builder = new FlatBufferBuilder();
        Function<Object, byte[]> serializer = mock(Function.class);

        // When and Then
        assertThrows(NullPointerException.class, () -> serializerFlatBuffers.addNode(node, builder, serializer));
    }
}
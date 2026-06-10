import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.davidmoten.rtree.Context;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.Geometry;
import com.github.davidmoten.rtree.InternalStructure;
import com.github.davidmoten.rtree.Leaf;
import com.github.davidmoten.rtree.Node;
import com.github.davidmoten.rtree.NonLeaf;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.SelectorRStar;
import com.github.davidmoten.rtree.Serializer;
import com.github.davidmoten.rtree.SerializerHelper;
import com.github.davidmoten.rtree.SplitterRStar;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.internal.FactoryDefault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SerializerKryoTest {

    @Mock
    private Func1<byte[], Object> deserializer;

    @Mock
    private Func1<Object, byte[]> serializer;

    @Mock
    private Func0<Kryo> kryoFactory;

    private SerializerKryo<Object, Geometry> serializerKryo;

    @BeforeEach
    void setup() {
        serializerKryo = new SerializerKryo<>(serializer, deserializer, kryoFactory);
    }

    @Test
    void testCreate() {
        // Given
        Func1<Object, byte[]> serializer = any();
        Func1<byte[], Object> deserializer = any();
        Func0<Kryo> kryoFactory = any();

        // When
        SerializerKryo<Object, Geometry> serializerKryo = SerializerKryo.create(serializer, deserializer, kryoFactory);

        // Then
        assertNotNull(serializerKryo);
    }

    @Test
    void testWrite() throws IOException {
        // Given
        RTree<Object, Geometry> tree = mock(RTree.class);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        // When
        serializerKryo.write(tree, os);

        // Then
        verify(kryoFactory, times(1)).call();
    }

    @Test
    void testRead() throws IOException {
        // Given
        ByteArrayInputStream is = new ByteArrayInputStream(new byte[]{});
        long sizeBytes = 10;
        InternalStructure structure = mock(InternalStructure.class);

        // When
        RTree<Object, Geometry> tree = serializerKryo.read(is, sizeBytes, structure);

        // Then
        assertNotNull(tree);
    }

    @Test
    void testWriteNode() throws IOException {
        // Given
        Node<Object, Geometry> node = mock(Node.class);
        Output output = new Output(new ByteArrayOutputStream());

        // When
        serializerKryo.writeNode(node, output);

        // Then
        verify(node, times(1)).isPresent();
    }

    @Test
    void testWriteValue() throws IOException {
        // Given
        Object value = new Object();
        Output output = new Output(new ByteArrayOutputStream());

        // When
        serializerKryo.writeValue(output, value);

        // Then
        verify(serializer, times(1)).call(value);
    }

    @Test
    void testWriteGeometry() throws IOException {
        // Given
        Geometry geometry = mock(Geometry.class);
        Output output = new Output(new ByteArrayOutputStream());

        // When
        serializerKryo.writeGeometry(output, geometry);

        // Then
        verify(geometry, times(1)).getClass();
    }

    @Test
    void testWriteBounds() throws IOException {
        // Given
        Rectangle rectangle = mock(Rectangle.class);
        Output output = new Output(new ByteArrayOutputStream());

        // When
        serializerKryo.writeBounds(output, rectangle);

        // Then
        verify(rectangle, times(1)).isDoublePrecision();
    }

    @Test
    void testWriteContext() throws IOException {
        // Given
        Context<Object, Geometry> context = mock(Context.class);
        Output output = new Output(new ByteArrayOutputStream());

        // When
        serializerKryo.writeContext(context, output);

        // Then
        verify(context, times(1)).minChildren();
    }

    @Test
    void testReadNode() throws IOException {
        // Given
        Input input = new Input(new ByteArrayInputStream(new byte[]{}));

        // When
        Node<Object, Geometry> node = serializerKryo.readNode(input);

        // Then
        assertNotNull(node);
    }

    @Test
    void testReadContext() throws IOException {
        // Given
        Input input = new Input(new ByteArrayInputStream(new byte[]{}));

        // When
        Context<Object, Geometry> context = serializerKryo.readContext(input);

        // Then
        assertNotNull(context);
    }
}
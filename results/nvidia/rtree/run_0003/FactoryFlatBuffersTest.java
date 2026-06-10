import com.github.davidmoten.guavamini.Preconditions;
import com.github.davidmoten.rtree.Context;
import com.github.davidmoten.rtree.Entries;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.Factory;
import com.github.davidmoten.rtree.Leaf;
import com.github.davidmoten.rtree.Node;
import com.github.davidmoten.rtree.NonLeaf;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FactoryFlatBuffersTest {

    @Mock
    private Func1<Object, byte[]> serializer;

    @Mock
    private Func1<byte[], Object> deserializer;

    @Mock
    private Context<Object, Geometry> context;

    @Mock
    private Geometry geometry;

    private FactoryFlatBuffers<Object, Geometry> factory;

    @BeforeEach
    public void setup() {
        factory = new FactoryFlatBuffers<>(serializer, deserializer);
    }

    @Test
    public void testCreateLeaf() {
        // Given
        List<Entry<Object, Geometry>> entries = new ArrayList<>();
        entries.add(Entries.entry("value", geometry));

        // When
        Leaf<Object, Geometry> leaf = factory.createLeaf(entries, context);

        // Then
        assertNotNull(leaf);
        verify(serializer, never()).apply(any());
        verify(deserializer, never()).apply(any());
    }

    @Test
    public void testCreateNonLeaf() {
        // Given
        List<Node<Object, Geometry>> children = new ArrayList<>();

        // When
        NonLeaf<Object, Geometry> nonLeaf = factory.createNonLeaf(children, context);

        // Then
        assertNotNull(nonLeaf);
        verify(serializer, never()).apply(any());
        verify(deserializer, never()).apply(any());
    }

    @Test
    public void testCreateEntry() {
        // Given
        Object value = "value";

        // When
        Entry<Object, Geometry> entry = factory.createEntry(value, geometry);

        // Then
        assertNotNull(entry);
        verify(serializer, never()).apply(any());
        verify(deserializer, never()).apply(any());
    }

    @Test
    public void testSerializer() {
        // When
        Func1<Object, byte[]> serializerResult = factory.serializer();

        // Then
        assertSame(serializer, serializerResult);
    }

    @Test
    public void testDeserializer() {
        // When
        Func1<byte[], Object> deserializerResult = factory.deserializer();

        // Then
        assertSame(deserializer, deserializerResult);
    }

    @Test
    public void testFactoryFlatBuffersConstructor_NullSerializer() {
        // Given
        Func1<Object, byte[]> nullSerializer = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> new FactoryFlatBuffers<>(nullSerializer, deserializer));
    }

    @Test
    public void testFactoryFlatBuffersConstructor_NullDeserializer() {
        // Given
        Func1<byte[], Object> nullDeserializer = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> new FactoryFlatBuffers<>(serializer, nullDeserializer));
    }
}
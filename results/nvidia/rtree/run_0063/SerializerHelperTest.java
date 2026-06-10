import com.github.davidmoten.rtree.Context;
import com.github.davidmoten.rtree.Node;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SerializerHelperTest {

    @Mock
    private Node<Object, Geometry> node;

    @Mock
    private Context<Object, Geometry> context;

    @BeforeEach
    public void setup() {
        // No setup needed
    }

    @Test
    public void testCreate_RootPresent() {
        // Given
        Optional<Node<Object, Geometry>> root = Optional.of(node);
        int size = 10;

        // When
        RTree<Object, Geometry> rtree = SerializerHelper.create(root, size, context);

        // Then
        assertNotNull(rtree);
        verifyStatic(RTree.class);
        RTree.create(root, size, context);
    }

    @Test
    public void testCreate_RootAbsent() {
        // Given
        Optional<Node<Object, Geometry>> root = Optional.empty();
        int size = 10;

        // When
        RTree<Object, Geometry> rtree = SerializerHelper.create(root, size, context);

        // Then
        assertNotNull(rtree);
        verifyStatic(RTree.class);
        RTree.create(root, size, context);
    }

    @Test
    public void testCreate_NullContext() {
        // Given
        Optional<Node<Object, Geometry>> root = Optional.of(node);
        int size = 10;
        Context<Object, Geometry> nullContext = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> SerializerHelper.create(root, size, nullContext));
    }

    @Test
    public void testCreate_NullRoot() {
        // Given
        Optional<Node<Object, Geometry>> root = null;
        int size = 10;

        // When and Then
        assertThrows(NullPointerException.class, () -> SerializerHelper.create(root, size, context));
    }

    @Test
    public void testCreate_NegativeSize() {
        // Given
        Optional<Node<Object, Geometry>> root = Optional.of(node);
        int size = -10;

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> SerializerHelper.create(root, size, context));
    }
}
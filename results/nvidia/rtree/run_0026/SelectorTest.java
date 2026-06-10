import com.github.davidmoten.rtree.Selector;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
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
public class SelectorTest {

    @Mock
    private Geometry geometry;

    @Mock
    private Rectangle rectangle;

    @Mock
    private Selector selector;

    private List<Node<Object, Geometry>> nodes;

    @BeforeEach
    void setup() {
        nodes = new ArrayList<>();
        Node<Object, Geometry> node1 = mock(Node.class);
        Node<Object, Geometry> node2 = mock(Node.class);
        nodes.add(node1);
        nodes.add(node2);
    }

    @Test
    void testSelect_GeometryIntersectsNode_ReturnsNode() {
        // Given
        when(geometry.intersects(any(Rectangle.class))).thenReturn(true);
        when(nodes.get(0).getBounds()).thenReturn(rectangle);

        // When
        Node<Object, Geometry> selectedNode = selector.select(geometry, nodes);

        // Then
        assertNotNull(selectedNode);
        verify(selector, times(1)).select(geometry, nodes);
    }

    @Test
    void testSelect_GeometryDoesNotIntersectAnyNode_ReturnsNull() {
        // Given
        when(geometry.intersects(any(Rectangle.class))).thenReturn(false);

        // When
        Node<Object, Geometry> selectedNode = selector.select(geometry, nodes);

        // Then
        assertNull(selectedNode);
        verify(selector, times(1)).select(geometry, nodes);
    }

    @Test
    void testSelect_EmptyListNodes_ThrowsException() {
        // Given
        List<Node<Object, Geometry>> emptyNodes = new ArrayList<>();

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> selector.select(geometry, emptyNodes));
        verify(selector, times(1)).select(geometry, emptyNodes);
    }

    @Test
    void testSelect_NullGeometry_ThrowsException() {
        // Given
        Geometry nullGeometry = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> selector.select(nullGeometry, nodes));
        verify(selector, times(1)).select(nullGeometry, nodes);
    }

    @Test
    void testSelect_NullListNodes_ThrowsException() {
        // Given
        List<Node<Object, Geometry>> nullNodes = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> selector.select(geometry, nullNodes));
        verify(selector, times(1)).select(geometry, nullNodes);
    }
}
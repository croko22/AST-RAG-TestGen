import com.github.davidmoten.rtree.SelectorMinimalOverlapArea;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.internal.Comparators;
import com.github.davidmoten.rtree.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SelectorMinimalOverlapAreaTest {

    @Mock
    private Geometry geometry;

    @Mock
    private Rectangle rectangle;

    @Mock
    private Node<?, ?> node1;

    @Mock
    private Node<?, ?> node2;

    private SelectorMinimalOverlapArea selectorMinimalOverlapArea;

    @BeforeEach
    public void setup() {
        selectorMinimalOverlapArea = new SelectorMinimalOverlapArea();
    }

    @Test
    public void testSelect_MinimalOverlapArea() {
        // Given
        List<Node<?, ?>> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);

        when(geometry.mbr()).thenReturn(rectangle);

        // When
        Node<?, ?> selectedNode = selectorMinimalOverlapArea.select(geometry, nodes);

        // Then
        assertNotNull(selectedNode);
        assertEquals(min(nodes, Comparators.overlapAreaThenAreaIncreaseThenAreaComparator(rectangle, nodes)), selectedNode);
    }

    @Test
    public void testSelect_EmptyList() {
        // Given
        List<Node<?, ?>> nodes = new ArrayList<>();

        when(geometry.mbr()).thenReturn(rectangle);

        // When and Then
        assertThrows(Exception.class, () -> selectorMinimalOverlapArea.select(geometry, nodes));
    }

    @Test
    public void testSelect_NullGeometry() {
        // Given
        List<Node<?, ?>> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);

        // When and Then
        assertThrows(NullPointerException.class, () -> selectorMinimalOverlapArea.select(null, nodes));
    }

    @Test
    public void testSelect_NullNodes() {
        // Given
        when(geometry.mbr()).thenReturn(rectangle);

        // When and Then
        assertThrows(NullPointerException.class, () -> selectorMinimalOverlapArea.select(geometry, null));
    }

    @Test
    public void testSelect_SingleNode() {
        // Given
        List<Node<?, ?>> nodes = new ArrayList<>();
        nodes.add(node1);

        when(geometry.mbr()).thenReturn(rectangle);

        // When
        Node<?, ?> selectedNode = selectorMinimalOverlapArea.select(geometry, nodes);

        // Then
        assertNotNull(selectedNode);
        assertEquals(node1, selectedNode);
    }
}
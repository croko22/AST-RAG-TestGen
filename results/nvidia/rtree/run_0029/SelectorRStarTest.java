import com.github.davidmoten.rtree.Selector;
import com.github.davidmoten.rtree.SelectorMinimalOverlapArea;
import com.github.davidmoten.rtree.SelectorMinimalAreaIncrease;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.internal.Leaf;
import com.github.davidmoten.rtree.internal.Node;
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
public class SelectorRStarTest {

    @Mock
    private Geometry geometry;

    @Mock
    private SelectorMinimalOverlapArea overlapAreaSelector;

    @Mock
    private SelectorMinimalAreaIncrease areaIncreaseSelector;

    private SelectorRStar selectorRStar;

    @BeforeEach
    void setup() {
        selectorRStar = new SelectorRStar();
        SelectorRStar.overlapAreaSelector = overlapAreaSelector;
        SelectorRStar.areaIncreaseSelector = areaIncreaseSelector;
    }

    @Test
    void testSelect_LeafNodes() {
        // Given
        List<Node<Object, Geometry>> nodes = new ArrayList<>();
        nodes.add(mock(Leaf.class));
        Node<Object, Geometry> expectedNode = mock(Node.class);
        when(overlapAreaSelector.select(any(), any())).thenReturn(expectedNode);

        // When
        Node<Object, Geometry> selectedNode = selectorRStar.select(geometry, nodes);

        // Then
        assertEquals(expectedNode, selectedNode);
        verify(overlapAreaSelector, times(1)).select(geometry, nodes);
        verify(areaIncreaseSelector, never()).select(any(), any());
    }

    @Test
    void testSelect_NonLeafNodes() {
        // Given
        List<Node<Object, Geometry>> nodes = new ArrayList<>();
        nodes.add(mock(Node.class));
        Node<Object, Geometry> expectedNode = mock(Node.class);
        when(areaIncreaseSelector.select(any(), any())).thenReturn(expectedNode);

        // When
        Node<Object, Geometry> selectedNode = selectorRStar.select(geometry, nodes);

        // Then
        assertEquals(expectedNode, selectedNode);
        verify(areaIncreaseSelector, times(1)).select(geometry, nodes);
        verify(overlapAreaSelector, never()).select(any(), any());
    }

    @Test
    void testSelect_EmptyNodesList() {
        // Given
        List<Node<Object, Geometry>> nodes = new ArrayList<>();

        // When and Then
        assertThrows(IndexOutOfBoundsException.class, () -> selectorRStar.select(geometry, nodes));
        verify(overlapAreaSelector, never()).select(any(), any());
        verify(areaIncreaseSelector, never()).select(any(), any());
    }

    @Test
    void testSelect_NullGeometry() {
        // Given
        List<Node<Object, Geometry>> nodes = new ArrayList<>();
        nodes.add(mock(Node.class));

        // When and Then
        assertThrows(NullPointerException.class, () -> selectorRStar.select(null, nodes));
        verify(overlapAreaSelector, never()).select(any(), any());
        verify(areaIncreaseSelector, never()).select(any(), any());
    }

    @Test
    void testSelect_NullNodesList() {
        // Given

        // When and Then
        assertThrows(NullPointerException.class, () -> selectorRStar.select(geometry, null));
        verify(overlapAreaSelector, never()).select(any(), any());
        verify(areaIncreaseSelector, never()).select(any(), any());
    }
}
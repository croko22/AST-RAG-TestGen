import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NodeFilterTest {

    @Mock
    private Node node;

    @Mock
    private Document document;

    private NodeFilter nodeFilter;

    @BeforeEach
    void setup() {
        nodeFilter = new NodeFilter() {
            @Override
            public NodeFilter.FilterResult head(Node node, int depth) {
                return NodeFilter.FilterResult.CONTINUE;
            }
        };
    }

    @Test
    void testHead() {
        // Given
        when(node.nodeName()).thenReturn("div");

        // When
        NodeFilter.FilterResult result = nodeFilter.head(node, 0);

        // Then
        assertEquals(NodeFilter.FilterResult.CONTINUE, result);
        verify(node, times(1)).nodeName();
    }

    @Test
    void testTail() {
        // Given
        when(node.nodeName()).thenReturn("div");

        // When
        NodeFilter.FilterResult result = nodeFilter.tail(node, 0);

        // Then
        assertEquals(NodeFilter.FilterResult.CONTINUE, result);
        verify(node, times(1)).nodeName();
    }

    @Test
    void testTraverse() {
        // Given
        when(node.childNodes()).thenReturn(mock(java.util.List.class));

        // When
        nodeFilter.traverse(node);

        // Then
        verify(node, times(1)).childNodes();
    }

    @Test
    void testHead_SkipChildren() {
        // Given
        NodeFilter nodeFilter = new NodeFilter() {
            @Override
            public NodeFilter.FilterResult head(Node node, int depth) {
                return NodeFilter.FilterResult.SKIP_CHILDREN;
            }
        };

        // When
        NodeFilter.FilterResult result = nodeFilter.head(node, 0);

        // Then
        assertEquals(NodeFilter.FilterResult.SKIP_CHILDREN, result);
        verify(node, times(1)).nodeName();
    }

    @Test
    void testHead_SkipEntirely() {
        // Given
        NodeFilter nodeFilter = new NodeFilter() {
            @Override
            public NodeFilter.FilterResult head(Node node, int depth) {
                return NodeFilter.FilterResult.SKIP_ENTIRELY;
            }
        };

        // When
        NodeFilter.FilterResult result = nodeFilter.head(node, 0);

        // Then
        assertEquals(NodeFilter.FilterResult.SKIP_ENTIRELY, result);
        verify(node, times(1)).nodeName();
    }

    @Test
    void testHead_Remove() {
        // Given
        NodeFilter nodeFilter = new NodeFilter() {
            @Override
            public NodeFilter.FilterResult head(Node node, int depth) {
                return NodeFilter.FilterResult.REMOVE;
            }
        };

        // When
        NodeFilter.FilterResult result = nodeFilter.head(node, 0);

        // Then
        assertEquals(NodeFilter.FilterResult.REMOVE, result);
        verify(node, times(1)).nodeName();
    }

    @Test
    void testHead_Stop() {
        // Given
        NodeFilter nodeFilter = new NodeFilter() {
            @Override
            public NodeFilter.FilterResult head(Node node, int depth) {
                return NodeFilter.FilterResult.STOP;
            }
        };

        // When
        NodeFilter.FilterResult result = nodeFilter.head(node, 0);

        // Then
        assertEquals(NodeFilter.FilterResult.STOP, result);
        verify(node, times(1)).nodeName();
    }

    @Test
    void testTail_Continue() {
        // Given
        NodeFilter nodeFilter = new NodeFilter() {
            @Override
            public NodeFilter.FilterResult tail(Node node, int depth) {
                return NodeFilter.FilterResult.CONTINUE;
            }
        };

        // When
        NodeFilter.FilterResult result = nodeFilter.tail(node, 0);

        // Then
        assertEquals(NodeFilter.FilterResult.CONTINUE, result);
        verify(node, times(1)).nodeName();
    }

    @Test
    void testTraverse_NullNode() {
        // Given
        Node node = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> nodeFilter.traverse(node));
    }

    @Test
    void testHead_NullNode() {
        // Given
        Node node = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> nodeFilter.head(node, 0));
    }

    @Test
    void testTail_NullNode() {
        // Given
        Node node = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> nodeFilter.tail(node, 0));
    }
}
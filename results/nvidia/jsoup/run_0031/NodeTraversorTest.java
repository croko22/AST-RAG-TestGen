import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NodeTraversorTest {

    @Mock
    private NodeVisitor nodeVisitor;

    @Mock
    private NodeFilter nodeFilter;

    @Mock
    private Node node;

    @Mock
    private Elements elements;

    @BeforeEach
    void setup() {
        // Initialize mock objects
        when(node.childNodeSize()).thenReturn(0);
        when(elements.isEmpty()).thenReturn(true);
    }

    @Test
    public void testTraverse_NodeVisitor_Node() {
        // Given
        when(nodeVisitor.head(any(Node.class), anyInt())).thenReturn();
        when(nodeVisitor.tail(any(Node.class), anyInt())).thenReturn();

        // When
        NodeTraversor.traverse(nodeVisitor, node);

        // Then
        verify(nodeVisitor, times(1)).head(node, 0);
        verify(nodeVisitor, times(1)).tail(node, 0);
    }

    @Test
    public void testTraverse_NodeVisitor_Elements() {
        // Given
        when(elements.isEmpty()).thenReturn(false);
        Element element = mock(Element.class);
        when(elements.first()).thenReturn(element);
        when(nodeVisitor.head(any(Node.class), anyInt())).thenReturn();
        when(nodeVisitor.tail(any(Node.class), anyInt())).thenReturn();

        // When
        NodeTraversor.traverse(nodeVisitor, elements);

        // Then
        verify(nodeVisitor, times(1)).head(element, 0);
        verify(nodeVisitor, times(1)).tail(element, 0);
    }

    @Test
    public void testFilter_NodeFilter_Node() {
        // Given
        when(nodeFilter.head(any(Node.class), anyInt())).thenReturn(NodeFilter.FilterResult.CONTINUE);
        when(nodeFilter.tail(any(Node.class), anyInt())).thenReturn(NodeFilter.FilterResult.CONTINUE);

        // When
        NodeFilter.FilterResult result = NodeTraversor.filter(nodeFilter, node);

        // Then
        assertEquals(NodeFilter.FilterResult.CONTINUE, result);
        verify(nodeFilter, times(1)).head(node, 0);
        verify(nodeFilter, times(1)).tail(node, 0);
    }

    @Test
    public void testFilter_NodeFilter_Elements() {
        // Given
        when(elements.isEmpty()).thenReturn(false);
        Element element = mock(Element.class);
        when(elements.first()).thenReturn(element);
        when(nodeFilter.head(any(Node.class), anyInt())).thenReturn(NodeFilter.FilterResult.CONTINUE);
        when(nodeFilter.tail(any(Node.class), anyInt())).thenReturn(NodeFilter.FilterResult.CONTINUE);

        // When
        NodeTraversor.filter(nodeFilter, elements);

        // Then
        verify(nodeFilter, times(1)).head(element, 0);
        verify(nodeFilter, times(1)).tail(element, 0);
    }

    @Test
    public void testFilter_Stop() {
        // Given
        when(nodeFilter.head(any(Node.class), anyInt())).thenReturn(NodeFilter.FilterResult.STOP);

        // When
        NodeFilter.FilterResult result = NodeTraversor.filter(nodeFilter, node);

        // Then
        assertEquals(NodeFilter.FilterResult.STOP, result);
        verify(nodeFilter, times(1)).head(node, 0);
        verify(nodeFilter, never()).tail(any(Node.class), anyInt());
    }

    @Test
    public void testFilter_Remove() {
        // Given
        when(nodeFilter.head(any(Node.class), anyInt())).thenReturn(NodeFilter.FilterResult.REMOVE);
        doAnswer(invocation -> {
            Node node = invocation.getArgument(0);
            node.remove();
            return null;
        }).when(node).remove();

        // When
        NodeTraversor.filter(nodeFilter, node);

        // Then
        verify(node, times(1)).remove();
    }
}
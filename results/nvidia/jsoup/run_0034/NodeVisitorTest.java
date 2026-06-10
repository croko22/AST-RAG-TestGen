import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
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
public class NodeVisitorTest {

    @Mock
    private Node node;

    @Mock
    private Element element;

    @Mock
    private Document document;

    private NodeVisitor nodeVisitor;

    @BeforeEach
    void setup() {
        nodeVisitor = new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                // Default implementation
            }

            @Override
            public void tail(Node node, int depth) {
                // Default implementation
            }
        };
    }

    @Test
    void testHead() {
        // Given
        when(node.nodeName()).thenReturn("div");

        // When
        nodeVisitor.head(node, 0);

        // Then
        verify(node, times(1)).nodeName();
    }

    @Test
    void testTail() {
        // Given
        when(node.nodeName()).thenReturn("div");

        // When
        nodeVisitor.tail(node, 0);

        // Then
        verify(node, times(1)).nodeName();
    }

    @Test
    void testTraverse() {
        // Given
        when(node.childNodes()).thenReturn(java.util.Collections.emptyList());

        // When
        nodeVisitor.traverse(node);

        // Then
        verify(node, times(1)).childNodes();
    }

    @Test
    void testHeadElement() {
        // Given
        when(element.normalName()).thenReturn("div");

        // When
        nodeVisitor.head(element, 0);

        // Then
        verify(element, times(1)).normalName();
    }

    @Test
    void testTailElement() {
        // Given
        when(element.normalName()).thenReturn("div");

        // When
        nodeVisitor.tail(element, 0);

        // Then
        verify(element, times(1)).normalName();
    }

    @Test
    void testTraverseElement() {
        // Given
        when(element.childNodes()).thenReturn(java.util.Collections.emptyList());

        // When
        nodeVisitor.traverse(element);

        // Then
        verify(element, times(1)).childNodes();
    }

    @Test
    void testHeadTextNode() {
        // Given
        TextNode textNode = new TextNode("Hello World");
        when(textNode.nodeName()).thenReturn("#text");

        // When
        nodeVisitor.head(textNode, 0);

        // Then
        verify(textNode, times(1)).nodeName();
    }

    @Test
    void testTailTextNode() {
        // Given
        TextNode textNode = new TextNode("Hello World");
        when(textNode.nodeName()).thenReturn("#text");

        // When
        nodeVisitor.tail(textNode, 0);

        // Then
        verify(textNode, times(1)).nodeName();
    }

    @Test
    void testTraverseTextNode() {
        // Given
        TextNode textNode = new TextNode("Hello World");
        when(textNode.childNodes()).thenReturn(java.util.Collections.emptyList());

        // When
        nodeVisitor.traverse(textNode);

        // Then
        verify(textNode, times(1)).childNodes();
    }
}
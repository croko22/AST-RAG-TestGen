import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PrinterTest {

    @Mock
    private Node root;

    @Mock
    private Element element;

    @Mock
    private TextNode textNode;

    @Mock
    private Tag tag;

    private Printer printer;

    @BeforeEach
    void setup() {
        printer = new Printer(root, new StringWriter(), new Document.OutputSettings());
    }

    @Test
    void testHead_NodeIsNull() {
        // Given
        Node node = null;
        int depth = 0;

        // When
        printer.head(node, depth);

        // Then
        // No exception is thrown
    }

    @Test
    void testHead_NodeIsTextNode() {
        // Given
        doReturn(TextNode.class).when(textNode).getClass();
        int depth = 0;

        // When
        printer.head(textNode, depth);

        // Then
        verify(textNode).outerHtmlHead(any(), any());
    }

    @Test
    void testHead_NodeIsElement() {
        // Given
        doReturn(Element.class).when(element).getClass();
        int depth = 0;

        // When
        printer.head(element, depth);

        // Then
        verify(element).outerHtmlHead(any(), any());
    }

    @Test
    void testHead_NodeIsLeafNode() {
        // Given
        Node node = new Node() {};
        int depth = 0;

        // When
        printer.head(node, depth);

        // Then
        verify(node).outerHtmlHead(any(), any());
    }

    @Test
    void testTail_NodeIsNull() {
        // Given
        Node node = null;
        int depth = 0;

        // When
        printer.tail(node, depth);

        // Then
        // No exception is thrown
    }

    @Test
    void testTail_NodeIsElement() {
        // Given
        doReturn(Element.class).when(element).getClass();
        int depth = 0;

        // When
        printer.tail(element, depth);

        // Then
        verify(element).outerHtmlTail(any(), any());
    }

    @Test
    void testTail_NodeIsNotElement() {
        // Given
        Node node = new Node() {};
        int depth = 0;

        // When
        printer.tail(node, depth);

        // Then
        // No exception is thrown
    }

    @Test
    void testAddHead_ElementIsNull() {
        // Given
        Element el = null;
        int depth = 0;

        // When
        printer.addHead(el, depth);

        // Then
        // No exception is thrown
    }

    @Test
    void testAddHead_ElementIsNotNull() {
        // Given
        doReturn(Element.class).when(element).getClass();
        int depth = 0;

        // When
        printer.addHead(element, depth);

        // Then
        verify(element).outerHtmlHead(any(), any());
    }

    @Test
    void testAddTail_ElementIsNull() {
        // Given
        Element el = null;
        int depth = 0;

        // When
        printer.addTail(el, depth);

        // Then
        // No exception is thrown
    }

    @Test
    void testAddTail_ElementIsNotNull() {
        // Given
        doReturn(Element.class).when(element).getClass();
        int depth = 0;

        // When
        printer.addTail(element, depth);

        // Then
        verify(element).outerHtmlTail(any(), any());
    }

    @Test
    void testAddText_TextNodeIsNull() {
        // Given
        TextNode textNode = null;
        int textOptions = 0;
        int depth = 0;

        // When
        printer.addText(textNode, textOptions, depth);

        // Then
        // No exception is thrown
    }

    @Test
    void testAddText_TextNodeIsNotNull() {
        // Given
        doReturn(TextNode.class).when(textNode).getClass();
        int textOptions = 0;
        int depth = 0;

        // When
        printer.addText(textNode, textOptions, depth);

        // Then
        verify(textNode).coreValue();
    }

    @Test
    void testAddNode_LeafNodeIsNull() {
        // Given
        Node node = null;
        int depth = 0;

        // When
        printer.addNode(node, depth);

        // Then
        // No exception is thrown
    }

    @Test
    void testAddNode_LeafNodeIsNotNull() {
        // Given
        Node node = new Node() {};
        int depth = 0;

        // When
        printer.addNode(node, depth);

        // Then
        verify(node).outerHtmlHead(any(), any());
    }

    @Test
    void testIndent_DepthIsNegative() {
        // Given
        int depth = -1;

        // When
        printer.indent(depth);

        // Then
        // No exception is thrown
    }

    @Test
    void testIndent_DepthIsZero() {
        // Given
        int depth = 0;

        // When
        printer.indent(depth);

        // Then
        // No exception is thrown
    }

    @Test
    void testIndent_DepthIsPositive() {
        // Given
        int depth = 1;

        // When
        printer.indent(depth);

        // Then
        // No exception is thrown
    }
}
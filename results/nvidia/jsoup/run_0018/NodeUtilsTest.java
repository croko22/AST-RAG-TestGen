import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.NodeUtils;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NodeUtilsTest {

    @Mock
    private Node node;

    @Mock
    private Document document;

    @Mock
    private Element element;

    @Mock
    private Parser parser;

    @BeforeEach
    void setup() {
        when(node.ownerDocument()).thenReturn(document);
        when(document.outputSettings()).thenReturn(new Document.OutputSettings());
        when(document.parser()).thenReturn(parser);
    }

    @Test
    void testOutputSettings_NodeHasOwnerDocument() {
        // Given
        when(node.ownerDocument()).thenReturn(document);

        // When
        Document.OutputSettings outputSettings = NodeUtils.outputSettings(node);

        // Then
        assertNotNull(outputSettings);
        verify(document).outputSettings();
    }

    @Test
    void testOutputSettings_NodeHasNoOwnerDocument() {
        // Given
        when(node.ownerDocument()).thenReturn(null);

        // When
        Document.OutputSettings outputSettings = NodeUtils.outputSettings(node);

        // Then
        assertNotNull(outputSettings);
    }

    @Test
    void testParser_NodeHasOwnerDocument() {
        // Given
        when(node.ownerDocument()).thenReturn(document);

        // When
        Parser parser = NodeUtils.parser(node);

        // Then
        assertNotNull(parser);
        verify(document).parser();
    }

    @Test
    void testParser_NodeHasNoOwnerDocument() {
        // Given
        when(node.ownerDocument()).thenReturn(null);

        // When
        Parser parser = NodeUtils.parser(node);

        // Then
        assertNotNull(parser);
    }

    @Test
    void testSelectXpath_ValidXpath() {
        // Given
        String xpath = "//div";
        when(element.select(any(String.class))).thenReturn(List.of());

        // When
        List<Node> nodes = NodeUtils.selectXpath(xpath, element, Node.class);

        // Then
        assertNotNull(nodes);
    }

    @Test
    void testSelectXpath_InvalidXpath() {
        // Given
        String xpath = "";
        when(element.select(any(String.class))).thenReturn(List.of());

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> NodeUtils.selectXpath(xpath, element, Node.class));
    }

    @Test
    void testStream_Node() {
        // Given

        // When
        Stream<Node> stream = NodeUtils.stream(node, Node.class);

        // Then
        assertNotNull(stream);
    }

    @Test
    void testSpliterator_Iterator() {
        // Given
        Iterator<Node> iterator = mock(Iterator.class);

        // When
        Spliterator<Node> spliterator = NodeUtils.spliterator(iterator);

        // Then
        assertNotNull(spliterator);
    }
}
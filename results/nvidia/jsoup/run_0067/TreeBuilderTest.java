import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.NodeVisitor;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.TokenType;
import org.jsoup.parser.Tokeniser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TreeBuilderTest {

    @Mock
    private Parser parser;

    @Mock
    private Tokeniser tokeniser;

    @Mock
    private NodeVisitor nodeVisitor;

    private TreeBuilder treeBuilder;

    @BeforeEach
    void setup() {
        treeBuilder = new TreeBuilder() {
            @Override
            abstract ParseSettings defaultSettings() {
                return null;
            }

            @Override
            abstract TreeBuilder newInstance() {
                return null;
            }

            @Override
            abstract boolean process(Token token) {
                return false;
            }

            @Override
            abstract List<Node> completeParseFragment() {
                return null;
            }
        };
        treeBuilder.parser = parser;
        treeBuilder.tokeniser = tokeniser;
    }

    @AfterEach
    void tearDown() {
        treeBuilder = null;
    }

    @Test
    void testInitialiseParse() {
        // Given
        String input = "<html><body>Hello World!</body></html>";
        String baseUri = "https://example.com";
        when(parser.defaultNamespace()).thenReturn("html");

        // When
        treeBuilder.initialiseParse(new StringReader(input), baseUri, parser);

        // Then
        assertNotNull(treeBuilder.doc);
        assertEquals(baseUri, treeBuilder.doc.baseUri());
        assertEquals(parser, treeBuilder.parser);
    }

    @Test
    void testCompleteParse() {
        // Given
        treeBuilder.reader = new CharacterReader(new StringReader(""));

        // When
        treeBuilder.completeParse();

        // Then
        assertNull(treeBuilder.reader);
        assertNull(treeBuilder.tokeniser);
        assertNull(treeBuilder.stack);
    }

    @Test
    void testParse() {
        // Given
        String input = "<html><body>Hello World!</body></html>";
        String baseUri = "https://example.com";
        when(parser.defaultNamespace()).thenReturn("html");

        // When
        Document document = treeBuilder.parse(new StringReader(input), baseUri, parser);

        // Then
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    void testParseFragment() {
        // Given
        String input = "<p>Hello World!</p>";
        Element context = mock(Element.class);
        String baseUri = "https://example.com";
        when(parser.defaultNamespace()).thenReturn("html");

        // When
        List<Node> nodes = treeBuilder.parseFragment(new StringReader(input), context, baseUri, parser);

        // Then
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
    }

    @Test
    void testNodeListener() {
        // Given
        NodeVisitor nodeVisitor = mock(NodeVisitor.class);

        // When
        treeBuilder.nodeListener(nodeVisitor);

        // Then
        assertEquals(nodeVisitor, treeBuilder.nodeListener);
    }

    @Test
    void testRunParser() {
        // Given
        when(tokeniser.read()).thenReturn(new Token.EndTag(treeBuilder));

        // When
        treeBuilder.runParser();

        // Then
        verify(tokeniser, atLeastOnce()).read();
    }

    @Test
    void testStepParser() {
        // Given
        when(tokeniser.read()).thenReturn(new Token.EndTag(treeBuilder));

        // When
        boolean result = treeBuilder.stepParser();

        // Then
        assertTrue(result);
        verify(tokeniser, atLeastOnce()).read();
    }

    @Test
    void testProcessStartTag() {
        // Given
        String name = "div";
        StartTag startTag = new StartTag(treeBuilder);
        startTag.name(name);

        // When
        boolean result = treeBuilder.processStartTag(name);

        // Then
        assertTrue(result);
    }

    @Test
    void testProcessStartTagWithAttributes() {
        // Given
        String name = "div";
        Attributes attributes = mock(Attributes.class);
        StartTag startTag = new StartTag(treeBuilder);
        startTag.nameAttr(name, attributes);

        // When
        boolean result = treeBuilder.processStartTag(name, attributes);

        // Then
        assertTrue(result);
    }

    @Test
    void testProcessEndTag() {
        // Given
        String name = "div";
        EndTag endTag = new EndTag(treeBuilder);
        endTag.name(name);

        // When
        boolean result = treeBuilder.processEndTag(name);

        // Then
        assertTrue(result);
    }

    @Test
    void testPop() {
        // Given
        Element element = mock(Element.class);
        treeBuilder.stack = new ArrayList<>();
        treeBuilder.stack.add(element);

        // When
        Element result = treeBuilder.pop();

        // Then
        assertEquals(element, result);
        verify(element).remove();
    }

    @Test
    void testPush() {
        // Given
        Element element = mock(Element.class);

        // When
        treeBuilder.push(element);

        // Then
        assertNotNull(treeBuilder.stack);
        assertEquals(1, treeBuilder.stack.size());
        assertEquals(element, treeBuilder.stack.get(0));
    }

    @Test
    void testEnforceStackDepthLimit() {
        // Given
        when(parser.getMaxDepth()).thenReturn(1);
        Element element = mock(Element.class);
        treeBuilder.stack = new ArrayList<>();
        treeBuilder.stack.add(element);

        // When
        treeBuilder.enforceStackDepthLimit();

        // Then
        verify(element).remove();
    }

    @Test
    void testOnStackPrunedForDepth() {
        // Given
        Element element = mock(Element.class);

        // When
        treeBuilder.onStackPrunedForDepth(element);

        // Then
        // No-op
    }

    @Test
    void testDefaultMaxDepth() {
        // Given

        // When
        int result = treeBuilder.defaultMaxDepth();

        // Then
        assertEquals(512, result);
    }

    @Test
    void testCurrentElement() {
        // Given
        Element element = mock(Element.class);
        treeBuilder.stack = new ArrayList<>();
        treeBuilder.stack.add(element);

        // When
        Element result = treeBuilder.currentElement();

        // Then
        assertEquals(element, result);
    }

    @Test
    void testCurrentElementIs() {
        // Given
        Element element = mock(Element.class);
        treeBuilder.stack = new ArrayList<>();
        treeBuilder.stack.add(element);
        when(element.normalName()).thenReturn("div");

        // When
        boolean result = treeBuilder.currentElementIs("div");

        // Then
        assertTrue(result);
    }

    @Test
    void testError() {
        // Given

        // When
        treeBuilder.error("Error message");

        // Then
        // No-op
    }

    @Test
    void testTagFor() {
        // Given
        String tagName = "div";
        String normalName = "div";
        String namespace = "html";

        // When
        Tag tag = treeBuilder.tagFor(tagName, normalName, namespace, mock(ParseSettings.class));

        // Then
        assertNotNull(tag);
    }

    @Test
    void testOnNodeInserted() {
        // Given
        Node node = mock(Node.class);

        // When
        treeBuilder.onNodeInserted(node);

        // Then
        verify(nodeVisitor).head(node, 0);
    }

    @Test
    void testOnNodeClosed() {
        // Given
        Node node = mock(Node.class);

        // When
        treeBuilder.onNodeClosed(node);

        // Then
        verify(nodeVisitor).tail(node, 0);
    }
}
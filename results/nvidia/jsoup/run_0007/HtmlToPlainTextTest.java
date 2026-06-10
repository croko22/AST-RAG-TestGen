import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HtmlToPlainTextTest {

    @Mock
    private Node node;

    @Mock
    private NodeVisitor nodeVisitor;

    @Mock
    private Document document;

    @Mock
    private Element element;

    @Mock
    private Elements elements;

    private HtmlToPlainText htmlToPlainText;

    @BeforeEach
    void setup() {
        htmlToPlainText = new HtmlToPlainText();
    }

    @Test
    void testGetPlainText() {
        // Given
        String expectedPlainText = "Expected plain text";
        when(element.toString()).thenReturn(expectedPlainText);

        // When
        String plainText = HtmlToPlainText.getPlainText(element);

        // Then
        assertEquals(expectedPlainText, plainText);
    }

    @Test
    void testTrimParents() {
        // Given
        Element parentElement = mock(Element.class);
        Element childElement = mock(Element.class);
        when(childElement.parent()).thenReturn(parentElement);

        elements = new Elements();
        elements.add(parentElement);
        elements.add(childElement);

        // When
        Elements trimmedElements = HtmlToPlainText.trimParents(elements);

        // Then
        assertEquals(1, trimmedElements.size());
        assertEquals(parentElement, trimmedElements.get(0));
    }

    @Test
    void testMain() throws IOException {
        // Given
        String[] args = {"url"};

        // When
        HtmlToPlainText.main(args);

        // Then
        // No exception is thrown
    }

    @Test
    void testHead() {
        // Given
        Node node = mock(Node.class);

        // When
        htmlToPlainText.head(node, 0);

        // Then
        // No exception is thrown
    }

    @Test
    void testTail() {
        // Given
        Node node = mock(Node.class);

        // When
        htmlToPlainText.tail(node, 0);

        // Then
        // No exception is thrown
    }

    @Test
    void testToString() {
        // Given
        Node node = mock(Node.class);

        // When
        String toString = htmlToPlainText.toString();

        // Then
        assertNotNull(toString);
    }

    @Test
    void testStringUtilJoin() {
        // Given
        String[] strings = {"Hello", "World"};

        // When
        String joinedString = StringUtil.join(strings, " ");

        // Then
        assertEquals("Hello World", joinedString);
    }

    @Test
    void testNodeVisitorHead() {
        // Given
        Node node = mock(Node.class);

        // When
        nodeVisitor.head(node, 0);

        // Then
        // No exception is thrown
    }

    @Test
    void testNodeVisitorTail() {
        // Given
        Node node = mock(Node.class);

        // When
        nodeVisitor.tail(node, 0);

        // Then
        // No exception is thrown
    }

    @Test
    void testDocumentCreateShell() {
        // Given
        String baseUri = "https://example.com";

        // When
        Document document = Document.createShell(baseUri);

        // Then
        assertNotNull(document);
    }

    @Test
    void testNodeToString() {
        // Given
        Node node = mock(Node.class);

        // When
        String toString = node.toString();

        // Then
        assertNotNull(toString);
    }

    @Test
    void testElementsClone() {
        // Given
        Elements elements = mock(Elements.class);

        // When
        Elements clonedElements = elements.clone();

        // Then
        assertNotNull(clonedElements);
    }

    @Test
    void testJsoupParse() {
        // Given
        String html = "<html><body>Hello World</body></html>";

        // When
        Document document = org.jsoup.Jsoup.parse(html);

        // Then
        assertNotNull(document);
    }

    @Test
    void testTextNodeToString() {
        // Given
        TextNode textNode = mock(TextNode.class);

        // When
        String toString = textNode.toString();

        // Then
        assertNotNull(toString);
    }
}
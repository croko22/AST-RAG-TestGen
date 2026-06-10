Here's a comprehensive test class for the `Node` class:

```java
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.select.NodeFilter;
import org.jsoup.select.NodeVisitor;

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
public class NodeTest {

    @Mock
    private NodeVisitor nodeVisitor;

    @Mock
    private NodeFilter nodeFilter;

    @Mock
    private Document document;

    @Mock
    private Element element;

    private Node node;

    @BeforeEach
    void setup() {
        node = new TextNode("Test Node");
    }

    @Test
    void testNodeName() {
        // Given
        String expectedName = "#text";

        // When
        String nodeName = node.nodeName();

        // Then
        assertEquals(expectedName, nodeName);
    }

    @Test
    void testNormalName() {
        // Given
        String expectedName = "#text";

        // When
        String normalName = node.normalName();

        // Then
        assertEquals(expectedName, normalName);
    }

    @Test
    void testNodeValue() {
        // Given
        String expectedValue = "Test Node";

        // When
        String nodeValue = node.nodeValue();

        // Then
        assertEquals(expectedValue, nodeValue);
    }

    @Test
    void testNameIs() {
        // Given
        String normalName = "#text";

        // When
        boolean result = node.nameIs(normalName);

        // Then
        assertTrue(result);
    }

    @Test
    void testParentNameIs() {
        // Given
        node.parentNode = element;
        String normalName = "div";

        // When
        when(element.normalName()).thenReturn(normalName);
        boolean result = node.parentNameIs(normalName);

        // Then
        assertTrue(result);
    }

    @Test
    void testParentElementIs() {
        // Given
        node.parentNode = element;
        String normalName = "div";
        String namespace = "http://www.w3.org/1999/xhtml";

        // When
        when(element.normalName()).thenReturn(normalName);
        when(element instanceof Element).thenReturn(true);
        boolean result = node.parentElementIs(normalName, namespace);

        // Then
        assertTrue(result);
    }

    @Test
    void testHasParent() {
        // Given
        node.parentNode = element;

        // When
        boolean result = node.hasParent();

        // Then
        assertTrue(result);
    }

    @Test
    void testAttr() {
        // Given
        String attributeKey = "href";

        // When
        String attr = node.attr(attributeKey);

        // Then
        assertEquals("", attr);
    }

    @Test
    void testAttributes() {
        // Given

        // When
        org.jsoup.nodes.Attributes attributes = node.attributes();

        // Then
        assertNotNull(attributes);
    }

    @Test
    void testAttributesSize() {
        // Given

        // When
        int size = node.attributesSize();

        // Then
        assertEquals(0, size);
    }

    @Test
    void testAttrWithKeyAndValue() {
        // Given
        String attributeKey = "href";
        String attributeValue = "https://www.example.com";

        // When
        Node result = node.attr(attributeKey, attributeValue);

        // Then
        assertSame(node, result);
    }

    @Test
    void testHasAttr() {
        // Given
        String attributeKey = "href";

        // When
        boolean result = node.hasAttr(attributeKey);

        // Then
        assertFalse(result);
    }

    @Test
    void testRemoveAttr() {
        // Given
        String attributeKey = "href";

        // When
        Node result = node.removeAttr(attributeKey);

        // Then
        assertSame(node, result);
    }

    @Test
    void testClearAttributes() {
        // Given

        // When
        Node result = node.clearAttributes();

        // Then
        assertSame(node, result);
    }

    @Test
    void testBaseUri() {
        // Given

        // When
        String baseUri = node.baseUri();

        // Then
        assertEquals("", baseUri);
    }

    @Test
    void testSetBaseUri() {
        // Given
        String baseUri = "https://www.example.com";

        // When
        node.setBaseUri(baseUri);

        // Then
        // No exception thrown
    }

    @Test
    void testAbsUrl() {
        // Given
        String attributeKey = "href";

        // When
        String absUrl = node.absUrl(attributeKey);

        // Then
        assertEquals("", absUrl);
    }

    @Test
    void testChildNode() {
        // Given
        int index = 0;

        // When
        Node childNode = node.childNode(index);

        // Then
        assertNull(childNode);
    }

    @Test
    void testChildNodes() {
        // Given

        // When
        List<Node> childNodes = node.childNodes();

        // Then
        assertNotNull(childNodes);
        assertTrue(childNodes.isEmpty());
    }

    @Test
    void testChildNodesCopy() {
        // Given

        // When
        List<Node> childNodesCopy = node.childNodesCopy();

        // Then
        assertNotNull(childNodesCopy);
        assertTrue(childNodesCopy.isEmpty());
    }

    @Test
    void testChildNodeSize() {
        // Given

        // When
        int size = node.childNodeSize();

        // Then
        assertEquals(0, size);
    }

    @Test
    void testEmpty() {
        // Given

        // When
        Node result = node.empty();

        // Then
        assertSame(node, result);
    }

    @Test
    void testParent() {
        // Given

        // When
        Node parent = node.parent();

        // Then
        assertNull(parent);
    }

    @Test
    void testParentElement() {
        // Given

        // When
        Element parentElement = node.parentElement();

        // Then
        assertNull(parentElement);
    }

    @Test
    void testParentNode() {
        // Given

        // When
        Node parentNode = node.parentNode();

        // Then
        assertNull(parentNode);
    }

    @Test
    void testRoot() {
        // Given

        // When
        Node root = node.root();

        // Then
        assertSame(node, root);
    }

    @Test
    void testOwnerDocument() {
        // Given

        // When
        Document ownerDocument = node.ownerDocument();

        // Then
        assertNull(ownerDocument);
    }

    @Test
    void testRemove() {
        // Given

        // When
        node.remove();

        // Then
        // No exception thrown
    }

    @Test
    void testBefore() {
        // Given
        String html = "<p>Hello World!</p>";

        // When
        Node result = node.before(html);

        // Then
        assertSame(node, result);
    }

    @Test
    void testBeforeNode() {
        // Given
        Node beforeNode = new TextNode("Before Node");

        // When
        Node result = node.before(beforeNode);

        // Then
        assertSame(node, result);
    }

    @Test
    void testAfter() {
        // Given
        String html = "<p>Hello World!</p>";

        // When
        Node result = node.after(html);

        // Then
        assertSame(node, result);
    }

    @Test
    void testAfterNode() {
        // Given
        Node afterNode = new TextNode("After Node");

        // When
        Node result = node.after(afterNode);

        // Then
        assertSame(node, result);
    }

    @Test
    void testWrap() {
        // Given
        String html = "<div></div>";

        // When
        Node result = node.wrap(html);

        // Then
        assertSame(node, result);
    }

    @Test
    void testUnwrap() {
        // Given

        // When
        Node result = node.unwrap();

        // Then
        assertNull(result);
    }

    @Test
    void testReplaceWith() {
        // Given
        Node replaceNode = new TextNode("Replace Node");

        // When
        node.replaceWith(replaceNode);

        // Then
        // No exception thrown
    }

    @Test
    void testSiblingNodes() {
        // Given

        // When
        List<Node> siblingNodes = node.siblingNodes();

        // Then
        assertNotNull(siblingNodes);
        assertTrue(siblingNodes.isEmpty());
    }

    @Test
    void testNextSibling() {
        // Given

        // When
        Node nextSibling = node.nextSibling();

        // Then
        assertNull(nextSibling);
    }

    @Test
    void testPreviousSibling() {
        // Given

        // When
        Node previousSibling = node.previousSibling();

        // Then
        assertNull(previousSibling);
    }

    @Test
    void testSiblingIndex() {
        // Given

        // When
        int siblingIndex = node.siblingIndex();

        // Then
        assertEquals(0, siblingIndex);
    }

    @Test
    void testFirstChild() {
        // Given

        // When
        Node firstChild = node.firstChild();

        // Then
        assertNull(firstChild);
    }

    @Test
    void testLastChild() {
        // Given

        // When
        Node lastChild = node.lastChild();

        // Then
        assertNull(lastChild);
    }

    @Test
    void testFirstSibling() {
        // Given

        // When
        Node firstSibling = node.firstSibling();

        // Then
        assertSame(node, firstSibling);
    }

    @Test
    void testLastSibling() {
        // Given

        // When
        Node lastSibling = node.lastSibling();

        // Then
        assertSame(node, lastSibling);
    }

    @Test
    void testNextElementSibling() {
        // Given

        // When
        Element nextElementSibling = node.nextElementSibling();

        // Then
        assertNull(nextElementSibling);
    }

    @Test
    void testPreviousElementSibling() {
        // Given

        // When
        Element previousElementSibling = node.previousElementSibling();

        // Then
        assertNull(previousElementSibling);
    }

    @Test
    void testTraverse() {
        // Given

        // When
        Node result = node.traverse(nodeVisitor);

        // Then
        assertSame(node, result);
    }

    @Test
    void testForEachNode() {
        // Given

        // When
        Node result = node.forEachNode(node -> {
            // Do nothing
        });

        // Then
        assertSame(node, result);
    }

    @Test
    void testFilter() {
        // Given

        // When
        Node result = node.filter(nodeFilter);

        // Then
        assertSame(node, result);
    }

    @Test
    void testNodeStream() {
        // Given

        // When
        Stream<Node> nodeStream = node.nodeStream();

        // Then
        assertNotNull(nodeStream);
    }

    @Test
    void testOuterHtml() {
        // Given

        // When
        String outerHtml = node.outerHtml();

        // Then
        assertNotNull(outerHtml);
    }

    @Test
    void testHtml() {
        // Given
        StringBuilder appendable = new StringBuilder();

        // When
        StringBuilder result = node.html(appendable);

        // Then
        assertSame(appendable, result);
    }

    @Test
    void testSourceRange() {
        // Given

        // When
        Range sourceRange = node.sourceRange();

        // Then
        assertNotNull(sourceRange);
    }

    @Test
    void testToString() {
        // Given

        // When
        String toString = node.toString();

        // Then
        assertNotNull(toString);
    }

    @Test
    void testEquals() {
        // Given
        Node otherNode = new TextNode("Test Node");

        // When
        boolean result = node.equals(otherNode);

        // Then
        assertFalse(result);
    }

    @Test
    void testHashCode() {
        // Given

        // When
        int hashCode = node.hashCode();

        // Then
        assertNotNull(hashCode);
    }

    @Test
    void testHasSameValue() {
        // Given
        Node otherNode = new TextNode("Test Node");

        // When
        boolean result = node.hasSameValue(otherNode);

        // Then
        assertTrue(result);
    }

    @Test
    void testClone() {
        // Given

        // When
        Node clone = node.clone();

        // Then
        assertNotNull(clone);
        assertNotSame(node, clone);
    }

    @Test
    void testShallowClone() {
        // Given

        // When
        Node shallowClone = node.shallowClone();

        // Then
        assertNotNull(shallowClone);
        assertNotSame(node, shallowClone);
    }
}
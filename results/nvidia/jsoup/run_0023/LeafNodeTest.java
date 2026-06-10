import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeafNodeTest {

    @Mock
    private Element parentNode;

    @Mock
    private Document document;

    private LeafNode leafNode;

    @BeforeEach
    void setup() {
        leafNode = new TextNode("test");
        leafNode.parentNode = parentNode;
    }

    @Test
    void testAttributes() {
        // Given
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        // When
        leafNode.value = attributes;

        // Then
        assertEquals(attributes, leafNode.attributes());
    }

    @Test
    void testParent() {
        // Given
        Element parent = mock(Element.class);

        // When
        leafNode.parentNode = parent;

        // Then
        assertEquals(parent, leafNode.parent());
    }

    @Test
    void testNodeValue() {
        // Given
        String value = "test";

        // When
        leafNode.value = value;

        // Then
        assertEquals(value, leafNode.nodeValue());
    }

    @Test
    void testAttr_KeyEqualsNodeName() {
        // Given
        String nodeName = "test";
        String value = "testValue";

        // When
        leafNode.value = value;

        // Then
        assertEquals(value, leafNode.attr(nodeName));
    }

    @Test
    void testAttr_KeyNotEqualsNodeName() {
        // Given
        String key = "key";
        String nodeName = "test";
        String value = "testValue";

        // When
        leafNode.value = value;

        // Then
        assertEquals("", leafNode.attr(key));
    }

    @Test
    void testAttr_KeyEqualsNodeName_WithAttributes() {
        // Given
        String nodeName = "test";
        Attributes attributes = new Attributes();
        attributes.put(nodeName, "testValue");

        // When
        leafNode.value = attributes;

        // Then
        assertEquals("testValue", leafNode.attr(nodeName));
    }

    @Test
    void testAttr_KeyNotEqualsNodeName_WithAttributes() {
        // Given
        String key = "key";
        String nodeName = "test";
        Attributes attributes = new Attributes();
        attributes.put(nodeName, "testValue");

        // When
        leafNode.value = attributes;

        // Then
        assertEquals("", leafNode.attr(key));
    }

    @Test
    void testAttr_SetValue() {
        // Given
        String key = "key";
        String value = "value";

        // When
        leafNode.attr(key, value);

        // Then
        assertEquals(value, leafNode.attr(key));
    }

    @Test
    void testHasAttr() {
        // Given
        String key = "key";
        Attributes attributes = new Attributes();
        attributes.put(key, "value");

        // When
        leafNode.value = attributes;

        // Then
        assertTrue(leafNode.hasAttr(key));
    }

    @Test
    void testRemoveAttr() {
        // Given
        String key = "key";
        Attributes attributes = new Attributes();
        attributes.put(key, "value");

        // When
        leafNode.value = attributes;
        leafNode.removeAttr(key);

        // Then
        assertFalse(leafNode.hasAttr(key));
    }

    @Test
    void testAbsUrl() {
        // Given
        String key = "key";
        Attributes attributes = new Attributes();
        attributes.put(key, "value");

        // When
        leafNode.value = attributes;

        // Then
        assertEquals("value", leafNode.absUrl(key));
    }

    @Test
    void testBaseUri() {
        // Given
        String baseUri = "baseUri";

        // When
        when(parentNode.baseUri()).thenReturn(baseUri);

        // Then
        assertEquals(baseUri, leafNode.baseUri());
    }

    @Test
    void testChildNodeSize() {
        // Then
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test
    void testEmpty() {
        // Then
        assertSame(leafNode, leafNode.empty());
    }
}
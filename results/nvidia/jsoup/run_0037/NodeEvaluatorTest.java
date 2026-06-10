import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeEvaluator;
import org.jsoup.select.NodeEvaluator.BlankValue;
import org.jsoup.select.NodeEvaluator.ContainsValue;
import org.jsoup.select.NodeEvaluator.InstanceType;
import org.jsoup.select.NodeEvaluator.MatchesValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NodeEvaluatorTest {

    @Mock
    private Element root;

    @Mock
    private Element element;

    @Mock
    private LeafNode leaf;

    @BeforeEach
    void setup() {
        when(root.nodeName()).thenReturn("div");
        when(element.nodeName()).thenReturn("p");
        when(leaf.nodeValue()).thenReturn("Hello World");
    }

    @Test
    public void testInstanceType_Matches() {
        // Given
        InstanceType instanceType = new InstanceType(Element.class, "Element");
        // When
        boolean result = instanceType.matches(root, element);
        // Then
        assertTrue(result);
    }

    @Test
    public void testInstanceType_NotMatches() {
        // Given
        InstanceType instanceType = new InstanceType(TextNode.class, "TextNode");
        // When
        boolean result = instanceType.matches(root, element);
        // Then
        assertFalse(result);
    }

    @Test
    public void testContainsValue_Matches() {
        // Given
        ContainsValue containsValue = new ContainsValue("Hello");
        // When
        boolean result = containsValue.matches(root, leaf);
        // Then
        assertTrue(result);
    }

    @Test
    public void testContainsValue_NotMatches() {
        // Given
        ContainsValue containsValue = new ContainsValue("Foo");
        // When
        boolean result = containsValue.matches(root, leaf);
        // Then
        assertFalse(result);
    }

    @Test
    public void testBlankValue_Matches() {
        // Given
        BlankValue blankValue = new BlankValue();
        when(leaf.nodeValue()).thenReturn("");
        // When
        boolean result = blankValue.matches(root, leaf);
        // Then
        assertTrue(result);
    }

    @Test
    public void testBlankValue_NotMatches() {
        // Given
        BlankValue blankValue = new BlankValue();
        // When
        boolean result = blankValue.matches(root, leaf);
        // Then
        assertFalse(result);
    }

    @Test
    public void testMatchesValue_Matches() {
        // Given
        MatchesValue matchesValue = new MatchesValue(org.jsoup.helper.Regex.compile("Hello"));
        // When
        boolean result = matchesValue.matches(root, leaf);
        // Then
        assertTrue(result);
    }

    @Test
    public void testMatchesValue_NotMatches() {
        // Given
        MatchesValue matchesValue = new MatchesValue(org.jsoup.helper.Regex.compile("Foo"));
        // When
        boolean result = matchesValue.matches(root, leaf);
        // Then
        assertFalse(result);
    }

    @Test
    public void testToString_InstanceType() {
        // Given
        InstanceType instanceType = new InstanceType(Element.class, "Element");
        // When
        String result = instanceType.toString();
        // Then
        assertEquals("::Element", result);
    }

    @Test
    public void testToString_ContainsValue() {
        // Given
        ContainsValue containsValue = new ContainsValue("Hello");
        // When
        String result = containsValue.toString();
        // Then
        assertEquals(":contains(Hello)", result);
    }

    @Test
    public void testToString_BlankValue() {
        // Given
        BlankValue blankValue = new BlankValue();
        // When
        String result = blankValue.toString();
        // Then
        assertEquals(":blank", result);
    }

    @Test
    public void testToString_MatchesValue() {
        // Given
        MatchesValue matchesValue = new MatchesValue(org.jsoup.helper.Regex.compile("Hello"));
        // When
        String result = matchesValue.toString();
        // Then
        assertEquals(":matches(Hello)", result);
    }
}
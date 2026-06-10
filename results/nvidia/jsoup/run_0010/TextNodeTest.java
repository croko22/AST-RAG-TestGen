import org.jsoup.nodes.TextNode;
import org.jsoup.internal.QuietAppendable;
import org.jsoup.internal.StringUtil;
import org.jsoup.helper.Validate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TextNodeTest {

    @Mock
    private QuietAppendable quietAppendable;

    private TextNode textNode;

    @BeforeEach
    void setup() {
        textNode = new TextNode("Test text");
    }

    @Test
    void testNodeName() {
        // When
        String nodeName = textNode.nodeName();

        // Then
        assertEquals("#text", nodeName);
    }

    @Test
    void testText() {
        // Given
        String expectedText = "Test text";

        // When
        String text = textNode.text();

        // Then
        assertEquals(expectedText, text);
    }

    @Test
    void testSetText() {
        // Given
        String newText = "New text";

        // When
        TextNode updatedTextNode = textNode.text(newText);

        // Then
        assertEquals(newText, updatedTextNode.text());
        assertSame(textNode, updatedTextNode);
    }

    @Test
    void testGetWholeText() {
        // Given
        String expectedWholeText = "Test text";

        // When
        String wholeText = textNode.getWholeText();

        // Then
        assertEquals(expectedWholeText, wholeText);
    }

    @Test
    void testIsBlank() {
        // Given
        TextNode blankTextNode = new TextNode("   ");
        TextNode nonBlankTextNode = new TextNode("Test text");

        // When
        boolean isBlank = blankTextNode.isBlank();
        boolean isNonBlank = nonBlankTextNode.isBlank();

        // Then
        assertTrue(isBlank);
        assertFalse(isNonBlank);
    }

    @Test
    void testSplitText() {
        // Given
        int offset = 4;
        String expectedHeadText = "Test";
        String expectedTailText = " text";

        // When
        TextNode tailNode = textNode.splitText(offset);

        // Then
        assertEquals(expectedHeadText, textNode.text());
        assertEquals(expectedTailText, tailNode.text());
    }

    @Test
    void testSplitText_OffsetZero() {
        // Given
        int offset = 0;

        // When
        TextNode tailNode = textNode.splitText(offset);

        // Then
        assertEquals("", textNode.text());
        assertEquals("Test text", tailNode.text());
    }

    @Test
    void testSplitText_OffsetEqualToTextLength() {
        // Given
        int offset = textNode.getWholeText().length();

        // When
        TextNode tailNode = textNode.splitText(offset);

        // Then
        assertEquals("Test text", textNode.text());
        assertEquals("", tailNode.text());
    }

    @Test
    void testSplitText_InvalidOffset() {
        // Given
        int invalidOffset = -1;

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> textNode.splitText(invalidOffset));
    }

    @Test
    void testToString() {
        // Given
        String expectedToString = "Test text";

        // When
        String toString = textNode.toString();

        // Then
        assertEquals(expectedToString, toString);
    }

    @Test
    void testClone() {
        // When
        TextNode clonedTextNode = textNode.clone();

        // Then
        assertEquals(textNode.text(), clonedTextNode.text());
        assertNotSame(textNode, clonedTextNode);
    }

    @Test
    void testCreateFromEncoded() {
        // Given
        String encodedText = "&lt;Test text&gt;";

        // When
        TextNode textNodeFromEncoded = TextNode.createFromEncoded(encodedText);

        // Then
        assertEquals("<Test text>", textNodeFromEncoded.text());
    }
}
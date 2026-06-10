import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HtmlTreeBuilderTest {

    @Mock
    private Parser parser;

    private HtmlTreeBuilder htmlTreeBuilder;

    @BeforeEach
    void setup() {
        htmlTreeBuilder = new HtmlTreeBuilder();
        htmlTreeBuilder.initialiseParse(null, "", parser);
    }

    @Test
    void testDefaultSettings() {
        // Given
        HtmlTreeBuilder htmlTreeBuilder = new HtmlTreeBuilder();

        // When
        ParseSettings defaultSettings = htmlTreeBuilder.defaultSettings();

        // Then
        assertNotNull(defaultSettings);
        assertEquals(ParseSettings.htmlDefault, defaultSettings);
    }

    @Test
    void testNewInstance() {
        // Given
        HtmlTreeBuilder htmlTreeBuilder = new HtmlTreeBuilder();

        // When
        HtmlTreeBuilder newInstance = htmlTreeBuilder.newInstance();

        // Then
        assertNotNull(newInstance);
        assertNotSame(htmlTreeBuilder, newInstance);
    }

    @Test
    void testInitialiseParse() {
        // Given
        String input = "<html><body>Hello World!</body></html>";
        String baseUri = "https://example.com";

        // When
        htmlTreeBuilder.initialiseParse(null, baseUri, parser);

        // Then
        assertEquals(baseUri, htmlTreeBuilder.getBaseUri());
    }

    @Test
    void testProcess() {
        // Given
        Token token = mock(Token.class);

        // When
        boolean result = htmlTreeBuilder.process(token);

        // Then
        assertTrue(result);
    }

    @Test
    void testUseCurrentOrForeignInsert() {
        // Given
        Token token = mock(Token.class);

        // When
        boolean result = htmlTreeBuilder.useCurrentOrForeignInsert(token);

        // Then
        assertTrue(result);
    }

    @Test
    void testInsertElementFor() {
        // Given
        Token.StartTag startTag = mock(Token.StartTag.class);
        when(startTag.name()).thenReturn("div");

        // When
        Element element = htmlTreeBuilder.insertElementFor(startTag);

        // Then
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }

    @Test
    void testInsertForeignElementFor() {
        // Given
        Token.StartTag startTag = mock(Token.StartTag.class);
        when(startTag.name()).thenReturn("div");

        // When
        Element element = htmlTreeBuilder.insertForeignElementFor(startTag, "http://www.w3.org/1999/xhtml");

        // Then
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }

    @Test
    void testInsertEmptyElementFor() {
        // Given
        Token.StartTag startTag = mock(Token.StartTag.class);
        when(startTag.name()).thenReturn("div");

        // When
        Element element = htmlTreeBuilder.insertEmptyElementFor(startTag);

        // Then
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }

    @Test
    void testInsertFormElement() {
        // Given
        Token.StartTag startTag = mock(Token.StartTag.class);
        when(startTag.name()).thenReturn("form");

        // When
        FormElement formElement = htmlTreeBuilder.insertFormElement(startTag, true, true);

        // Then
        assertNotNull(formElement);
        assertEquals("form", formElement.tagName());
    }

    @Test
    void testDoInsertElement() {
        // Given
        Element element = mock(Element.class);

        // When
        htmlTreeBuilder.doInsertElement(element);

        // Then
        verify(parser, times(1)).tokeniser();
    }

    @Test
    void testInsertCommentNode() {
        // Given
        Token.Comment token = mock(Token.Comment.class);
        when(token.getData()).thenReturn("Comment");

        // When
        htmlTreeBuilder.insertCommentNode(token);

        // Then
        verify(parser, times(1)).tokeniser();
    }

    @Test
    void testInsertCharacterNode() {
        // Given
        Token.Character token = mock(Token.Character.class);
        when(token.getData()).thenReturn("Character");

        // When
        htmlTreeBuilder.insertCharacterNode(token);

        // Then
        verify(parser, times(1)).tokeniser();
    }

    @Test
    void testGetStack() {
        // Given

        // When
        List<Element> stack = htmlTreeBuilder.getStack();

        // Then
        assertNotNull(stack);
    }

    @Test
    void testOnStack() {
        // Given
        Element element = mock(Element.class);

        // When
        boolean result = htmlTreeBuilder.onStack(element);

        // Then
        assertFalse(result);
    }

    @Test
    void testGetFromStack() {
        // Given
        String elName = "div";

        // When
        Element element = htmlTreeBuilder.getFromStack(elName);

        // Then
        assertNull(element);
    }

    @Test
    void testRemoveFromStack() {
        // Given
        Element element = mock(Element.class);

        // When
        boolean result = htmlTreeBuilder.removeFromStack(element);

        // Then
        assertFalse(result);
    }

    @Test
    void testPopStackToClose() {
        // Given
        String elName = "div";

        // When
        Element element = htmlTreeBuilder.popStackToClose(elName);

        // Then
        assertNull(element);
    }

    @Test
    void testClearStackToTableContext() {
        // Given

        // When
        htmlTreeBuilder.clearStackToTableContext();

        // Then
        verify(parser, times(1)).tokeniser();
    }

    @Test
    void testResetInsertionMode() {
        // Given

        // When
        boolean result = htmlTreeBuilder.resetInsertionMode();

        // Then
        assertTrue(result);
    }

    @Test
    void testInScope() {
        // Given
        String targetName = "div";

        // When
        boolean result = htmlTreeBuilder.inScope(targetName);

        // Then
        assertFalse(result);
    }

    @Test
    void testGenerateImpliedEndTags() {
        // Given
        String excludeTag = "div";

        // When
        htmlTreeBuilder.generateImpliedEndTags(excludeTag);

        // Then
        verify(parser, times(1)).tokeniser();
    }

    @Test
    void testCloseElement() {
        // Given
        String name = "div";

        // When
        htmlTreeBuilder.closeElement(name);

        // Then
        verify(parser, times(1)).tokeniser();
    }

    @Test
    void testIsSpecial() {
        // Given
        Element element = mock(Element.class);

        // When
        boolean result = HtmlTreeBuilder.isSpecial(element);

        // Then
        assertFalse(result);
    }

    @Test
    void testPushActiveFormattingElements() {
        // Given
        Element element = mock(Element.class);

        // When
        htmlTreeBuilder.pushActiveFormattingElements(element);

        // Then
        verify(parser, times(1)).tokeniser();
    }

    @Test
    void testRemoveFromActiveFormattingElements() {
        // Given
        Element element = mock(Element.class);

        // When
        htmlTreeBuilder.removeFromActiveFormattingElements(element);

        // Then
        verify(parser, times(1)).tokeniser();
    }

    @Test
    void testGetActiveFormattingElement() {
        // Given
        String nodeName = "div";

        // When
        Element element = htmlTreeBuilder.getActiveFormattingElement(nodeName);

        // Then
        assertNull(element);
    }

    @Test
    void testReplaceActiveFormattingElement() {
        // Given
        Element out = mock(Element.class);
        Element in = mock(Element.class);

        // When
        htmlTreeBuilder.replaceActiveFormattingElement(out, in);

        // Then
        verify(parser, times(1)).tokeniser();
    }

    @Test
    void testInsertMarkerToFormattingElements() {
        // Given

        // When
        htmlTreeBuilder.insertMarkerToFormattingElements();

        // Then
        verify(parser, times(1)).tokeniser();
    }

    @Test
    void testInsertInFosterParent() {
        // Given
        Node node = mock(Node.class);

        // When
        htmlTreeBuilder.insertInFosterParent(node);

        // Then
        verify(parser, times(1)).tokeniser();
    }

    @Test
    void testPushTemplateMode() {
        // Given
        HtmlTreeBuilderState state = mock(HtmlTreeBuilderState.class);

        // When
        htmlTreeBuilder.pushTemplateMode(state);

        // Then
        verify(parser, times(1)).tokeniser();
    }

    @Test
    void testPopTemplateMode() {
        // Given

        // When
        HtmlTreeBuilderState state = htmlTreeBuilder.popTemplateMode();

        // Then
        assertNull(state);
    }

    @Test
    void testCurrentTemplateMode() {
        // Given

        // When
        HtmlTreeBuilderState state = htmlTreeBuilder.currentTemplateMode();

        // Then
        assertNull(state);
    }
}
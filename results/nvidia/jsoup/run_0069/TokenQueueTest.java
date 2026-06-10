import org.jsoup.parser.TokenQueue;
import org.jsoup.parser.CharacterReader;
import org.jsoup.internal.StringUtil;
import org.jsoup.helper.Validate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenQueueTest {

    @Mock
    private CharacterReader reader;

    @InjectMocks
    private TokenQueue tokenQueue;

    @BeforeEach
    public void setup() {
        when(reader.isEmpty()).thenReturn(false);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(reader);
    }

    @Test
    public void testIsEmpty() {
        when(reader.isEmpty()).thenReturn(true);
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsume() {
        char c = 'a';
        when(reader.consume()).thenReturn(c);
        assertEquals(c, tokenQueue.consume());
    }

    @Test
    public void testAdvance() {
        tokenQueue.advance();
        verify(reader).advance();
    }

    @Test
    public void testMatches() {
        String seq = "abc";
        when(reader.matchesIgnoreCase(seq)).thenReturn(true);
        assertTrue(tokenQueue.matches(seq));
    }

    @Test
    public void testMatchesChar() {
        char c = 'a';
        when(reader.matches(c)).thenReturn(true);
        assertTrue(tokenQueue.matches(c));
    }

    @Test
    public void testMatchesAny() {
        char[] seq = {'a', 'b', 'c'};
        when(reader.matchesAny(seq)).thenReturn(true);
        assertTrue(tokenQueue.matchesAny(seq));
    }

    @Test
    public void testMatchChomp() {
        String seq = "abc";
        when(reader.matchConsumeIgnoreCase(seq)).thenReturn(true);
        assertTrue(tokenQueue.matchChomp(seq));
    }

    @Test
    public void testMatchChompChar() {
        char c = 'a';
        when(reader.matches(c)).thenReturn(true);
        assertTrue(tokenQueue.matchChomp(c));
    }

    @Test
    public void testMatchesWhitespace() {
        when(reader.current()).thenReturn(' ');
        assertTrue(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        when(reader.current()).thenReturn('a');
        assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testConsumeString() {
        String seq = "abc";
        when(reader.matchConsumeIgnoreCase(seq)).thenReturn(true);
        tokenQueue.consume(seq);
        verify(reader).matchConsumeIgnoreCase(seq);
    }

    @Test
    public void testConsumeTo() {
        String seq = "abc";
        when(reader.consumeTo(seq)).thenReturn("def");
        assertEquals("def", tokenQueue.consumeTo(seq));
    }

    @Test
    public void testConsumeToAny() {
        String[] seq = {"abc", "def"};
        when(reader.consumeToAny()).thenReturn("ghi");
        assertEquals("ghi", tokenQueue.consumeToAny(seq));
    }

    @Test
    public void testChompBalanced() {
        char open = '(';
        char close = ')';
        when(reader.consume()).thenReturn('a');
        assertEquals("a", tokenQueue.chompBalanced(open, close));
    }

    @Test
    public void testUnescape() {
        String in = "\\u0041";
        assertEquals("A", TokenQueue.unescape(in));
    }

    @Test
    public void testEscapeCssIdentifier() {
        String in = "abc";
        assertEquals(in, TokenQueue.escapeCssIdentifier(in));
    }

    @Test
    public void testConsumeWhitespace() {
        when(reader.current()).thenReturn(' ');
        assertTrue(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeElementSelector() {
        when(reader.consume()).thenReturn('a');
        assertEquals("a", tokenQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        when(reader.consume()).thenReturn('a');
        assertEquals("a", tokenQueue.consumeCssIdentifier());
    }

    @Test
    public void testRemainder() {
        when(reader.consumeToEnd()).thenReturn("abc");
        assertEquals("abc", tokenQueue.remainder());
    }

    @Test
    public void testToString() {
        when(reader.toString()).thenReturn("abc");
        assertEquals("abc", tokenQueue.toString());
    }

    @Test
    public void testClose() {
        tokenQueue.close();
        verify(reader).close();
    }
}
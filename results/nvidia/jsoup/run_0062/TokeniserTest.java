import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenData;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokeniserTest {

    @Mock
    private CharacterReader reader;

    @Mock
    private ParseErrorList errors;

    private Tokeniser tokeniser;

    @BeforeEach
    void setup() {
        tokeniser = new Tokeniser(new TreeBuilder(reader, errors));
    }

    @Test
    void testRead() {
        // Given
        when(reader.isEmpty()).thenReturn(false);

        // When
        Token token = tokeniser.read();

        // Then
        assertNotNull(token);
    }

    @Test
    void testEmit() {
        // Given
        Token token = new Token();

        // When
        tokeniser.emit(token);

        // Then
        verify(reader, times(1)).pos();
    }

    @Test
    void testEmitString() {
        // Given
        String str = "test";

        // When
        tokeniser.emit(str);

        // Then
        verify(reader, times(1)).pos();
    }

    @Test
    void testEmitChar() {
        // Given
        char c = 't';

        // When
        tokeniser.emit(c);

        // Then
        verify(reader, times(1)).pos();
    }

    @Test
    void testEmitCodepoints() {
        // Given
        int[] codepoints = {1, 2};

        // When
        tokeniser.emit(codepoints);

        // Then
        verify(reader, times(1)).pos();
    }

    @Test
    void testTransition() {
        // Given
        TokeniserState newState = TokeniserState.Data;

        // When
        tokeniser.transition(newState);

        // Then
        assertEquals(newState, tokeniser.state);
    }

    @Test
    void testAdvanceTransition() {
        // Given
        TokeniserState newState = TokeniserState.Data;

        // When
        tokeniser.advanceTransition(newState);

        // Then
        assertEquals(newState, tokeniser.state);
        verify(reader, times(1)).advance();
    }

    @Test
    void testConsumeCharacterReference() {
        // Given
        when(reader.current()).thenReturn('&');

        // When
        int[] codepoints = tokeniser.consumeCharacterReference(null, false);

        // Then
        assertNotNull(codepoints);
    }

    @Test
    void testCreateTagPending() {
        // Given
        boolean start = true;

        // When
        Token.Tag tag = tokeniser.createTagPending(start);

        // Then
        assertNotNull(tag);
    }

    @Test
    void testCreateXmlDeclPending() {
        // Given
        boolean isDeclaration = true;

        // When
        Token.XmlDecl decl = tokeniser.createXmlDeclPending(isDeclaration);

        // Then
        assertNotNull(decl);
    }

    @Test
    void testEmitTagPending() {
        // Given
        tokeniser.tagPending = new Token.StartTag(new TreeBuilder(reader, errors));

        // When
        tokeniser.emitTagPending();

        // Then
        verify(reader, times(1)).pos();
    }

    @Test
    void testCreateCommentPending() {
        // Given

        // When
        tokeniser.createCommentPending();

        // Then
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    void testEmitCommentPending() {
        // Given
        tokeniser.commentPending = new Token.Comment();

        // When
        tokeniser.emitCommentPending();

        // Then
        verify(reader, times(1)).pos();
    }

    @Test
    void testCreateBogusCommentPending() {
        // Given

        // When
        tokeniser.createBogusCommentPending();

        // Then
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    void testCreateDoctypePending() {
        // Given

        // When
        tokeniser.createDoctypePending();

        // Then
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    void testEmitDoctypePending() {
        // Given
        tokeniser.doctypePending = new Token.Doctype();

        // When
        tokeniser.emitDoctypePending();

        // Then
        verify(reader, times(1)).pos();
    }

    @Test
    void testCreateTempBuffer() {
        // Given

        // When
        tokeniser.createTempBuffer();

        // Then
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    void testIsAppropriateEndTagToken() {
        // Given
        tokeniser.lastStartTag = "test";

        // When
        boolean result = tokeniser.isAppropriateEndTagToken();

        // Then
        assertTrue(result);
    }

    @Test
    void testAppropriateEndTagName() {
        // Given
        tokeniser.lastStartTag = "test";

        // When
        String result = tokeniser.appropriateEndTagName();

        // Then
        assertEquals("test", result);
    }

    @Test
    void testAppropriateEndTagSeq() {
        // Given
        tokeniser.lastStartTag = "test";

        // When
        String result = tokeniser.appropriateEndTagSeq();

        // Then
        assertEquals("</test", result);
    }

    @Test
    void testError() {
        // Given
        TokeniserState state = TokeniserState.Data;

        // When
        tokeniser.error(state);

        // Then
        verify(errors, times(1)).add(any());
    }

    @Test
    void testEofError() {
        // Given
        TokeniserState state = TokeniserState.Data;

        // When
        tokeniser.eofError(state);

        // Then
        verify(errors, times(1)).add(any());
    }

    @Test
    void testCharacterReferenceError() {
        // Given
        String message = "test";

        // When
        tokeniser.characterReferenceError(message);

        // Then
        verify(errors, times(1)).add(any());
    }

    @Test
    void testUnescapeEntities() {
        // Given
        when(reader.isEmpty()).thenReturn(false);

        // When
        String result = tokeniser.unescapeEntities(false);

        // Then
        assertNotNull(result);
    }
}
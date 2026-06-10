import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenTest {

    @Mock
    private Attributes attributes;

    private Token token;

    @BeforeEach
    void setup() {
        token = new Token(TokenType.Doctype) {};
    }

    @Test
    void testTokenType() {
        assertEquals(TokenType.Doctype, token.type);
    }

    @Test
    void testStartPos() {
        token.startPos(10);
        assertEquals(10, token.startPos());
    }

    @Test
    void testEndPos() {
        token.endPos(20);
        assertEquals(20, token.endPos());
    }

    @Test
    void testReset() {
        token.startPos(10);
        token.endPos(20);
        Token resetToken = token.reset();
        assertEquals(-1, resetToken.startPos());
        assertEquals(-1, resetToken.endPos());
    }

    @Test
    void testTokenTypeToString() {
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    void testDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        assertEquals("test", doctype.getName());
        doctype.pubSysKey = "pubSysKey";
        assertEquals("pubSysKey", doctype.getPubSysKey());
        doctype.publicIdentifier.append("publicIdentifier");
        assertEquals("publicIdentifier", doctype.getPublicIdentifier());
        doctype.systemIdentifier.append("systemIdentifier");
        assertEquals("systemIdentifier", doctype.getSystemIdentifier());
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    void testTag() {
        Token.Tag tag = new Token.StartTag(null) {};
        tag.tagName.append("test");
        assertEquals("test", tag.name());
        tag.normalName = "normalName";
        assertEquals("normalName", tag.normalName());
        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());
        tag.attributes = attributes;
        assertTrue(tag.hasAttributes());
        tag.newAttribute();
        verify(attributes, times(1)).add(anyString(), anyString());
    }

    @Test
    void testStartTag() {
        Token.StartTag startTag = new Token.StartTag(null);
        startTag.tagName.append("test");
        assertEquals("test", startTag.name());
        startTag.attributes = attributes;
        assertEquals(attributes, startTag.attributes);
    }

    @Test
    void testEndTag() {
        Token.EndTag endTag = new Token.EndTag(null);
        endTag.tagName.append("test");
        assertEquals("test", endTag.name());
    }

    @Test
    void testComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("test");
        assertEquals("test", comment.getData());
    }

    @Test
    void testCharacter() {
        Token.Character character = new Token.Character();
        character.data.append("test");
        assertEquals("test", character.getData());
    }

    @Test
    void testCData() {
        Token.CData cData = new Token.CData("test");
        assertEquals("test", cData.getData());
    }

    @Test
    void testXmlDecl() {
        Token.XmlDecl xmlDecl = new Token.XmlDecl(null);
        xmlDecl.tagName.append("test");
        assertEquals("test", xmlDecl.name());
    }

    @Test
    void testEOF() {
        Token.EOF eof = new Token.EOF();
        assertNotNull(eof);
    }

    @Test
    void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());
    }

    @Test
    void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag(null);
        assertTrue(startTag.isStartTag());
    }

    @Test
    void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag(null);
        assertTrue(endTag.isEndTag());
    }

    @Test
    void testIsComment() {
        Token.Comment comment = new Token.Comment();
        assertTrue(comment.isComment());
    }

    @Test
    void testIsCharacter() {
        Token.Character character = new Token.Character();
        assertTrue(character.isCharacter());
    }

    @Test
    void testIsCData() {
        Token.CData cData = new Token.CData("test");
        assertTrue(cData.isCData());
    }

    @Test
    void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }
}
import org.jsoup.nodes.DocumentType;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TokeniserStateTest {

    @Mock
    private Tokeniser tokeniser;

    @Mock
    private CharacterReader characterReader;

    @BeforeEach
    void setup() {
        // Initialize tokeniser and characterReader with mock behavior
    }

    @Test
    void testDataState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on Data state
        TokeniserState.Data.read(tokeniser, characterReader);

        // Then: verify that the character is emitted
        verify(tokeniser).emit(any());
    }

    @Test
    void testCharacterReferenceInDataState() {
        // Given: characterReader returns '&'
        doReturn('&').when(characterReader).current();

        // When: read method is called on CharacterReferenceInData state
        TokeniserState.CharacterReferenceInData.read(tokeniser, characterReader);

        // Then: verify that the character reference is handled
        verify(tokeniser).consumeCharacterReference(any(), any());
    }

    @Test
    void testRcdataState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on Rcdata state
        TokeniserState.Rcdata.read(tokeniser, characterReader);

        // Then: verify that the character is emitted
        verify(tokeniser).emit(any());
    }

    @Test
    void testCharacterReferenceInRcdataState() {
        // Given: characterReader returns '&'
        doReturn('&').when(characterReader).current();

        // When: read method is called on CharacterReferenceInRcdata state
        TokeniserState.CharacterReferenceInRcdata.read(tokeniser, characterReader);

        // Then: verify that the character reference is handled
        verify(tokeniser).consumeCharacterReference(any(), any());
    }

    @Test
    void testTagOpenState() {
        // Given: characterReader returns '<'
        doReturn('<').when(characterReader).current();

        // When: read method is called on TagOpen state
        TokeniserState.TagOpen.read(tokeniser, characterReader);

        // Then: verify that the tag open is handled
        verify(tokeniser).createTagPending(any());
    }

    @Test
    void testEndTagOpenState() {
        // Given: characterReader returns '/'
        doReturn('/').when(characterReader).current();

        // When: read method is called on EndTagOpen state
        TokeniserState.EndTagOpen.read(tokeniser, characterReader);

        // Then: verify that the end tag open is handled
        verify(tokeniser).createTagPending(any());
    }

    @Test
    void testTagNameState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on TagName state
        TokeniserState.TagName.read(tokeniser, characterReader);

        // Then: verify that the tag name is handled
        verify(tokeniser).tagPending.appendTagName(any());
    }

    @Test
    void testRcdataLessthanSignState() {
        // Given: characterReader returns '<'
        doReturn('<').when(characterReader).current();

        // When: read method is called on RcdataLessthanSign state
        TokeniserState.RcdataLessthanSign.read(tokeniser, characterReader);

        // Then: verify that the RcdataLessthanSign is handled
        verify(tokeniser).createTempBuffer();
    }

    @Test
    void testRCDATAEndTagOpenState() {
        // Given: characterReader returns '/'
        doReturn('/').when(characterReader).current();

        // When: read method is called on RCDATAEndTagOpen state
        TokeniserState.RCDATAEndTagOpen.read(tokeniser, characterReader);

        // Then: verify that the RCDATAEndTagOpen is handled
        verify(tokeniser).createTagPending(any());
    }

    @Test
    void testRCDATAEndTagNameState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on RCDATAEndTagName state
        TokeniserState.RCDATAEndTagName.read(tokeniser, characterReader);

        // Then: verify that the RCDATAEndTagName is handled
        verify(tokeniser).tagPending.appendTagName(any());
    }

    @Test
    void testRawtextLessthanSignState() {
        // Given: characterReader returns '<'
        doReturn('<').when(characterReader).current();

        // When: read method is called on RawtextLessthanSign state
        TokeniserState.RawtextLessthanSign.read(tokeniser, characterReader);

        // Then: verify that the RawtextLessthanSign is handled
        verify(tokeniser).createTempBuffer();
    }

    @Test
    void testRawtextEndTagOpenState() {
        // Given: characterReader returns '/'
        doReturn('/').when(characterReader).current();

        // When: read method is called on RawtextEndTagOpen state
        TokeniserState.RawtextEndTagOpen.read(tokeniser, characterReader);

        // Then: verify that the RawtextEndTagOpen is handled
        verify(tokeniser).createTagPending(any());
    }

    @Test
    void testRawtextEndTagNameState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on RawtextEndTagName state
        TokeniserState.RawtextEndTagName.read(tokeniser, characterReader);

        // Then: verify that the RawtextEndTagName is handled
        verify(tokeniser).tagPending.appendTagName(any());
    }

    @Test
    void testScriptDataLessthanSignState() {
        // Given: characterReader returns '<'
        doReturn('<').when(characterReader).current();

        // When: read method is called on ScriptDataLessthanSign state
        TokeniserState.ScriptDataLessthanSign.read(tokeniser, characterReader);

        // Then: verify that the ScriptDataLessthanSign is handled
        verify(tokeniser).createTempBuffer();
    }

    @Test
    void testScriptDataEndTagOpenState() {
        // Given: characterReader returns '/'
        doReturn('/').when(characterReader).current();

        // When: read method is called on ScriptDataEndTagOpen state
        TokeniserState.ScriptDataEndTagOpen.read(tokeniser, characterReader);

        // Then: verify that the ScriptDataEndTagOpen is handled
        verify(tokeniser).createTagPending(any());
    }

    @Test
    void testScriptDataEndTagNameState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on ScriptDataEndTagName state
        TokeniserState.ScriptDataEndTagName.read(tokeniser, characterReader);

        // Then: verify that the ScriptDataEndTagName is handled
        verify(tokeniser).tagPending.appendTagName(any());
    }

    @Test
    void testBeforeAttributeNameState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on BeforeAttributeName state
        TokeniserState.BeforeAttributeName.read(tokeniser, characterReader);

        // Then: verify that the BeforeAttributeName is handled
        verify(tokeniser).tagPending.newAttribute();
    }

    @Test
    void testAttributeNameState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on AttributeName state
        TokeniserState.AttributeName.read(tokeniser, characterReader);

        // Then: verify that the AttributeName is handled
        verify(tokeniser).tagPending.appendAttributeName(any(), any(), any());
    }

    @Test
    void testAfterAttributeNameState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on AfterAttributeName state
        TokeniserState.AfterAttributeName.read(tokeniser, characterReader);

        // Then: verify that the AfterAttributeName is handled
        verify(tokeniser).tagPending.newAttribute();
    }

    @Test
    void testBeforeAttributeValueState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on BeforeAttributeValue state
        TokeniserState.BeforeAttributeValue.read(tokeniser, characterReader);

        // Then: verify that the BeforeAttributeValue is handled
        verify(tokeniser).tagPending.appendAttributeValue(any(), any(), any());
    }

    @Test
    void testAttributeValueDoubleQuotedState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on AttributeValue_doubleQuoted state
        TokeniserState.AttributeValue_doubleQuoted.read(tokeniser, characterReader);

        // Then: verify that the AttributeValue_doubleQuoted is handled
        verify(tokeniser).tagPending.appendAttributeValue(any(), any(), any());
    }

    @Test
    void testAttributeValueSingleQuotedState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on AttributeValue_singleQuoted state
        TokeniserState.AttributeValue_singleQuoted.read(tokeniser, characterReader);

        // Then: verify that the AttributeValue_singleQuoted is handled
        verify(tokeniser).tagPending.appendAttributeValue(any(), any(), any());
    }

    @Test
    void testAttributeValueUnquotedState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on AttributeValue_unquoted state
        TokeniserState.AttributeValue_unquoted.read(tokeniser, characterReader);

        // Then: verify that the AttributeValue_unquoted is handled
        verify(tokeniser).tagPending.appendAttributeValue(any(), any(), any());
    }

    @Test
    void testAfterAttributeValueQuotedState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on AfterAttributeValue_quoted state
        TokeniserState.AfterAttributeValue_quoted.read(tokeniser, characterReader);

        // Then: verify that the AfterAttributeValue_quoted is handled
        verify(tokeniser).tagPending.newAttribute();
    }

    @Test
    void testSelfClosingStartTagState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on SelfClosingStartTag state
        TokeniserState.SelfClosingStartTag.read(tokeniser, characterReader);

        // Then: verify that the SelfClosingStartTag is handled
        verify(tokeniser).tagPending.selfClosing = true;
    }

    @Test
    void testBogusCommentState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on BogusComment state
        TokeniserState.BogusComment.read(tokeniser, characterReader);

        // Then: verify that the BogusComment is handled
        verify(tokeniser).commentPending.append(any());
    }

    @Test
    void testMarkupDeclarationOpenState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on MarkupDeclarationOpen state
        TokeniserState.MarkupDeclarationOpen.read(tokeniser, characterReader);

        // Then: verify that the MarkupDeclarationOpen is handled
        verify(tokeniser).createCommentPending();
    }

    @Test
    void testMarkupProcessingOpenState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on MarkupProcessingOpen state
        TokeniserState.MarkupProcessingOpen.read(tokeniser, characterReader);

        // Then: verify that the MarkupProcessingOpen is handled
        verify(tokeniser).createXmlDeclPending(any());
    }

    @Test
    void testCommentStartState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on CommentStart state
        TokeniserState.CommentStart.read(tokeniser, characterReader);

        // Then: verify that the CommentStart is handled
        verify(tokeniser).commentPending.append(any());
    }

    @Test
    void testCommentStartDashState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on CommentStartDash state
        TokeniserState.CommentStartDash.read(tokeniser, characterReader);

        // Then: verify that the CommentStartDash is handled
        verify(tokeniser).commentPending.append(any());
    }

    @Test
    void testCommentState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on Comment state
        TokeniserState.Comment.read(tokeniser, characterReader);

        // Then: verify that the Comment is handled
        verify(tokeniser).commentPending.append(any());
    }

    @Test
    void testCommentEndDashState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on CommentEndDash state
        TokeniserState.CommentEndDash.read(tokeniser, characterReader);

        // Then: verify that the CommentEndDash is handled
        verify(tokeniser).commentPending.append(any());
    }

    @Test
    void testCommentEndState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on CommentEnd state
        TokeniserState.CommentEnd.read(tokeniser, characterReader);

        // Then: verify that the CommentEnd is handled
        verify(tokeniser).commentPending.append(any());
    }

    @Test
    void testCommentEndBangState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on CommentEndBang state
        TokeniserState.CommentEndBang.read(tokeniser, characterReader);

        // Then: verify that the CommentEndBang is handled
        verify(tokeniser).commentPending.append(any());
    }

    @Test
    void testDoctypeState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on Doctype state
        TokeniserState.Doctype.read(tokeniser, characterReader);

        // Then: verify that the Doctype is handled
        verify(tokeniser).createDoctypePending();
    }

    @Test
    void testBeforeDoctypeNameState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on BeforeDoctypeName state
        TokeniserState.BeforeDoctypeName.read(tokeniser, characterReader);

        // Then: verify that the BeforeDoctypeName is handled
        verify(tokeniser).createDoctypePending();
    }

    @Test
    void testDoctypeNameState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on DoctypeName state
        TokeniserState.DoctypeName.read(tokeniser, characterReader);

        // Then: verify that the DoctypeName is handled
        verify(tokeniser).doctypePending.name.append(any());
    }

    @Test
    void testAfterDoctypeNameState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on AfterDoctypeName state
        TokeniserState.AfterDoctypeName.read(tokeniser, characterReader);

        // Then: verify that the AfterDoctypeName is handled
        verify(tokeniser).doctypePending.forceQuirks = true;
    }

    @Test
    void testAfterDoctypePublicKeywordState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on AfterDoctypePublicKeyword state
        TokeniserState.AfterDoctypePublicKeyword.read(tokeniser, characterReader);

        // Then: verify that the AfterDoctypePublicKeyword is handled
        verify(tokeniser).doctypePending.pubSysKey = DocumentType.PUBLIC_KEY;
    }

    @Test
    void testBeforeDoctypePublicIdentifierState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on BeforeDoctypePublicIdentifier state
        TokeniserState.BeforeDoctypePublicIdentifier.read(tokeniser, characterReader);

        // Then: verify that the BeforeDoctypePublicIdentifier is handled
        verify(tokeniser).doctypePending.publicIdentifier.append(any());
    }

    @Test
    void testDoctypePublicIdentifierDoubleQuotedState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on DoctypePublicIdentifier_doubleQuoted state
        TokeniserState.DoctypePublicIdentifier_doubleQuoted.read(tokeniser, characterReader);

        // Then: verify that the DoctypePublicIdentifier_doubleQuoted is handled
        verify(tokeniser).doctypePending.publicIdentifier.append(any());
    }

    @Test
    void testDoctypePublicIdentifierSingleQuotedState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on DoctypePublicIdentifier_singleQuoted state
        TokeniserState.DoctypePublicIdentifier_singleQuoted.read(tokeniser, characterReader);

        // Then: verify that the DoctypePublicIdentifier_singleQuoted is handled
        verify(tokeniser).doctypePending.publicIdentifier.append(any());
    }

    @Test
    void testAfterDoctypePublicIdentifierState() {
        // Given: characterReader returns a character
        doReturn('a').when(characterReader).current();

        // When: read method is called on AfterDoctypePublicIdentifier state
        TokeniserState.AfterDoctypePublicIdentifier.read(tokeniser, characterReader);

        // Then: verify that the AfterDoctypePublicIdentifier is handled
        verify(tokeniser).doctypePending.force
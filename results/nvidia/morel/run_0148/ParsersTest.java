import net.hydromatic.morel.parse.Parsers;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.NoSuchElementException;

public class ParsersTest {

    @Test
    public void testUnquoteIdentifier_SimpleIdentifier() {
        // Given
        String identifier = "`simple`";

        // When
        String unquotedIdentifier = Parsers.unquoteIdentifier(identifier);

        // Then
        assertEquals("simple", unquotedIdentifier);
    }

    @Test
    public void testUnquoteIdentifier_IdentifierWithBackticks() {
        // Given
        String identifier = "`id``ent`";

        // When
        String unquotedIdentifier = Parsers.unquoteIdentifier(identifier);

        // Then
        assertEquals("id`ent", unquotedIdentifier);
    }

    @Test
    public void testUnquoteIdentifier_InvalidIdentifier() {
        // Given
        String identifier = "invalid";

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> Parsers.unquoteIdentifier(identifier));
    }

    @Test
    public void testUnquoteString_SimpleString() {
        // Given
        String string = "\"simple\"";

        // When
        String unquotedString = Parsers.unquoteString(string);

        // Then
        assertEquals("simple", unquotedString);
    }

    @Test
    public void testUnquoteString_StringWithEscapes() {
        // Given
        String string = "\"\\\"\\\\\\n\\t\\r\\b\\f\\v\\a\"";

        // When
        String unquotedString = Parsers.unquoteString(string);

        // Then
        assertEquals("\"\\n\t\r\b\f\v\a", unquotedString);
    }

    @Test
    public void testUnquoteString_InvalidString() {
        // Given
        String string = "invalid";

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> Parsers.unquoteString(string));
    }

    @Test
    public void testUnquoteCharLiteral_SimpleCharLiteral() {
        // Given
        String charLiteral = "#\"a\"";

        // When
        char unquotedChar = Parsers.unquoteCharLiteral(charLiteral);

        // Then
        assertEquals('a', unquotedChar);
    }

    @Test
    public void testUnquoteCharLiteral_CharLiteralWithEscapes() {
        // Given
        String charLiteral = "#\"\\\"\"";

        // When
        char unquotedChar = Parsers.unquoteCharLiteral(charLiteral);

        // Then
        assertEquals('"', unquotedChar);
    }

    @Test
    public void testUnquoteCharLiteral_InvalidCharLiteral() {
        // Given
        String charLiteral = "invalid";

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> Parsers.unquoteCharLiteral(charLiteral));
    }

    @Test
    public void testFromString_SimpleString() {
        // Given
        String string = "a";

        // When
        Character character = Parsers.fromString(string);

        // Then
        assertEquals('a', character);
    }

    @Test
    public void testFromString_EmptyString() {
        // Given
        String string = "";

        // When
        Character character = Parsers.fromString(string);

        // Then
        assertNull(character);
    }

    @Test
    public void testCharToString_SimpleChar() {
        // Given
        char character = 'a';

        // When
        String string = Parsers.charToString(character);

        // Then
        assertEquals("a", string);
    }

    @Test
    public void testCharToString_CharWithEscape() {
        // Given
        char character = '\n';

        // When
        String string = Parsers.charToString(character);

        // Then
        assertEquals("\\n", string);
    }

    @Test
    public void testStringToString_SimpleString() {
        // Given
        String string = "simple";

        // When
        String result = Parsers.stringToString(string);

        // Then
        assertEquals("simple", result);
    }

    @Test
    public void testStringToString_StringWithEscapes() {
        // Given
        String string = "string\nwith\tescapes";

        // When
        String result = Parsers.stringToString(string);

        // Then
        assertEquals("string\\nwith\\tescapes", result);
    }

    @Test
    public void testAppendId_SimpleId() {
        // Given
        StringBuilder buf = new StringBuilder();
        String id = "simple";

        // When
        StringBuilder result = Parsers.appendId(buf, id);

        // Then
        assertEquals("simple", result.toString());
    }

    @Test
    public void testAppendId_IdWithBackticks() {
        // Given
        StringBuilder buf = new StringBuilder();
        String id = "id`ent";

        // When
        StringBuilder result = Parsers.appendId(buf, id);

        // Then
        assertEquals("`id``ent`", result.toString());
    }

    @Test
    public void testAppendId_IdWithSpaces() {
        // Given
        StringBuilder buf = new StringBuilder();
        String id = "id ent";

        // When
        StringBuilder result = Parsers.appendId(buf, id);

        // Then
        assertEquals("`id ent`", result.toString());
    }
}
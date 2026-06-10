import org.jsoup.parser.ParseError;
import org.jsoup.parser.CharacterReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParseErrorTest {

    @Mock
    private CharacterReader characterReader;

    @BeforeEach
    public void setup() {
        when(characterReader.pos()).thenReturn(10);
        when(characterReader.posLineCol()).thenReturn("1:10");
    }

    @Test
    public void testParseError_Constructor1() {
        // Given
        String errorMsg = "Error message";

        // When
        ParseError parseError = new ParseError(characterReader, errorMsg);

        // Then
        assertNotNull(parseError);
        assertEquals(errorMsg, parseError.getErrorMessage());
        assertEquals(10, parseError.getPosition());
        assertEquals("1:10", parseError.getCursorPos());
    }

    @Test
    public void testParseError_Constructor2() {
        // Given
        String errorFormat = "Error %s";
        Object[] args = {"message"};

        // When
        ParseError parseError = new ParseError(characterReader, errorFormat, args);

        // Then
        assertNotNull(parseError);
        assertEquals(String.format(errorFormat, args), parseError.getErrorMessage());
        assertEquals(10, parseError.getPosition());
        assertEquals("1:10", parseError.getCursorPos());
    }

    @Test
    public void testParseError_Constructor3() {
        // Given
        int pos = 20;
        String errorMsg = "Error message";

        // When
        ParseError parseError = new ParseError(pos, errorMsg);

        // Then
        assertNotNull(parseError);
        assertEquals(errorMsg, parseError.getErrorMessage());
        assertEquals(pos, parseError.getPosition());
        assertEquals(String.valueOf(pos), parseError.getCursorPos());
    }

    @Test
    public void testParseError_Constructor4() {
        // Given
        int pos = 20;
        String errorFormat = "Error %s";
        Object[] args = {"message"};

        // When
        ParseError parseError = new ParseError(pos, errorFormat, args);

        // Then
        assertNotNull(parseError);
        assertEquals(String.format(errorFormat, args), parseError.getErrorMessage());
        assertEquals(pos, parseError.getPosition());
        assertEquals(String.valueOf(pos), parseError.getCursorPos());
    }

    @Test
    public void testGetErrorMessage() {
        // Given
        String errorMsg = "Error message";
        ParseError parseError = new ParseError(characterReader, errorMsg);

        // When
        String result = parseError.getErrorMessage();

        // Then
        assertEquals(errorMsg, result);
    }

    @Test
    public void testGetPosition() {
        // Given
        ParseError parseError = new ParseError(characterReader, "Error message");

        // When
        int result = parseError.getPosition();

        // Then
        assertEquals(10, result);
    }

    @Test
    public void testGetCursorPos() {
        // Given
        ParseError parseError = new ParseError(characterReader, "Error message");

        // When
        String result = parseError.getCursorPos();

        // Then
        assertEquals("1:10", result);
    }

    @Test
    public void testToString() {
        // Given
        String errorMsg = "Error message";
        ParseError parseError = new ParseError(characterReader, errorMsg);

        // When
        String result = parseError.toString();

        // Then
        assertEquals("<1:10>: " + errorMsg, result);
    }
}
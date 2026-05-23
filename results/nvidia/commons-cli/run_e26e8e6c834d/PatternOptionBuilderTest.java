import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PatternOptionBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PatternOptionBuilderTest {

    @Test
    public void testGetValueClass_AtSymbol() {
        // Given: the character '@'
        char ch = '@';

        // When: calling getValueClass
        Object result = PatternOptionBuilder.getValueClass(ch);

        // Then: the result should be the OBJECT_VALUE class
        assertEquals(PatternOptionBuilder.OBJECT_VALUE, result);
    }

    @Test
    public void testGetValueClass_Colon() {
        // Given: the character ':'
        char ch = ':';

        // When: calling getValueClass
        Object result = PatternOptionBuilder.getValueClass(ch);

        // Then: the result should be the STRING_VALUE class
        assertEquals(PatternOptionBuilder.STRING_VALUE, result);
    }

    @Test
    public void testGetValueClass_Percent() {
        // Given: the character '%'
        char ch = '%';

        // When: calling getValueClass
        Object result = PatternOptionBuilder.getValueClass(ch);

        // Then: the result should be the NUMBER_VALUE class
        assertEquals(PatternOptionBuilder.NUMBER_VALUE, result);
    }

    @Test
    public void testGetValueClass_Plus() {
        // Given: the character '+'
        char ch = '+';

        // When: calling getValueClass
        Object result = PatternOptionBuilder.getValueClass(ch);

        // Then: the result should be the CLASS_VALUE class
        assertEquals(PatternOptionBuilder.CLASS_VALUE, result);
    }

    @Test
    public void testGetValueClass_Hash() {
        // Given: the character '#'
        char ch = '#';

        // When: calling getValueClass
        Object result = PatternOptionBuilder.getValueClass(ch);

        // Then: the result should be the DATE_VALUE class
        assertEquals(PatternOptionBuilder.DATE_VALUE, result);
    }

    @Test
    public void testGetValueClass_LessThan() {
        // Given: the character '<'
        char ch = '<';

        // When: calling getValueClass
        Object result = PatternOptionBuilder.getValueClass(ch);

        // Then: the result should be the EXISTING_FILE_VALUE class
        assertEquals(PatternOptionBuilder.EXISTING_FILE_VALUE, result);
    }

    @Test
    public void testGetValueClass_GreaterThan() {
        // Given: the character '>'
        char ch = '>';

        // When: calling getValueClass
        Object result = PatternOptionBuilder.getValueClass(ch);

        // Then: the result should be the FILE_VALUE class
        assertEquals(PatternOptionBuilder.FILE_VALUE, result);
    }

    @Test
    public void testGetValueClass_Asterisk() {
        // Given: the character '*'
        char ch = '*';

        // When: calling getValueClass
        Object result = PatternOptionBuilder.getValueClass(ch);

        // Then: the result should be the FILES_VALUE class
        assertEquals(PatternOptionBuilder.FILES_VALUE, result);
    }

    @Test
    public void testGetValueClass_ForwardSlash() {
        // Given: the character '/'
        char ch = '/';

        // When: calling getValueClass
        Object result = PatternOptionBuilder.getValueClass(ch);

        // Then: the result should be the URL_VALUE class
        assertEquals(PatternOptionBuilder.URL_VALUE, result);
    }

    @Test
    public void testGetValueClass_Unknown() {
        // Given: an unknown character
        char ch = '?';

        // When: calling getValueClass
        Object result = PatternOptionBuilder.getValueClass(ch);

        // Then: the result should be null
        assertNull(result);
    }

    @Test
    public void testIsValueCode_AtSymbol() {
        // Given: the character '@'
        char ch = '@';

        // When: calling isValueCode
        boolean result = PatternOptionBuilder.isValueCode(ch);

        // Then: the result should be true
        assertTrue(result);
    }

    @Test
    public void testIsValueCode_Colon() {
        // Given: the character ':'
        char ch = ':';

        // When: calling isValueCode
        boolean result = PatternOptionBuilder.isValueCode(ch);

        // Then: the result should be true
        assertTrue(result);
    }

    @Test
    public void testIsValueCode_Percent() {
        // Given: the character '%'
        char ch = '%';

        // When: calling isValueCode
        boolean result = PatternOptionBuilder.isValueCode(ch);

        // Then: the result should be true
        assertTrue(result);
    }

    @Test
    public void testIsValueCode_Plus() {
        // Given: the character '+'
        char ch = '+';

        // When: calling isValueCode
        boolean result = PatternOptionBuilder.isValueCode(ch);

        // Then: the result should be true
        assertTrue(result);
    }

    @Test
    public void testIsValueCode_Hash() {
        // Given: the character '#'
        char ch = '#';

        // When: calling isValueCode
        boolean result = PatternOptionBuilder.isValueCode(ch);

        // Then: the result should be true
        assertTrue(result);
    }

    @Test
    public void testIsValueCode_LessThan() {
        // Given: the character '<'
        char ch = '<';

        // When: calling isValueCode
        boolean result = PatternOptionBuilder.isValueCode(ch);

        // Then: the result should be true
        assertTrue(result);
    }

    @Test
    public void testIsValueCode_GreaterThan() {
        // Given: the character '>'
        char ch = '>';

        // When: calling isValueCode
        boolean result = PatternOptionBuilder.isValueCode(ch);

        // Then: the result should be true
        assertTrue(result);
    }

    @Test
    public void testIsValueCode_Asterisk() {
        // Given: the character '*'
        char ch = '*';

        // When: calling isValueCode
        boolean result = PatternOptionBuilder.isValueCode(ch);

        // Then: the result should be true
        assertTrue(result);
    }

    @Test
    public void testIsValueCode_ForwardSlash() {
        // Given: the character '/'
        char ch = '/';

        // When: calling isValueCode
        boolean result = PatternOptionBuilder.isValueCode(ch);

        // Then: the result should be true
        assertTrue(result);
    }

    @Test
    public void testIsValueCode_Exclamation() {
        // Given: the character '!'
        char ch = '!';

        // When: calling isValueCode
        boolean result = PatternOptionBuilder.isValueCode(ch);

        // Then: the result should be true
        assertTrue(result);
    }

    @Test
    public void testIsValueCode_Unknown() {
        // Given: an unknown character
        char ch = '?';

        // When: calling isValueCode
        boolean result = PatternOptionBuilder.isValueCode(ch);

        // Then: the result should be false
        assertFalse(result);
    }

    @Test
    public void testParsePattern_SimpleOption() {
        // Given: a simple pattern
        String pattern = "a";

        // When: calling parsePattern
        Options result = PatternOptionBuilder.parsePattern(pattern);

        // Then: the result should contain one option
        assertEquals(1, result.getOptions().size());
        Option option = result.getOptions().get(0);
        assertEquals("a", option.getOpt());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
    }

    @Test
    public void testParsePattern_OptionWithValue() {
        // Given: a pattern with an option and a value
        String pattern = "a:";

        // When: calling parsePattern
        Options result = PatternOptionBuilder.parsePattern(pattern);

        // Then: the result should contain one option with a value
        assertEquals(1, result.getOptions().size());
        Option option = result.getOptions().get(0);
        assertEquals("a", option.getOpt());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
    }

    @Test
    public void testParsePattern_RequiredOption() {
        // Given: a pattern with a required option
        String pattern = "!a";

        // When: calling parsePattern
        Options result = PatternOptionBuilder.parsePattern(pattern);

        // Then: the result should contain one required option
        assertEquals(1, result.getOptions().size());
        Option option = result.getOptions().get(0);
        assertEquals("a", option.getOpt());
        assertFalse(option.hasArg());
        assertTrue(option.isRequired());
    }

    @Test
    public void testParsePattern_MultipleOptions() {
        // Given: a pattern with multiple options
        String pattern = "a:b:c";

        // When: calling parsePattern
        Options result = PatternOptionBuilder.parsePattern(pattern);

        // Then: the result should contain three options
        assertEquals(3, result.getOptions().size());
        Option optionA = result.getOptions().get(0);
        assertEquals("a", optionA.getOpt());
        assertFalse(optionA.hasArg());
        assertFalse(optionA.isRequired());
        Option optionB = result.getOptions().get(1);
        assertEquals("b", optionB.getOpt());
        assertTrue(optionB.hasArg());
        assertFalse(optionB.isRequired());
        Option optionC = result.getOptions().get(2);
        assertEquals("c", optionC.getOpt());
        assertFalse(optionC.hasArg());
        assertFalse(optionC.isRequired());
    }
}
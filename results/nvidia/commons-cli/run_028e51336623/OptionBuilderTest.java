import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OptionBuilderTest {

    @BeforeEach
    public void setUp() {
        OptionBuilder.reset();
    }

    @AfterEach
    public void tearDown() {
        OptionBuilder.reset();
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("longOpt");
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testCreate_WithOpt() {
        OptionBuilder.withLongOpt("longOpt");
        Option option = OptionBuilder.create('o');
        assertNotNull(option);
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("o", option.getOpt());
    }

    @Test
    public void testCreate_WithOptString() {
        OptionBuilder.withLongOpt("longOpt");
        Option option = OptionBuilder.create("o");
        assertNotNull(option);
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("o", option.getOpt());
    }

    @Test
    public void testCreate_WithoutLongOpt() {
        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.create());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertNotNull(optionBuilder);
        assertEquals(1, OptionBuilder.argCount);
    }

    @Test
    public void testHasArg_WithBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        assertNotNull(optionBuilder);
        assertEquals(1, OptionBuilder.argCount);
    }

    @Test
    public void testHasArg_WithBoolean_False() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        assertNotNull(optionBuilder);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.argCount);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertNotNull(optionBuilder);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.argCount);
    }

    @Test
    public void testHasArgs_WithInt() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        assertNotNull(optionBuilder);
        assertEquals(2, OptionBuilder.argCount);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertNotNull(optionBuilder);
        assertEquals(1, OptionBuilder.argCount);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertNotNull(optionBuilder);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.argCount);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs_WithInt() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        assertNotNull(optionBuilder);
        assertEquals(2, OptionBuilder.argCount);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertNotNull(optionBuilder);
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testIsRequired_WithBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        assertNotNull(optionBuilder);
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testIsRequired_WithBoolean_False() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        assertNotNull(optionBuilder);
        assertFalse(OptionBuilder.required);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        assertNotNull(optionBuilder);
        assertEquals("argName", OptionBuilder.argName);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        assertNotNull(optionBuilder);
        assertEquals("description", OptionBuilder.description);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        assertNotNull(optionBuilder);
        assertEquals("longOpt", OptionBuilder.longOption);
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertNotNull(optionBuilder);
        assertEquals(String.class, OptionBuilder.type);
    }

    @Test
    public void testWithType_Object() {
        OptionBuilder optionBuilder = OptionBuilder.withType((Object) String.class);
        assertNotNull(optionBuilder);
        assertEquals(String.class, OptionBuilder.type);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertNotNull(optionBuilder);
        assertEquals('=', OptionBuilder.valueSeparator);
    }

    @Test
    public void testWithValueSeparator_Char() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator('=');
        assertNotNull(optionBuilder);
        assertEquals('=', OptionBuilder.valueSeparator);
    }
}
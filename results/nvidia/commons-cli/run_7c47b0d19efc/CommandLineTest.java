import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommandLineTest {

    private CommandLine commandLine;
    private CommandLine.Builder builder;

    @BeforeEach
    public void setup() {
        commandLine = new CommandLine();
        builder = new CommandLine.Builder();
    }

    @Test
    public void testAddArg() {
        // Given
        String arg = "arg1";

        // When
        builder.addArg(arg);

        // Then
        assertEquals(1, builder.build().getArgList().size());
        assertEquals(arg, builder.build().getArgList().get(0));
    }

    @Test
    public void testAddOption() {
        // Given
        Option option = new Option("o", "option", true, "option description");

        // When
        builder.addOption(option);

        // Then
        assertEquals(1, builder.build().getOptions().length);
        assertEquals(option, builder.build().getOptions()[0]);
    }

    @Test
    public void testBuild() {
        // Given
        String arg = "arg1";
        Option option = new Option("o", "option", true, "option description");

        // When
        builder.addArg(arg);
        builder.addOption(option);
        CommandLine builtCommandLine = builder.build();

        // Then
        assertEquals(1, builtCommandLine.getArgList().size());
        assertEquals(arg, builtCommandLine.getArgList().get(0));
        assertEquals(1, builtCommandLine.getOptions().length);
        assertEquals(option, builtCommandLine.getOptions()[0]);
    }

    @Test
    public void testGetArgList() {
        // Given
        String arg1 = "arg1";
        String arg2 = "arg2";

        // When
        commandLine.addArg(arg1);
        commandLine.addArg(arg2);

        // Then
        assertEquals(2, commandLine.getArgList().size());
        assertEquals(arg1, commandLine.getArgList().get(0));
        assertEquals(arg2, commandLine.getArgList().get(1));
    }

    @Test
    public void testGetOptionObject_Char() {
        // Given
        Option option = new Option("o", "option", true, "option description");
        commandLine.addOption(option);

        // When
        Object object = commandLine.getOptionObject('o');

        // Then
        assertNotNull(object);
    }

    @Test
    public void testGetOptionObject_String() {
        // Given
        Option option = new Option("o", "option", true, "option description");
        commandLine.addOption(option);

        // When
        Object object = commandLine.getOptionObject("o");

        // Then
        assertNotNull(object);
    }

    @Test
    public void testGetOptionProperties_Option() {
        // Given
        Option option = new Option("o", "option", true, "option description");
        commandLine.addOption(option);

        // When
        Properties properties = commandLine.getOptionProperties(option);

        // Then
        assertNotNull(properties);
    }

    @Test
    public void testGetOptionProperties_String() {
        // Given
        Option option = new Option("o", "option", true, "option description");
        commandLine.addOption(option);

        // When
        Properties properties = commandLine.getOptionProperties("o");

        // Then
        assertNotNull(properties);
    }

    @Test
    public void testGetOptionValue_Char() {
        // Given
        Option option = new Option("o", "option", true, "option description");
        commandLine.addOption(option);

        // When
        String value = commandLine.getOptionValue('o');

        // Then
        assertNull(value);
    }

    @Test
    public void testGetOptionValue_Char_WithDefaultValue() {
        // Given
        Option option = new Option("o", "option", true, "option description");
        commandLine.addOption(option);
        String defaultValue = "default";

        // When
        String value = commandLine.getOptionValue('o', defaultValue);

        // Then
        assertEquals(defaultValue, value);
    }

    @Test
    public void testGetOptionValue_Option() {
        // Given
        Option option = new Option("o", "option", true, "option description");
        commandLine.addOption(option);

        // When
        String value = commandLine.getOptionValue(option);

        // Then
        assertNull(value);
    }

    @Test
    public void testGetOptionValue_Option_WithDefaultValue() {
        // Given
        Option option = new Option("o", "option", true, "option description");
        commandLine.addOption(option);
        String defaultValue = "default";

        // When
        String value = commandLine.getOptionValue(option, defaultValue);

        // Then
        assertEquals(defaultValue, value);
    }

    @Test
    public void testGetOptionValue_String() {
        // Given
        Option option = new Option("o", "option", true, "option description");
        commandLine.addOption(option);

        // When
        String value = commandLine.getOptionValue("o");

        // Then
        assertNull(value);
    }

    @Test
    public void testGetOptionValue_String_WithDefaultValue() {
        // Given
        Option option = new Option("o", "option", true, "option description");
        commandLine.addOption(option);
        String defaultValue = "default";

        // When
        String value = commandLine.getOptionValue("o", defaultValue);

        // Then
        assertEquals(defaultValue, value);
    }

    @Test
    public void testGetParsedOptionValue_Char() throws Exception {
        // Given
        Option option = new Option("o", "option", true, "option description");
        commandLine.addOption(option);

        // When
        Object parsedValue = commandLine.getParsedOptionValue('o');

        // Then
        assertNull(parsedValue);
    }

    @Test
    public void testGetParsedOptionValue_Option() throws Exception {
        // Given
        Option option = new Option("o", "option", true, "option description");
        commandLine.addOption(option);

        // When
        Object parsedValue = commandLine.getParsedOptionValue(option);

        // Then
        assertNull(parsedValue);
    }

    @Test
    public void testGetParsedOptionValue_String() throws Exception {
        // Given
        Option option = new Option("o", "option", true, "option description");
        commandLine.addOption(option);

        // When
        Object parsedValue = commandLine.getParsedOptionValue("o");

        // Then
        assertNull(parsedValue);
    }

    @Test
    public void testHasOption_Char() {
        // Given
        Option option = new Option("o", "option", true, "option description");
        commandLine.addOption(option);

        // When
        boolean hasOption = commandLine.hasOption('o');

        // Then
        assertTrue(hasOption);
    }

    @Test
    public void testHasOption_Option() {
        // Given
        Option option = new Option("o", "option", true, "option description");
        commandLine.addOption(option);

        // When
        boolean hasOption = commandLine.hasOption(option);

        // Then
        assertTrue(hasOption);
    }

    @Test
    public void testHasOption_String() {
        // Given
        Option option = new Option("o", "option", true, "option description");
        commandLine.addOption(option);

        // When
        boolean hasOption = commandLine.hasOption("o");

        // Then
        assertTrue(hasOption);
    }

    @Test
    public void testIterator() {
        // Given
        Option option1 = new Option("o1", "option1", true, "option1 description");
        Option option2 = new Option("o2", "option2", true, "option2 description");
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        // When
        Iterator<Option> iterator = commandLine.iterator();

        // Then
        assertTrue(iterator.hasNext());
        assertEquals(option1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(option2, iterator.next());
        assertFalse(iterator.hasNext());
    }
}
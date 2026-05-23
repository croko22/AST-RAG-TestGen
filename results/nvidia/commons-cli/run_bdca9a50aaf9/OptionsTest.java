import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OptionsTest {

    private Options options;

    @BeforeEach
    public void setup() {
        options = new Options();
    }

    @Test
    public void testAddOption() {
        // Given
        Option opt = new Option("a", "arg", true, "description");

        // When
        Options result = options.addOption(opt);

        // Then
        assertNotNull(result);
        assertEquals(options, result);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("arg"));
    }

    @Test
    public void testAddOption_StringBooleanString() {
        // Given
        String opt = "a";
        boolean hasArg = true;
        String description = "description";

        // When
        Options result = options.addOption(opt, hasArg, description);

        // Then
        assertNotNull(result);
        assertEquals(options, result);
        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testAddOption_StringString() {
        // Given
        String opt = "a";
        String description = "description";

        // When
        Options result = options.addOption(opt, description);

        // Then
        assertNotNull(result);
        assertEquals(options, result);
        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testAddOption_StringStringBooleanString() {
        // Given
        String opt = "a";
        String longOpt = "arg";
        boolean hasArg = true;
        String description = "description";

        // When
        Options result = options.addOption(opt, longOpt, hasArg, description);

        // Then
        assertNotNull(result);
        assertEquals(options, result);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("arg"));
    }

    @Test
    public void testAddOptionGroup() {
        // Given
        OptionGroup group = new OptionGroup();
        Option opt = new Option("a", "arg", true, "description");
        group.addOption(opt);

        // When
        Options result = options.addOptionGroup(group);

        // Then
        assertNotNull(result);
        assertEquals(options, result);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("arg"));
    }

    @Test
    public void testAddRequiredOption() {
        // Given
        String opt = "a";
        String longOpt = "arg";
        boolean hasArg = true;
        String description = "description";

        // When
        Options result = options.addRequiredOption(opt, longOpt, hasArg, description);

        // Then
        assertNotNull(result);
        assertEquals(options, result);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("arg"));
    }

    @Test
    public void testGetMatchingOptions() {
        // Given
        options.addOption("a", "arg", true, "description");
        options.addOption("b", "bar", true, "description");

        // When
        List<String> result = options.getMatchingOptions("a");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("arg", result.get(0));
    }

    @Test
    public void testGetOption() {
        // Given
        options.addOption("a", "arg", true, "description");

        // When
        Option result = options.getOption("a");

        // Then
        assertNotNull(result);
        assertEquals("a", result.getOpt());
        assertEquals("arg", result.getLongOpt());
    }

    @Test
    public void testGetOptionGroup() {
        // Given
        OptionGroup group = new OptionGroup();
        Option opt = new Option("a", "arg", true, "description");
        group.addOption(opt);
        options.addOptionGroup(group);

        // When
        OptionGroup result = options.getOptionGroup(opt);

        // Then
        assertNotNull(result);
        assertEquals(group, result);
    }

    @Test
    public void testGetOptions() {
        // Given
        options.addOption("a", "arg", true, "description");
        options.addOption("b", "bar", true, "description");

        // When
        Collection<org.apache.commons.cli.Option> result = options.getOptions();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetRequiredOptions() {
        // Given
        options.addOption("a", "arg", true, "description").setRequired(true);
        options.addOption("b", "bar", true, "description").setRequired(true);

        // When
        List result = options.getRequiredOptions();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testHasLongOption() {
        // Given
        options.addOption("a", "arg", true, "description");

        // When
        boolean result = options.hasLongOption("arg");

        // Then
        assertTrue(result);
    }

    @Test
    public void testHasOption() {
        // Given
        options.addOption("a", "arg", true, "description");

        // When
        boolean result = options.hasOption("a");

        // Then
        assertTrue(result);
    }

    @Test
    public void testHasShortOption() {
        // Given
        options.addOption("a", "arg", true, "description");

        // When
        boolean result = options.hasShortOption("a");

        // Then
        assertTrue(result);
    }

    @Test
    public void testToString() {
        // Given
        options.addOption("a", "arg", true, "description");

        // When
        String result = options.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("a"));
        assertTrue(result.contains("arg"));
    }
}
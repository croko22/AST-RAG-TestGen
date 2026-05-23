import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GnuParserTest {

    private GnuParser gnuParser;

    @BeforeEach
    public void setup() {
        gnuParser = new GnuParser();
    }

    @Test
    public void testFlatten_NoOptions() {
        // Given
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};

        // When
        String[] result = gnuParser.flatten(options, arguments, false);

        // Then
        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_WithOption() {
        // Given
        Options options = new Options();
        options.addOption(Option.builder("o").build());
        String[] arguments = {"-o", "arg1"};

        // When
        String[] result = gnuParser.flatten(options, arguments, false);

        // Then
        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_WithOptionAndValue() {
        // Given
        Options options = new Options();
        options.addOption(Option.builder("o").hasArg().build());
        String[] arguments = {"-o=value", "arg1"};

        // When
        String[] result = gnuParser.flatten(options, arguments, false);

        // Then
        String[] expected = {"-o", "value", "arg1"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithSpecialPropertiesOption() {
        // Given
        Options options = new Options();
        options.addOption(Option.builder("D").hasArg().build());
        String[] arguments = {"-Dproperty=value", "arg1"};

        // When
        String[] result = gnuParser.flatten(options, arguments, false);

        // Then
        String[] expected = {"-D", "property=value", "arg1"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithStopAtNonOption() {
        // Given
        Options options = new Options();
        String[] arguments = {"-o", "arg1", "arg2"};

        // When
        String[] result = gnuParser.flatten(options, arguments, true);

        // Then
        String[] expected = {"-o", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithDoubleHyphen() {
        // Given
        Options options = new Options();
        String[] arguments = {"--", "arg1", "arg2"};

        // When
        String[] result = gnuParser.flatten(options, arguments, false);

        // Then
        String[] expected = {"--", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithSingleHyphen() {
        // Given
        Options options = new Options();
        String[] arguments = {"-", "arg1", "arg2"};

        // When
        String[] result = gnuParser.flatten(options, arguments, false);

        // Then
        String[] expected = {"-", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }
}
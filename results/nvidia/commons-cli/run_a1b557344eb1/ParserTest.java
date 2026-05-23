import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParserTest {

    @Mock
    private Options options;

    @InjectMocks
    private Parser parser;

    private CommandLine commandLine;

    @BeforeEach
    public void setup() {
        commandLine = mock(CommandLine.class);
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
    }

    @Test
    public void testParse_Options() throws ParseException {
        // Given
        String[] arguments = new String[0];

        // When
        CommandLine result = parser.parse(options, arguments);

        // Then
        assertNotNull(result);
        verify(options, times(1)).helpOptions();
        verify(options, times(1)).getOptionGroups();
    }

    @Test
    public void testParse_Options_StopAtNonOption() throws ParseException {
        // Given
        String[] arguments = new String[0];

        // When
        CommandLine result = parser.parse(options, arguments, true);

        // Then
        assertNotNull(result);
        verify(options, times(1)).helpOptions();
        verify(options, times(1)).getOptionGroups();
    }

    @Test
    public void testParse_Options_Properties() throws ParseException {
        // Given
        String[] arguments = new String[0];
        Properties properties = new Properties();

        // When
        CommandLine result = parser.parse(options, arguments, properties);

        // Then
        assertNotNull(result);
        verify(options, times(1)).helpOptions();
        verify(options, times(1)).getOptionGroups();
    }

    @Test
    public void testParse_Options_Properties_StopAtNonOption() throws ParseException {
        // Given
        String[] arguments = new String[0];
        Properties properties = new Properties();

        // When
        CommandLine result = parser.parse(options, arguments, properties, true);

        // Then
        assertNotNull(result);
        verify(options, times(1)).helpOptions();
        verify(options, times(1)).getOptionGroups();
    }

    @Test
    public void testProcessArgs_Option_Iterator() throws ParseException {
        // Given
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg()
                .withDescription("option description")
                .create('o');
        ListIterator<String> iterator = mock(ListIterator.class);

        // When
        parser.processArgs(option, iterator);

        // Then
        verify(iterator, times(1)).hasNext();
    }

    @Test
    public void testProcessArgs_Option_Iterator_NoArg() throws ParseException {
        // Given
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("option description")
                .create('o');
        ListIterator<String> iterator = mock(ListIterator.class);

        // When
        parser.processArgs(option, iterator);

        // Then
        verify(iterator, times(1)).hasNext();
    }

    @Test
    public void testProcessArgs_Option_Iterator_RequiredArg() throws ParseException {
        // Given
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg()
                .required()
                .withDescription("option description")
                .create('o');
        ListIterator<String> iterator = mock(ListIterator.class);

        // When
        assertThrows(ParseException.class, () -> parser.processArgs(option, iterator));

        // Then
        verify(iterator, times(1)).hasNext();
    }
}
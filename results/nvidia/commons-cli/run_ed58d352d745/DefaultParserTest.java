import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultParserTest {

    @Mock
    private Options options;

    @Mock
    private Option option;

    private DefaultParser parser;

    @BeforeEach
    public void setup() {
        parser = new DefaultParser();
    }

    @Test
    public void testParse_Options() throws ParseException {
        // Given
        when(options.hasOption(anyString())).thenReturn(true);

        // When
        CommandLine commandLine = parser.parse(options);

        // Then
        assertNotNull(commandLine);
    }

    @Test
    public void testParse_Options_StopAtNonOption() throws ParseException {
        // Given
        when(options.hasOption(anyString())).thenReturn(true);

        // When
        CommandLine commandLine = parser.parse(options, true);

        // Then
        assertNotNull(commandLine);
    }

    @Test
    public void testParse_Options_Properties() throws ParseException {
        // Given
        when(options.hasOption(anyString())).thenReturn(true);
        Properties properties = new Properties();

        // When
        CommandLine commandLine = parser.parse(options, properties);

        // Then
        assertNotNull(commandLine);
    }

    @Test
    public void testParse_Options_Properties_StopAtNonOption() throws ParseException {
        // Given
        when(options.hasOption(anyString())).thenReturn(true);
        Properties properties = new Properties();

        // When
        CommandLine commandLine = parser.parse(options, properties, true);

        // Then
        assertNotNull(commandLine);
    }

    @Test
    public void testBuilder() {
        // When
        DefaultParser.Builder builder = DefaultParser.builder();

        // Then
        assertNotNull(builder);
    }

    @Test
    public void testSetAllowPartialMatching() {
        // When
        DefaultParser.Builder builder = DefaultParser.builder();
        builder.setAllowPartialMatching(true);

        // Then
        assertNotNull(builder);
    }

    @Test
    public void testSetStripLeadingAndTrailingQuotes() {
        // When
        DefaultParser.Builder builder = DefaultParser.builder();
        builder.setStripLeadingAndTrailingQuotes(true);

        // Then
        assertNotNull(builder);
    }

    @Test
    public void testBuild() {
        // When
        DefaultParser.Builder builder = DefaultParser.builder();
        DefaultParser parser = builder.build();

        // Then
        assertNotNull(parser);
    }
}
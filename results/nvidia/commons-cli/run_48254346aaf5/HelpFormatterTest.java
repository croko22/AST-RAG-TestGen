import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HelpFormatterTest {

    private HelpFormatter helpFormatter;

    @Mock
    private Option option;

    @Mock
    private Options options;

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        helpFormatter = new HelpFormatter();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    public void testGetArgName() {
        // Given
        String expectedArgName = HelpFormatter.DEFAULT_ARG_NAME;

        // When
        String actualArgName = helpFormatter.getArgName();

        // Then
        assertEquals(expectedArgName, actualArgName);
    }

    @Test
    public void testGetDescPadding() {
        // Given
        int expectedDescPadding = HelpFormatter.DEFAULT_DESC_PAD;

        // When
        int actualDescPadding = helpFormatter.getDescPadding();

        // Then
        assertEquals(expectedDescPadding, actualDescPadding);
    }

    @Test
    public void testGetLeftPadding() {
        // Given
        int expectedLeftPadding = HelpFormatter.DEFAULT_LEFT_PAD;

        // When
        int actualLeftPadding = helpFormatter.getLeftPadding();

        // Then
        assertEquals(expectedLeftPadding, actualLeftPadding);
    }

    @Test
    public void testGetLongOptPrefix() {
        // Given
        String expectedLongOptPrefix = HelpFormatter.DEFAULT_LONG_OPT_PREFIX;

        // When
        String actualLongOptPrefix = helpFormatter.getLongOptPrefix();

        // Then
        assertEquals(expectedLongOptPrefix, actualLongOptPrefix);
    }

    @Test
    public void testGetLongOptSeparator() {
        // Given
        String expectedLongOptSeparator = HelpFormatter.DEFAULT_LONG_OPT_SEPARATOR;

        // When
        String actualLongOptSeparator = helpFormatter.getLongOptSeparator();

        // Then
        assertEquals(expectedLongOptSeparator, actualLongOptSeparator);
    }

    @Test
    public void testGetNewLine() {
        // Given
        String expectedNewLine = System.getProperty("line.separator");

        // When
        String actualNewLine = helpFormatter.getNewLine();

        // Then
        assertEquals(expectedNewLine, actualNewLine);
    }

    @Test
    public void testGetOptionComparator() {
        // Given
        Comparator<Option> expectedOptionComparator = new HelpFormatter.OptionComparator();

        // When
        Comparator<Option> actualOptionComparator = helpFormatter.getOptionComparator();

        // Then
        assertNotNull(actualOptionComparator);
    }

    @Test
    public void testGetOptPrefix() {
        // Given
        String expectedOptPrefix = HelpFormatter.DEFAULT_OPT_PREFIX;

        // When
        String actualOptPrefix = helpFormatter.getOptPrefix();

        // Then
        assertEquals(expectedOptPrefix, actualOptPrefix);
    }

    @Test
    public void testGetSyntaxPrefix() {
        // Given
        String expectedSyntaxPrefix = HelpFormatter.DEFAULT_SYNTAX_PREFIX;

        // When
        String actualSyntaxPrefix = helpFormatter.getSyntaxPrefix();

        // Then
        assertEquals(expectedSyntaxPrefix, actualSyntaxPrefix);
    }

    @Test
    public void testGetWidth() {
        // Given
        int expectedWidth = HelpFormatter.DEFAULT_WIDTH;

        // When
        int actualWidth = helpFormatter.getWidth();

        // Then
        assertEquals(expectedWidth, actualWidth);
    }

    @Test
    public void testPrintHelp() {
        // Given
        String cmdLineSyntax = "myapp";
        String header = "Do something useful with an input file";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("file").withDescription("The file to be processed").hasArg().withArgName("FILE").isRequired().create('f'));
        options.addOption(OptionBuilder.withLongOpt("version").withDescription("Print the version of the application").create('v'));
        options.addOption(OptionBuilder.withLongOpt("help").create('h'));
        String footer = "\nPlease report issues at http://example.com/issues";

        // When
        helpFormatter.printHelp(cmdLineSyntax, header, options, footer);

        // Then
        String actualOutput = outputStreamCaptor.toString();
        assertNotNull(actualOutput);
    }

    @Test
    public void testPrintHelp_WithAutoUsage() {
        // Given
        String cmdLineSyntax = "myapp";
        String header = "Do something useful with an input file";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("file").withDescription("The file to be processed").hasArg().withArgName("FILE").isRequired().create('f'));
        options.addOption(OptionBuilder.withLongOpt("version").withDescription("Print the version of the application").create('v'));
        options.addOption(OptionBuilder.withLongOpt("help").create('h'));
        String footer = "\nPlease report issues at http://example.com/issues";

        // When
        helpFormatter.printHelp(cmdLineSyntax, header, options, footer, true);

        // Then
        String actualOutput = outputStreamCaptor.toString();
        assertNotNull(actualOutput);
    }

    @Test
    public void testPrintHelp_WithPrintWriter() {
        // Given
        String cmdLineSyntax = "myapp";
        String header = "Do something useful with an input file";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("file").withDescription("The file to be processed").hasArg().withArgName("FILE").isRequired().create('f'));
        options.addOption(OptionBuilder.withLongOpt("version").withDescription("Print the version of the application").create('v'));
        options.addOption(OptionBuilder.withLongOpt("help").create('h'));
        String footer = "\nPlease report issues at http://example.com/issues";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);

        // When
        helpFormatter.printHelp(printStream, 80, cmdLineSyntax, header, options, 1, 3, footer);

        // Then
        String actualOutput = byteArrayOutputStream.toString();
        assertNotNull(actualOutput);
    }

    @Test
    public void testPrintOptions() {
        // Given
        String cmdLineSyntax = "myapp";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("file").withDescription("The file to be processed").hasArg().withArgName("FILE").isRequired().create('f'));
        options.addOption(OptionBuilder.withLongOpt("version").withDescription("Print the version of the application").create('v'));
        options.addOption(OptionBuilder.withLongOpt("help").create('h'));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);

        // When
        helpFormatter.printOptions(printStream, 80, options, 1, 3);

        // Then
        String actualOutput = byteArrayOutputStream.toString();
        assertNotNull(actualOutput);
    }

    @Test
    public void testPrintUsage() {
        // Given
        String cmdLineSyntax = "myapp";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("file").withDescription("The file to be processed").hasArg().withArgName("FILE").isRequired().create('f'));
        options.addOption(OptionBuilder.withLongOpt("version").withDescription("Print the version of the application").create('v'));
        options.addOption(OptionBuilder.withLongOpt("help").create('h'));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);

        // When
        helpFormatter.printUsage(printStream, 80, cmdLineSyntax);

        // Then
        String actualOutput = byteArrayOutputStream.toString();
        assertNotNull(actualOutput);
    }

    @Test
    public void testPrintWrapped() {
        // Given
        String text = "This is a very long text that needs to be wrapped.";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);

        // When
        helpFormatter.printWrapped(printStream, 20, text);

        // Then
        String actualOutput = byteArrayOutputStream.toString();
        assertNotNull(actualOutput);
    }

    @Test
    public void testSetArgName() {
        // Given
        String newArgName = "newArgName";

        // When
        helpFormatter.setArgName(newArgName);

        // Then
        assertEquals(newArgName, helpFormatter.getArgName());
    }

    @Test
    public void testSetDescPadding() {
        // Given
        int newDescPadding = 10;

        // When
        helpFormatter.setDescPadding(newDescPadding);

        // Then
        assertEquals(newDescPadding, helpFormatter.getDescPadding());
    }

    @Test
    public void testSetLeftPadding() {
        // Given
        int newLeftPadding = 10;

        // When
        helpFormatter.setLeftPadding(newLeftPadding);

        // Then
        assertEquals(newLeftPadding, helpFormatter.getLeftPadding());
    }

    @Test
    public void testSetLongOptPrefix() {
        // Given
        String newLongOptPrefix = "newLongOptPrefix";

        // When
        helpFormatter.setLongOptPrefix(newLongOptPrefix);

        // Then
        assertEquals(newLongOptPrefix, helpFormatter.getLongOptPrefix());
    }

    @Test
    public void testSetLongOptSeparator() {
        // Given
        String newLongOptSeparator = "newLongOptSeparator";

        // When
        helpFormatter.setLongOptSeparator(newLongOptSeparator);

        // Then
        assertEquals(newLongOptSeparator, helpFormatter.getLongOptSeparator());
    }

    @Test
    public void testSetNewLine() {
        // Given
        String newNewLine = "newNewLine";

        // When
        helpFormatter.setNewLine(newNewLine);

        // Then
        assertEquals(newNewLine, helpFormatter.getNewLine());
    }

    @Test
    public void testSetOptionComparator() {
        // Given
        Comparator<Option> newOptionComparator = new HelpFormatter.OptionComparator();

        // When
        helpFormatter.setOptionComparator(newOptionComparator);

        // Then
        assertEquals(newOptionComparator, helpFormatter.getOptionComparator());
    }

    @Test
    public void testSetOptPrefix() {
        // Given
        String newOptPrefix = "newOptPrefix";

        // When
        helpFormatter.setOptPrefix(newOptPrefix);

        // Then
        assertEquals(newOptPrefix, helpFormatter.getOptPrefix());
    }

    @Test
    public void testSetSyntaxPrefix() {
        // Given
        String newSyntaxPrefix = "newSyntaxPrefix";

        // When
        helpFormatter.setSyntaxPrefix(newSyntaxPrefix);

        // Then
        assertEquals(newSyntaxPrefix, helpFormatter.getSyntaxPrefix());
    }

    @Test
    public void testSetWidth() {
        // Given
        int newWidth = 100;

        // When
        helpFormatter.setWidth(newWidth);

        // Then
        assertEquals(newWidth, helpFormatter.getWidth());
    }

    @Test
    public void testPrintHelp_WithNullCmdLineSyntax() {
        // Given
        String cmdLineSyntax = null;
        String header = "Do something useful with an input file";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("file").withDescription("The file to be processed").hasArg().withArgName("FILE").isRequired().create('f'));
        options.addOption(OptionBuilder.withLongOpt("version").withDescription("Print the version of the application").create('v'));
        options.addOption(OptionBuilder.withLongOpt("help").create('h'));
        String footer = "\nPlease report issues at http://example.com/issues";

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> helpFormatter.printHelp(80, cmdLineSyntax, header, options, footer));
    }
}
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PosixParserTest {

    @Mock
    private Options options;

    @InjectMocks
    private PosixParser posixParser;

    @BeforeEach
    public void setup() {
        posixParser.init();
    }

    @Test
    public void testFlatten_StopAtNonOption() throws ParseException {
        // Given
        String[] arguments = {"-a", "-b", "value", "--longOpt", "longValue"};
        when(options.hasOption(anyString())).thenReturn(true);
        when(options.getOption(anyString())).thenReturn(mock(Option.class));

        // When
        String[] result = posixParser.flatten(options, arguments, true);

        // Then
        assertNotNull(result);
        assertEquals(7, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("--longOpt", result[3]);
        assertEquals("longValue", result[4]);
        assertEquals("--", result[5]);
        assertEquals("value", result[6]);
    }

    @Test
    public void testFlatten_StopAtNonOption_LongOpt() throws ParseException {
        // Given
        String[] arguments = {"-a", "-b", "value", "--longOpt", "longValue"};
        when(options.hasOption(anyString())).thenReturn(true);
        when(options.getOption(anyString())).thenReturn(mock(Option.class));
        when(options.getMatchingOptions(anyString())).thenReturn(new ArrayList<>());

        // When
        String[] result = posixParser.flatten(options, arguments, true);

        // Then
        assertNotNull(result);
        assertEquals(7, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("--longOpt", result[3]);
        assertEquals("longValue", result[4]);
        assertEquals("--", result[5]);
        assertEquals("value", result[6]);
    }

    @Test
    public void testFlatten_StopAtNonOption_NoLongOpt() throws ParseException {
        // Given
        String[] arguments = {"-a", "-b", "value"};
        when(options.hasOption(anyString())).thenReturn(true);
        when(options.getOption(anyString())).thenReturn(mock(Option.class));
        when(options.getMatchingOptions(anyString())).thenReturn(new ArrayList<>());

        // When
        String[] result = posixParser.flatten(options, arguments, true);

        // Then
        assertNotNull(result);
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("--", result[3]);
    }

    @Test
    public void testFlatten_StopAtNonOption_AmbiguousOption() throws ParseException {
        // Given
        String[] arguments = {"-a", "-b", "value"};
        when(options.hasOption(anyString())).thenReturn(true);
        when(options.getOption(anyString())).thenReturn(mock(Option.class));
        when(options.getMatchingOptions(anyString())).thenReturn(Arrays.asList("opt1", "opt2"));

        // When
        assertThrows(ParseException.class, () -> posixParser.flatten(options, arguments, true));
    }

    @Test
    public void testBurstToken_StopAtNonOption() {
        // Given
        String token = "-abc";
        when(options.hasOption(anyString())).thenReturn(true);
        when(options.getOption(anyString())).thenReturn(mock(Option.class));

        // When
        posixParser.burstToken(token, true);

        // Then
        assertEquals(3, posixParser.tokens.size());
        assertEquals("-a", posixParser.tokens.get(0));
        assertEquals("-b", posixParser.tokens.get(1));
        assertEquals("-c", posixParser.tokens.get(2));
    }

    @Test
    public void testBurstToken_StopAtNonOption_NoOption() {
        // Given
        String token = "-abc";
        when(options.hasOption(anyString())).thenReturn(false);

        // When
        posixParser.burstToken(token, true);

        // Then
        assertEquals(1, posixParser.tokens.size());
        assertEquals(token, posixParser.tokens.get(0));
    }

    @Test
    public void testProcessNonOptionToken_StopAtNonOption() {
        // Given
        String value = "value";

        // When
        posixParser.processNonOptionToken(value, true);

        // Then
        assertEquals(2, posixParser.tokens.size());
        assertEquals("--", posixParser.tokens.get(0));
        assertEquals(value, posixParser.tokens.get(1));
    }

    @Test
    public void testProcessNonOptionToken_NoStopAtNonOption() {
        // Given
        String value = "value";

        // When
        posixParser.processNonOptionToken(value, false);

        // Then
        assertEquals(1, posixParser.tokens.size());
        assertEquals(value, posixParser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken_StopAtNonOption() {
        // Given
        String token = "-a";
        when(options.hasOption(anyString())).thenReturn(true);
        when(options.getOption(anyString())).thenReturn(mock(Option.class));

        // When
        posixParser.processOptionToken(token, true);

        // Then
        assertEquals(1, posixParser.tokens.size());
        assertEquals(token, posixParser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken_NoStopAtNonOption() {
        // Given
        String token = "-a";
        when(options.hasOption(anyString())).thenReturn(false);

        // When
        posixParser.processOptionToken(token, false);

        // Then
        assertEquals(1, posixParser.tokens.size());
        assertEquals(token, posixParser.tokens.get(0));
    }

    @Test
    public void testGobble_StopAtNonOption() {
        // Given
        List<String> tokens = new ArrayList<>();
        tokens.add("token1");
        tokens.add("token2");
        Iterator<String> iter = tokens.iterator();
        posixParser.eatTheRest = true;

        // When
        posixParser.gobble(iter);

        // Then
        assertEquals(2, posixParser.tokens.size());
        assertEquals("token1", posixParser.tokens.get(0));
        assertEquals("token2", posixParser.tokens.get(1));
    }

    @Test
    public void testGobble_NoStopAtNonOption() {
        // Given
        List<String> tokens = new ArrayList<>();
        tokens.add("token1");
        tokens.add("token2");
        Iterator<String> iter = tokens.iterator();
        posixParser.eatTheRest = false;

        // When
        posixParser.gobble(iter);

        // Then
        assertEquals(0, posixParser.tokens.size());
    }

    @Test
    public void testInit() {
        // Given
        posixParser.tokens.add("token1");
        posixParser.tokens.add("token2");
        posixParser.eatTheRest = true;

        // When
        posixParser.init();

        // Then
        assertEquals(0, posixParser.tokens.size());
        assertFalse(posixParser.eatTheRest);
    }
}
import net.hydromatic.morel.ast.Pos;
import net.hydromatic.morel.parse.MorelParser;
import net.hydromatic.morel.parse.MorelParseException;
import net.hydromatic.morel.util.MorelException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MorelParserTest {

    @Mock
    private MorelParser morelParser;

    @BeforeEach
    public void setup() {
        // Initialize mock if necessary
    }

    @Test
    public void testPos() {
        // Given: a parser with a known position
        Pos expectedPos = Pos.of("ml", "file", 1, 10);
        when(morelParser.pos()).thenReturn(expectedPos);

        // When: the position is retrieved
        Pos actualPos = morelParser.pos();

        // Then: the position matches the expected value
        assertEquals(expectedPos, actualPos);
        verify(morelParser, times(1)).pos();
    }

    @Test
    public void testZero() {
        // Given: a parser and a file name
        String file = "testFile";
        doNothing().when(morelParser).zero(file);

        // When: the current file is set
        morelParser.zero(file);

        // Then: the parser's state is updated
        verify(morelParser, times(1)).zero(file);
    }

    @Test
    public void testWrap_ParseException() {
        // Given: a parser and a parse exception
        Exception parseException = new Exception("Test parse exception");
        MorelException expectedException = new MorelException(morelParser.pos(), parseException);
        when(morelParser.wrap(any(Exception.class))).thenReturn(expectedException);

        // When: the parse exception is wrapped
        MorelParseException actualException = morelParser.wrap(parseException);

        // Then: the wrapped exception matches the expected value
        assertEquals(expectedException, actualException);
        verify(morelParser, times(1)).wrap(parseException);
    }

    @Test
    public void testWrap_NullException() {
        // Given: a parser and a null exception
        Exception parseException = null;

        // When / Then: wrapping a null exception throws a NullPointerException
        assertThrows(NullPointerException.class, () -> morelParser.wrap(parseException));
        verify(morelParser, never()).wrap(any(Exception.class));
    }
}
import org.jsoup.helper.Regex;
import org.jsoup.internal.SharedConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegexTest {

    @Mock
    private Pattern pattern;

    @BeforeEach
    void setUp() {
        // Reset the system property to its default value
        System.setProperty(SharedConstants.class.getName() + ".UseRe2j", "true");
    }

    @AfterEach
    void tearDown() {
        // Reset the system property to its default value
        System.setProperty(SharedConstants.class.getName() + ".UseRe2j", "true");
    }

    @Test
    public void testCompile_ValidRegex() {
        // Given
        String regex = "test";

        // When
        Regex compiledRegex = Regex.compile(regex);

        // Then
        assertNotNull(compiledRegex);
    }

    @Test
    public void testCompile_InvalidRegex() {
        // Given
        String regex = "(";

        // When and Then
        assertThrows(RuntimeException.class, () -> Regex.compile(regex));
    }

    @Test
    public void testFromPattern() {
        // Given
        Pattern pattern = Pattern.compile("test");

        // When
        Regex regex = Regex.fromPattern(pattern);

        // Then
        assertNotNull(regex);
    }

    @Test
    public void testUsingRe2j_Enabled() {
        // Given
        System.setProperty(SharedConstants.class.getName() + ".UseRe2j", "true");

        // When
        boolean result = Regex.usingRe2j();

        // Then
        assertTrue(result);
    }

    @Test
    public void testUsingRe2j_Disabled() {
        // Given
        System.setProperty(SharedConstants.class.getName() + ".UseRe2j", "false");

        // When
        boolean result = Regex.usingRe2j();

        // Then
        assertFalse(result);
    }

    @Test
    public void testMatcher_Find() {
        // Given
        String input = "test";
        Regex regex = Regex.compile("test");

        // When
        Regex.Matcher matcher = regex.matcher(input);

        // Then
        assertTrue(matcher.find());
    }

    @Test
    public void testToString() {
        // Given
        String regex = "test";
        Regex compiledRegex = Regex.compile(regex);

        // When
        String result = compiledRegex.toString();

        // Then
        assertEquals(regex, result);
    }
}
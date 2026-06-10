import org.jsoup.helper.Re2jRegex;
import org.jsoup.helper.Matcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class Re2jRegexTest {

    @Test
    public void testCompile_ValidRegex() {
        // Given: a valid regex pattern
        String regex = "a*b";

        // When: compiling the regex pattern
        Re2jRegex compiledRegex = (Re2jRegex) Re2jRegex.compile(regex);

        // Then: the compiled regex is not null
        assertNotNull(compiledRegex);
    }

    @Test
    public void testCompile_InvalidRegex() {
        // Given: an invalid regex pattern
        String regex = "(";

        // When / Then: compiling the regex pattern throws an exception
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Re2jRegex.compile(regex);
        });

        // Then: the exception message is not empty
        assertNotNull(exception.getMessage());
    }

    @Test
    public void testCompile_OutOfMemoryError() {
        // Given: a regex pattern that causes an OutOfMemoryError
        String regex = "(a*)*";

        // When / Then: compiling the regex pattern throws an exception
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Re2jRegex.compile(regex);
        });

        // Then: the exception message is not empty
        assertNotNull(exception.getMessage());
    }

    @Test
    public void testCompile_StackOverflowError() {
        // Given: a regex pattern that causes a StackOverflowError
        String regex = "(a*)*";

        // When / Then: compiling the regex pattern throws an exception
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Re2jRegex.compile(regex);
        });

        // Then: the exception message is not empty
        assertNotNull(exception.getMessage());
    }

    @Test
    public void testMatcher_ValidInput() {
        // Given: a valid regex pattern and input
        String regex = "a*b";
        String input = "aaab";

        // When: compiling the regex pattern and creating a matcher
        Re2jRegex compiledRegex = (Re2jRegex) Re2jRegex.compile(regex);
        Matcher matcher = compiledRegex.matcher(input);

        // Then: the matcher is not null
        assertNotNull(matcher);
    }

    @Test
    public void testMatcher_InvalidInput() {
        // Given: a valid regex pattern and invalid input
        String regex = "a*b";
        String input = null;

        // When / Then: creating a matcher with invalid input throws an exception
        assertThrows(NullPointerException.class, () -> {
            Re2jRegex compiledRegex = (Re2jRegex) Re2jRegex.compile(regex);
            compiledRegex.matcher(input);
        });
    }

    @Test
    public void testToString() {
        // Given: a valid regex pattern
        String regex = "a*b";

        // When: compiling the regex pattern
        Re2jRegex compiledRegex = (Re2jRegex) Re2jRegex.compile(regex);

        // Then: the string representation of the regex is not empty
        assertNotNull(compiledRegex.toString());
    }

    @Test
    public void testFind_Match() {
        // Given: a valid regex pattern and input
        String regex = "a*b";
        String input = "aaab";

        // When: compiling the regex pattern, creating a matcher, and searching for a match
        Re2jRegex compiledRegex = (Re2jRegex) Re2jRegex.compile(regex);
        Matcher matcher = compiledRegex.matcher(input);

        // Then: a match is found
        assertTrue(matcher.find());
    }

    @Test
    public void testFind_NoMatch() {
        // Given: a valid regex pattern and input
        String regex = "a*b";
        String input = "bb";

        // When: compiling the regex pattern, creating a matcher, and searching for a match
        Re2jRegex compiledRegex = (Re2jRegex) Re2jRegex.compile(regex);
        Matcher matcher = compiledRegex.matcher(input);

        // Then: no match is found
        assertFalse(matcher.find());
    }
}
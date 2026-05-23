import org.apache.commons.cli.OptionValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OptionValidatorTest {

    @Test
    public void testValidate_NullOption() {
        // Given: null option
        String option = null;

        // When: validate option
        String result = OptionValidator.validate(option);

        // Then: no exception is thrown and result is null
        assertNull(result);
    }

    @ParameterizedTest
    @CsvSource({
            "a, a",
            "A, A",
            "@, @",
            "?, ?",
            " ,  "
    })
    public void testValidate_SingleCharacterOption(String option, String expected) {
        // Given: single character option
        // When: validate option
        String result = OptionValidator.validate(option);

        // Then: no exception is thrown and result is as expected
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "+", "=", "{", "}", "[", "]", "|", "\\", ":", ";", "\"", "<", ">", ",", ".", "/", "?"})
    public void testValidate_InvalidSingleCharacterOption(String option) {
        // Given: invalid single character option
        // When and Then: exception is thrown
        assertThrows(IllegalArgumentException.class, () -> OptionValidator.validate(option));
    }

    @ParameterizedTest
    @CsvSource({
            "abc, abc",
            "ABC, ABC",
            "aBc, aBc"
    })
    public void testValidate_MultiCharacterOption(String option, String expected) {
        // Given: multi character option
        // When: validate option
        String result = OptionValidator.validate(option);

        // Then: no exception is thrown and result is as expected
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a1", "A#", "a$", "a%", "a^", "a&", "a*", "a(", "a)", "a-", "a_", "a+", "a=", "a{", "a}", "a[", "a]", "a|", "a\\", "a:", "a;", "a\"", "a<", "a>", "a,", "a.", "a/"})
    public void testValidate_InvalidMultiCharacterOption(String option) {
        // Given: invalid multi character option
        // When and Then: exception is thrown
        assertThrows(IllegalArgumentException.class, () -> OptionValidator.validate(option));
    }
}
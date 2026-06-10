import net.hydromatic.morel.util.WordComparator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.Collator;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class WordComparatorTest {

    @Test
    public void testCompare_SimpleStrings() {
        // Given: two simple strings
        String o1 = "a";
        String o2 = "b";

        // When: compare the strings
        int result = WordComparator.INSTANCE.compare(o1, o2);

        // Then: verify the result
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_Strings_withPunctuation() {
        // Given: two strings with punctuation
        String o1 = "a, b";
        String o2 = "a, c";

        // When: compare the strings
        int result = WordComparator.INSTANCE.compare(o1, o2);

        // Then: verify the result
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_Strings_withWordBoundaries() {
        // Given: two strings with word boundaries
        String o1 = "a c";
        String o2 = "ab c";

        // When: compare the strings
        int result = WordComparator.INSTANCE.compare(o1, o2);

        // Then: verify the result
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_Strings_withSamePrefix() {
        // Given: two strings with the same prefix
        String o1 = "a b";
        String o2 = "a c";

        // When: compare the strings
        int result = WordComparator.INSTANCE.compare(o1, o2);

        // Then: verify the result
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_Strings_withDifferentLength() {
        // Given: two strings with different lengths
        String o1 = "a";
        String o2 = "ab";

        // When: compare the strings
        int result = WordComparator.INSTANCE.compare(o1, o2);

        // Then: verify the result
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_NullStrings() {
        // Given: two null strings
        String o1 = null;
        String o2 = null;

        // When: compare the strings
        assertThrows(NullPointerException.class, () -> WordComparator.INSTANCE.compare(o1, o2));
    }

    @Test
    public void testCompare_NullAndNonNullStrings() {
        // Given: a null string and a non-null string
        String o1 = null;
        String o2 = "a";

        // When: compare the strings
        assertThrows(NullPointerException.class, () -> WordComparator.INSTANCE.compare(o1, o2));
    }

    @Test
    public void testFindPunctuation_PunctuationFound() {
        // Given: a string with punctuation
        String s = "a, b";
        int i = 0;
        boolean inverse = false;

        // When: find the punctuation
        int result = WordComparator.INSTANCE.findPunctuation(i, inverse, s);

        // Then: verify the result
        assertEquals(1, result);
    }

    @Test
    public void testFindPunctuation_NoPunctuationFound() {
        // Given: a string without punctuation
        String s = "ab";
        int i = 0;
        boolean inverse = false;

        // When: find the punctuation
        int result = WordComparator.INSTANCE.findPunctuation(i, inverse, s);

        // Then: verify the result
        assertEquals(-1, result);
    }

    @Test
    public void testIsPunctuation_PunctuationCharacter() {
        // Given: a punctuation character
        char c = ',';

        // When: check if the character is punctuation
        boolean result = WordComparator.INSTANCE.isPunctuation(c);

        // Then: verify the result
        assertTrue(result);
    }

    @Test
    public void testIsPunctuation_NonPunctuationCharacter() {
        // Given: a non-punctuation character
        char c = 'a';

        // When: check if the character is punctuation
        boolean result = WordComparator.INSTANCE.isPunctuation(c);

        // Then: verify the result
        assertFalse(result);
    }
}
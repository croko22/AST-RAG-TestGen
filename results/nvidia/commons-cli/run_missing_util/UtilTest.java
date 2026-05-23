import org.apache.commons.cli.Util;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UtilTest {

    @Test
    public void testStripLeadingAndTrailingQuotes_NoQuotes() {
        // Given: a string without quotes
        String str = "one two";
        
        // When: stripLeadingAndTrailingQuotes is called
        String result = Util.stripLeadingAndTrailingQuotes(str);
        
        // Then: the string remains unchanged
        assertEquals(str, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_WithQuotes() {
        // Given: a string with leading and trailing quotes
        String str = "\"one two\"";
        
        // When: stripLeadingAndTrailingQuotes is called
        String result = Util.stripLeadingAndTrailingQuotes(str);
        
        // Then: the quotes are removed
        assertEquals("one two", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_WithQuotesAndInnerQuotes() {
        // Given: a string with leading and trailing quotes and inner quotes
        String str = "\"one \"two\" three\"";
        
        // When: stripLeadingAndTrailingQuotes is called
        String result = Util.stripLeadingAndTrailingQuotes(str);
        
        // Then: the quotes are not removed because of inner quotes
        assertEquals(str, result);
    }

    @Test
    public void testStripLeadingHyphens_NoHyphens() {
        // Given: a string without leading hyphens
        String str = "one two";
        
        // When: stripLeadingHyphens is called
        String result = Util.stripLeadingHyphens(str);
        
        // Then: the string remains unchanged
        assertEquals(str, result);
    }

    @Test
    public void testStripLeadingHyphens_SingleHyphen() {
        // Given: a string with a single leading hyphen
        String str = "-one two";
        
        // When: stripLeadingHyphens is called
        String result = Util.stripLeadingHyphens(str);
        
        // Then: the hyphen is removed
        assertEquals("one two", result);
    }

    @Test
    public void testStripLeadingHyphens_DoubleHyphens() {
        // Given: a string with double leading hyphens
        String str = "--one two";
        
        // When: stripLeadingHyphens is called
        String result = Util.stripLeadingHyphens(str);
        
        // Then: the hyphens are removed
        assertEquals("one two", result);
    }

    @Test
    public void testStripLeadingHyphens_NullInput() {
        // Given: a null input string
        String str = null;
        
        // When: stripLeadingHyphens is called
        String result = Util.stripLeadingHyphens(str);
        
        // Then: null is returned
        assertNull(result);
    }
}
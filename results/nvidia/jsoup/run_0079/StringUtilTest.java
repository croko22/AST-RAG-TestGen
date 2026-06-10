import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StringUtilTest {

    @Mock
    private Iterator<String> iterator;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testJoinCollection() {
        Collection<String> collection = Arrays.asList("Hello", "World");
        String result = StringUtil.join(collection, ", ");
        assertEquals("Hello, World", result);
    }

    @Test
    void testJoinIterator() {
        iterator = Arrays.asList("Hello", "World").iterator();
        String result = StringUtil.join(iterator, ", ");
        assertEquals("Hello, World", result);
    }

    @Test
    void testJoinArray() {
        String[] array = {"Hello", "World"};
        String result = StringUtil.join(Arrays.asList(array), ", ");
        assertEquals("Hello, World", result);
    }

    @Test
    void testStringJoinerAdd() {
        StringUtil.StringJoiner joiner = new StringUtil.StringJoiner(", ");
        joiner.add("Hello");
        joiner.add("World");
        String result = joiner.complete();
        assertEquals("Hello, World", result);
    }

    @Test
    void testStringJoinerAppend() {
        StringUtil.StringJoiner joiner = new StringUtil.StringJoiner(", ");
        joiner.append("Hello");
        joiner.append("World");
        String result = joiner.complete();
        assertEquals("HelloWorld", result);
    }

    @Test
    void testPadding() {
        String result = StringUtil.padding(5);
        assertEquals("     ", result);
    }

    @Test
    void testPaddingWithMax() {
        String result = StringUtil.padding(5, 3);
        assertEquals("   ", result);
    }

    @Test
    void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("Hello"));
    }

    @Test
    void testStartsWithNewline() {
        assertTrue(StringUtil.startsWithNewline("\nHello"));
        assertFalse(StringUtil.startsWithNewline("Hello"));
    }

    @Test
    void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("12345"));
        assertFalse(StringUtil.isNumeric("Hello"));
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
    }

    @Test
    void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertFalse(StringUtil.isWhitespace('a'));
    }

    @Test
    void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace(160));
        assertFalse(StringUtil.isActuallyWhitespace('a'));
    }

    @Test
    void testIsInvisibleChar() {
        assertTrue(StringUtil.isInvisibleChar(8203));
        assertTrue(StringUtil.isInvisibleChar(173));
        assertFalse(StringUtil.isInvisibleChar('a'));
    }

    @Test
    void testNormaliseWhitespace() {
        String result = StringUtil.normaliseWhitespace("Hello   World");
        assertEquals("Hello World", result);
    }

    @Test
    void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "Hello   World", false);
        assertEquals("Hello World", sb.toString());
    }

    @Test
    void testIn() {
        String[] array = {"Hello", "World"};
        assertTrue(StringUtil.in("Hello", array));
        assertFalse(StringUtil.in("Foo", array));
    }

    @Test
    void testInSorted() {
        String[] array = {"Hello", "World"};
        Arrays.sort(array);
        assertTrue(StringUtil.inSorted("Hello", array));
        assertFalse(StringUtil.inSorted("Foo", array));
    }

    @Test
    void testIsAscii() {
        assertTrue(StringUtil.isAscii("Hello"));
        assertFalse(StringUtil.isAscii("Héllo"));
    }

    @Test
    void testResolveUrl() throws MalformedURLException {
        URL base = new URL("http://example.com");
        String relUrl = "/path/to/resource";
        URL result = StringUtil.resolve(base, relUrl);
        assertEquals("http://example.com/path/to/resource", result.toExternalForm());
    }

    @Test
    void testResolveString() {
        String baseUrl = "http://example.com";
        String relUrl = "/path/to/resource";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("http://example.com/path/to/resource", result);
    }

    @Test
    void testBorrowBuilder() {
        StringBuilder sb = StringUtil.borrowBuilder();
        assertNotNull(sb);
    }

    @Test
    void testReleaseBuilder() {
        StringBuilder sb = StringUtil.borrowBuilder();
        String result = StringUtil.releaseBuilder(sb);
        assertNotNull(result);
    }

    @Test
    void testReleaseBuilderVoid() {
        StringBuilder sb = StringUtil.borrowBuilder();
        StringUtil.releaseBuilderVoid(sb);
        assertNotNull(sb);
    }

    @Test
    void testJoining() {
        Collector<CharSequence, ?, String> collector = StringUtil.joining(", ");
        assertNotNull(collector);
    }

    @Test
    void testIsAsciiLetter() {
        assertTrue(StringUtil.isAsciiLetter('a'));
        assertTrue(StringUtil.isAsciiLetter('A'));
        assertFalse(StringUtil.isAsciiLetter('1'));
    }

    @Test
    void testIsDigit() {
        assertTrue(StringUtil.isDigit('1'));
        assertFalse(StringUtil.isDigit('a'));
    }

    @Test
    void testIsHexDigit() {
        assertTrue(StringUtil.isHexDigit('1'));
        assertTrue(StringUtil.isHexDigit('a'));
        assertTrue(StringUtil.isHexDigit('A'));
        assertFalse(StringUtil.isHexDigit('G'));
    }
}
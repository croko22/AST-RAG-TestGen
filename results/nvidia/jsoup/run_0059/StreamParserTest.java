Here's a comprehensive test class for the `StreamParser` class:

```java
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.StreamParser;
import org.jsoup.select.Evaluator;
import org.jsoup.select.Selector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StreamParserTest {

    @Mock
    private Parser parser;

    private StreamParser streamParser;

    @BeforeEach
    void setup() {
        streamParser = new StreamParser(parser);
    }

    @AfterEach
    void tearDown() {
        streamParser.close();
    }

    @Test
    void testParse() {
        // Given
        String input = "<html><body>Hello World!</body></html>";
        String baseUri = "https://example.com";

        // When
        StreamParser result = streamParser.parse(new StringReader(input), baseUri);

        // Then
        assertNotNull(result);
        assertEquals(streamParser, result);
    }

    @Test
    void testParseString() {
        // Given
        String input = "<html><body>Hello World!</body></html>";
        String baseUri = "https://example.com";

        // When
        StreamParser result = streamParser.parse(input, baseUri);

        // Then
        assertNotNull(result);
        assertEquals(streamParser, result);
    }

    @Test
    void testParseFragment() {
        // Given
        String input = "<p>Hello World!</p>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        String baseUri = "https://example.com";

        // When
        StreamParser result = streamParser.parseFragment(new StringReader(input), context, baseUri);

        // Then
        assertNotNull(result);
        assertEquals(streamParser, result);
    }

    @Test
    void testParseFragmentString() {
        // Given
        String input = "<p>Hello World!</p>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        String baseUri = "https://example.com";

        // When
        StreamParser result = streamParser.parseFragment(input, context, baseUri);

        // Then
        assertNotNull(result);
        assertEquals(streamParser, result);
    }

    @Test
    void testStream() {
        // Given
        String input = "<html><body><p>Hello</p><p>World!</p></body></html>";
        String baseUri = "https://example.com";
        streamParser.parse(new StringReader(input), baseUri);

        // When
        Iterator<Element> iterator = streamParser.stream().iterator();

        // Then
        assertTrue(iterator.hasNext());
        Element element = iterator.next();
        assertEquals("p", element.tagName());
        assertEquals("Hello", element.text());
    }

    @Test
    void testIterator() {
        // Given
        String input = "<html><body><p>Hello</p><p>World!</p></body></html>";
        String baseUri = "https://example.com";
        streamParser.parse(new StringReader(input), baseUri);

        // When
        Iterator<Element> iterator = streamParser.iterator();

        // Then
        assertTrue(iterator.hasNext());
        Element element = iterator.next();
        assertEquals("p", element.tagName());
        assertEquals("Hello", element.text());
    }

    @Test
    void testStop() {
        // Given
        String input = "<html><body><p>Hello</p><p>World!</p></body></html>";
        String baseUri = "https://example.com";
        streamParser.parse(new StringReader(input), baseUri);

        // When
        StreamParser result = streamParser.stop();

        // Then
        assertNotNull(result);
        assertEquals(streamParser, result);
    }

    @Test
    void testClose() {
        // Given
        String input = "<html><body><p>Hello</p><p>World!</p></body></html>";
        String baseUri = "https://example.com";
        streamParser.parse(new StringReader(input), baseUri);

        // When
        streamParser.close();

        // Then
        // No exception thrown
    }

    @Test
    void testDocument() {
        // Given
        String input = "<html><body><p>Hello</p><p>World!</p></body></html>";
        String baseUri = "https://example.com";
        streamParser.parse(new StringReader(input), baseUri);

        // When
        Document document = streamParser.document();

        // Then
        assertNotNull(document);
    }

    @Test
    void testComplete() throws IOException {
        // Given
        String input = "<html><body><p>Hello</p><p>World!</p></body></html>";
        String baseUri = "https://example.com";
        streamParser.parse(new StringReader(input), baseUri);

        // When
        Document document = streamParser.complete();

        // Then
        assertNotNull(document);
    }

    @Test
    void testCompleteFragment() throws IOException {
        // Given
        String input = "<p>Hello</p><p>World!</p>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        String baseUri = "https://example.com";
        streamParser.parseFragment(new StringReader(input), context, baseUri);

        // When
        List<Node> nodes = streamParser.completeFragment();

        // Then
        assertNotNull(nodes);
    }

    @Test
    void testSelectFirst() throws IOException {
        // Given
        String input = "<html><body><p>Hello</p><p>World!</p></body></html>";
        String baseUri = "https://example.com";
        streamParser.parse(new StringReader(input), baseUri);

        // When
        Element element = streamParser.selectFirst("p");

        // Then
        assertNotNull(element);
        assertEquals("p", element.tagName());
        assertEquals("Hello", element.text());
    }

    @Test
    void testExpectFirst() throws IOException {
        // Given
        String input = "<html><body><p>Hello</p><p>World!</p></body></html>";
        String baseUri = "https://example.com";
        streamParser.parse(new StringReader(input), baseUri);

        // When
        Element element = streamParser.expectFirst("p");

        // Then
        assertNotNull(element);
        assertEquals("p", element.tagName());
        assertEquals("Hello", element.text());
    }

    @Test
    void testSelectFirstEvaluator() throws IOException {
        // Given
        String input = "<html><body><p>Hello</p><p>World!</p></body></html>";
        String baseUri = "https://example.com";
        streamParser.parse(new StringReader(input), baseUri);

        // When
        Evaluator evaluator = Selector.evaluatorOf("p");
        Element element = streamParser.selectFirst(evaluator);

        // Then
        assertNotNull(element);
        assertEquals("p", element.tagName());
        assertEquals("Hello", element.text());
    }

    @Test
    void testSelectNext() throws IOException {
        // Given
        String input = "<html><body><p>Hello</p><p>World!</p></body></html>";
        String baseUri = "https://example.com";
        streamParser.parse(new StringReader(input), baseUri);

        // When
        Element element = streamParser.selectNext("p");

        // Then
        assertNotNull(element);
        assertEquals("p", element.tagName());
        assertEquals("World!", element.text());
    }

    @Test
    void testExpectNext() throws IOException {
        // Given
        String input = "<html><body><p>Hello</p><p>World!</p></body></html>";
        String baseUri = "https://example.com";
        streamParser.parse(new StringReader(input), baseUri);

        // When
        Element element = streamParser.expectNext("p");

        // Then
        assertNotNull(element);
        assertEquals("p", element.tagName());
        assertEquals("World!", element.text());
    }

    @Test
    void testSelectNextEvaluator() throws IOException {
        // Given
        String input = "<html><body><p>Hello</p><p>World!</p></body></html>";
        String baseUri = "https://example.com";
        streamParser.parse(new StringReader(input), baseUri);

        // When
        Evaluator evaluator = Selector.evaluatorOf("p");
        Element element = streamParser.selectNext(evaluator);

        // Then
        assertNotNull(element);
        assertEquals("p", element.tagName());
        assertEquals("World!", element.text());
    }
}
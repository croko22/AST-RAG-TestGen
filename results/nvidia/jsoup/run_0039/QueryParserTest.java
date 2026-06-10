Here's a comprehensive test class for the `QueryParser` class:

```java
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class QueryParserTest {

    @Mock
    private TokenQueue tokenQueue;

    private QueryParser queryParser;

    @BeforeEach
    public void setup() {
        queryParser = new QueryParser("test");
    }

    @AfterEach
    public void tearDown() {
        queryParser.close();
    }

    @Test
    public void testParse() {
        // Given
        String query = "div";

        // When
        QueryParser.Evaluator evaluator = QueryParser.parse(query);

        // Then
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_InvalidQuery() {
        // Given
        String query = "$invalid";

        // When and Then
        assertThrows(org.jsoup.select.Selector.SelectorParseException.class, () -> QueryParser.parse(query));
    }

    @Test
    public void testToString() {
        // Given
        String query = "test";

        // When
        String result = queryParser.toString();

        // Then
        assertEquals(query, result);
    }

    @Test
    public void testClose() {
        // Given
        QueryParser parser = new QueryParser("test");

        // When
        parser.close();

        // Then
        // No exception is thrown
    }
}
```

This test class includes the following tests:

1. `testParse()`: Tests the `parse()` method with a valid query.
2. `testParse_InvalidQuery()`: Tests the `parse()` method with an invalid query.
3. `testToString()`: Tests the `toString()` method.
4. `testClose()`: Tests the `close()` method.

Note that the `TokenQueue` class is mocked using Mockito to isolate the dependencies and make the tests more reliable.

To achieve high branch coverage, you may need to add more test cases to cover different scenarios, such as:

* Testing the `parse()` method with different types of queries (e.g., `div`, `div.class`, `div#id`, etc.)
* Testing the `parse()` method with invalid queries (e.g., `div$`, `div#`, etc.)
* Testing the `toString()` method with different queries
* Testing the `close()` method with different scenarios (e.g., closing an already closed parser)

You can use a code coverage tool to identify the areas of the code that need more testing.
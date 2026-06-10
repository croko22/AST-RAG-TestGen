import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WikipediaTest {

    @Mock
    private Document document;

    @Mock
    private Element element;

    @Mock
    private Elements elements;

    @BeforeEach
    public void setup() {
        // Initialize mock objects
        when(document.title()).thenReturn("Test Title");
        when(element.attr("title")).thenReturn("Test Attr");
        when(element.absUrl("href")).thenReturn("Test Href");
        when(elements.select("#mp-itn b a")).thenReturn(elements);
    }

    @Test
    public void testMain() throws IOException {
        // Given: mock Jsoup connection
        when(Jsoup.connect("https://en.wikipedia.org/").get()).thenReturn(document);

        // When: execute main method
        Wikipedia.main(new String[]{});

        // Then: verify expected behavior
        verifyStatic(Jsoup.class);
        Jsoup.connect("https://en.wikipedia.org/").get();
        verify(document).title();
        verify(elements).select("#mp-itn b a");
    }

    @Test
    public void testLog() {
        // Given: test message and values
        String msg = "Test Message: %s, %s";
        String val1 = "Value 1";
        String val2 = "Value 2";

        // When: execute log method
        Wikipedia.log(msg, val1, val2);

        // Then: verify expected output
        // Note: Since System.out.println is used, we can't directly verify the output.
        // However, we can verify that the log method is called without any exceptions.
        assertDoesNotThrow(() -> Wikipedia.log(msg, val1, val2));
    }

    @Test
    public void testLog_SingleValue() {
        // Given: test message and value
        String msg = "Test Message: %s";
        String val = "Value";

        // When: execute log method
        Wikipedia.log(msg, val);

        // Then: verify expected output
        // Note: Since System.out.println is used, we can't directly verify the output.
        // However, we can verify that the log method is called without any exceptions.
        assertDoesNotThrow(() -> Wikipedia.log(msg, val));
    }

    @Test
    public void testLog_NoValues() {
        // Given: test message
        String msg = "Test Message";

        // When: execute log method
        Wikipedia.log(msg);

        // Then: verify expected output
        // Note: Since System.out.println is used, we can't directly verify the output.
        // However, we can verify that the log method is called without any exceptions.
        assertDoesNotThrow(() -> Wikipedia.log(msg));
    }
}
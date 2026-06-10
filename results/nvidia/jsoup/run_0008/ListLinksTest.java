import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListLinksTest {

    @Mock
    private Document document;

    @Mock
    private Element element;

    @Mock
    private Elements elements;

    @InjectMocks
    private ListLinks listLinks;

    @BeforeEach
    void setup() {
        // Initialize mock objects
        when(document.select("a[href]")).thenReturn(elements);
        when(document.select("[src]")).thenReturn(elements);
        when(document.select("link[href]")).thenReturn(elements);
    }

    @Test
    void testMain() {
        // Test main method with valid arguments
        String[] args = {"https://www.example.com"};
        assertDoesNotThrow(() -> ListLinks.main(args));
    }

    @Test
    void testMain_InvalidArguments() {
        // Test main method with invalid arguments
        String[] args = {};
        assertThrows(IllegalArgumentException.class, () -> ListLinks.main(args));
    }

    @Test
    void testPrint() {
        // Test print method
        String msg = "Fetching %s...";
        String url = "https://www.example.com";
        ListLinks.print(msg, url);
        // Verify print statement
        verify(System.out).println(String.format(msg, url));
    }

    @Test
    void testTrim() {
        // Test trim method
        String s = "This is a long string";
        int width = 10;
        String trimmed = ListLinks.trim(s, width);
        assertEquals(s.substring(0, width - 1) + ".", trimmed);
    }

    @Test
    void testJsoupConnect() {
        // Test Jsoup connect method
        String url = "https://www.example.com";
        try {
            Document doc = Jsoup.connect(url).get();
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    void testJsoupConnect_InvalidUrl() {
        // Test Jsoup connect method with invalid URL
        String url = "invalid-url";
        assertThrows(IOException.class, () -> Jsoup.connect(url).get());
    }

    @Test
    void testDocumentSelect() {
        // Test document select method
        String cssQuery = "a[href]";
        Elements selected = document.select(cssQuery);
        assertNotNull(selected);
    }

    @Test
    void testElementSelect() {
        // Test element select method
        String cssQuery = "a[href]";
        Elements selected = element.select(cssQuery);
        assertNotNull(selected);
    }

    @Test
    void testElementsSelect() {
        // Test elements select method
        String cssQuery = "a[href]";
        Elements selected = elements.select(cssQuery);
        assertNotNull(selected);
    }
}
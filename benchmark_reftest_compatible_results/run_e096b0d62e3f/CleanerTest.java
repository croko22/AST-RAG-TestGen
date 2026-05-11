import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Safelist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CleanerTest {

    @InjectMocks
    private Cleaner cleaner;

    @Mock
    private Safelist safelist;

    @BeforeEach
    void setup() {
        cleaner = new Cleaner(Safelist.relaxed());
    }

    @Test
    void testClean() {
        // Given
        String html = "<p>Hello <b>world</b>!</p>";
        Document dirtyDocument = Jsoup.parse(html);

        // When
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Then
        assertNotNull(cleanDocument);
        assertEquals(html, cleanDocument.html());
    }

    @Test
    void testIsValid() {
        // Given
        String html = "<p>Hello <b>world</b>!</p>";
        Document dirtyDocument = Jsoup.parse(html);

        // When
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Then
        assertTrue(isValid);
    }

    @Test
    void testIsValid_InvalidHtml() {
        // Given
        String html = "<p>Hello <script>alert('XSS')</script> world!</p>";
        Document dirtyDocument = Jsoup.parse(html);

        // When
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Then
        assertFalse(isValid);
    }

    @Test
    void testIsValidBodyHtml() {
        // Given
        String html = "<p>Hello <b>world</b>!</p>";

        // When
        boolean isValid = cleaner.isValidBodyHtml(html);

        // Then
        assertTrue(isValid);
    }

    @Test
    void testIsValidBodyHtml_InvalidHtml() {
        // Given
        String html = "<p>Hello <script>alert('XSS')</script> world!</p>";

        // When
        boolean isValid = cleaner.isValidBodyHtml(html);

        // Then
        assertFalse(isValid);
    }
}
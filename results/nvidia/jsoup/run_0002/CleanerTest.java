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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CleanerTest {

    @InjectMocks
    private Cleaner cleaner;

    @Mock
    private Safelist safelist;

    @BeforeEach
    void setup() {
        cleaner = new Cleaner(safelist);
    }

    @Test
    void testClean() {
        // Given
        String dirtyHtml = "<p>Hello <b>World!</b></p>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        when(safelist.isSafeTag(any())).thenReturn(true);

        // When
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Then
        assertNotNull(cleanDocument);
        assertEquals(dirtyHtml, cleanDocument.body().html());
    }

    @Test
    void testClean_RemovesUnsafeTags() {
        // Given
        String dirtyHtml = "<p>Hello <script>alert('XSS')</script></p>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        when(safelist.isSafeTag(any())).thenReturn(false);

        // When
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Then
        assertNotNull(cleanDocument);
        assertNotEquals(dirtyHtml, cleanDocument.body().html());
    }

    @Test
    void testIsValid() {
        // Given
        String dirtyHtml = "<p>Hello <b>World!</b></p>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        when(safelist.isSafeTag(any())).thenReturn(true);

        // When
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Then
        assertTrue(isValid);
    }

    @Test
    void testIsValid_ReturnsFalseForUnsafeTags() {
        // Given
        String dirtyHtml = "<p>Hello <script>alert('XSS')</script></p>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        when(safelist.isSafeTag(any())).thenReturn(false);

        // When
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Then
        assertFalse(isValid);
    }

    @Test
    void testIsValidBodyHtml() {
        // Given
        String bodyHtml = "<p>Hello <b>World!</b></p>";
        when(safelist.isSafeTag(any())).thenReturn(true);

        // When
        boolean isValid = cleaner.isValidBodyHtml(bodyHtml);

        // Then
        assertTrue(isValid);
    }

    @Test
    void testIsValidBodyHtml_ReturnsFalseForUnsafeTags() {
        // Given
        String bodyHtml = "<p>Hello <script>alert('XSS')</script></p>";
        when(safelist.isSafeTag(any())).thenReturn(false);

        // When
        boolean isValid = cleaner.isValidBodyHtml(bodyHtml);

        // Then
        assertFalse(isValid);
    }
}
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NormalizerTest {

    @Mock
    private Attribute attribute;

    @Mock
    private Document document;

    @BeforeEach
    public void setup() {
        // Setup mocks if needed
    }

    @Test
    public void testLowerCase_LowerCaseInput() {
        // Given
        String input = "test";

        // When
        String result = org.jsoup.internal.Normalizer.lowerCase(input);

        // Then
        assertEquals("test", result);
    }

    @Test
    public void testLowerCase_UpperCaseInput() {
        // Given
        String input = "TEST";

        // When
        String result = org.jsoup.internal.Normalizer.lowerCase(input);

        // Then
        assertEquals("test", result);
    }

    @Test
    public void testLowerCase_NullInput() {
        // Given
        String input = null;

        // When
        String result = org.jsoup.internal.Normalizer.lowerCase(input);

        // Then
        assertEquals("", result);
    }

    @Test
    public void testNormalize_LowerCaseInput() {
        // Given
        String input = "test";

        // When
        String result = org.jsoup.internal.Normalizer.normalize(input);

        // Then
        assertEquals("test", result);
    }

    @Test
    public void testNormalize_UpperCaseInput() {
        // Given
        String input = "TEST";

        // When
        String result = org.jsoup.internal.Normalizer.normalize(input);

        // Then
        assertEquals("test", result);
    }

    @Test
    public void testNormalize_NullInput() {
        // Given
        String input = null;

        // When
        String result = org.jsoup.internal.Normalizer.normalize(input);

        // Then
        assertEquals("", result);
    }

    @Test
    public void testNormalize_WhiteSpaceInput() {
        // Given
        String input = "   test   ";

        // When
        String result = org.jsoup.internal.Normalizer.normalize(input);

        // Then
        assertEquals("test", result);
    }

    @Test
    public void testNormalize_Deprecated_LowerCaseInput() {
        // Given
        String input = "test";
        boolean isStringLiteral = true;

        // When
        String result = org.jsoup.internal.Normalizer.normalize(input, isStringLiteral);

        // Then
        assertEquals("test", result);
    }

    @Test
    public void testNormalize_Deprecated_UpperCaseInput() {
        // Given
        String input = "TEST";
        boolean isStringLiteral = true;

        // When
        String result = org.jsoup.internal.Normalizer.normalize(input, isStringLiteral);

        // Then
        assertEquals("test", result);
    }

    @Test
    public void testNormalize_Deprecated_NullInput() {
        // Given
        String input = null;
        boolean isStringLiteral = true;

        // When
        String result = org.jsoup.internal.Normalizer.normalize(input, isStringLiteral);

        // Then
        assertEquals("", result);
    }

    @Test
    public void testNormalize_Deprecated_WhiteSpaceInput() {
        // Given
        String input = "   test   ";
        boolean isStringLiteral = true;

        // When
        String result = org.jsoup.internal.Normalizer.normalize(input, isStringLiteral);

        // Then
        assertEquals("test", result);
    }

    @Test
    public void testXmlSafeTagName_ValidInput() {
        // Given
        String input = "test";

        // When
        String result = org.jsoup.internal.Normalizer.xmlSafeTagName(input);

        // Then
        assertEquals("test", result);
    }

    @Test
    public void testXmlSafeTagName_InvalidInput() {
        // Given
        String input = "test<test";

        // When
        when(Attribute.getValidKey(anyString(), anyString())).thenReturn("test_test");

        String result = org.jsoup.internal.Normalizer.xmlSafeTagName(input);

        // Then
        assertEquals("test_test", result);
    }

    @Test
    public void testXmlSafeTagName_NullInput() {
        // Given
        String input = null;

        // When
        String result = org.jsoup.internal.Normalizer.xmlSafeTagName(input);

        // Then
        assertNull(result);
    }
}
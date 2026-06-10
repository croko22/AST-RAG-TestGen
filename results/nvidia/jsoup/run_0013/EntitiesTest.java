import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.internal.StringUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntitiesTest {

    @Mock
    private Document.OutputSettings outputSettings;

    @Mock
    private Parser parser;

    @BeforeEach
    void setup() {
        // Setup mock objects
        when(outputSettings.escapeMode()).thenReturn(Entities.EscapeMode.base);
        when(outputSettings.charset()).thenReturn(Charset.forName("UTF-8"));
    }

    @Test
    public void testIsNamedEntity() {
        // Given
        String name = "lt";

        // When
        boolean result = Entities.isNamedEntity(name);

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsNamedEntityUnknown() {
        // Given
        String name = "unknown";

        // When
        boolean result = Entities.isNamedEntity(name);

        // Then
        assertFalse(result);
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Given
        String name = "lt";

        // When
        boolean result = Entities.isBaseNamedEntity(name);

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsBaseNamedEntityUnknown() {
        // Given
        String name = "unknown";

        // When
        boolean result = Entities.isBaseNamedEntity(name);

        // Then
        assertFalse(result);
    }

    @Test
    public void testGetByName() {
        // Given
        String name = "lt";

        // When
        String result = Entities.getByName(name);

        // Then
        assertEquals("<", result);
    }

    @Test
    public void testGetByNameUnknown() {
        // Given
        String name = "unknown";

        // When
        String result = Entities.getByName(name);

        // Then
        assertEquals("", result);
    }

    @Test
    public void testCodepointsForName() {
        // Given
        String name = "lt";

        // When
        int result = Entities.codepointsForName(name, new int[2]);

        // Then
        assertEquals(1, result);
    }

    @Test
    public void testCodepointsForNameUnknown() {
        // Given
        String name = "unknown";

        // When
        int result = Entities.codepointsForName(name, new int[2]);

        // Then
        assertEquals(0, result);
    }

    @Test
    public void testFindPrefix() {
        // Given
        String input = "notit";

        // When
        String result = Entities.findPrefix(input);

        // Then
        assertEquals("not", result);
    }

    @Test
    public void testFindPrefixNoMatch() {
        // Given
        String input = "unknown";

        // When
        String result = Entities.findPrefix(input);

        // Then
        assertEquals("", result);
    }

    @Test
    public void testEscape() {
        // Given
        String data = "<";

        // When
        String result = Entities.escape(data);

        // Then
        assertEquals("&lt;", result);
    }

    @Test
    public void testEscapeWithOutputSettings() {
        // Given
        String data = "<";

        // When
        String result = Entities.escape(data, outputSettings);

        // Then
        assertEquals("&lt;", result);
    }

    @Test
    public void testUnescape() {
        // Given
        String string = "&lt;";

        // When
        String result = Entities.unescape(string);

        // Then
        assertEquals("<", result);
    }
}
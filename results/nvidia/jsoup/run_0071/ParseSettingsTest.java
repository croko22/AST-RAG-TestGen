import org.jsoup.nodes.Attributes;
import org.jsoup.parser.ParseSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParseSettingsTest {

    @Mock
    private Attributes attributes;

    @InjectMocks
    private ParseSettings parseSettings;

    @BeforeEach
    public void setup() {
        parseSettings = new ParseSettings(true, true);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(attributes);
    }

    @Test
    public void testPreserveTagCase() {
        // Given: preserveTagCase is true
        ParseSettings settings = new ParseSettings(true, false);
        // When: preserveTagCase is called
        boolean result = settings.preserveTagCase();
        // Then: result is true
        assertTrue(result);
    }

    @Test
    public void testPreserveAttributeCase() {
        // Given: preserveAttributeCase is true
        ParseSettings settings = new ParseSettings(false, true);
        // When: preserveAttributeCase is called
        boolean result = settings.preserveAttributeCase();
        // Then: result is true
        assertTrue(result);
    }

    @Test
    public void testNormalizeTag_PreserveTagCase() {
        // Given: preserveTagCase is true
        ParseSettings settings = new ParseSettings(true, false);
        // When: normalizeTag is called
        String result = settings.normalizeTag("Tag");
        // Then: result is "Tag"
        assertEquals("Tag", result);
    }

    @Test
    public void testNormalizeTag_DoNotPreserveTagCase() {
        // Given: preserveTagCase is false
        ParseSettings settings = new ParseSettings(false, false);
        // When: normalizeTag is called
        String result = settings.normalizeTag("Tag");
        // Then: result is "tag"
        assertEquals("tag", result);
    }

    @Test
    public void testNormalizeAttribute_PreserveAttributeCase() {
        // Given: preserveAttributeCase is true
        ParseSettings settings = new ParseSettings(false, true);
        // When: normalizeAttribute is called
        String result = settings.normalizeAttribute("Attribute");
        // Then: result is "Attribute"
        assertEquals("Attribute", result);
    }

    @Test
    public void testNormalizeAttribute_DoNotPreserveAttributeCase() {
        // Given: preserveAttributeCase is false
        ParseSettings settings = new ParseSettings(false, false);
        // When: normalizeAttribute is called
        String result = settings.normalizeAttribute("Attribute");
        // Then: result is "attribute"
        assertEquals("attribute", result);
    }

    @Test
    public void testNormalizeAttributes_PreserveAttributeCase() {
        // Given: preserveAttributeCase is true
        ParseSettings settings = new ParseSettings(false, true);
        // When: normalizeAttributes is called
        settings.normalizeAttributes(attributes);
        // Then: no interaction with attributes
        verify(attributes, never()).normalize();
    }

    @Test
    public void testNormalizeAttributes_DoNotPreserveAttributeCase() {
        // Given: preserveAttributeCase is false
        ParseSettings settings = new ParseSettings(false, false);
        // When: normalizeAttributes is called
        settings.normalizeAttributes(attributes);
        // Then: attributes.normalize is called
        verify(attributes, times(1)).normalize();
    }

    @Test
    public void testNormalName() {
        // Given: name is "Name"
        String name = "Name";
        // When: normalName is called
        String result = ParseSettings.normalName(name);
        // Then: result is "name"
        assertEquals("name", result);
    }
}
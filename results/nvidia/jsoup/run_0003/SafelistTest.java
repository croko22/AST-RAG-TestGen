import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SafelistTest {

    @Mock
    private Element element;

    @Mock
    private Attribute attribute;

    private Safelist safelist;

    @BeforeEach
    public void setup() {
        safelist = Safelist.none();
    }

    @Test
    public void testNone() {
        // Given
        Safelist noneSafelist = Safelist.none();

        // When
        boolean isSafeTag = noneSafelist.isSafeTag("p");

        // Then
        assertFalse(isSafeTag);
    }

    @Test
    public void testSimpleText() {
        // Given
        Safelist simpleTextSafelist = Safelist.simpleText();

        // When
        boolean isSafeTag = simpleTextSafelist.isSafeTag("b");

        // Then
        assertTrue(isSafeTag);
    }

    @Test
    public void testBasic() {
        // Given
        Safelist basicSafelist = Safelist.basic();

        // When
        boolean isSafeTag = basicSafelist.isSafeTag("a");

        // Then
        assertTrue(isSafeTag);
    }

    @Test
    public void testBasicWithImages() {
        // Given
        Safelist basicWithImagesSafelist = Safelist.basicWithImages();

        // When
        boolean isSafeTag = basicWithImagesSafelist.isSafeTag("img");

        // Then
        assertTrue(isSafeTag);
    }

    @Test
    public void testRelaxed() {
        // Given
        Safelist relaxedSafelist = Safelist.relaxed();

        // When
        boolean isSafeTag = relaxedSafelist.isSafeTag("div");

        // Then
        assertTrue(isSafeTag);
    }

    @Test
    public void testAddTags() {
        // Given
        String[] tags = {"p", "span"};

        // When
        Safelist safelistWithTags = safelist.addTags(tags);

        // Then
        assertTrue(safelistWithTags.isSafeTag("p"));
        assertTrue(safelistWithTags.isSafeTag("span"));
    }

    @Test
    public void testRemoveTags() {
        // Given
        String[] tags = {"p", "span"};
        safelist.addTags(tags);

        // When
        Safelist safelistWithoutTags = safelist.removeTags(tags);

        // Then
        assertFalse(safelistWithoutTags.isSafeTag("p"));
        assertFalse(safelistWithoutTags.isSafeTag("span"));
    }

    @Test
    public void testAddAttributes() {
        // Given
        String tag = "a";
        String[] attributes = {"href", "title"};

        // When
        Safelist safelistWithAttributes = safelist.addAttributes(tag, attributes);

        // Then
        assertTrue(safelistWithAttributes.isSafeAttribute(tag, element, attribute));
    }

    @Test
    public void testRemoveAttributes() {
        // Given
        String tag = "a";
        String[] attributes = {"href", "title"};
        safelist.addAttributes(tag, attributes);

        // When
        Safelist safelistWithoutAttributes = safelist.removeAttributes(tag, attributes);

        // Then
        assertFalse(safelistWithoutAttributes.isSafeAttribute(tag, element, attribute));
    }

    @Test
    public void testAddEnforcedAttribute() {
        // Given
        String tag = "a";
        String attribute = "rel";
        String value = "nofollow";

        // When
        Safelist safelistWithEnforcedAttribute = safelist.addEnforcedAttribute(tag, attribute, value);

        // Then
        Attributes enforcedAttributes = safelistWithEnforcedAttribute.getEnforcedAttributes(tag);
        assertNotNull(enforcedAttributes);
        assertEquals(value, enforcedAttributes.get(attribute));
    }

    @Test
    public void testRemoveEnforcedAttribute() {
        // Given
        String tag = "a";
        String attribute = "rel";
        String value = "nofollow";
        safelist.addEnforcedAttribute(tag, attribute, value);

        // When
        Safelist safelistWithoutEnforcedAttribute = safelist.removeEnforcedAttribute(tag, attribute);

        // Then
        Attributes enforcedAttributes = safelistWithoutEnforcedAttribute.getEnforcedAttributes(tag);
        assertNotNull(enforcedAttributes);
        assertNull(enforcedAttributes.get(attribute));
    }

    @Test
    public void testPreserveRelativeLinks() {
        // Given
        boolean preserve = true;

        // When
        Safelist safelistWithPreserveRelativeLinks = safelist.preserveRelativeLinks(preserve);

        // Then
        assertTrue(safelistWithPreserveRelativeLinks.preserveRelativeLinks());
    }

    @Test
    public void testAddProtocols() {
        // Given
        String tag = "a";
        String attribute = "href";
        String[] protocols = {"http", "https"};

        // When
        Safelist safelistWithProtocols = safelist.addProtocols(tag, attribute, protocols);

        // Then
        // TODO: Add assertion for protocols
    }

    @Test
    public void testRemoveProtocols() {
        // Given
        String tag = "a";
        String attribute = "href";
        String[] protocols = {"http", "https"};
        safelist.addProtocols(tag, attribute, protocols);

        // When
        Safelist safelistWithoutProtocols = safelist.removeProtocols(tag, attribute, protocols);

        // Then
        // TODO: Add assertion for protocols
    }

    @Test
    public void testIsSafeTag() {
        // Given
        String tag = "p";

        // When
        boolean isSafeTag = safelist.isSafeTag(tag);

        // Then
        assertFalse(isSafeTag);
    }

    @Test
    public void testIsSafeAttribute() {
        // Given
        String tagName = "a";
        Element element = mock(Element.class);
        Attribute attribute = mock(Attribute.class);

        // When
        boolean isSafeAttribute = safelist.isSafeAttribute(tagName, element, attribute);

        // Then
        assertFalse(isSafeAttribute);
    }

    @Test
    public void testGetEnforcedAttributes() {
        // Given
        String tagName = "a";

        // When
        Attributes enforcedAttributes = safelist.getEnforcedAttributes(tagName);

        // Then
        assertNotNull(enforcedAttributes);
        assertTrue(enforcedAttributes.isEmpty());
    }
}
import org.jsoup.helper.Validate;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.parser.TagSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagSetTest {

    @Mock
    private Tag mockTag;

    @Mock
    private Consumer<Tag> mockCustomizer;

    private TagSet tagSet;

    @BeforeEach
    void setup() {
        tagSet = TagSet.Html();
    }

    @Test
    void testHtml() {
        // Given
        TagSet expectedTagSet = TagSet.Html();

        // When
        TagSet actualTagSet = TagSet.Html();

        // Then
        assertEquals(expectedTagSet, actualTagSet);
    }

    @Test
    void testAdd() {
        // Given
        Tag tag = mockTag;

        // When
        TagSet result = tagSet.add(tag);

        // Then
        assertEquals(tagSet, result);
        verify(tag).set(Tag.Known);
    }

    @Test
    void testGet() {
        // Given
        String tagName = "test";
        String namespace = Parser.NamespaceHtml;

        // When
        Tag result = tagSet.get(tagName, namespace);

        // Then
        assertNotNull(result);
    }

    @Test
    void testValueOf() {
        // Given
        String tagName = "test";
        String namespace = Parser.NamespaceHtml;

        // When
        Tag result = tagSet.valueOf(tagName, namespace);

        // Then
        assertNotNull(result);
    }

    @Test
    void testValueOfWithSettings() {
        // Given
        String tagName = "test";
        String namespace = Parser.NamespaceHtml;
        ParseSettings settings = mock(ParseSettings.class);

        // When
        Tag result = tagSet.valueOf(tagName, namespace, settings);

        // Then
        assertNotNull(result);
    }

    @Test
    void testOnNewTag() {
        // Given
        Consumer<Tag> customizer = mockCustomizer;

        // When
        TagSet result = tagSet.onNewTag(customizer);

        // Then
        assertEquals(tagSet, result);
    }

    @Test
    void testEquals() {
        // Given
        TagSet otherTagSet = TagSet.Html();

        // When
        boolean result = tagSet.equals(otherTagSet);

        // Then
        assertTrue(result);
    }

    @Test
    void testHashCode() {
        // Given
        int expectedHashCode = tagSet.hashCode();

        // When
        int actualHashCode = tagSet.hashCode();

        // Then
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    void testInitHtmlDefault() {
        // Given
        TagSet expectedTagSet = TagSet.initHtmlDefault();

        // When
        TagSet actualTagSet = TagSet.initHtmlDefault();

        // Then
        assertEquals(expectedTagSet, actualTagSet);
    }

    @Test
    void testSetupTags() {
        // Given
        String namespace = Parser.NamespaceHtml;
        String[] tagNames = {"test"};
        Consumer<Tag> tagModifier = mock(Consumer.class);

        // When
        TagSet result = tagSet.setupTags(namespace, tagModifier);

        // Then
        assertEquals(tagSet, result);
    }

    @Test
    void testCopyCustomizers() {
        // Given
        TagSet baseTagSet = TagSet.Html();
        ArrayList<Consumer<Tag>> customizers = new ArrayList<>();

        // When
        ArrayList<Consumer<Tag>> result = TagSet.copyCustomizers(baseTagSet);

        // Then
        assertNotNull(result);
    }

    @Test
    void testDoAdd() {
        // Given
        Tag tag = mockTag;

        // When
        tagSet.doAdd(tag);

        // Then
        verify(tag).set(Tag.Known);
    }
}
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TagTest {

    private Tag tag;

    @BeforeEach
    public void setup() {
        tag = new Tag("div");
    }

    @Test
    public void testGetName() {
        // Given: a tag with name "div"
        // When: getName is called
        String name = tag.getName();
        // Then: the name is "div"
        assertEquals("div", name);
    }

    @Test
    public void testName() {
        // Given: a tag with name "div"
        // When: name is called
        String name = tag.name();
        // Then: the name is "div"
        assertEquals("div", name);
    }

    @Test
    public void testName_SetName() {
        // Given: a tag with name "div"
        // When: name is set to "span"
        Tag newTag = tag.name("span");
        // Then: the name is "span"
        assertEquals("span", newTag.getName());
    }

    @Test
    public void testPrefix() {
        // Given: a tag with name "div"
        // When: prefix is called
        String prefix = tag.prefix();
        // Then: the prefix is empty
        assertEquals("", prefix);
    }

    @Test
    public void testLocalName() {
        // Given: a tag with name "div"
        // When: localName is called
        String localName = tag.localName();
        // Then: the local name is "div"
        assertEquals("div", localName);
    }

    @Test
    public void testNormalName() {
        // Given: a tag with name "div"
        // When: normalName is called
        String normalName = tag.normalName();
        // Then: the normal name is "div"
        assertEquals("div", normalName);
    }

    @Test
    public void testNamespace() {
        // Given: a tag with name "div"
        // When: namespace is called
        String namespace = tag.namespace();
        // Then: the namespace is the HTML namespace
        assertEquals(Parser.NamespaceHtml, namespace);
    }

    @Test
    public void testNamespace_SetNamespace() {
        // Given: a tag with name "div"
        // When: namespace is set to "http://www.w3.org/1999/xhtml"
        Tag newTag = tag.namespace("http://www.w3.org/1999/xhtml");
        // Then: the namespace is "http://www.w3.org/1999/xhtml"
        assertEquals("http://www.w3.org/1999/xhtml", newTag.namespace());
    }

    @Test
    public void testSet_GetOption() {
        // Given: a tag with no options set
        // When: option Block is set
        Tag newTag = tag.set(Tag.Block);
        // Then: option Block is set
        assertTrue(newTag.is(Tag.Block));
    }

    @Test
    public void testClear_GetOption() {
        // Given: a tag with option Block set
        Tag newTag = tag.set(Tag.Block);
        // When: option Block is cleared
        newTag = newTag.clear(Tag.Block);
        // Then: option Block is not set
        assertFalse(newTag.is(Tag.Block));
    }

    @Test
    public void testValueOf_TagName_Namespace_Settings() {
        // Given: a tag name "div", namespace "http://www.w3.org/1999/xhtml", and settings
        Tag newTag = Tag.valueOf("div", "http://www.w3.org/1999/xhtml", ParseSettings.preserveCase);
        // Then: the tag name is "div", namespace is "http://www.w3.org/1999/xhtml"
        assertEquals("div", newTag.getName());
        assertEquals("http://www.w3.org/1999/xhtml", newTag.namespace());
    }

    @Test
    public void testValueOf_TagName() {
        // Given: a tag name "div"
        Tag newTag = Tag.valueOf("div");
        // Then: the tag name is "div", namespace is the HTML namespace
        assertEquals("div", newTag.getName());
        assertEquals(Parser.NamespaceHtml, newTag.namespace());
    }

    @Test
    public void testValueOf_TagName_Settings() {
        // Given: a tag name "div" and settings
        Tag newTag = Tag.valueOf("div", ParseSettings.preserveCase);
        // Then: the tag name is "div", namespace is the HTML namespace
        assertEquals("div", newTag.getName());
        assertEquals(Parser.NamespaceHtml, newTag.namespace());
    }

    @Test
    public void testIsBlock() {
        // Given: a tag with no options set
        // When: isBlock is called
        boolean isBlock = tag.isBlock();
        // Then: the tag is not a block tag
        assertFalse(isBlock);
    }

    @Test
    public void testFormatAsBlock() {
        // Given: a tag with no options set
        // When: formatAsBlock is called
        boolean formatAsBlock = tag.formatAsBlock();
        // Then: the tag does not format as a block
        assertFalse(formatAsBlock);
    }

    @Test
    public void testIsInline() {
        // Given: a tag with no options set
        // When: isInline is called
        boolean isInline = tag.isInline();
        // Then: the tag is an inline tag
        assertTrue(isInline);
    }

    @Test
    public void testIsEmpty() {
        // Given: a tag with no options set
        // When: isEmpty is called
        boolean isEmpty = tag.isEmpty();
        // Then: the tag is not empty
        assertFalse(isEmpty);
    }

    @Test
    public void testIsSelfClosing() {
        // Given: a tag with no options set
        // When: isSelfClosing is called
        boolean isSelfClosing = tag.isSelfClosing();
        // Then: the tag is not self-closing
        assertFalse(isSelfClosing);
    }

    @Test
    public void testIsKnownTag() {
        // Given: a tag with no options set
        // When: isKnownTag is called
        boolean isKnownTag = tag.isKnownTag();
        // Then: the tag is not a known tag
        assertFalse(isKnownTag);
    }

    @Test
    public void testIsKnownTag_Static() {
        // Given: a tag name "div"
        // When: isKnownTag is called
        boolean isKnownTag = Tag.isKnownTag("div");
        // Then: the tag is a known tag
        assertTrue(isKnownTag);
    }

    @Test
    public void testPreserveWhitespace() {
        // Given: a tag with no options set
        // When: preserveWhitespace is called
        boolean preserveWhitespace = tag.preserveWhitespace();
        // Then: the tag does not preserve whitespace
        assertFalse(preserveWhitespace);
    }

    @Test
    public void testIsFormSubmittable() {
        // Given: a tag with no options set
        // When: isFormSubmittable is called
        boolean isFormSubmittable = tag.isFormSubmittable();
        // Then: the tag is not form submittable
        assertFalse(isFormSubmittable);
    }

    @Test
    public void testEquals() {
        // Given: two tags with the same name and namespace
        Tag otherTag = new Tag("div");
        // When: equals is called
        boolean equals = tag.equals(otherTag);
        // Then: the tags are equal
        assertTrue(equals);
    }

    @Test
    public void testHashCode() {
        // Given: a tag with name "div" and namespace "http://www.w3.org/1999/xhtml"
        // When: hashCode is called
        int hashCode = tag.hashCode();
        // Then: the hash code is not zero
        assertNotEquals(0, hashCode);
    }

    @Test
    public void testToString() {
        // Given: a tag with name "div"
        // When: toString is called
        String toString = tag.toString();
        // Then: the string representation is "div"
        assertEquals("div", toString);
    }
}
import org.jsoup.internal.Normalizer;
import org.jsoup.internal.QuietAppendable;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AttributeTest {

    @Mock
    private Attributes parent;

    private Attribute attribute;

    @BeforeEach
    public void setup() {
        attribute = new Attribute("test", "value", parent);
    }

    @Test
    public void testGetKey() {
        assertEquals("test", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newTest");
        assertEquals("newTest", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testHasDeclaredValue() {
        assertTrue(attribute.hasDeclaredValue());
    }

    @Test
    public void testSetValue() {
        String oldValue = attribute.setValue("newValue");
        assertEquals("value", oldValue);
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testPrefix() {
        assertEquals("", attribute.prefix());
    }

    @Test
    public void testLocalName() {
        assertEquals("test", attribute.localName());
    }

    @Test
    public void testNamespace() {
        assertEquals("", attribute.namespace());
    }

    @Test
    public void testHtml() {
        assertNotNull(attribute.html());
    }

    @Test
    public void testGetValidKey() {
        String validKey = Attribute.getValidKey("test", Document.OutputSettings.Syntax.html);
        assertEquals("test", validKey);
    }

    @Test
    public void testToString() {
        assertNotNull(attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute encodedAttribute = Attribute.createFromEncoded("test", "encodedValue");
        assertNotNull(encodedAttribute);
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
    }

    @Test
    public void testEquals() {
        Attribute otherAttribute = new Attribute("test", "value", parent);
        assertTrue(attribute.equals(otherAttribute));
    }

    @Test
    public void testHashCode() {
        int hashCode = attribute.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertNotNull(clonedAttribute);
    }

    @Test
    public void testShouldCollapseAttribute() {
        boolean shouldCollapse = Attribute.shouldCollapseAttribute("checked", null, new Document.OutputSettings());
        assertTrue(shouldCollapse);
    }
}
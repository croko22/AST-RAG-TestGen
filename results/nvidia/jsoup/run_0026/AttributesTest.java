import org.jsoup.nodes.Attributes;
import org.jsoup.parser.ParseSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AttributesTest {

    private Attributes attributes;

    @BeforeEach
    public void setup() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        // Given
        attributes.add("key", "value");

        // When
        String result = attributes.get("key");

        // Then
        assertEquals("value", result);
    }

    @Test
    public void testGetIgnoreCase() {
        // Given
        attributes.add("Key", "value");

        // When
        String result = attributes.getIgnoreCase("key");

        // Then
        assertEquals("value", result);
    }

    @Test
    public void testAttribute() {
        // Given
        attributes.add("key", "value");

        // When
        Attributes.Attribute attribute = attributes.attribute("key");

        // Then
        assertNotNull(attribute);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testAdd() {
        // Given
        attributes.add("key1", "value1");
        attributes.add("key2", "value2");

        // When
        int size = attributes.size();

        // Then
        assertEquals(2, size);
    }

    @Test
    public void testPut() {
        // Given
        attributes.add("key", "value1");
        attributes.put("key", "value2");

        // When
        String result = attributes.get("key");

        // Then
        assertEquals("value2", result);
    }

    @Test
    public void testPutBoolean() {
        // Given
        attributes.put("key", true);

        // When
        boolean result = attributes.hasKey("key");

        // Then
        assertTrue(result);
    }

    @Test
    public void testRemove() {
        // Given
        attributes.add("key", "value");

        // When
        attributes.remove("key");

        // Then
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        // Given
        attributes.add("Key", "value");

        // When
        attributes.removeIgnoreCase("key");

        // Then
        assertFalse(attributes.hasKey("Key"));
    }

    @Test
    public void testHasKey() {
        // Given
        attributes.add("key", "value");

        // When
        boolean result = attributes.hasKey("key");

        // Then
        assertTrue(result);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        // Given
        attributes.add("Key", "value");

        // When
        boolean result = attributes.hasKeyIgnoreCase("key");

        // Then
        assertTrue(result);
    }

    @Test
    public void testHasDeclaredValueForKey() {
        // Given
        attributes.add("key", "value");

        // When
        boolean result = attributes.hasDeclaredValueForKey("key");

        // Then
        assertTrue(result);
    }

    @Test
    public void testHasDeclaredValueForKeyIgnoreCase() {
        // Given
        attributes.add("Key", "value");

        // When
        boolean result = attributes.hasDeclaredValueForKeyIgnoreCase("key");

        // Then
        assertTrue(result);
    }

    @Test
    public void testSize() {
        // Given
        attributes.add("key1", "value1");
        attributes.add("key2", "value2");

        // When
        int size = attributes.size();

        // Then
        assertEquals(2, size);
    }

    @Test
    public void testIsEmpty() {
        // Given

        // When
        boolean result = attributes.isEmpty();

        // Then
        assertTrue(result);
    }

    @Test
    public void testAddAll() {
        // Given
        Attributes otherAttributes = new Attributes();
        otherAttributes.add("key1", "value1");
        otherAttributes.add("key2", "value2");
        attributes.add("key3", "value3");

        // When
        attributes.addAll(otherAttributes);

        // Then
        assertEquals(3, attributes.size());
    }

    @Test
    public void testSourceRange() {
        // Given
        Attributes otherAttributes = new Attributes();
        otherAttributes.add("key", "value");

        // When
        Attributes result = attributes.sourceRange("key");

        // Then
        assertNotNull(result);
    }

    @Test
    public void testIterator() {
        // Given
        attributes.add("key1", "value1");
        attributes.add("key2", "value2");

        // When
        Iterator<Attributes.Attribute> iterator = attributes.iterator();

        // Then
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testAsList() {
        // Given
        attributes.add("key1", "value1");
        attributes.add("key2", "value2");

        // When
        List<Attributes.Attribute> list = attributes.asList();

        // Then
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    public void testDataset() {
        // Given
        attributes.add("data-key", "value");

        // When
        Map<String, String> dataset = attributes.dataset();

        // Then
        assertNotNull(dataset);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testHtml() {
        // Given
        attributes.add("key", "value");

        // When
        String html = attributes.html();

        // Then
        assertNotNull(html);
    }

    @Test
    public void testToString() {
        // Given
        attributes.add("key", "value");

        // When
        String toString = attributes.toString();

        // Then
        assertNotNull(toString);
    }

    @Test
    public void testEquals() {
        // Given
        Attributes otherAttributes = new Attributes();
        otherAttributes.add("key", "value");

        // When
        boolean result = attributes.equals(otherAttributes);

        // Then
        assertFalse(result);
    }

    @Test
    public void testHashCode() {
        // Given

        // When
        int hashCode = attributes.hashCode();

        // Then
        assertNotNull(hashCode);
    }

    @Test
    public void testClone() {
        // Given
        attributes.add("key", "value");

        // When
        Attributes clone = attributes.clone();

        // Then
        assertNotNull(clone);
        assertEquals(attributes.size(), clone.size());
    }

    @Test
    public void testNormalize() {
        // Given
        attributes.add("Key", "value");

        // When
        attributes.normalize();

        // Then
        assertEquals("key", attributes.get("key"));
    }

    @Test
    public void testDeduplicate() {
        // Given
        attributes.add("key", "value1");
        attributes.add("key", "value2");
        ParseSettings parseSettings = new ParseSettings();

        // When
        int deduplicate = attributes.deduplicate(parseSettings);

        // Then
        assertEquals(1, deduplicate);
    }
}
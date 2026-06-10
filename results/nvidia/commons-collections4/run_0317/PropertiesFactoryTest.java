import org.apache.commons.collections4.properties.PropertiesFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PropertiesFactoryTest {

    @InjectMocks
    private PropertiesFactory propertiesFactory;

    @Test
    public void testClear() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");

        // When
        propertiesFactory.clear();

        // Then
        assertTrue(propertiesFactory.isEmpty());
    }

    @Test
    public void testCompute() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        BiFunction<Object, Object, Object> remappingFunction = (k, v) -> "new value";

        // When
        Object result = propertiesFactory.compute("key", remappingFunction);

        // Then
        assertEquals("new value", result);
    }

    @Test
    public void testComputeIfAbsent() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        Function<Object, Object> mappingFunction = k -> "new value";

        // When
        Object result = propertiesFactory.computeIfAbsent("key", mappingFunction);

        // Then
        assertEquals("new value", result);
    }

    @Test
    public void testComputeIfPresent() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");
        BiFunction<Object, Object, Object> remappingFunction = (k, v) -> "new value";

        // When
        Object result = propertiesFactory.computeIfPresent("key", remappingFunction);

        // Then
        assertEquals("new value", result);
    }

    @Test
    public void testContains() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");

        // When
        boolean result = propertiesFactory.contains("value");

        // Then
        assertTrue(result);
    }

    @Test
    public void testContainsKey() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");

        // When
        boolean result = propertiesFactory.containsKey("key");

        // Then
        assertTrue(result);
    }

    @Test
    public void testContainsValue() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");

        // When
        boolean result = propertiesFactory.containsValue("value");

        // Then
        assertTrue(result);
    }

    @Test
    public void testElements() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");

        // When
        Enumeration<Object> result = propertiesFactory.elements();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testEntrySet() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");

        // When
        Set<Map.Entry<Object, Object>> result = propertiesFactory.entrySet();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testEquals() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        Properties otherProperties = propertiesFactory.createProperties();

        // When
        boolean result = propertiesFactory.equals(otherProperties);

        // Then
        assertTrue(result);
    }

    @Test
    public void testForEach() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");
        BiConsumer<Object, Object> action = (k, v) -> {
            // Do something
        };

        // When
        propertiesFactory.forEach(action);

        // Then
        // No exception thrown
    }

    @Test
    public void testGet() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");

        // When
        Object result = propertiesFactory.get("key");

        // Then
        assertEquals("value", result);
    }

    @Test
    public void testGetOrDefault() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");

        // When
        Object result = propertiesFactory.getOrDefault("key", "default");

        // Then
        assertEquals("value", result);
    }

    @Test
    public void testGetProperty() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");

        // When
        String result = propertiesFactory.getProperty("key");

        // Then
        assertEquals("value", result);
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");

        // When
        String result = propertiesFactory.getProperty("key", "default");

        // Then
        assertEquals("value", result);
    }

    @Test
    public void testHashCode() {
        // Given
        Properties properties = propertiesFactory.createProperties();

        // When
        int result = propertiesFactory.hashCode();

        // Then
        assertEquals(0, result);
    }

    @Test
    public void testIsEmpty() {
        // Given
        Properties properties = propertiesFactory.createProperties();

        // When
        boolean result = propertiesFactory.isEmpty();

        // Then
        assertTrue(result);
    }

    @Test
    public void testKeys() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");

        // When
        Enumeration<Object> result = propertiesFactory.keys();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testKeySet() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");

        // When
        Set<Object> result = propertiesFactory.keySet();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testLoad() throws IOException {
        // Given
        Properties properties = propertiesFactory.createProperties();
        InputStream inStream = new ByteArrayInputStream("key=value".getBytes());

        // When
        propertiesFactory.load(inStream);

        // Then
        // No exception thrown
    }

    @Test
    public void testLoadFromReader() throws IOException {
        // Given
        Properties properties = propertiesFactory.createProperties();
        Reader reader = new StringReader("key=value");

        // When
        propertiesFactory.load(reader);

        // Then
        // No exception thrown
    }

    @Test
    public void testLoadFromXML() throws IOException {
        // Given
        Properties properties = propertiesFactory.createProperties();
        InputStream in = new ByteArrayInputStream("<!DOCTYPE properties><properties><entry key='key'>value</entry></properties>".getBytes());

        // When
        propertiesFactory.loadFromXML(in);

        // Then
        // No exception thrown
    }

    @Test
    public void testMerge() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        BiFunction<Object, Object, Object> remappingFunction = (k, v) -> "new value";

        // When
        Object result = propertiesFactory.merge("key", "value", remappingFunction);

        // Then
        assertEquals("new value", result);
    }

    @Test
    public void testPropertyNames() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");

        // When
        Enumeration<?> result = propertiesFactory.propertyNames();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testPut() {
        // Given
        Properties properties = propertiesFactory.createProperties();

        // When
        Object result = propertiesFactory.put("key", "value");

        // Then
        assertEquals("value", result);
    }

    @Test
    public void testPutAll() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        Map<Object, Object> map = new HashMap<>();
        map.put("key", "value");

        // When
        propertiesFactory.putAll(map);

        // Then
        // No exception thrown
    }

    @Test
    public void testPutIfAbsent() {
        // Given
        Properties properties = propertiesFactory.createProperties();

        // When
        Object result = propertiesFactory.putIfAbsent("key", "value");

        // Then
        assertEquals("value", result);
    }

    @Test
    public void testRemove() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");

        // When
        Object result = propertiesFactory.remove("key");

        // Then
        assertEquals("value", result);
    }

    @Test
    public void testRemoveWithOldValue() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");

        // When
        boolean result = propertiesFactory.remove("key", "value");

        // Then
        assertTrue(result);
    }

    @Test
    public void testReplace() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");

        // When
        Object result = propertiesFactory.replace("key", "new value");

        // Then
        assertEquals("new value", result);
    }

    @Test
    public void testReplaceWithOldValue() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");

        // When
        boolean result = propertiesFactory.replace("key", "value", "new value");

        // Then
        assertTrue(result);
    }

    @Test
    public void testReplaceAll() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        BiFunction<Object, Object, Object> function = (k, v) -> "new value";

        // When
        propertiesFactory.replaceAll(function);

        // Then
        // No exception thrown
    }

    @Test
    public void testSave() throws IOException {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");
        OutputStream out = new ByteArrayOutputStream();

        // When
        propertiesFactory.save(out, "comments");

        // Then
        // No exception thrown
    }

    @Test
    public void testSetProperty() {
        // Given
        Properties properties = propertiesFactory.createProperties();

        // When
        Object result = propertiesFactory.setProperty("key", "value");

        // Then
        assertEquals("value", result);
    }

    @Test
    public void testSize() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");

        // When
        int result = propertiesFactory.size();

        // Then
        assertEquals(1, result);
    }

    @Test
    public void testStringPropertyNames() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");

        // When
        Set<String> result = propertiesFactory.stringPropertyNames();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testToString() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");

        // When
        String result = propertiesFactory.toString();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testValues() {
        // Given
        Properties properties = propertiesFactory.createProperties();
        properties.put("key", "value");

        // When
        Collection<Object> result = propertiesFactory.values();

        // Then
        assertNotNull(result);
    }
}
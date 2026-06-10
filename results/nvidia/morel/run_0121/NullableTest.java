import net.hydromatic.morel.eval.Prop;
import net.hydromatic.morel.eval.Prop.Output;
import net.hydromatic.morel.util.JavaVersion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PropTest {

    @Mock
    private JavaVersion javaVersion;

    private Map<Prop, Object> map;

    @BeforeEach
    void setup() {
        map = new HashMap<>();
    }

    @Test
    void testLookup() {
        // Given
        String propName = "banner";

        // When
        Prop prop = Prop.lookup(propName);

        // Then
        assertNotNull(prop);
        assertEquals(propName, prop.camelName);
    }

    @Test
    void testLookupNotFound() {
        // Given
        String propName = "non-existent-prop";

        // When and Then
        assertThrows(RuntimeException.class, () -> Prop.lookup(propName));
    }

    @Test
    void testGet() {
        // Given
        Prop prop = Prop.BANNER;
        map.put(prop, "test-value");

        // When
        Object value = prop.get(map);

        // Then
        assertNotNull(value);
        assertEquals("test-value", value);
    }

    @Test
    void testGetDefaultValue() {
        // Given
        Prop prop = Prop.BANNER;

        // When
        Object value = prop.get(map);

        // Then
        assertNotNull(value);
        assertEquals(JavaVersion.banner(null), value);
    }

    @Test
    void testBooleanValue() {
        // Given
        Prop prop = Prop.HYBRID;
        map.put(prop, true);

        // When
        boolean value = prop.booleanValue(map);

        // Then
        assertTrue(value);
    }

    @Test
    void testBooleanValueInvalidType() {
        // Given
        Prop prop = Prop.HYBRID;
        map.put(prop, "invalid-value");

        // When and Then
        assertThrows(RuntimeException.class, () -> prop.booleanValue(map));
    }

    @Test
    void testIntValue() {
        // Given
        Prop prop = Prop.INLINE_PASS_COUNT;
        map.put(prop, 10);

        // When
        int value = prop.intValue(map);

        // Then
        assertEquals(10, value);
    }

    @Test
    void testIntValueInvalidType() {
        // Given
        Prop prop = Prop.INLINE_PASS_COUNT;
        map.put(prop, "invalid-value");

        // When and Then
        assertThrows(RuntimeException.class, () -> prop.intValue(map));
    }

    @Test
    void testStringValue() {
        // Given
        Prop prop = Prop.BANNER;
        map.put(prop, "test-value");

        // When
        String value = prop.stringValue(map);

        // Then
        assertNotNull(value);
        assertEquals("test-value", value);
    }

    @Test
    void testStringValueInvalidType() {
        // Given
        Prop prop = Prop.BANNER;
        map.put(prop, 10);

        // When and Then
        assertThrows(RuntimeException.class, () -> prop.stringValue(map));
    }

    @Test
    void testFileValue() {
        // Given
        Prop prop = Prop.DIRECTORY;
        map.put(prop, new File("test-directory"));

        // When
        File value = prop.fileValue(map);

        // Then
        assertNotNull(value);
        assertEquals("test-directory", value.getAbsolutePath());
    }

    @Test
    void testFileValueInvalidType() {
        // Given
        Prop prop = Prop.DIRECTORY;
        map.put(prop, "invalid-value");

        // When and Then
        assertThrows(RuntimeException.class, () -> prop.fileValue(map));
    }

    @Test
    void testEnumValue() {
        // Given
        Prop prop = Prop.OUTPUT;
        map.put(prop, Output.TABULAR);

        // When
        Output value = prop.enumValue(map, Output.class);

        // Then
        assertNotNull(value);
        assertEquals(Output.TABULAR, value);
    }

    @Test
    void testEnumValueInvalidType() {
        // Given
        Prop prop = Prop.OUTPUT;
        map.put(prop, "invalid-value");

        // When and Then
        assertThrows(RuntimeException.class, () -> prop.enumValue(map, Output.class));
    }

    @Test
    void testSetLenient() {
        // Given
        Prop prop = Prop.OUTPUT;
        map.put(prop, Output.TABULAR);

        // When
        prop.setLenient(map, "CLASSIC");

        // Then
        assertEquals(Output.CLASSIC, map.get(prop));
    }

    @Test
    void testSetLenientInvalidValue() {
        // Given
        Prop prop = Prop.OUTPUT;
        map.put(prop, Output.TABULAR);

        // When and Then
        assertThrows(RuntimeException.class, () -> prop.setLenient(map, "INVALID-VALUE"));
    }

    @Test
    void testSet() {
        // Given
        Prop prop = Prop.BANNER;
        map.put(prop, "test-value");

        // When
        prop.set(map, "new-test-value");

        // Then
        assertEquals("new-test-value", map.get(prop));
    }

    @Test
    void testSetInvalidType() {
        // Given
        Prop prop = Prop.BANNER;
        map.put(prop, 10);

        // When and Then
        assertThrows(RuntimeException.class, () -> prop.set(map, "new-test-value"));
    }

    @Test
    void testRemove() {
        // Given
        Prop prop = Prop.BANNER;
        map.put(prop, "test-value");

        // When
        Object oldValue = prop.remove(map);

        // Then
        assertNotNull(oldValue);
        assertEquals("test-value", oldValue);
        assertFalse(map.containsKey(prop));
    }

    @Test
    void testTypeName() {
        // Given
        Prop prop = Prop.BANNER;

        // When
        String typeName = prop.typeName();

        // Then
        assertNotNull(typeName);
        assertEquals("string", typeName);
    }

    @Test
    void testDefaultValue() {
        // Given
        Prop prop = Prop.BANNER;

        // When
        Object defaultValue = prop.defaultValue();

        // Then
        assertNotNull(defaultValue);
        assertEquals("Morel version ...", defaultValue);
    }
}
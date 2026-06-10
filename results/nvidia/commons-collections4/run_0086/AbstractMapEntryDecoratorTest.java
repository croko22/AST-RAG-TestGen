import org.apache.commons.collections4.KeyValue;
import org.apache.commons.collections4.keyvalue.AbstractMapEntryDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractMapEntryDecoratorTest {

    @Mock
    private Map.Entry<String, String> entry;

    private AbstractMapEntryDecorator<String, String> decorator;

    @BeforeEach
    public void setup() {
        decorator = new AbstractMapEntryDecorator<>(entry) {
        };
    }

    @Test
    public void testEquals_SameObject() {
        // Given: same object
        // When: equals is called
        boolean result = decorator.equals(decorator);
        // Then: true is returned
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_SameEntry() {
        // Given: different object with same entry
        AbstractMapEntryDecorator<String, String> otherDecorator = new AbstractMapEntryDecorator<>(entry) {
        };
        // When: equals is called
        boolean result = decorator.equals(otherDecorator);
        // Then: true is returned if entries are equal
        when(entry.equals(any())).thenReturn(true);
        assertTrue(decorator.equals(otherDecorator));
    }

    @Test
    public void testEquals_DifferentObject_DifferentEntry() {
        // Given: different object with different entry
        Map.Entry<String, String> otherEntry = mock(Map.Entry.class);
        AbstractMapEntryDecorator<String, String> otherDecorator = new AbstractMapEntryDecorator<>(otherEntry) {
        };
        // When: equals is called
        boolean result = decorator.equals(otherDecorator);
        // Then: false is returned if entries are not equal
        when(entry.equals(any())).thenReturn(false);
        assertFalse(decorator.equals(otherDecorator));
    }

    @Test
    public void testGetKey() {
        // Given: entry with key
        String key = "key";
        when(entry.getKey()).thenReturn(key);
        // When: getKey is called
        String result = decorator.getKey();
        // Then: key is returned
        assertEquals(key, result);
    }

    @Test
    public void testGetValue() {
        // Given: entry with value
        String value = "value";
        when(entry.getValue()).thenReturn(value);
        // When: getValue is called
        String result = decorator.getValue();
        // Then: value is returned
        assertEquals(value, result);
    }

    @Test
    public void testHashCode() {
        // Given: entry with hash code
        int hashCode = 123;
        when(entry.hashCode()).thenReturn(hashCode);
        // When: hashCode is called
        int result = decorator.hashCode();
        // Then: hash code is returned
        assertEquals(hashCode, result);
    }

    @Test
    public void testSetValue() {
        // Given: entry with new value
        String newValue = "new value";
        when(entry.setValue(any())).thenReturn(newValue);
        // When: setValue is called
        String result = decorator.setValue(newValue);
        // Then: new value is returned
        assertEquals(newValue, result);
    }

    @Test
    public void testToString() {
        // Given: entry with string representation
        String toString = "toString";
        when(entry.toString()).thenReturn(toString);
        // When: toString is called
        String result = decorator.toString();
        // Then: string representation is returned
        assertEquals(toString, result);
    }
}
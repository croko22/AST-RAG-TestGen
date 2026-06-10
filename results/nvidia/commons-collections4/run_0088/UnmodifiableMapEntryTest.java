import org.apache.commons.collections4.KeyValue;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.keyvalue.UnmodifiableMapEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnmodifiableMapEntryTest {

    @Mock
    private KeyValue<String, String> keyValue;

    @Mock
    private Map.Entry<String, String> mapEntry;

    private UnmodifiableMapEntry<String, String> unmodifiableMapEntry;

    @BeforeEach
    public void setup() {
        when(keyValue.getKey()).thenReturn("key");
        when(keyValue.getValue()).thenReturn("value");
        when(mapEntry.getKey()).thenReturn("key");
        when(mapEntry.getValue()).thenReturn("value");
    }

    @Test
    public void testConstructor_KeyValue() {
        // Given: a KeyValue object
        // When: create an UnmodifiableMapEntry from the KeyValue
        unmodifiableMapEntry = new UnmodifiableMapEntry<>(keyValue);
        // Then: verify the key and value
        assertEquals("key", unmodifiableMapEntry.getKey());
        assertEquals("value", unmodifiableMapEntry.getValue());
    }

    @Test
    public void testConstructor_MapEntry() {
        // Given: a Map.Entry object
        // When: create an UnmodifiableMapEntry from the Map.Entry
        unmodifiableMapEntry = new UnmodifiableMapEntry<>(mapEntry);
        // Then: verify the key and value
        assertEquals("key", unmodifiableMapEntry.getKey());
        assertEquals("value", unmodifiableMapEntry.getValue());
    }

    @Test
    public void testConstructor_KeyValue_Null() {
        // Given: a null KeyValue object
        // When / Then: expect a NullPointerException
        assertThrows(NullPointerException.class, () -> new UnmodifiableMapEntry<>(null));
    }

    @Test
    public void testConstructor_MapEntry_Null() {
        // Given: a null Map.Entry object
        // When / Then: expect a NullPointerException
        assertThrows(NullPointerException.class, () -> new UnmodifiableMapEntry<>(null));
    }

    @Test
    public void testSetValue() {
        // Given: an UnmodifiableMapEntry object
        unmodifiableMapEntry = new UnmodifiableMapEntry<>("key", "value");
        // When: call setValue
        // Then: expect an UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMapEntry.setValue("new value"));
    }

    @Test
    public void testSetValue_NullValue() {
        // Given: an UnmodifiableMapEntry object
        unmodifiableMapEntry = new UnmodifiableMapEntry<>("key", "value");
        // When: call setValue with a null value
        // Then: expect an UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMapEntry.setValue(null));
    }
}
import org.apache.commons.collections4.map.AbstractInputCheckedMapDecorator;
import org.apache.commons.collections4.keyvalue.AbstractMapEntryDecorator;
import org.apache.commons.collections4.iterators.AbstractIteratorDecorator;
import org.apache.commons.collections4.set.AbstractSetDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractInputCheckedMapDecoratorTest {

    @Mock
    private Map<String, String> map;

    private AbstractInputCheckedMapDecorator<String, String> decorator;

    @BeforeEach
    public void setup() {
        decorator = new AbstractInputCheckedMapDecorator<String, String>(map) {
            @Override
            protected String checkSetValue(String value) {
                return value;
            }
        };
    }

    @Test
    public void testEntrySet() {
        // Given
        when(map.entrySet()).thenReturn(Set.of(new HashMap.SimpleEntry<>("key", "value")));

        // When
        Set<Map.Entry<String, String>> entrySet = decorator.entrySet();

        // Then
        assertNotNull(entrySet);
        assertEquals(1, entrySet.size());
        verify(map, times(1)).entrySet();
    }

    @Test
    public void testEntrySet_Iterator() {
        // Given
        when(map.entrySet()).thenReturn(Set.of(new HashMap.SimpleEntry<>("key", "value")));

        // When
        Iterator<Map.Entry<String, String>> iterator = decorator.entrySet().iterator();

        // Then
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals(new HashMap.SimpleEntry<>("key", "value"), iterator.next());
        verify(map, times(1)).entrySet();
    }

    @Test
    public void testSetValue() {
        // Given
        AbstractMapEntryDecorator<String, String> entry = mock(AbstractMapEntryDecorator.class);
        when(entry.getMapEntry()).thenReturn(new HashMap.SimpleEntry<>("key", "value"));
        when(map.get(any())).thenReturn("oldValue");

        // When
        String newValue = decorator.checkSetValue("newValue");

        // Then
        assertEquals("newValue", newValue);
        verify(entry, never()).setValue(any());
    }

    @Test
    public void testSetValue_WithEntry() {
        // Given
        AbstractMapEntryDecorator<String, String> entry = mock(AbstractMapEntryDecorator.class);
        when(entry.getMapEntry()).thenReturn(new HashMap.SimpleEntry<>("key", "value"));
        when(map.get(any())).thenReturn("oldValue");

        // When
        String newValue = decorator.checkSetValue("newValue");

        // Then
        assertEquals("newValue", newValue);
        verify(entry, never()).setValue(any());
    }

    @Test
    public void testIsSetValueChecking() {
        // Given

        // When
        boolean result = decorator.isSetValueChecking();

        // Then
        assertTrue(result);
    }

    @Test
    public void testNext() {
        // Given
        AbstractIteratorDecorator<Map.Entry<String, String>> iterator = mock(AbstractIteratorDecorator.class);
        when(iterator.next()).thenReturn(new HashMap.SimpleEntry<>("key", "value"));

        // When
        Map.Entry<String, String> next = iterator.next();

        // Then
        assertNotNull(next);
        assertEquals(new HashMap.SimpleEntry<>("key", "value"), next);
        verify(iterator, times(1)).next();
    }
}
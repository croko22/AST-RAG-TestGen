import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.FactoryTransformer;
import org.apache.commons.collections4.map.DefaultedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultedMapTest {

    @Mock
    private Factory<String> factory;

    @Mock
    private Transformer<String, String> transformer;

    private Map<String, String> map;

    @BeforeEach
    void setup() {
        map = new HashMap<>();
        map.put("key1", "value1");
        when(factory.create()).thenReturn("default");
        when(transformer.transform(any())).thenReturn("transformed");
    }

    @Test
    void testDefaultedMap_Factory() {
        // Given
        DefaultedMap<String, String> defaultedMap = DefaultedMap.defaultedMap(map, factory);

        // When
        String result = defaultedMap.get("key2");

        // Then
        assertEquals("default", result);
        verify(factory, times(1)).create();
    }

    @Test
    void testDefaultedMap_Transformer() {
        // Given
        Map<String, String> defaultedMap = DefaultedMap.defaultedMap(map, transformer);

        // When
        String result = defaultedMap.get("key2");

        // Then
        assertEquals("transformed", result);
        verify(transformer, times(1)).transform("key2");
    }

    @Test
    void testDefaultedMap_DefaultValue() {
        // Given
        DefaultedMap<String, String> defaultedMap = DefaultedMap.defaultedMap(map, "default");

        // When
        String result = defaultedMap.get("key2");

        // Then
        assertEquals("default", result);
    }

    @Test
    void testGet_ExistingKey() {
        // Given
        DefaultedMap<String, String> defaultedMap = DefaultedMap.defaultedMap(map, factory);

        // When
        String result = defaultedMap.get("key1");

        // Then
        assertEquals("value1", result);
        verify(factory, never()).create();
    }

    @Test
    void testGet_NonExistingKey() {
        // Given
        DefaultedMap<String, String> defaultedMap = DefaultedMap.defaultedMap(map, factory);

        // When
        String result = defaultedMap.get("key2");

        // Then
        assertEquals("default", result);
        verify(factory, times(1)).create();
    }

    @Test
    void testDefaultedMap_NullFactory() {
        // Given
        assertThrows(NullPointerException.class, () -> DefaultedMap.defaultedMap(map, null));
    }

    @Test
    void testDefaultedMap_NullTransformer() {
        // Given
        assertThrows(NullPointerException.class, () -> DefaultedMap.defaultedMap(map, null));
    }

    @Test
    void testDefaultedMap_NullMap() {
        // Given
        assertThrows(NullPointerException.class, () -> DefaultedMap.defaultedMap(null, factory));
    }
}
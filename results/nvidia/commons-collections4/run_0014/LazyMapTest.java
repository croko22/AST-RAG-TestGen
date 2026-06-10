import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.FactoryTransformer;
import org.apache.commons.collections4.map.LazyMap;
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
public class LazyMapTest {

    @Mock
    private Factory<String> factory;

    @Mock
    private Transformer<String, String> transformer;

    private Map<String, String> map;

    @BeforeEach
    void setup() {
        map = new HashMap<>();
    }

    @Test
    void testLazyMapFactory() {
        // Given
        when(factory.create()).thenReturn("value");

        // When
        LazyMap<String, String> lazyMap = LazyMap.lazyMap(map, factory);

        // Then
        assertNotNull(lazyMap);
    }

    @Test
    void testLazyMapTransformer() {
        // Given
        when(transformer.transform(any())).thenReturn("value");

        // When
        LazyMap<String, String> lazyMap = LazyMap.lazyMap(map, transformer);

        // Then
        assertNotNull(lazyMap);
    }

    @Test
    void testGetExistingKey() {
        // Given
        map.put("key", "value");
        LazyMap<String, String> lazyMap = LazyMap.lazyMap(map, factory);

        // When
        String result = lazyMap.get("key");

        // Then
        assertEquals("value", result);
        verify(factory, never()).create();
    }

    @Test
    void testGetNonExistingKey() {
        // Given
        when(factory.create()).thenReturn("value");
        LazyMap<String, String> lazyMap = LazyMap.lazyMap(map, factory);

        // When
        String result = lazyMap.get("key");

        // Then
        assertEquals("value", result);
        verify(factory, times(1)).create();
    }

    @Test
    void testGetNonExistingKeyWithTransformer() {
        // Given
        when(transformer.transform(any())).thenReturn("value");
        LazyMap<String, String> lazyMap = LazyMap.lazyMap(map, transformer);

        // When
        String result = lazyMap.get("key");

        // Then
        assertEquals("value", result);
        verify(transformer, times(1)).transform("key");
    }

    @Test
    void testLazyMapFactoryNullMap() {
        // Given
        map = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> LazyMap.lazyMap(map, factory));
    }

    @Test
    void testLazyMapFactoryNullFactory() {
        // Given
        factory = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> LazyMap.lazyMap(map, factory));
    }

    @Test
    void testLazyMapTransformerNullMap() {
        // Given
        map = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> LazyMap.lazyMap(map, transformer));
    }

    @Test
    void testLazyMapTransformerNullTransformer() {
        // Given
        transformer = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> LazyMap.lazyMap(map, transformer));
    }
}
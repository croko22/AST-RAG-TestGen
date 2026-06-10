import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.map.TransformedMap;
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
public class TransformedMapTest {

    @Mock
    private Transformer<String, String> keyTransformer;

    @Mock
    private Transformer<String, String> valueTransformer;

    @Mock
    private Map<String, String> map;

    private TransformedMap<String, String> transformedMap;

    @BeforeEach
    void setup() {
        transformedMap = TransformedMap.transformingMap(map, keyTransformer, valueTransformer);
    }

    @Test
    void testTransformedMap() {
        // Given
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("key1", "value1");
        inputMap.put("key2", "value2");

        // When
        TransformedMap<String, String> result = TransformedMap.transformedMap(inputMap, keyTransformer, valueTransformer);

        // Then
        assertNotNull(result);
    }

    @Test
    void testTransformingMap() {
        // Given
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("key1", "value1");
        inputMap.put("key2", "value2");

        // When
        TransformedMap<String, String> result = TransformedMap.transformingMap(inputMap, keyTransformer, valueTransformer);

        // Then
        assertNotNull(result);
    }

    @Test
    void testPut() {
        // Given
        String key = "key";
        String value = "value";

        // When
        String result = transformedMap.put(key, value);

        // Then
        verify(keyTransformer, times(1)).apply(any());
        verify(valueTransformer, times(1)).apply(any());
    }

    @Test
    void testPutAll() {
        // Given
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("key1", "value1");
        inputMap.put("key2", "value2");

        // When
        transformedMap.putAll(inputMap);

        // Then
        verify(keyTransformer, times(2)).apply(any());
        verify(valueTransformer, times(2)).apply(any());
    }

    @Test
    void testTransformedMapNullMap() {
        // Given
        Map<String, String> inputMap = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> TransformedMap.transformedMap(inputMap, keyTransformer, valueTransformer));
    }

    @Test
    void testTransformingMapNullMap() {
        // Given
        Map<String, String> inputMap = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> TransformedMap.transformingMap(inputMap, keyTransformer, valueTransformer));
    }
}
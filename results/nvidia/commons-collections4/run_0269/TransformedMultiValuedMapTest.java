import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.multimap.TransformedMultiValuedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransformedMultiValuedMapTest {

    @Mock
    private MultiValuedMap<String, String> multiValuedMap;

    @Mock
    private Transformer<String, String> keyTransformer;

    @Mock
    private Transformer<String, String> valueTransformer;

    private TransformedMultiValuedMap<String, String> transformedMultiValuedMap;

    @BeforeEach
    void setup() {
        transformedMultiValuedMap = new TransformedMultiValuedMap<>(multiValuedMap, keyTransformer, valueTransformer);
    }

    @Test
    void testTransformedMap() {
        // Given
        MultiValuedMap<String, String> map = mock(MultiValuedMap.class);
        Transformer<String, String> keyTransformer = mock(Transformer.class);
        Transformer<String, String> valueTransformer = mock(Transformer.class);

        // When
        TransformedMultiValuedMap<String, String> transformedMap = TransformedMultiValuedMap.transformedMap(map, keyTransformer, valueTransformer);

        // Then
        assertNotNull(transformedMap);
    }

    @Test
    void testTransformingMap() {
        // Given
        MultiValuedMap<String, String> map = mock(MultiValuedMap.class);
        Transformer<String, String> keyTransformer = mock(Transformer.class);
        Transformer<String, String> valueTransformer = mock(Transformer.class);

        // When
        TransformedMultiValuedMap<String, String> transformingMap = TransformedMultiValuedMap.transformingMap(map, keyTransformer, valueTransformer);

        // Then
        assertNotNull(transformingMap);
    }

    @Test
    void testPut() {
        // Given
        String key = "key";
        String value = "value";

        // When
        boolean result = transformedMultiValuedMap.put(key, value);

        // Then
        verify(multiValuedMap).put(any(), any());
    }

    @Test
    void testPutAll_Iterable() {
        // Given
        String key = "key";
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");

        // When
        boolean result = transformedMultiValuedMap.putAll(key, values);

        // Then
        verify(multiValuedMap).putAll(any(), any());
    }

    @Test
    void testPutAll_Map() {
        // Given
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        // When
        boolean result = transformedMultiValuedMap.putAll(map);

        // Then
        verify(multiValuedMap, times(2)).put(any(), any());
    }

    @Test
    void testPutAll_MultiValuedMap() {
        // Given
        MultiValuedMap<String, String> multiValuedMap = mock(MultiValuedMap.class);

        // When
        boolean result = transformedMultiValuedMap.putAll(multiValuedMap);

        // Then
        verify(multiValuedMap, times(1)).entries();
    }
}
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.map.LinkedMap;
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
public class TransformedSplitMapTest {

    @Mock
    private Transformer<String, Integer> keyTransformer;

    @Mock
    private Transformer<Integer, String> valueTransformer;

    @Mock
    private Map<Integer, String> map;

    private TransformedSplitMap<String, Integer, Integer, String> transformedSplitMap;

    @BeforeEach
    void setup() {
        transformedSplitMap = new TransformedSplitMap<>(map, keyTransformer, valueTransformer);
    }

    @Test
    void testTransformingMap() {
        // Given
        Map<Integer, String> inputMap = new HashMap<>();
        inputMap.put(1, "one");
        inputMap.put(2, "two");

        // When
        TransformedSplitMap<String, Integer, Integer, String> result = TransformedSplitMap.transformingMap(inputMap, keyTransformer, valueTransformer);

        // Then
        assertNotNull(result);
    }

    @Test
    void testClear() {
        // When
        transformedSplitMap.clear();

        // Then
        verify(map, times(1)).clear();
    }

    @Test
    void testPut() {
        // Given
        String key = "key";
        Integer value = 10;

        when(keyTransformer.apply(any())).thenReturn(1);
        when(valueTransformer.apply(any())).thenReturn("ten");

        // When
        String result = transformedSplitMap.put(key, value);

        // Then
        verify(keyTransformer, times(1)).apply(key);
        verify(valueTransformer, times(1)).apply(value);
        verify(map, times(1)).put(1, "ten");
    }

    @Test
    void testPutAll() {
        // Given
        Map<String, Integer> inputMap = new HashMap<>();
        inputMap.put("one", 1);
        inputMap.put("two", 2);

        when(keyTransformer.apply(any())).thenReturn(1);
        when(valueTransformer.apply(any())).thenReturn("one");

        // When
        transformedSplitMap.putAll(inputMap);

        // Then
        verify(keyTransformer, times(2)).apply(any());
        verify(valueTransformer, times(2)).apply(any());
        verify(map, times(1)).putAll(any());
    }
}
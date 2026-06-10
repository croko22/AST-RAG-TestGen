import org.apache.commons.collections4.functors.MapTransformer;
import org.apache.commons.collections4.Transformer;
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
public class MapTransformerTest {

    @Mock
    private Map<String, String> mockMap;

    private MapTransformer<String, String> mapTransformer;

    @BeforeEach
    public void setup() {
        mapTransformer = new MapTransformer<>(mockMap);
    }

    @Test
    public void testMapTransformer_NullMap() {
        // Given: null map
        Map<String, String> nullMap = null;

        // When: create map transformer with null map
        Transformer<String, String> transformer = MapTransformer.mapTransformer(nullMap);

        // Then: verify transformer returns null for any input
        assertNull(transformer.transform("anyInput"));
    }

    @Test
    public void testMapTransformer_NonNullMap() {
        // Given: non-null map
        Map<String, String> nonNullMap = new HashMap<>();
        nonNullMap.put("key", "value");

        // When: create map transformer with non-null map
        Transformer<String, String> transformer = MapTransformer.mapTransformer(nonNullMap);

        // Then: verify transformer returns value from map
        assertEquals("value", transformer.transform("key"));
    }

    @Test
    public void testGetMap() {
        // Given: map transformer with mock map
        MapTransformer<String, String> mapTransformer = new MapTransformer<>(mockMap);

        // When: get map from map transformer
        Map<? super String, ? extends String> map = mapTransformer.getMap();

        // Then: verify returned map is the same as the mock map
        assertSame(mockMap, map);
    }

    @Test
    public void testTransform_KeyFoundInMap() {
        // Given: map transformer with mock map and key found in map
        when(mockMap.get(any())).thenReturn("value");

        // When: transform input using map transformer
        String result = mapTransformer.transform("key");

        // Then: verify result is the value from the map
        assertEquals("value", result);
    }

    @Test
    public void testTransform_KeyNotFoundInMap() {
        // Given: map transformer with mock map and key not found in map
        when(mockMap.get(any())).thenReturn(null);

        // When: transform input using map transformer
        String result = mapTransformer.transform("key");

        // Then: verify result is null
        assertNull(result);
    }
}
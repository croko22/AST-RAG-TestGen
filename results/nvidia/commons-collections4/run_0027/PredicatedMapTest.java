import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PredicatedMapTest {

    @Mock
    private Map<String, String> map;

    @InjectMocks
    private PredicatedMap<String, String> predicatedMap;

    @BeforeEach
    void setup() {
        predicatedMap = new PredicatedMap<>(new HashMap<>(), TruePredicate.truePredicate(), TruePredicate.truePredicate());
    }

    @Test
    public void testPredicatedMap() {
        // Given
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("key1", "value1");
        inputMap.put("key2", "value2");

        // When
        PredicatedMap<String, String> result = PredicatedMap.predicatedMap(inputMap, TruePredicate.truePredicate(), TruePredicate.truePredicate());

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testPut() {
        // Given
        String key = "key";
        String value = "value";

        // When
        String result = predicatedMap.put(key, value);

        // Then
        assertNotNull(result);
        assertEquals(value, result);
        verify(map, times(1)).put(key, value);
    }

    @Test
    public void testPutAll() {
        // Given
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("key1", "value1");
        inputMap.put("key2", "value2");

        // When
        predicatedMap.putAll(inputMap);

        // Then
        verify(map, times(1)).putAll(inputMap);
    }

    @Test
    public void testPut_InvalidKey() {
        // Given
        Predicate<String> keyPredicate = new Predicate<String>() {
            @Override
            public boolean evaluate(String object) {
                return false;
            }

            @Override
            public boolean test(String t) {
                return false;
            }
        };
        predicatedMap = new PredicatedMap<>(new HashMap<>(), keyPredicate, TruePredicate.truePredicate());
        String key = "key";
        String value = "value";

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> predicatedMap.put(key, value));
    }

    @Test
    public void testPut_InvalidValue() {
        // Given
        Predicate<String> valuePredicate = new Predicate<String>() {
            @Override
            public boolean evaluate(String object) {
                return false;
            }

            @Override
            public boolean test(String t) {
                return false;
            }
        };
        predicatedMap = new PredicatedMap<>(new HashMap<>(), TruePredicate.truePredicate(), valuePredicate);
        String key = "key";
        String value = "value";

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> predicatedMap.put(key, value));
    }

    @Test
    public void testPutAll_InvalidKey() {
        // Given
        Predicate<String> keyPredicate = new Predicate<String>() {
            @Override
            public boolean evaluate(String object) {
                return false;
            }

            @Override
            public boolean test(String t) {
                return false;
            }
        };
        predicatedMap = new PredicatedMap<>(new HashMap<>(), keyPredicate, TruePredicate.truePredicate());
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("key1", "value1");
        inputMap.put("key2", "value2");

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> predicatedMap.putAll(inputMap));
    }

    @Test
    public void testPutAll_InvalidValue() {
        // Given
        Predicate<String> valuePredicate = new Predicate<String>() {
            @Override
            public boolean evaluate(String object) {
                return false;
            }

            @Override
            public boolean test(String t) {
                return false;
            }
        };
        predicatedMap = new PredicatedMap<>(new HashMap<>(), TruePredicate.truePredicate(), valuePredicate);
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("key1", "value1");
        inputMap.put("key2", "value2");

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> predicatedMap.putAll(inputMap));
    }
}
import org.apache.commons.collections4.OrderedBidiMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderedBidiMapTest {

    @Mock
    private OrderedBidiMap<String, Integer> orderedBidiMap;

    @BeforeEach
    void setup() {
        // Initialize the mock object if necessary
    }

    @Test
    public void testInverseBidiMap() {
        // Given: an ordered bidirectional map
        // When: the inverse bidirectional map is requested
        OrderedBidiMap<Integer, String> inverseMap = orderedBidiMap.inverseBidiMap();

        // Then: the inverse map should not be null
        assertNotNull(inverseMap);

        // Verify that the inverse map is an instance of OrderedBidiMap
        assertTrue(inverseMap instanceof OrderedBidiMap);
    }

    @Test
    public void testInverseBidiMap_Consistency() {
        // Given: an ordered bidirectional map
        // When: the inverse bidirectional map is requested twice
        OrderedBidiMap<Integer, String> inverseMap1 = orderedBidiMap.inverseBidiMap();
        OrderedBidiMap<Integer, String> inverseMap2 = orderedBidiMap.inverseBidiMap();

        // Then: both inverse maps should be the same instance
        assertSame(inverseMap1, inverseMap2);
    }

    @Test
    public void testInverseBidiMap_InverseOfInverse() {
        // Given: an ordered bidirectional map
        // When: the inverse bidirectional map is requested, and then its inverse is requested
        OrderedBidiMap<Integer, String> inverseMap = orderedBidiMap.inverseBidiMap();
        OrderedBidiMap<String, Integer> inverseOfInverse = inverseMap.inverseBidiMap();

        // Then: the inverse of the inverse should be the original map
        assertSame(orderedBidiMap, inverseOfInverse);
    }
}
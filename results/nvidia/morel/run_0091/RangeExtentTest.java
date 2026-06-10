import net.hydromatic.morel.type.RangeExtent;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RangeExtentTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private Type type;

    private RangeExtent rangeExtent;

    @BeforeEach
    void setup() {
        Map<String, ImmutableRangeSet> rangeSetMap = new HashMap<>();
        rangeExtent = new RangeExtent(typeSystem, type, rangeSetMap);
    }

    @Test
    void testToString_Unbounded() {
        // Given
        when(type.toString()).thenReturn("Type");

        // When
        String result = rangeExtent.toString();

        // Then
        assertEquals("Type", result);
    }

    @Test
    void testToString_Bounded() {
        // Given
        Map<String, ImmutableRangeSet> rangeSetMap = new HashMap<>();
        rangeSetMap.put("path", ImmutableRangeSet.of(Range.range(1, 10)));
        rangeExtent = new RangeExtent(typeSystem, type, rangeSetMap);
        when(type.toString()).thenReturn("Type");

        // When
        String result = rangeExtent.toString();

        // Then
        assertEquals("Type {path=[1..10]}", result);
    }

    @Test
    void testIsUnbounded_EmptyRangeSet() {
        // Given
        Map<String, ImmutableRangeSet> rangeSetMap = new HashMap<>();

        // When
        boolean result = rangeExtent.isUnbounded();

        // Then
        assertTrue(result);
    }

    @Test
    void testIsUnbounded_NonEmptyRangeSet() {
        // Given
        Map<String, ImmutableRangeSet> rangeSetMap = new HashMap<>();
        rangeSetMap.put("path", ImmutableRangeSet.of(Range.range(1, 10)));
        rangeExtent = new RangeExtent(typeSystem, type, rangeSetMap);

        // When
        boolean result = rangeExtent.isUnbounded();

        // Then
        assertFalse(result);
    }
}
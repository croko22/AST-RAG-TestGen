import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.Range;
import net.hydromatic.morel.ast.Core;
import net.hydromatic.morel.compile.Extents;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ExtentsTest {

    @Mock
    private Core.Exp exp;

    @BeforeEach
    void setup() {
        // Initialize mock objects if needed
    }

    @Test
    void testIsInfinite_Extent() {
        // Given: exp is an extent with iterable == null
        when(exp.isExtent()).thenReturn(true);
        when(exp.getRangeExtent()).thenReturn(null);

        // When: isInfinite is called
        boolean result = Extents.isInfinite(exp);

        // Then: result is true
        assertTrue(result);
    }

    @Test
    void testIsInfinite_NotAnExtent() {
        // Given: exp is not an extent
        when(exp.isExtent()).thenReturn(false);

        // When: isInfinite is called
        boolean result = Extents.isInfinite(exp);

        // Then: result is false
        assertFalse(result);
    }

    @Test
    void testIntersect_NoRangeSetMaps() {
        // Given: an empty list of range set maps
        List<Map<String, ImmutableRangeSet<Integer>>> rangeSetMaps = new ArrayList<>();

        // When: intersect is called
        Map<String, ImmutableRangeSet<Integer>> result = Extents.intersect(rangeSetMaps);

        // Then: result is an empty map
        assertTrue(result.isEmpty());
    }

    @Test
    void testIntersect_OneRangeSetMap() {
        // Given: a list with one range set map
        List<Map<String, ImmutableRangeSet<Integer>>> rangeSetMaps = new ArrayList<>();
        Map<String, ImmutableRangeSet<Integer>> rangeSetMap = ImmutableMap.of("path", ImmutableRangeSet.of(Range.closed(1, 10)));
        rangeSetMaps.add(rangeSetMap);

        // When: intersect is called
        Map<String, ImmutableRangeSet<Integer>> result = Extents.intersect(rangeSetMaps);

        // Then: result is the same as the input range set map
        assertEquals(rangeSetMap, result);
    }

    @Test
    void testIntersect_MultipleRangeSetMaps() {
        // Given: a list with multiple range set maps
        List<Map<String, ImmutableRangeSet<Integer>>> rangeSetMaps = new ArrayList<>();
        Map<String, ImmutableRangeSet<Integer>> rangeSetMap1 = ImmutableMap.of("path1", ImmutableRangeSet.of(Range.closed(1, 10)));
        Map<String, ImmutableRangeSet<Integer>> rangeSetMap2 = ImmutableMap.of("path2", ImmutableRangeSet.of(Range.closed(5, 15)));
        rangeSetMaps.add(rangeSetMap1);
        rangeSetMaps.add(rangeSetMap2);

        // When: intersect is called
        Map<String, ImmutableRangeSet<Integer>> result = Extents.intersect(rangeSetMaps);

        // Then: result is the intersection of the input range set maps
        Map<String, ImmutableRangeSet<Integer>> expected = ImmutableMap.of(
                "path1", ImmutableRangeSet.of(Range.closed(1, 10)),
                "path2", ImmutableRangeSet.of(Range.closed(5, 15))
        );
        assertEquals(expected, result);
    }

    @Test
    void testUnion_NoRangeSetMaps() {
        // Given: an empty list of range set maps
        List<Map<String, ImmutableRangeSet<Integer>>> rangeSetMaps = new ArrayList<>();

        // When: union is called
        Map<String, ImmutableRangeSet<Integer>> result = Extents.union(rangeSetMaps);

        // Then: result is a map with a single entry for path "/" and an empty range set
        Map<String, ImmutableRangeSet<Integer>> expected = ImmutableMap.of("/", ImmutableRangeSet.of());
        assertEquals(expected, result);
    }

    @Test
    void testUnion_OneRangeSetMap() {
        // Given: a list with one range set map
        List<Map<String, ImmutableRangeSet<Integer>>> rangeSetMaps = new ArrayList<>();
        Map<String, ImmutableRangeSet<Integer>> rangeSetMap = ImmutableMap.of("path", ImmutableRangeSet.of(Range.closed(1, 10)));
        rangeSetMaps.add(rangeSetMap);

        // When: union is called
        Map<String, ImmutableRangeSet<Integer>> result = Extents.union(rangeSetMaps);

        // Then: result is the same as the input range set map
        assertEquals(rangeSetMap, result);
    }

    @Test
    void testUnion_MultipleRangeSetMaps() {
        // Given: a list with multiple range set maps
        List<Map<String, ImmutableRangeSet<Integer>>> rangeSetMaps = new ArrayList<>();
        Map<String, ImmutableRangeSet<Integer>> rangeSetMap1 = ImmutableMap.of("path1", ImmutableRangeSet.of(Range.closed(1, 10)));
        Map<String, ImmutableRangeSet<Integer>> rangeSetMap2 = ImmutableMap.of("path2", ImmutableRangeSet.of(Range.closed(5, 15)));
        rangeSetMaps.add(rangeSetMap1);
        rangeSetMaps.add(rangeSetMap2);

        // When: union is called
        Map<String, ImmutableRangeSet<Integer>> result = Extents.union(rangeSetMaps);

        // Then: result is the union of the input range set maps
        Map<String, ImmutableRangeSet<Integer>> expected = ImmutableMap.of(
                "path1", ImmutableRangeSet.of(Range.closed(1, 10)),
                "path2", ImmutableRangeSet.of(Range.closed(5, 15))
        );
        assertEquals(expected, result);
    }
}
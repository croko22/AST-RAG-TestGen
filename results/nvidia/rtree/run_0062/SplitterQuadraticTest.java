import com.github.davidmoten.rtree.SplitterQuadratic;
import com.github.davidmoten.rtree.geometry.HasGeometry;
import com.github.davidmoten.rtree.geometry.ListPair;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.internal.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SplitterQuadraticTest {

    @Mock
    private HasGeometry geometry1;

    @Mock
    private HasGeometry geometry2;

    @Mock
    private Rectangle rectangle1;

    @Mock
    private Rectangle rectangle2;

    private SplitterQuadratic splitterQuadratic;

    @BeforeEach
    void setup() {
        splitterQuadratic = new SplitterQuadratic();
    }

    @Test
    void testSplit_TwoItems_MinSizeOne() {
        // Given
        List<HasGeometry> items = new ArrayList<>();
        items.add(geometry1);
        items.add(geometry2);
        int minSize = 1;

        // When
        ListPair<HasGeometry> result = splitterQuadratic.split(items, minSize);

        // Then
        assertNotNull(result);
        assertNotNull(result.group1());
        assertNotNull(result.group2());
        assertEquals(1, result.group1().size());
        assertEquals(1, result.group2().size());
    }

    @Test
    void testSplit_ThreeItems_MinSizeOne() {
        // Given
        List<HasGeometry> items = new ArrayList<>();
        HasGeometry geometry3 = mock(HasGeometry.class);
        items.add(geometry1);
        items.add(geometry2);
        items.add(geometry3);
        int minSize = 1;

        // When
        ListPair<HasGeometry> result = splitterQuadratic.split(items, minSize);

        // Then
        assertNotNull(result);
        assertNotNull(result.group1());
        assertNotNull(result.group2());
        assertTrue(result.group1().size() > 0);
        assertTrue(result.group2().size() > 0);
    }

    @Test
    void testSplit_FourItems_MinSizeTwo() {
        // Given
        List<HasGeometry> items = new ArrayList<>();
        HasGeometry geometry3 = mock(HasGeometry.class);
        HasGeometry geometry4 = mock(HasGeometry.class);
        items.add(geometry1);
        items.add(geometry2);
        items.add(geometry3);
        items.add(geometry4);
        int minSize = 2;

        // When
        ListPair<HasGeometry> result = splitterQuadratic.split(items, minSize);

        // Then
        assertNotNull(result);
        assertNotNull(result.group1());
        assertNotNull(result.group2());
        assertEquals(2, result.group1().size());
        assertEquals(2, result.group2().size());
    }

    @Test
    void testSplit_LessThanTwoItems_ThrowsException() {
        // Given
        List<HasGeometry> items = new ArrayList<>();
        items.add(geometry1);
        int minSize = 1;

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> splitterQuadratic.split(items, minSize));
    }

    @Test
    void testWorstCombination_TwoItems() {
        // Given
        List<HasGeometry> items = new ArrayList<>();
        items.add(geometry1);
        items.add(geometry2);

        // When
        Pair<HasGeometry> result = SplitterQuadratic.worstCombination(items);

        // Then
        assertNotNull(result);
        assertNotNull(result.value1());
        assertNotNull(result.value2());
        assertEquals(geometry1, result.value1());
        assertEquals(geometry2, result.value2());
    }

    @Test
    void testGetBestCandidateForGroup_OneItem() {
        // Given
        List<HasGeometry> list = new ArrayList<>();
        list.add(geometry1);
        List<HasGeometry> group = new ArrayList<>();
        Rectangle groupMbr = mock(Rectangle.class);

        // When
        HasGeometry result = SplitterQuadratic.getBestCandidateForGroup(list, group, groupMbr);

        // Then
        assertNotNull(result);
        assertEquals(geometry1, result);
    }

    @Test
    void testAssignRemaining_TwoGroups_OneItem() {
        // Given
        List<HasGeometry> group1 = new ArrayList<>();
        group1.add(geometry1);
        List<HasGeometry> group2 = new ArrayList<>();
        group2.add(geometry2);
        List<HasGeometry> remaining = new ArrayList<>();
        HasGeometry geometry3 = mock(HasGeometry.class);
        remaining.add(geometry3);
        int minGroupSize = 1;

        // When
        SplitterQuadratic.assignRemaining(group1, group2, remaining, minGroupSize);

        // Then
        assertNotNull(group1);
        assertNotNull(group2);
        assertNotNull(remaining);
        assertTrue(group1.size() > 0);
        assertTrue(group2.size() > 0);
        assertEquals(0, remaining.size());
    }
}
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.HasGeometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ComparatorsTest {

    @Mock
    private Rectangle rectangle;

    @Mock
    private HasGeometry hasGeometry1;

    @Mock
    private HasGeometry hasGeometry2;

    @Mock
    private Geometry geometry1;

    @Mock
    private Geometry geometry2;

    @Mock
    private Entry<String, Geometry> entry1;

    @Mock
    private Entry<String, Geometry> entry2;

    private List<HasGeometry> list;

    @BeforeEach
    void setup() {
        list = new ArrayList<>();
        list.add(hasGeometry1);
        list.add(hasGeometry2);

        when(hasGeometry1.geometry()).thenReturn(geometry1);
        when(hasGeometry2.geometry()).thenReturn(geometry2);
        when(entry1.geometry()).thenReturn(geometry1);
        when(entry2.geometry()).thenReturn(geometry2);
    }

    @Test
    void testOverlapAreaThenAreaIncreaseThenAreaComparator() {
        // Given
        Comparator<HasGeometry> comparator = Comparators.overlapAreaThenAreaIncreaseThenAreaComparator(rectangle, list);

        // When
        when(geometry1.mbr()).thenReturn(rectangle);
        when(geometry2.mbr()).thenReturn(rectangle);
        when(rectangle.area()).thenReturn(10.0);
        when(rectangle.add(any(Rectangle.class))).thenReturn(rectangle);
        when(rectangle.intersectionArea(any(Rectangle.class))).thenReturn(5.0);

        int result = comparator.compare(hasGeometry1, hasGeometry2);

        // Then
        assertNotNull(comparator);
        assertEquals(0, result);
    }

    @Test
    void testAreaIncreaseThenAreaComparator() {
        // Given
        Comparator<HasGeometry> comparator = Comparators.areaIncreaseThenAreaComparator(rectangle);

        // When
        when(geometry1.mbr()).thenReturn(rectangle);
        when(geometry2.mbr()).thenReturn(rectangle);
        when(rectangle.area()).thenReturn(10.0);
        when(rectangle.add(any(Rectangle.class))).thenReturn(rectangle);

        int result = comparator.compare(hasGeometry1, hasGeometry2);

        // Then
        assertNotNull(comparator);
        assertEquals(0, result);
    }

    @Test
    void testAscendingDistance() {
        // Given
        Comparator<Entry<String, Geometry>> comparator = Comparators.ascendingDistance(rectangle);

        // When
        when(geometry1.distance(rectangle)).thenReturn(10.0);
        when(geometry2.distance(rectangle)).thenReturn(5.0);

        int result = comparator.compare(entry1, entry2);

        // Then
        assertNotNull(comparator);
        assertEquals(1, result);
    }

    @Test
    void testAscendingDistance_EqualDistance() {
        // Given
        Comparator<Entry<String, Geometry>> comparator = Comparators.ascendingDistance(rectangle);

        // When
        when(geometry1.distance(rectangle)).thenReturn(10.0);
        when(geometry2.distance(rectangle)).thenReturn(10.0);

        int result = comparator.compare(entry1, entry2);

        // Then
        assertNotNull(comparator);
        assertEquals(0, result);
    }

    @Test
    void testAscendingDistance_Reversed() {
        // Given
        Comparator<Entry<String, Geometry>> comparator = Comparators.ascendingDistance(rectangle);

        // When
        when(geometry1.distance(rectangle)).thenReturn(5.0);
        when(geometry2.distance(rectangle)).thenReturn(10.0);

        int result = comparator.compare(entry1, entry2);

        // Then
        assertNotNull(comparator);
        assertEquals(-1, result);
    }
}
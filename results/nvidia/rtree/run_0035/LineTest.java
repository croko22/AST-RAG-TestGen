import com.github.davidmoten.rtree.geometry.Circle;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Line;
import com.github.davidmoten.rtree.geometry.Point;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LineTest {

    @Mock
    private Line line;

    @Mock
    private Line otherLine;

    @Mock
    private Point point;

    @Mock
    private Circle circle;

    @BeforeEach
    void setup() {
        // Setup mocks
        when(line.x1()).thenReturn(0.0);
        when(line.y1()).thenReturn(0.0);
        when(line.x2()).thenReturn(1.0);
        when(line.y2()).thenReturn(1.0);
    }

    @Test
    public void testX1() {
        // Given: line with predefined coordinates
        // When: get x1
        double x1 = line.x1();
        // Then: verify result
        assertEquals(0.0, x1);
    }

    @Test
    public void testY1() {
        // Given: line with predefined coordinates
        // When: get y1
        double y1 = line.y1();
        // Then: verify result
        assertEquals(0.0, y1);
    }

    @Test
    public void testX2() {
        // Given: line with predefined coordinates
        // When: get x2
        double x2 = line.x2();
        // Then: verify result
        assertEquals(1.0, x2);
    }

    @Test
    public void testY2() {
        // Given: line with predefined coordinates
        // When: get y2
        double y2 = line.y2();
        // Then: verify result
        assertEquals(1.0, y2);
    }

    @Test
    public void testIntersects_Line_Intersects() {
        // Given: two lines that intersect
        when(otherLine.x1()).thenReturn(0.5);
        when(otherLine.y1()).thenReturn(0.5);
        when(otherLine.x2()).thenReturn(1.5);
        when(otherLine.y2()).thenReturn(1.5);
        // When: check intersection
        boolean intersects = line.intersects(otherLine);
        // Then: verify result
        assertTrue(intersects);
    }

    @Test
    public void testIntersects_Line_DoesNotIntersect() {
        // Given: two lines that do not intersect
        when(otherLine.x1()).thenReturn(2.0);
        when(otherLine.y1()).thenReturn(2.0);
        when(otherLine.x2()).thenReturn(3.0);
        when(otherLine.y2()).thenReturn(3.0);
        // When: check intersection
        boolean intersects = line.intersects(otherLine);
        // Then: verify result
        assertFalse(intersects);
    }

    @Test
    public void testIntersects_Point_OnLine() {
        // Given: point on the line
        when(point.x()).thenReturn(0.5);
        when(point.y()).thenReturn(0.5);
        // When: check intersection
        boolean intersects = line.intersects(point);
        // Then: verify result
        assertTrue(intersects);
    }

    @Test
    public void testIntersects_Point_NotOnLine() {
        // Given: point not on the line
        when(point.x()).thenReturn(2.0);
        when(point.y()).thenReturn(2.0);
        // When: check intersection
        boolean intersects = line.intersects(point);
        // Then: verify result
        assertFalse(intersects);
    }

    @Test
    public void testIntersects_Circle_Intersects() {
        // Given: circle that intersects the line
        when(circle.x()).thenReturn(0.5);
        when(circle.y()).thenReturn(0.5);
        when(circle.radius()).thenReturn(1.0);
        // When: check intersection
        boolean intersects = line.intersects(circle);
        // Then: verify result
        assertTrue(intersects);
    }

    @Test
    public void testIntersects_Circle_DoesNotIntersect() {
        // Given: circle that does not intersect the line
        when(circle.x()).thenReturn(2.0);
        when(circle.y()).thenReturn(2.0);
        when(circle.radius()).thenReturn(0.1);
        // When: check intersection
        boolean intersects = line.intersects(circle);
        // Then: verify result
        assertFalse(intersects);
    }
}
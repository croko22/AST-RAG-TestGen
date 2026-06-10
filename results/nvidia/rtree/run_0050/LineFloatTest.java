import com.github.davidmoten.rtree.geometry.Circle;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Line;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.geometry.internal.LineFloat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LineFloatTest {

    private LineFloat line;

    @BeforeEach
    public void setup() {
        line = LineFloat.create(0, 0, 1, 1);
    }

    @Test
    public void testCreate() {
        LineFloat line = LineFloat.create(0, 0, 1, 1);
        assertNotNull(line);
        assertEquals(0, line.x1(), 1e-9);
        assertEquals(0, line.y1(), 1e-9);
        assertEquals(1, line.x2(), 1e-9);
        assertEquals(1, line.y2(), 1e-9);
    }

    @Test
    public void testDistance_RectangleContainsLine() {
        Rectangle rectangle = Geometries.rectangle(0, 0, 1, 1);
        assertEquals(0, line.distance(rectangle), 1e-9);
    }

    @Test
    public void testDistance_RectangleDoesNotContainLine() {
        Rectangle rectangle = Geometries.rectangle(2, 2, 3, 3);
        assertNotEquals(0, line.distance(rectangle), 1e-9);
    }

    @Test
    public void testMbr() {
        Rectangle mbr = line.mbr();
        assertNotNull(mbr);
        assertEquals(0, mbr.x1(), 1e-9);
        assertEquals(0, mbr.y1(), 1e-9);
        assertEquals(1, mbr.x2(), 1e-9);
        assertEquals(1, mbr.y2(), 1e-9);
    }

    @Test
    public void testIntersects_Rectangle() {
        Rectangle rectangle = Geometries.rectangle(0, 0, 1, 1);
        assertTrue(line.intersects(rectangle));
    }

    @Test
    public void testIntersects_RectangleNot() {
        Rectangle rectangle = Geometries.rectangle(2, 2, 3, 3);
        assertFalse(line.intersects(rectangle));
    }

    @Test
    public void testX1() {
        assertEquals(0, line.x1(), 1e-9);
    }

    @Test
    public void testY1() {
        assertEquals(0, line.y1(), 1e-9);
    }

    @Test
    public void testX2() {
        assertEquals(1, line.x2(), 1e-9);
    }

    @Test
    public void testY2() {
        assertEquals(1, line.y2(), 1e-9);
    }

    @Test
    public void testIntersects_Line() {
        Line line2 = Geometries.line(0, 0, 1, 1);
        assertTrue(line.intersects(line2));
    }

    @Test
    public void testIntersects_LineNot() {
        Line line2 = Geometries.line(2, 2, 3, 3);
        assertFalse(line.intersects(line2));
    }

    @Test
    public void testIntersects_Point() {
        Point point = Geometries.point(0, 0);
        assertTrue(line.intersects(point));
    }

    @Test
    public void testIntersects_PointNot() {
        Point point = Geometries.point(2, 2);
        assertFalse(line.intersects(point));
    }

    @Test
    public void testIntersects_Circle() {
        Circle circle = Geometries.circle(0, 0, 1);
        assertTrue(line.intersects(circle));
    }

    @Test
    public void testIntersects_CircleNot() {
        Circle circle = Geometries.circle(2, 2, 1);
        assertFalse(line.intersects(circle));
    }

    @Test
    public void testHashCode() {
        int hashCode = line.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testEquals() {
        LineFloat line2 = LineFloat.create(0, 0, 1, 1);
        assertTrue(line.equals(line2));
    }

    @Test
    public void testEqualsNot() {
        LineFloat line2 = LineFloat.create(2, 2, 3, 3);
        assertFalse(line.equals(line2));
    }

    @Test
    public void testIsDoublePrecision() {
        assertFalse(line.isDoublePrecision());
    }
}
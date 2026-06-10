import com.github.davidmoten.rtree.geometry.Circle;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Line;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.geometry.internal.LineDouble;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LineDoubleTest {

    @Test
    public void testCreate() {
        // Given
        double x1 = 1.0;
        double y1 = 2.0;
        double x2 = 3.0;
        double y2 = 4.0;

        // When
        LineDouble line = LineDouble.create(x1, y1, x2, y2);

        // Then
        assertNotNull(line);
        assertEquals(x1, line.x1());
        assertEquals(y1, line.y1());
        assertEquals(x2, line.x2());
        assertEquals(y2, line.y2());
    }

    @Test
    public void testDistance_RectangleContainsPoint() {
        // Given
        double x1 = 1.0;
        double y1 = 2.0;
        double x2 = 3.0;
        double y2 = 4.0;
        LineDouble line = LineDouble.create(x1, y1, x2, y2);
        Rectangle rectangle = Geometries.rectangle(0.0, 0.0, 5.0, 5.0);

        // When
        double distance = line.distance(rectangle);

        // Then
        assertEquals(0.0, distance, 1e-9);
    }

    @Test
    public void testDistance_RectangleDoesNotContainPoint() {
        // Given
        double x1 = 1.0;
        double y1 = 2.0;
        double x2 = 3.0;
        double y2 = 4.0;
        LineDouble line = LineDouble.create(x1, y1, x2, y2);
        Rectangle rectangle = Geometries.rectangle(10.0, 10.0, 15.0, 15.0);

        // When
        double distance = line.distance(rectangle);

        // Then
        assertNotEquals(0.0, distance, 1e-9);
    }

    @Test
    public void testMbr() {
        // Given
        double x1 = 1.0;
        double y1 = 2.0;
        double x2 = 3.0;
        double y2 = 4.0;
        LineDouble line = LineDouble.create(x1, y1, x2, y2);

        // When
        Rectangle mbr = line.mbr();

        // Then
        assertNotNull(mbr);
        assertEquals(Math.min(x1, x2), mbr.x1());
        assertEquals(Math.min(y1, y2), mbr.y1());
        assertEquals(Math.max(x1, x2), mbr.x2());
        assertEquals(Math.max(y1, y2), mbr.y2());
    }

    @Test
    public void testIntersects_Rectangle() {
        // Given
        double x1 = 1.0;
        double y1 = 2.0;
        double x2 = 3.0;
        double y2 = 4.0;
        LineDouble line = LineDouble.create(x1, y1, x2, y2);
        Rectangle rectangle = Geometries.rectangle(0.0, 0.0, 5.0, 5.0);

        // When
        boolean intersects = line.intersects(rectangle);

        // Then
        assertTrue(intersects);
    }

    @Test
    public void testIntersects_RectangleDoesNotIntersect() {
        // Given
        double x1 = 1.0;
        double y1 = 2.0;
        double x2 = 3.0;
        double y2 = 4.0;
        LineDouble line = LineDouble.create(x1, y1, x2, y2);
        Rectangle rectangle = Geometries.rectangle(10.0, 10.0, 15.0, 15.0);

        // When
        boolean intersects = line.intersects(rectangle);

        // Then
        assertFalse(intersects);
    }

    @Test
    public void testIntersects_Line() {
        // Given
        double x1 = 1.0;
        double y1 = 2.0;
        double x2 = 3.0;
        double y2 = 4.0;
        LineDouble line = LineDouble.create(x1, y1, x2, y2);
        Line otherLine = LineDouble.create(2.0, 3.0, 4.0, 5.0);

        // When
        boolean intersects = line.intersects(otherLine);

        // Then
        assertTrue(intersects);
    }

    @Test
    public void testIntersects_LineDoesNotIntersect() {
        // Given
        double x1 = 1.0;
        double y1 = 2.0;
        double x2 = 3.0;
        double y2 = 4.0;
        LineDouble line = LineDouble.create(x1, y1, x2, y2);
        Line otherLine = LineDouble.create(10.0, 10.0, 15.0, 15.0);

        // When
        boolean intersects = line.intersects(otherLine);

        // Then
        assertFalse(intersects);
    }

    @Test
    public void testIntersects_Point() {
        // Given
        double x1 = 1.0;
        double y1 = 2.0;
        double x2 = 3.0;
        double y2 = 4.0;
        LineDouble line = LineDouble.create(x1, y1, x2, y2);
        Point point = Geometries.point(2.0, 3.0);

        // When
        boolean intersects = line.intersects(point);

        // Then
        assertTrue(intersects);
    }

    @Test
    public void testIntersects_PointDoesNotIntersect() {
        // Given
        double x1 = 1.0;
        double y1 = 2.0;
        double x2 = 3.0;
        double y2 = 4.0;
        LineDouble line = LineDouble.create(x1, y1, x2, y2);
        Point point = Geometries.point(10.0, 10.0);

        // When
        boolean intersects = line.intersects(point);

        // Then
        assertFalse(intersects);
    }

    @Test
    public void testIntersects_Circle() {
        // Given
        double x1 = 1.0;
        double y1 = 2.0;
        double x2 = 3.0;
        double y2 = 4.0;
        LineDouble line = LineDouble.create(x1, y1, x2, y2);
        Circle circle = Geometries.circle(2.0, 3.0, 1.0);

        // When
        boolean intersects = line.intersects(circle);

        // Then
        assertTrue(intersects);
    }

    @Test
    public void testIntersects_CircleDoesNotIntersect() {
        // Given
        double x1 = 1.0;
        double y1 = 2.0;
        double x2 = 3.0;
        double y2 = 4.0;
        LineDouble line = LineDouble.create(x1, y1, x2, y2);
        Circle circle = Geometries.circle(10.0, 10.0, 1.0);

        // When
        boolean intersects = line.intersects(circle);

        // Then
        assertFalse(intersects);
    }

    @Test
    public void testHashCode() {
        // Given
        double x1 = 1.0;
        double y1 = 2.0;
        double x2 = 3.0;
        double y2 = 4.0;
        LineDouble line = LineDouble.create(x1, y1, x2, y2);

        // When
        int hashCode = line.hashCode();

        // Then
        assertNotNull(hashCode);
    }

    @Test
    public void testEquals() {
        // Given
        double x1 = 1.0;
        double y1 = 2.0;
        double x2 = 3.0;
        double y2 = 4.0;
        LineDouble line = LineDouble.create(x1, y1, x2, y2);
        LineDouble otherLine = LineDouble.create(x1, y1, x2, y2);

        // When
        boolean equals = line.equals(otherLine);

        // Then
        assertTrue(equals);
    }

    @Test
    public void testEquals_DifferentLines() {
        // Given
        double x1 = 1.0;
        double y1 = 2.0;
        double x2 = 3.0;
        double y2 = 4.0;
        LineDouble line = LineDouble.create(x1, y1, x2, y2);
        LineDouble otherLine = LineDouble.create(10.0, 10.0, 15.0, 15.0);

        // When
        boolean equals = line.equals(otherLine);

        // Then
        assertFalse(equals);
    }

    @Test
    public void testIsDoublePrecision() {
        // Given
        double x1 = 1.0;
        double y1 = 2.0;
        double x2 = 3.0;
        double y2 = 4.0;
        LineDouble line = LineDouble.create(x1, y1, x2, y2);

        // When
        boolean isDoublePrecision = line.isDoublePrecision();

        // Then
        assertTrue(isDoublePrecision);
    }
}
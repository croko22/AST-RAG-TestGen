import com.github.davidmoten.rtree.geometry.Circle;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Intersects;
import com.github.davidmoten.rtree.geometry.Line;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.geometry.Rectangle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import rx.functions.Func2;

public class IntersectsTest {

    @Test
    public void testRectangleIntersectsCircle() {
        // Given
        Rectangle rectangle = new Rectangle(0, 0, 10, 10);
        Circle circle = new Circle(5, 5, 5);

        // When
        boolean result = Intersects.rectangleIntersectsCircle.call(rectangle, circle);

        // Then
        assertTrue(result);
    }

    @Test
    public void testRectangleDoesNotIntersectCircle() {
        // Given
        Rectangle rectangle = new Rectangle(0, 0, 10, 10);
        Circle circle = new Circle(20, 20, 5);

        // When
        boolean result = Intersects.rectangleIntersectsCircle.call(rectangle, circle);

        // Then
        assertFalse(result);
    }

    @Test
    public void testCircleIntersectsRectangle() {
        // Given
        Circle circle = new Circle(5, 5, 5);
        Rectangle rectangle = new Rectangle(0, 0, 10, 10);

        // When
        boolean result = Intersects.circleIntersectsRectangle.call(circle, rectangle);

        // Then
        assertTrue(result);
    }

    @Test
    public void testCircleDoesNotIntersectRectangle() {
        // Given
        Circle circle = new Circle(20, 20, 5);
        Rectangle rectangle = new Rectangle(0, 0, 10, 10);

        // When
        boolean result = Intersects.circleIntersectsRectangle.call(circle, rectangle);

        // Then
        assertFalse(result);
    }

    @Test
    public void testPointIntersectsCircle() {
        // Given
        Point point = new Point(5, 5);
        Circle circle = new Circle(5, 5, 5);

        // When
        boolean result = Intersects.pointIntersectsCircle.call(point, circle);

        // Then
        assertTrue(result);
    }

    @Test
    public void testPointDoesNotIntersectCircle() {
        // Given
        Point point = new Point(20, 20);
        Circle circle = new Circle(5, 5, 5);

        // When
        boolean result = Intersects.pointIntersectsCircle.call(point, circle);

        // Then
        assertFalse(result);
    }

    @Test
    public void testCircleIntersectsPoint() {
        // Given
        Circle circle = new Circle(5, 5, 5);
        Point point = new Point(5, 5);

        // When
        boolean result = Intersects.circleIntersectsPoint.call(circle, point);

        // Then
        assertTrue(result);
    }

    @Test
    public void testCircleDoesNotIntersectPoint() {
        // Given
        Circle circle = new Circle(5, 5, 5);
        Point point = new Point(20, 20);

        // When
        boolean result = Intersects.circleIntersectsPoint.call(circle, point);

        // Then
        assertFalse(result);
    }

    @Test
    public void testCircleIntersectsCircle() {
        // Given
        Circle circle1 = new Circle(5, 5, 5);
        Circle circle2 = new Circle(5, 5, 5);

        // When
        boolean result = Intersects.circleIntersectsCircle.call(circle1, circle2);

        // Then
        assertTrue(result);
    }

    @Test
    public void testCircleDoesNotIntersectCircle() {
        // Given
        Circle circle1 = new Circle(5, 5, 5);
        Circle circle2 = new Circle(20, 20, 5);

        // When
        boolean result = Intersects.circleIntersectsCircle.call(circle1, circle2);

        // Then
        assertFalse(result);
    }

    @Test
    public void testLineIntersectsLine() {
        // Given
        Line line1 = new Line(0, 0, 10, 10);
        Line line2 = new Line(0, 10, 10, 0);

        // When
        boolean result = Intersects.lineIntersectsLine.call(line1, line2);

        // Then
        assertTrue(result);
    }

    @Test
    public void testLineDoesNotIntersectLine() {
        // Given
        Line line1 = new Line(0, 0, 10, 10);
        Line line2 = new Line(20, 20, 30, 30);

        // When
        boolean result = Intersects.lineIntersectsLine.call(line1, line2);

        // Then
        assertFalse(result);
    }

    @Test
    public void testLineIntersectsRectangle() {
        // Given
        Line line = new Line(0, 0, 10, 10);
        Rectangle rectangle = new Rectangle(0, 0, 10, 10);

        // When
        boolean result = Intersects.lineIntersectsRectangle.call(line, rectangle);

        // Then
        assertTrue(result);
    }

    @Test
    public void testLineDoesNotIntersectRectangle() {
        // Given
        Line line = new Line(20, 20, 30, 30);
        Rectangle rectangle = new Rectangle(0, 0, 10, 10);

        // When
        boolean result = Intersects.lineIntersectsRectangle.call(line, rectangle);

        // Then
        assertFalse(result);
    }

    @Test
    public void testRectangleIntersectsLine() {
        // Given
        Rectangle rectangle = new Rectangle(0, 0, 10, 10);
        Line line = new Line(0, 0, 10, 10);

        // When
        boolean result = Intersects.rectangleIntersectsLine.call(rectangle, line);

        // Then
        assertTrue(result);
    }

    @Test
    public void testRectangleDoesNotIntersectLine() {
        // Given
        Rectangle rectangle = new Rectangle(0, 0, 10, 10);
        Line line = new Line(20, 20, 30, 30);

        // When
        boolean result = Intersects.rectangleIntersectsLine.call(rectangle, line);

        // Then
        assertFalse(result);
    }

    @Test
    public void testLineIntersectsCircle() {
        // Given
        Line line = new Line(0, 0, 10, 10);
        Circle circle = new Circle(5, 5, 5);

        // When
        boolean result = Intersects.lineIntersectsCircle.call(line, circle);

        // Then
        assertTrue(result);
    }

    @Test
    public void testLineDoesNotIntersectCircle() {
        // Given
        Line line = new Line(20, 20, 30, 30);
        Circle circle = new Circle(5, 5, 5);

        // When
        boolean result = Intersects.lineIntersectsCircle.call(line, circle);

        // Then
        assertFalse(result);
    }

    @Test
    public void testCircleIntersectsLine() {
        // Given
        Circle circle = new Circle(5, 5, 5);
        Line line = new Line(0, 0, 10, 10);

        // When
        boolean result = Intersects.circleIntersectsLine.call(circle, line);

        // Then
        assertTrue(result);
    }

    @Test
    public void testCircleDoesNotIntersectLine() {
        // Given
        Circle circle = new Circle(5, 5, 5);
        Line line = new Line(20, 20, 30, 30);

        // When
        boolean result = Intersects.circleIntersectsLine.call(circle, line);

        // Then
        assertFalse(result);
    }

    @Test
    public void testLineIntersectsPoint() {
        // Given
        Line line = new Line(0, 0, 10, 10);
        Point point = new Point(5, 5);

        // When
        boolean result = Intersects.lineIntersectsPoint.call(line, point);

        // Then
        assertTrue(result);
    }

    @Test
    public void testLineDoesNotIntersectPoint() {
        // Given
        Line line = new Line(20, 20, 30, 30);
        Point point = new Point(5, 5);

        // When
        boolean result = Intersects.lineIntersectsPoint.call(line, point);

        // Then
        assertFalse(result);
    }

    @Test
    public void testPointIntersectsLine() {
        // Given
        Point point = new Point(5, 5);
        Line line = new Line(0, 0, 10, 10);

        // When
        boolean result = Intersects.pointIntersectsLine.call(point, line);

        // Then
        assertTrue(result);
    }

    @Test
    public void testPointDoesNotIntersectLine() {
        // Given
        Point point = new Point(5, 5);
        Line line = new Line(20, 20, 30, 30);

        // When
        boolean result = Intersects.pointIntersectsLine.call(point, line);

        // Then
        assertFalse(result);
    }

    @Test
    public void testGeometryIntersectsLine() {
        // Given
        Geometry geometry = new Rectangle(0, 0, 10, 10);
        Line line = new Line(0, 0, 10, 10);

        // When
        boolean result = Intersects.geometryIntersectsLine.call(geometry, line);

        // Then
        assertTrue(result);
    }

    @Test
    public void testGeometryDoesNotIntersectLine() {
        // Given
        Geometry geometry = new Rectangle(0, 0, 10, 10);
        Line line = new Line(20, 20, 30, 30);

        // When
        boolean result = Intersects.geometryIntersectsLine.call(geometry, line);

        // Then
        assertFalse(result);
    }

    @Test
    public void testGeometryIntersectsCircle() {
        // Given
        Geometry geometry = new Rectangle(0, 0, 10, 10);
        Circle circle = new Circle(5, 5, 5);

        // When
        boolean result = Intersects.geometryIntersectsCircle.call(geometry, circle);

        // Then
        assertTrue(result);
    }

    @Test
    public void testGeometryDoesNotIntersectCircle() {
        // Given
        Geometry geometry = new Rectangle(0, 0, 10, 10);
        Circle circle = new Circle(20, 20, 5);

        // When
        boolean result = Intersects.geometryIntersectsCircle.call(geometry, circle);

        // Then
        assertFalse(result);
    }

    @Test
    public void testCircleIntersectsGeometry() {
        // Given
        Circle circle = new Circle(5, 5, 5);
        Geometry geometry = new Rectangle(0, 0, 10, 10);

        // When
        boolean result = Intersects.circleIntersectsGeometry.call(circle, geometry);

        // Then
        assertTrue(result);
    }

    @Test
    public void testCircleDoesNotIntersectGeometry() {
        // Given
        Circle circle = new Circle(20, 20, 5);
        Geometry geometry = new Rectangle(0, 0, 10, 10);

        // When
        boolean result = Intersects.circleIntersectsGeometry.call(circle, geometry);

        // Then
        assertFalse(result);
    }

    @Test
    public void testGeometryIntersectsRectangle() {
        // Given
        Geometry geometry = new Rectangle(0, 0, 10, 10);
        Rectangle rectangle = new Rectangle(0, 0, 10, 10);

        // When
        boolean result = Intersects.geometryIntersectsRectangle.call(geometry, rectangle);

        // Then
        assertTrue(result);
    }

    @Test
    public void testGeometryDoesNotIntersectRectangle() {
        // Given
        Geometry geometry = new Rectangle(0, 0, 10, 10);
        Rectangle rectangle = new Rectangle(20, 20, 30, 30);

        // When
        boolean result = Intersects.geometryIntersectsRectangle.call(geometry, rectangle);

        // Then
        assertFalse(result);
    }

    @Test
    public void testRectangleIntersectsGeometry() {
        // Given
        Rectangle rectangle = new Rectangle(0, 0, 10, 10);
        Geometry geometry = new Rectangle(0, 0, 10, 10);

        // When
        boolean result = Intersects.rectangleIntersectsGeometry.call(rectangle, geometry);

        // Then
        assertTrue(result);
    }

    @Test
    public void testRectangleDoesNotIntersectGeometry() {
        // Given
        Rectangle rectangle = new Rectangle(0, 0, 10, 10);
        Geometry geometry = new Rectangle(20, 20, 30, 30);

        // When
        boolean result = Intersects.rectangleIntersectsGeometry.call(rectangle, geometry);

        // Then
        assertFalse(result);
    }

    @Test
    public void testGeometryIntersectsPoint() {
        // Given
        Geometry geometry = new Rectangle(0, 0, 10, 10);
        Point point = new Point(5, 5);

        // When
        boolean result = Intersects.geometryIntersectsPoint.call(geometry, point);

        // Then
        assertTrue(result);
    }

    @Test
    public void testGeometryDoesNotIntersectPoint() {
        // Given
        Geometry geometry = new Rectangle(0, 0, 10, 10);
        Point point = new Point(20, 20);

        // When
        boolean result = Intersects.geometryIntersectsPoint.call(geometry, point);

        // Then
        assertFalse(result);
    }

    @Test
    public void testPointIntersectsGeometry() {
        // Given
        Point point = new Point(5, 5);
        Geometry geometry = new Rectangle(0, 0, 10, 10);

        // When
        boolean result = Intersects.pointIntersectsGeometry.call(point, geometry);

        // Then
        assertTrue(result);
    }

    @Test
    public void testPointDoesNotIntersectGeometry() {
        // Given
        Point point = new Point(20, 20);
        Geometry geometry = new Rectangle(0, 0, 10, 10);

        // When
        boolean result = Intersects.pointIntersectsGeometry.call(point, geometry);

        // Then
        assertFalse(result);
    }
}
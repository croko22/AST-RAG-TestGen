import com.github.davidmoten.rtree.geometry.Circle;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.geometry.Line;
import com.github.davidmoten.rtree.geometry.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GeometryUtilTest {

    @Test
    public void testDistanceSquared() {
        // Given
        double x1 = 1.0;
        double y1 = 2.0;
        double x2 = 4.0;
        double y2 = 6.0;

        // When
        double distanceSquared = com.github.davidmoten.rtree.geometry.internal.GeometryUtil.distanceSquared(x1, y1, x2, y2);

        // Then
        assertEquals(9.0 + 16.0, distanceSquared);
    }

    @Test
    public void testMaxDouble() {
        // Given
        double a = 1.0;
        double b = 2.0;

        // When
        double max = com.github.davidmoten.rtree.geometry.internal.GeometryUtil.max(a, b);

        // Then
        assertEquals(2.0, max);
    }

    @Test
    public void testMaxFloat() {
        // Given
        float a = 1.0f;
        float b = 2.0f;

        // When
        float max = com.github.davidmoten.rtree.geometry.internal.GeometryUtil.max(a, b);

        // Then
        assertEquals(2.0f, max);
    }

    @Test
    public void testMinDouble() {
        // Given
        double a = 1.0;
        double b = 2.0;

        // When
        double min = com.github.davidmoten.rtree.geometry.internal.GeometryUtil.min(a, b);

        // Then
        assertEquals(1.0, min);
    }

    @Test
    public void testMinFloat() {
        // Given
        float a = 1.0f;
        float b = 2.0f;

        // When
        float min = com.github.davidmoten.rtree.geometry.internal.GeometryUtil.min(a, b);

        // Then
        assertEquals(1.0f, min);
    }

    @Test
    public void testDistanceToRectangle() {
        // Given
        double x = 1.0;
        double y = 2.0;
        Rectangle rectangle = new Rectangle() {
            @Override
            public double x1() {
                return 0.0;
            }

            @Override
            public double y1() {
                return 0.0;
            }

            @Override
            public double x2() {
                return 4.0;
            }

            @Override
            public double y2() {
                return 4.0;
            }

            @Override
            public double area() {
                return 0;
            }

            @Override
            public double intersectionArea(Rectangle r) {
                return 0;
            }

            @Override
            public double perimeter() {
                return 0;
            }

            @Override
            public Rectangle add(Rectangle r) {
                return null;
            }

            @Override
            public boolean contains(double x, double y) {
                return false;
            }

            @Override
            public boolean isDoublePrecision() {
                return false;
            }
        };

        // When
        double distance = com.github.davidmoten.rtree.geometry.internal.GeometryUtil.distance(x, y, rectangle);

        // Then
        assertEquals(0.0, distance);
    }

    @Test
    public void testDistanceToRectangleWithCoordinates() {
        // Given
        double x = 1.0;
        double y = 2.0;
        double a1 = 0.0;
        double b1 = 0.0;
        double a2 = 4.0;
        double b2 = 4.0;

        // When
        double distance = com.github.davidmoten.rtree.geometry.internal.GeometryUtil.distance(x, y, a1, b1, a2, b2);

        // Then
        assertEquals(0.0, distance);
    }

    @Test
    public void testDistanceToRectangleWithTwoPoints() {
        // Given
        double x1 = 1.0;
        double y1 = 2.0;
        double x2 = 3.0;
        double y2 = 4.0;
        double a1 = 0.0;
        double b1 = 0.0;
        double a2 = 4.0;
        double b2 = 4.0;

        // When
        double distance = com.github.davidmoten.rtree.geometry.internal.GeometryUtil.distance(x1, y1, x2, y2, a1, b1, a2, b2);

        // Then
        assertEquals(0.0, distance);
    }

    @Test
    public void testIntersects() {
        // Given
        double x1 = 1.0;
        double y1 = 2.0;
        double x2 = 4.0;
        double y2 = 4.0;
        double a1 = 2.0;
        double b1 = 2.0;
        double a2 = 3.0;
        double b2 = 3.0;

        // When
        boolean intersects = com.github.davidmoten.rtree.geometry.internal.GeometryUtil.intersects(x1, y1, x2, y2, a1, b1, a2, b2);

        // Then
        assertTrue(intersects);
    }

    @Test
    public void testLineIntersects() {
        // Given
        double x1 = 1.0;
        double y1 = 2.0;
        double x2 = 4.0;
        double y2 = 4.0;
        Circle circle = new Circle() {
            @Override
            public double x() {
                return 2.5;
            }

            @Override
            public double y() {
                return 2.5;
            }

            @Override
            public double radius() {
                return 1.0;
            }

            @Override
            public boolean intersects(Circle c) {
                return false;
            }

            @Override
            public boolean intersects(Point point) {
                return false;
            }

            @Override
            public boolean intersects(Line line) {
                return false;
            }
        };

        // When
        boolean intersects = com.github.davidmoten.rtree.geometry.internal.GeometryUtil.lineIntersects(x1, y1, x2, y2, circle);

        // Then
        assertTrue(intersects);
    }

    @Test
    public void testLineIntersectsNot() {
        // Given
        double x1 = 1.0;
        double y1 = 2.0;
        double x2 = 4.0;
        double y2 = 4.0;
        Circle circle = new Circle() {
            @Override
            public double x() {
                return 10.0;
            }

            @Override
            public double y() {
                return 10.0;
            }

            @Override
            public double radius() {
                return 1.0;
            }

            @Override
            public boolean intersects(Circle c) {
                return false;
            }

            @Override
            public boolean intersects(Point point) {
                return false;
            }

            @Override
            public boolean intersects(Line line) {
                return false;
            }
        };

        // When
        boolean intersects = com.github.davidmoten.rtree.geometry.internal.GeometryUtil.lineIntersects(x1, y1, x2, y2, circle);

        // Then
        assertFalse(intersects);
    }
}
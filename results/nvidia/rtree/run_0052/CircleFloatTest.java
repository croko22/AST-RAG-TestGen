import com.github.davidmoten.rtree.geometry.Circle;
import com.github.davidmoten.rtree.geometry.CircleFloat;
import com.github.davidmoten.rtree.geometry.Line;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.geometry.Rectangle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class CircleFloatTest {

    @Test
    public void testCreate() {
        // Given
        float x = 1.0f;
        float y = 2.0f;
        float radius = 3.0f;

        // When
        CircleFloat circle = CircleFloat.create(x, y, radius);

        // Then
        assertEquals(x, circle.x(), 0.001);
        assertEquals(y, circle.y(), 0.001);
        assertEquals(radius, circle.radius(), 0.001);
    }

    @Test
    public void testX() {
        // Given
        float x = 1.0f;
        float y = 2.0f;
        float radius = 3.0f;
        CircleFloat circle = CircleFloat.create(x, y, radius);

        // When
        double result = circle.x();

        // Then
        assertEquals(x, result, 0.001);
    }

    @Test
    public void testY() {
        // Given
        float x = 1.0f;
        float y = 2.0f;
        float radius = 3.0f;
        CircleFloat circle = CircleFloat.create(x, y, radius);

        // When
        double result = circle.y();

        // Then
        assertEquals(y, result, 0.001);
    }

    @Test
    public void testRadius() {
        // Given
        float x = 1.0f;
        float y = 2.0f;
        float radius = 3.0f;
        CircleFloat circle = CircleFloat.create(x, y, radius);

        // When
        double result = circle.radius();

        // Then
        assertEquals(radius, result, 0.001);
    }

    @Test
    public void testMbr() {
        // Given
        float x = 1.0f;
        float y = 2.0f;
        float radius = 3.0f;
        CircleFloat circle = CircleFloat.create(x, y, radius);

        // When
        Rectangle mbr = circle.mbr();

        // Then
        assertEquals(x - radius, mbr.x1(), 0.001);
        assertEquals(y - radius, mbr.y1(), 0.001);
        assertEquals(x + radius, mbr.x2(), 0.001);
        assertEquals(y + radius, mbr.y2(), 0.001);
    }

    @Test
    public void testDistance() {
        // Given
        float x = 1.0f;
        float y = 2.0f;
        float radius = 3.0f;
        CircleFloat circle = CircleFloat.create(x, y, radius);
        Rectangle rectangle = new Rectangle() {
            @Override
            public double x1() {
                return 0;
            }

            @Override
            public double y1() {
                return 0;
            }

            @Override
            public double x2() {
                return 10;
            }

            @Override
            public double y2() {
                return 10;
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
        double result = circle.distance(rectangle);

        // Then
        assertTrue(result >= 0);
    }

    @Test
    public void testIntersectsRectangle() {
        // Given
        float x = 1.0f;
        float y = 2.0f;
        float radius = 3.0f;
        CircleFloat circle = CircleFloat.create(x, y, radius);
        Rectangle rectangle = new Rectangle() {
            @Override
            public double x1() {
                return 0;
            }

            @Override
            public double y1() {
                return 0;
            }

            @Override
            public double x2() {
                return 10;
            }

            @Override
            public double y2() {
                return 10;
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
        boolean result = circle.intersects(rectangle);

        // Then
        assertTrue(result);
    }

    @Test
    public void testIntersectsCircle() {
        // Given
        float x = 1.0f;
        float y = 2.0f;
        float radius = 3.0f;
        CircleFloat circle = CircleFloat.create(x, y, radius);
        Circle otherCircle = new CircleFloat(4.0f, 5.0f, 2.0f);

        // When
        boolean result = circle.intersects(otherCircle);

        // Then
        assertTrue(result);
    }

    @Test
    public void testHashCode() {
        // Given
        float x = 1.0f;
        float y = 2.0f;
        float radius = 3.0f;
        CircleFloat circle = CircleFloat.create(x, y, radius);

        // When
        int result = circle.hashCode();

        // Then
        assertTrue(result != 0);
    }

    @Test
    public void testEquals() {
        // Given
        float x = 1.0f;
        float y = 2.0f;
        float radius = 3.0f;
        CircleFloat circle = CircleFloat.create(x, y, radius);
        CircleFloat otherCircle = CircleFloat.create(x, y, radius);

        // When
        boolean result = circle.equals(otherCircle);

        // Then
        assertTrue(result);
    }

    @Test
    public void testIntersectsPoint() {
        // Given
        float x = 1.0f;
        float y = 2.0f;
        float radius = 3.0f;
        CircleFloat circle = CircleFloat.create(x, y, radius);
        Point point = new Point() {
            @Override
            public double x() {
                return 1.0;
            }

            @Override
            public double y() {
                return 2.0;
            }
        };

        // When
        boolean result = circle.intersects(point);

        // Then
        assertTrue(result);
    }

    @Test
    public void testIntersectsLine() {
        // Given
        float x = 1.0f;
        float y = 2.0f;
        float radius = 3.0f;
        CircleFloat circle = CircleFloat.create(x, y, radius);
        Line line = new Line() {
            @Override
            public double x1() {
                return 0;
            }

            @Override
            public double y1() {
                return 0;
            }

            @Override
            public double x2() {
                return 10;
            }

            @Override
            public double y2() {
                return 10;
            }

            @Override
            public boolean intersects(Line b) {
                return false;
            }

            @Override
            public boolean intersects(Point point) {
                return false;
            }

            @Override
            public boolean intersects(Circle circle) {
                return true;
            }
        };

        // When
        boolean result = circle.intersects(line);

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsDoublePrecision() {
        // Given
        float x = 1.0f;
        float y = 2.0f;
        float radius = 3.0f;
        CircleFloat circle = CircleFloat.create(x, y, radius);

        // When
        boolean result = circle.isDoublePrecision();

        // Then
        assertFalse(result);
    }
}
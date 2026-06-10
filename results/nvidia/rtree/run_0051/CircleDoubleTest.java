import com.github.davidmoten.rtree.geometry.Circle;
import com.github.davidmoten.rtree.geometry.CircleDouble;
import com.github.davidmoten.rtree.geometry.Line;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.internal.util.ObjectsHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CircleDoubleTest {

    private CircleDouble circle;

    @BeforeEach
    public void setup() {
        circle = CircleDouble.create(0, 0, 1);
    }

    @Test
    public void testCreate() {
        // Given
        double x = 1;
        double y = 2;
        double radius = 3;

        // When
        CircleDouble createdCircle = CircleDouble.create(x, y, radius);

        // Then
        assertEquals(x, createdCircle.x());
        assertEquals(y, createdCircle.y());
        assertEquals(radius, createdCircle.radius());
    }

    @Test
    public void testX() {
        // Given
        double x = 0;

        // When
        double result = circle.x();

        // Then
        assertEquals(x, result);
    }

    @Test
    public void testY() {
        // Given
        double y = 0;

        // When
        double result = circle.y();

        // Then
        assertEquals(y, result);
    }

    @Test
    public void testRadius() {
        // Given
        double radius = 1;

        // When
        double result = circle.radius();

        // Then
        assertEquals(radius, result);
    }

    @Test
    public void testMbr() {
        // Given
        Rectangle expectedMbr = new Rectangle() {
            @Override
            public double x1() {
                return -1;
            }

            @Override
            public double y1() {
                return -1;
            }

            @Override
            public double x2() {
                return 1;
            }

            @Override
            public double y2() {
                return 1;
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
        Rectangle result = circle.mbr();

        // Then
        assertEquals(expectedMbr.x1(), result.x1());
        assertEquals(expectedMbr.y1(), result.y1());
        assertEquals(expectedMbr.x2(), result.x2());
        assertEquals(expectedMbr.y2(), result.y2());
    }

    @Test
    public void testDistance_Rectangle() {
        // Given
        Rectangle rectangle = new Rectangle() {
            @Override
            public double x1() {
                return 2;
            }

            @Override
            public double y1() {
                return 2;
            }

            @Override
            public double x2() {
                return 3;
            }

            @Override
            public double y2() {
                return 3;
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
        assertEquals(1, result);
    }

    @Test
    public void testIntersects_Rectangle() {
        // Given
        Rectangle rectangle = new Rectangle() {
            @Override
            public double x1() {
                return -1;
            }

            @Override
            public double y1() {
                return -1;
            }

            @Override
            public double x2() {
                return 1;
            }

            @Override
            public double y2() {
                return 1;
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
    public void testIntersects_Circle() {
        // Given
        Circle otherCircle = new CircleDouble(0, 0, 1);

        // When
        boolean result = circle.intersects(otherCircle);

        // Then
        assertTrue(result);
    }

    @Test
    public void testHashCode() {
        // Given
        CircleDouble otherCircle = CircleDouble.create(0, 0, 1);

        // When
        int result = circle.hashCode();

        // Then
        assertEquals(otherCircle.hashCode(), result);
    }

    @Test
    public void testEquals() {
        // Given
        CircleDouble otherCircle = CircleDouble.create(0, 0, 1);

        // When
        boolean result = circle.equals(otherCircle);

        // Then
        assertTrue(result);
    }

    @Test
    public void testIntersects_Point() {
        // Given
        Point point = new Point() {
            @Override
            public double x() {
                return 0;
            }

            @Override
            public double y() {
                return 0;
            }
        };

        // When
        boolean result = circle.intersects(point);

        // Then
        assertTrue(result);
    }

    @Test
    public void testIntersects_Line() {
        // Given
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
                return 1;
            }

            @Override
            public double y2() {
                return 1;
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
                return false;
            }
        };

        // When
        boolean result = circle.intersects(line);

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsDoublePrecision() {
        // When
        boolean result = circle.isDoublePrecision();

        // Then
        assertTrue(result);
    }
}
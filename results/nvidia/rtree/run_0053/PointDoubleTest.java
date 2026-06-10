import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.geometry.internal.PointDouble;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PointDoubleTest {

    private PointDouble point;

    @BeforeEach
    public void setup() {
        point = PointDouble.create(10.0, 20.0);
    }

    @Test
    public void testCreate() {
        PointDouble point = PointDouble.create(10.0, 20.0);
        assertNotNull(point);
        assertEquals(10.0, point.x(), 0.001);
        assertEquals(20.0, point.y(), 0.001);
    }

    @Test
    public void testMbr() {
        Rectangle mbr = point.mbr();
        assertNotNull(mbr);
        assertEquals(point.x1(), mbr.x1(), 0.001);
        assertEquals(point.y1(), mbr.y1(), 0.001);
        assertEquals(point.x2(), mbr.x2(), 0.001);
        assertEquals(point.y2(), mbr.y2(), 0.001);
    }

    @Test
    public void testDistance() {
        Rectangle rectangle = new Rectangle() {
            @Override
            public double x1() {
                return 5.0;
            }

            @Override
            public double y1() {
                return 5.0;
            }

            @Override
            public double x2() {
                return 15.0;
            }

            @Override
            public double y2() {
                return 15.0;
            }
        };
        double distance = point.distance(rectangle);
        assertTrue(distance >= 0);
    }

    @Test
    public void testIntersects() {
        Rectangle rectangle = new Rectangle() {
            @Override
            public double x1() {
                return 5.0;
            }

            @Override
            public double y1() {
                return 5.0;
            }

            @Override
            public double x2() {
                return 15.0;
            }

            @Override
            public double y2() {
                return 15.0;
            }
        };
        boolean intersects = point.intersects(rectangle);
        assertTrue(intersects);
    }

    @Test
    public void testX() {
        assertEquals(10.0, point.x(), 0.001);
    }

    @Test
    public void testY() {
        assertEquals(20.0, point.y(), 0.001);
    }

    @Test
    public void testToString() {
        String toString = point.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("x=") && toString.contains("y="));
    }

    @Test
    public void testGeometry() {
        Geometry geometry = point.geometry();
        assertNotNull(geometry);
        assertEquals(point, geometry);
    }

    @Test
    public void testX1() {
        assertEquals(10.0, point.x1(), 0.001);
    }

    @Test
    public void testY1() {
        assertEquals(20.0, point.y1(), 0.001);
    }

    @Test
    public void testX2() {
        assertEquals(10.0, point.x2(), 0.001);
    }

    @Test
    public void testY2() {
        assertEquals(20.0, point.y2(), 0.001);
    }

    @Test
    public void testArea() {
        assertEquals(0.0, point.area(), 0.001);
    }

    @Test
    public void testAdd() {
        Rectangle rectangle = new Rectangle() {
            @Override
            public double x1() {
                return 5.0;
            }

            @Override
            public double y1() {
                return 5.0;
            }

            @Override
            public double x2() {
                return 15.0;
            }

            @Override
            public double y2() {
                return 15.0;
            }
        };
        Rectangle added = point.add(rectangle);
        assertNotNull(added);
        assertEquals(5.0, added.x1(), 0.001);
        assertEquals(5.0, added.y1(), 0.001);
        assertEquals(15.0, added.x2(), 0.001);
        assertEquals(15.0, added.y2(), 0.001);
    }

    @Test
    public void testContains() {
        boolean contains = point.contains(10.0, 20.0);
        assertTrue(contains);
    }

    @Test
    public void testIntersectionArea() {
        Rectangle rectangle = new Rectangle() {
            @Override
            public double x1() {
                return 5.0;
            }

            @Override
            public double y1() {
                return 5.0;
            }

            @Override
            public double x2() {
                return 15.0;
            }

            @Override
            public double y2() {
                return 15.0;
            }
        };
        double intersectionArea = point.intersectionArea(rectangle);
        assertEquals(0.0, intersectionArea, 0.001);
    }

    @Test
    public void testPerimeter() {
        assertEquals(0.0, point.perimeter(), 0.001);
    }

    @Test
    public void testIsDoublePrecision() {
        assertTrue(point.isDoublePrecision());
    }

    @Test
    public void testHashCode() {
        int hashCode = point.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testEquals() {
        PointDouble other = PointDouble.create(10.0, 20.0);
        assertTrue(point.equals(other));
    }

    @Test
    public void testEqualsNot() {
        PointDouble other = PointDouble.create(15.0, 25.0);
        assertFalse(point.equals(other));
    }

    @Test
    public void testEqualsNull() {
        assertFalse(point.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        assertFalse(point.equals("string"));
    }
}
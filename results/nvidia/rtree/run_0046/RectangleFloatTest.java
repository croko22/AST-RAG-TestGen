import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.geometry.internal.RectangleFloat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RectangleFloatTest {

    private RectangleFloat rectangle;

    @BeforeEach
    public void setup() {
        rectangle = (RectangleFloat) RectangleFloat.create(0.0f, 0.0f, 10.0f, 10.0f);
    }

    @Test
    public void testCreate() {
        // Given
        float x1 = 0.0f;
        float y1 = 0.0f;
        float x2 = 10.0f;
        float y2 = 10.0f;

        // When
        RectangleFloat createdRectangle = (RectangleFloat) RectangleFloat.create(x1, y1, x2, y2);

        // Then
        assertEquals(x1, createdRectangle.x1, 0.001);
        assertEquals(y1, createdRectangle.y1, 0.001);
        assertEquals(x2, createdRectangle.x2, 0.001);
        assertEquals(y2, createdRectangle.y2, 0.001);
    }

    @Test
    public void testX1() {
        // Given
        double expectedX1 = 0.0;

        // When
        double actualX1 = rectangle.x1();

        // Then
        assertEquals(expectedX1, actualX1, 0.001);
    }

    @Test
    public void testY1() {
        // Given
        double expectedY1 = 0.0;

        // When
        double actualY1 = rectangle.y1();

        // Then
        assertEquals(expectedY1, actualY1, 0.001);
    }

    @Test
    public void testX2() {
        // Given
        double expectedX2 = 10.0;

        // When
        double actualX2 = rectangle.x2();

        // Then
        assertEquals(expectedX2, actualX2, 0.001);
    }

    @Test
    public void testY2() {
        // Given
        double expectedY2 = 10.0;

        // When
        double actualY2 = rectangle.y2();

        // Then
        assertEquals(expectedY2, actualY2, 0.001);
    }

    @Test
    public void testArea() {
        // Given
        double expectedArea = 100.0;

        // When
        double actualArea = rectangle.area();

        // Then
        assertEquals(expectedArea, actualArea, 0.001);
    }

    @Test
    public void testAdd_Rectangle() {
        // Given
        Rectangle otherRectangle = RectangleFloat.create(5.0f, 5.0f, 15.0f, 15.0f);
        double expectedX1 = 0.0;
        double expectedY1 = 0.0;
        double expectedX2 = 15.0;
        double expectedY2 = 15.0;

        // When
        Rectangle addedRectangle = rectangle.add(otherRectangle);

        // Then
        assertEquals(expectedX1, addedRectangle.x1(), 0.001);
        assertEquals(expectedY1, addedRectangle.y1(), 0.001);
        assertEquals(expectedX2, addedRectangle.x2(), 0.001);
        assertEquals(expectedY2, addedRectangle.y2(), 0.001);
    }

    @Test
    public void testContains_PointInside() {
        // Given
        double x = 5.0;
        double y = 5.0;
        boolean expectedContains = true;

        // When
        boolean actualContains = rectangle.contains(x, y);

        // Then
        assertEquals(expectedContains, actualContains);
    }

    @Test
    public void testContains_PointOutside() {
        // Given
        double x = 15.0;
        double y = 15.0;
        boolean expectedContains = false;

        // When
        boolean actualContains = rectangle.contains(x, y);

        // Then
        assertEquals(expectedContains, actualContains);
    }

    @Test
    public void testIntersects_RectangleIntersecting() {
        // Given
        Rectangle otherRectangle = RectangleFloat.create(5.0f, 5.0f, 15.0f, 15.0f);
        boolean expectedIntersects = true;

        // When
        boolean actualIntersects = rectangle.intersects(otherRectangle);

        // Then
        assertEquals(expectedIntersects, actualIntersects);
    }

    @Test
    public void testIntersects_RectangleNotIntersecting() {
        // Given
        Rectangle otherRectangle = RectangleFloat.create(15.0f, 15.0f, 20.0f, 20.0f);
        boolean expectedIntersects = false;

        // When
        boolean actualIntersects = rectangle.intersects(otherRectangle);

        // Then
        assertEquals(expectedIntersects, actualIntersects);
    }

    @Test
    public void testDistance_Rectangle() {
        // Given
        Rectangle otherRectangle = RectangleFloat.create(15.0f, 15.0f, 20.0f, 20.0f);
        double expectedDistance = 5.0;

        // When
        double actualDistance = rectangle.distance(otherRectangle);

        // Then
        assertEquals(expectedDistance, actualDistance, 0.001);
    }

    @Test
    public void testMbr() {
        // Given
        Rectangle expectedMbr = rectangle;

        // When
        Rectangle actualMbr = rectangle.mbr();

        // Then
        assertEquals(expectedMbr, actualMbr);
    }

    @Test
    public void testHashCode() {
        // Given
        int expectedHashCode = Objects.hash(0.0f, 0.0f, 10.0f, 10.0f);

        // When
        int actualHashCode = rectangle.hashCode();

        // Then
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testEquals_RectangleEqual() {
        // Given
        Rectangle otherRectangle = RectangleFloat.create(0.0f, 0.0f, 10.0f, 10.0f);
        boolean expectedEquals = true;

        // When
        boolean actualEquals = rectangle.equals(otherRectangle);

        // Then
        assertEquals(expectedEquals, actualEquals);
    }

    @Test
    public void testEquals_RectangleNotEqual() {
        // Given
        Rectangle otherRectangle = RectangleFloat.create(5.0f, 5.0f, 15.0f, 15.0f);
        boolean expectedEquals = false;

        // When
        boolean actualEquals = rectangle.equals(otherRectangle);

        // Then
        assertEquals(expectedEquals, actualEquals);
    }

    @Test
    public void testIntersectionArea_RectangleIntersecting() {
        // Given
        Rectangle otherRectangle = RectangleFloat.create(5.0f, 5.0f, 15.0f, 15.0f);
        double expectedIntersectionArea = 25.0;

        // When
        double actualIntersectionArea = rectangle.intersectionArea(otherRectangle);

        // Then
        assertEquals(expectedIntersectionArea, actualIntersectionArea, 0.001);
    }

    @Test
    public void testIntersectionArea_RectangleNotIntersecting() {
        // Given
        Rectangle otherRectangle = RectangleFloat.create(15.0f, 15.0f, 20.0f, 20.0f);
        double expectedIntersectionArea = 0.0;

        // When
        double actualIntersectionArea = rectangle.intersectionArea(otherRectangle);

        // Then
        assertEquals(expectedIntersectionArea, actualIntersectionArea, 0.001);
    }

    @Test
    public void testPerimeter() {
        // Given
        double expectedPerimeter = 40.0;

        // When
        double actualPerimeter = rectangle.perimeter();

        // Then
        assertEquals(expectedPerimeter, actualPerimeter, 0.001);
    }

    @Test
    public void testGeometry() {
        // Given
        Geometry expectedGeometry = rectangle;

        // When
        Geometry actualGeometry = rectangle.geometry();

        // Then
        assertEquals(expectedGeometry, actualGeometry);
    }

    @Test
    public void testIsDoublePrecision() {
        // Given
        boolean expectedIsDoublePrecision = false;

        // When
        boolean actualIsDoublePrecision = rectangle.isDoublePrecision();

        // Then
        assertEquals(expectedIsDoublePrecision, actualIsDoublePrecision);
    }

    @Test
    public void testToString() {
        // Given
        String expectedToString = "Rectangle [x1=0.0, y1=0.0, x2=10.0, y2=10.0]";

        // When
        String actualToString = rectangle.toString();

        // Then
        assertEquals(expectedToString, actualToString);
    }
}
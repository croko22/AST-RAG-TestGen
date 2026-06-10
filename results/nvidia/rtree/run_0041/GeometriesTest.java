import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.internal.CircleDouble;
import com.github.davidmoten.rtree.geometry.internal.CircleFloat;
import com.github.davidmoten.rtree.geometry.internal.LineDouble;
import com.github.davidmoten.rtree.geometry.internal.LineFloat;
import com.github.davidmoten.rtree.geometry.internal.PointDouble;
import com.github.davidmoten.rtree.geometry.internal.PointFloat;
import com.github.davidmoten.rtree.geometry.internal.RectangleDouble;
import com.github.davidmoten.rtree.geometry.internal.RectangleFloat;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GeometriesTest {

    @Test
    public void testPointDouble() {
        // Given
        double x = 10.0;
        double y = 20.0;

        // When
        PointDouble point = (PointDouble) Geometries.point(x, y);

        // Then
        assertNotNull(point);
        assertEquals(x, point.x(), 1e-9);
        assertEquals(y, point.y(), 1e-9);
    }

    @Test
    public void testPointFloat() {
        // Given
        float x = 10.0f;
        float y = 20.0f;

        // When
        PointFloat point = (PointFloat) Geometries.point(x, y);

        // Then
        assertNotNull(point);
        assertEquals(x, point.xFloat(), 1e-9f);
        assertEquals(y, point.yFloat(), 1e-9f);
    }

    @Test
    public void testPointGeographicDouble() {
        // Given
        double lon = 10.0;
        double lat = 20.0;

        // When
        PointDouble point = (PointDouble) Geometries.pointGeographic(lon, lat);

        // Then
        assertNotNull(point);
        assertEquals(Geometries.normalizeLongitudeDouble(lon), point.x(), 1e-9);
        assertEquals(lat, point.y(), 1e-9);
    }

    @Test
    public void testPointGeographicFloat() {
        // Given
        float lon = 10.0f;
        float lat = 20.0f;

        // When
        PointFloat point = (PointFloat) Geometries.pointGeographic(lon, lat);

        // Then
        assertNotNull(point);
        assertEquals(Geometries.normalizeLongitude(lon), point.xFloat(), 1e-9f);
        assertEquals(lat, point.yFloat(), 1e-9f);
    }

    @Test
    public void testRectangleDouble() {
        // Given
        double x1 = 10.0;
        double y1 = 20.0;
        double x2 = 30.0;
        double y2 = 40.0;

        // When
        RectangleDouble rectangle = (RectangleDouble) Geometries.rectangle(x1, y1, x2, y2);

        // Then
        assertNotNull(rectangle);
        assertEquals(x1, rectangle.x1(), 1e-9);
        assertEquals(y1, rectangle.y1(), 1e-9);
        assertEquals(x2, rectangle.x2(), 1e-9);
        assertEquals(y2, rectangle.y2(), 1e-9);
    }

    @Test
    public void testRectangleFloat() {
        // Given
        float x1 = 10.0f;
        float y1 = 20.0f;
        float x2 = 30.0f;
        float y2 = 40.0f;

        // When
        RectangleFloat rectangle = (RectangleFloat) Geometries.rectangle(x1, y1, x2, y2);

        // Then
        assertNotNull(rectangle);
        assertEquals(x1, rectangle.x1(), 1e-9f);
        assertEquals(y1, rectangle.y1(), 1e-9f);
        assertEquals(x2, rectangle.x2(), 1e-9f);
        assertEquals(y2, rectangle.y2(), 1e-9f);
    }

    @Test
    public void testRectangleGeographicDouble() {
        // Given
        double lon1 = 10.0;
        double lat1 = 20.0;
        double lon2 = 30.0;
        double lat2 = 40.0;

        // When
        RectangleFloat rectangle = (RectangleFloat) Geometries.rectangleGeographic(lon1, lat1, lon2, lat2);

        // Then
        assertNotNull(rectangle);
        assertEquals(Geometries.normalizeLongitude((float) lon1), rectangle.x1(), 1e-9f);
        assertEquals(lat1, rectangle.y1(), 1e-9f);
        assertEquals(Geometries.normalizeLongitude((float) lon2), rectangle.x2(), 1e-9f);
        assertEquals(lat2, rectangle.y2(), 1e-9f);
    }

    @Test
    public void testRectangleGeographicFloat() {
        // Given
        float lon1 = 10.0f;
        float lat1 = 20.0f;
        float lon2 = 30.0f;
        float lat2 = 40.0f;

        // When
        RectangleFloat rectangle = (RectangleFloat) Geometries.rectangleGeographic(lon1, lat1, lon2, lat2);

        // Then
        assertNotNull(rectangle);
        assertEquals(Geometries.normalizeLongitude(lon1), rectangle.x1(), 1e-9f);
        assertEquals(lat1, rectangle.y1(), 1e-9f);
        assertEquals(Geometries.normalizeLongitude(lon2), rectangle.x2(), 1e-9f);
        assertEquals(lat2, rectangle.y2(), 1e-9f);
    }

    @Test
    public void testCircleDouble() {
        // Given
        double x = 10.0;
        double y = 20.0;
        double radius = 30.0;

        // When
        CircleDouble circle = (CircleDouble) Geometries.circle(x, y, radius);

        // Then
        assertNotNull(circle);
        assertEquals(x, circle.x(), 1e-9);
        assertEquals(y, circle.y(), 1e-9);
        assertEquals(radius, circle.radius(), 1e-9);
    }

    @Test
    public void testCircleFloat() {
        // Given
        float x = 10.0f;
        float y = 20.0f;
        float radius = 30.0f;

        // When
        CircleFloat circle = (CircleFloat) Geometries.circle(x, y, radius);

        // Then
        assertNotNull(circle);
        assertEquals(x, circle.x(), 1e-9f);
        assertEquals(y, circle.y(), 1e-9f);
        assertEquals(radius, circle.radius(), 1e-9f);
    }

    @Test
    public void testLineDouble() {
        // Given
        double x1 = 10.0;
        double y1 = 20.0;
        double x2 = 30.0;
        double y2 = 40.0;

        // When
        LineDouble line = (LineDouble) Geometries.line(x1, y1, x2, y2);

        // Then
        assertNotNull(line);
        assertEquals(x1, line.x1(), 1e-9);
        assertEquals(y1, line.y1(), 1e-9);
        assertEquals(x2, line.x2(), 1e-9);
        assertEquals(y2, line.y2(), 1e-9);
    }

    @Test
    public void testLineFloat() {
        // Given
        float x1 = 10.0f;
        float y1 = 20.0f;
        float x2 = 30.0f;
        float y2 = 40.0f;

        // When
        LineFloat line = (LineFloat) Geometries.line(x1, y1, x2, y2);

        // Then
        assertNotNull(line);
        assertEquals(x1, line.x1(), 1e-9f);
        assertEquals(y1, line.y1(), 1e-9f);
        assertEquals(x2, line.x2(), 1e-9f);
        assertEquals(y2, line.y2(), 1e-9f);
    }
}
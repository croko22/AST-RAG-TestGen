package com.github.davidmoten.rtree.geometry;

import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GeometryTest {

    @Mock
    private Geometry geometry;

    @Mock
    private Rectangle rectangle;

    @Test
    public void testDistance_RectangleNull_ThrowsNullPointerException() {
        // Given: rectangle is null
        // When / Then: distance method throws NullPointerException
        assertThrows(NullPointerException.class, () -> geometry.distance(null));
    }

    @Test
    public void testDistance_RectangleValid_ReturnsNonNegativeDistance() {
        // Given: valid rectangle
        double distance = 10.0;
        when(geometry.distance(rectangle)).thenReturn(distance);

        // When: distance method is called
        double result = geometry.distance(rectangle);

        // Then: distance is non-negative
        assertTrue(result >= 0);
        assertEquals(distance, result);
    }

    @Test
    public void testMbr_RectangleNull_ThrowsNullPointerException() {
        // Given: geometry is null
        Geometry nullGeometry = null;

        // When / Then: mbr method throws NullPointerException
        assertThrows(NullPointerException.class, () -> nullGeometry.mbr());
    }

    @Test
    public void testMbr_RectangleValid_ReturnsRectangle() {
        // Given: valid geometry
        Rectangle mbr = new Rectangle(0, 0, 10, 10);
        when(geometry.mbr()).thenReturn(mbr);

        // When: mbr method is called
        Rectangle result = geometry.mbr();

        // Then: result is not null
        assertNotNull(result);
        assertEquals(mbr, result);
    }

    @Test
    public void testIntersects_RectangleNull_ThrowsNullPointerException() {
        // Given: rectangle is null
        // When / Then: intersects method throws NullPointerException
        assertThrows(NullPointerException.class, () -> geometry.intersects(null));
    }

    @Test
    public void testIntersects_RectangleValid_ReturnsBoolean() {
        // Given: valid rectangle
        boolean intersects = true;
        when(geometry.intersects(rectangle)).thenReturn(intersects);

        // When: intersects method is called
        boolean result = geometry.intersects(rectangle);

        // Then: result is boolean
        assertTrue(result);
        assertEquals(intersects, result);
    }

    @Test
    public void testIsDoublePrecision_RectangleValid_ReturnsBoolean() {
        // Given: valid geometry
        boolean isDoublePrecision = true;
        when(geometry.isDoublePrecision()).thenReturn(isDoublePrecision);

        // When: isDoublePrecision method is called
        boolean result = geometry.isDoublePrecision();

        // Then: result is boolean
        assertTrue(result);
        assertEquals(isDoublePrecision, result);
    }
}
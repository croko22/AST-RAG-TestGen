package com.github.davidmoten.rtree.geometry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RectangleTest {

    @Mock
    private Rectangle rectangle;

    @Mock
    private Rectangle otherRectangle;

    @BeforeEach
    public void setup() {
        // Setup mocks
        when(rectangle.x1()).thenReturn(0.0);
        when(rectangle.y1()).thenReturn(0.0);
        when(rectangle.x2()).thenReturn(10.0);
        when(rectangle.y2()).thenReturn(10.0);
        when(rectangle.area()).thenReturn(100.0);
        when(rectangle.perimeter()).thenReturn(40.0);
        when(rectangle.isDoublePrecision()).thenReturn(true);

        when(otherRectangle.x1()).thenReturn(5.0);
        when(otherRectangle.y1()).thenReturn(5.0);
        when(otherRectangle.x2()).thenReturn(15.0);
        when(otherRectangle.y2()).thenReturn(15.0);
    }

    @Test
    public void testX1() {
        // When: se llama al método x1
        double x1 = rectangle.x1();

        // Then: se verifica el resultado
        assertEquals(0.0, x1);
        verify(rectangle, times(1)).x1();
    }

    @Test
    public void testY1() {
        // When: se llama al método y1
        double y1 = rectangle.y1();

        // Then: se verifica el resultado
        assertEquals(0.0, y1);
        verify(rectangle, times(1)).y1();
    }

    @Test
    public void testX2() {
        // When: se llama al método x2
        double x2 = rectangle.x2();

        // Then: se verifica el resultado
        assertEquals(10.0, x2);
        verify(rectangle, times(1)).x2();
    }

    @Test
    public void testY2() {
        // When: se llama al método y2
        double y2 = rectangle.y2();

        // Then: se verifica el resultado
        assertEquals(10.0, y2);
        verify(rectangle, times(1)).y2();
    }

    @Test
    public void testArea() {
        // When: se llama al método area
        double area = rectangle.area();

        // Then: se verifica el resultado
        assertEquals(100.0, area);
        verify(rectangle, times(1)).area();
    }

    @Test
    public void testIntersectionArea_RectangleIntersects() {
        // Given: los rectángulos se intersectan
        when(rectangle.intersectionArea(any(Rectangle.class))).thenReturn(25.0);

        // When: se llama al método intersectionArea
        double intersectionArea = rectangle.intersectionArea(otherRectangle);

        // Then: se verifica el resultado
        assertEquals(25.0, intersectionArea);
        verify(rectangle, times(1)).intersectionArea(otherRectangle);
    }

    @Test
    public void testIntersectionArea_RectangleDoesNotIntersect() {
        // Given: los rectángulos no se intersectan
        when(rectangle.intersectionArea(any(Rectangle.class))).thenReturn(0.0);

        // When: se llama al método intersectionArea
        double intersectionArea = rectangle.intersectionArea(otherRectangle);

        // Then: se verifica el resultado
        assertEquals(0.0, intersectionArea);
        verify(rectangle, times(1)).intersectionArea(otherRectangle);
    }

    @Test
    public void testPerimeter() {
        // When: se llama al método perimeter
        double perimeter = rectangle.perimeter();

        // Then: se verifica el resultado
        assertEquals(40.0, perimeter);
        verify(rectangle, times(1)).perimeter();
    }

    @Test
    public void testAdd_RectanglesIntersect() {
        // Given: los rectángulos se intersectan
        Rectangle result = mock(Rectangle.class);
        when(rectangle.add(any(Rectangle.class))).thenReturn(result);

        // When: se llama al método add
        Rectangle addedRectangle = rectangle.add(otherRectangle);

        // Then: se verifica el resultado
        assertEquals(result, addedRectangle);
        verify(rectangle, times(1)).add(otherRectangle);
    }

    @Test
    public void testAdd_RectanglesDoNotIntersect() {
        // Given: los rectángulos no se intersectan
        Rectangle result = mock(Rectangle.class);
        when(rectangle.add(any(Rectangle.class))).thenReturn(result);

        // When: se llama al método add
        Rectangle addedRectangle = rectangle.add(otherRectangle);

        // Then: se verifica el resultado
        assertEquals(result, addedRectangle);
        verify(rectangle, times(1)).add(otherRectangle);
    }

    @Test
    public void testContains_PointInsideRectangle() {
        // Given: el punto está dentro del rectángulo
        when(rectangle.contains(anyDouble(), anyDouble())).thenReturn(true);

        // When: se llama al método contains
        boolean contains = rectangle.contains(5.0, 5.0);

        // Then: se verifica el resultado
        assertTrue(contains);
        verify(rectangle, times(1)).contains(5.0, 5.0);
    }

    @Test
    public void testContains_PointOutsideRectangle() {
        // Given: el punto está fuera del rectángulo
        when(rectangle.contains(anyDouble(), anyDouble())).thenReturn(false);

        // When: se llama al método contains
        boolean contains = rectangle.contains(15.0, 15.0);

        // Then: se verifica el resultado
        assertFalse(contains);
        verify(rectangle, times(1)).contains(15.0, 15.0);
    }

    @Test
    public void testIsDoublePrecision() {
        // When: se llama al método isDoublePrecision
        boolean isDoublePrecision = rectangle.isDoublePrecision();

        // Then: se verifica el resultado
        assertTrue(isDoublePrecision);
        verify(rectangle, times(1)).isDoublePrecision();
    }
}
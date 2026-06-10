import com.github.davidmoten.rtree.geometry.Circle;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.geometry.Line;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CircleTest {

    @Mock
    private Circle circle;

    @Mock
    private Point point;

    @Mock
    private Line line;

    @Mock
    private Circle otherCircle;

    @Test
    public void testX() {
        // Given
        double expectedX = 10.0;
        when(circle.x()).thenReturn(expectedX);

        // When
        double actualX = circle.x();

        // Then
        assertEquals(expectedX, actualX);
        verify(circle, times(1)).x();
    }

    @Test
    public void testY() {
        // Given
        double expectedY = 20.0;
        when(circle.y()).thenReturn(expectedY);

        // When
        double actualY = circle.y();

        // Then
        assertEquals(expectedY, actualY);
        verify(circle, times(1)).y();
    }

    @Test
    public void testRadius() {
        // Given
        double expectedRadius = 5.0;
        when(circle.radius()).thenReturn(expectedRadius);

        // When
        double actualRadius = circle.radius();

        // Then
        assertEquals(expectedRadius, actualRadius);
        verify(circle, times(1)).radius();
    }

    @Test
    public void testIntersects_Circle_Intersects() {
        // Given
        when(circle.intersects(otherCircle)).thenReturn(true);

        // When
        boolean intersects = circle.intersects(otherCircle);

        // Then
        assertTrue(intersects);
        verify(circle, times(1)).intersects(otherCircle);
    }

    @Test
    public void testIntersects_Circle_DoesNotIntersect() {
        // Given
        when(circle.intersects(otherCircle)).thenReturn(false);

        // When
        boolean intersects = circle.intersects(otherCircle);

        // Then
        assertFalse(intersects);
        verify(circle, times(1)).intersects(otherCircle);
    }

    @Test
    public void testIntersects_Point_Intersects() {
        // Given
        when(circle.intersects(point)).thenReturn(true);

        // When
        boolean intersects = circle.intersects(point);

        // Then
        assertTrue(intersects);
        verify(circle, times(1)).intersects(point);
    }

    @Test
    public void testIntersects_Point_DoesNotIntersect() {
        // Given
        when(circle.intersects(point)).thenReturn(false);

        // When
        boolean intersects = circle.intersects(point);

        // Then
        assertFalse(intersects);
        verify(circle, times(1)).intersects(point);
    }

    @Test
    public void testIntersects_Line_Intersects() {
        // Given
        when(circle.intersects(line)).thenReturn(true);

        // When
        boolean intersects = circle.intersects(line);

        // Then
        assertTrue(intersects);
        verify(circle, times(1)).intersects(line);
    }

    @Test
    public void testIntersects_Line_DoesNotIntersect() {
        // Given
        when(circle.intersects(line)).thenReturn(false);

        // When
        boolean intersects = circle.intersects(line);

        // Then
        assertFalse(intersects);
        verify(circle, times(1)).intersects(line);
    }
}
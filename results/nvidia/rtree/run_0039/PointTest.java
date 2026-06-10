package com.github.davidmoten.rtree.geometry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PointTest {

    @Mock
    private Point point;

    @Test
    public void testX() {
        // Given
        double expectedX = 10.0;
        when(point.x()).thenReturn(expectedX);

        // When
        double actualX = point.x();

        // Then
        assertEquals(expectedX, actualX);
        verify(point, times(1)).x();
    }

    @Test
    public void testY() {
        // Given
        double expectedY = 20.0;
        when(point.y()).thenReturn(expectedY);

        // When
        double actualY = point.y();

        // Then
        assertEquals(expectedY, actualY);
        verify(point, times(1)).y();
    }
}
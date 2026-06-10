package com.github.davidmoten.rtree;

import com.github.davidmoten.rtree.geometry.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RectangleDepthTest {

    @Mock
    private Rectangle rectangle;

    private RectangleDepth rectangleDepth;

    @BeforeEach
    public void setup() {
        rectangleDepth = new RectangleDepth(rectangle, 1);
    }

    @Test
    public void testGetRectangle() {
        // Given: rectangle is set in the constructor
        // When: getRectangle is called
        Rectangle result = rectangleDepth.getRectangle();
        // Then: the result should be the same as the rectangle passed in the constructor
        assertEquals(rectangle, result);
    }

    @Test
    public void testGetDepth() {
        // Given: depth is set in the constructor
        // When: getDepth is called
        int result = rectangleDepth.getDepth();
        // Then: the result should be the same as the depth passed in the constructor
        assertEquals(1, result);
    }

    @Test
    public void testGetDepth_DifferentValue() {
        // Given: depth is set in the constructor with a different value
        RectangleDepth rectangleDepthDifferent = new RectangleDepth(rectangle, 5);
        // When: getDepth is called
        int result = rectangleDepthDifferent.getDepth();
        // Then: the result should be the same as the depth passed in the constructor
        assertEquals(5, result);
    }

    @Test
    public void testGetRectangle_NullRectangle() {
        // Given: rectangle is null
        assertThrows(NullPointerException.class, () -> new RectangleDepth(null, 1));
    }

    @Test
    public void testGetDepth_NegativeValue() {
        // Given: depth is set in the constructor with a negative value
        assertThrows(IllegalArgumentException.class, () -> new RectangleDepth(rectangle, -1));
    }
}
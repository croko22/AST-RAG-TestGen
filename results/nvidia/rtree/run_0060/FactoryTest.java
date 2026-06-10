package com.github.davidmoten.rtree;

import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FactoryTest {

    @Mock
    private Geometry geometry;

    @Mock
    private Rectangle rectangle;

    private Factory<String, Geometry> factory;

    @BeforeEach
    public void setup() {
        // Initialize the factory with mock dependencies
        factory = new Factory<String, Geometry>() {
            @Override
            public Entry<String, Geometry> entry(String id, Geometry geometry) {
                return null;
            }

            @Override
            public NonLeaf<String, Geometry> nonLeaf(String id, Geometry geometry, NonLeafChild... children) {
                return null;
            }

            @Override
            public Leaf<String, Geometry> leaf(String id, Geometry geometry) {
                return null;
            }
        };
    }

    @Test
    public void testEntry() {
        // Given: mock geometry and id
        String id = "test-id";
        when(geometry.mbr()).thenReturn(rectangle);

        // When: create an entry
        Entry<String, Geometry> entry = factory.entry(id, geometry);

        // Then: verify the result and interactions with mocks
        assertNotNull(entry);
        verify(geometry, times(1)).mbr();
    }

    @Test
    public void testNonLeaf() {
        // Given: mock geometry, id, and children
        String id = "test-id";
        NonLeafChild[] children = new NonLeafChild[0];
        when(geometry.mbr()).thenReturn(rectangle);

        // When: create a non-leaf node
        NonLeaf<String, Geometry> nonLeaf = factory.nonLeaf(id, geometry, children);

        // Then: verify the result and interactions with mocks
        assertNotNull(nonLeaf);
        verify(geometry, times(1)).mbr();
    }

    @Test
    public void testLeaf() {
        // Given: mock geometry and id
        String id = "test-id";
        when(geometry.mbr()).thenReturn(rectangle);

        // When: create a leaf node
        Leaf<String, Geometry> leaf = factory.leaf(id, geometry);

        // Then: verify the result and interactions with mocks
        assertNotNull(leaf);
        verify(geometry, times(1)).mbr();
    }

    @Test
    public void testGeometryDistance() {
        // Given: mock geometry and rectangle
        double distance = 10.0;
        when(geometry.distance(any(Rectangle.class))).thenReturn(distance);

        // When: calculate the distance
        double result = geometry.distance(rectangle);

        // Then: verify the result and interactions with mocks
        assertEquals(distance, result);
        verify(geometry, times(1)).distance(any(Rectangle.class));
    }

    @Test
    public void testGeometryMbr() {
        // Given: mock geometry and rectangle
        when(geometry.mbr()).thenReturn(rectangle);

        // When: get the mbr
        Rectangle result = geometry.mbr();

        // Then: verify the result and interactions with mocks
        assertEquals(rectangle, result);
        verify(geometry, times(1)).mbr();
    }

    @Test
    public void testGeometryIntersects() {
        // Given: mock geometry and rectangle
        boolean intersects = true;
        when(geometry.intersects(any(Rectangle.class))).thenReturn(intersects);

        // When: check if the geometry intersects with the rectangle
        boolean result = geometry.intersects(rectangle);

        // Then: verify the result and interactions with mocks
        assertEquals(intersects, result);
        verify(geometry, times(1)).intersects(any(Rectangle.class));
    }

    @Test
    public void testGeometryIsDoublePrecision() {
        // Given: mock geometry
        boolean isDoublePrecision = true;
        when(geometry.isDoublePrecision()).thenReturn(isDoublePrecision);

        // When: check if the geometry is double precision
        boolean result = geometry.isDoublePrecision();

        // Then: verify the result and interactions with mocks
        assertEquals(isDoublePrecision, result);
        verify(geometry, times(1)).isDoublePrecision();
    }
}
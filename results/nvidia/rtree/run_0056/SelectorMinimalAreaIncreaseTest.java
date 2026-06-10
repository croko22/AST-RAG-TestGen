package com.github.davidmoten.rtree;

import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.internal.Comparators;
import com.github.davidmoten.rtree.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SelectorMinimalAreaIncreaseTest {

    @Mock
    private Geometry geometry;

    @Mock
    private Rectangle rectangle;

    private SelectorMinimalAreaIncrease selectorMinimalAreaIncrease;

    @BeforeEach
    void setup() {
        selectorMinimalAreaIncrease = new SelectorMinimalAreaIncrease();
    }

    @Test
    void testSelect_NullGeometry_ThrowsNullPointerException() {
        // Given
        Geometry geometry = null;
        List<Node<Object, Geometry>> nodes = new ArrayList<>();

        // When / Then
        assertThrows(NullPointerException.class, () -> selectorMinimalAreaIncrease.select(geometry, nodes));
    }

    @Test
    void testSelect_NullNodes_ThrowsNullPointerException() {
        // Given
        List<Node<Object, Geometry>> nodes = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> selectorMinimalAreaIncrease.select(geometry, nodes));
    }

    @Test
    void testSelect_EmptyNodes_ReturnsNull() {
        // Given
        List<Node<Object, Geometry>> nodes = new ArrayList<>();

        // When
        Node<Object, Geometry> result = selectorMinimalAreaIncrease.select(geometry, nodes);

        // Then
        assertNull(result);
    }

    @Test
    void testSelect_SingleNode_ReturnsNode() {
        // Given
        Node<Object, Geometry> node = mock(Node.class);
        List<Node<Object, Geometry>> nodes = new ArrayList<>();
        nodes.add(node);

        // When
        Node<Object, Geometry> result = selectorMinimalAreaIncrease.select(geometry, nodes);

        // Then
        assertEquals(node, result);
    }

    @Test
    void testSelect_MultipleNodes_ReturnsNodeWithMinimalAreaIncrease() {
        // Given
        Node<Object, Geometry> node1 = mock(Node.class);
        Node<Object, Geometry> node2 = mock(Node.class);
        List<Node<Object, Geometry>> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);

        // When
        when(Comparators.areaIncreaseThenAreaComparator(any(Rectangle.class))).thenReturn((o1, o2) -> 0);
        Node<Object, Geometry> result = selectorMinimalAreaIncrease.select(geometry, nodes);

        // Then
        assertNotNull(result);
        assertTrue(result.equals(node1) || result.equals(node2));
    }

    @Test
    void testSelect_MultipleNodes_ComparatorReturnsNegativeValue_ReturnsFirstNode() {
        // Given
        Node<Object, Geometry> node1 = mock(Node.class);
        Node<Object, Geometry> node2 = mock(Node.class);
        List<Node<Object, Geometry>> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);

        // When
        when(Comparators.areaIncreaseThenAreaComparator(any(Rectangle.class))).thenReturn((o1, o2) -> -1);
        Node<Object, Geometry> result = selectorMinimalAreaIncrease.select(geometry, nodes);

        // Then
        assertEquals(node1, result);
    }

    @Test
    void testSelect_MultipleNodes_ComparatorReturnsPositiveValue_ReturnsSecondNode() {
        // Given
        Node<Object, Geometry> node1 = mock(Node.class);
        Node<Object, Geometry> node2 = mock(Node.class);
        List<Node<Object, Geometry>> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);

        // When
        when(Comparators.areaIncreaseThenAreaComparator(any(Rectangle.class))).thenReturn((o1, o2) -> 1);
        Node<Object, Geometry> result = selectorMinimalAreaIncrease.select(geometry, nodes);

        // Then
        assertEquals(node2, result);
    }
}
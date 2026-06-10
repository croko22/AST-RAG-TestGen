package com.github.davidmoten.rtree;

import com.github.davidmoten.guavamini.Preconditions;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NodePositionTest {

    @Mock
    private Node<Object, Geometry> node;

    @Mock
    private Geometry geometry;

    private NodePosition<Object, Geometry> nodePosition;

    @BeforeEach
    public void setup() {
        nodePosition = new NodePosition<>(node, 0);
    }

    @Test
    public void testNodePosition_Constructor() {
        // Given: node and position
        Node<Object, Geometry> node = mock(Node.class);
        int position = 10;

        // When: create NodePosition
        NodePosition<Object, Geometry> nodePosition = new NodePosition<>(node, position);

        // Then: verify node and position
        assertEquals(node, nodePosition.node());
        assertEquals(position, nodePosition.position());
    }

    @Test
    public void testNodePosition_Constructor_NullNode() {
        // Given: null node
        Node<Object, Geometry> node = null;
        int position = 10;

        // When / Then: expect NullPointerException
        assertThrows(NullPointerException.class, () -> new NodePosition<>(node, position));
    }

    @Test
    public void testNodePosition_Node() {
        // Given: node
        Node<Object, Geometry> node = mock(Node.class);

        // When: create NodePosition
        NodePosition<Object, Geometry> nodePosition = new NodePosition<>(node, 0);

        // Then: verify node
        assertEquals(node, nodePosition.node());
    }

    @Test
    public void testNodePosition_Position() {
        // Given: position
        int position = 10;

        // When: create NodePosition
        NodePosition<Object, Geometry> nodePosition = new NodePosition<>(node, position);

        // Then: verify position
        assertEquals(position, nodePosition.position());
    }

    @Test
    public void testNodePosition_NextPosition() {
        // Given: node and position
        Node<Object, Geometry> node = mock(Node.class);
        int position = 10;

        // When: create NodePosition and next position
        NodePosition<Object, Geometry> nodePosition = new NodePosition<>(node, position);
        NodePosition<Object, Geometry> nextPosition = nodePosition.nextPosition();

        // Then: verify next position
        assertEquals(node, nextPosition.node());
        assertEquals(position + 1, nextPosition.position());
    }

    @Test
    public void testNodePosition_ToString() {
        // Given: node and position
        Node<Object, Geometry> node = mock(Node.class);
        int position = 10;

        // When: create NodePosition and convert to string
        NodePosition<Object, Geometry> nodePosition = new NodePosition<>(node, position);
        String toString = nodePosition.toString();

        // Then: verify string representation
        assertTrue(toString.contains("NodePosition [node=" + node + ", position=" + position + "]"));
    }
}
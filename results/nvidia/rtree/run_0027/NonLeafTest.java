package com.github.davidmoten.rtree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NonLeafTest {

    @Mock
    private Node<String, Geometry> childNode;

    @Mock
    private Geometry geometry;

    private NonLeaf<String, Geometry> nonLeaf;

    @BeforeEach
    public void setup() {
        nonLeaf = new NonLeaf<String, Geometry>() {
            @Override
            public Node<String, Geometry> child(int i) {
                return childNode;
            }

            @Override
            public List<Node<String, Geometry>> children() {
                List<Node<String, Geometry>> children = new ArrayList<>();
                children.add(childNode);
                return children;
            }
        };
    }

    @Test
    public void testChild() {
        // Given
        when(childNode.equals(any())).thenReturn(true);

        // When
        Node<String, Geometry> result = nonLeaf.child(0);

        // Then
        assertEquals(childNode, result);
        verify(childNode, times(1)).equals(any());
    }

    @Test
    public void testChildren() {
        // Given
        List<Node<String, Geometry>> expectedChildren = new ArrayList<>();
        expectedChildren.add(childNode);

        // When
        List<Node<String, Geometry>> result = nonLeaf.children();

        // Then
        assertEquals(expectedChildren, result);
    }

    @Test
    public void testChild_IndexOutOfBoundsException() {
        // Given
        assertThrows(IndexOutOfBoundsException.class, () -> nonLeaf.child(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> nonLeaf.child(1));
    }

    @Test
    public void testChildren_Null() {
        // Given
        NonLeaf<String, Geometry> nonLeafNull = new NonLeaf<String, Geometry>() {
            @Override
            public Node<String, Geometry> child(int i) {
                return null;
            }

            @Override
            public List<Node<String, Geometry>> children() {
                return null;
            }
        };

        // When and Then
        assertThrows(NullPointerException.class, () -> nonLeafNull.child(0));
        assertThrows(NullPointerException.class, () -> nonLeafNull.children());
    }
}
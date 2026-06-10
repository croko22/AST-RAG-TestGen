import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.Nodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NodesTest {

    @Mock
    private Node node1;

    @Mock
    private Node node2;

    @Mock
    private Node node3;

    private Nodes<Node> nodes;

    @BeforeEach
    void setup() {
        nodes = new Nodes<>();
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
    }

    @Test
    void testClone() {
        // When
        Nodes<Node> clonedNodes = nodes.clone();

        // Then
        assertEquals(nodes.size(), clonedNodes.size());
        assertNotSame(nodes, clonedNodes);
    }

    @Test
    void testAsList() {
        // When
        List<Node> nodeList = nodes.asList();

        // Then
        assertEquals(nodes.size(), nodeList.size());
        assertNotSame(nodes, nodeList);
    }

    @Test
    void testRemove() {
        // Given
        doNothing().when(node1).remove();
        doNothing().when(node2).remove();
        doNothing().when(node3).remove();

        // When
        nodes.remove();

        // Then
        verify(node1).remove();
        verify(node2).remove();
        verify(node3).remove();
    }

    @Test
    void testOuterHtml() {
        // Given
        when(node1.outerHtml()).thenReturn("node1Html");
        when(node2.outerHtml()).thenReturn("node2Html");
        when(node3.outerHtml()).thenReturn("node3Html");

        // When
        String outerHtml = nodes.outerHtml();

        // Then
        assertEquals("node1Html\nnode2Html\nnode3Html", outerHtml);
    }

    @Test
    void testToString() {
        // Given
        when(node1.outerHtml()).thenReturn("node1Html");
        when(node2.outerHtml()).thenReturn("node2Html");
        when(node3.outerHtml()).thenReturn("node3Html");

        // When
        String toString = nodes.toString();

        // Then
        assertEquals("node1Html\nnode2Html\nnode3Html", toString);
    }

    @Test
    void testBefore() {
        // Given
        doNothing().when(node1).before(anyString());
        doNothing().when(node2).before(anyString());
        doNothing().when(node3).before(anyString());

        // When
        nodes.before("html");

        // Then
        verify(node1).before("html");
        verify(node2).before("html");
        verify(node3).before("html");
    }

    @Test
    void testAfter() {
        // Given
        doNothing().when(node1).after(anyString());
        doNothing().when(node2).after(anyString());
        doNothing().when(node3).after(anyString());

        // When
        nodes.after("html");

        // Then
        verify(node1).after("html");
        verify(node2).after("html");
        verify(node3).after("html");
    }

    @Test
    void testWrap() {
        // Given
        doNothing().when(node1).wrap(anyString());
        doNothing().when(node2).wrap(anyString());
        doNothing().when(node3).wrap(anyString());

        // When
        nodes.wrap("html");

        // Then
        verify(node1).wrap("html");
        verify(node2).wrap("html");
        verify(node3).wrap("html");
    }

    @Test
    void testFirst() {
        // When
        Node firstNode = nodes.first();

        // Then
        assertSame(node1, firstNode);
    }

    @Test
    void testLast() {
        // When
        Node lastNode = nodes.last();

        // Then
        assertSame(node3, lastNode);
    }

    @Test
    void testSet() {
        // Given
        Node newNode = mock(Node.class);
        doNothing().when(node1).replaceWith(any(Node.class));

        // When
        Node oldNode = nodes.set(0, newNode);

        // Then
        assertSame(node1, oldNode);
        verify(node1).replaceWith(newNode);
    }

    @Test
    void testRemoveInt() {
        // Given
        doNothing().when(node1).remove();

        // When
        Node removedNode = nodes.remove(0);

        // Then
        assertSame(node1, removedNode);
        verify(node1).remove();
    }

    @Test
    void testRemoveObject() {
        // Given
        doNothing().when(node1).remove();

        // When
        boolean removed = nodes.remove(node1);

        // Then
        assertTrue(removed);
        verify(node1).remove();
    }

    @Test
    void testDeselectInt() {
        // When
        Node deselectedNode = nodes.deselect(0);

        // Then
        assertSame(node1, deselectedNode);
    }

    @Test
    void testDeselectObject() {
        // When
        boolean deselected = nodes.deselect(node1);

        // Then
        assertTrue(deselected);
    }

    @Test
    void testClear() {
        // Given
        doNothing().when(node1).remove();
        doNothing().when(node2).remove();
        doNothing().when(node3).remove();

        // When
        nodes.clear();

        // Then
        verify(node1).remove();
        verify(node2).remove();
        verify(node3).remove();
        assertTrue(nodes.isEmpty());
    }

    @Test
    void testDeselectAll() {
        // When
        nodes.deselectAll();

        // Then
        assertTrue(nodes.isEmpty());
    }

    @Test
    void testRemoveAll() {
        // Given
        Collection<Node> nodesToRemove = Arrays.asList(node1, node2);

        // When
        boolean removed = nodes.removeAll(nodesToRemove);

        // Then
        assertTrue(removed);
        assertEquals(1, nodes.size());
        assertSame(node3, nodes.get(0));
    }

    @Test
    void testRetainAll() {
        // Given
        Collection<Node> nodesToRetain = Arrays.asList(node1, node3);

        // When
        boolean removed = nodes.retainAll(nodesToRetain);

        // Then
        assertTrue(removed);
        assertEquals(2, nodes.size());
        assertSame(node1, nodes.get(0));
        assertSame(node3, nodes.get(1));
    }

    @Test
    void testRemoveIf() {
        // Given
        Predicate<Node> predicate = node -> node == node1 || node == node2;

        // When
        boolean removed = nodes.removeIf(predicate);

        // Then
        assertTrue(removed);
        assertEquals(1, nodes.size());
        assertSame(node3, nodes.get(0));
    }

    @Test
    void testReplaceAll() {
        // Given
        UnaryOperator<Node> operator = node -> mock(Node.class);

        // When
        nodes.replaceAll(operator);

        // Then
        for (Node node : nodes) {
            assertNotSame(node1, node);
            assertNotSame(node2, node);
            assertNotSame(node3, node);
        }
    }
}
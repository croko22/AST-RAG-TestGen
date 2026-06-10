import org.apache.commons.collections4.iterators.NodeListIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NodeListIteratorTest {

    @Mock
    private NodeList nodeList;

    @Mock
    private Node node;

    private NodeListIterator nodeListIterator;

    @BeforeEach
    public void setup() {
        nodeListIterator = new NodeListIterator(nodeList);
    }

    @Test
    public void testConstructor_Node() {
        // Given
        Node node = mock(Node.class);
        NodeList nodeList = mock(NodeList.class);
        when(node.getChildNodes()).thenReturn(nodeList);

        // When
        NodeListIterator nodeListIterator = new NodeListIterator(node);

        // Then
        assertSame(nodeList, nodeListIterator.nodeList);
    }

    @Test
    public void testConstructor_NodeList() {
        // Given
        NodeList nodeList = mock(NodeList.class);

        // When
        NodeListIterator nodeListIterator = new NodeListIterator(nodeList);

        // Then
        assertSame(nodeList, nodeListIterator.nodeList);
    }

    @Test
    public void testHasNext_True() {
        // Given
        when(nodeList.getLength()).thenReturn(1);
        when(nodeList.item(0)).thenReturn(node);

        // When
        boolean result = nodeListIterator.hasNext();

        // Then
        assertTrue(result);
    }

    @Test
    public void testHasNext_False() {
        // Given
        when(nodeList.getLength()).thenReturn(0);

        // When
        boolean result = nodeListIterator.hasNext();

        // Then
        assertFalse(result);
    }

    @Test
    public void testNext() {
        // Given
        when(nodeList.getLength()).thenReturn(1);
        when(nodeList.item(0)).thenReturn(node);

        // When
        Node result = nodeListIterator.next();

        // Then
        assertSame(node, result);
        assertEquals(1, nodeListIterator.index);
    }

    @Test
    public void testNext_NoMoreElements() {
        // Given
        when(nodeList.getLength()).thenReturn(0);

        // When
        assertThrows(NoSuchElementException.class, () -> nodeListIterator.next());
    }

    @Test
    public void testRemove() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> nodeListIterator.remove());
    }

    @Test
    public void testConstructor_NullNode() {
        // Given
        Node node = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> new NodeListIterator(node));
    }

    @Test
    public void testConstructor_NullNodeList() {
        // Given
        NodeList nodeList = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> new NodeListIterator(nodeList));
    }
}
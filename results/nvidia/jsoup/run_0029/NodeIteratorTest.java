import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.NodeIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NodeIteratorTest {

    @Mock
    private Node node;

    private Document document;

    @BeforeEach
    void setup() {
        document = Jsoup.parse("<html><body><p>Hello World!</p></body></html>");
    }

    @Test
    void testFrom() {
        // Given
        Node startNode = document.body();

        // When
        NodeIterator<Node> iterator = NodeIterator.from(startNode);

        // Then
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
    }

    @Test
    void testRestart() {
        // Given
        Node startNode = document.body();
        NodeIterator<Node> iterator = NodeIterator.from(startNode);

        // When
        iterator.restart(document.body());

        // Then
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
    }

    @Test
    void testHasNext() {
        // Given
        Node startNode = document.body();
        NodeIterator<Node> iterator = NodeIterator.from(startNode);

        // Then
        assertTrue(iterator.hasNext());
    }

    @Test
    void testNext() {
        // Given
        Node startNode = document.body();
        NodeIterator<Node> iterator = NodeIterator.from(startNode);

        // When
        Node nextNode = iterator.next();

        // Then
        assertNotNull(nextNode);
        assertEquals(startNode, nextNode);
    }

    @Test
    void testRemove() {
        // Given
        Node startNode = document.body();
        NodeIterator<Node> iterator = NodeIterator.from(startNode);

        // When
        iterator.next();
        iterator.remove();

        // Then
        assertFalse(document.body().hasParent());
    }

    @Test
    void testHasNextAfterRemove() {
        // Given
        Node startNode = document.body();
        NodeIterator<Node> iterator = NodeIterator.from(startNode);

        // When
        iterator.next();
        iterator.remove();

        // Then
        assertFalse(iterator.hasNext());
    }

    @Test
    void testNextAfterRemove() {
        // Given
        Node startNode = document.body();
        NodeIterator<Node> iterator = NodeIterator.from(startNode);

        // When
        iterator.next();
        iterator.remove();

        // Then
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    void testRestartAfterRemove() {
        // Given
        Node startNode = document.body();
        NodeIterator<Node> iterator = NodeIterator.from(startNode);

        // When
        iterator.next();
        iterator.remove();
        iterator.restart(document.body());

        // Then
        assertTrue(iterator.hasNext());
    }

    @Test
    void testFromWithNullNode() {
        // Given
        Node startNode = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> NodeIterator.from(startNode));
    }

    @Test
    void testRestartWithNullNode() {
        // Given
        Node startNode = document.body();
        NodeIterator<Node> iterator = NodeIterator.from(startNode);

        // When and Then
        assertThrows(NullPointerException.class, () -> iterator.restart(null));
    }
}
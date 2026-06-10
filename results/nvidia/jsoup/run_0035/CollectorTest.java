import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Collector;
import org.jsoup.select.Evaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CollectorTest {

    @Mock
    private Evaluator evaluator;

    @Mock
    private Element root;

    @BeforeEach
    void setup() {
        // Setup mocks
        when(evaluator.wantsNodes()).thenReturn(true);
        when(evaluator.asPredicate(any())).thenReturn(node -> true);
        when(evaluator.asNodePredicate(any())).thenReturn(node -> true);
    }

    @Test
    void testCollect() {
        // Given
        List<Element> elements = new ArrayList<>();
        elements.add(mock(Element.class));
        elements.add(mock(Element.class));
        when(root.stream()).thenReturn(elements.stream());

        // When
        Elements collected = Collector.collect(evaluator, root);

        // Then
        assertNotNull(collected);
        assertEquals(2, collected.size());
        verify(evaluator, times(1)).reset();
    }

    @Test
    void testStream() {
        // Given
        List<Element> elements = new ArrayList<>();
        elements.add(mock(Element.class));
        elements.add(mock(Element.class));
        when(root.stream()).thenReturn(elements.stream());

        // When
        Stream<Element> stream = Collector.stream(evaluator, root);

        // Then
        assertNotNull(stream);
        assertEquals(2, stream.collect(Collectors.toList()).size());
        verify(evaluator, times(1)).reset();
    }

    @Test
    void testStreamNodes() {
        // Given
        List<Node> nodes = new ArrayList<>();
        nodes.add(mock(Node.class));
        nodes.add(mock(Node.class));
        when(root.nodeStream(any())).thenReturn(nodes.stream());

        // When
        Stream<Node> stream = Collector.streamNodes(evaluator, root, Node.class);

        // Then
        assertNotNull(stream);
        assertEquals(2, stream.collect(Collectors.toList()).size());
        verify(evaluator, times(1)).reset();
    }

    @Test
    void testFindFirst() {
        // Given
        List<Element> elements = new ArrayList<>();
        elements.add(mock(Element.class));
        elements.add(mock(Element.class));
        when(root.stream()).thenReturn(elements.stream());

        // When
        Element first = Collector.findFirst(evaluator, root);

        // Then
        assertNotNull(first);
        verify(evaluator, times(1)).reset();
    }

    @Test
    void testFindFirstNode() {
        // Given
        List<Node> nodes = new ArrayList<>();
        nodes.add(mock(Node.class));
        nodes.add(mock(Node.class));
        when(root.nodeStream(any())).thenReturn(nodes.stream());

        // When
        Node first = Collector.findFirstNode(evaluator, root, Node.class);

        // Then
        assertNotNull(first);
        verify(evaluator, times(1)).reset();
    }

    @Test
    void testCollectNodes() {
        // Given
        List<Node> nodes = new ArrayList<>();
        nodes.add(mock(Node.class));
        nodes.add(mock(Node.class));
        when(root.nodeStream(any())).thenReturn(nodes.stream());

        // When
        Nodes<Node> collected = Collector.collectNodes(evaluator, root, Node.class);

        // Then
        assertNotNull(collected);
        assertEquals(2, collected.size());
        verify(evaluator, times(1)).reset();
    }

    @Test
    void testCollectEmpty() {
        // Given
        when(root.stream()).thenReturn(Stream.empty());

        // When
        Elements collected = Collector.collect(evaluator, root);

        // Then
        assertNotNull(collected);
        assertTrue(collected.isEmpty());
        verify(evaluator, times(1)).reset();
    }

    @Test
    void testStreamEmpty() {
        // Given
        when(root.stream()).thenReturn(Stream.empty());

        // When
        Stream<Element> stream = Collector.stream(evaluator, root);

        // Then
        assertNotNull(stream);
        assertTrue(stream.collect(Collectors.toList()).isEmpty());
        verify(evaluator, times(1)).reset();
    }

    @Test
    void testStreamNodesEmpty() {
        // Given
        when(root.nodeStream(any())).thenReturn(Stream.empty());

        // When
        Stream<Node> stream = Collector.streamNodes(evaluator, root, Node.class);

        // Then
        assertNotNull(stream);
        assertTrue(stream.collect(Collectors.toList()).isEmpty());
        verify(evaluator, times(1)).reset();
    }

    @Test
    void testFindFirstEmpty() {
        // Given
        when(root.stream()).thenReturn(Stream.empty());

        // When
        Element first = Collector.findFirst(evaluator, root);

        // Then
        assertNull(first);
        verify(evaluator, times(1)).reset();
    }

    @Test
    void testFindFirstNodeEmpty() {
        // Given
        when(root.nodeStream(any())).thenReturn(Stream.empty());

        // When
        Node first = Collector.findFirstNode(evaluator, root, Node.class);

        // Then
        assertNull(first);
        verify(evaluator, times(1)).reset();
    }

    @Test
    void testCollectNodesEmpty() {
        // Given
        when(root.nodeStream(any())).thenReturn(Stream.empty());

        // When
        Nodes<Node> collected = Collector.collectNodes(evaluator, root, Node.class);

        // Then
        assertNotNull(collected);
        assertTrue(collected.isEmpty());
        verify(evaluator, times(1)).reset();
    }
}
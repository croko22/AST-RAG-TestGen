import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Evaluator;
import org.jsoup.select.StructuralEvaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StructuralEvaluatorTest {

    @Mock
    private Evaluator evaluator;

    @Mock
    private Element root;

    @Mock
    private Element element;

    private StructuralEvaluator structuralEvaluator;

    @BeforeEach
    void setup() {
        structuralEvaluator = new StructuralEvaluator.Has(evaluator);
    }

    @Test
    void testMatches_Element_Element() {
        // Given
        when(evaluator.matches(root, element)).thenReturn(true);

        // When
        boolean result = structuralEvaluator.matches(root, element);

        // Then
        assertTrue(result);
        verify(evaluator, times(1)).matches(root, element);
    }

    @Test
    void testMatches_Element_LeafNode() {
        // Given
        LeafNode leafNode = mock(LeafNode.class);
        when(evaluator.matches(root, leafNode)).thenReturn(true);

        // When
        boolean result = structuralEvaluator.matches(root, leafNode);

        // Then
        assertTrue(result);
        verify(evaluator, times(1)).matches(root, leafNode);
    }

    @Test
    void testToString() {
        // Given
        when(evaluator.toString()).thenReturn("Evaluator");

        // When
        String result = structuralEvaluator.toString();

        // Then
        assertEquals(":has(Evaluator)", result);
    }

    @Test
    void testEvaluateMatch_Element_Node() {
        // Given
        Node node = mock(Node.class);
        when(evaluator.matches(root, node)).thenReturn(true);

        // When
        boolean result = structuralEvaluator.evaluateMatch(root, node);

        // Then
        assertTrue(result);
        verify(evaluator, times(1)).matches(root, node);
    }

    @Test
    void testReset() {
        // When
        structuralEvaluator.reset();

        // Then
        verify(evaluator, times(1)).reset();
    }

    @Test
    void testCost() {
        // Given
        when(evaluator.cost()).thenReturn(10);

        // When
        int result = structuralEvaluator.cost();

        // Then
        assertEquals(10, result);
    }

    @Test
    void testWantsNodes() {
        // Given
        when(evaluator.wantsNodes()).thenReturn(true);

        // When
        boolean result = structuralEvaluator.wantsNodes();

        // Then
        assertTrue(result);
    }
}
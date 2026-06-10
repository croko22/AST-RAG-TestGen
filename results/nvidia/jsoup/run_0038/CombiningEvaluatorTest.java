import org.jsoup.nodes.Element;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CombiningEvaluatorTest {

    @Mock
    private Evaluator evaluator1;

    @Mock
    private Evaluator evaluator2;

    @Mock
    private Element root;

    @Mock
    private Element element;

    @Mock
    private LeafNode leafNode;

    private CombiningEvaluator combiningEvaluator;

    @BeforeEach
    public void setup() {
        combiningEvaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));
    }

    @Test
    public void testAddEvaluator() {
        // Given
        Evaluator newEvaluator = mock(Evaluator.class);

        // When
        combiningEvaluator.add(newEvaluator);

        // Then
        assertEquals(3, combiningEvaluator.evaluators.size());
        assertTrue(combiningEvaluator.evaluators.contains(newEvaluator));
    }

    @Test
    public void testMatchesElement() {
        // Given
        when(evaluator1.matches(any(Element.class), any(Element.class))).thenReturn(true);
        when(evaluator2.matches(any(Element.class), any(Element.class))).thenReturn(true);

        // When
        boolean result = combiningEvaluator.matches(root, element);

        // Then
        assertTrue(result);
        verify(evaluator1, times(1)).matches(root, element);
        verify(evaluator2, times(1)).matches(root, element);
    }

    @Test
    public void testMatchesElement_False() {
        // Given
        when(evaluator1.matches(any(Element.class), any(Element.class))).thenReturn(true);
        when(evaluator2.matches(any(Element.class), any(Element.class))).thenReturn(false);

        // When
        boolean result = combiningEvaluator.matches(root, element);

        // Then
        assertFalse(result);
        verify(evaluator1, times(1)).matches(root, element);
        verify(evaluator2, times(1)).matches(root, element);
    }

    @Test
    public void testMatchesLeafNode() {
        // Given
        when(evaluator1.matches(any(Element.class), any(LeafNode.class))).thenReturn(true);
        when(evaluator2.matches(any(Element.class), any(LeafNode.class))).thenReturn(true);

        // When
        boolean result = combiningEvaluator.matches(root, leafNode);

        // Then
        assertTrue(result);
        verify(evaluator1, times(1)).matches(root, leafNode);
        verify(evaluator2, times(1)).matches(root, leafNode);
    }

    @Test
    public void testMatchesLeafNode_False() {
        // Given
        when(evaluator1.matches(any(Element.class), any(LeafNode.class))).thenReturn(true);
        when(evaluator2.matches(any(Element.class), any(LeafNode.class))).thenReturn(false);

        // When
        boolean result = combiningEvaluator.matches(root, leafNode);

        // Then
        assertFalse(result);
        verify(evaluator1, times(1)).matches(root, leafNode);
        verify(evaluator2, times(1)).matches(root, leafNode);
    }

    @Test
    public void testToString() {
        // Given
        when(evaluator1.toString()).thenReturn("Evaluator1");
        when(evaluator2.toString()).thenReturn("Evaluator2");

        // When
        String result = combiningEvaluator.toString();

        // Then
        assertEquals("Evaluator1Evaluator2", result);
    }

    @Test
    public void testOrEvaluator() {
        // Given
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));

        // When
        when(evaluator1.matches(any(Element.class), any(Element.class))).thenReturn(false);
        when(evaluator2.matches(any(Element.class), any(Element.class))).thenReturn(true);

        // Then
        assertTrue(orEvaluator.matches(root, element));
        verify(evaluator1, times(1)).matches(root, element);
        verify(evaluator2, times(1)).matches(root, element);
    }
}
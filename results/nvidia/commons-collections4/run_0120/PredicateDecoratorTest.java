import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.PredicateDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PredicateDecoratorTest {

    @Mock
    private PredicateDecorator<String> predicateDecorator;

    @Mock
    private Predicate<String> predicate1;

    @Mock
    private Predicate<String> predicate2;

    @BeforeEach
    public void setup() {
        // Initialize mocks
        when(predicate1.evaluate(any())).thenReturn(true);
        when(predicate2.evaluate(any())).thenReturn(true);
    }

    @Test
    public void testGetPredicates() {
        // Given: predicate decorator with two predicates
        Predicate<String>[] predicates = new Predicate[]{predicate1, predicate2};
        when(predicateDecorator.getPredicates()).thenReturn(predicates);

        // When: get predicates from decorator
        Predicate<String>[] result = predicateDecorator.getPredicates();

        // Then: verify result and interactions
        assertNotNull(result);
        assertArrayEquals(predicates, result);
        verify(predicateDecorator, times(1)).getPredicates();
    }

    @Test
    public void testGetPredicates_Null() {
        // Given: predicate decorator with null predicates
        when(predicateDecorator.getPredicates()).thenReturn(null);

        // When: get predicates from decorator
        Predicate<String>[] result = predicateDecorator.getPredicates();

        // Then: verify result and interactions
        assertNull(result);
        verify(predicateDecorator, times(1)).getPredicates();
    }

    @Test
    public void testGetPredicates_Empty() {
        // Given: predicate decorator with empty predicates
        Predicate<String>[] predicates = new Predicate[]{};
        when(predicateDecorator.getPredicates()).thenReturn(predicates);

        // When: get predicates from decorator
        Predicate<String>[] result = predicateDecorator.getPredicates();

        // Then: verify result and interactions
        assertNotNull(result);
        assertTrue(result.length == 0);
        verify(predicateDecorator, times(1)).getPredicates();
    }

    @Test
    public void testEvaluate() {
        // Given: predicate decorator with two predicates
        Predicate<String>[] predicates = new Predicate[]{predicate1, predicate2};
        when(predicateDecorator.getPredicates()).thenReturn(predicates);

        // When: evaluate decorator with test object
        boolean result = predicateDecorator.evaluate("test");

        // Then: verify result and interactions
        assertTrue(result);
        verify(predicate1, times(1)).evaluate("test");
        verify(predicate2, times(1)).evaluate("test");
    }

    @Test
    public void testEvaluate_False() {
        // Given: predicate decorator with two predicates, one returns false
        when(predicate1.evaluate(any())).thenReturn(false);
        Predicate<String>[] predicates = new Predicate[]{predicate1, predicate2};
        when(predicateDecorator.getPredicates()).thenReturn(predicates);

        // When: evaluate decorator with test object
        boolean result = predicateDecorator.evaluate("test");

        // Then: verify result and interactions
        assertFalse(result);
        verify(predicate1, times(1)).evaluate("test");
        verify(predicate2, never()).evaluate("test");
    }

    @Test
    public void testTest() {
        // Given: predicate decorator with two predicates
        Predicate<String>[] predicates = new Predicate[]{predicate1, predicate2};
        when(predicateDecorator.getPredicates()).thenReturn(predicates);

        // When: test decorator with test object
        boolean result = predicateDecorator.test("test");

        // Then: verify result and interactions
        assertTrue(result);
        verify(predicate1, times(1)).test("test");
        verify(predicate2, times(1)).test("test");
    }

    @Test
    public void testTest_False() {
        // Given: predicate decorator with two predicates, one returns false
        when(predicate1.test(any())).thenReturn(false);
        Predicate<String>[] predicates = new Predicate[]{predicate1, predicate2};
        when(predicateDecorator.getPredicates()).thenReturn(predicates);

        // When: test decorator with test object
        boolean result = predicateDecorator.test("test");

        // Then: verify result and interactions
        assertFalse(result);
        verify(predicate1, times(1)).test("test");
        verify(predicate2, never()).test("test");
    }
}
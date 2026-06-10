import org.apache.commons.collections4.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Predicate as JavaPredicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PredicateTest {

    @Mock
    private Predicate<Object> predicate;

    @BeforeEach
    void setup() {
        // No setup required for this test class
    }

    @Test
    public void testEvaluate() {
        // Given: a predicate that returns true for a given object
        when(predicate.evaluate(any())).thenReturn(true);

        // When: the evaluate method is called
        boolean result = predicate.evaluate(new Object());

        // Then: the result should be true
        assertTrue(result);

        // Verify: the evaluate method was called once
        verify(predicate, times(1)).evaluate(any());
    }

    @Test
    public void testEvaluate_False() {
        // Given: a predicate that returns false for a given object
        when(predicate.evaluate(any())).thenReturn(false);

        // When: the evaluate method is called
        boolean result = predicate.evaluate(new Object());

        // Then: the result should be false
        assertFalse(result);

        // Verify: the evaluate method was called once
        verify(predicate, times(1)).evaluate(any());
    }

    @Test
    public void testEvaluate_ThrowsClassCastException() {
        // Given: a predicate that throws a ClassCastException
        when(predicate.evaluate(any())).thenThrow(ClassCastException.class);

        // When / Then: the evaluate method throws a ClassCastException
        assertThrows(ClassCastException.class, () -> predicate.evaluate(new Object()));

        // Verify: the evaluate method was called once
        verify(predicate, times(1)).evaluate(any());
    }

    @Test
    public void testEvaluate_ThrowsIllegalArgumentException() {
        // Given: a predicate that throws an IllegalArgumentException
        when(predicate.evaluate(any())).thenThrow(IllegalArgumentException.class);

        // When / Then: the evaluate method throws an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> predicate.evaluate(new Object()));

        // Verify: the evaluate method was called once
        verify(predicate, times(1)).evaluate(any());
    }

    @Test
    public void testTest() {
        // Given: a predicate that returns true for a given object
        when(predicate.test(any())).thenReturn(true);

        // When: the test method is called
        boolean result = predicate.test(new Object());

        // Then: the result should be true
        assertTrue(result);

        // Verify: the test method was called once
        verify(predicate, times(1)).test(any());
    }

    @Test
    public void testTest_False() {
        // Given: a predicate that returns false for a given object
        when(predicate.test(any())).thenReturn(false);

        // When: the test method is called
        boolean result = predicate.test(new Object());

        // Then: the result should be false
        assertFalse(result);

        // Verify: the test method was called once
        verify(predicate, times(1)).test(any());
    }

    @Test
    public void testTest_ThrowsClassCastException() {
        // Given: a predicate that throws a ClassCastException
        when(predicate.test(any())).thenThrow(ClassCastException.class);

        // When / Then: the test method throws a ClassCastException
        assertThrows(ClassCastException.class, () -> predicate.test(new Object()));

        // Verify: the test method was called once
        verify(predicate, times(1)).test(any());
    }

    @Test
    public void testTest_ThrowsIllegalArgumentException() {
        // Given: a predicate that throws an IllegalArgumentException
        when(predicate.test(any())).thenThrow(IllegalArgumentException.class);

        // When / Then: the test method throws an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> predicate.test(new Object()));

        // Verify: the test method was called once
        verify(predicate, times(1)).test(any());
    }
}
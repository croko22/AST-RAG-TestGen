import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NullIsTruePredicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Predicate as JavaPredicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NullIsTruePredicateTest {

    @Mock
    private Predicate<Object> predicate;

    @Test
    public void testNullIsTruePredicate() {
        // Given: a predicate
        Predicate<Object> nullIsTruePredicate = NullIsTruePredicate.nullIsTruePredicate(predicate);

        // When: the nullIsTruePredicate is created
        // Then: the predicate is not null
        assertNotNull(nullIsTruePredicate);
    }

    @Test
    public void testNullIsTruePredicate_NullPredicate_ThrowsNullPointerException() {
        // Given: a null predicate
        Predicate<Object> nullPredicate = null;

        // When / Then: creating the nullIsTruePredicate with a null predicate throws a NullPointerException
        assertThrows(NullPointerException.class, () -> NullIsTruePredicate.nullIsTruePredicate(nullPredicate));
    }

    @Test
    public void testTest_NullObject_ReturnsTrue() {
        // Given: a null object and a predicate
        Object nullObject = null;
        Predicate<Object> nullIsTruePredicate = NullIsTruePredicate.nullIsTruePredicate(predicate);

        // When: the test method is called with a null object
        boolean result = nullIsTruePredicate.test(nullObject);

        // Then: the result is true
        assertTrue(result);
        verify(predicate, never()).test(any());
    }

    @Test
    public void testTest_NonNullObject_ReturnsTrue_WhenPredicateReturnsTrue() {
        // Given: a non-null object and a predicate that returns true
        Object nonNullObject = new Object();
        Predicate<Object> nullIsTruePredicate = NullIsTruePredicate.nullIsTruePredicate(predicate);
        when(predicate.test(nonNullObject)).thenReturn(true);

        // When: the test method is called with a non-null object
        boolean result = nullIsTruePredicate.test(nonNullObject);

        // Then: the result is true
        assertTrue(result);
        verify(predicate, times(1)).test(nonNullObject);
    }

    @Test
    public void testTest_NonNullObject_ReturnsFalse_WhenPredicateReturnsFalse() {
        // Given: a non-null object and a predicate that returns false
        Object nonNullObject = new Object();
        Predicate<Object> nullIsTruePredicate = NullIsTruePredicate.nullIsTruePredicate(predicate);
        when(predicate.test(nonNullObject)).thenReturn(false);

        // When: the test method is called with a non-null object
        boolean result = nullIsTruePredicate.test(nonNullObject);

        // Then: the result is false
        assertFalse(result);
        verify(predicate, times(1)).test(nonNullObject);
    }
}
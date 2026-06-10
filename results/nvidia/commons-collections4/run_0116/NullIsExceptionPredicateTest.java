import org.apache.commons.collections4.FunctorException;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NullIsExceptionPredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NullIsExceptionPredicateTest {

    @Mock
    private Predicate<Object> predicate;

    private NullIsExceptionPredicate<Object> nullIsExceptionPredicate;

    @BeforeEach
    void setup() {
        nullIsExceptionPredicate = new NullIsExceptionPredicate<>(predicate);
    }

    @Test
    public void testNullIsExceptionPredicate_Success() {
        // Given: a valid predicate
        Predicate<Object> result = NullIsExceptionPredicate.nullIsExceptionPredicate(predicate);
        // Then: the result is not null
        assertNotNull(result);
    }

    @Test
    public void testNullIsExceptionPredicate_NullPredicate_ThrowsNullPointerException() {
        // Given: a null predicate
        Predicate<Object> nullPredicate = null;
        // When / Then: a NullPointerException is thrown
        assertThrows(NullPointerException.class, () -> NullIsExceptionPredicate.nullIsExceptionPredicate(nullPredicate));
    }

    @Test
    public void testTest_NullObject_ThrowsFunctorException() {
        // Given: a null object
        Object nullObject = null;
        // When / Then: a FunctorException is thrown
        assertThrows(FunctorException.class, () -> nullIsExceptionPredicate.test(nullObject));
    }

    @Test
    public void testTest_NonNullObject_ReturnsTrue() {
        // Given: a non-null object
        Object nonNullObject = new Object();
        // And: the predicate returns true
        when(predicate.test(any())).thenReturn(true);
        // When: the test method is called
        boolean result = nullIsExceptionPredicate.test(nonNullObject);
        // Then: the result is true
        assertTrue(result);
        // And: the predicate is called once
        verify(predicate, times(1)).test(nonNullObject);
    }

    @Test
    public void testTest_NonNullObject_ReturnsFalse() {
        // Given: a non-null object
        Object nonNullObject = new Object();
        // And: the predicate returns false
        when(predicate.test(any())).thenReturn(false);
        // When: the test method is called
        boolean result = nullIsExceptionPredicate.test(nonNullObject);
        // Then: the result is false
        assertFalse(result);
        // And: the predicate is called once
        verify(predicate, times(1)).test(nonNullObject);
    }

    @Test
    public void testGetPredicates_ReturnsArrayWithPredicate() {
        // When: the getPredicates method is called
        Predicate<?>[] result = nullIsExceptionPredicate.getPredicates();
        // Then: the result is an array with the predicate
        assertNotNull(result);
        assertEquals(1, result.length);
        assertSame(predicate, result[0]);
    }
}
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NullIsFalsePredicate;
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
public class NullIsFalsePredicateTest {

    @Mock
    private Predicate<Object> predicate;

    private NullIsFalsePredicate<Object> nullIsFalsePredicate;

    @BeforeEach
    void setup() {
        nullIsFalsePredicate = new NullIsFalsePredicate<>(predicate);
    }

    @Test
    public void testNullIsFalsePredicate() {
        // Given
        Predicate<Object> predicateToDecorate = mock(Predicate.class);
        // When
        NullIsFalsePredicate<Object> nullIsFalsePredicate = NullIsFalsePredicate.nullIsFalsePredicate(predicateToDecorate);
        // Then
        assertNotNull(nullIsFalsePredicate);
    }

    @Test
    public void testNullIsFalsePredicate_NullPredicate() {
        // Given
        Predicate<Object> predicateToDecorate = null;
        // When and Then
        assertThrows(NullPointerException.class, () -> NullIsFalsePredicate.nullIsFalsePredicate(predicateToDecorate));
    }

    @Test
    public void testTest_NullObject() {
        // Given
        Object object = null;
        // When
        boolean result = nullIsFalsePredicate.test(object);
        // Then
        assertFalse(result);
        verify(predicate, never()).test(any());
    }

    @Test
    public void testTest_NonNullObject_PredicateReturnsTrue() {
        // Given
        Object object = new Object();
        when(predicate.test(object)).thenReturn(true);
        // When
        boolean result = nullIsFalsePredicate.test(object);
        // Then
        assertTrue(result);
        verify(predicate, times(1)).test(object);
    }

    @Test
    public void testTest_NonNullObject_PredicateReturnsFalse() {
        // Given
        Object object = new Object();
        when(predicate.test(object)).thenReturn(false);
        // When
        boolean result = nullIsFalsePredicate.test(object);
        // Then
        assertFalse(result);
        verify(predicate, times(1)).test(object);
    }

    @Test
    public void testGetPredicates() {
        // When
        Predicate<?>[] predicates = nullIsFalsePredicate.getPredicates();
        // Then
        assertNotNull(predicates);
        assertEquals(1, predicates.length);
        assertSame(predicate, predicates[0]);
    }
}
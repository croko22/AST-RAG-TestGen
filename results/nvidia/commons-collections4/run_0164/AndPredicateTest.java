import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.AndPredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AndPredicateTest {

    @Mock
    private Predicate<Object> predicate1;

    @Mock
    private Predicate<Object> predicate2;

    private AndPredicate<Object> andPredicate;

    @BeforeEach
    public void setup() {
        andPredicate = new AndPredicate<>(predicate1, predicate2);
    }

    @Test
    public void testAndPredicate_Success() {
        // Given: both predicates return true
        when(predicate1.test(any())).thenReturn(true);
        when(predicate2.test(any())).thenReturn(true);

        // When: evaluate the and predicate
        boolean result = andPredicate.test("test");

        // Then: the result is true
        assertTrue(result);
        verify(predicate1, times(1)).test("test");
        verify(predicate2, times(1)).test("test");
    }

    @Test
    public void testAndPredicate_FirstPredicateFails() {
        // Given: the first predicate returns false
        when(predicate1.test(any())).thenReturn(false);
        when(predicate2.test(any())).thenReturn(true);

        // When: evaluate the and predicate
        boolean result = andPredicate.test("test");

        // Then: the result is false
        assertFalse(result);
        verify(predicate1, times(1)).test("test");
        verify(predicate2, never()).test("test");
    }

    @Test
    public void testAndPredicate_SecondPredicateFails() {
        // Given: the second predicate returns false
        when(predicate1.test(any())).thenReturn(true);
        when(predicate2.test(any())).thenReturn(false);

        // When: evaluate the and predicate
        boolean result = andPredicate.test("test");

        // Then: the result is false
        assertFalse(result);
        verify(predicate1, times(1)).test("test");
        verify(predicate2, times(1)).test("test");
    }

    @Test
    public void testAndPredicate_BothPredicatesFail() {
        // Given: both predicates return false
        when(predicate1.test(any())).thenReturn(false);
        when(predicate2.test(any())).thenReturn(false);

        // When: evaluate the and predicate
        boolean result = andPredicate.test("test");

        // Then: the result is false
        assertFalse(result);
        verify(predicate1, times(1)).test("test");
        verify(predicate2, never()).test("test");
    }

    @Test
    public void testAndPredicate_NullPredicate1() {
        // Given: the first predicate is null
        assertThrows(NullPointerException.class, () -> AndPredicate.andPredicate(null, predicate2));
    }

    @Test
    public void testAndPredicate_NullPredicate2() {
        // Given: the second predicate is null
        assertThrows(NullPointerException.class, () -> AndPredicate.andPredicate(predicate1, null));
    }

    @Test
    public void testGetPredicates() {
        // When: get the predicates
        Predicate<?>[] predicates = andPredicate.getPredicates();

        // Then: the predicates are returned
        assertArrayEquals(new Predicate<?>[]{predicate1, predicate2}, predicates);
    }
}
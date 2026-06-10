import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.OrPredicate;
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
public class OrPredicateTest {

    @Mock
    private Predicate<Object> predicate1;

    @Mock
    private Predicate<Object> predicate2;

    private OrPredicate<Object> orPredicate;

    @BeforeEach
    void setup() {
        orPredicate = new OrPredicate<>(predicate1, predicate2);
    }

    @Test
    public void testOrPredicate_Success() {
        // Given
        when(predicate1.test(any())).thenReturn(true);
        when(predicate2.test(any())).thenReturn(false);

        // When
        boolean result = orPredicate.test("object");

        // Then
        assertTrue(result);
        verify(predicate1, times(1)).test("object");
        verify(predicate2, times(1)).test("object");
    }

    @Test
    public void testOrPredicate_FirstPredicateReturnsFalse_SecondPredicateReturnsTrue() {
        // Given
        when(predicate1.test(any())).thenReturn(false);
        when(predicate2.test(any())).thenReturn(true);

        // When
        boolean result = orPredicate.test("object");

        // Then
        assertTrue(result);
        verify(predicate1, times(1)).test("object");
        verify(predicate2, times(1)).test("object");
    }

    @Test
    public void testOrPredicate_BothPredicatesReturnFalse() {
        // Given
        when(predicate1.test(any())).thenReturn(false);
        when(predicate2.test(any())).thenReturn(false);

        // When
        boolean result = orPredicate.test("object");

        // Then
        assertFalse(result);
        verify(predicate1, times(1)).test("object");
        verify(predicate2, times(1)).test("object");
    }

    @Test
    public void testOrPredicate_BothPredicatesReturnTrue() {
        // Given
        when(predicate1.test(any())).thenReturn(true);
        when(predicate2.test(any())).thenReturn(true);

        // When
        boolean result = orPredicate.test("object");

        // Then
        assertTrue(result);
        verify(predicate1, times(1)).test("object");
        verify(predicate2, times(1)).test("object");
    }

    @Test
    public void testOrPredicate_StaticMethod() {
        // Given
        OrPredicate<Object> orPredicateStatic = OrPredicate.orPredicate(predicate1, predicate2);

        // When
        when(predicate1.test(any())).thenReturn(true);
        when(predicate2.test(any())).thenReturn(false);

        boolean result = orPredicateStatic.test("object");

        // Then
        assertTrue(result);
        verify(predicate1, times(1)).test("object");
        verify(predicate2, times(1)).test("object");
    }

    @Test
    public void testOrPredicate_StaticMethod_NullPredicate1() {
        // Given
        assertThrows(NullPointerException.class, () -> OrPredicate.orPredicate(null, predicate2));
    }

    @Test
    public void testOrPredicate_StaticMethod_NullPredicate2() {
        // Given
        assertThrows(NullPointerException.class, () -> OrPredicate.orPredicate(predicate1, null));
    }

    @Test
    public void testGetPredicates() {
        // When
        Predicate<?>[] predicates = orPredicate.getPredicates();

        // Then
        assertEquals(2, predicates.length);
        assertSame(predicate1, predicates[0]);
        assertSame(predicate2, predicates[1]);
    }
}
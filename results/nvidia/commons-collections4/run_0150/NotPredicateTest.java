import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NotPredicate;
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
public class NotPredicateTest {

    @Mock
    private Predicate<Object> predicate;

    private NotPredicate<Object> notPredicate;

    @BeforeEach
    void setup() {
        notPredicate = new NotPredicate<>(predicate);
    }

    @Test
    void testNotPredicate() {
        // Given
        Predicate<Object> result = NotPredicate.notPredicate(predicate);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof NotPredicate);
    }

    @Test
    void testNotPredicate_NullPredicate() {
        // Given
        Predicate<Object> nullPredicate = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> NotPredicate.notPredicate(nullPredicate));
    }

    @Test
    void testTest_True() {
        // Given
        Object object = new Object();
        when(predicate.evaluate(any())).thenReturn(false);

        // When
        boolean result = notPredicate.test(object);

        // Then
        assertTrue(result);
        verify(predicate, times(1)).evaluate(object);
    }

    @Test
    void testTest_False() {
        // Given
        Object object = new Object();
        when(predicate.evaluate(any())).thenReturn(true);

        // When
        boolean result = notPredicate.test(object);

        // Then
        assertFalse(result);
        verify(predicate, times(1)).evaluate(object);
    }

    @Test
    void testGetPredicates() {
        // When
        Predicate<?>[] result = notPredicate.getPredicates();

        // Then
        assertNotNull(result);
        assertEquals(1, result.length);
        assertSame(predicate, result[0]);
    }
}
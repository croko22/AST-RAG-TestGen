import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.OnePredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OnePredicateTest {

    @Mock
    private Predicate<Object> predicate1;

    @Mock
    private Predicate<Object> predicate2;

    @Mock
    private Predicate<Object> predicate3;

    private OnePredicate<Object> onePredicate;

    @BeforeEach
    public void setup() {
        onePredicate = new OnePredicate<>(predicate1, predicate2, predicate3);
    }

    @Test
    public void testOnePredicate_Collection() {
        // Given
        Collection<Predicate<Object>> predicates = new ArrayList<>();
        predicates.add(predicate1);
        predicates.add(predicate2);
        predicates.add(predicate3);

        // When
        OnePredicate<Object> result = OnePredicate.onePredicate(predicates);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testOnePredicate_VarArgs() {
        // Given

        // When
        OnePredicate<Object> result = OnePredicate.onePredicate(predicate1, predicate2, predicate3);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testOnePredicate_VarArgs_Empty() {
        // Given

        // When
        OnePredicate<Object> result = OnePredicate.onePredicate();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testOnePredicate_VarArgs_Null() {
        // Given

        // When and Then
        assertThrows(NullPointerException.class, () -> OnePredicate.onePredicate((Predicate<Object>) null));
    }

    @Test
    public void testTest_MultiplePredicates_OneMatch() {
        // Given
        when(predicate1.test(any())).thenReturn(true);
        when(predicate2.test(any())).thenReturn(false);
        when(predicate3.test(any())).thenReturn(false);

        // When
        boolean result = onePredicate.test(new Object());

        // Then
        assertTrue(result);
        verify(predicate1, times(1)).test(any());
        verify(predicate2, times(1)).test(any());
        verify(predicate3, times(1)).test(any());
    }

    @Test
    public void testTest_MultiplePredicates_MultipleMatches() {
        // Given
        when(predicate1.test(any())).thenReturn(true);
        when(predicate2.test(any())).thenReturn(true);
        when(predicate3.test(any())).thenReturn(false);

        // When
        boolean result = onePredicate.test(new Object());

        // Then
        assertFalse(result);
        verify(predicate1, times(1)).test(any());
        verify(predicate2, times(1)).test(any());
        verify(predicate3, times(1)).test(any());
    }

    @Test
    public void testTest_MultiplePredicates_NoMatches() {
        // Given
        when(predicate1.test(any())).thenReturn(false);
        when(predicate2.test(any())).thenReturn(false);
        when(predicate3.test(any())).thenReturn(false);

        // When
        boolean result = onePredicate.test(new Object());

        // Then
        assertFalse(result);
        verify(predicate1, times(1)).test(any());
        verify(predicate2, times(1)).test(any());
        verify(predicate3, times(1)).test(any());
    }

    @Test
    public void testTest_SinglePredicate_Match() {
        // Given
        OnePredicate<Object> singlePredicate = new OnePredicate<>(predicate1);

        // When
        when(predicate1.test(any())).thenReturn(true);
        boolean result = singlePredicate.test(new Object());

        // Then
        assertTrue(result);
        verify(predicate1, times(1)).test(any());
    }

    @Test
    public void testTest_SinglePredicate_NoMatch() {
        // Given
        OnePredicate<Object> singlePredicate = new OnePredicate<>(predicate1);

        // When
        when(predicate1.test(any())).thenReturn(false);
        boolean result = singlePredicate.test(new Object());

        // Then
        assertFalse(result);
        verify(predicate1, times(1)).test(any());
    }

    @Test
    public void testTest_NoPredicates() {
        // Given
        OnePredicate<Object> noPredicates = new OnePredicate<>();

        // When
        boolean result = noPredicates.test(new Object());

        // Then
        assertFalse(result);
    }
}
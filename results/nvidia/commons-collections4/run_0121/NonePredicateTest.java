import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NonePredicate;
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
public class NonePredicateTest {

    @Mock
    private Predicate<Object> predicate1;

    @Mock
    private Predicate<Object> predicate2;

    private Collection<Predicate<Object>> predicates;

    @BeforeEach
    void setup() {
        predicates = new ArrayList<>();
    }

    @Test
    public void testNonePredicate_EmptyCollection_ReturnsTruePredicate() {
        // Given: an empty collection of predicates
        Collection<Predicate<Object>> emptyPredicates = new ArrayList<>();

        // When: nonePredicate is created with the empty collection
        Predicate<Object> nonePredicate = NonePredicate.nonePredicate(emptyPredicates);

        // Then: the returned predicate is a TruePredicate
        assertTrue(nonePredicate.test(new Object()));
    }

    @Test
    public void testNonePredicate_EmptyArray_ReturnsTruePredicate() {
        // Given: an empty array of predicates
        Predicate<Object>[] emptyPredicates = new Predicate[0];

        // When: nonePredicate is created with the empty array
        Predicate<Object> nonePredicate = NonePredicate.nonePredicate(emptyPredicates);

        // Then: the returned predicate is a TruePredicate
        assertTrue(nonePredicate.test(new Object()));
    }

    @Test
    public void testNonePredicate_SinglePredicate_ReturnsNonePredicate() {
        // Given: a collection with a single predicate
        predicates.add(predicate1);

        // When: nonePredicate is created with the collection
        Predicate<Object> nonePredicate = NonePredicate.nonePredicate(predicates);

        // Then: the returned predicate is a NonePredicate
        assertNotNull(nonePredicate);
    }

    @Test
    public void testNonePredicate_MultiplePredicates_ReturnsNonePredicate() {
        // Given: a collection with multiple predicates
        predicates.add(predicate1);
        predicates.add(predicate2);

        // When: nonePredicate is created with the collection
        Predicate<Object> nonePredicate = NonePredicate.nonePredicate(predicates);

        // Then: the returned predicate is a NonePredicate
        assertNotNull(nonePredicate);
    }

    @Test
    public void testTest_PredicateReturnsTrue_ReturnsFalse() {
        // Given: a NonePredicate with a predicate that returns true
        predicates.add(predicate1);
        when(predicate1.test(any())).thenReturn(true);
        Predicate<Object> nonePredicate = NonePredicate.nonePredicate(predicates);

        // When: the test method is called
        boolean result = nonePredicate.test(new Object());

        // Then: the result is false
        assertFalse(result);
    }

    @Test
    public void testTest_AllPredicatesReturnFalse_ReturnsTrue() {
        // Given: a NonePredicate with multiple predicates that return false
        predicates.add(predicate1);
        predicates.add(predicate2);
        when(predicate1.test(any())).thenReturn(false);
        when(predicate2.test(any())).thenReturn(false);
        Predicate<Object> nonePredicate = NonePredicate.nonePredicate(predicates);

        // When: the test method is called
        boolean result = nonePredicate.test(new Object());

        // Then: the result is true
        assertTrue(result);
    }

    @Test
    public void testTest_NullPredicate_ThrowsNullPointerException() {
        // Given: a collection with a null predicate
        predicates.add(null);

        // When: nonePredicate is created with the collection
        assertThrows(NullPointerException.class, () -> NonePredicate.nonePredicate(predicates));
    }

    @Test
    public void testNonePredicate_NullCollection_ThrowsNullPointerException() {
        // Given: a null collection of predicates
        Collection<Predicate<Object>> nullPredicates = null;

        // When: nonePredicate is created with the null collection
        assertThrows(NullPointerException.class, () -> NonePredicate.nonePredicate(nullPredicates));
    }
}
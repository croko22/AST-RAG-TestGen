import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.AllPredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AllPredicateTest {

    @Mock
    private Predicate<Object> predicate1;

    @Mock
    private Predicate<Object> predicate2;

    private AllPredicate<Object> allPredicate;

    @BeforeEach
    public void setup() {
        allPredicate = new AllPredicate<>(predicate1, predicate2);
    }

    @Test
    public void testAllPredicate_Collection() {
        // Given: a collection of predicates
        Collection<Predicate<Object>> predicates = Collections.singleton(predicate1);

        // When: creating an AllPredicate instance
        Predicate<Object> allPredicate = AllPredicate.allPredicate(predicates);

        // Then: the AllPredicate instance is created successfully
        assertNotNull(allPredicate);
    }

    @Test
    public void testAllPredicate_VarArgs() {
        // Given: a variable number of predicates
        Predicate<Object> predicate1 = mock(Predicate.class);
        Predicate<Object> predicate2 = mock(Predicate.class);

        // When: creating an AllPredicate instance
        Predicate<Object> allPredicate = AllPredicate.allPredicate(predicate1, predicate2);

        // Then: the AllPredicate instance is created successfully
        assertNotNull(allPredicate);
    }

    @Test
    public void testAllPredicate_EmptyCollection() {
        // Given: an empty collection of predicates
        Collection<Predicate<Object>> predicates = Collections.emptyList();

        // When: creating an AllPredicate instance
        Predicate<Object> allPredicate = AllPredicate.allPredicate(predicates);

        // Then: the AllPredicate instance always returns true
        assertTrue(allPredicate.test(new Object()));
    }

    @Test
    public void testAllPredicate_EmptyVarArgs() {
        // Given: no predicates

        // When: creating an AllPredicate instance
        Predicate<Object> allPredicate = AllPredicate.allPredicate();

        // Then: the AllPredicate instance always returns true
        assertTrue(allPredicate.test(new Object()));
    }

    @Test
    public void testTest_AllPredicatesReturnTrue() {
        // Given: all predicates return true
        when(predicate1.test(any())).thenReturn(true);
        when(predicate2.test(any())).thenReturn(true);

        // When: evaluating the AllPredicate instance
        boolean result = allPredicate.test(new Object());

        // Then: the AllPredicate instance returns true
        assertTrue(result);
    }

    @Test
    public void testTest_AtLeastOnePredicateReturnsFalse() {
        // Given: at least one predicate returns false
        when(predicate1.test(any())).thenReturn(true);
        when(predicate2.test(any())).thenReturn(false);

        // When: evaluating the AllPredicate instance
        boolean result = allPredicate.test(new Object());

        // Then: the AllPredicate instance returns false
        assertFalse(result);
    }

    @Test
    public void testTest_NullPredicate() {
        // Given: a null predicate
        Predicate<Object> nullPredicate = null;

        // When and Then: creating an AllPredicate instance with a null predicate throws NullPointerException
        assertThrows(NullPointerException.class, () -> AllPredicate.allPredicate(nullPredicate));
    }

    @Test
    public void testTest_NullCollection() {
        // Given: a null collection of predicates
        Collection<Predicate<Object>> nullCollection = null;

        // When and Then: creating an AllPredicate instance with a null collection throws NullPointerException
        assertThrows(NullPointerException.class, () -> AllPredicate.allPredicate(nullCollection));
    }
}
import org.apache.commons.collections4.functors.AbstractQuantifierPredicate;
import org.apache.commons.collections4.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractQuantifierPredicateTest {

    @Mock
    private Predicate<Object> predicate1;

    @Mock
    private Predicate<Object> predicate2;

    private AbstractQuantifierPredicate<Object> abstractQuantifierPredicate;

    @BeforeEach
    public void setup() {
        abstractQuantifierPredicate = new AbstractQuantifierPredicate<>(predicate1, predicate2) {
            @Override
            public boolean evaluate(Object object) {
                return false;
            }
        };
    }

    @Test
    public void testConstructor() {
        // Given: predicates to check
        Predicate<Object>[] predicates = new Predicate[]{predicate1, predicate2};

        // When: creating an instance of AbstractQuantifierPredicate
        AbstractQuantifierPredicate<Object> instance = new AbstractQuantifierPredicate<>(predicates) {
            @Override
            public boolean evaluate(Object object) {
                return false;
            }
        };

        // Then: verify that the predicates are set correctly
        assertSame(predicates, instance.iPredicates);
    }

    @Test
    public void testGetPredicates() {
        // Given: an instance of AbstractQuantifierPredicate
        AbstractQuantifierPredicate<Object> instance = new AbstractQuantifierPredicate<>(predicate1, predicate2) {
            @Override
            public boolean evaluate(Object object) {
                return false;
            }
        };

        // When: getting the predicates
        Predicate<Object>[] predicates = instance.getPredicates();

        // Then: verify that the predicates are returned as a copy
        assertNotSame(instance.iPredicates, predicates);
        assertEquals(instance.iPredicates.length, predicates.length);
        assertSame(instance.iPredicates[0], predicates[0]);
        assertSame(instance.iPredicates[1], predicates[1]);
    }

    @Test
    public void testGetPredicates_Null() {
        // Given: an instance of AbstractQuantifierPredicate with null predicates
        AbstractQuantifierPredicate<Object> instance = new AbstractQuantifierPredicate<>((Predicate<Object>[]) null) {
            @Override
            public boolean evaluate(Object object) {
                return false;
            }
        };

        // When: getting the predicates
        Predicate<Object>[] predicates = instance.getPredicates();

        // Then: verify that an empty array is returned
        assertNotNull(predicates);
        assertEquals(0, predicates.length);
    }

    @Test
    public void testGetPredicates_Empty() {
        // Given: an instance of AbstractQuantifierPredicate with an empty array of predicates
        AbstractQuantifierPredicate<Object> instance = new AbstractQuantifierPredicate<>(new Predicate[0]) {
            @Override
            public boolean evaluate(Object object) {
                return false;
            }
        };

        // When: getting the predicates
        Predicate<Object>[] predicates = instance.getPredicates();

        // Then: verify that an empty array is returned
        assertNotNull(predicates);
        assertEquals(0, predicates.length);
    }
}
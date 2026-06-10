import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.collection.PredicatedCollection;
import org.apache.commons.collections4.set.PredicatedSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PredicatedSetTest {

    @Mock
    private Set<String> set;

    @Mock
    private Predicate<String> predicate;

    private PredicatedSet<String> predicatedSet;

    @BeforeEach
    void setup() {
        predicatedSet = new PredicatedSet<>(set, predicate);
    }

    @Test
    public void testPredicatedSet() {
        // Given
        Set<String> inputSet = new HashSet<>();
        inputSet.add("test");
        Predicate<String> inputPredicate = new Predicate<String>() {
            @Override
            public boolean evaluate(String object) {
                return true;
            }
        };

        // When
        PredicatedSet<String> result = PredicatedSet.predicatedSet(inputSet, inputPredicate);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testPredicatedSet_NullSet() {
        // Given
        Set<String> inputSet = null;
        Predicate<String> inputPredicate = new Predicate<String>() {
            @Override
            public boolean evaluate(String object) {
                return true;
            }
        };

        // When / Then
        assertThrows(NullPointerException.class, () -> PredicatedSet.predicatedSet(inputSet, inputPredicate));
    }

    @Test
    public void testPredicatedSet_NullPredicate() {
        // Given
        Set<String> inputSet = new HashSet<>();
        Predicate<String> inputPredicate = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> PredicatedSet.predicatedSet(inputSet, inputPredicate));
    }

    @Test
    public void testEquals_SameObject() {
        // Given
        Object object = predicatedSet;

        // When
        boolean result = predicatedSet.equals(object);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_SameSet() {
        // Given
        Set<String> inputSet = new HashSet<>();
        inputSet.add("test");
        Predicate<String> inputPredicate = new Predicate<String>() {
            @Override
            public boolean evaluate(String object) {
                return true;
            }
        };
        PredicatedSet<String> otherPredicatedSet = new PredicatedSet<>(inputSet, inputPredicate);
        Object object = otherPredicatedSet;

        // When
        boolean result = predicatedSet.equals(object);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_DifferentSet() {
        // Given
        Set<String> inputSet = new HashSet<>();
        inputSet.add("test");
        Predicate<String> inputPredicate = new Predicate<String>() {
            @Override
            public boolean evaluate(String object) {
                return true;
            }
        };
        PredicatedSet<String> otherPredicatedSet = new PredicatedSet<>(new HashSet<>(), inputPredicate);
        Object object = otherPredicatedSet;

        // When
        boolean result = predicatedSet.equals(object);

        // Then
        assertFalse(result);
    }

    @Test
    public void testHashCode() {
        // Given
        Set<String> inputSet = new HashSet<>();
        inputSet.add("test");
        Predicate<String> inputPredicate = new Predicate<String>() {
            @Override
            public boolean evaluate(String object) {
                return true;
            }
        };
        PredicatedSet<String> otherPredicatedSet = new PredicatedSet<>(inputSet, inputPredicate);

        // When
        int result = predicatedSet.hashCode();

        // Then
        assertEquals(otherPredicatedSet.hashCode(), result);
    }
}
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.AnyPredicate;
import org.apache.commons.collections4.functors.FalsePredicate;
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
public class AnyPredicateTest {

    @Mock
    private Predicate<String> predicate1;

    @Mock
    private Predicate<String> predicate2;

    @Mock
    private Predicate<String> predicate3;

    private AnyPredicate<String> anyPredicate;

    @BeforeEach
    void setup() {
        anyPredicate = new AnyPredicate<>();
    }

    @Test
    void testAnyPredicate_Collection() {
        // Given
        Collection<Predicate<String>> predicates = new ArrayList<>();
        predicates.add(predicate1);
        predicates.add(predicate2);

        // When
        Predicate<String> result = AnyPredicate.anyPredicate(predicates);

        // Then
        assertNotSame(result, predicate1);
        assertNotSame(result, predicate2);
        assertTrue(result instanceof AnyPredicate);
    }

    @Test
    void testAnyPredicate_VarArgs() {
        // Given

        // When
        Predicate<String> result = AnyPredicate.anyPredicate(predicate1, predicate2);

        // Then
        assertNotSame(result, predicate1);
        assertNotSame(result, predicate2);
        assertTrue(result instanceof AnyPredicate);
    }

    @Test
    void testAnyPredicate_EmptyCollection() {
        // Given
        Collection<Predicate<String>> predicates = new ArrayList<>();

        // When
        Predicate<String> result = AnyPredicate.anyPredicate(predicates);

        // Then
        assertSame(FalsePredicate.falsePredicate(), result);
    }

    @Test
    void testAnyPredicate_SingleElementCollection() {
        // Given
        Collection<Predicate<String>> predicates = new ArrayList<>();
        predicates.add(predicate1);

        // When
        Predicate<String> result = AnyPredicate.anyPredicate(predicates);

        // Then
        assertSame(predicate1, result);
    }

    @Test
    void testAnyPredicate_EmptyVarArgs() {
        // Given

        // When
        Predicate<String> result = AnyPredicate.anyPredicate();

        // Then
        assertSame(FalsePredicate.falsePredicate(), result);
    }

    @Test
    void testAnyPredicate_SingleElementVarArgs() {
        // Given

        // When
        Predicate<String> result = AnyPredicate.anyPredicate(predicate1);

        // Then
        assertSame(predicate1, result);
    }

    @Test
    void testTest_ReturnsTrue() {
        // Given
        Collection<Predicate<String>> predicates = new ArrayList<>();
        predicates.add(predicate1);
        predicates.add(predicate2);
        AnyPredicate<String> anyPredicate = (AnyPredicate<String>) AnyPredicate.anyPredicate(predicates);
        when(predicate1.test(any())).thenReturn(false);
        when(predicate2.test(any())).thenReturn(true);

        // When
        boolean result = anyPredicate.test("test");

        // Then
        assertTrue(result);
        verify(predicate1, times(1)).test("test");
        verify(predicate2, times(1)).test("test");
    }

    @Test
    void testTest_ReturnsFalse() {
        // Given
        Collection<Predicate<String>> predicates = new ArrayList<>();
        predicates.add(predicate1);
        predicates.add(predicate2);
        AnyPredicate<String> anyPredicate = (AnyPredicate<String>) AnyPredicate.anyPredicate(predicates);
        when(predicate1.test(any())).thenReturn(false);
        when(predicate2.test(any())).thenReturn(false);

        // When
        boolean result = anyPredicate.test("test");

        // Then
        assertFalse(result);
        verify(predicate1, times(1)).test("test");
        verify(predicate2, times(1)).test("test");
    }

    @Test
    void testTest_PredicateThrowsException() {
        // Given
        Collection<Predicate<String>> predicates = new ArrayList<>();
        predicates.add(predicate1);
        predicates.add(predicate2);
        AnyPredicate<String> anyPredicate = (AnyPredicate<String>) AnyPredicate.anyPredicate(predicates);
        when(predicate1.test(any())).thenThrow(new RuntimeException());
        when(predicate2.test(any())).thenReturn(true);

        // When
        boolean result = anyPredicate.test("test");

        // Then
        assertTrue(result);
        verify(predicate1, times(1)).test("test");
        verify(predicate2, times(1)).test("test");
    }
}
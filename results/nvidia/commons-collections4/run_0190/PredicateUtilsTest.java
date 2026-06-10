import org.apache.commons.collections4.functors.AllPredicate;
import org.apache.commons.collections4.functors.AnyPredicate;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.apache.commons.collections4.functors.ExceptionPredicate;
import org.apache.commons.collections4.functors.FalsePredicate;
import org.apache.commons.collections4.functors.IdentityPredicate;
import org.apache.commons.collections4.functors.InstanceofPredicate;
import org.apache.commons.collections4.functors.NonePredicate;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.apache.commons.collections4.functors.NullIsExceptionPredicate;
import org.apache.commons.collections4.functors.NullIsFalsePredicate;
import org.apache.commons.collections4.functors.NullIsTruePredicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.apache.commons.collections4.functors.OnePredicate;
import org.apache.commons.collections4.functors.OrPredicate;
import org.apache.commons.collections4.functors.TransformerPredicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.apache.commons.collections4.PredicateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PredicateUtilsTest {

    @Mock
    private Predicate<Object> predicate1;

    @Mock
    private Predicate<Object> predicate2;

    @BeforeEach
    public void setup() {
        // Setup mocks
        when(predicate1.test(any())).thenReturn(true);
        when(predicate2.test(any())).thenReturn(false);
    }

    @Test
    public void testAllPredicate_Collection() {
        // Given
        Collection<Predicate<Object>> predicates = Arrays.asList(predicate1, predicate2);

        // When
        Predicate<Object> allPredicate = PredicateUtils.allPredicate(predicates);

        // Then
        assertFalse(allPredicate.test(new Object()));
        verify(predicate1, times(1)).test(any());
        verify(predicate2, times(1)).test(any());
    }

    @Test
    public void testAllPredicate_Varargs() {
        // Given

        // When
        Predicate<Object> allPredicate = PredicateUtils.allPredicate(predicate1, predicate2);

        // Then
        assertFalse(allPredicate.test(new Object()));
        verify(predicate1, times(1)).test(any());
        verify(predicate2, times(1)).test(any());
    }

    @Test
    public void testAndPredicate() {
        // Given

        // When
        Predicate<Object> andPredicate = PredicateUtils.andPredicate(predicate1, predicate2);

        // Then
        assertFalse(andPredicate.test(new Object()));
        verify(predicate1, times(1)).test(any());
        verify(predicate2, times(1)).test(any());
    }

    @Test
    public void testAnyPredicate_Collection() {
        // Given
        Collection<Predicate<Object>> predicates = Arrays.asList(predicate1, predicate2);

        // When
        Predicate<Object> anyPredicate = PredicateUtils.anyPredicate(predicates);

        // Then
        assertTrue(anyPredicate.test(new Object()));
        verify(predicate1, times(1)).test(any());
        verify(predicate2, times(1)).test(any());
    }

    @Test
    public void testAnyPredicate_Varargs() {
        // Given

        // When
        Predicate<Object> anyPredicate = PredicateUtils.anyPredicate(predicate1, predicate2);

        // Then
        assertTrue(anyPredicate.test(new Object()));
        verify(predicate1, times(1)).test(any());
        verify(predicate2, times(1)).test(any());
    }

    @Test
    public void testAsPredicate() {
        // Given
        Transformer<Object, Boolean> transformer = obj -> true;

        // When
        Predicate<Object> asPredicate = PredicateUtils.asPredicate(transformer);

        // Then
        assertTrue(asPredicate.test(new Object()));
    }

    @Test
    public void testEitherPredicate() {
        // Given

        // When
        Predicate<Object> eitherPredicate = PredicateUtils.eitherPredicate(predicate1, predicate2);

        // Then
        assertTrue(eitherPredicate.test(new Object()));
        verify(predicate1, times(1)).test(any());
        verify(predicate2, times(1)).test(any());
    }

    @Test
    public void testEqualPredicate() {
        // Given
        Object value = new Object();

        // When
        Predicate<Object> equalPredicate = PredicateUtils.equalPredicate(value);

        // Then
        assertTrue(equalPredicate.test(value));
        assertFalse(equalPredicate.test(new Object()));
    }

    @Test
    public void testExceptionPredicate() {
        // Given

        // When
        Predicate<Object> exceptionPredicate = PredicateUtils.exceptionPredicate();

        // Then
        assertThrows(RuntimeException.class, () -> exceptionPredicate.test(new Object()));
    }

    @Test
    public void testFalsePredicate() {
        // Given

        // When
        Predicate<Object> falsePredicate = PredicateUtils.falsePredicate();

        // Then
        assertFalse(falsePredicate.test(new Object()));
    }

    @Test
    public void testIdentityPredicate() {
        // Given
        Object value = new Object();

        // When
        Predicate<Object> identityPredicate = PredicateUtils.identityPredicate(value);

        // Then
        assertTrue(identityPredicate.test(value));
        assertFalse(identityPredicate.test(new Object()));
    }

    @Test
    public void testInstanceofPredicate() {
        // Given
        Class<?> type = Object.class;

        // When
        Predicate<Object> instanceofPredicate = PredicateUtils.instanceofPredicate(type);

        // Then
        assertTrue(instanceofPredicate.test(new Object()));
        assertFalse(instanceofPredicate.test(null));
    }

    @Test
    public void testInvokerPredicate() {
        // Given
        String methodName = "toString";

        // When
        Predicate<Object> invokerPredicate = PredicateUtils.invokerPredicate(methodName);

        // Then
        assertTrue(invokerPredicate.test(new Object()));
    }

    @Test
    public void testNeitherPredicate() {
        // Given

        // When
        Predicate<Object> neitherPredicate = PredicateUtils.neitherPredicate(predicate1, predicate2);

        // Then
        assertFalse(neitherPredicate.test(new Object()));
        verify(predicate1, times(1)).test(any());
        verify(predicate2, times(1)).test(any());
    }

    @Test
    public void testNonePredicate_Collection() {
        // Given
        Collection<Predicate<Object>> predicates = Arrays.asList(predicate1, predicate2);

        // When
        Predicate<Object> nonePredicate = PredicateUtils.nonePredicate(predicates);

        // Then
        assertFalse(nonePredicate.test(new Object()));
        verify(predicate1, times(1)).test(any());
        verify(predicate2, times(1)).test(any());
    }

    @Test
    public void testNonePredicate_Varargs() {
        // Given

        // When
        Predicate<Object> nonePredicate = PredicateUtils.nonePredicate(predicate1, predicate2);

        // Then
        assertFalse(nonePredicate.test(new Object()));
        verify(predicate1, times(1)).test(any());
        verify(predicate2, times(1)).test(any());
    }

    @Test
    public void testNotNullPredicate() {
        // Given

        // When
        Predicate<Object> notNullPredicate = PredicateUtils.notNullPredicate();

        // Then
        assertTrue(notNullPredicate.test(new Object()));
        assertFalse(notNullPredicate.test(null));
    }

    @Test
    public void testNotPredicate() {
        // Given

        // When
        Predicate<Object> notPredicate = PredicateUtils.notPredicate(predicate1);

        // Then
        assertFalse(notPredicate.test(new Object()));
        verify(predicate1, times(1)).test(any());
    }

    @Test
    public void testNullIsExceptionPredicate() {
        // Given

        // When
        Predicate<Object> nullIsExceptionPredicate = PredicateUtils.nullIsExceptionPredicate(predicate1);

        // Then
        assertThrows(RuntimeException.class, () -> nullIsExceptionPredicate.test(null));
        verify(predicate1, times(1)).test(any());
    }

    @Test
    public void testNullIsFalsePredicate() {
        // Given

        // When
        Predicate<Object> nullIsFalsePredicate = PredicateUtils.nullIsFalsePredicate(predicate1);

        // Then
        assertFalse(nullIsFalsePredicate.test(null));
        verify(predicate1, times(1)).test(any());
    }

    @Test
    public void testNullIsTruePredicate() {
        // Given

        // When
        Predicate<Object> nullIsTruePredicate = PredicateUtils.nullIsTruePredicate(predicate1);

        // Then
        assertTrue(nullIsTruePredicate.test(null));
        verify(predicate1, times(1)).test(any());
    }

    @Test
    public void testNullPredicate() {
        // Given

        // When
        Predicate<Object> nullPredicate = PredicateUtils.nullPredicate();

        // Then
        assertTrue(nullPredicate.test(null));
        assertFalse(nullPredicate.test(new Object()));
    }

    @Test
    public void testOnePredicate_Collection() {
        // Given
        Collection<Predicate<Object>> predicates = Arrays.asList(predicate1, predicate2);

        // When
        Predicate<Object> onePredicate = PredicateUtils.onePredicate(predicates);

        // Then
        assertTrue(onePredicate.test(new Object()));
        verify(predicate1, times(1)).test(any());
        verify(predicate2, times(1)).test(any());
    }

    @Test
    public void testOnePredicate_Varargs() {
        // Given

        // When
        Predicate<Object> onePredicate = PredicateUtils.onePredicate(predicate1, predicate2);

        // Then
        assertTrue(onePredicate.test(new Object()));
        verify(predicate1, times(1)).test(any());
        verify(predicate2, times(1)).test(any());
    }

    @Test
    public void testOrPredicate() {
        // Given

        // When
        Predicate<Object> orPredicate = PredicateUtils.orPredicate(predicate1, predicate2);

        // Then
        assertTrue(orPredicate.test(new Object()));
        verify(predicate1, times(1)).test(any());
        verify(predicate2, times(1)).test(any());
    }

    @Test
    public void testTransformedPredicate() {
        // Given
        Transformer<Object, Object> transformer = obj -> obj;

        // When
        Predicate<Object> transformedPredicate = PredicateUtils.transformedPredicate(transformer, predicate1);

        // Then
        assertTrue(transformedPredicate.test(new Object()));
        verify(predicate1, times(1)).test(any());
    }

    @Test
    public void testTruePredicate() {
        // Given

        // When
        Predicate<Object> truePredicate = PredicateUtils.truePredicate();

        // Then
        assertTrue(truePredicate.test(new Object()));
    }

    @Test
    public void testUniquePredicate() {
        // Given

        // When
        Predicate<Object> uniquePredicate = PredicateUtils.uniquePredicate();

        // Then
        assertTrue(uniquePredicate.test(new Object()));
        assertFalse(uniquePredicate.test(new Object()));
    }
}
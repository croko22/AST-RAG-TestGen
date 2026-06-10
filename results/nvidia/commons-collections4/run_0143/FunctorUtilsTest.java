import org.apache.commons.collections4.functors.FunctorUtils;
import org.apache.commons.collections4.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate as JavaPredicate;

public class FunctorUtilsTest {

    private Collection<JavaPredicate<Integer>> predicatesCollection;
    private JavaPredicate<Integer> predicate;
    private Consumer<Integer> consumer;
    private Function<Integer, Integer> function;

    @BeforeEach
    void setup() {
        predicatesCollection = new ArrayList<>();
        predicate = (t) -> t > 0;
        consumer = (t) -> System.out.println(t);
        function = (t) -> t * 2;
    }

    @Test
    void testCoercePredicate() {
        // Given
        JavaPredicate<Integer> coercedPredicate = FunctorUtils.coerce(predicate);

        // Then
        assertNotNull(coercedPredicate);
        assertTrue(coercedPredicate.test(1));
        assertFalse(coercedPredicate.test(-1));
    }

    @Test
    void testCoerceTransformer() {
        // Given
        Function<Integer, Integer> coercedTransformer = FunctorUtils.coerce(function);

        // Then
        assertNotNull(coercedTransformer);
        assertEquals(2, (int) coercedTransformer.apply(1));
    }

    @Test
    void testCopyConsumers() {
        // Given
        Consumer<Integer>[] copiedConsumers = FunctorUtils.copy(consumer);

        // Then
        assertNotNull(copiedConsumers);
        assertEquals(1, copiedConsumers.length);
        assertSame(consumer, copiedConsumers[0]);
    }

    @Test
    void testCopyPredicates() {
        // Given
        JavaPredicate<Integer>[] copiedPredicates = FunctorUtils.copy(predicate);

        // Then
        assertNotNull(copiedPredicates);
        assertEquals(1, copiedPredicates.length);
        assertSame(predicate, copiedPredicates[0]);
    }

    @Test
    void testCopyTransformers() {
        // Given
        Function<Integer, Integer>[] copiedTransformers = FunctorUtils.copy(function);

        // Then
        assertNotNull(copiedTransformers);
        assertEquals(1, copiedTransformers.length);
        assertSame(function, copiedTransformers[0]);
    }

    @Test
    void testValidatePredicatesCollection() {
        // Given
        predicatesCollection.add(predicate);

        // When
        Predicate<? super Integer>[] validatedPredicates = FunctorUtils.validate(predicatesCollection);

        // Then
        assertNotNull(validatedPredicates);
        assertEquals(1, validatedPredicates.length);
        assertSame(predicate, validatedPredicates[0]);
    }

    @Test
    void testValidatePredicatesArray() {
        // Given
        JavaPredicate<Integer>[] predicatesArray = new JavaPredicate[]{predicate};

        // When
        FunctorUtils.validate(predicatesArray);

        // Then
        // No exception thrown
    }

    @Test
    void testValidateConsumers() {
        // Given

        // When
        FunctorUtils.validate(consumer);

        // Then
        // No exception thrown
    }

    @Test
    void testValidateFunctions() {
        // Given

        // When
        FunctorUtils.validate(function);

        // Then
        // No exception thrown
    }

    @Test
    void testValidateNullPredicatesCollection() {
        // Given
        predicatesCollection = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> FunctorUtils.validate(predicatesCollection));
    }

    @Test
    void testValidateNullPredicatesArray() {
        // Given
        JavaPredicate<Integer>[] predicatesArray = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> FunctorUtils.validate(predicatesArray));
    }

    @Test
    void testValidateNullConsumer() {
        // Given
        consumer = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> FunctorUtils.validate(consumer));
    }

    @Test
    void testValidateNullFunction() {
        // Given
        function = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> FunctorUtils.validate(function));
    }
}
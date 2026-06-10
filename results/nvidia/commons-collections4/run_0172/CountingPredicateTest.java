import org.apache.commons.collections4.bloomfilter.CountingPredicate;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CountingPredicateTest {

    @Mock
    private BiPredicate<String, String> biPredicateMock;

    private CountingPredicate<String> countingPredicate;

    @BeforeEach
    public void setup() {
        String[] array = {"value1", "value2", "value3"};
        countingPredicate = new CountingPredicate<>(array, biPredicateMock);
    }

    @Test
    public void testTest_Method_Call_BiPredicate_With_Valid_Values() {
        // Given
        when(biPredicateMock.test(any(), any())).thenReturn(true);

        // When
        boolean result = countingPredicate.test("otherValue");

        // Then
        assertTrue(result);
        verify(biPredicateMock, times(1)).test("value1", "otherValue");
    }

    @Test
    public void testTest_Method_Call_BiPredicate_With_Null_Value() {
        // Given
        String[] array = {};
        countingPredicate = new CountingPredicate<>(array, biPredicateMock);
        when(biPredicateMock.test(any(), any())).thenReturn(true);

        // When
        boolean result = countingPredicate.test("otherValue");

        // Then
        assertTrue(result);
        verify(biPredicateMock, times(1)).test(null, "otherValue");
    }

    @Test
    public void testTest_Method_Call_BiPredicate_With_Multiple_Values() {
        // Given
        when(biPredicateMock.test(any(), any())).thenReturn(true);

        // When
        countingPredicate.test("otherValue1");
        countingPredicate.test("otherValue2");
        countingPredicate.test("otherValue3");
        boolean result = countingPredicate.test("otherValue4");

        // Then
        assertTrue(result);
        verify(biPredicateMock, times(1)).test("value1", "otherValue1");
        verify(biPredicateMock, times(1)).test("value2", "otherValue2");
        verify(biPredicateMock, times(1)).test("value3", "otherValue3");
        verify(biPredicateMock, times(1)).test(null, "otherValue4");
    }

    @Test
    public void testProcessRemaining_Method_Call_BiPredicate_With_Null_Value() {
        // Given
        when(biPredicateMock.test(any(), any())).thenReturn(true);

        // When
        countingPredicate.test("otherValue1");
        boolean result = countingPredicate.processRemaining();

        // Then
        assertTrue(result);
        verify(biPredicateMock, times(1)).test("value1", "otherValue1");
        verify(biPredicateMock, times(2)).test("value2", null);
        verify(biPredicateMock, times(1)).test("value3", null);
    }

    @Test
    public void testProcessRemaining_Method_Call_BiPredicate_With_Null_Value_False() {
        // Given
        when(biPredicateMock.test(any(), any())).thenReturn(false);

        // When
        countingPredicate.test("otherValue1");
        boolean result = countingPredicate.processRemaining();

        // Then
        assertFalse(result);
        verify(biPredicateMock, times(1)).test("value1", "otherValue1");
        verify(biPredicateMock, times(1)).test("value2", null);
    }
}
import org.apache.commons.collections4.bloomfilter.CountingLongPredicate;
import org.apache.commons.collections4.bloomfilter.LongBiPredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CountingLongPredicateTest {

    @Mock
    private LongBiPredicate func;

    private long[] ary;
    private CountingLongPredicate countingLongPredicate;

    @BeforeEach
    void setup() {
        ary = new long[]{1, 2, 3};
        countingLongPredicate = new CountingLongPredicate(ary, func);
    }

    @Test
    void testConstructor() {
        // Given
        long[] ary = new long[]{1, 2, 3};
        LongBiPredicate func = mock(LongBiPredicate.class);

        // When
        CountingLongPredicate countingLongPredicate = new CountingLongPredicate(ary, func);

        // Then
        assertNotNull(countingLongPredicate);
    }

    @Test
    void testTest_FirstElement() {
        // Given
        when(func.test(anyLong(), anyLong())).thenReturn(true);

        // When
        boolean result = countingLongPredicate.test(10);

        // Then
        assertTrue(result);
        verify(func, times(1)).test(1, 10);
    }

    @Test
    void testTest_SecondElement() {
        // Given
        when(func.test(anyLong(), anyLong())).thenReturn(true);
        countingLongPredicate.test(10); // Consume first element

        // When
        boolean result = countingLongPredicate.test(20);

        // Then
        assertTrue(result);
        verify(func, times(1)).test(2, 20);
    }

    @Test
    void testTest_ThirdElement() {
        // Given
        when(func.test(anyLong(), anyLong())).thenReturn(true);
        countingLongPredicate.test(10); // Consume first element
        countingLongPredicate.test(20); // Consume second element

        // When
        boolean result = countingLongPredicate.test(30);

        // Then
        assertTrue(result);
        verify(func, times(1)).test(3, 30);
    }

    @Test
    void testTest_AfterArrayExhausted() {
        // Given
        when(func.test(anyLong(), anyLong())).thenReturn(true);
        countingLongPredicate.test(10); // Consume first element
        countingLongPredicate.test(20); // Consume second element
        countingLongPredicate.test(30); // Consume third element

        // When
        boolean result = countingLongPredicate.test(40);

        // Then
        assertTrue(result);
        verify(func, times(1)).test(0, 40);
    }

    @Test
    void testProcessRemaining_NoRemainingElements() {
        // Given
        when(func.test(anyLong(), anyLong())).thenReturn(true);
        countingLongPredicate.test(10); // Consume first element
        countingLongPredicate.test(20); // Consume second element
        countingLongPredicate.test(30); // Consume third element

        // When
        boolean result = countingLongPredicate.processRemaining();

        // Then
        assertTrue(result);
        verify(func, never()).test(anyLong(), eq(0L));
    }

    @Test
    void testProcessRemaining_OneRemainingElement() {
        // Given
        when(func.test(anyLong(), anyLong())).thenReturn(true);
        countingLongPredicate.test(10); // Consume first element
        countingLongPredicate.test(20); // Consume second element

        // When
        boolean result = countingLongPredicate.processRemaining();

        // Then
        assertTrue(result);
        verify(func, times(1)).test(3, 0);
    }

    @Test
    void testProcessRemaining_TwoRemainingElements() {
        // Given
        when(func.test(anyLong(), anyLong())).thenReturn(true);
        countingLongPredicate.test(10); // Consume first element

        // When
        boolean result = countingLongPredicate.processRemaining();

        // Then
        assertTrue(result);
        verify(func, times(1)).test(2, 0);
        verify(func, times(1)).test(3, 0);
    }

    @Test
    void testProcessRemaining_AllRemainingElements() {
        // Given
        when(func.test(anyLong(), anyLong())).thenReturn(true);

        // When
        boolean result = countingLongPredicate.processRemaining();

        // Then
        assertTrue(result);
        verify(func, times(1)).test(1, 0);
        verify(func, times(1)).test(2, 0);
        verify(func, times(1)).test(3, 0);
    }
}
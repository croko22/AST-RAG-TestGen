import org.apache.commons.collections4.bag.AbstractSortedBagDecorator;
import org.apache.commons.collections4.bag.SortedBag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractSortedBagDecoratorTest {

    @Mock
    private SortedBag<String> sortedBag;

    @Mock
    private Comparator<String> comparator;

    private AbstractSortedBagDecorator<String> decorator;

    @BeforeEach
    void setup() {
        decorator = new AbstractSortedBagDecorator<>(sortedBag) {
            // No-op implementation for testing purposes
        };
    }

    @Test
    void testComparator() {
        // Given
        when(sortedBag.comparator()).thenReturn(comparator);

        // When
        Comparator<? super String> result = decorator.comparator();

        // Then
        assertEquals(comparator, result);
        verify(sortedBag, times(1)).comparator();
    }

    @Test
    void testFirst() {
        // Given
        String firstElement = "first";
        when(sortedBag.first()).thenReturn(firstElement);

        // When
        String result = decorator.first();

        // Then
        assertEquals(firstElement, result);
        verify(sortedBag, times(1)).first();
    }

    @Test
    void testLast() {
        // Given
        String lastElement = "last";
        when(sortedBag.last()).thenReturn(lastElement);

        // When
        String result = decorator.last();

        // Then
        assertEquals(lastElement, result);
        verify(sortedBag, times(1)).last();
    }

    @Test
    void testComparatorNull() {
        // Given
        when(sortedBag.comparator()).thenReturn(null);

        // When
        Comparator<? super String> result = decorator.comparator();

        // Then
        assertNull(result);
        verify(sortedBag, times(1)).comparator();
    }

    @Test
    void testFirstNull() {
        // Given
        when(sortedBag.first()).thenReturn(null);

        // When
        String result = decorator.first();

        // Then
        assertNull(result);
        verify(sortedBag, times(1)).first();
    }

    @Test
    void testLastNull() {
        // Given
        when(sortedBag.last()).thenReturn(null);

        // When
        String result = decorator.last();

        // Then
        assertNull(result);
        verify(sortedBag, times(1)).last();
    }
}
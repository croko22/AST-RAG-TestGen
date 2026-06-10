import org.apache.commons.collections4.ComparatorUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransformingComparatorTest {

    @Mock
    private Transformer<String, Integer> transformerMock;

    @Mock
    private Comparator<Integer> comparatorMock;

    private TransformingComparator<String, Integer> transformingComparator;

    @BeforeEach
    public void setup() {
        transformingComparator = new TransformingComparator<>(transformerMock, comparatorMock);
    }

    @Test
    public void testCompare() {
        // Given
        String obj1 = "obj1";
        String obj2 = "obj2";
        when(transformerMock.apply(obj1)).thenReturn(1);
        when(transformerMock.apply(obj2)).thenReturn(2);
        when(comparatorMock.compare(1, 2)).thenReturn(-1);

        // When
        int result = transformingComparator.compare(obj1, obj2);

        // Then
        assertEquals(-1, result);
    }

    @Test
    public void testEquals_SameInstance() {
        // Given
        TransformingComparator<String, Integer> sameInstance = transformingComparator;

        // When
        boolean result = transformingComparator.equals(sameInstance);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_Null() {
        // Given
        TransformingComparator<String, Integer> nullInstance = null;

        // When
        boolean result = transformingComparator.equals(nullInstance);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentClass() {
        // Given
        Object differentClass = new Object();

        // When
        boolean result = transformingComparator.equals(differentClass);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals_SameAttributes() {
        // Given
        TransformingComparator<String, Integer> sameAttributes = new TransformingComparator<>(transformerMock, comparatorMock);

        // When
        boolean result = transformingComparator.equals(sameAttributes);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentAttributes() {
        // Given
        @Mock
        private Transformer<String, Integer> differentTransformerMock;
        @Mock
        private Comparator<Integer> differentComparatorMock;
        TransformingComparator<String, Integer> differentAttributes = new TransformingComparator<>(differentTransformerMock, differentComparatorMock);

        // When
        boolean result = transformingComparator.equals(differentAttributes);

        // Then
        assertFalse(result);
    }

    @Test
    public void testHashCode() {
        // Given
        when(transformerMock.hashCode()).thenReturn(1);
        when(comparatorMock.hashCode()).thenReturn(2);

        // When
        int result = transformingComparator.hashCode();

        // Then
        assertEquals(17 * 37 * 37 + 1 * 37 + 2, result);
    }
}
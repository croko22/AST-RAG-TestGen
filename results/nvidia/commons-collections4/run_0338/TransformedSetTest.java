import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.collection.TransformedCollection;
import org.apache.commons.collections4.set.TransformedSet;
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
public class TransformedSetTest {

    @Mock
    private Set<String> set;

    @Mock
    private Transformer<String, String> transformer;

    private TransformedSet<String> transformedSet;

    @BeforeEach
    void setup() {
        transformedSet = new TransformedSet<>(set, transformer);
    }

    @Test
    public void testTransformedSet() {
        // Given
        when(set.isEmpty()).thenReturn(true);

        // When
        TransformedSet<String> result = TransformedSet.transformedSet(set, transformer);

        // Then
        assertNotNull(result);
        verify(set, times(1)).isEmpty();
    }

    @Test
    public void testTransformedSet_ExistingElements() {
        // Given
        when(set.isEmpty()).thenReturn(false);
        String[] values = {"value1", "value2"};
        when(set.toArray()).thenReturn(values);
        when(transformer.apply(any())).thenReturn("transformedValue");

        // When
        TransformedSet<String> result = TransformedSet.transformedSet(set, transformer);

        // Then
        assertNotNull(result);
        verify(set, times(1)).isEmpty();
        verify(set, times(1)).toArray();
        verify(transformer, times(2)).apply(any());
    }

    @Test
    public void testTransformingSet() {
        // Given
        when(set.isEmpty()).thenReturn(true);

        // When
        TransformedSet<String> result = TransformedSet.transformingSet(set, transformer);

        // Then
        assertNotNull(result);
        verify(set, times(1)).isEmpty();
    }

    @Test
    public void testEquals_SameObject() {
        // Given
        Object object = transformedSet;

        // When
        boolean result = transformedSet.equals(object);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_SameDecorated() {
        // Given
        Set<String> sameSet = set;
        TransformedSet<String> sameTransformedSet = new TransformedSet<>(sameSet, transformer);
        Object object = sameTransformedSet;

        // When
        boolean result = transformedSet.equals(object);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_DifferentDecorated() {
        // Given
        Set<String> differentSet = mock(Set.class);
        TransformedSet<String> differentTransformedSet = new TransformedSet<>(differentSet, transformer);
        Object object = differentTransformedSet;

        // When
        boolean result = transformedSet.equals(object);

        // Then
        assertFalse(result);
    }

    @Test
    public void testHashCode() {
        // Given
        int expectedHashCode = 123;
        when(set.hashCode()).thenReturn(expectedHashCode);

        // When
        int result = transformedSet.hashCode();

        // Then
        assertEquals(expectedHashCode, result);
    }
}
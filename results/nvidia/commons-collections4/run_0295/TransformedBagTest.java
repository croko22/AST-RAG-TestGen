import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.collection.TransformedCollection;
import org.apache.commons.collections4.set.TransformedSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransformedBagTest {

    @Mock
    private Bag<String> bag;

    @Mock
    private Transformer<String, String> transformer;

    private TransformedBag<String> transformedBag;

    @BeforeEach
    void setup() {
        transformedBag = new TransformedBag<>(bag, transformer);
    }

    @Test
    public void testTransformedBag() {
        // Given
        when(bag.isEmpty()).thenReturn(false);
        String[] values = {"value1", "value2"};
        when(bag.toArray()).thenReturn(values);

        // When
        TransformedBag<String> result = TransformedBag.transformedBag(bag, transformer);

        // Then
        assertNotNull(result);
        verify(bag, times(1)).isEmpty();
        verify(bag, times(1)).toArray();
        for (String value : values) {
            verify(bag, times(1)).add(any(), anyInt());
        }
    }

    @Test
    public void testTransformingBag() {
        // Given

        // When
        TransformedBag<String> result = TransformedBag.transformingBag(bag, transformer);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testAdd() {
        // Given
        String object = "object";
        int nCopies = 2;
        when(transformer.apply(object)).thenReturn("transformedObject");

        // When
        boolean result = transformedBag.add(object, nCopies);

        // Then
        assertTrue(result);
        verify(bag, times(1)).add("transformedObject", nCopies);
    }

    @Test
    public void testEquals() {
        // Given
        Object object = new Object();

        // When
        boolean result = transformedBag.equals(object);

        // Then
        assertFalse(result);
        verify(bag, times(1)).equals(object);
    }

    @Test
    public void testGetCount() {
        // Given
        Object object = new Object();
        int count = 2;
        when(bag.getCount(object)).thenReturn(count);

        // When
        int result = transformedBag.getCount(object);

        // Then
        assertEquals(count, result);
        verify(bag, times(1)).getCount(object);
    }

    @Test
    public void testHashCode() {
        // Given
        int hashCode = 123;
        when(bag.hashCode()).thenReturn(hashCode);

        // When
        int result = transformedBag.hashCode();

        // Then
        assertEquals(hashCode, result);
        verify(bag, times(1)).hashCode();
    }

    @Test
    public void testRemove() {
        // Given
        Object object = new Object();
        int nCopies = 2;
        boolean resultRemove = true;
        when(bag.remove(object, nCopies)).thenReturn(resultRemove);

        // When
        boolean result = transformedBag.remove(object, nCopies);

        // Then
        assertTrue(result);
        verify(bag, times(1)).remove(object, nCopies);
    }

    @Test
    public void testUniqueSet() {
        // Given
        Set<String> set = mock(Set.class);
        when(bag.uniqueSet()).thenReturn(set);
        TransformedSet<String> transformedSet = mock(TransformedSet.class);
        when(transformedSet.transform(any())).thenReturn(set);

        // When
        Set<String> result = transformedBag.uniqueSet();

        // Then
        assertNotNull(result);
        verify(bag, times(1)).uniqueSet();
    }
}
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.iterators.ObjectGraphIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ObjectGraphIteratorTest {

    @Mock
    private Transformer<Object, Object> transformer;

    private ObjectGraphIterator<Object> objectGraphIterator;

    @BeforeEach
    void setup() {
        objectGraphIterator = new ObjectGraphIterator<>(new ArrayList<>(), transformer);
    }

    @Test
    void testHasNext_EmptyIterator_ReturnsFalse() {
        // Given
        Iterator<Object> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(false);

        // When
        objectGraphIterator = new ObjectGraphIterator<>(iterator, transformer);

        // Then
        assertFalse(objectGraphIterator.hasNext());
    }

    @Test
    void testHasNext_NonEmptyIterator_ReturnsTrue() {
        // Given
        Iterator<Object> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true);

        // When
        objectGraphIterator = new ObjectGraphIterator<>(iterator, transformer);

        // Then
        assertTrue(objectGraphIterator.hasNext());
    }

    @Test
    void testNext_EmptyIterator_ThrowsException() {
        // Given
        Iterator<Object> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(false);

        // When
        objectGraphIterator = new ObjectGraphIterator<>(iterator, transformer);

        // Then
        assertThrows(NoSuchElementException.class, () -> objectGraphIterator.next());
    }

    @Test
    void testNext_NonEmptyIterator_ReturnsNextElement() {
        // Given
        Iterator<Object> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true);
        Object nextElement = new Object();
        when(iterator.next()).thenReturn(nextElement);

        // When
        objectGraphIterator = new ObjectGraphIterator<>(iterator, transformer);

        // Then
        assertEquals(nextElement, objectGraphIterator.next());
    }

    @Test
    void testRemove_EmptyIterator_ThrowsException() {
        // Given
        Iterator<Object> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(false);

        // When
        objectGraphIterator = new ObjectGraphIterator<>(iterator, transformer);

        // Then
        assertThrows(IllegalStateException.class, () -> objectGraphIterator.remove());
    }

    @Test
    void testRemove_NonEmptyIterator_RemovesLastElement() {
        // Given
        Iterator<Object> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true);
        Object nextElement = new Object();
        when(iterator.next()).thenReturn(nextElement);
        doNothing().when(iterator).remove();

        // When
        objectGraphIterator = new ObjectGraphIterator<>(iterator, transformer);
        objectGraphIterator.next();

        // Then
        objectGraphIterator.remove();
        verify(iterator, times(1)).remove();
    }

    @Test
    void testTransformer_Apply_ReturnsTransformedObject() {
        // Given
        Object input = new Object();
        Object transformedObject = new Object();
        when(transformer.apply(input)).thenReturn(transformedObject);

        // When
        Object result = transformer.apply(input);

        // Then
        assertEquals(transformedObject, result);
    }

    @Test
    void testTransformer_Transform_ReturnsTransformedObject() {
        // Given
        Object input = new Object();
        Object transformedObject = new Object();
        when(transformer.transform(input)).thenReturn(transformedObject);

        // When
        Object result = transformer.transform(input);

        // Then
        assertEquals(transformedObject, result);
    }
}
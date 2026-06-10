import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.iterators.TransformIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransformIteratorTest {

    @Mock
    private Iterator<String> iterator;

    @Mock
    private Transformer<String, String> transformer;

    private TransformIterator<String, String> transformIterator;

    @BeforeEach
    public void setup() {
        transformIterator = new TransformIterator<>();
    }

    @Test
    public void testGetIterator() {
        // Given
        transformIterator.setIterator(iterator);

        // When
        Iterator<? extends String> result = transformIterator.getIterator();

        // Then
        assertEquals(iterator, result);
    }

    @Test
    public void testGetTransformer() {
        // Given
        transformIterator.setTransformer(transformer);

        // When
        Transformer<? super String, ? extends String> result = transformIterator.getTransformer();

        // Then
        assertEquals(transformer, result);
    }

    @Test
    public void testHasNext() {
        // Given
        when(iterator.hasNext()).thenReturn(true);
        transformIterator.setIterator(iterator);

        // When
        boolean result = transformIterator.hasNext();

        // Then
        assertTrue(result);
        verify(iterator, times(1)).hasNext();
    }

    @Test
    public void testNext() {
        // Given
        when(iterator.next()).thenReturn("test");
        when(transformer.apply(any())).thenReturn("transformed");
        transformIterator.setIterator(iterator);
        transformIterator.setTransformer(transformer);

        // When
        String result = transformIterator.next();

        // Then
        assertEquals("transformed", result);
        verify(iterator, times(1)).next();
        verify(transformer, times(1)).apply(any());
    }

    @Test
    public void testNextWithoutTransformer() {
        // Given
        when(iterator.next()).thenReturn("test");
        transformIterator.setIterator(iterator);

        // When
        String result = transformIterator.next();

        // Then
        assertEquals("test", result);
        verify(iterator, times(1)).next();
    }

    @Test
    public void testNextWithNullTransformer() {
        // Given
        when(iterator.next()).thenReturn("test");
        transformIterator.setIterator(iterator);
        transformIterator.setTransformer(null);

        // When
        String result = transformIterator.next();

        // Then
        assertEquals("test", result);
        verify(iterator, times(1)).next();
    }

    @Test
    public void testNextWithoutIterator() {
        // Given
        assertThrows(NullPointerException.class, () -> transformIterator.next());
    }

    @Test
    public void testRemove() {
        // Given
        transformIterator.setIterator(iterator);

        // When
        transformIterator.remove();

        // Then
        verify(iterator, times(1)).remove();
    }

    @Test
    public void testRemoveWithoutIterator() {
        // Given
        assertThrows(NullPointerException.class, () -> transformIterator.remove());
    }

    @Test
    public void testSetIterator() {
        // Given
        transformIterator.setIterator(iterator);

        // When
        TransformIterator<String, String> newIterator = new TransformIterator<>();
        newIterator.setIterator(iterator);

        // Then
        assertEquals(iterator, newIterator.getIterator());
    }

    @Test
    public void testSetTransformer() {
        // Given
        transformIterator.setTransformer(transformer);

        // When
        TransformIterator<String, String> newIterator = new TransformIterator<>();
        newIterator.setTransformer(transformer);

        // Then
        assertEquals(transformer, newIterator.getTransformer());
    }

    @Test
    public void testSetTransformerToNull() {
        // Given
        transformIterator.setTransformer(transformer);
        transformIterator.setTransformer(null);

        // When
        String result = transformIterator.getTransformer();

        // Then
        assertNull(result);
    }

    @Test
    public void testTransform() {
        // Given
        when(transformer.apply(any())).thenReturn("transformed");
        transformIterator.setTransformer(transformer);

        // When
        String result = transformIterator.transform("test");

        // Then
        assertEquals("transformed", result);
        verify(transformer, times(1)).apply(any());
    }

    @Test
    public void testTransformWithoutTransformer() {
        // Given
        transformIterator.setTransformer(null);

        // When
        String result = transformIterator.transform("test");

        // Then
        assertEquals("test", result);
    }

    @Test
    public void testHasNextWithoutIterator() {
        // Given
        assertThrows(NullPointerException.class, () -> transformIterator.hasNext());
    }

    @Test
    public void testNextWithoutNextElement() {
        // Given
        when(iterator.hasNext()).thenReturn(false);
        transformIterator.setIterator(iterator);

        // When
        assertThrows(NoSuchElementException.class, () -> transformIterator.next());
    }
}
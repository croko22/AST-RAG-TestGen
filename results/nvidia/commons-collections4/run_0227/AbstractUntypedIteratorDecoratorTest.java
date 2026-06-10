import org.apache.commons.collections4.iterators.AbstractUntypedIteratorDecorator;
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
public class AbstractUntypedIteratorDecoratorTest {

    @Mock
    private Iterator<Integer> iterator;

    private AbstractUntypedIteratorDecorator<Integer, Integer> decorator;

    @BeforeEach
    void setup() {
        decorator = new AbstractUntypedIteratorDecorator<Integer, Integer>(iterator) {
            @Override
            public Integer next() {
                return iterator.next();
            }
        };
    }

    @Test
    void testConstructor_NullIterator_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AbstractUntypedIteratorDecorator<Integer, Integer>(null));
    }

    @Test
    void testHasNext_ReturnsTrue_WhenIteratorHasNext() {
        when(iterator.hasNext()).thenReturn(true);
        assertTrue(decorator.hasNext());
        verify(iterator, times(1)).hasNext();
    }

    @Test
    void testHasNext_ReturnsFalse_WhenIteratorDoesNotHaveNext() {
        when(iterator.hasNext()).thenReturn(false);
        assertFalse(decorator.hasNext());
        verify(iterator, times(1)).hasNext();
    }

    @Test
    void testRemove_InvokesRemoveOnIterator() {
        decorator.remove();
        verify(iterator, times(1)).remove();
    }

    @Test
    void testRemove_ThrowsUnsupportedOperationException_WhenIteratorDoesNotSupportRemove() {
        doThrow(new UnsupportedOperationException()).when(iterator).remove();
        assertThrows(UnsupportedOperationException.class, () -> decorator.remove());
        verify(iterator, times(1)).remove();
    }
}
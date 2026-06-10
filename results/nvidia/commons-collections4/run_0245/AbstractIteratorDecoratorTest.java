import org.apache.commons.collections4.iterators.AbstractIteratorDecorator;
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
public class AbstractIteratorDecoratorTest {

    @Mock
    private Iterator<String> iterator;

    private AbstractIteratorDecorator<String> decorator;

    @BeforeEach
    public void setup() {
        decorator = new AbstractIteratorDecorator<>(iterator) {};
    }

    @Test
    public void testNext() {
        // Given
        String expected = "element";
        when(iterator.next()).thenReturn(expected);

        // When
        String actual = decorator.next();

        // Then
        assertEquals(expected, actual);
        verify(iterator, times(1)).next();
    }

    @Test
    public void testNext_NoSuchElementException() {
        // Given
        when(iterator.next()).thenThrow(NoSuchElementException.class);

        // When / Then
        assertThrows(NoSuchElementException.class, () -> decorator.next());
        verify(iterator, times(1)).next();
    }

    @Test
    public void testConstructor_NullIterator() {
        // Given / When / Then
        assertThrows(NullPointerException.class, () -> new AbstractIteratorDecorator<>(null) {});
    }
}
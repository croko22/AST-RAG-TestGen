import org.apache.commons.collections4.iterators.AbstractListIteratorDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractListIteratorDecoratorTest {

    @Mock
    private ListIterator<String> listIterator;

    private AbstractListIteratorDecorator<String> abstractListIteratorDecorator;

    @BeforeEach
    public void setup() {
        abstractListIteratorDecorator = new AbstractListIteratorDecorator<>(listIterator);
    }

    @Test
    public void testAdd() {
        // Given
        String obj = "Test Object";

        // When
        abstractListIteratorDecorator.add(obj);

        // Then
        verify(listIterator, times(1)).add(obj);
    }

    @Test
    public void testHasNext() {
        // Given
        when(listIterator.hasNext()).thenReturn(true);

        // When
        boolean result = abstractListIteratorDecorator.hasNext();

        // Then
        assertTrue(result);
        verify(listIterator, times(1)).hasNext();
    }

    @Test
    public void testHasPrevious() {
        // Given
        when(listIterator.hasPrevious()).thenReturn(true);

        // When
        boolean result = abstractListIteratorDecorator.hasPrevious();

        // Then
        assertTrue(result);
        verify(listIterator, times(1)).hasPrevious();
    }

    @Test
    public void testNext() {
        // Given
        String nextObject = "Next Object";
        when(listIterator.next()).thenReturn(nextObject);

        // When
        String result = abstractListIteratorDecorator.next();

        // Then
        assertEquals(nextObject, result);
        verify(listIterator, times(1)).next();
    }

    @Test
    public void testNextIndex() {
        // Given
        int nextIndex = 10;
        when(listIterator.nextIndex()).thenReturn(nextIndex);

        // When
        int result = abstractListIteratorDecorator.nextIndex();

        // Then
        assertEquals(nextIndex, result);
        verify(listIterator, times(1)).nextIndex();
    }

    @Test
    public void testPrevious() {
        // Given
        String previousObject = "Previous Object";
        when(listIterator.previous()).thenReturn(previousObject);

        // When
        String result = abstractListIteratorDecorator.previous();

        // Then
        assertEquals(previousObject, result);
        verify(listIterator, times(1)).previous();
    }

    @Test
    public void testPreviousIndex() {
        // Given
        int previousIndex = 5;
        when(listIterator.previousIndex()).thenReturn(previousIndex);

        // When
        int result = abstractListIteratorDecorator.previousIndex();

        // Then
        assertEquals(previousIndex, result);
        verify(listIterator, times(1)).previousIndex();
    }

    @Test
    public void testRemove() {
        // When
        abstractListIteratorDecorator.remove();

        // Then
        verify(listIterator, times(1)).remove();
    }

    @Test
    public void testSet() {
        // Given
        String obj = "Test Object";

        // When
        abstractListIteratorDecorator.set(obj);

        // Then
        verify(listIterator, times(1)).set(obj);
    }

    @Test
    public void testConstructor_NullListIterator() {
        // Given
        ListIterator<String> nullListIterator = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> new AbstractListIteratorDecorator<>(nullListIterator));
    }

    @Test
    public void testConstructor_ValidListIterator() {
        // Given
        List<String> list = new ArrayList<>();
        list.add("Test Object");
        ListIterator<String> validListIterator = list.listIterator();

        // When
        AbstractListIteratorDecorator<String> abstractListIteratorDecorator = new AbstractListIteratorDecorator<>(validListIterator);

        // Then
        assertNotNull(abstractListIteratorDecorator);
    }
}
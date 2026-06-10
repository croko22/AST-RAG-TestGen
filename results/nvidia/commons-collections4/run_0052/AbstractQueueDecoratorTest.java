import org.apache.commons.collections4.queue.AbstractQueueDecorator;
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
public class AbstractQueueDecoratorTest {

    @Mock
    private Queue<String> queue;

    private AbstractQueueDecorator<String> decorator;

    @BeforeEach
    void setup() {
        decorator = new AbstractQueueDecorator<String>(queue) {
        };
    }

    @Test
    void testElement() {
        // Given
        String element = "test";
        when(queue.element()).thenReturn(element);

        // When
        String result = decorator.element();

        // Then
        assertEquals(element, result);
        verify(queue, times(1)).element();
    }

    @Test
    void testElement_ThrowsNoSuchElementException() {
        // Given
        when(queue.element()).thenThrow(new NoSuchElementException());

        // When and Then
        assertThrows(NoSuchElementException.class, () -> decorator.element());
        verify(queue, times(1)).element();
    }

    @Test
    void testOffer() {
        // Given
        String obj = "test";
        when(queue.offer(obj)).thenReturn(true);

        // When
        boolean result = decorator.offer(obj);

        // Then
        assertTrue(result);
        verify(queue, times(1)).offer(obj);
    }

    @Test
    void testOffer_ReturnsFalse() {
        // Given
        String obj = "test";
        when(queue.offer(obj)).thenReturn(false);

        // When
        boolean result = decorator.offer(obj);

        // Then
        assertFalse(result);
        verify(queue, times(1)).offer(obj);
    }

    @Test
    void testPeek() {
        // Given
        String element = "test";
        when(queue.peek()).thenReturn(element);

        // When
        String result = decorator.peek();

        // Then
        assertEquals(element, result);
        verify(queue, times(1)).peek();
    }

    @Test
    void testPeek_ReturnsNull() {
        // Given
        when(queue.peek()).thenReturn(null);

        // When
        String result = decorator.peek();

        // Then
        assertNull(result);
        verify(queue, times(1)).peek();
    }

    @Test
    void testPoll() {
        // Given
        String element = "test";
        when(queue.poll()).thenReturn(element);

        // When
        String result = decorator.poll();

        // Then
        assertEquals(element, result);
        verify(queue, times(1)).poll();
    }

    @Test
    void testPoll_ReturnsNull() {
        // Given
        when(queue.poll()).thenReturn(null);

        // When
        String result = decorator.poll();

        // Then
        assertNull(result);
        verify(queue, times(1)).poll();
    }

    @Test
    void testRemove() {
        // Given
        String element = "test";
        when(queue.remove()).thenReturn(element);

        // When
        String result = decorator.remove();

        // Then
        assertEquals(element, result);
        verify(queue, times(1)).remove();
    }

    @Test
    void testRemove_ThrowsNoSuchElementException() {
        // Given
        when(queue.remove()).thenThrow(new NoSuchElementException());

        // When and Then
        assertThrows(NoSuchElementException.class, () -> decorator.remove());
        verify(queue, times(1)).remove();
    }
}
import org.apache.commons.collections4.iterators.PushbackIterator;
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
public class PushbackIteratorTest {

    @Mock
    private Iterator<String> iterator;

    private PushbackIterator<String> pushbackIterator;

    @BeforeEach
    void setup() {
        pushbackIterator = PushbackIterator.pushbackIterator(iterator);
    }

    @Test
    void testPushbackIterator_NullIterator_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> PushbackIterator.pushbackIterator(null));
    }

    @Test
    void testPushbackIterator_AlreadyPushbackIterator_ReturnsSameInstance() {
        PushbackIterator<String> pushbackIterator = mock(PushbackIterator.class);
        when(pushbackIterator.hasNext()).thenReturn(true);
        PushbackIterator<String> result = PushbackIterator.pushbackIterator(pushbackIterator);
        assertSame(pushbackIterator, result);
    }

    @Test
    void testHasNext_NoElementsInQueue_NoMoreElementsInIterator_ReturnsFalse() {
        when(iterator.hasNext()).thenReturn(false);
        assertFalse(pushbackIterator.hasNext());
    }

    @Test
    void testHasNext_NoElementsInQueue_MoreElementsInIterator_ReturnsTrue() {
        when(iterator.hasNext()).thenReturn(true);
        assertTrue(pushbackIterator.hasNext());
    }

    @Test
    void testHasNext_ElementsInQueue_NoMoreElementsInIterator_ReturnsTrue() {
        pushbackIterator.pushback("element");
        assertTrue(pushbackIterator.hasNext());
    }

    @Test
    void testNext_NoElementsInQueue_NoMoreElementsInIterator_ThrowsNoSuchElementException() {
        when(iterator.hasNext()).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> pushbackIterator.next());
    }

    @Test
    void testNext_NoElementsInQueue_MoreElementsInIterator_ReturnsNextElementFromIterator() {
        when(iterator.hasNext()).thenReturn(true);
        when(iterator.next()).thenReturn("element");
        assertEquals("element", pushbackIterator.next());
    }

    @Test
    void testNext_ElementsInQueue_NoMoreElementsInIterator_ReturnsNextElementFromQueue() {
        pushbackIterator.pushback("element");
        assertEquals("element", pushbackIterator.next());
    }

    @Test
    void testPushback_Element_PushedBackToQueue() {
        pushbackIterator.pushback("element");
        assertEquals("element", pushbackIterator.next());
    }

    @Test
    void testRemove_AlwaysThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> pushbackIterator.remove());
    }

    @Test
    void testMultiplePushback_ElementsPushedBackToQueueInReverseOrder() {
        pushbackIterator.pushback("element1");
        pushbackIterator.pushback("element2");
        assertEquals("element2", pushbackIterator.next());
        assertEquals("element1", pushbackIterator.next());
    }

    @Test
    void testPushbackIterator_IteratorWithMultipleElements_IteratesOverAllElements() {
        List<String> elements = new ArrayList<>();
        elements.add("element1");
        elements.add("element2");
        elements.add("element3");
        Iterator<String> iterator = elements.iterator();
        PushbackIterator<String> pushbackIterator = PushbackIterator.pushbackIterator(iterator);
        assertTrue(pushbackIterator.hasNext());
        assertEquals("element1", pushbackIterator.next());
        pushbackIterator.pushback("pushedBackElement");
        assertTrue(pushbackIterator.hasNext());
        assertEquals("pushedBackElement", pushbackIterator.next());
        assertTrue(pushbackIterator.hasNext());
        assertEquals("element2", pushbackIterator.next());
        assertTrue(pushbackIterator.hasNext());
        assertEquals("element3", pushbackIterator.next());
        assertFalse(pushbackIterator.hasNext());
    }
}
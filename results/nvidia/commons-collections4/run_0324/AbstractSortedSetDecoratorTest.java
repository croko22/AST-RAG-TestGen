import org.apache.commons.collections4.set.AbstractSortedSetDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractSortedSetDecoratorTest {

    @Mock
    private SortedSet<String> decoratedSet;

    private AbstractSortedSetDecorator<String> decorator;

    @BeforeEach
    void setup() {
        decorator = new AbstractSortedSetDecorator<>(decoratedSet) {
        };
    }

    @Test
    public void testComparator() {
        // Given
        Comparator<String> comparator = mock(Comparator.class);
        when(decoratedSet.comparator()).thenReturn(comparator);

        // When
        Comparator<? super String> result = decorator.comparator();

        // Then
        assertEquals(comparator, result);
        verify(decoratedSet, times(1)).comparator();
    }

    @Test
    public void testFirst() {
        // Given
        String firstElement = "first";
        when(decoratedSet.first()).thenReturn(firstElement);

        // When
        String result = decorator.first();

        // Then
        assertEquals(firstElement, result);
        verify(decoratedSet, times(1)).first();
    }

    @Test
    public void testHeadSet() {
        // Given
        String toElement = "toElement";
        SortedSet<String> headSet = mock(SortedSet.class);
        when(decoratedSet.headSet(toElement)).thenReturn(headSet);

        // When
        SortedSet<String> result = decorator.headSet(toElement);

        // Then
        assertEquals(headSet, result);
        verify(decoratedSet, times(1)).headSet(toElement);
    }

    @Test
    public void testLast() {
        // Given
        String lastElement = "last";
        when(decoratedSet.last()).thenReturn(lastElement);

        // When
        String result = decorator.last();

        // Then
        assertEquals(lastElement, result);
        verify(decoratedSet, times(1)).last();
    }

    @Test
    public void testSubSet() {
        // Given
        String fromElement = "fromElement";
        String toElement = "toElement";
        SortedSet<String> subSet = mock(SortedSet.class);
        when(decoratedSet.subSet(fromElement, toElement)).thenReturn(subSet);

        // When
        SortedSet<String> result = decorator.subSet(fromElement, toElement);

        // Then
        assertEquals(subSet, result);
        verify(decoratedSet, times(1)).subSet(fromElement, toElement);
    }

    @Test
    public void testTailSet() {
        // Given
        String fromElement = "fromElement";
        SortedSet<String> tailSet = mock(SortedSet.class);
        when(decoratedSet.tailSet(fromElement)).thenReturn(tailSet);

        // When
        SortedSet<String> result = decorator.tailSet(fromElement);

        // Then
        assertEquals(tailSet, result);
        verify(decoratedSet, times(1)).tailSet(fromElement);
    }

    @Test
    public void testComparatorNull() {
        // Given
        when(decoratedSet.comparator()).thenReturn(null);

        // When
        Comparator<? super String> result = decorator.comparator();

        // Then
        assertNull(result);
        verify(decoratedSet, times(1)).comparator();
    }

    @Test
    public void testFirstNull() {
        // Given
        when(decoratedSet.first()).thenReturn(null);

        // When
        String result = decorator.first();

        // Then
        assertNull(result);
        verify(decoratedSet, times(1)).first();
    }

    @Test
    public void testHeadSetNull() {
        // Given
        String toElement = "toElement";
        when(decoratedSet.headSet(toElement)).thenReturn(null);

        // When
        SortedSet<String> result = decorator.headSet(toElement);

        // Then
        assertNull(result);
        verify(decoratedSet, times(1)).headSet(toElement);
    }

    @Test
    public void testLastNull() {
        // Given
        when(decoratedSet.last()).thenReturn(null);

        // When
        String result = decorator.last();

        // Then
        assertNull(result);
        verify(decoratedSet, times(1)).last();
    }

    @Test
    public void testSubSetNull() {
        // Given
        String fromElement = "fromElement";
        String toElement = "toElement";
        when(decoratedSet.subSet(fromElement, toElement)).thenReturn(null);

        // When
        SortedSet<String> result = decorator.subSet(fromElement, toElement);

        // Then
        assertNull(result);
        verify(decoratedSet, times(1)).subSet(fromElement, toElement);
    }

    @Test
    public void testTailSetNull() {
        // Given
        String fromElement = "fromElement";
        when(decoratedSet.tailSet(fromElement)).thenReturn(null);

        // When
        SortedSet<String> result = decorator.tailSet(fromElement);

        // Then
        assertNull(result);
        verify(decoratedSet, times(1)).tailSet(fromElement);
    }
}
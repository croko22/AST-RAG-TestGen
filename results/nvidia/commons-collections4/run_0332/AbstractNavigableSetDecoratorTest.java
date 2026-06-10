import org.apache.commons.collections4.set.AbstractNavigableSetDecorator;
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
public class AbstractNavigableSetDecoratorTest {

    @Mock
    private NavigableSet<String> navigableSet;

    private AbstractNavigableSetDecorator<String> abstractNavigableSetDecorator;

    @BeforeEach
    void setup() {
        abstractNavigableSetDecorator = new AbstractNavigableSetDecorator<>(navigableSet) {
        };
    }

    @Test
    void testCeiling() {
        // Given
        String element = "element";
        String ceilingElement = "ceilingElement";
        when(navigableSet.ceiling(element)).thenReturn(ceilingElement);

        // When
        String result = abstractNavigableSetDecorator.ceiling(element);

        // Then
        assertEquals(ceilingElement, result);
        verify(navigableSet, times(1)).ceiling(element);
    }

    @Test
    void testDescendingIterator() {
        // Given
        Iterator<String> iterator = mock(Iterator.class);
        when(navigableSet.descendingIterator()).thenReturn(iterator);

        // When
        Iterator<String> result = abstractNavigableSetDecorator.descendingIterator();

        // Then
        assertEquals(iterator, result);
        verify(navigableSet, times(1)).descendingIterator();
    }

    @Test
    void testDescendingSet() {
        // Given
        NavigableSet<String> descendingSet = mock(NavigableSet.class);
        when(navigableSet.descendingSet()).thenReturn(descendingSet);

        // When
        NavigableSet<String> result = abstractNavigableSetDecorator.descendingSet();

        // Then
        assertEquals(descendingSet, result);
        verify(navigableSet, times(1)).descendingSet();
    }

    @Test
    void testFloor() {
        // Given
        String element = "element";
        String floorElement = "floorElement";
        when(navigableSet.floor(element)).thenReturn(floorElement);

        // When
        String result = abstractNavigableSetDecorator.floor(element);

        // Then
        assertEquals(floorElement, result);
        verify(navigableSet, times(1)).floor(element);
    }

    @Test
    void testHeadSet() {
        // Given
        String toElement = "toElement";
        boolean inclusive = true;
        NavigableSet<String> headSet = mock(NavigableSet.class);
        when(navigableSet.headSet(toElement, inclusive)).thenReturn(headSet);

        // When
        NavigableSet<String> result = abstractNavigableSetDecorator.headSet(toElement, inclusive);

        // Then
        assertEquals(headSet, result);
        verify(navigableSet, times(1)).headSet(toElement, inclusive);
    }

    @Test
    void testHigher() {
        // Given
        String element = "element";
        String higherElement = "higherElement";
        when(navigableSet.higher(element)).thenReturn(higherElement);

        // When
        String result = abstractNavigableSetDecorator.higher(element);

        // Then
        assertEquals(higherElement, result);
        verify(navigableSet, times(1)).higher(element);
    }

    @Test
    void testLower() {
        // Given
        String element = "element";
        String lowerElement = "lowerElement";
        when(navigableSet.lower(element)).thenReturn(lowerElement);

        // When
        String result = abstractNavigableSetDecorator.lower(element);

        // Then
        assertEquals(lowerElement, result);
        verify(navigableSet, times(1)).lower(element);
    }

    @Test
    void testPollFirst() {
        // Given
        String firstElement = "firstElement";
        when(navigableSet.pollFirst()).thenReturn(firstElement);

        // When
        String result = abstractNavigableSetDecorator.pollFirst();

        // Then
        assertEquals(firstElement, result);
        verify(navigableSet, times(1)).pollFirst();
    }

    @Test
    void testPollLast() {
        // Given
        String lastElement = "lastElement";
        when(navigableSet.pollLast()).thenReturn(lastElement);

        // When
        String result = abstractNavigableSetDecorator.pollLast();

        // Then
        assertEquals(lastElement, result);
        verify(navigableSet, times(1)).pollLast();
    }

    @Test
    void testSubSet() {
        // Given
        String fromElement = "fromElement";
        boolean fromInclusive = true;
        String toElement = "toElement";
        boolean toInclusive = true;
        NavigableSet<String> subSet = mock(NavigableSet.class);
        when(navigableSet.subSet(fromElement, fromInclusive, toElement, toInclusive)).thenReturn(subSet);

        // When
        NavigableSet<String> result = abstractNavigableSetDecorator.subSet(fromElement, fromInclusive, toElement, toInclusive);

        // Then
        assertEquals(subSet, result);
        verify(navigableSet, times(1)).subSet(fromElement, fromInclusive, toElement, toInclusive);
    }

    @Test
    void testTailSet() {
        // Given
        String fromElement = "fromElement";
        boolean inclusive = true;
        NavigableSet<String> tailSet = mock(NavigableSet.class);
        when(navigableSet.tailSet(fromElement, inclusive)).thenReturn(tailSet);

        // When
        NavigableSet<String> result = abstractNavigableSetDecorator.tailSet(fromElement, inclusive);

        // Then
        assertEquals(tailSet, result);
        verify(navigableSet, times(1)).tailSet(fromElement, inclusive);
    }
}
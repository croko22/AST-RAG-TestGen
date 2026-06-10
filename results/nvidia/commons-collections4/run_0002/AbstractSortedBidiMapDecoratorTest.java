import org.apache.commons.collections4.bidimap.AbstractSortedBidiMapDecorator;
import org.apache.commons.collections4.bidimap.SortedBidiMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.SortedMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractSortedBidiMapDecoratorTest {

    @Mock
    private SortedBidiMap<String, Integer> sortedBidiMap;

    private AbstractSortedBidiMapDecorator<String, Integer> abstractSortedBidiMapDecorator;

    @BeforeEach
    void setup() {
        abstractSortedBidiMapDecorator = new AbstractSortedBidiMapDecorator<>(sortedBidiMap) {
        };
    }

    @Test
    void testComparator() {
        // Given
        Comparator<String> comparator = mock(Comparator.class);
        when(sortedBidiMap.comparator()).thenReturn(comparator);

        // When
        Comparator<String> result = abstractSortedBidiMapDecorator.comparator();

        // Then
        assertEquals(comparator, result);
        verify(sortedBidiMap, times(1)).comparator();
    }

    @Test
    void testHeadMap() {
        // Given
        SortedMap<String, Integer> headMap = mock(SortedMap.class);
        when(sortedBidiMap.headMap(any())).thenReturn(headMap);

        // When
        SortedMap<String, Integer> result = abstractSortedBidiMapDecorator.headMap("key");

        // Then
        assertEquals(headMap, result);
        verify(sortedBidiMap, times(1)).headMap("key");
    }

    @Test
    void testInverseBidiMap() {
        // Given
        SortedBidiMap<Integer, String> inverseBidiMap = mock(SortedBidiMap.class);
        when(sortedBidiMap.inverseBidiMap()).thenReturn(inverseBidiMap);

        // When
        SortedBidiMap<Integer, String> result = abstractSortedBidiMapDecorator.inverseBidiMap();

        // Then
        assertEquals(inverseBidiMap, result);
        verify(sortedBidiMap, times(1)).inverseBidiMap();
    }

    @Test
    void testSubMap() {
        // Given
        SortedMap<String, Integer> subMap = mock(SortedMap.class);
        when(sortedBidiMap.subMap(any(), any())).thenReturn(subMap);

        // When
        SortedMap<String, Integer> result = abstractSortedBidiMapDecorator.subMap("fromKey", "toKey");

        // Then
        assertEquals(subMap, result);
        verify(sortedBidiMap, times(1)).subMap("fromKey", "toKey");
    }

    @Test
    void testTailMap() {
        // Given
        SortedMap<String, Integer> tailMap = mock(SortedMap.class);
        when(sortedBidiMap.tailMap(any())).thenReturn(tailMap);

        // When
        SortedMap<String, Integer> result = abstractSortedBidiMapDecorator.tailMap("fromKey");

        // Then
        assertEquals(tailMap, result);
        verify(sortedBidiMap, times(1)).tailMap("fromKey");
    }

    @Test
    void testValueComparator() {
        // Given
        Comparator<Integer> valueComparator = mock(Comparator.class);
        when(sortedBidiMap.valueComparator()).thenReturn(valueComparator);

        // When
        Comparator<Integer> result = abstractSortedBidiMapDecorator.valueComparator();

        // Then
        assertEquals(valueComparator, result);
        verify(sortedBidiMap, times(1)).valueComparator();
    }
}
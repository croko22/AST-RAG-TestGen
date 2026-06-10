import org.apache.commons.collections4.map.AbstractSortedMapDecorator;
import org.apache.commons.collections4.iterators.ListIteratorWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractSortedMapDecoratorTest {

    @Mock
    private SortedMap<String, String> sortedMap;

    @InjectMocks
    private AbstractSortedMapDecorator<String, String> abstractSortedMapDecorator;

    @BeforeEach
    void setup() {
        abstractSortedMapDecorator = new AbstractSortedMapDecorator<>(new TreeMap<>());
    }

    @Test
    void testComparator() {
        // Given
        Comparator<String> comparator = mock(Comparator.class);
        when(sortedMap.comparator()).thenReturn(comparator);

        // When
        Comparator<String> result = abstractSortedMapDecorator.comparator();

        // Then
        assertEquals(comparator, result);
    }

    @Test
    void testFirstKey() {
        // Given
        String firstKey = "firstKey";
        when(sortedMap.firstKey()).thenReturn(firstKey);

        // When
        String result = abstractSortedMapDecorator.firstKey();

        // Then
        assertEquals(firstKey, result);
    }

    @Test
    void testHeadMap() {
        // Given
        String toKey = "toKey";
        SortedMap<String, String> headMap = mock(SortedMap.class);
        when(sortedMap.headMap(toKey)).thenReturn(headMap);

        // When
        SortedMap<String, String> result = abstractSortedMapDecorator.headMap(toKey);

        // Then
        assertEquals(headMap, result);
    }

    @Test
    void testLastKey() {
        // Given
        String lastKey = "lastKey";
        when(sortedMap.lastKey()).thenReturn(lastKey);

        // When
        String result = abstractSortedMapDecorator.lastKey();

        // Then
        assertEquals(lastKey, result);
    }

    @Test
    void testMapIterator() {
        // Given
        // No need to mock anything here, just test the method call

        // When
        abstractSortedMapDecorator.mapIterator();

        // Then
        // No need to assert anything here, just verify the method was called
        verify(sortedMap, times(1)).entrySet();
    }

    @Test
    void testNextKey() {
        // Given
        String key = "key";
        String nextKey = "nextKey";
        when(sortedMap.tailMap(key)).thenReturn(mock(SortedMap.class));

        // When
        String result = abstractSortedMapDecorator.nextKey(key);

        // Then
        // No need to assert anything here, just verify the method was called
        verify(sortedMap, times(1)).tailMap(key);
    }

    @Test
    void testPreviousKey() {
        // Given
        String key = "key";
        String previousKey = "previousKey";
        when(sortedMap.headMap(key)).thenReturn(mock(SortedMap.class));

        // When
        String result = abstractSortedMapDecorator.previousKey(key);

        // Then
        // No need to assert anything here, just verify the method was called
        verify(sortedMap, times(1)).headMap(key);
    }

    @Test
    void testSubMap() {
        // Given
        String fromKey = "fromKey";
        String toKey = "toKey";
        SortedMap<String, String> subMap = mock(SortedMap.class);
        when(sortedMap.subMap(fromKey, toKey)).thenReturn(subMap);

        // When
        SortedMap<String, String> result = abstractSortedMapDecorator.subMap(fromKey, toKey);

        // Then
        assertEquals(subMap, result);
    }

    @Test
    void testTailMap() {
        // Given
        String fromKey = "fromKey";
        SortedMap<String, String> tailMap = mock(SortedMap.class);
        when(sortedMap.tailMap(fromKey)).thenReturn(tailMap);

        // When
        SortedMap<String, String> result = abstractSortedMapDecorator.tailMap(fromKey);

        // Then
        assertEquals(tailMap, result);
    }
}
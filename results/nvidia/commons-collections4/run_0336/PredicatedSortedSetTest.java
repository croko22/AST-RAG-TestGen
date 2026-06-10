import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.set.PredicatedSortedSet;
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
public class PredicatedSortedSetTest {

    @Mock
    private SortedSet<String> sortedSet;

    @Mock
    private Predicate<String> predicate;

    private PredicatedSortedSet<String> predicatedSortedSet;

    @BeforeEach
    public void setup() {
        predicatedSortedSet = new PredicatedSortedSet<>(sortedSet, predicate);
    }

    @Test
    public void testPredicatedSortedSet() {
        // Given
        SortedSet<String> set = new TreeSet<>();
        Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean evaluate(String object) {
                return true;
            }
        };

        // When
        PredicatedSortedSet<String> predicatedSortedSet = PredicatedSortedSet.predicatedSortedSet(set, predicate);

        // Then
        assertNotNull(predicatedSortedSet);
    }

    @Test
    public void testPredicatedSortedSet_NullSet() {
        // Given
        SortedSet<String> set = null;
        Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean evaluate(String object) {
                return true;
            }
        };

        // When / Then
        assertThrows(NullPointerException.class, () -> PredicatedSortedSet.predicatedSortedSet(set, predicate));
    }

    @Test
    public void testPredicatedSortedSet_NullPredicate() {
        // Given
        SortedSet<String> set = new TreeSet<>();
        Predicate<String> predicate = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> PredicatedSortedSet.predicatedSortedSet(set, predicate));
    }

    @Test
    public void testComparator() {
        // Given
        Comparator<String> comparator = mock(Comparator.class);
        when(sortedSet.comparator()).thenReturn(comparator);

        // When
        Comparator<String> result = predicatedSortedSet.comparator();

        // Then
        assertEquals(comparator, result);
        verify(sortedSet, times(1)).comparator();
    }

    @Test
    public void testFirst() {
        // Given
        String first = "first";
        when(sortedSet.first()).thenReturn(first);

        // When
        String result = predicatedSortedSet.first();

        // Then
        assertEquals(first, result);
        verify(sortedSet, times(1)).first();
    }

    @Test
    public void testHeadSet() {
        // Given
        String toElement = "toElement";
        SortedSet<String> headSet = mock(SortedSet.class);
        when(sortedSet.headSet(toElement)).thenReturn(headSet);

        // When
        SortedSet<String> result = predicatedSortedSet.headSet(toElement);

        // Then
        assertNotNull(result);
        verify(sortedSet, times(1)).headSet(toElement);
    }

    @Test
    public void testLast() {
        // Given
        String last = "last";
        when(sortedSet.last()).thenReturn(last);

        // When
        String result = predicatedSortedSet.last();

        // Then
        assertEquals(last, result);
        verify(sortedSet, times(1)).last();
    }

    @Test
    public void testSubSet() {
        // Given
        String fromElement = "fromElement";
        String toElement = "toElement";
        SortedSet<String> subSet = mock(SortedSet.class);
        when(sortedSet.subSet(fromElement, toElement)).thenReturn(subSet);

        // When
        SortedSet<String> result = predicatedSortedSet.subSet(fromElement, toElement);

        // Then
        assertNotNull(result);
        verify(sortedSet, times(1)).subSet(fromElement, toElement);
    }

    @Test
    public void testTailSet() {
        // Given
        String fromElement = "fromElement";
        SortedSet<String> tailSet = mock(SortedSet.class);
        when(sortedSet.tailSet(fromElement)).thenReturn(tailSet);

        // When
        SortedSet<String> result = predicatedSortedSet.tailSet(fromElement);

        // Then
        assertNotNull(result);
        verify(sortedSet, times(1)).tailSet(fromElement);
    }
}
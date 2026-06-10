import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.set.TransformedNavigableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransformedNavigableSetTest {

    @Mock
    private NavigableSet<String> navigableSet;

    @Mock
    private Transformer<String, String> transformer;

    @InjectMocks
    private TransformedNavigableSet<String> transformedNavigableSet;

    @BeforeEach
    void setup() {
        transformedNavigableSet = TransformedNavigableSet.transformingNavigableSet(navigableSet, transformer);
    }

    @Test
    public void testTransformedNavigableSet() {
        // Given
        NavigableSet<String> set = new TreeSet<>();
        set.add("a");
        set.add("b");
        Transformer<String, String> transformer = s -> s.toUpperCase();

        // When
        TransformedNavigableSet<String> transformedSet = TransformedNavigableSet.transformedNavigableSet(set, transformer);

        // Then
        assertEquals(2, transformedSet.size());
        assertTrue(transformedSet.contains("A"));
        assertTrue(transformedSet.contains("B"));
    }

    @Test
    public void testTransformingNavigableSet() {
        // Given
        NavigableSet<String> set = new TreeSet<>();
        set.add("a");
        set.add("b");
        Transformer<String, String> transformer = s -> s.toUpperCase();

        // When
        TransformedNavigableSet<String> transformedSet = TransformedNavigableSet.transformingNavigableSet(set, transformer);

        // Then
        assertEquals(2, transformedSet.size());
        assertTrue(transformedSet.contains("a"));
        assertTrue(transformedSet.contains("b"));
    }

    @Test
    public void testCeiling() {
        // Given
        when(navigableSet.ceiling(any())).thenReturn("b");

        // When
        String result = transformedNavigableSet.ceiling("a");

        // Then
        assertEquals("b", result);
        verify(navigableSet, times(1)).ceiling(any());
    }

    @Test
    public void testDescendingIterator() {
        // Given
        when(navigableSet.descendingIterator()).thenReturn(new ArrayList<String>().iterator());

        // When
        Iterator<String> result = transformedNavigableSet.descendingIterator();

        // Then
        assertNotNull(result);
        verify(navigableSet, times(1)).descendingIterator();
    }

    @Test
    public void testDescendingSet() {
        // Given
        when(navigableSet.descendingSet()).thenReturn(new TreeSet<>());

        // When
        NavigableSet<String> result = transformedNavigableSet.descendingSet();

        // Then
        assertNotNull(result);
        verify(navigableSet, times(1)).descendingSet();
    }

    @Test
    public void testFloor() {
        // Given
        when(navigableSet.floor(any())).thenReturn("a");

        // When
        String result = transformedNavigableSet.floor("b");

        // Then
        assertEquals("a", result);
        verify(navigableSet, times(1)).floor(any());
    }

    @Test
    public void testHeadSet() {
        // Given
        when(navigableSet.headSet(any(), anyBoolean())).thenReturn(new TreeSet<>());

        // When
        NavigableSet<String> result = transformedNavigableSet.headSet("b", true);

        // Then
        assertNotNull(result);
        verify(navigableSet, times(1)).headSet(any(), anyBoolean());
    }

    @Test
    public void testHigher() {
        // Given
        when(navigableSet.higher(any())).thenReturn("c");

        // When
        String result = transformedNavigableSet.higher("b");

        // Then
        assertEquals("c", result);
        verify(navigableSet, times(1)).higher(any());
    }

    @Test
    public void testLower() {
        // Given
        when(navigableSet.lower(any())).thenReturn("a");

        // When
        String result = transformedNavigableSet.lower("b");

        // Then
        assertEquals("a", result);
        verify(navigableSet, times(1)).lower(any());
    }

    @Test
    public void testPollFirst() {
        // Given
        when(navigableSet.pollFirst()).thenReturn("a");

        // When
        String result = transformedNavigableSet.pollFirst();

        // Then
        assertEquals("a", result);
        verify(navigableSet, times(1)).pollFirst();
    }

    @Test
    public void testPollLast() {
        // Given
        when(navigableSet.pollLast()).thenReturn("c");

        // When
        String result = transformedNavigableSet.pollLast();

        // Then
        assertEquals("c", result);
        verify(navigableSet, times(1)).pollLast();
    }

    @Test
    public void testSubSet() {
        // Given
        when(navigableSet.subSet(any(), anyBoolean(), any(), anyBoolean())).thenReturn(new TreeSet<>());

        // When
        NavigableSet<String> result = transformedNavigableSet.subSet("a", true, "c", false);

        // Then
        assertNotNull(result);
        verify(navigableSet, times(1)).subSet(any(), anyBoolean(), any(), anyBoolean());
    }

    @Test
    public void testTailSet() {
        // Given
        when(navigableSet.tailSet(any(), anyBoolean())).thenReturn(new TreeSet<>());

        // When
        NavigableSet<String> result = transformedNavigableSet.tailSet("b", true);

        // Then
        assertNotNull(result);
        verify(navigableSet, times(1)).tailSet(any(), anyBoolean());
    }
}
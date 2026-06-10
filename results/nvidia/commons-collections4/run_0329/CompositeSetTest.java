import org.apache.commons.collections4.set.CompositeSet;
import org.apache.commons.collections4.set.CompositeSet.SetMutator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompositeSetTest {

    @Mock
    private SetMutator<String> setMutator;

    private CompositeSet<String> compositeSet;

    @BeforeEach
    public void setup() {
        compositeSet = new CompositeSet<>();
        compositeSet.setMutator(setMutator);
    }

    @Test
    public void testAdd() {
        // Given
        String element = "element";
        when(setMutator.add(any(CompositeSet.class), any(List.class), any(String.class))).thenReturn(true);

        // When
        boolean result = compositeSet.add(element);

        // Then
        assertTrue(result);
        verify(setMutator, times(1)).add(any(CompositeSet.class), any(List.class), eq(element));
    }

    @Test
    public void testAddAll() {
        // Given
        Collection<String> elements = new ArrayList<>();
        elements.add("element1");
        elements.add("element2");
        when(setMutator.addAll(any(CompositeSet.class), any(List.class), any(Collection.class))).thenReturn(true);

        // When
        boolean result = compositeSet.addAll(elements);

        // Then
        assertTrue(result);
        verify(setMutator, times(1)).addAll(any(CompositeSet.class), any(List.class), eq(elements));
    }

    @Test
    public void testAddComposited() {
        // Given
        Set<String> set = new HashSet<>();

        // When
        compositeSet.addComposited(set);

        // Then
        assertNotNull(compositeSet.getSets());
        assertEquals(1, compositeSet.getSets().size());
        assertTrue(compositeSet.getSets().contains(set));
    }

    @Test
    public void testClear() {
        // Given
        Set<String> set = new HashSet<>();
        set.add("element");
        compositeSet.addComposited(set);

        // When
        compositeSet.clear();

        // Then
        assertTrue(compositeSet.isEmpty());
    }

    @Test
    public void testContains() {
        // Given
        Set<String> set = new HashSet<>();
        set.add("element");
        compositeSet.addComposited(set);

        // When
        boolean result = compositeSet.contains("element");

        // Then
        assertTrue(result);
    }

    @Test
    public void testContainsAll() {
        // Given
        Set<String> set = new HashSet<>();
        set.add("element1");
        set.add("element2");
        compositeSet.addComposited(set);
        Collection<String> elements = new ArrayList<>();
        elements.add("element1");
        elements.add("element2");

        // When
        boolean result = compositeSet.containsAll(elements);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals() {
        // Given
        Set<String> set = new HashSet<>();
        set.add("element");
        compositeSet.addComposited(set);
        CompositeSet<String> otherCompositeSet = new CompositeSet<>();
        otherCompositeSet.addComposited(set);

        // When
        boolean result = compositeSet.equals(otherCompositeSet);

        // Then
        assertTrue(result);
    }

    @Test
    public void testGetSets() {
        // Given
        Set<String> set = new HashSet<>();

        // When
        compositeSet.addComposited(set);

        // Then
        assertNotNull(compositeSet.getSets());
        assertEquals(1, compositeSet.getSets().size());
        assertTrue(compositeSet.getSets().contains(set));
    }

    @Test
    public void testHashCode() {
        // Given
        Set<String> set = new HashSet<>();
        set.add("element");
        compositeSet.addComposited(set);

        // When
        int result = compositeSet.hashCode();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testIsEmpty() {
        // Given
        Set<String> set = new HashSet<>();
        compositeSet.addComposited(set);

        // When
        boolean result = compositeSet.isEmpty();

        // Then
        assertTrue(result);
    }

    @Test
    public void testIterator() {
        // Given
        Set<String> set = new HashSet<>();
        set.add("element");
        compositeSet.addComposited(set);

        // When
        Iterator<String> iterator = compositeSet.iterator();

        // Then
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals("element", iterator.next());
    }

    @Test
    public void testRemove() {
        // Given
        Set<String> set = new HashSet<>();
        set.add("element");
        compositeSet.addComposited(set);

        // When
        boolean result = compositeSet.remove("element");

        // Then
        assertTrue(result);
        assertTrue(compositeSet.isEmpty());
    }

    @Test
    public void testRemoveAll() {
        // Given
        Set<String> set = new HashSet<>();
        set.add("element1");
        set.add("element2");
        compositeSet.addComposited(set);
        Collection<String> elements = new ArrayList<>();
        elements.add("element1");
        elements.add("element2");

        // When
        boolean result = compositeSet.removeAll(elements);

        // Then
        assertTrue(result);
        assertTrue(compositeSet.isEmpty());
    }

    @Test
    public void testRemoveComposited() {
        // Given
        Set<String> set = new HashSet<>();
        compositeSet.addComposited(set);

        // When
        compositeSet.removeComposited(set);

        // Then
        assertTrue(compositeSet.getSets().isEmpty());
    }

    @Test
    public void testRemoveIf() {
        // Given
        Set<String> set = new HashSet<>();
        set.add("element");
        compositeSet.addComposited(set);

        // When
        boolean result = compositeSet.removeIf(element -> true);

        // Then
        assertTrue(result);
        assertTrue(compositeSet.isEmpty());
    }

    @Test
    public void testRetainAll() {
        // Given
        Set<String> set = new HashSet<>();
        set.add("element1");
        set.add("element2");
        compositeSet.addComposited(set);
        Collection<String> elements = new ArrayList<>();
        elements.add("element1");

        // When
        boolean result = compositeSet.retainAll(elements);

        // Then
        assertTrue(result);
        assertEquals(1, compositeSet.size());
    }

    @Test
    public void testSetMutator() {
        // Given
        SetMutator<String> newSetMutator = mock(SetMutator.class);

        // When
        compositeSet.setMutator(newSetMutator);

        // Then
        assertSame(newSetMutator, compositeSet.getMutator());
    }

    @Test
    public void testSize() {
        // Given
        Set<String> set = new HashSet<>();
        set.add("element");
        compositeSet.addComposited(set);

        // When
        int result = compositeSet.size();

        // Then
        assertEquals(1, result);
    }

    @Test
    public void testToSet() {
        // Given
        Set<String> set = new HashSet<>();
        set.add("element");
        compositeSet.addComposited(set);

        // When
        Set<String> result = compositeSet.toSet();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains("element"));
    }
}
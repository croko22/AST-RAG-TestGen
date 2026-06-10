import org.apache.commons.collections4.list.UnmodifiableList;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;
import org.apache.commons.collections4.iterators.UnmodifiableListIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnmodifiableListTest {

    @Mock
    private List<String> mockList;

    private UnmodifiableList<String> unmodifiableList;

    @BeforeEach
    public void setup() {
        unmodifiableList = new UnmodifiableList<>(mockList);
    }

    @Test
    public void testUnmodifiableList() {
        // Given
        List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        UnmodifiableList<String> unmodifiableList = UnmodifiableList.unmodifiableList(list);

        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.add(0, "d"));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.add("d"));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.addAll(Arrays.asList("d", "e")));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.addAll(0, Arrays.asList("d", "e")));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.clear());
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.remove(0));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.remove("a"));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.removeAll(Arrays.asList("a", "b")));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.removeIf(s -> true));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.retainAll(Arrays.asList("a", "b")));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.set(0, "d"));

        // Then
        Iterator<String> iterator = unmodifiableList.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("c", iterator.next());
        assertFalse(iterator.hasNext());

        ListIterator<String> listIterator = unmodifiableList.listIterator();
        assertTrue(listIterator.hasNext());
        assertEquals("a", listIterator.next());
        assertTrue(listIterator.hasNext());
        assertEquals("b", listIterator.next());
        assertTrue(listIterator.hasNext());
        assertEquals("c", listIterator.next());
        assertFalse(listIterator.hasNext());

        ListIterator<String> listIteratorWithIndex = unmodifiableList.listIterator(1);
        assertTrue(listIteratorWithIndex.hasNext());
        assertEquals("b", listIteratorWithIndex.next());
        assertTrue(listIteratorWithIndex.hasNext());
        assertEquals("c", listIteratorWithIndex.next());
        assertFalse(listIteratorWithIndex.hasNext());

        List<String> subList = unmodifiableList.subList(1, 2);
        assertEquals(1, subList.size());
        assertEquals("b", subList.get(0));
    }

    @Test
    public void testUnmodifiableListFactoryMethod() {
        // Given
        List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));

        // When
        UnmodifiableList<String> unmodifiableList = UnmodifiableList.unmodifiableList(list);

        // Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.add(0, "d"));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.add("d"));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.addAll(Arrays.asList("d", "e")));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.addAll(0, Arrays.asList("d", "e")));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.clear());
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.remove(0));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.remove("a"));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.removeAll(Arrays.asList("a", "b")));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.removeIf(s -> true));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.retainAll(Arrays.asList("a", "b")));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.set(0, "d"));
    }

    @Test
    public void testAdd() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.add(0, "a"));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.add("a"));
    }

    @Test
    public void testAddAll() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.addAll(Arrays.asList("a", "b")));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.addAll(0, Arrays.asList("a", "b")));
    }

    @Test
    public void testClear() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.clear());
    }

    @Test
    public void testRemove() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.remove(0));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.remove("a"));
    }

    @Test
    public void testRemoveAll() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.removeAll(Arrays.asList("a", "b")));
    }

    @Test
    public void testRemoveIf() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.removeIf(s -> true));
    }

    @Test
    public void testRetainAll() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.retainAll(Arrays.asList("a", "b")));
    }

    @Test
    public void testSet() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.set(0, "a"));
    }

    @Test
    public void testSubList() {
        // Given
        when(mockList.subList(0, 1)).thenReturn(Arrays.asList("a"));

        // When
        List<String> subList = unmodifiableList.subList(0, 1);

        // Then
        assertEquals(1, subList.size());
        assertEquals("a", subList.get(0));
    }

    @Test
    public void testIterator() {
        // Given
        when(mockList.iterator()).thenReturn(Arrays.asList("a", "b", "c").iterator());

        // When
        Iterator<String> iterator = unmodifiableList.iterator();

        // Then
        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("c", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testListIterator() {
        // Given
        when(mockList.listIterator()).thenReturn(Arrays.asList("a", "b", "c").listIterator());

        // When
        ListIterator<String> listIterator = unmodifiableList.listIterator();

        // Then
        assertTrue(listIterator.hasNext());
        assertEquals("a", listIterator.next());
        assertTrue(listIterator.hasNext());
        assertEquals("b", listIterator.next());
        assertTrue(listIterator.hasNext());
        assertEquals("c", listIterator.next());
        assertFalse(listIterator.hasNext());
    }

    @Test
    public void testListIteratorWithIndex() {
        // Given
        when(mockList.listIterator(1)).thenReturn(Arrays.asList("a", "b", "c").listIterator(1));

        // When
        ListIterator<String> listIterator = unmodifiableList.listIterator(1);

        // Then
        assertTrue(listIterator.hasNext());
        assertEquals("b", listIterator.next());
        assertTrue(listIterator.hasNext());
        assertEquals("c", listIterator.next());
        assertFalse(listIterator.hasNext());
    }
}
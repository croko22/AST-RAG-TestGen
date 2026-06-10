import org.apache.commons.collections4.list.SetUniqueList;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.set.UnmodifiableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Predicate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SetUniqueListTest {

    @Mock
    private List<String> list;

    @InjectMocks
    private SetUniqueList<String> setUniqueList;

    @BeforeEach
    public void setup() {
        setUniqueList = new SetUniqueList<>(new ArrayList<>(), new HashSet<>());
    }

    @Test
    public void testSetUniqueList() {
        // Given
        List<String> inputList = Arrays.asList("a", "b", "c", "a", "b", "c");

        // When
        SetUniqueList<String> result = SetUniqueList.setUniqueList(inputList);

        // Then
        assertEquals(3, result.size());
        assertTrue(result.contains("a"));
        assertTrue(result.contains("b"));
        assertTrue(result.contains("c"));
    }

    @Test
    public void testAdd() {
        // Given
        String element = "a";

        // When
        boolean result = setUniqueList.add(element);

        // Then
        assertTrue(result);
        assertEquals(1, setUniqueList.size());
        assertTrue(setUniqueList.contains(element));
    }

    @Test
    public void testAdd_Duplicate() {
        // Given
        String element = "a";
        setUniqueList.add(element);

        // When
        boolean result = setUniqueList.add(element);

        // Then
        assertFalse(result);
        assertEquals(1, setUniqueList.size());
        assertTrue(setUniqueList.contains(element));
    }

    @Test
    public void testAddAtIndex() {
        // Given
        String element = "a";
        setUniqueList.add("b");
        setUniqueList.add("c");

        // When
        setUniqueList.add(1, element);

        // Then
        assertEquals(3, setUniqueList.size());
        assertTrue(setUniqueList.contains(element));
        assertEquals(element, setUniqueList.get(1));
    }

    @Test
    public void testAddAtIndex_Duplicate() {
        // Given
        String element = "a";
        setUniqueList.add("b");
        setUniqueList.add("c");
        setUniqueList.add(element);

        // When
        setUniqueList.add(1, element);

        // Then
        assertEquals(3, setUniqueList.size());
        assertTrue(setUniqueList.contains(element));
        assertEquals("b", setUniqueList.get(1));
    }

    @Test
    public void testAddAll() {
        // Given
        Collection<String> collection = Arrays.asList("a", "b", "c", "a", "b", "c");

        // When
        boolean result = setUniqueList.addAll(collection);

        // Then
        assertTrue(result);
        assertEquals(3, setUniqueList.size());
        assertTrue(setUniqueList.contains("a"));
        assertTrue(setUniqueList.contains("b"));
        assertTrue(setUniqueList.contains("c"));
    }

    @Test
    public void testAddAllAtIndex() {
        // Given
        Collection<String> collection = Arrays.asList("a", "b", "c", "a", "b", "c");
        setUniqueList.add("d");
        setUniqueList.add("e");

        // When
        boolean result = setUniqueList.addAll(1, collection);

        // Then
        assertTrue(result);
        assertEquals(5, setUniqueList.size());
        assertTrue(setUniqueList.contains("a"));
        assertTrue(setUniqueList.contains("b"));
        assertTrue(setUniqueList.contains("c"));
        assertTrue(setUniqueList.contains("d"));
        assertTrue(setUniqueList.contains("e"));
    }

    @Test
    public void testAsSet() {
        // Given
        setUniqueList.add("a");
        setUniqueList.add("b");
        setUniqueList.add("c");

        // When
        Set<String> result = setUniqueList.asSet();

        // Then
        assertEquals(3, result.size());
        assertTrue(result.contains("a"));
        assertTrue(result.contains("b"));
        assertTrue(result.contains("c"));
    }

    @Test
    public void testClear() {
        // Given
        setUniqueList.add("a");
        setUniqueList.add("b");
        setUniqueList.add("c");

        // When
        setUniqueList.clear();

        // Then
        assertEquals(0, setUniqueList.size());
    }

    @Test
    public void testContains() {
        // Given
        setUniqueList.add("a");

        // When
        boolean result = setUniqueList.contains("a");

        // Then
        assertTrue(result);
    }

    @Test
    public void testContainsAll() {
        // Given
        setUniqueList.add("a");
        setUniqueList.add("b");
        setUniqueList.add("c");
        Collection<String> collection = Arrays.asList("a", "b", "c");

        // When
        boolean result = setUniqueList.containsAll(collection);

        // Then
        assertTrue(result);
    }

    @Test
    public void testIterator() {
        // Given
        setUniqueList.add("a");
        setUniqueList.add("b");
        setUniqueList.add("c");

        // When
        Iterator<String> iterator = setUniqueList.iterator();

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
        setUniqueList.add("a");
        setUniqueList.add("b");
        setUniqueList.add("c");

        // When
        ListIterator<String> iterator = setUniqueList.listIterator();

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
    public void testListIteratorAtIndex() {
        // Given
        setUniqueList.add("a");
        setUniqueList.add("b");
        setUniqueList.add("c");

        // When
        ListIterator<String> iterator = setUniqueList.listIterator(1);

        // Then
        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("c", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testRemove() {
        // Given
        setUniqueList.add("a");
        setUniqueList.add("b");
        setUniqueList.add("c");

        // When
        boolean result = setUniqueList.remove("a");

        // Then
        assertTrue(result);
        assertEquals(2, setUniqueList.size());
        assertFalse(setUniqueList.contains("a"));
    }

    @Test
    public void testRemoveAtIndex() {
        // Given
        setUniqueList.add("a");
        setUniqueList.add("b");
        setUniqueList.add("c");

        // When
        String result = setUniqueList.remove(1);

        // Then
        assertEquals("b", result);
        assertEquals(2, setUniqueList.size());
        assertFalse(setUniqueList.contains("b"));
    }

    @Test
    public void testRemoveAll() {
        // Given
        setUniqueList.add("a");
        setUniqueList.add("b");
        setUniqueList.add("c");
        Collection<String> collection = Arrays.asList("a", "b");

        // When
        boolean result = setUniqueList.removeAll(collection);

        // Then
        assertTrue(result);
        assertEquals(1, setUniqueList.size());
        assertFalse(setUniqueList.contains("a"));
        assertFalse(setUniqueList.contains("b"));
    }

    @Test
    public void testRemoveIf() {
        // Given
        setUniqueList.add("a");
        setUniqueList.add("b");
        setUniqueList.add("c");
        Predicate<String> predicate = s -> s.equals("a") || s.equals("b");

        // When
        boolean result = setUniqueList.removeIf(predicate);

        // Then
        assertTrue(result);
        assertEquals(1, setUniqueList.size());
        assertFalse(setUniqueList.contains("a"));
        assertFalse(setUniqueList.contains("b"));
    }

    @Test
    public void testRetainAll() {
        // Given
        setUniqueList.add("a");
        setUniqueList.add("b");
        setUniqueList.add("c");
        Collection<String> collection = Arrays.asList("a", "b");

        // When
        boolean result = setUniqueList.retainAll(collection);

        // Then
        assertTrue(result);
        assertEquals(2, setUniqueList.size());
        assertTrue(setUniqueList.contains("a"));
        assertTrue(setUniqueList.contains("b"));
        assertFalse(setUniqueList.contains("c"));
    }

    @Test
    public void testSet() {
        // Given
        setUniqueList.add("a");
        setUniqueList.add("b");
        setUniqueList.add("c");

        // When
        String result = setUniqueList.set(1, "d");

        // Then
        assertEquals("b", result);
        assertEquals(3, setUniqueList.size());
        assertTrue(setUniqueList.contains("a"));
        assertTrue(setUniqueList.contains("d"));
        assertTrue(setUniqueList.contains("c"));
    }

    @Test
    public void testSubList() {
        // Given
        setUniqueList.add("a");
        setUniqueList.add("b");
        setUniqueList.add("c");

        // When
        List<String> result = setUniqueList.subList(1, 3);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.contains("b"));
        assertTrue(result.contains("c"));
    }
}
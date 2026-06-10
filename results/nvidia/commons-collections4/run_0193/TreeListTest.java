import org.apache.commons.collections4.list.TreeList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TreeListTest {

    @InjectMocks
    private TreeList<String> treeList;

    @BeforeEach
    void setup() {
        treeList = new TreeList<>();
    }

    @AfterEach
    void tearDown() {
        treeList.clear();
    }

    @Test
    void testAdd() {
        // Given
        String element = "Test Element";

        // When
        treeList.add(element);

        // Then
        assertEquals(1, treeList.size());
        assertTrue(treeList.contains(element));
    }

    @Test
    void testAddAll() {
        // Given
        List<String> elements = Arrays.asList("Element1", "Element2", "Element3");

        // When
        treeList.addAll(elements);

        // Then
        assertEquals(3, treeList.size());
        assertTrue(treeList.containsAll(elements));
    }

    @Test
    void testClear() {
        // Given
        treeList.addAll(Arrays.asList("Element1", "Element2", "Element3"));

        // When
        treeList.clear();

        // Then
        assertEquals(0, treeList.size());
    }

    @Test
    void testContains() {
        // Given
        treeList.add("Test Element");

        // When
        boolean result = treeList.contains("Test Element");

        // Then
        assertTrue(result);
    }

    @Test
    void testGet() {
        // Given
        treeList.add("Test Element");

        // When
        String result = treeList.get(0);

        // Then
        assertEquals("Test Element", result);
    }

    @Test
    void testIndexOf() {
        // Given
        treeList.add("Test Element");

        // When
        int result = treeList.indexOf("Test Element");

        // Then
        assertEquals(0, result);
    }

    @Test
    void testIterator() {
        // Given
        treeList.addAll(Arrays.asList("Element1", "Element2", "Element3"));

        // When
        List<String> result = new ArrayList<>();
        treeList.iterator().forEachRemaining(result::add);

        // Then
        assertEquals(3, result.size());
        assertTrue(result.containsAll(treeList));
    }

    @Test
    void testListIterator() {
        // Given
        treeList.addAll(Arrays.asList("Element1", "Element2", "Element3"));

        // When
        List<String> result = new ArrayList<>();
        treeList.listIterator().forEachRemaining(result::add);

        // Then
        assertEquals(3, result.size());
        assertTrue(result.containsAll(treeList));
    }

    @Test
    void testRemove() {
        // Given
        treeList.add("Test Element");

        // When
        treeList.remove(0);

        // Then
        assertEquals(0, treeList.size());
    }

    @Test
    void testSet() {
        // Given
        treeList.add("Test Element");

        // When
        treeList.set(0, "New Element");

        // Then
        assertEquals("New Element", treeList.get(0));
    }

    @Test
    void testSize() {
        // Given
        treeList.addAll(Arrays.asList("Element1", "Element2", "Element3"));

        // When
        int result = treeList.size();

        // Then
        assertEquals(3, result);
    }

    @Test
    void testToArray() {
        // Given
        treeList.addAll(Arrays.asList("Element1", "Element2", "Element3"));

        // When
        Object[] result = treeList.toArray();

        // Then
        assertEquals(3, result.length);
        assertTrue(Arrays.asList(result).containsAll(treeList));
    }
}
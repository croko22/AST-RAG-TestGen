import org.apache.commons.collections4.set.ListOrderedSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ListOrderedSetTest {

    @Mock
    private Set<String> set;

    @Mock
    private List<String> list;

    private ListOrderedSet<String> listOrderedSet;

    @BeforeEach
    public void setup() {
        listOrderedSet = new ListOrderedSet<>(set, list);
    }

    @Test
    public void testListOrderedSetCreation() {
        // Given
        Set<String> set = new HashSet<>();
        List<String> list = new ArrayList<>();

        // When
        ListOrderedSet<String> listOrderedSet = new ListOrderedSet<>(set, list);

        // Then
        assertNotNull(listOrderedSet);
    }

    @Test
    public void testListOrderedSetAdd() {
        // Given
        String element = "Element";

        // When
        boolean result = listOrderedSet.add(element);

        // Then
        assertTrue(result);
    }

    @Test
    public void testListOrderedSetAddAll() {
        // Given
        Collection<String> collection = new ArrayList<>();
        collection.add("Element1");
        collection.add("Element2");

        // When
        boolean result = listOrderedSet.addAll(collection);

        // Then
        assertTrue(result);
    }

    @Test
    public void testListOrderedSetAddAllWithIndex() {
        // Given
        Collection<String> collection = new ArrayList<>();
        collection.add("Element1");
        collection.add("Element2");

        // When
        boolean result = listOrderedSet.addAll(0, collection);

        // Then
        assertTrue(result);
    }

    @Test
    public void testListOrderedSetAsList() {
        // Given

        // When
        List<String> result = listOrderedSet.asList();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testListOrderedSetClear() {
        // Given

        // When
        listOrderedSet.clear();

        // Then
        assertTrue(listOrderedSet.isEmpty());
    }

    @Test
    public void testListOrderedSetGet() {
        // Given
        String element = "Element";
        listOrderedSet.add(element);

        // When
        String result = listOrderedSet.get(0);

        // Then
        assertEquals(element, result);
    }

    @Test
    public void testListOrderedSetIndexOf() {
        // Given
        String element = "Element";
        listOrderedSet.add(element);

        // When
        int result = listOrderedSet.indexOf(element);

        // Then
        assertEquals(0, result);
    }

    @Test
    public void testListOrderedSetIterator() {
        // Given

        // When
        ListOrderedSet.OrderedSetIterator<String> result = (ListOrderedSet.OrderedSetIterator<String>) listOrderedSet.iterator();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testListOrderedSetRemove() {
        // Given
        String element = "Element";
        listOrderedSet.add(element);

        // When
        boolean result = listOrderedSet.remove(element);

        // Then
        assertTrue(result);
    }

    @Test
    public void testListOrderedSetRemoveAll() {
        // Given
        Collection<String> collection = new ArrayList<>();
        collection.add("Element1");
        collection.add("Element2");
        listOrderedSet.add("Element1");
        listOrderedSet.add("Element2");

        // When
        boolean result = listOrderedSet.removeAll(collection);

        // Then
        assertTrue(result);
    }

    @Test
    public void testListOrderedSetRemoveIf() {
        // Given
        listOrderedSet.add("Element1");
        listOrderedSet.add("Element2");

        // When
        boolean result = listOrderedSet.removeIf(e -> e.equals("Element1"));

        // Then
        assertTrue(result);
    }

    @Test
    public void testListOrderedSetRetainAll() {
        // Given
        Collection<String> collection = new ArrayList<>();
        collection.add("Element1");
        listOrderedSet.add("Element1");
        listOrderedSet.add("Element2");

        // When
        boolean result = listOrderedSet.retainAll(collection);

        // Then
        assertTrue(result);
    }

    @Test
    public void testListOrderedSetToString() {
        // Given

        // When
        String result = listOrderedSet.toString();

        // Then
        assertNotNull(result);
    }
}
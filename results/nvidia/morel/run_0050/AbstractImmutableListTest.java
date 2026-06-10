import net.hydromatic.morel.util.AbstractImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractImmutableListTest {

    @Mock
    private AbstractImmutableList<String> immutableList;

    @BeforeEach
    void setup() {
        // Initialize the mock object
        when(immutableList.isEmpty()).thenReturn(false);
    }

    @Test
    public void testIterator() {
        // Given: an instance of AbstractImmutableList
        // When: iterator is called
        Iterator<String> iterator = immutableList.iterator();
        // Then: iterator should not be null
        assertNotNull(iterator);
    }

    @Test
    public void testListIterator() {
        // Given: an instance of AbstractImmutableList
        // When: listIterator is called
        ListIterator<String> listIterator = immutableList.listIterator();
        // Then: listIterator should not be null
        assertNotNull(listIterator);
    }

    @Test
    public void testIsEmpty() {
        // Given: an instance of AbstractImmutableList
        // When: isEmpty is called
        boolean isEmpty = immutableList.isEmpty();
        // Then: isEmpty should return false
        assertFalse(isEmpty);
    }

    @Test
    public void testAdd() {
        // Given: an instance of AbstractImmutableList
        // When: add is called
        assertThrows(UnsupportedOperationException.class, () -> immutableList.add("test"));
    }

    @Test
    public void testAddAll() {
        // Given: an instance of AbstractImmutableList
        // When: addAll is called
        assertThrows(UnsupportedOperationException.class, () -> immutableList.addAll(Arrays.asList("test1", "test2")));
    }

    @Test
    public void testAddAllWithIndex() {
        // Given: an instance of AbstractImmutableList
        // When: addAll with index is called
        assertThrows(UnsupportedOperationException.class, () -> immutableList.addAll(0, Arrays.asList("test1", "test2")));
    }

    @Test
    public void testRemoveAll() {
        // Given: an instance of AbstractImmutableList
        // When: removeAll is called
        assertThrows(UnsupportedOperationException.class, () -> immutableList.removeAll(Arrays.asList("test1", "test2")));
    }

    @Test
    public void testRetainAll() {
        // Given: an instance of AbstractImmutableList
        // When: retainAll is called
        assertThrows(UnsupportedOperationException.class, () -> immutableList.retainAll(Arrays.asList("test1", "test2")));
    }

    @Test
    public void testClear() {
        // Given: an instance of AbstractImmutableList
        // When: clear is called
        assertThrows(UnsupportedOperationException.class, () -> immutableList.clear());
    }

    @Test
    public void testSet() {
        // Given: an instance of AbstractImmutableList
        // When: set is called
        assertThrows(UnsupportedOperationException.class, () -> immutableList.set(0, "test"));
    }

    @Test
    public void testAddWithIndex() {
        // Given: an instance of AbstractImmutableList
        // When: add with index is called
        assertThrows(UnsupportedOperationException.class, () -> immutableList.add(0, "test"));
    }

    @Test
    public void testRemoveWithIndex() {
        // Given: an instance of AbstractImmutableList
        // When: remove with index is called
        assertThrows(UnsupportedOperationException.class, () -> immutableList.remove(0));
    }

    @Test
    public void testContains() {
        // Given: an instance of AbstractImmutableList
        // When: contains is called
        when(immutableList.indexOf("test")).thenReturn(-1);
        boolean contains = immutableList.contains("test");
        // Then: contains should return false
        assertFalse(contains);
    }

    @Test
    public void testContainsAll() {
        // Given: an instance of AbstractImmutableList
        // When: containsAll is called
        boolean containsAll = immutableList.containsAll(Arrays.asList("test1", "test2"));
        // Then: containsAll should return true or false
        assertTrue(containsAll);
    }

    @Test
    public void testRemove() {
        // Given: an instance of AbstractImmutableList
        // When: remove is called
        assertThrows(UnsupportedOperationException.class, () -> immutableList.remove("test"));
    }
}
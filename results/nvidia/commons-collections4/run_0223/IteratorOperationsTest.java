import org.apache.commons.collections4.iterators.IteratorOperations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IteratorOperationsTest {

    @Mock
    private IteratorOperations<String> iteratorOperations;

    @Mock
    private Collection<String> collection;

    @Mock
    private Supplier<Collection<String>> collectionSupplier;

    @BeforeEach
    public void setup() {
        // Initialize the iterator with some elements
        when(iteratorOperations.hasNext()).thenReturn(true, true, false);
        when(iteratorOperations.next()).thenReturn("Element1", "Element2");
    }

    @Test
    public void testAddTo() {
        // Given: a collection to add elements to
        Collection<String> targetCollection = new ArrayList<>();

        // When: add elements to the collection
        Collection<String> result = iteratorOperations.addTo(targetCollection);

        // Then: verify the result and the collection
        assertEquals(targetCollection, result);
        verify(iteratorOperations, times(2)).next();
        verify(iteratorOperations, times(2)).forEachRemaining(any());
    }

    @Test
    public void testRemoveNext() {
        // Given: the iterator has elements
        when(iteratorOperations.hasNext()).thenReturn(true, false);

        // When: remove the next element
        String result = iteratorOperations.removeNext();

        // Then: verify the result and the iterator
        assertNotNull(result);
        assertEquals("Element1", result);
        verify(iteratorOperations, times(1)).next();
        verify(iteratorOperations, times(1)).remove();
    }

    @Test
    public void testToCollection() {
        // Given: a collection supplier
        when(collectionSupplier.get()).thenReturn(new ArrayList<>());

        // When: convert the iterator to a collection
        Collection<String> result = iteratorOperations.toCollection(collectionSupplier);

        // Then: verify the result and the collection
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertEquals(2, result.size());
        verify(collectionSupplier, times(1)).get();
        verify(iteratorOperations, times(2)).next();
    }

    @Test
    public void testToList() {
        // When: convert the iterator to a list
        List<String> result = iteratorOperations.toList();

        // Then: verify the result and the list
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertEquals(2, result.size());
        verify(iteratorOperations, times(2)).next();
    }

    @Test
    public void testToSet() {
        // When: convert the iterator to a set
        Set<String> result = iteratorOperations.toSet();

        // Then: verify the result and the set
        assertNotNull(result);
        assertTrue(result instanceof Set);
        assertEquals(2, result.size());
        verify(iteratorOperations, times(2)).next();
    }
}
import org.apache.commons.collections4.collection.AbstractCollectionDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractCollectionDecoratorTest {

    @Mock
    private Collection<String> collection;

    private AbstractCollectionDecorator<String> abstractCollectionDecorator;

    @BeforeEach
    public void setup() {
        abstractCollectionDecorator = new AbstractCollectionDecorator<String>(collection) {
        };
    }

    @Test
    public void testAdd() {
        // Given
        String object = "Test";
        when(collection.add(object)).thenReturn(true);

        // When
        boolean result = abstractCollectionDecorator.add(object);

        // Then
        assertTrue(result);
        verify(collection, times(1)).add(object);
    }

    @Test
    public void testAddAll() {
        // Given
        Collection<String> coll = new ArrayList<>();
        coll.add("Test1");
        coll.add("Test2");
        when(collection.addAll(coll)).thenReturn(true);

        // When
        boolean result = abstractCollectionDecorator.addAll(coll);

        // Then
        assertTrue(result);
        verify(collection, times(1)).addAll(coll);
    }

    @Test
    public void testClear() {
        // When
        abstractCollectionDecorator.clear();

        // Then
        verify(collection, times(1)).clear();
    }

    @Test
    public void testContains() {
        // Given
        String object = "Test";
        when(collection.contains(object)).thenReturn(true);

        // When
        boolean result = abstractCollectionDecorator.contains(object);

        // Then
        assertTrue(result);
        verify(collection, times(1)).contains(object);
    }

    @Test
    public void testContainsAll() {
        // Given
        Collection<String> coll = new ArrayList<>();
        coll.add("Test1");
        coll.add("Test2");
        when(collection.containsAll(coll)).thenReturn(true);

        // When
        boolean result = abstractCollectionDecorator.containsAll(coll);

        // Then
        assertTrue(result);
        verify(collection, times(1)).containsAll(coll);
    }

    @Test
    public void testIsEmpty() {
        // Given
        when(collection.isEmpty()).thenReturn(true);

        // When
        boolean result = abstractCollectionDecorator.isEmpty();

        // Then
        assertTrue(result);
        verify(collection, times(1)).isEmpty();
    }

    @Test
    public void testIterator() {
        // When
        Iterator<String> iterator = abstractCollectionDecorator.iterator();

        // Then
        assertNotNull(iterator);
        verify(collection, times(1)).iterator();
    }

    @Test
    public void testRemove() {
        // Given
        String object = "Test";
        when(collection.remove(object)).thenReturn(true);

        // When
        boolean result = abstractCollectionDecorator.remove(object);

        // Then
        assertTrue(result);
        verify(collection, times(1)).remove(object);
    }

    @Test
    public void testRemoveAll() {
        // Given
        Collection<String> coll = new ArrayList<>();
        coll.add("Test1");
        coll.add("Test2");
        when(collection.removeAll(coll)).thenReturn(true);

        // When
        boolean result = abstractCollectionDecorator.removeAll(coll);

        // Then
        assertTrue(result);
        verify(collection, times(1)).removeAll(coll);
    }

    @Test
    public void testRemoveIf() {
        // Given
        Predicate<String> filter = any();
        when(collection.removeIf(filter)).thenReturn(true);

        // When
        boolean result = abstractCollectionDecorator.removeIf(filter);

        // Then
        assertTrue(result);
        verify(collection, times(1)).removeIf(filter);
    }

    @Test
    public void testRetainAll() {
        // Given
        Collection<String> coll = new ArrayList<>();
        coll.add("Test1");
        coll.add("Test2");
        when(collection.retainAll(coll)).thenReturn(true);

        // When
        boolean result = abstractCollectionDecorator.retainAll(coll);

        // Then
        assertTrue(result);
        verify(collection, times(1)).retainAll(coll);
    }

    @Test
    public void testSize() {
        // Given
        when(collection.size()).thenReturn(10);

        // When
        int result = abstractCollectionDecorator.size();

        // Then
        assertEquals(10, result);
        verify(collection, times(1)).size();
    }

    @Test
    public void testToString() {
        // Given
        when(collection.toString()).thenReturn("Test");

        // When
        String result = abstractCollectionDecorator.toString();

        // Then
        assertEquals("Test", result);
        verify(collection, times(1)).toString();
    }
}
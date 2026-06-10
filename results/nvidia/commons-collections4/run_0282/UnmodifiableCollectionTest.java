import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.collection.UnmodifiableCollection;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnmodifiableCollectionTest {

    @Mock
    private Collection<String> collection;

    @Mock
    private Iterator<String> iterator;

    @Mock
    private Predicate<String> predicate;

    private UnmodifiableCollection<String> unmodifiableCollection;

    @BeforeEach
    public void setup() {
        unmodifiableCollection = new UnmodifiableCollection<>(collection);
    }

    @Test
    public void testUnmodifiableCollection() {
        // Given
        Collection<String> coll = new ArrayList<>();
        coll.add("element1");
        coll.add("element2");

        // When
        Collection<String> unmodifiableColl = UnmodifiableCollection.unmodifiableCollection(coll);

        // Then
        assertNotSame(coll, unmodifiableColl);
        assertTrue(unmodifiableColl.contains("element1"));
        assertTrue(unmodifiableColl.contains("element2"));
    }

    @Test
    public void testUnmodifiableCollection_AlreadyUnmodifiable() {
        // Given
        UnmodifiableCollection<String> coll = new UnmodifiableCollection<>(new ArrayList<>());

        // When
        Collection<String> unmodifiableColl = UnmodifiableCollection.unmodifiableCollection(coll);

        // Then
        assertSame(coll, unmodifiableColl);
    }

    @Test
    public void testAdd() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableCollection.add("element"));
    }

    @Test
    public void testAddAll() {
        // Given
        Collection<String> elements = new ArrayList<>();
        elements.add("element1");
        elements.add("element2");

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableCollection.addAll(elements));
    }

    @Test
    public void testClear() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableCollection.clear());
    }

    @Test
    public void testIterator() {
        // Given
        when(collection.iterator()).thenReturn(iterator);

        // When
        Iterator<String> unmodifiableIterator = unmodifiableCollection.iterator();

        // Then
        assertNotSame(iterator, unmodifiableIterator);
        verify(collection, times(1)).iterator();
    }

    @Test
    public void testRemove() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableCollection.remove("element"));
    }

    @Test
    public void testRemoveAll() {
        // Given
        Collection<String> elements = new ArrayList<>();
        elements.add("element1");
        elements.add("element2");

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableCollection.removeAll(elements));
    }

    @Test
    public void testRemoveIf() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableCollection.removeIf(predicate));
    }

    @Test
    public void testRetainAll() {
        // Given
        Collection<String> elements = new ArrayList<>();
        elements.add("element1");
        elements.add("element2");

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableCollection.retainAll(elements));
    }
}
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.bag.UnmodifiableBag;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;
import org.apache.commons.collections4.set.UnmodifiableSet;
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
public class UnmodifiableBagTest {

    @Mock
    private Bag<String> bag;

    private UnmodifiableBag<String> unmodifiableBag;

    @BeforeEach
    public void setup() {
        unmodifiableBag = new UnmodifiableBag<>(bag);
    }

    @Test
    public void testUnmodifiableBagFactoryMethod() {
        // Given
        Bag<String> originalBag = mock(Bag.class);

        // When
        Bag<String> unmodifiableBag = UnmodifiableBag.unmodifiableBag(originalBag);

        // Then
        assertNotSame(originalBag, unmodifiableBag);
    }

    @Test
    public void testUnmodifiableBagFactoryMethod_ReturnsOriginalBag_IfAlreadyUnmodifiable() {
        // Given
        UnmodifiableBag<String> originalBag = new UnmodifiableBag<>(bag);

        // When
        Bag<String> unmodifiableBag = UnmodifiableBag.unmodifiableBag(originalBag);

        // Then
        assertSame(originalBag, unmodifiableBag);
    }

    @Test
    public void testAdd_ThrowsUnsupportedOperationException() {
        // Given
        String object = "Test Object";

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableBag.add(object));
    }

    @Test
    public void testAdd_WithCount_ThrowsUnsupportedOperationException() {
        // Given
        String object = "Test Object";
        int count = 2;

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableBag.add(object, count));
    }

    @Test
    public void testAddAll_ThrowsUnsupportedOperationException() {
        // Given
        Collection<String> collection = new ArrayList<>();

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableBag.addAll(collection));
    }

    @Test
    public void testClear_ThrowsUnsupportedOperationException() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableBag.clear());
    }

    @Test
    public void testIterator() {
        // Given
        Iterator<String> iterator = mock(Iterator.class);

        when(bag.iterator()).thenReturn(iterator);

        // When
        Iterator<String> unmodifiableIterator = unmodifiableBag.iterator();

        // Then
        assertNotSame(iterator, unmodifiableIterator);
        assertTrue(unmodifiableIterator instanceof UnmodifiableIterator);
    }

    @Test
    public void testRemove_ThrowsUnsupportedOperationException() {
        // Given
        Object object = "Test Object";

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableBag.remove(object));
    }

    @Test
    public void testRemove_WithCount_ThrowsUnsupportedOperationException() {
        // Given
        Object object = "Test Object";
        int count = 2;

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableBag.remove(object, count));
    }

    @Test
    public void testRemoveAll_ThrowsUnsupportedOperationException() {
        // Given
        Collection<?> collection = new ArrayList<>();

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableBag.removeAll(collection));
    }

    @Test
    public void testRemoveIf_ThrowsUnsupportedOperationException() {
        // Given
        Predicate<String> filter = any();

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableBag.removeIf(filter));
    }

    @Test
    public void testRetainAll_ThrowsUnsupportedOperationException() {
        // Given
        Collection<?> collection = new ArrayList<>();

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableBag.retainAll(collection));
    }

    @Test
    public void testUniqueSet() {
        // Given
        Set<String> set = mock(Set.class);

        when(bag.uniqueSet()).thenReturn(set);

        // When
        Set<String> unmodifiableSet = unmodifiableBag.uniqueSet();

        // Then
        assertNotSame(set, unmodifiableSet);
        assertTrue(unmodifiableSet instanceof UnmodifiableSet);
    }
}
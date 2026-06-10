import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.multiset.UnmodifiableMultiSet;
import org.apache.commons.collections4.set.UnmodifiableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnmodifiableMultiSetTest {

    @Mock
    private MultiSet<String> multiSet;

    private UnmodifiableMultiSet<String> unmodifiableMultiSet;

    @BeforeEach
    void setup() {
        unmodifiableMultiSet = UnmodifiableMultiSet.unmodifiableMultiSet(multiSet);
    }

    @Test
    void testUnmodifiableMultiSet() {
        // Given
        MultiSet<String> originalMultiSet = mock(MultiSet.class);
        UnmodifiableMultiSet<String> unmodifiableMultiSet = UnmodifiableMultiSet.unmodifiableMultiSet(originalMultiSet);

        // When
        MultiSet<String> result = UnmodifiableMultiSet.unmodifiableMultiSet(unmodifiableMultiSet);

        // Then
        assertSame(unmodifiableMultiSet, result);
    }

    @Test
    void testAdd() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiSet.add("test"));
    }

    @Test
    void testAddWithCount() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiSet.add("test", 1));
    }

    @Test
    void testAddAll() {
        // Given
        Collection<String> collection = new ArrayList<>();

        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiSet.addAll(collection));
    }

    @Test
    void testClear() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiSet.clear());
    }

    @Test
    void testEntrySet() {
        // When
        Set<org.apache.commons.collections4.MultiSet.Entry<String>> entrySet = unmodifiableMultiSet.entrySet();

        // Then
        assertNotNull(entrySet);
        assertTrue(entrySet instanceof UnmodifiableSet);
    }

    @Test
    void testIterator() {
        // When
        Iterator<String> iterator = unmodifiableMultiSet.iterator();

        // Then
        assertNotNull(iterator);
    }

    @Test
    void testRemove() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiSet.remove("test"));
    }

    @Test
    void testRemoveWithCount() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiSet.remove("test", 1));
    }

    @Test
    void testRemoveAll() {
        // Given
        Collection<String> collection = new ArrayList<>();

        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiSet.removeAll(collection));
    }

    @Test
    void testRemoveIf() {
        // Given
        Predicate<String> predicate = mock(Predicate.class);

        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiSet.removeIf(predicate));
    }

    @Test
    void testRetainAll() {
        // Given
        Collection<String> collection = new ArrayList<>();

        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiSet.retainAll(collection));
    }

    @Test
    void testSetCount() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiSet.setCount("test", 1));
    }

    @Test
    void testUniqueSet() {
        // When
        Set<String> uniqueSet = unmodifiableMultiSet.uniqueSet();

        // Then
        assertNotNull(uniqueSet);
        assertTrue(uniqueSet instanceof UnmodifiableSet);
    }
}
import org.apache.commons.collections4.map.UnmodifiableEntrySet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnmodifiableEntrySetTest {

    @Mock
    private Set<Map.Entry<String, String>> set;

    @Mock
    private Map.Entry<String, String> entry;

    private UnmodifiableEntrySet<String, String> unmodifiableEntrySet;

    @BeforeEach
    public void setup() {
        unmodifiableEntrySet = new UnmodifiableEntrySet<>(set);
    }

    @Test
    public void testUnmodifiableEntrySet() {
        // Given
        when(set.iterator()).thenReturn(Collections.emptyIterator());

        // When
        Set<Map.Entry<String, String>> result = UnmodifiableEntrySet.unmodifiableEntrySet(set);

        // Then
        assertNotNull(result);
        assertSame(set, result);
    }

    @Test
    public void testAdd() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableEntrySet.add(entry));
    }

    @Test
    public void testAddAll() {
        // Given
        Collection<Map.Entry<String, String>> collection = new ArrayList<>();

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableEntrySet.addAll(collection));
    }

    @Test
    public void testClear() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableEntrySet.clear());
    }

    @Test
    public void testIterator() {
        // Given
        when(set.iterator()).thenReturn(Collections.emptyIterator());

        // When
        Iterator<Map.Entry<String, String>> iterator = unmodifiableEntrySet.iterator();

        // Then
        assertNotNull(iterator);
    }

    @Test
    public void testRemove() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableEntrySet.remove(entry));
    }

    @Test
    public void testRemoveAll() {
        // Given
        Collection<?> collection = new ArrayList<>();

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableEntrySet.removeAll(collection));
    }

    @Test
    public void testRemoveIf() {
        // Given
        Predicate<Map.Entry<String, String>> predicate = any();

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableEntrySet.removeIf(predicate));
    }

    @Test
    public void testRetainAll() {
        // Given
        Collection<?> collection = new ArrayList<>();

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableEntrySet.retainAll(collection));
    }

    @Test
    public void testToArray() {
        // Given
        when(set.toArray()).thenReturn(new Object[0]);

        // When
        Object[] array = unmodifiableEntrySet.toArray();

        // Then
        assertNotNull(array);
    }

    @Test
    public void testToArrayWithArray() {
        // Given
        when(set.toArray(any())).thenReturn(new Object[0]);

        // When
        String[] array = unmodifiableEntrySet.toArray(new String[0]);

        // Then
        assertNotNull(array);
    }
}
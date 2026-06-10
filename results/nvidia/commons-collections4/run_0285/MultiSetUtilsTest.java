import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.multiset.HashMultiSet;
import org.apache.commons.collections4.multiset.PredicatedMultiSet;
import org.apache.commons.collections4.multiset.SynchronizedMultiSet;
import org.apache.commons.collections4.multiset.UnmodifiableMultiSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MultiSetUtilsTest {

    @Mock
    private MultiSet<String> multiSet;

    @Mock
    private Predicate<String> predicate;

    @BeforeEach
    public void setup() {
        // Initialize mocks
        when(multiSet.add(any())).thenReturn(true);
        when(multiSet.add(any(), anyInt())).thenReturn(1);
        when(multiSet.containsAll(any(Collection.class))).thenReturn(true);
        when(multiSet.getCount(any())).thenReturn(1);
        when(multiSet.remove(any())).thenReturn(true);
        when(multiSet.remove(any(), anyInt())).thenReturn(1);
        when(multiSet.removeAll(any(Collection.class))).thenReturn(true);
        when(multiSet.retainAll(any(Collection.class))).thenReturn(true);
        when(multiSet.setCount(any(), anyInt())).thenReturn(1);
        when(multiSet.size()).thenReturn(1);
        when(predicate.test(any())).thenReturn(true);
    }

    @Test
    public void testEmptyMultiSet() {
        // Given
        MultiSet<String> emptyMultiSet = MultiSetUtils.emptyMultiSet();

        // When
        boolean addResult = emptyMultiSet.add("test");
        int addCountResult = emptyMultiSet.add("test", 2);
        boolean containsAllResult = emptyMultiSet.containsAll(Collection.of("test"));
        int countResult = emptyMultiSet.getCount("test");
        boolean removeResult = emptyMultiSet.remove("test");
        int removeCountResult = emptyMultiSet.remove("test", 2);
        boolean removeAllResult = emptyMultiSet.removeAll(Collection.of("test"));
        boolean retainAllResult = emptyMultiSet.retainAll(Collection.of("test"));
        int setCountResult = emptyMultiSet.setCount("test", 2);
        int sizeResult = emptyMultiSet.size();

        // Then
        assertFalse(addResult);
        assertEquals(0, addCountResult);
        assertFalse(containsAllResult);
        assertEquals(0, countResult);
        assertFalse(removeResult);
        assertEquals(0, removeCountResult);
        assertFalse(removeAllResult);
        assertFalse(retainAllResult);
        assertEquals(0, setCountResult);
        assertEquals(0, sizeResult);
    }

    @Test
    public void testPredicatedMultiSet() {
        // Given
        MultiSet<String> predicatedMultiSet = MultiSetUtils.predicatedMultiSet(multiSet, predicate);

        // When
        boolean addResult = predicatedMultiSet.add("test");
        int addCountResult = predicatedMultiSet.add("test", 2);
        boolean containsAllResult = predicatedMultiSet.containsAll(Collection.of("test"));
        int countResult = predicatedMultiSet.getCount("test");
        boolean removeResult = predicatedMultiSet.remove("test");
        int removeCountResult = predicatedMultiSet.remove("test", 2);
        boolean removeAllResult = predicatedMultiSet.removeAll(Collection.of("test"));
        boolean retainAllResult = predicatedMultiSet.retainAll(Collection.of("test"));
        int setCountResult = predicatedMultiSet.setCount("test", 2);
        int sizeResult = predicatedMultiSet.size();

        // Then
        assertTrue(addResult);
        assertEquals(1, addCountResult);
        assertTrue(containsAllResult);
        assertEquals(1, countResult);
        assertTrue(removeResult);
        assertEquals(1, removeCountResult);
        assertTrue(removeAllResult);
        assertTrue(retainAllResult);
        assertEquals(1, setCountResult);
        assertEquals(1, sizeResult);
    }

    @Test
    public void testPredicatedMultiSetNullMultiSet() {
        // Given
        MultiSet<String> predicatedMultiSet = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> MultiSetUtils.predicatedMultiSet(predicatedMultiSet, predicate));
    }

    @Test
    public void testPredicatedMultiSetNullPredicate() {
        // Given
        Predicate<String> nullPredicate = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> MultiSetUtils.predicatedMultiSet(multiSet, nullPredicate));
    }

    @Test
    public void testSynchronizedMultiSet() {
        // Given
        MultiSet<String> synchronizedMultiSet = MultiSetUtils.synchronizedMultiSet(multiSet);

        // When
        boolean addResult = synchronizedMultiSet.add("test");
        int addCountResult = synchronizedMultiSet.add("test", 2);
        boolean containsAllResult = synchronizedMultiSet.containsAll(Collection.of("test"));
        int countResult = synchronizedMultiSet.getCount("test");
        boolean removeResult = synchronizedMultiSet.remove("test");
        int removeCountResult = synchronizedMultiSet.remove("test", 2);
        boolean removeAllResult = synchronizedMultiSet.removeAll(Collection.of("test"));
        boolean retainAllResult = synchronizedMultiSet.retainAll(Collection.of("test"));
        int setCountResult = synchronizedMultiSet.setCount("test", 2);
        int sizeResult = synchronizedMultiSet.size();

        // Then
        assertTrue(addResult);
        assertEquals(1, addCountResult);
        assertTrue(containsAllResult);
        assertEquals(1, countResult);
        assertTrue(removeResult);
        assertEquals(1, removeCountResult);
        assertTrue(removeAllResult);
        assertTrue(retainAllResult);
        assertEquals(1, setCountResult);
        assertEquals(1, sizeResult);
    }

    @Test
    public void testSynchronizedMultiSetNullMultiSet() {
        // Given
        MultiSet<String> nullMultiSet = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> MultiSetUtils.synchronizedMultiSet(nullMultiSet));
    }

    @Test
    public void testUnmodifiableMultiSet() {
        // Given
        MultiSet<String> unmodifiableMultiSet = MultiSetUtils.unmodifiableMultiSet(multiSet);

        // When
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiSet.add("test"));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiSet.add("test", 2));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiSet.remove("test"));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiSet.remove("test", 2));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiSet.removeAll(Collection.of("test")));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiSet.retainAll(Collection.of("test")));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiSet.setCount("test", 2));
    }

    @Test
    public void testUnmodifiableMultiSetNullMultiSet() {
        // Given
        MultiSet<String> nullMultiSet = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> MultiSetUtils.unmodifiableMultiSet(nullMultiSet));
    }
}
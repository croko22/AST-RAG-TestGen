import org.apache.commons.collections4.iterators.EmptyIterator;
import org.apache.commons.collections4.ResettableIterator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmptyIteratorTest {

    @Test
    public void testEmptyIterator() {
        // Given: no setup needed
        // When: get an empty iterator instance
        Iterator instance = EmptyIterator.emptyIterator();

        // Then: verify instance is not null
        assertNotNull(instance);

        // Then: verify instance has no elements
        assertFalse(instance.hasNext());
    }

    @Test
    public void testResettableEmptyIterator() {
        // Given: no setup needed
        // When: get a resettable empty iterator instance
        ResettableIterator instance = EmptyIterator.resettableEmptyIterator();

        // Then: verify instance is not null
        assertNotNull(instance);

        // Then: verify instance has no elements
        assertFalse(instance.hasNext());
    }

    @Test
    public void testResettableEmptyIterator_Reset() {
        // Given: get a resettable empty iterator instance
        ResettableIterator instance = EmptyIterator.resettableEmptyIterator();

        // When: reset the iterator
        // Note: Since the iterator is empty, reset has no effect
        instance.reset();

        // Then: verify instance still has no elements
        assertFalse(instance.hasNext());
    }

    @Test
    public void testEmptyIterator_MultipleCalls() {
        // Given: no setup needed
        // When: get multiple empty iterator instances
        Iterator instance1 = EmptyIterator.emptyIterator();
        Iterator instance2 = EmptyIterator.emptyIterator();

        // Then: verify instances are the same (singleton)
        assertSame(instance1, instance2);
    }

    @Test
    public void testResettableEmptyIterator_MultipleCalls() {
        // Given: no setup needed
        // When: get multiple resettable empty iterator instances
        ResettableIterator instance1 = EmptyIterator.resettableEmptyIterator();
        ResettableIterator instance2 = EmptyIterator.resettableEmptyIterator();

        // Then: verify instances are the same (singleton)
        assertSame(instance1, instance2);
    }
}
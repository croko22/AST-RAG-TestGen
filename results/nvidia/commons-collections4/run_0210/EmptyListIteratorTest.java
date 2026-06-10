import org.apache.commons.collections4.iterators.EmptyListIterator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmptyListIteratorTest {

    @Test
    public void testEmptyListIterator() {
        // Given: no setup needed
        // When: get an instance of the empty list iterator
        ListIterator instance = EmptyListIterator.emptyListIterator();
        // Then: verify the instance is not null
        assertNotNull(instance);
    }

    @Test
    public void testResettableEmptyListIterator() {
        // Given: no setup needed
        // When: get an instance of the resettable empty list iterator
        EmptyListIterator.ResettableListIterator instance = EmptyListIterator.resettableEmptyListIterator();
        // Then: verify the instance is not null
        assertNotNull(instance);
    }

    @Test
    public void testEmptyListIterator_Singleton() {
        // Given: no setup needed
        // When: get two instances of the empty list iterator
        ListIterator instance1 = EmptyListIterator.emptyListIterator();
        ListIterator instance2 = EmptyListIterator.emptyListIterator();
        // Then: verify both instances are the same
        assertSame(instance1, instance2);
    }

    @Test
    public void testResettableEmptyListIterator_Singleton() {
        // Given: no setup needed
        // When: get two instances of the resettable empty list iterator
        EmptyListIterator.ResettableListIterator instance1 = EmptyListIterator.resettableEmptyListIterator();
        EmptyListIterator.ResettableListIterator instance2 = EmptyListIterator.resettableEmptyListIterator();
        // Then: verify both instances are the same
        assertSame(instance1, instance2);
    }

    @Test
    public void testEmptyListIterator_TypeSafety() {
        // Given: no setup needed
        // When: get an instance of the empty list iterator with a specific type
        ListIterator<String> instance = EmptyListIterator.emptyListIterator();
        // Then: verify the instance is not null
        assertNotNull(instance);
    }

    @Test
    public void testResettableEmptyListIterator_TypeSafety() {
        // Given: no setup needed
        // When: get an instance of the resettable empty list iterator with a specific type
        EmptyListIterator.ResettableListIterator instance = EmptyListIterator.resettableEmptyListIterator();
        // Then: verify the instance is not null
        assertNotNull(instance);
    }
}
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.collection.AbstractCollectionDecorator;
import org.apache.commons.collections4.multiset.AbstractMultiSetDecorator;
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
public class AbstractMultiSetDecoratorTest {

    @Mock
    private MultiSet<String> multiset;

    private AbstractMultiSetDecorator<String> decorator;

    @BeforeEach
    public void setup() {
        decorator = new AbstractMultiSetDecorator<String>(multiset) {
        };
    }

    @Test
    public void testAdd() {
        // Given
        String object = "test";
        int count = 5;
        when(multiset.add(object, count)).thenReturn(3);

        // When
        int result = decorator.add(object, count);

        // Then
        assertEquals(3, result);
        verify(multiset, times(1)).add(object, count);
    }

    @Test
    public void testEntrySet() {
        // Given
        Set<MultiSet.Entry<String>> entrySet = new HashSet<>();
        when(multiset.entrySet()).thenReturn(entrySet);

        // When
        Set<MultiSet.Entry<String>> result = decorator.entrySet();

        // Then
        assertEquals(entrySet, result);
        verify(multiset, times(1)).entrySet();
    }

    @Test
    public void testEquals_SameObject() {
        // Given
        Object object = decorator;

        // When
        boolean result = decorator.equals(object);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_SameContent() {
        // Given
        MultiSet<String> otherMultiset = mock(MultiSet.class);
        when(otherMultiset.equals(multiset)).thenReturn(true);
        AbstractMultiSetDecorator<String> otherDecorator = new AbstractMultiSetDecorator<String>(otherMultiset) {
        };

        // When
        boolean result = decorator.equals(otherDecorator);

        // Then
        assertTrue(result);
        verify(multiset, times(1)).equals(otherMultiset);
    }

    @Test
    public void testEquals_DifferentObject_DifferentContent() {
        // Given
        MultiSet<String> otherMultiset = mock(MultiSet.class);
        when(otherMultiset.equals(multiset)).thenReturn(false);
        AbstractMultiSetDecorator<String> otherDecorator = new AbstractMultiSetDecorator<String>(otherMultiset) {
        };

        // When
        boolean result = decorator.equals(otherDecorator);

        // Then
        assertFalse(result);
        verify(multiset, times(1)).equals(otherMultiset);
    }

    @Test
    public void testGetCount() {
        // Given
        String object = "test";
        when(multiset.getCount(object)).thenReturn(5);

        // When
        int result = decorator.getCount(object);

        // Then
        assertEquals(5, result);
        verify(multiset, times(1)).getCount(object);
    }

    @Test
    public void testHashCode() {
        // Given
        when(multiset.hashCode()).thenReturn(12345);

        // When
        int result = decorator.hashCode();

        // Then
        assertEquals(12345, result);
        verify(multiset, times(1)).hashCode();
    }

    @Test
    public void testRemove() {
        // Given
        String object = "test";
        int count = 5;
        when(multiset.remove(object, count)).thenReturn(3);

        // When
        int result = decorator.remove(object, count);

        // Then
        assertEquals(3, result);
        verify(multiset, times(1)).remove(object, count);
    }

    @Test
    public void testSetCount() {
        // Given
        String object = "test";
        int count = 5;
        when(multiset.setCount(object, count)).thenReturn(3);

        // When
        int result = decorator.setCount(object, count);

        // Then
        assertEquals(3, result);
        verify(multiset, times(1)).setCount(object, count);
    }

    @Test
    public void testUniqueSet() {
        // Given
        Set<String> uniqueSet = new HashSet<>();
        when(multiset.uniqueSet()).thenReturn(uniqueSet);

        // When
        Set<String> result = decorator.uniqueSet();

        // Then
        assertEquals(uniqueSet, result);
        verify(multiset, times(1)).uniqueSet();
    }
}
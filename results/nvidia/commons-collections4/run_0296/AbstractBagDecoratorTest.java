import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.AbstractBagDecorator;
import org.apache.commons.collections4.collection.AbstractCollectionDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractBagDecoratorTest {

    @Mock
    private Bag<String> bag;

    @InjectMocks
    private AbstractBagDecorator<String> abstractBagDecorator;

    @BeforeEach
    void setup() {
        abstractBagDecorator = new AbstractBagDecorator<>(bag) {
        };
    }

    @Test
    public void testAdd() {
        // Given
        String object = "Test Object";
        int count = 5;
        when(bag.add(object, count)).thenReturn(true);

        // When
        boolean result = abstractBagDecorator.add(object, count);

        // Then
        assertTrue(result);
        verify(bag, times(1)).add(object, count);
    }

    @Test
    public void testAdd_False() {
        // Given
        String object = "Test Object";
        int count = 5;
        when(bag.add(object, count)).thenReturn(false);

        // When
        boolean result = abstractBagDecorator.add(object, count);

        // Then
        assertFalse(result);
        verify(bag, times(1)).add(object, count);
    }

    @Test
    public void testEquals_SameObject() {
        // Given
        Object object = abstractBagDecorator;

        // When
        boolean result = abstractBagDecorator.equals(object);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject() {
        // Given
        Object object = new Object();

        // When
        boolean result = abstractBagDecorator.equals(object);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals_Null() {
        // Given
        Object object = null;

        // When
        boolean result = abstractBagDecorator.equals(object);

        // Then
        assertFalse(result);
    }

    @Test
    public void testGetCount() {
        // Given
        String object = "Test Object";
        int count = 5;
        when(bag.getCount(object)).thenReturn(count);

        // When
        int result = abstractBagDecorator.getCount(object);

        // Then
        assertEquals(count, result);
        verify(bag, times(1)).getCount(object);
    }

    @Test
    public void testHashCode() {
        // Given
        int hashCode = 12345;
        when(bag.hashCode()).thenReturn(hashCode);

        // When
        int result = abstractBagDecorator.hashCode();

        // Then
        assertEquals(hashCode, result);
        verify(bag, times(1)).hashCode();
    }

    @Test
    public void testRemove() {
        // Given
        String object = "Test Object";
        int count = 5;
        when(bag.remove(object, count)).thenReturn(true);

        // When
        boolean result = abstractBagDecorator.remove(object, count);

        // Then
        assertTrue(result);
        verify(bag, times(1)).remove(object, count);
    }

    @Test
    public void testRemove_False() {
        // Given
        String object = "Test Object";
        int count = 5;
        when(bag.remove(object, count)).thenReturn(false);

        // When
        boolean result = abstractBagDecorator.remove(object, count);

        // Then
        assertFalse(result);
        verify(bag, times(1)).remove(object, count);
    }

    @Test
    public void testUniqueSet() {
        // Given
        Set<String> uniqueSet = new HashSet<>();
        uniqueSet.add("Test Object 1");
        uniqueSet.add("Test Object 2");
        when(bag.uniqueSet()).thenReturn(uniqueSet);

        // When
        Set<String> result = abstractBagDecorator.uniqueSet();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bag, times(1)).uniqueSet();
    }

    @Test
    public void testUniqueSet_Empty() {
        // Given
        Set<String> uniqueSet = new HashSet<>();
        when(bag.uniqueSet()).thenReturn(uniqueSet);

        // When
        Set<String> result = abstractBagDecorator.uniqueSet();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bag, times(1)).uniqueSet();
    }
}
import org.apache.commons.collections4.collection.AbstractCollectionDecorator;
import org.apache.commons.collections4.list.AbstractListDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractListDecoratorTest {

    @Mock
    private List<String> list;

    private AbstractListDecorator<String> abstractListDecorator;

    @BeforeEach
    public void setup() {
        abstractListDecorator = new AbstractListDecorator<String>(list) {
        };
    }

    @Test
    public void testAdd() {
        // Given
        String object = "Test";
        int index = 0;

        // When
        abstractListDecorator.add(index, object);

        // Then
        verify(list, times(1)).add(index, object);
    }

    @Test
    public void testAddAll() {
        // Given
        Collection<String> collection = Arrays.asList("Test1", "Test2");
        int index = 0;

        // When
        boolean result = abstractListDecorator.addAll(index, collection);

        // Then
        verify(list, times(1)).addAll(index, collection);
        assertTrue(result);
    }

    @Test
    public void testEquals() {
        // Given
        Object object = abstractListDecorator;

        // When
        boolean result = abstractListDecorator.equals(object);

        // Then
        assertTrue(result);
    }

    @Test
    public void testGet() {
        // Given
        int index = 0;
        String object = "Test";

        // When
        when(list.get(index)).thenReturn(object);
        String result = abstractListDecorator.get(index);

        // Then
        verify(list, times(1)).get(index);
        assertEquals(object, result);
    }

    @Test
    public void testHashCode() {
        // Given
        int hashCode = 123;

        // When
        when(list.hashCode()).thenReturn(hashCode);
        int result = abstractListDecorator.hashCode();

        // Then
        verify(list, times(1)).hashCode();
        assertEquals(hashCode, result);
    }

    @Test
    public void testIndexOf() {
        // Given
        Object object = "Test";
        int index = 0;

        // When
        when(list.indexOf(object)).thenReturn(index);
        int result = abstractListDecorator.indexOf(object);

        // Then
        verify(list, times(1)).indexOf(object);
        assertEquals(index, result);
    }

    @Test
    public void testLastIndexOf() {
        // Given
        Object object = "Test";
        int index = 0;

        // When
        when(list.lastIndexOf(object)).thenReturn(index);
        int result = abstractListDecorator.lastIndexOf(object);

        // Then
        verify(list, times(1)).lastIndexOf(object);
        assertEquals(index, result);
    }

    @Test
    public void testListIterator() {
        // Given
        ListIterator<String> listIterator = mock(ListIterator.class);

        // When
        when(list.listIterator()).thenReturn(listIterator);
        ListIterator<String> result = abstractListDecorator.listIterator();

        // Then
        verify(list, times(1)).listIterator();
        assertEquals(listIterator, result);
    }

    @Test
    public void testListIteratorWithIndex() {
        // Given
        int index = 0;
        ListIterator<String> listIterator = mock(ListIterator.class);

        // When
        when(list.listIterator(index)).thenReturn(listIterator);
        ListIterator<String> result = abstractListDecorator.listIterator(index);

        // Then
        verify(list, times(1)).listIterator(index);
        assertEquals(listIterator, result);
    }

    @Test
    public void testRemove() {
        // Given
        int index = 0;
        String object = "Test";

        // When
        when(list.remove(index)).thenReturn(object);
        String result = abstractListDecorator.remove(index);

        // Then
        verify(list, times(1)).remove(index);
        assertEquals(object, result);
    }

    @Test
    public void testSet() {
        // Given
        int index = 0;
        String object = "Test";
        String resultObject = "TestResult";

        // When
        when(list.set(index, object)).thenReturn(resultObject);
        String result = abstractListDecorator.set(index, object);

        // Then
        verify(list, times(1)).set(index, object);
        assertEquals(resultObject, result);
    }

    @Test
    public void testSubList() {
        // Given
        int fromIndex = 0;
        int toIndex = 1;
        List<String> subList = new ArrayList<>();

        // When
        when(list.subList(fromIndex, toIndex)).thenReturn(subList);
        List<String> result = abstractListDecorator.subList(fromIndex, toIndex);

        // Then
        verify(list, times(1)).subList(fromIndex, toIndex);
        assertEquals(subList, result);
    }
}
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.collection.TransformedCollection;
import org.apache.commons.collections4.iterators.AbstractListIteratorDecorator;
import org.apache.commons.collections4.list.TransformedList;
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
public class TransformedListTest {

    @Mock
    private List<String> list;

    @Mock
    private Transformer<String, String> transformer;

    private TransformedList<String> transformedList;

    @BeforeEach
    public void setup() {
        transformedList = new TransformedList<>(list, transformer);
    }

    @Test
    public void testAdd() {
        // Given
        String object = "object";
        when(transformer.apply(object)).thenReturn("transformedObject");

        // When
        transformedList.add(object);

        // Then
        verify(list).add("transformedObject");
    }

    @Test
    public void testSet() {
        // Given
        String object = "object";
        when(transformer.apply(object)).thenReturn("transformedObject");

        // When
        transformedList.set(0, object);

        // Then
        verify(list).set(0, "transformedObject");
    }

    @Test
    public void testTransformedList() {
        // Given
        List<String> originalList = new ArrayList<>(Arrays.asList("object1", "object2"));
        when(transformer.apply(any())).thenAnswer(invocation -> "transformed" + invocation.getArgument(0));

        // When
        TransformedList<String> transformedList = TransformedList.transformedList(originalList, transformer);

        // Then
        assertEquals(2, transformedList.size());
        assertEquals("transformedobject1", transformedList.get(0));
        assertEquals("transformedobject2", transformedList.get(1));
    }

    @Test
    public void testTransformingList() {
        // Given
        List<String> originalList = new ArrayList<>(Arrays.asList("object1", "object2"));

        // When
        TransformedList<String> transformingList = TransformedList.transformingList(originalList, transformer);

        // Then
        assertEquals(2, transformingList.size());
        assertEquals("object1", transformingList.get(0));
        assertEquals("object2", transformingList.get(1));
    }

    @Test
    public void testAddAtIndex() {
        // Given
        String object = "object";
        when(transformer.apply(object)).thenReturn("transformedObject");

        // When
        transformedList.add(0, object);

        // Then
        verify(list).add(0, "transformedObject");
    }

    @Test
    public void testAddAll() {
        // Given
        Collection<String> collection = new ArrayList<>(Arrays.asList("object1", "object2"));
        when(transformer.apply(any())).thenAnswer(invocation -> "transformed" + invocation.getArgument(0));

        // When
        boolean result = transformedList.addAll(0, collection);

        // Then
        assertTrue(result);
        verify(list).addAll(0, argThat(c -> c.size() == 2 && c.contains("transformedobject1") && c.contains("transformedobject2")));
    }

    @Test
    public void testEquals() {
        // Given
        TransformedList<String> otherList = new TransformedList<>(list, transformer);

        // When
        boolean result = transformedList.equals(otherList);

        // Then
        assertTrue(result);
    }

    @Test
    public void testGet() {
        // Given
        String object = "object";
        when(list.get(0)).thenReturn(object);

        // When
        String result = transformedList.get(0);

        // Then
        assertEquals(object, result);
    }

    @Test
    public void testHashCode() {
        // Given
        int hashCode = 123;
        when(list.hashCode()).thenReturn(hashCode);

        // When
        int result = transformedList.hashCode();

        // Then
        assertEquals(hashCode, result);
    }

    @Test
    public void testIndexOf() {
        // Given
        String object = "object";
        when(list.indexOf(object)).thenReturn(0);

        // When
        int result = transformedList.indexOf(object);

        // Then
        assertEquals(0, result);
    }

    @Test
    public void testLastIndexOf() {
        // Given
        String object = "object";
        when(list.lastIndexOf(object)).thenReturn(0);

        // When
        int result = transformedList.lastIndexOf(object);

        // Then
        assertEquals(0, result);
    }

    @Test
    public void testListIterator() {
        // Given
        ListIterator<String> iterator = mock(ListIterator.class);

        // When
        ListIterator<String> result = transformedList.listIterator();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testRemove() {
        // Given
        String object = "object";
        when(list.remove(0)).thenReturn(object);

        // When
        String result = transformedList.remove(0);

        // Then
        assertEquals(object, result);
    }

    @Test
    public void testSetAtIndex() {
        // Given
        String object = "object";
        when(transformer.apply(object)).thenReturn("transformedObject");

        // When
        String result = transformedList.set(0, object);

        // Then
        verify(list).set(0, "transformedObject");
    }

    @Test
    public void testSubList() {
        // Given
        List<String> subList = new ArrayList<>(Arrays.asList("object1", "object2"));
        when(list.subList(0, 2)).thenReturn(subList);

        // When
        List<String> result = transformedList.subList(0, 2);

        // Then
        assertNotNull(result);
    }
}
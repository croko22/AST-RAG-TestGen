import org.apache.commons.collections4.list.FixedSizeList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FixedSizeListTest {

    @Mock
    private List<String> mockList;

    private FixedSizeList<String> fixedSizeList;

    @BeforeEach
    void setup() {
        fixedSizeList = FixedSizeList.fixedSizeList(mockList);
    }

    @Test
    void testFixedSizeList() {
        // Given
        List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        FixedSizeList<String> fixedSizeList = FixedSizeList.fixedSizeList(list);

        // Then
        assertEquals(list, fixedSizeList);
    }

    @Test
    void testFixedSizeList_NullList() {
        // Then
        assertThrows(NullPointerException.class, () -> FixedSizeList.fixedSizeList(null));
    }

    @Test
    void testAdd() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> fixedSizeList.add("a"));
    }

    @Test
    void testAdd_Index_Object() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> fixedSizeList.add(0, "a"));
    }

    @Test
    void testAddAll() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> fixedSizeList.addAll(Arrays.asList("a", "b")));
    }

    @Test
    void testAddAll_Index_Collection() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> fixedSizeList.addAll(0, Arrays.asList("a", "b")));
    }

    @Test
    void testClear() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> fixedSizeList.clear());
    }

    @Test
    void testGet() {
        // Given
        when(mockList.get(0)).thenReturn("a");

        // When
        String result = fixedSizeList.get(0);

        // Then
        assertEquals("a", result);
        verify(mockList, times(1)).get(0);
    }

    @Test
    void testIndexOf() {
        // Given
        when(mockList.indexOf("a")).thenReturn(0);

        // When
        int result = fixedSizeList.indexOf("a");

        // Then
        assertEquals(0, result);
        verify(mockList, times(1)).indexOf("a");
    }

    @Test
    void testIsFull() {
        // When
        boolean result = fixedSizeList.isFull();

        // Then
        assertTrue(result);
    }

    @Test
    void testIterator() {
        // Given
        Iterator<String> iterator = mock(Iterator.class);
        when(mockList.iterator()).thenReturn(iterator);

        // When
        Iterator<String> result = fixedSizeList.iterator();

        // Then
        assertNotSame(iterator, result);
        verify(mockList, times(1)).iterator();
    }

    @Test
    void testLastIndexOf() {
        // Given
        when(mockList.lastIndexOf("a")).thenReturn(0);

        // When
        int result = fixedSizeList.lastIndexOf("a");

        // Then
        assertEquals(0, result);
        verify(mockList, times(1)).lastIndexOf("a");
    }

    @Test
    void testListIterator() {
        // Given
        ListIterator<String> iterator = mock(ListIterator.class);
        when(mockList.listIterator()).thenReturn(iterator);

        // When
        ListIterator<String> result = fixedSizeList.listIterator();

        // Then
        assertNotSame(iterator, result);
        verify(mockList, times(1)).listIterator();
    }

    @Test
    void testListIterator_Index() {
        // Given
        ListIterator<String> iterator = mock(ListIterator.class);
        when(mockList.listIterator(0)).thenReturn(iterator);

        // When
        ListIterator<String> result = fixedSizeList.listIterator(0);

        // Then
        assertNotSame(iterator, result);
        verify(mockList, times(1)).listIterator(0);
    }

    @Test
    void testMaxSize() {
        // Given
        when(mockList.size()).thenReturn(10);

        // When
        int result = fixedSizeList.maxSize();

        // Then
        assertEquals(10, result);
        verify(mockList, times(1)).size();
    }

    @Test
    void testRemove() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> fixedSizeList.remove(0));
    }

    @Test
    void testRemove_Object() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> fixedSizeList.remove("a"));
    }

    @Test
    void testRemoveAll() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> fixedSizeList.removeAll(Arrays.asList("a", "b")));
    }

    @Test
    void testRemoveIf() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> fixedSizeList.removeIf(e -> true));
    }

    @Test
    void testRetainAll() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> fixedSizeList.retainAll(Arrays.asList("a", "b")));
    }

    @Test
    void testSet() {
        // Given
        when(mockList.set(0, "a")).thenReturn("b");

        // When
        String result = fixedSizeList.set(0, "a");

        // Then
        assertEquals("b", result);
        verify(mockList, times(1)).set(0, "a");
    }

    @Test
    void testSubList() {
        // Given
        List<String> subList = new ArrayList<>(Arrays.asList("a", "b"));
        when(mockList.subList(0, 2)).thenReturn(subList);

        // When
        List<String> result = fixedSizeList.subList(0, 2);

        // Then
        assertNotSame(subList, result);
        verify(mockList, times(1)).subList(0, 2);
    }
}
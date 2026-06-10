import net.hydromatic.morel.util.TailList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TailListTest {

    @Mock
    private List<String> mockList;

    private TailList<String> tailList;

    @BeforeEach
    void setup() {
        tailList = new TailList<>(mockList, 0);
    }

    @Test
    void testGet() {
        // Given
        String element = "Element";
        when(mockList.get(0)).thenReturn(element);

        // When
        String result = tailList.get(0);

        // Then
        assertEquals(element, result);
        verify(mockList, times(1)).get(0);
    }

    @Test
    void testSize() {
        // Given
        int size = 10;
        when(mockList.size()).thenReturn(size);

        // When
        int result = tailList.size();

        // Then
        assertEquals(size, result);
        verify(mockList, times(1)).size();
    }

    @Test
    void testClear() {
        // Given
        int size = 10;
        when(mockList.size()).thenReturn(size);

        // When
        tailList.clear();

        // Then
        verify(mockList, times(size)).remove(anyInt());
    }

    @Test
    void testAdd() {
        // Given
        String element = "Element";
        boolean result = true;
        when(mockList.add(element)).thenReturn(result);

        // When
        boolean added = tailList.add(element);

        // Then
        assertTrue(added);
        assertEquals(result, added);
        verify(mockList, times(1)).add(element);
    }

    @Test
    void testAddAtIndex() {
        // Given
        String element = "Element";
        int index = 5;

        // When
        tailList.add(index, element);

        // Then
        verify(mockList, times(1)).add(index, element);
    }

    @Test
    void testAddAllAtIndex() {
        // Given
        List<String> elements = new ArrayList<>();
        elements.add("Element1");
        elements.add("Element2");
        int index = 5;
        boolean result = true;
        when(mockList.addAll(index, elements)).thenReturn(result);

        // When
        boolean added = tailList.addAll(index, elements);

        // Then
        assertTrue(added);
        assertEquals(result, added);
        verify(mockList, times(1)).addAll(index, elements);
    }

    @Test
    void testAddAll() {
        // Given
        List<String> elements = new ArrayList<>();
        elements.add("Element1");
        elements.add("Element2");
        boolean result = true;
        when(mockList.addAll(elements)).thenReturn(result);

        // When
        boolean added = tailList.addAll(elements);

        // Then
        assertTrue(added);
        assertEquals(result, added);
        verify(mockList, times(1)).addAll(elements);
    }

    @Test
    void testRemove() {
        // Given
        String element = "Element";
        when(mockList.remove(0)).thenReturn(element);

        // When
        String removed = tailList.remove(0);

        // Then
        assertEquals(element, removed);
        verify(mockList, times(1)).remove(0);
    }

    @Test
    void testSet() {
        // Given
        String element = "Element";
        when(mockList.set(0, element)).thenReturn(element);

        // When
        String set = tailList.set(0, element);

        // Then
        assertEquals(element, set);
        verify(mockList, times(1)).set(0, element);
    }

    @Test
    void testConstructorWithStartIndex() {
        // Given
        List<String> list = new ArrayList<>();
        int startIndex = 5;

        // When
        TailList<String> tailList = new TailList<>(list, startIndex);

        // Then
        assertNotNull(tailList);
    }

    @Test
    void testConstructorWithoutStartIndex() {
        // Given
        List<String> list = new ArrayList<>();

        // When
        TailList<String> tailList = new TailList<>(list);

        // Then
        assertNotNull(tailList);
    }
}
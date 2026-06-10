import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.list.LazyList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LazyListTest {

    @Mock
    private Factory<String> factory;

    @Mock
    private Transformer<Integer, String> transformer;

    private List<String> list;

    @BeforeEach
    void setup() {
        list = new ArrayList<>();
        when(factory.create()).thenReturn("Mocked Object");
        when(transformer.apply(anyInt())).thenReturn("Mocked Object");
    }

    @Test
    void testLazyListFactory() {
        // Given
        LazyList<String> lazyList = LazyList.lazyList(list, factory);

        // When
        String result = lazyList.get(0);

        // Then
        assertNotNull(result);
        assertEquals("Mocked Object", result);
        verify(factory, times(1)).create();
    }

    @Test
    void testLazyListTransformer() {
        // Given
        LazyList<String> lazyList = LazyList.lazyList(list, transformer);

        // When
        String result = lazyList.get(0);

        // Then
        assertNotNull(result);
        assertEquals("Mocked Object", result);
        verify(transformer, times(1)).apply(0);
    }

    @Test
    void testGetIndexWithinBounds() {
        // Given
        list.add("Existing Object");
        LazyList<String> lazyList = LazyList.lazyList(list, factory);

        // When
        String result = lazyList.get(0);

        // Then
        assertNotNull(result);
        assertEquals("Existing Object", result);
        verify(factory, never()).create();
    }

    @Test
    void testGetIndexBeyondBounds() {
        // Given
        LazyList<String> lazyList = LazyList.lazyList(list, factory);

        // When
        String result = lazyList.get(5);

        // Then
        assertNotNull(result);
        assertEquals("Mocked Object", result);
        verify(factory, times(1)).create();
    }

    @Test
    void testGetIndexWithPlaceholder() {
        // Given
        list.add(null);
        LazyList<String> lazyList = LazyList.lazyList(list, factory);

        // When
        String result = lazyList.get(0);

        // Then
        assertNotNull(result);
        assertEquals("Mocked Object", result);
        verify(factory, times(1)).create();
    }

    @Test
    void testSubList() {
        // Given
        list.add("Object 1");
        list.add("Object 2");
        list.add("Object 3");
        LazyList<String> lazyList = LazyList.lazyList(list, factory);

        // When
        List<String> subList = lazyList.subList(1, 3);

        // Then
        assertNotNull(subList);
        assertEquals(2, subList.size());
        assertEquals("Object 2", subList.get(0));
        assertEquals("Object 3", subList.get(1));
    }
}
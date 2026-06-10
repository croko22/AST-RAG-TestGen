import net.hydromatic.morel.util.MapList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MapListTest {

    @Mock
    private IntFunction<String> intFunctionMock;

    private MapList<String> mapList;

    @BeforeEach
    void setup() {
        // Given: a function that returns a string based on the input index
        when(intFunctionMock.apply(anyInt())).thenAnswer(invocation -> "Element " + invocation.getArgument(0));

        // When: creating a MapList with the given function and size
        mapList = MapList.of(10, intFunctionMock);
    }

    @Test
    void testOf_Success() {
        // Then: the MapList is created successfully
        assertNotNull(mapList);
        assertEquals(10, mapList.size());
    }

    @Test
    void testGet_ElementExists() {
        // When: getting an element at a valid index
        String element = mapList.get(5);

        // Then: the element is returned correctly
        assertEquals("Element 5", element);
        verify(intFunctionMock, times(1)).apply(5);
    }

    @Test
    void testGet_ElementDoesNotExist() {
        // When: getting an element at an invalid index
        assertThrows(IndexOutOfBoundsException.class, () -> mapList.get(10));
        verify(intFunctionMock, never()).apply(anyInt());
    }

    @Test
    void testGet_NegativeIndex() {
        // When: getting an element at a negative index
        assertThrows(IndexOutOfBoundsException.class, () -> mapList.get(-1));
        verify(intFunctionMock, never()).apply(anyInt());
    }

    @Test
    void testSize() {
        // When: getting the size of the MapList
        int size = mapList.size();

        // Then: the size is returned correctly
        assertEquals(10, size);
    }
}
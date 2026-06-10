import org.apache.commons.collections4.IterableGet;
import org.apache.commons.collections4.MapIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IterableGetTest {

    @Mock
    private MapIterator<String, Integer> mapIteratorMock;

    @Mock
    private IterableGet<String, Integer> iterableGetMock;

    private Map<String, Integer> testMap;

    @BeforeEach
    void setup() {
        testMap = new HashMap<>();
        testMap.put("one", 1);
        testMap.put("two", 2);
        testMap.put("three", 3);
    }

    @Test
    void testMapIterator() {
        // Given: a mock implementation of IterableGet
        when(iterableGetMock.mapIterator()).thenReturn(mapIteratorMock);

        // When: mapIterator is called on the mock
        MapIterator<String, Integer> result = iterableGetMock.mapIterator();

        // Then: the result should be the same as the mock
        assertEquals(mapIteratorMock, result);

        // Verify: the mapIterator method was called once
        verify(iterableGetMock, times(1)).mapIterator();
    }

    @Test
    void testMapIterator_ReturnsNull() {
        // Given: a mock implementation of IterableGet that returns null
        when(iterableGetMock.mapIterator()).thenReturn(null);

        // When: mapIterator is called on the mock
        MapIterator<String, Integer> result = iterableGetMock.mapIterator();

        // Then: the result should be null
        assertNull(result);

        // Verify: the mapIterator method was called once
        verify(iterableGetMock, times(1)).mapIterator();
    }

    @Test
    void testMapIterator_ThrowsException() {
        // Given: a mock implementation of IterableGet that throws an exception
        when(iterableGetMock.mapIterator()).thenThrow(new RuntimeException("Test exception"));

        // When/Then: calling mapIterator should throw an exception
        assertThrows(RuntimeException.class, () -> iterableGetMock.mapIterator());

        // Verify: the mapIterator method was called once
        verify(iterableGetMock, times(1)).mapIterator();
    }
}
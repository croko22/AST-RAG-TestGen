import org.apache.commons.collections4.BoundedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoundedMapTest {

    @Mock
    private BoundedMap<String, String> boundedMap;

    @BeforeEach
    void setup() {
        // Initialize the mock object if needed
    }

    @Test
    public void testIsFull_ReturnsTrue() {
        // Given: the map is full
        when(boundedMap.isFull()).thenReturn(true);

        // When: the method is called
        boolean result = boundedMap.isFull();

        // Then: the result is true
        assertTrue(result);
        verify(boundedMap, times(1)).isFull();
    }

    @Test
    public void testIsFull_ReturnsFalse() {
        // Given: the map is not full
        when(boundedMap.isFull()).thenReturn(false);

        // When: the method is called
        boolean result = boundedMap.isFull();

        // Then: the result is false
        assertFalse(result);
        verify(boundedMap, times(1)).isFull();
    }

    @Test
    public void testMaxSize_ReturnsPositiveValue() {
        // Given: the maximum size is a positive value
        int maxSize = 10;
        when(boundedMap.maxSize()).thenReturn(maxSize);

        // When: the method is called
        int result = boundedMap.maxSize();

        // Then: the result is the expected value
        assertEquals(maxSize, result);
        verify(boundedMap, times(1)).maxSize();
    }

    @Test
    public void testMaxSize_ReturnsZero() {
        // Given: the maximum size is zero
        int maxSize = 0;
        when(boundedMap.maxSize()).thenReturn(maxSize);

        // When: the method is called
        int result = boundedMap.maxSize();

        // Then: the result is zero
        assertEquals(maxSize, result);
        verify(boundedMap, times(1)).maxSize();
    }

    @Test
    public void testMaxSize_ReturnsNegativeValue() {
        // Given: the maximum size is a negative value
        int maxSize = -1;
        when(boundedMap.maxSize()).thenReturn(maxSize);

        // When: the method is called
        int result = boundedMap.maxSize();

        // Then: the result is the expected value
        assertEquals(maxSize, result);
        verify(boundedMap, times(1)).maxSize();
    }
}
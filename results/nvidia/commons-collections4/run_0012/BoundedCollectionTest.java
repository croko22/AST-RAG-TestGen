import org.apache.commons.collections4.BoundedCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoundedCollectionTest {

    @Mock
    private BoundedCollection<String> boundedCollection;

    @BeforeEach
    void setup() {
        // Initialize the mock object
        boundedCollection = mock(BoundedCollection.class);
    }

    @Test
    public void testIsFull_True() {
        // Given: the collection is full
        when(boundedCollection.isFull()).thenReturn(true);

        // When: check if the collection is full
        boolean result = boundedCollection.isFull();

        // Then: verify the result
        assertTrue(result);
        verify(boundedCollection, times(1)).isFull();
    }

    @Test
    public void testIsFull_False() {
        // Given: the collection is not full
        when(boundedCollection.isFull()).thenReturn(false);

        // When: check if the collection is full
        boolean result = boundedCollection.isFull();

        // Then: verify the result
        assertFalse(result);
        verify(boundedCollection, times(1)).isFull();
    }

    @Test
    public void testMaxSize() {
        // Given: the maximum size of the collection
        int maxSize = 10;
        when(boundedCollection.maxSize()).thenReturn(maxSize);

        // When: get the maximum size of the collection
        int result = boundedCollection.maxSize();

        // Then: verify the result
        assertEquals(maxSize, result);
        verify(boundedCollection, times(1)).maxSize();
    }

    @Test
    public void testMaxSize_Zero() {
        // Given: the maximum size of the collection is zero
        int maxSize = 0;
        when(boundedCollection.maxSize()).thenReturn(maxSize);

        // When: get the maximum size of the collection
        int result = boundedCollection.maxSize();

        // Then: verify the result
        assertEquals(maxSize, result);
        verify(boundedCollection, times(1)).maxSize();
    }

    @Test
    public void testMaxSize_Negative() {
        // Given: the maximum size of the collection is negative
        int maxSize = -1;
        when(boundedCollection.maxSize()).thenReturn(maxSize);

        // When: get the maximum size of the collection
        int result = boundedCollection.maxSize();

        // Then: verify the result
        assertEquals(maxSize, result);
        verify(boundedCollection, times(1)).maxSize();
    }
}
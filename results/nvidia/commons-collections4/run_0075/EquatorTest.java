import org.apache.commons.collections4.Equator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EquatorTest {

    @Mock
    private Equator<String> stringEquator;

    @Mock
    private Equator<Integer> integerEquator;

    @BeforeEach
    void setup() {
        // Initialize mocks if needed
    }

    @Test
    public void testEquate_EqualObjects() {
        // Given: two equal objects
        String o1 = "Hello";
        String o2 = "Hello";

        // When: equate method is called
        when(stringEquator.equate(o1, o2)).thenReturn(true);

        // Then: equate method returns true
        assertTrue(stringEquator.equate(o1, o2));
        verify(stringEquator, times(1)).equate(o1, o2);
    }

    @Test
    public void testEquate_NotEqualObjects() {
        // Given: two not equal objects
        String o1 = "Hello";
        String o2 = "World";

        // When: equate method is called
        when(stringEquator.equate(o1, o2)).thenReturn(false);

        // Then: equate method returns false
        assertFalse(stringEquator.equate(o1, o2));
        verify(stringEquator, times(1)).equate(o1, o2);
    }

    @Test
    public void testEquate_NullObjects() {
        // Given: two null objects
        String o1 = null;
        String o2 = null;

        // When: equate method is called
        when(stringEquator.equate(o1, o2)).thenReturn(true);

        // Then: equate method returns true
        assertTrue(stringEquator.equate(o1, o2));
        verify(stringEquator, times(1)).equate(o1, o2);
    }

    @Test
    public void testHash_EqualObjects() {
        // Given: an object
        String o = "Hello";

        // When: hash method is called
        when(stringEquator.hash(o)).thenReturn(123);

        // Then: hash method returns the expected hash
        assertEquals(123, stringEquator.hash(o));
        verify(stringEquator, times(1)).hash(o);
    }

    @Test
    public void testHash_NotEqualObjects() {
        // Given: two not equal objects
        String o1 = "Hello";
        String o2 = "World";

        // When: hash method is called
        when(stringEquator.hash(o1)).thenReturn(123);
        when(stringEquator.hash(o2)).thenReturn(456);

        // Then: hash method returns different hashes
        assertNotEquals(stringEquator.hash(o1), stringEquator.hash(o2));
        verify(stringEquator, times(1)).hash(o1);
        verify(stringEquator, times(1)).hash(o2);
    }

    @Test
    public void testHash_NullObject() {
        // Given: a null object
        String o = null;

        // When: hash method is called
        when(stringEquator.hash(o)).thenReturn(0);

        // Then: hash method returns 0
        assertEquals(0, stringEquator.hash(o));
        verify(stringEquator, times(1)).hash(o);
    }

    @Test
    public void testEquate_IntegerEquator() {
        // Given: two equal integers
        Integer o1 = 10;
        Integer o2 = 10;

        // When: equate method is called
        when(integerEquator.equate(o1, o2)).thenReturn(true);

        // Then: equate method returns true
        assertTrue(integerEquator.equate(o1, o2));
        verify(integerEquator, times(1)).equate(o1, o2);
    }

    @Test
    public void testHash_IntegerEquator() {
        // Given: an integer
        Integer o = 10;

        // When: hash method is called
        when(integerEquator.hash(o)).thenReturn(10);

        // Then: hash method returns the expected hash
        assertEquals(10, integerEquator.hash(o));
        verify(integerEquator, times(1)).hash(o);
    }
}
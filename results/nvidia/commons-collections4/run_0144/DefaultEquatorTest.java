import org.apache.commons.collections4.functors.DefaultEquator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DefaultEquatorTest {

    @Test
    public void testDefaultEquator() {
        // Given: no specific setup needed
        // When: default equator is retrieved
        DefaultEquator<Object> equator = DefaultEquator.defaultEquator();
        // Then: verify the equator instance
        assertNotNull(equator);
    }

    @Test
    public void testEquate_Null_Null() {
        // Given: two null objects
        DefaultEquator<Object> equator = DefaultEquator.defaultEquator();
        // When: equate is called with two null objects
        boolean result = equator.equate(null, null);
        // Then: verify the result
        assertTrue(result);
    }

    @Test
    public void testEquate_Null_NonNull() {
        // Given: one null and one non-null object
        DefaultEquator<Object> equator = DefaultEquator.defaultEquator();
        Object nonNullObject = new Object();
        // When: equate is called with one null and one non-null object
        boolean result = equator.equate(null, nonNullObject);
        // Then: verify the result
        assertFalse(result);
    }

    @Test
    public void testEquate_NonNull_Null() {
        // Given: one non-null and one null object
        DefaultEquator<Object> equator = DefaultEquator.defaultEquator();
        Object nonNullObject = new Object();
        // When: equate is called with one non-null and one null object
        boolean result = equator.equate(nonNullObject, null);
        // Then: verify the result
        assertFalse(result);
    }

    @Test
    public void testEquate_NonNull_NonNull_SameObject() {
        // Given: two non-null objects that are the same
        DefaultEquator<Object> equator = DefaultEquator.defaultEquator();
        Object nonNullObject = new Object();
        // When: equate is called with two non-null objects that are the same
        boolean result = equator.equate(nonNullObject, nonNullObject);
        // Then: verify the result
        assertTrue(result);
    }

    @Test
    public void testEquate_NonNull_NonNull_DifferentObjects() {
        // Given: two non-null objects that are different
        DefaultEquator<Object> equator = DefaultEquator.defaultEquator();
        Object nonNullObject1 = new Object();
        Object nonNullObject2 = new Object();
        // When: equate is called with two non-null objects that are different
        boolean result = equator.equate(nonNullObject1, nonNullObject2);
        // Then: verify the result
        assertFalse(result);
    }

    @Test
    public void testHash_Null() {
        // Given: a null object
        DefaultEquator<Object> equator = DefaultEquator.defaultEquator();
        // When: hash is called with a null object
        int result = equator.hash(null);
        // Then: verify the result
        assertEquals(DefaultEquator.HASHCODE_NULL, result);
    }

    @Test
    public void testHash_NonNull() {
        // Given: a non-null object
        DefaultEquator<Object> equator = DefaultEquator.defaultEquator();
        Object nonNullObject = new Object();
        // When: hash is called with a non-null object
        int result = equator.hash(nonNullObject);
        // Then: verify the result
        assertEquals(nonNullObject.hashCode(), result);
    }
}
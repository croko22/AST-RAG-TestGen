import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.CloneTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CloneTransformerTest {

    @Mock
    private Object mockObject;

    private CloneTransformer<Object> cloneTransformer;

    @BeforeEach
    public void setup() {
        cloneTransformer = CloneTransformer.cloneTransformer();
    }

    @Test
    public void testCloneTransformer() {
        // Given: no specific setup needed
        // When: cloneTransformer is called
        Transformer<Object, Object> transformer = CloneTransformer.cloneTransformer();
        // Then: verify the transformer is not null
        assertNotNull(transformer);
    }

    @Test
    public void testTransform_NullInput() {
        // Given: null input
        Object input = null;
        // When: transform is called
        Object result = cloneTransformer.transform(input);
        // Then: verify the result is null
        assertNull(result);
    }

    @Test
    public void testTransform_NonNullInput() {
        // Given: non-null input
        when(mockObject.toString()).thenReturn("Mock Object");
        // When: transform is called
        Object result = cloneTransformer.transform(mockObject);
        // Then: verify the result is not null and is a clone of the input
        assertNotNull(result);
        assertNotSame(mockObject, result);
        assertEquals(mockObject.toString(), result.toString());
    }

    @Test
    public void testTransform_MultipleCalls() {
        // Given: multiple calls to transform with the same input
        when(mockObject.toString()).thenReturn("Mock Object");
        // When: transform is called multiple times
        Object result1 = cloneTransformer.transform(mockObject);
        Object result2 = cloneTransformer.transform(mockObject);
        // Then: verify the results are not null and are clones of the input
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(mockObject, result1);
        assertNotSame(mockObject, result2);
        assertNotSame(result1, result2);
        assertEquals(mockObject.toString(), result1.toString());
        assertEquals(mockObject.toString(), result2.toString());
    }
}
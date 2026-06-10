import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class NOPTransformerTest {

    @Test
    public void testNOPTransformer() {
        // Given: None
        // When: Create a new instance of NOPTransformer
        NOPTransformer<String> nopTransformer = new NOPTransformer<>();
        // Then: Verify the instance is created successfully
        assertNotNull(nopTransformer);
    }

    @Test
    public void testNopTransformerFactory() {
        // Given: None
        // When: Get the singleton instance using the factory method
        Transformer<String, String> instance1 = NOPTransformer.nopTransformer();
        Transformer<String, String> instance2 = NOPTransformer.nopTransformer();
        // Then: Verify both instances are the same
        assertSame(instance1, instance2);
    }

    @Test
    public void testTransform() {
        // Given: An input object
        String input = "Test Input";
        // When: Transform the input using the NOPTransformer
        Transformer<String, String> nopTransformer = NOPTransformer.nopTransformer();
        String result = nopTransformer.transform(input);
        // Then: Verify the result is the same as the input
        assertEquals(input, result);
    }

    @Test
    public void testTransformNullInput() {
        // Given: A null input
        String input = null;
        // When: Transform the input using the NOPTransformer
        Transformer<String, String> nopTransformer = NOPTransformer.nopTransformer();
        String result = nopTransformer.transform(input);
        // Then: Verify the result is null
        assertNull(result);
    }
}
import org.apache.commons.collections4.functors.StringValueTransformer;
import org.apache.commons.collections4.Transformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StringValueTransformerTest {

    private StringValueTransformer<Object> transformer;

    @BeforeEach
    public void setup() {
        transformer = StringValueTransformer.stringValueTransformer();
    }

    @Test
    public void testStringValueTransformer() {
        // Given: a transformer instance
        Transformer<Object, String> instance1 = StringValueTransformer.stringValueTransformer();
        Transformer<Object, String> instance2 = StringValueTransformer.stringValueTransformer();

        // Then: both instances should be the same (singleton pattern)
        assertSame(instance1, instance2);
    }

    @Test
    public void testTransform_NullInput() {
        // Given: a null input
        Object input = null;

        // When: transforming the input
        String result = transformer.transform(input);

        // Then: the result should be "null"
        assertEquals("null", result);
    }

    @Test
    public void testTransform_StringInput() {
        // Given: a string input
        Object input = "Hello, World!";

        // When: transforming the input
        String result = transformer.transform(input);

        // Then: the result should be the same as the input
        assertEquals("Hello, World!", result);
    }

    @Test
    public void testTransform_IntegerInput() {
        // Given: an integer input
        Object input = 123;

        // When: transforming the input
        String result = transformer.transform(input);

        // Then: the result should be the string representation of the input
        assertEquals("123", result);
    }

    @Test
    public void testTransform_CustomObjectInput() {
        // Given: a custom object input
        Object input = new Object() {
            @Override
            public String toString() {
                return "Custom Object";
            }
        };

        // When: transforming the input
        String result = transformer.transform(input);

        // Then: the result should be the string representation of the input
        assertEquals("Custom Object", result);
    }
}
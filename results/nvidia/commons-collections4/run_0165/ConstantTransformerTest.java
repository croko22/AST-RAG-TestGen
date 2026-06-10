import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConstantTransformerTest {

    private ConstantTransformer<String, String> constantTransformer;
    private ConstantTransformer<String, String> nullTransformer;

    @BeforeEach
    void setup() {
        constantTransformer = (ConstantTransformer<String, String>) ConstantTransformer.constantTransformer("constant");
        nullTransformer = (ConstantTransformer<String, String>) ConstantTransformer.nullTransformer();
    }

    @Test
    public void testConstantTransformer() {
        // Given: a constant to return
        String constantToReturn = "constant";

        // When: creating a constant transformer
        Transformer<String, String> transformer = ConstantTransformer.constantTransformer(constantToReturn);

        // Then: the transformer returns the constant
        assertEquals(constantToReturn, transformer.transform("input"));
    }

    @Test
    public void testNullTransformer() {
        // Given: no constant to return

        // When: creating a null transformer
        Transformer<String, String> transformer = ConstantTransformer.nullTransformer();

        // Then: the transformer returns null
        assertNull(transformer.transform("input"));
    }

    @Test
    public void testEquals() {
        // Given: two constant transformers with the same constant
        ConstantTransformer<String, String> transformer1 = (ConstantTransformer<String, String>) ConstantTransformer.constantTransformer("constant");
        ConstantTransformer<String, String> transformer2 = (ConstantTransformer<String, String>) ConstantTransformer.constantTransformer("constant");

        // When: checking if the transformers are equal
        boolean equals = transformer1.equals(transformer2);

        // Then: the transformers are equal
        assertTrue(equals);
    }

    @Test
    public void testEquals_DifferentConstants() {
        // Given: two constant transformers with different constants
        ConstantTransformer<String, String> transformer1 = (ConstantTransformer<String, String>) ConstantTransformer.constantTransformer("constant1");
        ConstantTransformer<String, String> transformer2 = (ConstantTransformer<String, String>) ConstantTransformer.constantTransformer("constant2");

        // When: checking if the transformers are equal
        boolean equals = transformer1.equals(transformer2);

        // Then: the transformers are not equal
        assertFalse(equals);
    }

    @Test
    public void testEquals_Null() {
        // Given: a constant transformer and null
        ConstantTransformer<String, String> transformer = (ConstantTransformer<String, String>) ConstantTransformer.constantTransformer("constant");

        // When: checking if the transformer is equal to null
        boolean equals = transformer.equals(null);

        // Then: the transformer is not equal to null
        assertFalse(equals);
    }

    @Test
    public void testGetConstant() {
        // Given: a constant transformer
        ConstantTransformer<String, String> transformer = (ConstantTransformer<String, String>) ConstantTransformer.constantTransformer("constant");

        // When: getting the constant
        String constant = transformer.getConstant();

        // Then: the constant is returned
        assertEquals("constant", constant);
    }

    @Test
    public void testHashCode() {
        // Given: a constant transformer
        ConstantTransformer<String, String> transformer = (ConstantTransformer<String, String>) ConstantTransformer.constantTransformer("constant");

        // When: getting the hash code
        int hashCode = transformer.hashCode();

        // Then: the hash code is not zero
        assertNotEquals(0, hashCode);
    }

    @Test
    public void testTransform() {
        // Given: a constant transformer
        ConstantTransformer<String, String> transformer = (ConstantTransformer<String, String>) ConstantTransformer.constantTransformer("constant");

        // When: transforming an input
        String result = transformer.transform("input");

        // Then: the constant is returned
        assertEquals("constant", result);
    }
}
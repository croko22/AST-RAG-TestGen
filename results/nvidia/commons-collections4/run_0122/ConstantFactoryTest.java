import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.functors.ConstantFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConstantFactoryTest {

    private ConstantFactory<String> constantFactory;
    private ConstantFactory<String> nullConstantFactory;

    @BeforeEach
    public void setup() {
        constantFactory = ConstantFactory.constantFactory("constantValue");
        nullConstantFactory = ConstantFactory.constantFactory(null);
    }

    @Test
    public void testConstantFactory_NotNull() {
        // Given: a non-null constant value
        String constantValue = "constantValue";

        // When: creating a constant factory with the given value
        ConstantFactory<String> factory = ConstantFactory.constantFactory(constantValue);

        // Then: the created factory should return the constant value
        assertEquals(constantValue, factory.create());
    }

    @Test
    public void testConstantFactory_Null() {
        // Given: a null constant value

        // When: creating a constant factory with the null value
        ConstantFactory<String> factory = ConstantFactory.constantFactory(null);

        // Then: the created factory should return null
        assertNull(factory.create());
    }

    @Test
    public void testCreate() {
        // Given: a constant factory with a non-null constant value

        // When: calling the create method on the factory
        String result = constantFactory.create();

        // Then: the result should be the constant value
        assertEquals("constantValue", result);
    }

    @Test
    public void testCreate_NullConstant() {
        // Given: a constant factory with a null constant value

        // When: calling the create method on the factory
        String result = nullConstantFactory.create();

        // Then: the result should be null
        assertNull(result);
    }

    @Test
    public void testGetConstant() {
        // Given: a constant factory with a non-null constant value

        // When: calling the getConstant method on the factory
        String result = constantFactory.getConstant();

        // Then: the result should be the constant value
        assertEquals("constantValue", result);
    }

    @Test
    public void testGetConstant_NullConstant() {
        // Given: a constant factory with a null constant value

        // When: calling the getConstant method on the factory
        String result = nullConstantFactory.getConstant();

        // Then: the result should be null
        assertNull(result);
    }
}
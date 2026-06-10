import org.apache.commons.collections4.properties.OrderedProperties;
import org.apache.commons.collections4.properties.OrderedPropertiesFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrderedPropertiesFactoryTest {

    private OrderedPropertiesFactory orderedPropertiesFactory;

    @BeforeEach
    public void setup() {
        orderedPropertiesFactory = OrderedPropertiesFactory.INSTANCE;
    }

    @Test
    public void testCreateProperties() {
        // When: createProperties method is called
        OrderedProperties orderedProperties = orderedPropertiesFactory.createProperties();

        // Then: verify that the created instance is not null
        assertNotNull(orderedProperties);

        // Then: verify that the created instance is of type OrderedProperties
        assertTrue(orderedProperties instanceof OrderedProperties);
    }

    @Test
    public void testGetInstance() {
        // When: INSTANCE field is accessed
        OrderedPropertiesFactory instance1 = OrderedPropertiesFactory.INSTANCE;
        OrderedPropertiesFactory instance2 = OrderedPropertiesFactory.INSTANCE;

        // Then: verify that the instances are the same
        assertSame(instance1, instance2);
    }
}
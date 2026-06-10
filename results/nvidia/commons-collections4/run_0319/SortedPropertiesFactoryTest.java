import org.apache.commons.collections4.properties.SortedProperties;
import org.apache.commons.collections4.properties.SortedPropertiesFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SortedPropertiesFactoryTest {

    private SortedPropertiesFactory sortedPropertiesFactory;

    @BeforeEach
    public void setup() {
        sortedPropertiesFactory = SortedPropertiesFactory.INSTANCE;
    }

    @Test
    public void testCreateProperties_InstanceCreation() {
        // When: createProperties method is called
        SortedProperties sortedProperties = sortedPropertiesFactory.createProperties();

        // Then: verify that a new instance of SortedProperties is created
        assertNotNull(sortedProperties);
        assertTrue(sortedProperties instanceof SortedProperties);
    }

    @Test
    public void testCreateProperties_SingletonInstance() {
        // Given: the singleton instance is obtained
        SortedPropertiesFactory instance1 = SortedPropertiesFactory.INSTANCE;
        SortedPropertiesFactory instance2 = SortedPropertiesFactory.INSTANCE;

        // Then: verify that both instances are the same
        assertSame(instance1, instance2);
    }

    @Test
    public void testCreateProperties_MultipleCalls() {
        // When: createProperties method is called multiple times
        SortedProperties sortedProperties1 = sortedPropertiesFactory.createProperties();
        SortedProperties sortedProperties2 = sortedPropertiesFactory.createProperties();

        // Then: verify that different instances are created
        assertNotNull(sortedProperties1);
        assertNotNull(sortedProperties2);
        assertNotSame(sortedProperties1, sortedProperties2);
    }
}
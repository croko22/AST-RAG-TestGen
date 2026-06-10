import org.ice4j.attribute.LifetimeAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Objects;

public class LifetimeAttributeTest {

    private LifetimeAttribute lifetimeAttribute;

    @BeforeEach
    public void setup() {
        lifetimeAttribute = new LifetimeAttribute();
    }

    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        // Given: same object
        LifetimeAttribute sameObject = lifetimeAttribute;

        // When: equals method is called
        boolean result = lifetimeAttribute.equals(sameObject);

        // Then: returns true
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_SameAttributes_ReturnsTrue() {
        // Given: different object with same attributes
        LifetimeAttribute differentObject = new LifetimeAttribute();
        differentObject.setLifetime(lifetimeAttribute.getLifetime());

        // When: equals method is called
        boolean result = lifetimeAttribute.equals(differentObject);

        // Then: returns true
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_DifferentAttributes_ReturnsFalse() {
        // Given: different object with different attributes
        LifetimeAttribute differentObject = new LifetimeAttribute();
        differentObject.setLifetime(lifetimeAttribute.getLifetime() + 1);

        // When: equals method is called
        boolean result = lifetimeAttribute.equals(differentObject);

        // Then: returns false
        assertFalse(result);
    }

    @Test
    public void testEquals_NullObject_ReturnsFalse() {
        // Given: null object
        Object nullObject = null;

        // When: equals method is called
        boolean result = lifetimeAttribute.equals(nullObject);

        // Then: returns false
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        // Given: different class object
        Object differentClass = new Object();

        // When: equals method is called
        boolean result = lifetimeAttribute.equals(differentClass);

        // Then: returns false
        assertFalse(result);
    }

    @Test
    public void testGetName_ReturnsCorrectName() {
        // Given: LifetimeAttribute object

        // When: getName method is called
        String result = lifetimeAttribute.getName();

        // Then: returns correct name
        assertEquals("LIFETIME", result);
    }

    @Test
    public void testGetDataLength_ReturnsCorrectLength() {
        // Given: LifetimeAttribute object

        // When: getDataLength method is called
        char result = lifetimeAttribute.getDataLength();

        // Then: returns correct length
        assertEquals(4, result);
    }

    @Test
    public void testSetLifetime_SetsCorrectLifetime() {
        // Given: LifetimeAttribute object and new lifetime value
        int newLifetime = 100;

        // When: setLifetime method is called
        lifetimeAttribute.setLifetime(newLifetime);

        // Then: lifetime is set correctly
        assertEquals(newLifetime, lifetimeAttribute.getLifetime());
    }

    @Test
    public void testGetLifetime_ReturnsCorrectLifetime() {
        // Given: LifetimeAttribute object and lifetime value
        int lifetime = 100;
        lifetimeAttribute.setLifetime(lifetime);

        // When: getLifetime method is called
        int result = lifetimeAttribute.getLifetime();

        // Then: returns correct lifetime
        assertEquals(lifetime, result);
    }
}
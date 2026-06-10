import org.ice4j.attribute.Attribute;
import org.ice4j.attribute.RealmAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

public class RealmAttributeTest {

    private RealmAttribute realmAttribute;

    @BeforeEach
    public void setup() {
        realmAttribute = new RealmAttribute();
    }

    @Test
    public void testGetName() {
        // Given: RealmAttribute instance
        // When: getName method is called
        String name = realmAttribute.getName();
        // Then: name should be "REALM"
        assertEquals("REALM", name);
    }

    @Test
    public void testGetDataLength_WithNullRealm() {
        // Given: RealmAttribute instance with null realm
        // When: getDataLength method is called
        char length = realmAttribute.getDataLength();
        // Then: length should be 0
        assertEquals(0, length);
    }

    @Test
    public void testGetDataLength_WithRealm() {
        // Given: RealmAttribute instance with realm
        byte[] realm = "example".getBytes();
        realmAttribute.setRealm(realm);
        // When: getDataLength method is called
        char length = realmAttribute.getDataLength();
        // Then: length should be equal to realm length
        assertEquals(realm.length, length);
    }

    @Test
    public void testSetRealm_Null() {
        // Given: RealmAttribute instance
        // When: setRealm method is called with null
        realmAttribute.setRealm(null);
        // Then: realm should be null
        assertNull(realmAttribute.getRealm());
    }

    @Test
    public void testSetRealm_WithValue() {
        // Given: RealmAttribute instance
        byte[] realm = "example".getBytes();
        // When: setRealm method is called with value
        realmAttribute.setRealm(realm);
        // Then: realm should be equal to value
        assertArrayEquals(realm, realmAttribute.getRealm());
    }

    @Test
    public void testEquals_SameInstance() {
        // Given: RealmAttribute instance
        // When: equals method is called with same instance
        boolean result = realmAttribute.equals(realmAttribute);
        // Then: result should be true
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentInstance_SameValues() {
        // Given: Two RealmAttribute instances with same values
        RealmAttribute other = new RealmAttribute();
        byte[] realm = "example".getBytes();
        realmAttribute.setRealm(realm);
        other.setRealm(realm);
        // When: equals method is called
        boolean result = realmAttribute.equals(other);
        // Then: result should be true
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentInstance_DifferentValues() {
        // Given: Two RealmAttribute instances with different values
        RealmAttribute other = new RealmAttribute();
        byte[] realm1 = "example1".getBytes();
        byte[] realm2 = "example2".getBytes();
        realmAttribute.setRealm(realm1);
        other.setRealm(realm2);
        // When: equals method is called
        boolean result = realmAttribute.equals(other);
        // Then: result should be false
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentClass() {
        // Given: RealmAttribute instance and instance of different class
        Object other = new Object();
        // When: equals method is called
        boolean result = realmAttribute.equals(other);
        // Then: result should be false
        assertFalse(result);
    }
}
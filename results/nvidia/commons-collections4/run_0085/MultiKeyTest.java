import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MultiKeyTest {

    private MultiKey<String> multiKey;

    @BeforeEach
    public void setup() {
        multiKey = new MultiKey<>("key1", "key2");
    }

    @Test
    public void testEquals_SameInstance_ReturnsTrue() {
        assertTrue(multiKey.equals(multiKey));
    }

    @Test
    public void testEquals_DifferentInstanceSameKeys_ReturnsTrue() {
        MultiKey<String> otherMultiKey = new MultiKey<>("key1", "key2");
        assertTrue(multiKey.equals(otherMultiKey));
    }

    @Test
    public void testEquals_DifferentInstanceDifferentKeys_ReturnsFalse() {
        MultiKey<String> otherMultiKey = new MultiKey<>("key3", "key4");
        assertFalse(multiKey.equals(otherMultiKey));
    }

    @Test
    public void testEquals_Null_ReturnsFalse() {
        assertFalse(multiKey.equals(null));
    }

    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        assertFalse(multiKey.equals("string"));
    }

    @Test
    public void testGetKey_ValidIndex_ReturnsKey() {
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
    }

    @Test
    public void testGetKey_InvalidIndex_ThrowsIndexOutOfBoundsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> multiKey.getKey(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> multiKey.getKey(2));
    }

    @Test
    public void testHashCode_ConsistentHashCodes_ReturnsSameHashCode() {
        MultiKey<String> otherMultiKey = new MultiKey<>("key1", "key2");
        assertEquals(multiKey.hashCode(), otherMultiKey.hashCode());
    }

    @Test
    public void testSize_ReturnsSizeOfKeysArray() {
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testToString_ReturnsStringRepresentation() {
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }

    @Test
    public void testMultiKey_ConstructorWithTwoKeys_CreatesMultiKey() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals(2, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
    }

    @Test
    public void testMultiKey_ConstructorWithThreeKeys_CreatesMultiKey() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertEquals(3, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }

    @Test
    public void testMultiKey_ConstructorWithFourKeys_CreatesMultiKey() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        assertEquals(4, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("key4", multiKey.getKey(3));
    }

    @Test
    public void testMultiKey_ConstructorWithFiveKeys_CreatesMultiKey() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        assertEquals(5, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("key4", multiKey.getKey(3));
        assertEquals("key5", multiKey.getKey(4));
    }

    @Test
    public void testMultiKey_ConstructorWithArray_CreatesMultiKey() {
        String[] keys = {"key1", "key2"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertEquals(2, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
    }

    @Test
    public void testMultiKey_ConstructorWithArrayAndMakeClone_CreatesMultiKey() {
        String[] keys = {"key1", "key2"};
        MultiKey<String> multiKey = new MultiKey<>(keys, true);
        assertEquals(2, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
    }
}
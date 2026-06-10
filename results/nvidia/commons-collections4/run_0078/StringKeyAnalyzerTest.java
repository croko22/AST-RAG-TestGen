import org.apache.commons.collections4.trie.analyzer.StringKeyAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StringKeyAnalyzerTest {

    private StringKeyAnalyzer analyzer;

    @BeforeEach
    public void setup() {
        analyzer = StringKeyAnalyzer.INSTANCE;
    }

    @Test
    public void testBitsPerElement() {
        // Given: StringKeyAnalyzer instance
        // When: bitsPerElement method is called
        int bitsPerElement = analyzer.bitsPerElement();
        // Then: verify the result
        assertEquals(16, bitsPerElement);
    }

    @Test
    public void testLengthInBits_NullKey() {
        // Given: null key
        String key = null;
        // When: lengthInBits method is called
        int lengthInBits = analyzer.lengthInBits(key);
        // Then: verify the result
        assertEquals(0, lengthInBits);
    }

    @Test
    public void testLengthInBits_NonNullKey() {
        // Given: non-null key
        String key = "Hello";
        // When: lengthInBits method is called
        int lengthInBits = analyzer.lengthInBits(key);
        // Then: verify the result
        assertEquals(key.length() * 16, lengthInBits);
    }

    @Test
    public void testIsBitSet_NullKey() {
        // Given: null key
        String key = null;
        int bitIndex = 0;
        int lengthInBits = 16;
        // When: isBitSet method is called
        boolean isBitSet = analyzer.isBitSet(key, bitIndex, lengthInBits);
        // Then: verify the result
        assertFalse(isBitSet);
    }

    @Test
    public void testIsBitSet_BitIndexOutOfRange() {
        // Given: key and bit index out of range
        String key = "Hello";
        int bitIndex = key.length() * 16;
        int lengthInBits = key.length() * 16;
        // When: isBitSet method is called
        boolean isBitSet = analyzer.isBitSet(key, bitIndex, lengthInBits);
        // Then: verify the result
        assertFalse(isBitSet);
    }

    @Test
    public void testIsBitSet_BitSet() {
        // Given: key and bit index
        String key = "Hello";
        int bitIndex = 0;
        int lengthInBits = key.length() * 16;
        // When: isBitSet method is called
        boolean isBitSet = analyzer.isBitSet(key, bitIndex, lengthInBits);
        // Then: verify the result
        assertTrue(isBitSet);
    }

    @Test
    public void testIsPrefix_NullPrefix() {
        // Given: null prefix
        String prefix = null;
        int offsetInBits = 0;
        int lengthInBits = 16;
        String key = "Hello";
        // When: isPrefix method is called
        assertThrows(NullPointerException.class, () -> analyzer.isPrefix(prefix, offsetInBits, lengthInBits, key));
    }

    @Test
    public void testIsPrefix_NullKey() {
        // Given: null key
        String prefix = "He";
        int offsetInBits = 0;
        int lengthInBits = 16;
        String key = null;
        // When: isPrefix method is called
        assertThrows(NullPointerException.class, () -> analyzer.isPrefix(prefix, offsetInBits, lengthInBits, key));
    }

    @Test
    public void testIsPrefix_PrefixLengthNotMultipleOfCharacterSize() {
        // Given: prefix length not multiple of character size
        String prefix = "He";
        int offsetInBits = 1;
        int lengthInBits = 16;
        String key = "Hello";
        // When: isPrefix method is called
        assertThrows(IllegalArgumentException.class, () -> analyzer.isPrefix(prefix, offsetInBits, lengthInBits, key));
    }

    @Test
    public void testIsPrefix_PrefixIsPrefixOfKey() {
        // Given: prefix is prefix of key
        String prefix = "He";
        int offsetInBits = 0;
        int lengthInBits = 16;
        String key = "Hello";
        // When: isPrefix method is called
        boolean isPrefix = analyzer.isPrefix(prefix, offsetInBits, lengthInBits, key);
        // Then: verify the result
        assertTrue(isPrefix);
    }

    @Test
    public void testIsPrefix_PrefixIsNotPrefixOfKey() {
        // Given: prefix is not prefix of key
        String prefix = "Hi";
        int offsetInBits = 0;
        int lengthInBits = 16;
        String key = "Hello";
        // When: isPrefix method is called
        boolean isPrefix = analyzer.isPrefix(prefix, offsetInBits, lengthInBits, key);
        // Then: verify the result
        assertFalse(isPrefix);
    }

    @Test
    public void testBitIndex_NullKey() {
        // Given: null key
        String key = null;
        int offsetInBits = 0;
        int lengthInBits = 16;
        String other = "Hello";
        int otherOffsetInBits = 0;
        int otherLengthInBits = 16;
        // When: bitIndex method is called
        assertThrows(NullPointerException.class, () -> analyzer.bitIndex(key, offsetInBits, lengthInBits, other, otherOffsetInBits, otherLengthInBits));
    }

    @Test
    public void testBitIndex_NullOther() {
        // Given: null other
        String key = "Hello";
        int offsetInBits = 0;
        int lengthInBits = 16;
        String other = null;
        int otherOffsetInBits = 0;
        int otherLengthInBits = 16;
        // When: bitIndex method is called
        assertThrows(NullPointerException.class, () -> analyzer.bitIndex(key, offsetInBits, lengthInBits, other, otherOffsetInBits, otherLengthInBits));
    }

    @Test
    public void testBitIndex_OffsetOrLengthNotMultipleOfCharacterSize() {
        // Given: offset or length not multiple of character size
        String key = "Hello";
        int offsetInBits = 1;
        int lengthInBits = 16;
        String other = "Hello";
        int otherOffsetInBits = 0;
        int otherLengthInBits = 16;
        // When: bitIndex method is called
        assertThrows(IllegalArgumentException.class, () -> analyzer.bitIndex(key, offsetInBits, lengthInBits, other, otherOffsetInBits, otherLengthInBits));
    }

    @Test
    public void testBitIndex_KeysAreEqual() {
        // Given: keys are equal
        String key = "Hello";
        int offsetInBits = 0;
        int lengthInBits = 16;
        String other = "Hello";
        int otherOffsetInBits = 0;
        int otherLengthInBits = 16;
        // When: bitIndex method is called
        int bitIndex = analyzer.bitIndex(key, offsetInBits, lengthInBits, other, otherOffsetInBits, otherLengthInBits);
        // Then: verify the result
        assertEquals(StringKeyAnalyzer.EQUAL_BIT_KEY, bitIndex);
    }

    @Test
    public void testBitIndex_KeysAreNotEqual() {
        // Given: keys are not equal
        String key = "Hello";
        int offsetInBits = 0;
        int lengthInBits = 16;
        String other = "World";
        int otherOffsetInBits = 0;
        int otherLengthInBits = 16;
        // When: bitIndex method is called
        int bitIndex = analyzer.bitIndex(key, offsetInBits, lengthInBits, other, otherOffsetInBits, otherLengthInBits);
        // Then: verify the result
        assertNotEquals(StringKeyAnalyzer.EQUAL_BIT_KEY, bitIndex);
    }
}
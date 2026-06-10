import org.apache.commons.collections4.bloomfilter.BitMaps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BitMapsTest {

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testContains_BitSet() {
        long[] bitMaps = new long[1];
        BitMaps.set(bitMaps, 0);
        assertTrue(BitMaps.contains(bitMaps, 0));
    }

    @Test
    public void testContains_BitNotSet() {
        long[] bitMaps = new long[1];
        assertFalse(BitMaps.contains(bitMaps, 0));
    }

    @Test
    public void testContains_BitIndexOutOfRange() {
        long[] bitMaps = new long[1];
        assertThrows(IndexOutOfBoundsException.class, () -> BitMaps.contains(bitMaps, 64));
    }

    @Test
    public void testGetLongBit() {
        assertEquals(1L, BitMaps.getLongBit(0));
        assertEquals(2L, BitMaps.getLongBit(1));
        assertEquals(4L, BitMaps.getLongBit(2));
    }

    @Test
    public void testGetLongBit_NegativeIndex() {
        // Behavior is not defined for negative index
        assertThrows(IllegalArgumentException.class, () -> BitMaps.getLongBit(-1));
    }

    @Test
    public void testGetLongIndex() {
        assertEquals(0, BitMaps.getLongIndex(0));
        assertEquals(0, BitMaps.getLongIndex(63));
        assertEquals(1, BitMaps.getLongIndex(64));
    }

    @Test
    public void testGetLongIndex_NegativeIndex() {
        // Behavior is not defined for negative index
        assertThrows(IllegalArgumentException.class, () -> BitMaps.getLongIndex(-1));
    }

    @Test
    public void testMod() {
        assertEquals(1, BitMaps.mod(10L, 3));
        assertEquals(2, BitMaps.mod(11L, 3));
        assertEquals(0, BitMaps.mod(12L, 3));
    }

    @Test
    public void testMod_DivisorZero() {
        assertThrows(ArithmeticException.class, () -> BitMaps.mod(10L, 0));
    }

    @Test
    public void testMod_DivisorNegative() {
        // Behavior is not defined for negative divisor
        assertThrows(IllegalArgumentException.class, () -> BitMaps.mod(10L, -3));
    }

    @Test
    public void testNumberOfBitMaps() {
        assertEquals(1, BitMaps.numberOfBitMaps(64));
        assertEquals(2, BitMaps.numberOfBitMaps(65));
        assertEquals(2, BitMaps.numberOfBitMaps(128));
    }

    @Test
    public void testNumberOfBitMaps_NegativeNumberOfBits() {
        // Behavior is not defined for negative number of bits
        assertThrows(IllegalArgumentException.class, () -> BitMaps.numberOfBitMaps(-1));
    }

    @Test
    public void testSet() {
        long[] bitMaps = new long[1];
        BitMaps.set(bitMaps, 0);
        assertTrue(BitMaps.contains(bitMaps, 0));
    }

    @Test
    public void testSet_BitIndexOutOfRange() {
        long[] bitMaps = new long[1];
        assertThrows(IndexOutOfBoundsException.class, () -> BitMaps.set(bitMaps, 64));
    }
}
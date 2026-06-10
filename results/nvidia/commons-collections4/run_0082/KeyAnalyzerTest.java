import org.apache.commons.collections4.trie.KeyAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KeyAnalyzerTest {

    @Mock
    private KeyAnalyzer<String> keyAnalyzer;

    @BeforeEach
    void setup() {
        // Initialize the mock object
        keyAnalyzer = mock(KeyAnalyzer.class);
    }

    @Test
    public void testBitIndex() {
        // Given
        String key = "testKey";
        int offsetInBits = 0;
        int lengthInBits = 10;
        String other = "otherKey";
        int otherOffsetInBits = 0;
        int otherLengthInBits = 10;
        int expectedBitIndex = 5;

        // When
        when(keyAnalyzer.bitIndex(any(), anyInt(), anyInt(), any(), anyInt(), anyInt())).thenReturn(expectedBitIndex);

        // Then
        int bitIndex = keyAnalyzer.bitIndex(key, offsetInBits, lengthInBits, other, otherOffsetInBits, otherLengthInBits);
        assertEquals(expectedBitIndex, bitIndex);
        verify(keyAnalyzer, times(1)).bitIndex(key, offsetInBits, lengthInBits, other, otherOffsetInBits, otherLengthInBits);
    }

    @Test
    public void testBitsPerElement() {
        // Given
        int expectedBitsPerElement = 8;

        // When
        when(keyAnalyzer.bitsPerElement()).thenReturn(expectedBitsPerElement);

        // Then
        int bitsPerElement = keyAnalyzer.bitsPerElement();
        assertEquals(expectedBitsPerElement, bitsPerElement);
        verify(keyAnalyzer, times(1)).bitsPerElement();
    }

    @Test
    public void testCompare_Equal() {
        // Given
        String o1 = "testKey";
        String o2 = "testKey";

        // When
        when(keyAnalyzer.compare(any(), any())).thenReturn(0);

        // Then
        int comparison = keyAnalyzer.compare(o1, o2);
        assertEquals(0, comparison);
        verify(keyAnalyzer, times(1)).compare(o1, o2);
    }

    @Test
    public void testCompare_NotEqual() {
        // Given
        String o1 = "testKey";
        String o2 = "otherKey";

        // When
        when(keyAnalyzer.compare(any(), any())).thenReturn(1);

        // Then
        int comparison = keyAnalyzer.compare(o1, o2);
        assertEquals(1, comparison);
        verify(keyAnalyzer, times(1)).compare(o1, o2);
    }

    @Test
    public void testIsBitSet() {
        // Given
        String key = "testKey";
        int bitIndex = 5;
        int lengthInBits = 10;
        boolean expectedBitSet = true;

        // When
        when(keyAnalyzer.isBitSet(any(), anyInt(), anyInt())).thenReturn(expectedBitSet);

        // Then
        boolean isBitSet = keyAnalyzer.isBitSet(key, bitIndex, lengthInBits);
        assertEquals(expectedBitSet, isBitSet);
        verify(keyAnalyzer, times(1)).isBitSet(key, bitIndex, lengthInBits);
    }

    @Test
    public void testIsPrefix() {
        // Given
        String prefix = "test";
        int offsetInBits = 0;
        int lengthInBits = 10;
        String key = "testKey";
        boolean expectedPrefix = true;

        // When
        when(keyAnalyzer.isPrefix(any(), anyInt(), anyInt(), any())).thenReturn(expectedPrefix);

        // Then
        boolean isPrefix = keyAnalyzer.isPrefix(prefix, offsetInBits, lengthInBits, key);
        assertEquals(expectedPrefix, isPrefix);
        verify(keyAnalyzer, times(1)).isPrefix(prefix, offsetInBits, lengthInBits, key);
    }

    @Test
    public void testLengthInBits() {
        // Given
        String key = "testKey";
        int expectedLengthInBits = 10;

        // When
        when(keyAnalyzer.lengthInBits(any())).thenReturn(expectedLengthInBits);

        // Then
        int lengthInBits = keyAnalyzer.lengthInBits(key);
        assertEquals(expectedLengthInBits, lengthInBits);
        verify(keyAnalyzer, times(1)).lengthInBits(key);
    }

    @Test
    public void testIsValidBitIndex() {
        // Given
        int bitIndex = 5;

        // Then
        boolean isValid = KeyAnalyzer.isValidBitIndex(bitIndex);
        assertTrue(isValid);
    }

    @Test
    public void testIsEqualBitKey() {
        // Given
        int bitIndex = KeyAnalyzer.EQUAL_BIT_KEY;

        // Then
        boolean isEqual = KeyAnalyzer.isEqualBitKey(bitIndex);
        assertTrue(isEqual);
    }

    @Test
    public void testIsNullBitKey() {
        // Given
        int bitIndex = KeyAnalyzer.NULL_BIT_KEY;

        // Then
        boolean isNull = KeyAnalyzer.isNullBitKey(bitIndex);
        assertTrue(isNull);
    }

    @Test
    public void testIsOutOfBoundsIndex() {
        // Given
        int bitIndex = KeyAnalyzer.OUT_OF_BOUNDS_BIT_KEY;

        // Then
        boolean isOutOfBounds = KeyAnalyzer.isOutOfBoundsIndex(bitIndex);
        assertTrue(isOutOfBounds);
    }
}
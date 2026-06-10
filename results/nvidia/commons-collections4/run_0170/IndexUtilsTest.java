import org.apache.commons.collections4.bloomfilter.IndexUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IndexUtilsTest {

    private int[] array;
    private int index;

    @BeforeEach
    void setup() {
        array = new int[10];
        index = 5;
    }

    @Test
    void testEnsureCapacityForAdd_IndexWithinBounds() {
        // Given: index is within the bounds of the array
        index = 5;
        int[] result = IndexUtils.ensureCapacityForAdd(array, index);
        // Then: the original array is returned
        assertSame(array, result);
    }

    @Test
    void testEnsureCapacityForAdd_IndexAtBounds() {
        // Given: index is at the bounds of the array
        index = 10;
        int[] result = IndexUtils.ensureCapacityForAdd(array, index);
        // Then: a new array with increased capacity is returned
        assertNotSame(array, result);
        assertEquals(20, result.length);
    }

    @Test
    void testEnsureCapacityForAdd_IndexExceedsMaxArraySize() {
        // Given: index exceeds the maximum array size
        index = IndexUtils.MAX_ARRAY_SIZE + 1;
        int[] result = IndexUtils.ensureCapacityForAdd(array, index);
        // Then: a new array with the maximum capacity is returned
        assertNotSame(array, result);
        assertEquals(IndexUtils.MAX_ARRAY_SIZE, result.length);
    }

    @Test
    void testEnsureCapacityForAdd_NullArray() {
        // Given: null array
        array = null;
        assertThrows(NullPointerException.class, () -> IndexUtils.ensureCapacityForAdd(array, index));
    }

    @Test
    void testEnsureCapacityForAdd_NegativeIndex() {
        // Given: negative index
        index = -1;
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> IndexUtils.ensureCapacityForAdd(array, index));
    }
}
import org.apache.commons.collections4.bloomfilter.Shape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShapeTest {

    private Shape shape;

    @BeforeEach
    public void setup() {
        shape = Shape.fromKM(5, 100);
    }

    @Test
    public void testFromKM() {
        Shape shape = Shape.fromKM(5, 100);
        assertEquals(5, shape.getNumberOfHashFunctions());
        assertEquals(100, shape.getNumberOfBits());
    }

    @Test
    public void testFromNM() {
        Shape shape = Shape.fromNM(100, 1000);
        assertNotNull(shape);
    }

    @Test
    public void testFromNMK() {
        Shape shape = Shape.fromNMK(100, 1000, 5);
        assertNotNull(shape);
    }

    @Test
    public void testFromNP() {
        Shape shape = Shape.fromNP(100, 0.1);
        assertNotNull(shape);
    }

    @Test
    public void testFromPMK() {
        Shape shape = Shape.fromPMK(0.1, 1000, 5);
        assertNotNull(shape);
    }

    @Test
    public void testEquals() {
        Shape shape1 = Shape.fromKM(5, 100);
        Shape shape2 = Shape.fromKM(5, 100);
        assertTrue(shape1.equals(shape2));
    }

    @Test
    public void testNotEquals() {
        Shape shape1 = Shape.fromKM(5, 100);
        Shape shape2 = Shape.fromKM(6, 100);
        assertFalse(shape1.equals(shape2));
    }

    @Test
    public void testEstimateMaxN() {
        double maxN = shape.estimateMaxN();
        assertTrue(maxN > 0);
    }

    @Test
    public void testEstimateN() {
        double n = shape.estimateN(50);
        assertTrue(n > 0);
    }

    @Test
    public void testGetNumberOfBits() {
        int numberOfBits = shape.getNumberOfBits();
        assertEquals(100, numberOfBits);
    }

    @Test
    public void testGetNumberOfHashFunctions() {
        int numberOfHashFunctions = shape.getNumberOfHashFunctions();
        assertEquals(5, numberOfHashFunctions);
    }

    @Test
    public void testGetProbability() {
        double probability = shape.getProbability(50);
        assertTrue(probability > 0);
    }

    @Test
    public void testHashCode() {
        int hashCode = shape.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testIsSparse() {
        boolean isSparse = shape.isSparse(50);
        assertTrue(isSparse);
    }

    @Test
    public void testToString() {
        String toString = shape.toString();
        assertNotNull(toString);
    }

    @Test
    public void testFromKM_InvalidNumberOfHashFunctions() {
        assertThrows(IllegalArgumentException.class, () -> Shape.fromKM(0, 100));
    }

    @Test
    public void testFromKM_InvalidNumberOfBits() {
        assertThrows(IllegalArgumentException.class, () -> Shape.fromKM(5, 0));
    }

    @Test
    public void testFromNM_InvalidNumberOfItems() {
        assertThrows(IllegalArgumentException.class, () -> Shape.fromNM(0, 1000));
    }

    @Test
    public void testFromNM_InvalidNumberOfBits() {
        assertThrows(IllegalArgumentException.class, () -> Shape.fromNM(100, 0));
    }

    @Test
    public void testFromNMK_InvalidNumberOfItems() {
        assertThrows(IllegalArgumentException.class, () -> Shape.fromNMK(0, 1000, 5));
    }

    @Test
    public void testFromNMK_InvalidNumberOfBits() {
        assertThrows(IllegalArgumentException.class, () -> Shape.fromNMK(100, 0, 5));
    }

    @Test
    public void testFromNMK_InvalidNumberOfHashFunctions() {
        assertThrows(IllegalArgumentException.class, () -> Shape.fromNMK(100, 1000, 0));
    }

    @Test
    public void testFromNP_InvalidNumberOfItems() {
        assertThrows(IllegalArgumentException.class, () -> Shape.fromNP(0, 0.1));
    }

    @Test
    public void testFromNP_InvalidProbability() {
        assertThrows(IllegalArgumentException.class, () -> Shape.fromNP(100, 1.1));
    }

    @Test
    public void testFromPMK_InvalidProbability() {
        assertThrows(IllegalArgumentException.class, () -> Shape.fromPMK(1.1, 1000, 5));
    }

    @Test
    public void testFromPMK_InvalidNumberOfBits() {
        assertThrows(IllegalArgumentException.class, () -> Shape.fromPMK(0.1, 0, 5));
    }

    @Test
    public void testFromPMK_InvalidNumberOfHashFunctions() {
        assertThrows(IllegalArgumentException.class, () -> Shape.fromPMK(0.1, 1000, 0));
    }
}
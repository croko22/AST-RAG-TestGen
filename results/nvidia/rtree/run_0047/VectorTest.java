import com.github.davidmoten.rtree.geometry.internal.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VectorTest {

    private Vector vector1;
    private Vector vector2;

    @BeforeEach
    public void setup() {
        vector1 = Vector.create(1.0, 2.0);
        vector2 = Vector.create(3.0, 4.0);
    }

    @Test
    public void testCreateVector() {
        // Given: x and y coordinates
        double x = 1.0;
        double y = 2.0;

        // When: create a new vector
        Vector vector = Vector.create(x, y);

        // Then: verify the vector's coordinates
        assertEquals(x, vector.x);
        assertEquals(y, vector.y);
    }

    @Test
    public void testDotProduct() {
        // Given: two vectors
        Vector vector1 = Vector.create(1.0, 2.0);
        Vector vector2 = Vector.create(3.0, 4.0);

        // When: calculate the dot product
        double dotProduct = vector1.dot(vector2);

        // Then: verify the dot product
        assertEquals(1.0 * 3.0 + 2.0 * 4.0, dotProduct);
    }

    @Test
    public void testTimes() {
        // Given: a vector and a scalar value
        Vector vector = Vector.create(1.0, 2.0);
        double value = 2.0;

        // When: multiply the vector by the scalar value
        Vector result = vector.times(value);

        // Then: verify the result
        assertEquals(value * vector.x, result.x);
        assertEquals(value * vector.y, result.y);
    }

    @Test
    public void testMinus() {
        // Given: two vectors
        Vector vector1 = Vector.create(1.0, 2.0);
        Vector vector2 = Vector.create(3.0, 4.0);

        // When: subtract vector2 from vector1
        Vector result = vector1.minus(vector2);

        // Then: verify the result
        assertEquals(vector1.x - vector2.x, result.x);
        assertEquals(vector1.y - vector2.y, result.y);
    }

    @Test
    public void testModulus() {
        // Given: a vector
        Vector vector = Vector.create(3.0, 4.0);

        // When: calculate the modulus
        double modulus = vector.modulus();

        // Then: verify the modulus
        assertEquals(Math.sqrt(vector.x * vector.x + vector.y * vector.y), modulus);
    }

    @Test
    public void testModulusSquared() {
        // Given: a vector
        Vector vector = Vector.create(3.0, 4.0);

        // When: calculate the modulus squared
        double modulusSquared = vector.modulusSquared();

        // Then: verify the modulus squared
        assertEquals(vector.x * vector.x + vector.y * vector.y, modulusSquared);
    }

    @Test
    public void testModulus_ZeroVector() {
        // Given: a zero vector
        Vector vector = Vector.create(0.0, 0.0);

        // When: calculate the modulus
        double modulus = vector.modulus();

        // Then: verify the modulus
        assertEquals(0.0, modulus);
    }

    @Test
    public void testModulusSquared_ZeroVector() {
        // Given: a zero vector
        Vector vector = Vector.create(0.0, 0.0);

        // When: calculate the modulus squared
        double modulusSquared = vector.modulusSquared();

        // Then: verify the modulus squared
        assertEquals(0.0, modulusSquared);
    }
}
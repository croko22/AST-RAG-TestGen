import com.github.davidmoten.rtree.internal.util.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PairTest {

    @Test
    public void testConstructorAndGetters_StringValues() {
        // Given
        String value1 = "value1";
        String value2 = "value2";

        // When
        Pair<String> pair = new Pair<>(value1, value2);

        // Then
        assertEquals(value1, pair.value1());
        assertEquals(value2, pair.value2());
    }

    @Test
    public void testConstructorAndGetters_IntegerValues() {
        // Given
        Integer value1 = 1;
        Integer value2 = 2;

        // When
        Pair<Integer> pair = new Pair<>(value1, value2);

        // Then
        assertEquals(value1, pair.value1());
        assertEquals(value2, pair.value2());
    }

    @Test
    public void testConstructorAndGetters_NullValues() {
        // Given
        String value1 = null;
        String value2 = null;

        // When
        Pair<String> pair = new Pair<>(value1, value2);

        // Then
        assertNull(pair.value1());
        assertNull(pair.value2());
    }

    @Test
    public void testConstructorAndGetters_DifferentTypes() {
        // Given
        String value1 = "value1";
        Integer value2 = 2;

        // When
        Pair<Object> pair = new Pair<>(value1, value2);

        // Then
        assertEquals(value1, pair.value1());
        assertEquals(value2, pair.value2());
    }
}
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.HasGeometry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntryTest {

    @Mock
    private Geometry geometry;

    @Mock
    private Object value;

    @Test
    public void testValue() {
        // Given
        Entry<Object, Geometry> entry = new Entry<Object, Geometry>() {
            @Override
            public Object value() {
                return value;
            }

            @Override
            public Geometry geometry() {
                return geometry;
            }
        };

        // When
        Object result = entry.value();

        // Then
        assertEquals(value, result);
        verifyNoInteractions(geometry);
    }

    @Test
    public void testGeometry() {
        // Given
        Entry<Object, Geometry> entry = new Entry<Object, Geometry>() {
            @Override
            public Object value() {
                return value;
            }

            @Override
            public Geometry geometry() {
                return geometry;
            }
        };

        // When
        Geometry result = entry.geometry();

        // Then
        assertEquals(geometry, result);
        verifyNoInteractions(value);
    }
}
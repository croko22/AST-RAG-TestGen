import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.HasGeometry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HasGeometryTest {

    @Mock
    private Geometry geometry;

    @Mock
    private HasGeometry hasGeometry;

    @BeforeEach
    void setup() {
        when(hasGeometry.geometry()).thenReturn(geometry);
    }

    @Test
    public void testGeometry() {
        // When: se ejecuta el método geometry
        Geometry result = hasGeometry.geometry();

        // Then: se verifica el resultado
        assertEquals(geometry, result);

        // Then: se verifica la interacción con el mock
        verify(hasGeometry, times(1)).geometry();
    }

    @Test
    public void testGeometry_Null() {
        // Given: el mock de HasGeometry devuelve null
        when(hasGeometry.geometry()).thenReturn(null);

        // When: se ejecuta el método geometry
        Geometry result = hasGeometry.geometry();

        // Then: se verifica el resultado
        assertNull(result);

        // Then: se verifica la interacción con el mock
        verify(hasGeometry, times(1)).geometry();
    }
}
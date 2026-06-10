import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.github.davidmoten.rtree.fbs.generated.GeometryType_;

public class GeometryType_Test {

    @Test
    public void test_name_PointFloat() {
        // Given: GeometryType_ PointFloat
        int type = GeometryType_.PointFloat;
        
        // When: se llama al método name
        String name = GeometryType_.name(type);
        
        // Then: se verifica el resultado
        assertEquals("PointFloat", name);
    }

    @Test
    public void test_name_BoxFloat() {
        // Given: GeometryType_ BoxFloat
        int type = GeometryType_.BoxFloat;
        
        // When: se llama al método name
        String name = GeometryType_.name(type);
        
        // Then: se verifica el resultado
        assertEquals("BoxFloat", name);
    }

    @Test
    public void test_name_CircleFloat() {
        // Given: GeometryType_ CircleFloat
        int type = GeometryType_.CircleFloat;
        
        // When: se llama al método name
        String name = GeometryType_.name(type);
        
        // Then: se verifica el resultado
        assertEquals("CircleFloat", name);
    }

    @Test
    public void test_name_LineFloat() {
        // Given: GeometryType_ LineFloat
        int type = GeometryType_.LineFloat;
        
        // When: se llama al método name
        String name = GeometryType_.name(type);
        
        // Then: se verifica el resultado
        assertEquals("LineFloat", name);
    }

    @Test
    public void test_name_PointDouble() {
        // Given: GeometryType_ PointDouble
        int type = GeometryType_.PointDouble;
        
        // When: se llama al método name
        String name = GeometryType_.name(type);
        
        // Then: se verifica el resultado
        assertEquals("PointDouble", name);
    }

    @Test
    public void test_name_BoxDouble() {
        // Given: GeometryType_ BoxDouble
        int type = GeometryType_.BoxDouble;
        
        // When: se llama al método name
        String name = GeometryType_.name(type);
        
        // Then: se verifica el resultado
        assertEquals("BoxDouble", name);
    }

    @Test
    public void test_name_CircleDouble() {
        // Given: GeometryType_ CircleDouble
        int type = GeometryType_.CircleDouble;
        
        // When: se llama al método name
        String name = GeometryType_.name(type);
        
        // Then: se verifica el resultado
        assertEquals("CircleDouble", name);
    }

    @Test
    public void test_name_LineDouble() {
        // Given: GeometryType_ LineDouble
        int type = GeometryType_.LineDouble;
        
        // When: se llama al método name
        String name = GeometryType_.name(type);
        
        // Then: se verifica el resultado
        assertEquals("LineDouble", name);
    }

    @Test
    public void test_name_InvalidType() {
        // Given: tipo no válido
        int type = -1;
        
        // When: se llama al método name
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> GeometryType_.name(type));
    }

    @Test
    public void test_name_TypeGreaterThanMaxValue() {
        // Given: tipo mayor que el máximo valor
        int type = GeometryType_.names.length;
        
        // When: se llama al método name
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> GeometryType_.name(type));
    }
}
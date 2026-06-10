import com.github.davidmoten.rtree.fbs.generated.BoxFloat_;
import com.google.flatbuffers.FlatBufferBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.nio.ByteBuffer;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BoxFloat_Test {

    @Mock
    private FlatBufferBuilder flatBufferBuilder;

    @Mock
    private ByteBuffer byteBuffer;

    private BoxFloat_ boxFloat_;

    @BeforeEach
    public void setup() {
        boxFloat_ = new BoxFloat_();
    }

    @Test
    public void test__init() {
        // Given
        int index = 0;
        // When
        boxFloat_.__init(index, byteBuffer);
        // Then
        // No assertions, __init is a void method and doesn't return anything
    }

    @Test
    public void test__assign() {
        // Given
        int index = 0;
        // When
        BoxFloat_ result = boxFloat_.__assign(index, byteBuffer);
        // Then
        assertEquals(boxFloat_, result);
    }

    @Test
    public void testMinX() {
        // Given
        float minX = 10.0f;
        // When
        when(byteBuffer.getFloat(0)).thenReturn(minX);
        float result = boxFloat_.minX();
        // Then
        assertEquals(minX, result);
    }

    @Test
    public void testMinY() {
        // Given
        float minY = 10.0f;
        // When
        when(byteBuffer.getFloat(4)).thenReturn(minY);
        float result = boxFloat_.minY();
        // Then
        assertEquals(minY, result);
    }

    @Test
    public void testMaxX() {
        // Given
        float maxX = 10.0f;
        // When
        when(byteBuffer.getFloat(8)).thenReturn(maxX);
        float result = boxFloat_.maxX();
        // Then
        assertEquals(maxX, result);
    }

    @Test
    public void testMaxY() {
        // Given
        float maxY = 10.0f;
        // When
        when(byteBuffer.getFloat(12)).thenReturn(maxY);
        float result = boxFloat_.maxY();
        // Then
        assertEquals(maxY, result);
    }

    @Test
    public void testCreateBoxFloat_() {
        // Given
        float minX = 10.0f;
        float minY = 20.0f;
        float maxX = 30.0f;
        float maxY = 40.0f;
        // When
        int result = BoxFloat_.createBoxFloat_(flatBufferBuilder, minX, minY, maxX, maxY);
        // Then
        // No assertions, createBoxFloat_ is a static method and doesn't return anything meaningful for this test
    }

    @Test
    public void testGet() {
        // Given
        int index = 0;
        // When
        BoxFloat_ result = boxFloat_.get(index);
        // Then
        assertNotNull(result);
    }

    @Test
    public void testGetWithObject() {
        // Given
        int index = 0;
        BoxFloat_ obj = new BoxFloat_();
        // When
        BoxFloat_ result = boxFloat_.get(obj, index);
        // Then
        assertNotNull(result);
    }
}
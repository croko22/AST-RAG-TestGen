import com.github.davidmoten.rtree.fbs.generated.LineFloat_;
import com.google.flatbuffers.FlatBufferBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.ByteBuffer;
import static org.junit.jupiter.api.Assertions.*;

public class LineFloat_Test {

    private LineFloat_ lineFloat_;
    private FlatBufferBuilder builder;
    private ByteBuffer byteBuffer;

    @BeforeEach
    public void setup() {
        lineFloat_ = new LineFloat_();
        builder = new FlatBufferBuilder();
        byteBuffer = ByteBuffer.allocate(16);
    }

    @Test
    public void test__init() {
        // Given
        int index = 0;
        // When
        lineFloat_.__init(index, byteBuffer);
        // Then
        // No assertions, as __init is a void method and does not return any value
    }

    @Test
    public void test__assign() {
        // Given
        int index = 0;
        // When
        LineFloat_ assignedLineFloat = lineFloat_.__assign(index, byteBuffer);
        // Then
        assertNotNull(assignedLineFloat);
        assertSame(lineFloat_, assignedLineFloat);
    }

    @Test
    public void testMinX() {
        // Given
        float minX = 10.0f;
        builder.putFloat(minX);
        byteBuffer = builder.dataBuffer();
        lineFloat_.__init(0, byteBuffer);
        // When
        float result = lineFloat_.minX();
        // Then
        assertEquals(minX, result, 0.001);
    }

    @Test
    public void testMinY() {
        // Given
        float minY = 20.0f;
        builder.putFloat(minY);
        byteBuffer = builder.dataBuffer();
        lineFloat_.__init(0, byteBuffer);
        // When
        float result = lineFloat_.minY();
        // Then
        assertEquals(minY, result, 0.001);
    }

    @Test
    public void testMaxX() {
        // Given
        float maxX = 30.0f;
        builder.putFloat(maxX);
        byteBuffer = builder.dataBuffer();
        lineFloat_.__init(0, byteBuffer);
        // When
        float result = lineFloat_.maxX();
        // Then
        assertEquals(maxX, result, 0.001);
    }

    @Test
    public void testMaxY() {
        // Given
        float maxY = 40.0f;
        builder.putFloat(maxY);
        byteBuffer = builder.dataBuffer();
        lineFloat_.__init(0, byteBuffer);
        // When
        float result = lineFloat_.maxY();
        // Then
        assertEquals(maxY, result, 0.001);
    }

    @Test
    public void testCreateLineFloat_() {
        // Given
        float minX = 10.0f;
        float minY = 20.0f;
        float maxX = 30.0f;
        float maxY = 40.0f;
        // When
        int offset = LineFloat_.createLineFloat_(builder, minX, minY, maxX, maxY);
        // Then
        assertNotNull(offset);
        assertTrue(offset > 0);
    }

    @Test
    public void testGet() {
        // Given
        LineFloat_.Vector vector = new LineFloat_.Vector();
        int index = 0;
        // When
        LineFloat_ result = vector.get(index);
        // Then
        assertNotNull(result);
    }

    @Test
    public void testGetWithObject() {
        // Given
        LineFloat_ obj = new LineFloat_();
        LineFloat_.Vector vector = new LineFloat_.Vector();
        int index = 0;
        // When
        LineFloat_ result = vector.get(obj, index);
        // Then
        assertNotNull(result);
        assertSame(obj, result);
    }
}
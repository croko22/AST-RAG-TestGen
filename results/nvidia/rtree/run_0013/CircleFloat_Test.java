import com.github.davidmoten.rtree.fbs.generated.CircleFloat_;
import com.google.flatbuffers.FlatBufferBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CircleFloat_Test {

    @Mock
    private FlatBufferBuilder builder;

    @Mock
    private ByteBuffer byteBuffer;

    private CircleFloat_ circleFloat;

    @BeforeEach
    public void setup() {
        circleFloat = new CircleFloat_();
    }

    @AfterEach
    public void tearDown() {
        circleFloat = null;
    }

    @Test
    public void test__init() {
        // Given
        int index = 0;

        // When
        circleFloat.__init(index, byteBuffer);

        // Then
        // No assertions, __init is a void method
    }

    @Test
    public void test__assign() {
        // Given
        int index = 0;

        // When
        CircleFloat_ assignedCircleFloat = circleFloat.__assign(index, byteBuffer);

        // Then
        assertNotNull(assignedCircleFloat);
        assertSame(circleFloat, assignedCircleFloat);
    }

    @Test
    public void testX() {
        // Given
        float x = 10.0f;
        builder = new FlatBufferBuilder();
        int offset = CircleFloat_.createCircleFloat_(builder, x, 20.0f, 30.0f);
        byteBuffer = builder.dataBuffer();

        // When
        circleFloat.__init(0, byteBuffer);
        float result = circleFloat.x();

        // Then
        assertEquals(x, result, 0.001);
    }

    @Test
    public void testY() {
        // Given
        float y = 20.0f;
        builder = new FlatBufferBuilder();
        int offset = CircleFloat_.createCircleFloat_(builder, 10.0f, y, 30.0f);
        byteBuffer = builder.dataBuffer();

        // When
        circleFloat.__init(0, byteBuffer);
        float result = circleFloat.y();

        // Then
        assertEquals(y, result, 0.001);
    }

    @Test
    public void testRadius() {
        // Given
        float radius = 30.0f;
        builder = new FlatBufferBuilder();
        int offset = CircleFloat_.createCircleFloat_(builder, 10.0f, 20.0f, radius);
        byteBuffer = builder.dataBuffer();

        // When
        circleFloat.__init(0, byteBuffer);
        float result = circleFloat.radius();

        // Then
        assertEquals(radius, result, 0.001);
    }

    @Test
    public void testCreateCircleFloat_() {
        // Given
        float x = 10.0f;
        float y = 20.0f;
        float radius = 30.0f;
        builder = new FlatBufferBuilder();

        // When
        int offset = CircleFloat_.createCircleFloat_(builder, x, y, radius);

        // Then
        assertNotNull(builder.dataBuffer());
        assertEquals(12, builder.offset());
    }

    @Test
    public void testGet() {
        // Given
        float x = 10.0f;
        float y = 20.0f;
        float radius = 30.0f;
        builder = new FlatBufferBuilder();
        int offset = CircleFloat_.createCircleFloat_(builder, x, y, radius);
        byteBuffer = builder.dataBuffer();

        // When
        CircleFloat_.Vector vector = new CircleFloat_.Vector();
        vector.__assign(0, 12, byteBuffer);
        CircleFloat_ result = vector.get(0);

        // Then
        assertNotNull(result);
        assertEquals(x, result.x(), 0.001);
        assertEquals(y, result.y(), 0.001);
        assertEquals(radius, result.radius(), 0.001);
    }

    @Test
    public void testGetWithObject() {
        // Given
        float x = 10.0f;
        float y = 20.0f;
        float radius = 30.0f;
        builder = new FlatBufferBuilder();
        int offset = CircleFloat_.createCircleFloat_(builder, x, y, radius);
        byteBuffer = builder.dataBuffer();

        // When
        CircleFloat_.Vector vector = new CircleFloat_.Vector();
        vector.__assign(0, 12, byteBuffer);
        CircleFloat_ obj = new CircleFloat_();
        CircleFloat_ result = vector.get(obj, 0);

        // Then
        assertNotNull(result);
        assertEquals(x, result.x(), 0.001);
        assertEquals(y, result.y(), 0.001);
        assertEquals(radius, result.radius(), 0.001);
    }
}
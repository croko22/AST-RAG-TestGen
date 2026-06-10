import com.github.davidmoten.rtree.fbs.generated.CircleDouble_;
import com.google.flatbuffers.FlatBufferBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CircleDouble_Test {

    @Mock
    private FlatBufferBuilder flatBufferBuilder;

    @Mock
    private ByteBuffer byteBuffer;

    private CircleDouble_ circleDouble_;

    @BeforeEach
    void setup() {
        circleDouble_ = new CircleDouble_();
    }

    @AfterEach
    void tearDown() {
        circleDouble_ = null;
    }

    @Test
    public void test__init() {
        // Given
        int index = 0;
        // When
        circleDouble_.__init(index, byteBuffer);
        // Then
        // No assertions, __init is a void method
    }

    @Test
    public void test__assign() {
        // Given
        int index = 0;
        // When
        CircleDouble_ assignedCircleDouble = circleDouble_.__assign(index, byteBuffer);
        // Then
        assertNotNull(assignedCircleDouble);
        assertSame(circleDouble_, assignedCircleDouble);
    }

    @Test
    public void testX() {
        // Given
        double x = 10.0;
        // When
        // We can't directly set the x value, so we create a new CircleDouble_ with the given x value
        int offset = CircleDouble_.createCircleDouble_(new FlatBufferBuilder(), x, 0, 0);
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putDouble(x);
        buffer.putDouble(0);
        buffer.putDouble(0);
        buffer.flip();
        circleDouble_.__init(0, buffer);
        double result = circleDouble_.x();
        // Then
        assertEquals(x, result, 0.001);
    }

    @Test
    public void testY() {
        // Given
        double y = 20.0;
        // When
        // We can't directly set the y value, so we create a new CircleDouble_ with the given y value
        int offset = CircleDouble_.createCircleDouble_(new FlatBufferBuilder(), 0, y, 0);
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putDouble(0);
        buffer.putDouble(y);
        buffer.putDouble(0);
        buffer.flip();
        circleDouble_.__init(0, buffer);
        double result = circleDouble_.y();
        // Then
        assertEquals(y, result, 0.001);
    }

    @Test
    public void testRadius() {
        // Given
        double radius = 30.0;
        // When
        // We can't directly set the radius value, so we create a new CircleDouble_ with the given radius value
        int offset = CircleDouble_.createCircleDouble_(new FlatBufferBuilder(), 0, 0, radius);
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putDouble(radius);
        buffer.putDouble(0);
        buffer.putDouble(0);
        buffer.flip();
        circleDouble_.__init(0, buffer);
        double result = circleDouble_.radius();
        // Then
        assertEquals(radius, result, 0.001);
    }

    @Test
    public void testCreateCircleDouble_() {
        // Given
        double x = 10.0;
        double y = 20.0;
        double radius = 30.0;
        // When
        int offset = CircleDouble_.createCircleDouble_(new FlatBufferBuilder(), x, y, radius);
        // Then
        assertNotEquals(0, offset);
    }

    @Test
    public void testGet() {
        // Given
        double x = 10.0;
        double y = 20.0;
        double radius = 30.0;
        int offset = CircleDouble_.createCircleDouble_(new FlatBufferBuilder(), x, y, radius);
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putDouble(radius);
        buffer.putDouble(y);
        buffer.putDouble(x);
        buffer.flip();
        circleDouble_.__init(0, buffer);
        CircleDouble_.Vector vector = new CircleDouble_.Vector();
        vector.__assign(0, 24, buffer);
        // When
        CircleDouble_ result = vector.get(0);
        // Then
        assertNotNull(result);
        assertEquals(x, result.x(), 0.001);
        assertEquals(y, result.y(), 0.001);
        assertEquals(radius, result.radius(), 0.001);
    }

    @Test
    public void testGetWithObject() {
        // Given
        double x = 10.0;
        double y = 20.0;
        double radius = 30.0;
        int offset = CircleDouble_.createCircleDouble_(new FlatBufferBuilder(), x, y, radius);
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putDouble(radius);
        buffer.putDouble(y);
        buffer.putDouble(x);
        buffer.flip();
        circleDouble_.__init(0, buffer);
        CircleDouble_.Vector vector = new CircleDouble_.Vector();
        vector.__assign(0, 24, buffer);
        CircleDouble_ obj = new CircleDouble_();
        // When
        CircleDouble_ result = vector.get(obj, 0);
        // Then
        assertNotNull(result);
        assertEquals(x, result.x(), 0.001);
        assertEquals(y, result.y(), 0.001);
        assertEquals(radius, result.radius(), 0.001);
    }
}
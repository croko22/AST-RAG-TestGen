import com.github.davidmoten.rtree.fbs.generated.PointDouble_;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PointDouble_Test {

    @Mock
    private ByteBuffer byteBuffer;

    @Mock
    private FlatBufferBuilder flatBufferBuilder;

    private PointDouble_ pointDouble_;

    @BeforeEach
    public void setup() {
        pointDouble_ = new PointDouble_();
    }

    @AfterEach
    public void tearDown() {
        pointDouble_ = null;
    }

    @Test
    public void test__init() {
        // Given
        int index = 10;
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        byteBuffer.order(ByteOrder.nativeOrder());

        // When
        pointDouble_.__init(index, byteBuffer);

        // Then
        // No assertions, as this method does not return any value
    }

    @Test
    public void test__assign() {
        // Given
        int index = 10;
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        byteBuffer.order(ByteOrder.nativeOrder());

        // When
        PointDouble_ assignedPointDouble = pointDouble_.__assign(index, byteBuffer);

        // Then
        assertNotNull(assignedPointDouble);
        assertSame(pointDouble_, assignedPointDouble);
    }

    @Test
    public void testX() {
        // Given
        double x = 10.5;
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        byteBuffer.order(ByteOrder.nativeOrder());
        byteBuffer.putDouble(x);
        byteBuffer.putDouble(20.5);
        byteBuffer.flip();

        pointDouble_.__init(0, byteBuffer);

        // When
        double result = pointDouble_.x();

        // Then
        assertEquals(x, result, 0.001);
    }

    @Test
    public void testY() {
        // Given
        double y = 20.5;
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        byteBuffer.order(ByteOrder.nativeOrder());
        byteBuffer.putDouble(10.5);
        byteBuffer.putDouble(y);
        byteBuffer.flip();

        pointDouble_.__init(0, byteBuffer);

        // When
        double result = pointDouble_.y();

        // Then
        assertEquals(y, result, 0.001);
    }

    @Test
    public void testCreatePointDouble() {
        // Given
        double x = 10.5;
        double y = 20.5;
        FlatBufferBuilder flatBufferBuilder = new FlatBufferBuilder(16);

        // When
        int offset = PointDouble_.createPointDouble_(flatBufferBuilder, x, y);

        // Then
        assertNotNull(flatBufferBuilder);
        assertEquals(8, offset);
    }
}
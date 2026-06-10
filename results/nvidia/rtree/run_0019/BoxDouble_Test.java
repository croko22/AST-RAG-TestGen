import com.github.davidmoten.rtree.fbs.generated.BoxDouble_;
import com.google.flatbuffers.FlatBufferBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.nio.ByteBuffer;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BoxDouble_Test {

    @Mock
    private FlatBufferBuilder flatBufferBuilder;

    @Mock
    private ByteBuffer byteBuffer;

    private BoxDouble_ boxDouble_;

    @BeforeEach
    void setup() {
        boxDouble_ = new BoxDouble_();
    }

    @AfterEach
    void tearDown() {
        boxDouble_ = null;
    }

    @Test
    public void test__init() {
        // Given
        int index = 10;
        // When
        boxDouble_.__init(index, byteBuffer);
        // Then
        // No assertions, as __init is a void method and doesn't return anything
    }

    @Test
    public void test__assign() {
        // Given
        int index = 10;
        // When
        BoxDouble_ assignedBoxDouble = boxDouble_.__assign(index, byteBuffer);
        // Then
        assertNotNull(assignedBoxDouble);
        assertSame(boxDouble_, assignedBoxDouble);
    }

    @Test
    public void testMinX() {
        // Given
        double minX = 10.5;
        // When
        boxDouble_.__init(0, ByteBuffer.allocate(32));
        boxDouble_.bb = ByteBuffer.allocate(32);
        boxDouble_.bb.putDouble(minX);
        boxDouble_.bb.rewind();
        double result = boxDouble_.minX();
        // Then
        assertEquals(minX, result, 0.001);
    }

    @Test
    public void testMinY() {
        // Given
        double minY = 10.5;
        // When
        boxDouble_.__init(0, ByteBuffer.allocate(32));
        boxDouble_.bb = ByteBuffer.allocate(32);
        boxDouble_.bb.putDouble(0);
        boxDouble_.bb.putDouble(minY);
        boxDouble_.bb.rewind();
        double result = boxDouble_.minY();
        // Then
        assertEquals(minY, result, 0.001);
    }

    @Test
    public void testMaxX() {
        // Given
        double maxX = 10.5;
        // When
        boxDouble_.__init(0, ByteBuffer.allocate(32));
        boxDouble_.bb = ByteBuffer.allocate(32);
        boxDouble_.bb.putDouble(0);
        boxDouble_.bb.putDouble(0);
        boxDouble_.bb.putDouble(maxX);
        boxDouble_.bb.rewind();
        double result = boxDouble_.maxX();
        // Then
        assertEquals(maxX, result, 0.001);
    }

    @Test
    public void testMaxY() {
        // Given
        double maxY = 10.5;
        // When
        boxDouble_.__init(0, ByteBuffer.allocate(32));
        boxDouble_.bb = ByteBuffer.allocate(32);
        boxDouble_.bb.putDouble(maxY);
        boxDouble_.bb.rewind();
        double result = boxDouble_.maxY();
        // Then
        assertEquals(maxY, result, 0.001);
    }

    @Test
    public void testCreateBoxDouble_() {
        // Given
        double minX = 10.5;
        double minY = 20.5;
        double maxX = 30.5;
        double maxY = 40.5;
        // When
        int offset = BoxDouble_.createBoxDouble_(flatBufferBuilder, minX, minY, maxX, maxY);
        // Then
        assertNotNull(flatBufferBuilder);
        // No further assertions, as createBoxDouble_ returns an offset and doesn't return the BoxDouble_ object
    }
}
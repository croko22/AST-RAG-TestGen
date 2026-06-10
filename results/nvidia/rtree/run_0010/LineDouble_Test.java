import com.github.davidmoten.rtree.fbs.generated.LineDouble_;
import com.google.flatbuffers.FlatBufferBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.nio.ByteBuffer;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LineDoubleTest {

    @Mock
    private FlatBufferBuilder builder;

    @Mock
    private ByteBuffer byteBuffer;

    private LineDouble_ lineDouble;

    @BeforeEach
    public void setup() {
        lineDouble = new LineDouble_();
    }

    @AfterEach
    public void tearDown() {
        lineDouble = null;
    }

    @Test
    public void testMinX() {
        // Given
        double minX = 10.0;
        double minY = 20.0;
        double maxX = 30.0;
        double maxY = 40.0;
        int offset = LineDouble_.createLineDouble_(new FlatBufferBuilder(1024), minX, minY, maxX, maxY);

        // When
        LineDouble_ lineDouble = new LineDouble_();
        lineDouble.__init(offset, ByteBuffer.wrap(new byte[1024]));

        // Then
        assertEquals(minX, lineDouble.minX(), 0.001);
    }

    @Test
    public void testMinY() {
        // Given
        double minX = 10.0;
        double minY = 20.0;
        double maxX = 30.0;
        double maxY = 40.0;
        int offset = LineDouble_.createLineDouble_(new FlatBufferBuilder(1024), minX, minY, maxX, maxY);

        // When
        LineDouble_ lineDouble = new LineDouble_();
        lineDouble.__init(offset, ByteBuffer.wrap(new byte[1024]));

        // Then
        assertEquals(minY, lineDouble.minY(), 0.001);
    }

    @Test
    public void testMaxX() {
        // Given
        double minX = 10.0;
        double minY = 20.0;
        double maxX = 30.0;
        double maxY = 40.0;
        int offset = LineDouble_.createLineDouble_(new FlatBufferBuilder(1024), minX, minY, maxX, maxY);

        // When
        LineDouble_ lineDouble = new LineDouble_();
        lineDouble.__init(offset, ByteBuffer.wrap(new byte[1024]));

        // Then
        assertEquals(maxX, lineDouble.maxX(), 0.001);
    }

    @Test
    public void testMaxY() {
        // Given
        double minX = 10.0;
        double minY = 20.0;
        double maxX = 30.0;
        double maxY = 40.0;
        int offset = LineDouble_.createLineDouble_(new FlatBufferBuilder(1024), minX, minY, maxX, maxY);

        // When
        LineDouble_ lineDouble = new LineDouble_();
        lineDouble.__init(offset, ByteBuffer.wrap(new byte[1024]));

        // Then
        assertEquals(maxY, lineDouble.maxY(), 0.001);
    }

    @Test
    public void testCreateLineDouble() {
        // Given
        double minX = 10.0;
        double minY = 20.0;
        double maxX = 30.0;
        double maxY = 40.0;

        // When
        int offset = LineDouble_.createLineDouble_(new FlatBufferBuilder(1024), minX, minY, maxX, maxY);

        // Then
        assertNotEquals(0, offset);
    }

    @Test
    public void testAssign() {
        // Given
        double minX = 10.0;
        double minY = 20.0;
        double maxX = 30.0;
        double maxY = 40.0;
        int offset = LineDouble_.createLineDouble_(new FlatBufferBuilder(1024), minX, minY, maxX, maxY);

        // When
        LineDouble_ lineDouble = new LineDouble_();
        lineDouble.__assign(offset, ByteBuffer.wrap(new byte[1024]));

        // Then
        assertNotNull(lineDouble);
    }

    @Test
    public void testGet() {
        // Given
        double minX = 10.0;
        double minY = 20.0;
        double maxX = 30.0;
        double maxY = 40.0;
        int offset = LineDouble_.createLineDouble_(new FlatBufferBuilder(1024), minX, minY, maxX, maxY);

        // When
        LineDouble_ lineDouble = new LineDouble_();
        LineDouble_ result = lineDouble.get(new LineDouble_(), 0);

        // Then
        assertNotNull(result);
    }
}
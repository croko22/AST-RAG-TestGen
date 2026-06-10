import com.github.davidmoten.rtree.fbs.generated.PointFloat_;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PointFloat_Test {

    @Mock
    private ByteBuffer byteBuffer;

    @BeforeEach
    public void setup() {
        // Initialize the ByteBuffer with a known state
        byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.order(ByteOrder.nativeOrder());
    }

    @AfterEach
    public void tearDown() {
        // Reset the ByteBuffer after each test
        byteBuffer.clear();
    }

    @Test
    public void test__init() {
        // Given: a valid index and ByteBuffer
        int index = 0;
        byteBuffer.putFloat(0.0f);
        byteBuffer.putFloat(1.0f);
        byteBuffer.flip();

        // When: __init is called
        PointFloat_ pointFloat = new PointFloat_();
        pointFloat.__init(index, byteBuffer);

        // Then: the PointFloat_ object is initialized correctly
        assertEquals(0.0f, pointFloat.x(), 0.001f);
        assertEquals(1.0f, pointFloat.y(), 0.001f);
    }

    @Test
    public void test__assign() {
        // Given: a valid index and ByteBuffer
        int index = 0;
        byteBuffer.putFloat(0.0f);
        byteBuffer.putFloat(1.0f);
        byteBuffer.flip();

        // When: __assign is called
        PointFloat_ pointFloat = new PointFloat_();
        PointFloat_ assignedPointFloat = pointFloat.__assign(index, byteBuffer);

        // Then: the PointFloat_ object is assigned correctly
        assertEquals(0.0f, assignedPointFloat.x(), 0.001f);
        assertEquals(1.0f, assignedPointFloat.y(), 0.001f);
    }

    @Test
    public void testX() {
        // Given: a valid PointFloat_ object
        PointFloat_ pointFloat = new PointFloat_();
        pointFloat.__init(0, ByteBuffer.allocate(8));
        pointFloat.__assign(0, ByteBuffer.allocate(8).putFloat(0.0f).putFloat(1.0f));

        // When: x() is called
        float x = pointFloat.x();

        // Then: the x value is returned correctly
        assertEquals(0.0f, x, 0.001f);
    }

    @Test
    public void testY() {
        // Given: a valid PointFloat_ object
        PointFloat_ pointFloat = new PointFloat_();
        pointFloat.__init(0, ByteBuffer.allocate(8));
        pointFloat.__assign(0, ByteBuffer.allocate(8).putFloat(0.0f).putFloat(1.0f));

        // When: y() is called
        float y = pointFloat.y();

        // Then: the y value is returned correctly
        assertEquals(1.0f, y, 0.001f);
    }

    @Test
    public void testCreatePointFloat_() {
        // Given: a valid FlatBufferBuilder and x, y values
        com.google.flatbuffers.FlatBufferBuilder builder = new com.google.flatbuffers.FlatBufferBuilder();
        float x = 0.0f;
        float y = 1.0f;

        // When: createPointFloat_ is called
        int offset = PointFloat_.createPointFloat_(builder, x, y);

        // Then: the PointFloat_ object is created correctly
        PointFloat_.Vector vector = new PointFloat_.Vector();
        vector.__assign(offset, 8, builder.dataBuffer());
        PointFloat_ pointFloat = vector.get(0);
        assertEquals(x, pointFloat.x(), 0.001f);
        assertEquals(y, pointFloat.y(), 0.001f);
    }

    @Test
    public void testGet() {
        // Given: a valid PointFloat_ object and index
        PointFloat_ pointFloat = new PointFloat_();
        pointFloat.__init(0, ByteBuffer.allocate(8));
        pointFloat.__assign(0, ByteBuffer.allocate(8).putFloat(0.0f).putFloat(1.0f));
        int index = 0;

        // When: get() is called
        PointFloat_ gottenPointFloat = pointFloat.get(index);

        // Then: the PointFloat_ object is returned correctly
        assertEquals(0.0f, gottenPointFloat.x(), 0.001f);
        assertEquals(1.0f, gottenPointFloat.y(), 0.001f);
    }

    @Test
    public void testGetWithObject() {
        // Given: a valid PointFloat_ object, another PointFloat_ object, and index
        PointFloat_ pointFloat = new PointFloat_();
        pointFloat.__init(0, ByteBuffer.allocate(8));
        pointFloat.__assign(0, ByteBuffer.allocate(8).putFloat(0.0f).putFloat(1.0f));
        PointFloat_ anotherPointFloat = new PointFloat_();
        int index = 0;

        // When: get() is called with another PointFloat_ object
        PointFloat_ gottenPointFloat = pointFloat.get(anotherPointFloat, index);

        // Then: the PointFloat_ object is returned correctly
        assertEquals(0.0f, gottenPointFloat.x(), 0.001f);
        assertEquals(1.0f, gottenPointFloat.y(), 0.001f);
    }
}
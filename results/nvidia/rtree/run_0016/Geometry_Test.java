import com.github.davidmoten.rtree.fbs.generated.Geometry_;
import com.github.davidmoten.rtree.fbs.generated.BoxFloat_;
import com.github.davidmoten.rtree.fbs.generated.PointFloat_;
import com.github.davidmoten.rtree.fbs.generated.CircleFloat_;
import com.github.davidmoten.rtree.fbs.generated.BoxDouble_;
import com.github.davidmoten.rtree.fbs.generated.PointDouble_;
import com.github.davidmoten.rtree.fbs.generated.CircleDouble_;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GeometryTest {

    @BeforeEach
    public void setup() {
        Geometry_.ValidateVersion();
    }

    @Test
    public void testValidateVersion() {
        // ValidateVersion is a static method and does not throw any exceptions
        assertDoesNotThrow(Geometry_::ValidateVersion);
    }

    @Test
    public void testGetRootAsGeometry_ByteBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        Geometry_ geometry = Geometry_.getRootAsGeometry_(byteBuffer);
        assertNotNull(geometry);
    }

    @Test
    public void testGetRootAsGeometry_ByteBufferGeometry() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        Geometry_ geometry = new Geometry_();
        Geometry_ result = Geometry_.getRootAsGeometry_(byteBuffer, geometry);
        assertNotNull(result);
    }

    @Test
    public void testInit() {
        Geometry_ geometry = new Geometry_();
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        geometry.__init(0, byteBuffer);
    }

    @Test
    public void testAssign() {
        Geometry_ geometry = new Geometry_();
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        Geometry_ result = geometry.__assign(0, byteBuffer);
        assertNotNull(result);
    }

    @Test
    public void testType() {
        Geometry_ geometry = new Geometry_();
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        geometry.__assign(0, byteBuffer);
        byte type = geometry.type();
        assertEquals(0, type);
    }

    @Test
    public void testStartGeometry() {
        FlatBufferBuilder builder = new FlatBufferBuilder(10);
        Geometry_.startGeometry_(builder);
    }

    @Test
    public void testAddType() {
        FlatBufferBuilder builder = new FlatBufferBuilder(10);
        Geometry_.startGeometry_(builder);
        Geometry_.addType(builder, (byte) 1);
    }

    @Test
    public void testAddBoxFloat() {
        FlatBufferBuilder builder = new FlatBufferBuilder(10);
        Geometry_.startGeometry_(builder);
        int boxFloatOffset = 1;
        Geometry_.addBoxFloat(builder, boxFloatOffset);
    }

    @Test
    public void testAddPointFloat() {
        FlatBufferBuilder builder = new FlatBufferBuilder(10);
        Geometry_.startGeometry_(builder);
        int pointFloatOffset = 1;
        Geometry_.addPointFloat(builder, pointFloatOffset);
    }

    @Test
    public void testAddCircleFloat() {
        FlatBufferBuilder builder = new FlatBufferBuilder(10);
        Geometry_.startGeometry_(builder);
        int circleFloatOffset = 1;
        Geometry_.addCircleFloat(builder, circleFloatOffset);
    }

    @Test
    public void testAddLineFloat() {
        FlatBufferBuilder builder = new FlatBufferBuilder(10);
        Geometry_.startGeometry_(builder);
        int lineFloatOffset = 1;
        Geometry_.addLineFloat(builder, lineFloatOffset);
    }

    @Test
    public void testAddBoxDouble() {
        FlatBufferBuilder builder = new FlatBufferBuilder(10);
        Geometry_.startGeometry_(builder);
        int boxDoubleOffset = 1;
        Geometry_.addBoxDouble(builder, boxDoubleOffset);
    }

    @Test
    public void testAddPointDouble() {
        FlatBufferBuilder builder = new FlatBufferBuilder(10);
        Geometry_.startGeometry_(builder);
        int pointDoubleOffset = 1;
        Geometry_.addPointDouble(builder, pointDoubleOffset);
    }

    @Test
    public void testAddCircleDouble() {
        FlatBufferBuilder builder = new FlatBufferBuilder(10);
        Geometry_.startGeometry_(builder);
        int circleDoubleOffset = 1;
        Geometry_.addCircleDouble(builder, circleDoubleOffset);
    }

    @Test
    public void testAddLineDouble() {
        FlatBufferBuilder builder = new FlatBufferBuilder(10);
        Geometry_.startGeometry_(builder);
        int lineDoubleOffset = 1;
        Geometry_.addLineDouble(builder, lineDoubleOffset);
    }

    @Test
    public void testEndGeometry() {
        FlatBufferBuilder builder = new FlatBufferBuilder(10);
        Geometry_.startGeometry_(builder);
        int result = Geometry_.endGeometry_(builder);
        assertNotEquals(0, result);
    }

    @Test
    public void testAssignVector() {
        Geometry_ geometry = new Geometry_();
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        geometry.__assign(0, byteBuffer);
        Geometry_.Vector vector = geometry.__assign(0, 1, byteBuffer);
        assertNotNull(vector);
    }

    @Test
    public void testGetVector() {
        Geometry_ geometry = new Geometry_();
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        geometry.__assign(0, byteBuffer);
        Geometry_ result = geometry.get(0);
        assertNotNull(result);
    }

    @Test
    public void testGetVectorGeometry() {
        Geometry_ geometry = new Geometry_();
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        geometry.__assign(0, byteBuffer);
        Geometry_ obj = new Geometry_();
        Geometry_ result = geometry.get(obj, 0);
        assertNotNull(result);
    }
}
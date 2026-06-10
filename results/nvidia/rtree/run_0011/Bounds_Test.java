import com.github.davidmoten.rtree.fbs.generated.Bounds_;
import com.github.davidmoten.rtree.fbs.generated.BoxDouble_;
import com.github.davidmoten.rtree.fbs.generated.BoxFloat_;
import com.google.flatbuffers.FlatBufferBuilder;
import org.junit.jupiter.api.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class Bounds_Test {

    @Test
    public void testValidateVersion() {
        // Given: no preconditions
        // When: ValidateVersion is called
        Bounds_.ValidateVersion();
        // Then: no exceptions are thrown
    }

    @Test
    public void testGetRootAsBounds_ByteBuffer() {
        // Given: a valid ByteBuffer
        ByteBuffer bb = ByteBuffer.allocate(1024);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        // When: getRootAsBounds_ is called
        Bounds_ bounds = Bounds_.getRootAsBounds_(bb);
        // Then: the result is not null
        assertNotNull(bounds);
    }

    @Test
    public void testGetRootAsBounds_ByteBufferBounds() {
        // Given: a valid ByteBuffer and a Bounds_ object
        ByteBuffer bb = ByteBuffer.allocate(1024);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        Bounds_ bounds = new Bounds_();
        // When: getRootAsBounds_ is called
        Bounds_ result = Bounds_.getRootAsBounds_(bb, bounds);
        // Then: the result is the same as the input Bounds_ object
        assertSame(bounds, result);
    }

    @Test
    public void test__init() {
        // Given: a valid Bounds_ object, index, and ByteBuffer
        Bounds_ bounds = new Bounds_();
        int index = 0;
        ByteBuffer bb = ByteBuffer.allocate(1024);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        // When: __init is called
        bounds.__init(index, bb);
        // Then: no exceptions are thrown
    }

    @Test
    public void test__assign() {
        // Given: a valid Bounds_ object, index, and ByteBuffer
        Bounds_ bounds = new Bounds_();
        int index = 0;
        ByteBuffer bb = ByteBuffer.allocate(1024);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        // When: __assign is called
        Bounds_ result = bounds.__assign(index, bb);
        // Then: the result is the same as the input Bounds_ object
        assertSame(bounds, result);
    }

    @Test
    public void testType() {
        // Given: a valid Bounds_ object
        Bounds_ bounds = new Bounds_();
        // When: type is called
        byte result = bounds.type();
        // Then: the result is a valid byte value
        assertTrue(result >= 0 && result <= 127);
    }

    @Test
    public void testStartBounds_() {
        // Given: a valid FlatBufferBuilder
        FlatBufferBuilder builder = new FlatBufferBuilder(1024);
        // When: startBounds_ is called
        Bounds_.startBounds_(builder);
        // Then: no exceptions are thrown
    }

    @Test
    public void testAddType() {
        // Given: a valid FlatBufferBuilder and a byte value
        FlatBufferBuilder builder = new FlatBufferBuilder(1024);
        byte type = 0;
        // When: addType is called
        Bounds_.addType(builder, type);
        // Then: no exceptions are thrown
    }

    @Test
    public void testAddBoxFloat() {
        // Given: a valid FlatBufferBuilder and a BoxFloat_ offset
        FlatBufferBuilder builder = new FlatBufferBuilder(1024);
        int offset = 0;
        // When: addBoxFloat is called
        Bounds_.addBoxFloat(builder, offset);
        // Then: no exceptions are thrown
    }

    @Test
    public void testAddBoxDouble() {
        // Given: a valid FlatBufferBuilder and a BoxDouble_ offset
        FlatBufferBuilder builder = new FlatBufferBuilder(1024);
        int offset = 0;
        // When: addBoxDouble is called
        Bounds_.addBoxDouble(builder, offset);
        // Then: no exceptions are thrown
    }

    @Test
    public void testEndBounds_() {
        // Given: a valid FlatBufferBuilder
        FlatBufferBuilder builder = new FlatBufferBuilder(1024);
        // When: endBounds_ is called
        int result = Bounds_.endBounds_(builder);
        // Then: the result is a valid integer value
        assertTrue(result >= 0);
    }

    @Test
    public void test__assign_Vector() {
        // Given: a valid Bounds_ object, vector, element size, and ByteBuffer
        Bounds_ bounds = new Bounds_();
        int vector = 0;
        int elementSize = 0;
        ByteBuffer bb = ByteBuffer.allocate(1024);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        // When: __assign is called
        Bounds_.Vector result = bounds.__assign(vector, elementSize, bb);
        // Then: the result is not null
        assertNotNull(result);
    }

    @Test
    public void testGet() {
        // Given: a valid Bounds_ object and an index
        Bounds_ bounds = new Bounds_();
        int index = 0;
        // When: get is called
        Bounds_ result = bounds.get(index);
        // Then: the result is not null
        assertNotNull(result);
    }

    @Test
    public void testGet_Bounds() {
        // Given: a valid Bounds_ object, another Bounds_ object, and an index
        Bounds_ bounds = new Bounds_();
        Bounds_ otherBounds = new Bounds_();
        int index = 0;
        // When: get is called
        Bounds_ result = bounds.get(otherBounds, index);
        // Then: the result is the same as the input otherBounds object
        assertSame(otherBounds, result);
    }
}
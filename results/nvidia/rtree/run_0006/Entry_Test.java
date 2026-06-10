import com.github.davidmoten.rtree.fbs.generated.Entry_;
import com.github.davidmoten.rtree.fbs.generated.Geometry_;
import com.google.flatbuffers.FlatBufferBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EntryTest {

    @Mock
    private Geometry_ geometry;

    @Mock
    private FlatBufferBuilder builder;

    @BeforeEach
    void setup() {
        // Initialize any necessary objects or mocks here
    }

    @AfterEach
    void tearDown() {
        // Clean up any resources used in the tests here
    }

    @Test
    public void testValidateVersion() {
        // Given: no specific setup needed
        // When: ValidateVersion is called
        Entry_.ValidateVersion();
        // Then: no specific assertions needed, just verify that the method call doesn't throw any exceptions
    }

    @Test
    public void testGetRootAsEntry_ByteBuffer() {
        // Given: a valid ByteBuffer
        ByteBuffer bb = ByteBuffer.allocate(10);
        // When: getRootAsEntry_ is called with the ByteBuffer
        Entry_ entry = Entry_.getRootAsEntry_(bb);
        // Then: verify that the returned Entry_ is not null
        assertNotNull(entry);
    }

    @Test
    public void testGetRootAsEntry_ByteBufferEntry() {
        // Given: a valid ByteBuffer and an Entry_ object
        ByteBuffer bb = ByteBuffer.allocate(10);
        Entry_ entry = new Entry_();
        // When: getRootAsEntry_ is called with the ByteBuffer and Entry_ object
        Entry_ result = Entry_.getRootAsEntry_(bb, entry);
        // Then: verify that the returned Entry_ is the same as the input Entry_
        assertSame(entry, result);
    }

    @Test
    public void testInit() {
        // Given: a valid Entry_ object, index, and ByteBuffer
        Entry_ entry = new Entry_();
        int index = 0;
        ByteBuffer bb = ByteBuffer.allocate(10);
        // When: __init is called with the index and ByteBuffer
        entry.__init(index, bb);
        // Then: no specific assertions needed, just verify that the method call doesn't throw any exceptions
    }

    @Test
    public void testAssign() {
        // Given: a valid Entry_ object, index, and ByteBuffer
        Entry_ entry = new Entry_();
        int index = 0;
        ByteBuffer bb = ByteBuffer.allocate(10);
        // When: __assign is called with the index and ByteBuffer
        Entry_ result = entry.__assign(index, bb);
        // Then: verify that the returned Entry_ is the same as the input Entry_
        assertSame(entry, result);
    }

    @Test
    public void testObject() {
        // Given: a valid Entry_ object and index
        Entry_ entry = new Entry_();
        int index = 0;
        // When: object is called with the index
        byte result = entry.object(index);
        // Then: verify that the returned byte value is within the expected range
        assertTrue(result >= 0 && result <= 255);
    }

    @Test
    public void testObjectLength() {
        // Given: a valid Entry_ object
        Entry_ entry = new Entry_();
        // When: objectLength is called
        int result = entry.objectLength();
        // Then: verify that the returned length is a non-negative integer
        assertTrue(result >= 0);
    }

    @Test
    public void testObjectVector() {
        // Given: a valid Entry_ object
        Entry_ entry = new Entry_();
        // When: objectVector is called
        ByteVector result = entry.objectVector();
        // Then: verify that the returned ByteVector is not null
        assertNotNull(result);
    }

    @Test
    public void testObjectVectorByteVector() {
        // Given: a valid Entry_ object and ByteVector
        Entry_ entry = new Entry_();
        ByteVector vector = new ByteVector();
        // When: objectVector is called with the ByteVector
        ByteVector result = entry.objectVector(vector);
        // Then: verify that the returned ByteVector is the same as the input ByteVector
        assertSame(vector, result);
    }

    @Test
    public void testObjectAsByteBuffer() {
        // Given: a valid Entry_ object
        Entry_ entry = new Entry_();
        // When: objectAsByteBuffer is called
        ByteBuffer result = entry.objectAsByteBuffer();
        // Then: verify that the returned ByteBuffer is not null
        assertNotNull(result);
    }

    @Test
    public void testObjectInByteBuffer() {
        // Given: a valid Entry_ object and ByteBuffer
        Entry_ entry = new Entry_();
        ByteBuffer bb = ByteBuffer.allocate(10);
        // When: objectInByteBuffer is called with the ByteBuffer
        ByteBuffer result = entry.objectInByteBuffer(bb);
        // Then: verify that the returned ByteBuffer is not null
        assertNotNull(result);
    }

    @Test
    public void testCreateEntry() {
        // Given: a valid FlatBufferBuilder, geometry offset, and object offset
        FlatBufferBuilder builder = new FlatBufferBuilder();
        int geometryOffset = 0;
        int objectOffset = 0;
        // When: createEntry_ is called with the builder, geometry offset, and object offset
        int result = Entry_.createEntry_(builder, geometryOffset, objectOffset);
        // Then: verify that the returned offset is a non-negative integer
        assertTrue(result >= 0);
    }

    @Test
    public void testStartEntry() {
        // Given: a valid FlatBufferBuilder
        FlatBufferBuilder builder = new FlatBufferBuilder();
        // When: startEntry_ is called with the builder
        Entry_.startEntry_(builder);
        // Then: no specific assertions needed, just verify that the method call doesn't throw any exceptions
    }

    @Test
    public void testAddGeometry() {
        // Given: a valid FlatBufferBuilder and geometry offset
        FlatBufferBuilder builder = new FlatBufferBuilder();
        int geometryOffset = 0;
        // When: addGeometry is called with the builder and geometry offset
        Entry_.addGeometry(builder, geometryOffset);
        // Then: no specific assertions needed, just verify that the method call doesn't throw any exceptions
    }

    @Test
    public void testAddObject() {
        // Given: a valid FlatBufferBuilder and object offset
        FlatBufferBuilder builder = new FlatBufferBuilder();
        int objectOffset = 0;
        // When: addObject is called with the builder and object offset
        Entry_.addObject(builder, objectOffset);
        // Then: no specific assertions needed, just verify that the method call doesn't throw any exceptions
    }

    @Test
    public void testCreateObjectVectorByteArray() {
        // Given: a valid FlatBufferBuilder and byte array
        FlatBufferBuilder builder = new FlatBufferBuilder();
        byte[] data = new byte[10];
        // When: createObjectVector is called with the builder and byte array
        int result = Entry_.createObjectVector(builder, data);
        // Then: verify that the returned offset is a non-negative integer
        assertTrue(result >= 0);
    }

    @Test
    public void testCreateObjectVectorByteBuffer() {
        // Given: a valid FlatBufferBuilder and ByteBuffer
        FlatBufferBuilder builder = new FlatBufferBuilder();
        ByteBuffer data = ByteBuffer.allocate(10);
        // When: createObjectVector is called with the builder and ByteBuffer
        int result = Entry_.createObjectVector(builder, data);
        // Then: verify that the returned offset is a non-negative integer
        assertTrue(result >= 0);
    }

    @Test
    public void testStartObjectVector() {
        // Given: a valid FlatBufferBuilder and number of elements
        FlatBufferBuilder builder = new FlatBufferBuilder();
        int numElems = 10;
        // When: startObjectVector is called with the builder and number of elements
        Entry_.startObjectVector(builder, numElems);
        // Then: no specific assertions needed, just verify that the method call doesn't throw any exceptions
    }

    @Test
    public void testEndEntry() {
        // Given: a valid FlatBufferBuilder
        FlatBufferBuilder builder = new FlatBufferBuilder();
        // When: endEntry_ is called with the builder
        int result = Entry_.endEntry_(builder);
        // Then: verify that the returned offset is a non-negative integer
        assertTrue(result >= 0);
    }
}
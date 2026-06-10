import com.github.davidmoten.rtree.fbs.generated.Context_;
import com.github.davidmoten.rtree.fbs.generated.Bounds_;
import com.google.flatbuffers.FlatBufferBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ContextTest {

    @BeforeEach
    public void setup() {
        // Setup any necessary mocks or objects before each test
    }

    @AfterEach
    public void tearDown() {
        // Clean up any resources after each test
    }

    @Test
    public void testValidateVersion() {
        // Given: No specific setup needed
        // When: Validate version is called
        Context_.ValidateVersion();
        // Then: No exception is thrown
    }

    @Test
    public void testGetRootAsContext_ByteBuffer() {
        // Given: A valid ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // When: getRootAsContext_ is called
        Context_ context = Context_.getRootAsContext_(byteBuffer);
        // Then: A Context_ object is returned
        assertNotNull(context);
    }

    @Test
    public void testGetRootAsContext_ByteBufferContext() {
        // Given: A valid ByteBuffer and Context_ object
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        Context_ context = new Context_();
        // When: getRootAsContext_ is called
        Context_ result = Context_.getRootAsContext_(byteBuffer, context);
        // Then: The same Context_ object is returned
        assertSame(context, result);
    }

    @Test
    public void testInit() {
        // Given: A valid index and ByteBuffer
        int index = 0;
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        Context_ context = new Context_();
        // When: __init is called
        context.__init(index, byteBuffer);
        // Then: No exception is thrown
    }

    @Test
    public void testAssign() {
        // Given: A valid index and ByteBuffer
        int index = 0;
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        Context_ context = new Context_();
        // When: __assign is called
        Context_ result = context.__assign(index, byteBuffer);
        // Then: The same Context_ object is returned
        assertSame(context, result);
    }

    @Test
    public void testMinChildren() {
        // Given: A valid Context_ object
        Context_ context = new Context_();
        // When: minChildren is called
        int result = context.minChildren();
        // Then: A default value of 0 is returned
        assertEquals(0, result);
    }

    @Test
    public void testMaxChildren() {
        // Given: A valid Context_ object
        Context_ context = new Context_();
        // When: maxChildren is called
        int result = context.maxChildren();
        // Then: A default value of 0 is returned
        assertEquals(0, result);
    }

    @Test
    public void testCreateContext() {
        // Given: A valid FlatBufferBuilder, boundsOffset, minChildren, and maxChildren
        FlatBufferBuilder builder = new FlatBufferBuilder(1024);
        int boundsOffset = 0;
        int minChildren = 1;
        int maxChildren = 2;
        // When: createContext_ is called
        int result = Context_.createContext_(builder, boundsOffset, minChildren, maxChildren);
        // Then: A valid offset is returned
        assertTrue(result > 0);
    }

    @Test
    public void testStartContext() {
        // Given: A valid FlatBufferBuilder
        FlatBufferBuilder builder = new FlatBufferBuilder(1024);
        // When: startContext_ is called
        Context_.startContext_(builder);
        // Then: No exception is thrown
    }

    @Test
    public void testAddBounds() {
        // Given: A valid FlatBufferBuilder and boundsOffset
        FlatBufferBuilder builder = new FlatBufferBuilder(1024);
        int boundsOffset = 0;
        // When: addBounds is called
        Context_.addBounds(builder, boundsOffset);
        // Then: No exception is thrown
    }

    @Test
    public void testAddMinChildren() {
        // Given: A valid FlatBufferBuilder and minChildren
        FlatBufferBuilder builder = new FlatBufferBuilder(1024);
        int minChildren = 1;
        // When: addMinChildren is called
        Context_.addMinChildren(builder, minChildren);
        // Then: No exception is thrown
    }

    @Test
    public void testAddMaxChildren() {
        // Given: A valid FlatBufferBuilder and maxChildren
        FlatBufferBuilder builder = new FlatBufferBuilder(1024);
        int maxChildren = 2;
        // When: addMaxChildren is called
        Context_.addMaxChildren(builder, maxChildren);
        // Then: No exception is thrown
    }

    @Test
    public void testEndContext() {
        // Given: A valid FlatBufferBuilder
        FlatBufferBuilder builder = new FlatBufferBuilder(1024);
        Context_.startContext_(builder);
        // When: endContext_ is called
        int result = Context_.endContext_(builder);
        // Then: A valid offset is returned
        assertTrue(result > 0);
    }

    @Test
    public void testAssignVector() {
        // Given: A valid vector, element size, and ByteBuffer
        int vector = 0;
        int elementSize = 1;
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        Context_.Vector contextVector = new Context_.Vector();
        // When: __assign is called
        Context_.Vector result = contextVector.__assign(vector, elementSize, byteBuffer);
        // Then: The same Context_.Vector object is returned
        assertSame(contextVector, result);
    }

    @Test
    public void testGet() {
        // Given: A valid index
        int index = 0;
        Context_.Vector contextVector = new Context_.Vector();
        // When: get is called
        Context_ result = contextVector.get(index);
        // Then: A Context_ object is returned
        assertNotNull(result);
    }

    @Test
    public void testGetContext() {
        // Given: A valid Context_ object and index
        int index = 0;
        Context_ context = new Context_();
        Context_.Vector contextVector = new Context_.Vector();
        // When: get is called
        Context_ result = contextVector.get(context, index);
        // Then: The same Context_ object is returned
        assertSame(context, result);
    }
}
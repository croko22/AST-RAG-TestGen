import com.github.davidmoten.rtree.fbs.generated.Tree_;
import com.google.flatbuffers.FlatBufferBuilder;
import org.junit.jupiter.api.*;
import java.nio.ByteBuffer;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class Tree_Test {

    @Test
    public void testValidateVersion() {
        // Given: no setup required
        // When: ValidateVersion is called
        Tree_.ValidateVersion();
        // Then: no exception is thrown
    }

    @Test
    public void testGetRootAsTree_ByteBuffer() {
        // Given: a ByteBuffer
        ByteBuffer _bb = ByteBuffer.allocate(10);
        // When: getRootAsTree_ is called
        Tree_ tree = Tree_.getRootAsTree_(_bb);
        // Then: tree is not null
        assertNotNull(tree);
    }

    @Test
    public void testGetRootAsTree_ByteBufferTree() {
        // Given: a ByteBuffer and a Tree_
        ByteBuffer _bb = ByteBuffer.allocate(10);
        Tree_ tree = new Tree_();
        // When: getRootAsTree_ is called
        Tree_ result = Tree_.getRootAsTree_(_bb, tree);
        // Then: result is not null
        assertNotNull(result);
    }

    @Test
    public void test__init() {
        // Given: an index and a ByteBuffer
        int _i = 0;
        ByteBuffer _bb = ByteBuffer.allocate(10);
        Tree_ tree = new Tree_();
        // When: __init is called
        tree.__init(_i, _bb);
        // Then: no exception is thrown
    }

    @Test
    public void test__assign() {
        // Given: an index and a ByteBuffer
        int _i = 0;
        ByteBuffer _bb = ByteBuffer.allocate(10);
        Tree_ tree = new Tree_();
        // When: __assign is called
        Tree_ result = tree.__assign(_i, _bb);
        // Then: result is not null
        assertNotNull(result);
    }

    @Test
    public void testSize() {
        // Given: a Tree_
        Tree_ tree = new Tree_();
        // When: size is called
        long size = tree.size();
        // Then: size is 0
        assertEquals(0, size);
    }

    @Test
    public void testCreateTree_() {
        // Given: a FlatBufferBuilder, contextOffset, rootOffset, and size
        FlatBufferBuilder builder = new FlatBufferBuilder(10);
        int contextOffset = 0;
        int rootOffset = 0;
        long size = 10;
        // When: createTree_ is called
        int offset = Tree_.createTree_(builder, contextOffset, rootOffset, size);
        // Then: offset is not 0
        assertNotEquals(0, offset);
    }

    @Test
    public void testStartTree_() {
        // Given: a FlatBufferBuilder
        FlatBufferBuilder builder = new FlatBufferBuilder(10);
        // When: startTree_ is called
        Tree_.startTree_(builder);
        // Then: no exception is thrown
    }

    @Test
    public void testAddContext() {
        // Given: a FlatBufferBuilder and a contextOffset
        FlatBufferBuilder builder = new FlatBufferBuilder(10);
        int contextOffset = 0;
        // When: addContext is called
        Tree_.addContext(builder, contextOffset);
        // Then: no exception is thrown
    }

    @Test
    public void testAddRoot() {
        // Given: a FlatBufferBuilder and a rootOffset
        FlatBufferBuilder builder = new FlatBufferBuilder(10);
        int rootOffset = 0;
        // When: addRoot is called
        Tree_.addRoot(builder, rootOffset);
        // Then: no exception is thrown
    }

    @Test
    public void testAddSize() {
        // Given: a FlatBufferBuilder and a size
        FlatBufferBuilder builder = new FlatBufferBuilder(10);
        long size = 10;
        // When: addSize is called
        Tree_.addSize(builder, size);
        // Then: no exception is thrown
    }

    @Test
    public void testEndTree_() {
        // Given: a FlatBufferBuilder
        FlatBufferBuilder builder = new FlatBufferBuilder(10);
        // When: endTree_ is called
        int offset = Tree_.endTree_(builder);
        // Then: offset is not 0
        assertNotEquals(0, offset);
    }

    @Test
    public void testFinishTree_Buffer() {
        // Given: a FlatBufferBuilder and an offset
        FlatBufferBuilder builder = new FlatBufferBuilder(10);
        int offset = 0;
        // When: finishTree_Buffer is called
        Tree_.finishTree_Buffer(builder, offset);
        // Then: no exception is thrown
    }

    @Test
    public void testFinishSizePrefixedTree_Buffer() {
        // Given: a FlatBufferBuilder and an offset
        FlatBufferBuilder builder = new FlatBufferBuilder(10);
        int offset = 0;
        // When: finishSizePrefixedTree_Buffer is called
        Tree_.finishSizePrefixedTree_Buffer(builder, offset);
        // Then: no exception is thrown
    }

    @Test
    public void test__assign_Vector() {
        // Given: a vector, element size, and a ByteBuffer
        int _vector = 0;
        int _element_size = 10;
        ByteBuffer _bb = ByteBuffer.allocate(10);
        Tree_.Vector vector = new Tree_.Vector();
        // When: __assign is called
        Tree_.Vector result = vector.__assign(_vector, _element_size, _bb);
        // Then: result is not null
        assertNotNull(result);
    }

    @Test
    public void testGet() {
        // Given: a Tree_ and an index
        Tree_ tree = new Tree_();
        int j = 0;
        // When: get is called
        Tree_ result = tree.get(j);
        // Then: result is not null
        assertNotNull(result);
    }

    @Test
    public void testGet_Tree() {
        // Given: a Tree_, a Tree_, and an index
        Tree_ tree = new Tree_();
        Tree_ obj = new Tree_();
        int j = 0;
        // When: get is called
        Tree_ result = tree.get(obj, j);
        // Then: result is not null
        assertNotNull(result);
    }
}
import com.github.davidmoten.rtree.fbs.generated.Node_;
import com.github.davidmoten.rtree.fbs.generated.Bounds_;
import com.github.davidmoten.rtree.fbs.generated.Entry_;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class Node_Test {

    @Test
    public void testValidateVersion() {
        // Given: no setup needed
        // When: ValidateVersion is called
        Node_.ValidateVersion();
        // Then: no exception is thrown
    }

    @Test
    public void testGetRootAsNode_ByteBuffer() {
        // Given: a ByteBuffer
        ByteBuffer _bb = ByteBuffer.allocate(10);
        // When: getRootAsNode_ is called
        Node_ node = Node_.getRootAsNode_(_bb);
        // Then: node is not null
        assertNotNull(node);
    }

    @Test
    public void testGetRootAsNode_ByteBufferNode() {
        // Given: a ByteBuffer and a Node_
        ByteBuffer _bb = ByteBuffer.allocate(10);
        Node_ node = new Node_();
        // When: getRootAsNode_ is called
        Node_ result = Node_.getRootAsNode_(_bb, node);
        // Then: result is not null
        assertNotNull(result);
    }

    @Test
    public void test__init() {
        // Given: a Node_ and a ByteBuffer
        Node_ node = new Node_();
        ByteBuffer _bb = ByteBuffer.allocate(10);
        // When: __init is called
        node.__init(0, _bb);
        // Then: no exception is thrown
    }

    @Test
    public void test__assign() {
        // Given: a Node_ and a ByteBuffer
        Node_ node = new Node_();
        ByteBuffer _bb = ByteBuffer.allocate(10);
        // When: __assign is called
        Node_ result = node.__assign(0, _bb);
        // Then: result is not null
        assertNotNull(result);
    }

    @Test
    public void testChildrenLength() {
        // Given: a Node_
        Node_ node = new Node_();
        // When: childrenLength is called
        int length = node.childrenLength();
        // Then: length is 0
        assertEquals(0, length);
    }

    @Test
    public void testEntriesLength() {
        // Given: a Node_
        Node_ node = new Node_();
        // When: entriesLength is called
        int length = node.entriesLength();
        // Then: length is 0
        assertEquals(0, length);
    }

    @Test
    public void testCreateNode() {
        // Given: a FlatBufferBuilder
        com.google.flatbuffers.FlatBufferBuilder builder = new com.google.flatbuffers.FlatBufferBuilder();
        // When: createNode_ is called
        int offset = Node_.createNode_(builder, 0, 0, 0);
        // Then: offset is not 0
        assertNotEquals(0, offset);
    }

    @Test
    public void testStartNode() {
        // Given: a FlatBufferBuilder
        com.google.flatbuffers.FlatBufferBuilder builder = new com.google.flatbuffers.FlatBufferBuilder();
        // When: startNode_ is called
        Node_.startNode_(builder);
        // Then: no exception is thrown
    }

    @Test
    public void testAddMbb() {
        // Given: a FlatBufferBuilder
        com.google.flatbuffers.FlatBufferBuilder builder = new com.google.flatbuffers.FlatBufferBuilder();
        // When: addMbb is called
        Node_.addMbb(builder, 0);
        // Then: no exception is thrown
    }

    @Test
    public void testAddChildren() {
        // Given: a FlatBufferBuilder
        com.google.flatbuffers.FlatBufferBuilder builder = new com.google.flatbuffers.FlatBufferBuilder();
        // When: addChildren is called
        Node_.addChildren(builder, 0);
        // Then: no exception is thrown
    }

    @Test
    public void testCreateChildrenVector() {
        // Given: a FlatBufferBuilder
        com.google.flatbuffers.FlatBufferBuilder builder = new com.google.flatbuffers.FlatBufferBuilder();
        // When: createChildrenVector is called
        int offset = Node_.createChildrenVector(builder, new int[0]);
        // Then: offset is not 0
        assertNotEquals(0, offset);
    }

    @Test
    public void testStartChildrenVector() {
        // Given: a FlatBufferBuilder
        com.google.flatbuffers.FlatBufferBuilder builder = new com.google.flatbuffers.FlatBufferBuilder();
        // When: startChildrenVector is called
        Node_.startChildrenVector(builder, 0);
        // Then: no exception is thrown
    }

    @Test
    public void testAddEntries() {
        // Given: a FlatBufferBuilder
        com.google.flatbuffers.FlatBufferBuilder builder = new com.google.flatbuffers.FlatBufferBuilder();
        // When: addEntries is called
        Node_.addEntries(builder, 0);
        // Then: no exception is thrown
    }

    @Test
    public void testCreateEntriesVector() {
        // Given: a FlatBufferBuilder
        com.google.flatbuffers.FlatBufferBuilder builder = new com.google.flatbuffers.FlatBufferBuilder();
        // When: createEntriesVector is called
        int offset = Node_.createEntriesVector(builder, new int[0]);
        // Then: offset is not 0
        assertNotEquals(0, offset);
    }

    @Test
    public void testStartEntriesVector() {
        // Given: a FlatBufferBuilder
        com.google.flatbuffers.FlatBufferBuilder builder = new com.google.flatbuffers.FlatBufferBuilder();
        // When: startEntriesVector is called
        Node_.startEntriesVector(builder, 0);
        // Then: no exception is thrown
    }

    @Test
    public void testEndNode() {
        // Given: a FlatBufferBuilder
        com.google.flatbuffers.FlatBufferBuilder builder = new com.google.flatbuffers.FlatBufferBuilder();
        // When: endNode_ is called
        int offset = Node_.endNode_(builder);
        // Then: offset is not 0
        assertNotEquals(0, offset);
    }

    @Test
    public void test__assignVector() {
        // Given: a Node_ and a ByteBuffer
        Node_ node = new Node_();
        ByteBuffer _bb = ByteBuffer.allocate(10);
        // When: __assign is called
        Node_.Vector result = node.__assign(0, 0, _bb);
        // Then: result is not null
        assertNotNull(result);
    }

    @Test
    public void testGet() {
        // Given: a Node_
        Node_ node = new Node_();
        // When: get is called
        Node_ result = node.get(0);
        // Then: result is not null
        assertNotNull(result);
    }

    @Test
    public void testGetNode() {
        // Given: a Node_ and a Node_
        Node_ node = new Node_();
        Node_ obj = new Node_();
        // When: get is called
        Node_ result = node.get(obj, 0);
        // Then: result is not null
        assertNotNull(result);
    }
}
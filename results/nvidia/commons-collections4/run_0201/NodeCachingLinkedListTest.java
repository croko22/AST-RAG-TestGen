import org.apache.commons.collections4.list.NodeCachingLinkedList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class NodeCachingLinkedListTest {

    private NodeCachingLinkedList<String> nodeCachingLinkedList;

    @BeforeEach
    void setup() {
        nodeCachingLinkedList = new NodeCachingLinkedList<>();
    }

    @AfterEach
    void tearDown() {
        nodeCachingLinkedList = null;
    }

    @Test
    void testConstructor() {
        // Given: default constructor
        NodeCachingLinkedList<String> list = new NodeCachingLinkedList<>();

        // Then: verify default maximum cache size
        assertEquals(20, list.getMaximumCacheSize());
    }

    @Test
    void testConstructorWithCollection() {
        // Given: a collection
        Collection<String> collection = new LinkedList<>();
        collection.add("Element1");
        collection.add("Element2");

        // When: create a NodeCachingLinkedList with the collection
        NodeCachingLinkedList<String> list = new NodeCachingLinkedList<>(collection);

        // Then: verify the list contains the elements from the collection
        assertEquals(2, list.size());
        assertTrue(list.contains("Element1"));
        assertTrue(list.contains("Element2"));
    }

    @Test
    void testConstructorWithMaximumCacheSize() {
        // Given: a maximum cache size
        int maximumCacheSize = 10;

        // When: create a NodeCachingLinkedList with the maximum cache size
        NodeCachingLinkedList<String> list = new NodeCachingLinkedList<>(maximumCacheSize);

        // Then: verify the maximum cache size
        assertEquals(maximumCacheSize, list.getMaximumCacheSize());
    }

    @Test
    void testAddNodeToCache() {
        // Given: a node
        NodeCachingLinkedList<String>.Node<String> node = nodeCachingLinkedList.new Node<>("Element");

        // When: add the node to the cache
        nodeCachingLinkedList.addNodeToCache(node);

        // Then: verify the cache size
        assertEquals(1, nodeCachingLinkedList.cacheSize);
    }

    @Test
    void testCreateNode() {
        // Given: a value
        String value = "Element";

        // When: create a new node with the value
        NodeCachingLinkedList<String>.Node<String> node = nodeCachingLinkedList.createNode(value);

        // Then: verify the node's value
        assertEquals(value, node.getValue());
    }

    @Test
    void testGetMaximumCacheSize() {
        // Given: a maximum cache size
        int maximumCacheSize = 10;

        // When: set the maximum cache size
        nodeCachingLinkedList.setMaximumCacheSize(maximumCacheSize);

        // Then: verify the maximum cache size
        assertEquals(maximumCacheSize, nodeCachingLinkedList.getMaximumCacheSize());
    }

    @Test
    void testGetNodeFromCache() {
        // Given: a node in the cache
        NodeCachingLinkedList<String>.Node<String> node = nodeCachingLinkedList.new Node<>("Element");
        nodeCachingLinkedList.addNodeToCache(node);

        // When: get a node from the cache
        NodeCachingLinkedList<String>.Node<String> cachedNode = nodeCachingLinkedList.getNodeFromCache();

        // Then: verify the cached node
        assertNotNull(cachedNode);
        assertEquals(node, cachedNode);
    }

    @Test
    void testIsCacheFull() {
        // Given: a maximum cache size
        int maximumCacheSize = 1;

        // When: set the maximum cache size
        nodeCachingLinkedList.setMaximumCacheSize(maximumCacheSize);

        // And: add a node to the cache
        NodeCachingLinkedList<String>.Node<String> node = nodeCachingLinkedList.new Node<>("Element");
        nodeCachingLinkedList.addNodeToCache(node);

        // Then: verify the cache is full
        assertTrue(nodeCachingLinkedList.isCacheFull());
    }

    @Test
    void testRemoveAllNodes() {
        // Given: some nodes in the list
        nodeCachingLinkedList.add("Element1");
        nodeCachingLinkedList.add("Element2");

        // When: remove all nodes
        nodeCachingLinkedList.removeAllNodes();

        // Then: verify the list is empty
        assertEquals(0, nodeCachingLinkedList.size());
    }

    @Test
    void testRemoveNode() {
        // Given: a node in the list
        NodeCachingLinkedList<String>.Node<String> node = nodeCachingLinkedList.new Node<>("Element");
        nodeCachingLinkedList.add(node.getValue());

        // When: remove the node
        nodeCachingLinkedList.removeNode(node);

        // Then: verify the node is removed
        assertEquals(0, nodeCachingLinkedList.size());
    }

    @Test
    void testSetMaximumCacheSize() {
        // Given: a new maximum cache size
        int maximumCacheSize = 10;

        // When: set the maximum cache size
        nodeCachingLinkedList.setMaximumCacheSize(maximumCacheSize);

        // Then: verify the maximum cache size
        assertEquals(maximumCacheSize, nodeCachingLinkedList.getMaximumCacheSize());
    }

    @Test
    void testShrinkCacheToMaximumSize() {
        // Given: a maximum cache size
        int maximumCacheSize = 1;

        // When: set the maximum cache size
        nodeCachingLinkedList.setMaximumCacheSize(maximumCacheSize);

        // And: add some nodes to the cache
        for (int i = 0; i < 10; i++) {
            NodeCachingLinkedList<String>.Node<String> node = nodeCachingLinkedList.new Node<>("Element");
            nodeCachingLinkedList.addNodeToCache(node);
        }

        // Then: verify the cache size
        assertEquals(maximumCacheSize, nodeCachingLinkedList.cacheSize);
    }
}
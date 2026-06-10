import org.apache.commons.collections4.ArrayStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayStackTest {

    private ArrayStack<String> arrayStack;

    @BeforeEach
    public void setup() {
        arrayStack = new ArrayStack<>();
    }

    @Test
    public void testEmpty_InitiallyEmpty() {
        // Given: a newly created ArrayStack
        // When: checking if the stack is empty
        boolean isEmpty = arrayStack.empty();
        // Then: the stack should be empty
        assertTrue(isEmpty);
    }

    @Test
    public void testEmpty_AfterPushingElement() {
        // Given: a newly created ArrayStack
        // When: pushing an element onto the stack
        arrayStack.push("Element");
        // Then: the stack should not be empty
        assertFalse(arrayStack.empty());
    }

    @Test
    public void testPeek_OnEmptyStack() {
        // Given: a newly created ArrayStack
        // When/Then: peeking at the top element should throw an exception
        assertThrows(EmptyStackException.class, () -> arrayStack.peek());
    }

    @Test
    public void testPeek_OnNonEmptyStack() {
        // Given: a newly created ArrayStack with an element
        arrayStack.push("Element");
        // When: peeking at the top element
        String topElement = arrayStack.peek();
        // Then: the top element should be returned
        assertEquals("Element", topElement);
    }

    @Test
    public void testPeek_OnNonEmptyStack_WithIndex() {
        // Given: a newly created ArrayStack with multiple elements
        arrayStack.push("Element1");
        arrayStack.push("Element2");
        // When: peeking at the top element with an index
        String topElement = arrayStack.peek(1);
        // Then: the top element at the given index should be returned
        assertEquals("Element1", topElement);
    }

    @Test
    public void testPeek_OnNonEmptyStack_WithInvalidIndex() {
        // Given: a newly created ArrayStack with multiple elements
        arrayStack.push("Element1");
        arrayStack.push("Element2");
        // When/Then: peeking at the top element with an invalid index should throw an exception
        assertThrows(EmptyStackException.class, () -> arrayStack.peek(2));
    }

    @Test
    public void testPop_OnEmptyStack() {
        // Given: a newly created ArrayStack
        // When/Then: popping an element from the stack should throw an exception
        assertThrows(EmptyStackException.class, () -> arrayStack.pop());
    }

    @Test
    public void testPop_OnNonEmptyStack() {
        // Given: a newly created ArrayStack with an element
        arrayStack.push("Element");
        // When: popping an element from the stack
        String poppedElement = arrayStack.pop();
        // Then: the popped element should be returned and the stack should be empty
        assertEquals("Element", poppedElement);
        assertTrue(arrayStack.empty());
    }

    @Test
    public void testPush_OnEmptyStack() {
        // Given: a newly created ArrayStack
        // When: pushing an element onto the stack
        String pushedElement = arrayStack.push("Element");
        // Then: the pushed element should be returned and the stack should not be empty
        assertEquals("Element", pushedElement);
        assertFalse(arrayStack.empty());
    }

    @Test
    public void testSearch_OnEmptyStack() {
        // Given: a newly created ArrayStack
        // When: searching for an element on the stack
        int searchResult = arrayStack.search("Element");
        // Then: the search result should be -1
        assertEquals(-1, searchResult);
    }

    @Test
    public void testSearch_OnNonEmptyStack_ElementFound() {
        // Given: a newly created ArrayStack with an element
        arrayStack.push("Element");
        // When: searching for the element on the stack
        int searchResult = arrayStack.search("Element");
        // Then: the search result should be 1
        assertEquals(1, searchResult);
    }

    @Test
    public void testSearch_OnNonEmptyStack_ElementNotFound() {
        // Given: a newly created ArrayStack with an element
        arrayStack.push("Element1");
        // When: searching for a different element on the stack
        int searchResult = arrayStack.search("Element2");
        // Then: the search result should be -1
        assertEquals(-1, searchResult);
    }
}
import org.jsoup.internal.SoftPool;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.function.Supplier;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SoftPoolTest {

    @Mock
    private Supplier<String> stringSupplier;

    @BeforeEach
    void setup() {
        when(stringSupplier.get()).thenReturn("New String");
    }

    @Test
    void testBorrow_NewObjectCreated_WhenPoolIsEmpty() {
        // Given: an empty pool
        SoftPool<String> pool = new SoftPool<>(stringSupplier);

        // When: borrow an object from the pool
        String borrowedObject = pool.borrow();

        // Then: a new object is created and returned
        assertEquals("New String", borrowedObject);
    }

    @Test
    void testBorrow_ExistingObjectReturned_WhenPoolIsNotEmpty() {
        // Given: a pool with an existing object
        SoftPool<String> pool = new SoftPool<>(stringSupplier);
        String existingObject = "Existing String";
        pool.release(existingObject);

        // When: borrow an object from the pool
        String borrowedObject = pool.borrow();

        // Then: the existing object is returned
        assertEquals(existingObject, borrowedObject);
    }

    @Test
    void testRelease_ObjectAddedToPool_WhenPoolIsNotFull() {
        // Given: a pool with less than MaxIdle objects
        SoftPool<String> pool = new SoftPool<>(stringSupplier);
        String objectToRelease = "Object to Release";

        // When: release an object back to the pool
        pool.release(objectToRelease);

        // Then: the object is added to the pool
        String borrowedObject = pool.borrow();
        assertEquals(objectToRelease, borrowedObject);
    }

    @Test
    void testRelease_ObjectNotAddedToPool_WhenPoolIsFull() {
        // Given: a pool with MaxIdle objects
        SoftPool<String> pool = new SoftPool<>(stringSupplier);
        for (int i = 0; i < SoftPool.MaxIdle; i++) {
            pool.release("Object " + i);
        }

        // When: release an object back to the pool
        String objectToRelease = "Object to Release";
        pool.release(objectToRelease);

        // Then: the object is not added to the pool
        String borrowedObject = pool.borrow();
        assertNotEquals(objectToRelease, borrowedObject);
    }

    @Test
    void testGetStack_StackIsInitialized_WhenThreadLocalIsNew() {
        // Given: a new SoftPool instance
        SoftPool<String> pool = new SoftPool<>(stringSupplier);

        // When: get the stack from the pool
        ArrayDeque<String> stack = pool.getStack();

        // Then: the stack is initialized
        assertNotNull(stack);
    }

    @Test
    void testGetStack_StackIsReinitialized_WhenSoftReferenceIsCleared() {
        // Given: a SoftPool instance with a cleared soft reference
        SoftPool<String> pool = new SoftPool<>(stringSupplier);
        pool.threadLocalStack.set(new SoftReference<>(null));

        // When: get the stack from the pool
        ArrayDeque<String> stack = pool.getStack();

        // Then: the stack is reinitialized
        assertNotNull(stack);
    }
}
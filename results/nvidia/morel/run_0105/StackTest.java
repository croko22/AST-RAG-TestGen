import net.hydromatic.morel.eval.Session;
import net.hydromatic.morel.eval.Stack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StackTest {

    @Mock
    private Session session;

    private Stack stack;

    @BeforeEach
    void setup() {
        stack = new Stack(session, new Object[10], 0);
    }

    @Test
    void testWithCapacity() {
        // Given
        int capacity = 10;

        // When
        Stack result = Stack.withCapacity(capacity);

        // Then
        assertNotNull(result);
        assertEquals(capacity, result.slots.length);
    }

    @Test
    void testWithCapacity_Zero() {
        // Given
        int capacity = 0;

        // When
        Stack result = Stack.withCapacity(capacity);

        // Then
        assertNotNull(result);
        assertEquals(0, result.slots.length);
    }

    @Test
    void testCurrentEnv() {
        // Given
        Map<String, Object> globalEnv = new HashMap<>();
        when(session.globalEnv).thenReturn(globalEnv);

        // When
        Map<String, Object> result = stack.currentEnv();

        // Then
        assertNotNull(result);
        assertSame(globalEnv, result);
    }

    @Test
    void testCurrentEnv_NullGlobalEnv() {
        // Given
        when(session.globalEnv).thenReturn(null);

        // When and Then
        assertThrows(NullPointerException.class, () -> stack.currentEnv());
    }

    @Test
    void testPush() {
        // Given
        Object value = "test";

        // When
        stack.push(value);

        // Then
        assertEquals(value, stack.slots[0]);
        assertEquals(1, stack.top);
    }

    @Test
    void testSave() {
        // Given
        stack.push("test");

        // When
        int result = stack.save();

        // Then
        assertEquals(1, result);
    }

    @Test
    void testRestore() {
        // Given
        stack.push("test");
        int savedTop = stack.save();

        // When
        stack.restore(savedTop);

        // Then
        assertEquals(savedTop, stack.top);
    }

    @Test
    void testEnsureSize_EnoughSpace() {
        // Given
        stack = new Stack(session, new Object[10], 0);
        int needed = 5;

        // When
        Stack result = stack.ensureSize(needed);

        // Then
        assertSame(stack, result);
    }

    @Test
    void testEnsureSize_NotEnoughSpace() {
        // Given
        stack = new Stack(session, new Object[5], 0);
        int needed = 10;

        // When
        Stack result = stack.ensureSize(needed);

        // Then
        assertNotNull(result);
        assertNotSame(stack, result);
        assertEquals(10, result.slots.length);
    }
}
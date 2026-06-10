import net.hydromatic.morel.util.ThreadLocals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ThreadLocalsTest {

    @Mock
    private ThreadLocal<String> threadLocal;

    @Mock
    private Runnable runnable;

    @Mock
    private Supplier<String> supplier;

    @Mock
    private UnaryOperator<String> transform;

    @BeforeEach
    void setup() {
        // Initialize mocks
        when(threadLocal.get()).thenReturn("originalValue");
        when(transform.apply("originalValue")).thenReturn("transformedValue");
        when(supplier.get()).thenReturn("supplierResult");
    }

    @AfterEach
    void tearDown() {
        // Reset mocks
        reset(threadLocal, runnable, supplier, transform);
    }

    @Test
    public void testLetRunnable() {
        // Given: threadLocal with original value
        when(threadLocal.get()).thenReturn("originalValue");

        // When: let is called with a new value and a runnable
        ThreadLocals.let(threadLocal, "newValue", runnable);

        // Then: verify that the original value is restored and the runnable is executed
        verify(threadLocal, times(1)).set("newValue");
        verify(runnable, times(1)).run();
        verify(threadLocal, times(1)).set("originalValue");
    }

    @Test
    public void testLetSupplier() {
        // Given: threadLocal with original value
        when(threadLocal.get()).thenReturn("originalValue");

        // When: let is called with a new value and a supplier
        String result = ThreadLocals.let(threadLocal, "newValue", supplier);

        // Then: verify that the original value is restored and the supplier is executed
        verify(threadLocal, times(1)).set("newValue");
        verify(supplier, times(1)).get();
        verify(threadLocal, times(1)).set("originalValue");
        assertEquals("supplierResult", result);
    }

    @Test
    public void testMutateRunnable() {
        // Given: threadLocal with original value
        when(threadLocal.get()).thenReturn("originalValue");

        // When: mutate is called with a transform and a runnable
        ThreadLocals.mutate(threadLocal, transform, runnable);

        // Then: verify that the transformed value is set and the runnable is executed
        verify(threadLocal, times(1)).set("transformedValue");
        verify(runnable, times(1)).run();
        verify(threadLocal, times(1)).set("originalValue");
    }

    @Test
    public void testMutateSupplier() {
        // Given: threadLocal with original value
        when(threadLocal.get()).thenReturn("originalValue");

        // When: mutate is called with a transform and a supplier
        String result = ThreadLocals.mutate(threadLocal, transform, supplier);

        // Then: verify that the transformed value is set and the supplier is executed
        verify(threadLocal, times(1)).set("transformedValue");
        verify(supplier, times(1)).get();
        verify(threadLocal, times(1)).set("originalValue");
        assertEquals("supplierResult", result);
    }

    @Test
    public void testLetRunnableException() {
        // Given: threadLocal with original value
        when(threadLocal.get()).thenReturn("originalValue");
        doThrow(new RuntimeException("Test exception")).when(runnable).run();

        // When: let is called with a new value and a runnable that throws an exception
        assertThrows(RuntimeException.class, () -> ThreadLocals.let(threadLocal, "newValue", runnable));

        // Then: verify that the original value is restored
        verify(threadLocal, times(1)).set("newValue");
        verify(runnable, times(1)).run();
        verify(threadLocal, times(1)).set("originalValue");
    }

    @Test
    public void testLetSupplierException() {
        // Given: threadLocal with original value
        when(threadLocal.get()).thenReturn("originalValue");
        when(supplier.get()).thenThrow(new RuntimeException("Test exception"));

        // When: let is called with a new value and a supplier that throws an exception
        assertThrows(RuntimeException.class, () -> ThreadLocals.let(threadLocal, "newValue", supplier));

        // Then: verify that the original value is restored
        verify(threadLocal, times(1)).set("newValue");
        verify(supplier, times(1)).get();
        verify(threadLocal, times(1)).set("originalValue");
    }

    @Test
    public void testMutateRunnableException() {
        // Given: threadLocal with original value
        when(threadLocal.get()).thenReturn("originalValue");
        doThrow(new RuntimeException("Test exception")).when(runnable).run();

        // When: mutate is called with a transform and a runnable that throws an exception
        assertThrows(RuntimeException.class, () -> ThreadLocals.mutate(threadLocal, transform, runnable));

        // Then: verify that the original value is restored
        verify(threadLocal, times(1)).set("transformedValue");
        verify(runnable, times(1)).run();
        verify(threadLocal, times(1)).set("originalValue");
    }

    @Test
    public void testMutateSupplierException() {
        // Given: threadLocal with original value
        when(threadLocal.get()).thenReturn("originalValue");
        when(supplier.get()).thenThrow(new RuntimeException("Test exception"));

        // When: mutate is called with a transform and a supplier that throws an exception
        assertThrows(RuntimeException.class, () -> ThreadLocals.mutate(threadLocal, transform, supplier));

        // Then: verify that the original value is restored
        verify(threadLocal, times(1)).set("transformedValue");
        verify(supplier, times(1)).get();
        verify(threadLocal, times(1)).set("originalValue");
    }
}
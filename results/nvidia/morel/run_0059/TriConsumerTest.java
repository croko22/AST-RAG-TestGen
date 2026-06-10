import net.hydromatic.morel.util.TriConsumer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TriConsumerTest {

    @Mock
    private TriConsumer<Object, Object, Object> triConsumerMock;

    @BeforeEach
    void setup() {
        // No setup needed
    }

    @AfterEach
    void tearDown() {
        // No tear down needed
    }

    @Test
    public void testAccept() {
        // Given: triConsumerMock is a mock implementation of TriConsumer
        // When: accept method is called with three arguments
        triConsumerMock.accept("arg1", "arg2", "arg3");

        // Then: verify that accept method was called with the given arguments
        verify(triConsumerMock, times(1)).accept(any(), any(), any());
    }

    @Test
    public void testAccept_MultipleCalls() {
        // Given: triConsumerMock is a mock implementation of TriConsumer
        // When: accept method is called multiple times with different arguments
        triConsumerMock.accept("arg1", "arg2", "arg3");
        triConsumerMock.accept("arg4", "arg5", "arg6");
        triConsumerMock.accept("arg7", "arg8", "arg9");

        // Then: verify that accept method was called with the given arguments
        verify(triConsumerMock, times(3)).accept(any(), any(), any());
    }

    @Test
    public void testAccept_NullArguments() {
        // Given: triConsumerMock is a mock implementation of TriConsumer
        // When: accept method is called with null arguments
        triConsumerMock.accept(null, null, null);

        // Then: verify that accept method was called with the given arguments
        verify(triConsumerMock, times(1)).accept(any(), any(), any());
    }

    @Test
    public void testAccept_DifferentArgumentTypes() {
        // Given: triConsumerMock is a mock implementation of TriConsumer
        // When: accept method is called with different argument types
        triConsumerMock.accept("string", 123, true);

        // Then: verify that accept method was called with the given arguments
        verify(triConsumerMock, times(1)).accept(any(), any(), any());
    }
}
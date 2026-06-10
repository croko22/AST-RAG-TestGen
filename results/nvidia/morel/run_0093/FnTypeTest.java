import net.hydromatic.morel.type.FnType;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import net.hydromatic.morel.type.TypeVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.UnaryOperator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FnTypeTest {

    @Mock
    private Type paramType;

    @Mock
    private Type resultType;

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private TypeVisitor<?> typeVisitor;

    @Mock
    private UnaryOperator<Type> transform;

    private FnType fnType;

    @BeforeEach
    void setup() {
        fnType = new FnType(paramType, resultType);
    }

    @Test
    public void testKey() {
        // Given: mock key method for paramType and resultType
        Key paramKey = mock(Key.class);
        Key resultKey = mock(Key.class);
        when(paramType.key()).thenReturn(paramKey);
        when(resultType.key()).thenReturn(resultKey);

        // When: call key method on fnType
        Key key = fnType.key();

        // Then: verify key method calls and result
        verify(paramType, times(1)).key();
        verify(resultType, times(1)).key();
        assertNotNull(key);
    }

    @Test
    public void testAccept() {
        // Given: mock typeVisitor
        Object result = new Object();
        when(typeVisitor.visit(any(FnType.class))).thenReturn(result);

        // When: call accept method on fnType
        Object actualResult = fnType.accept(typeVisitor);

        // Then: verify accept method call and result
        verify(typeVisitor, times(1)).visit(fnType);
        assertEquals(result, actualResult);
    }

    @Test
    public void testCopy_NoChange() {
        // Given: mock copy method for paramType and resultType
        when(paramType.copy(any(TypeSystem.class), any(UnaryOperator.class))).thenReturn(paramType);
        when(resultType.copy(any(TypeSystem.class), any(UnaryOperator.class))).thenReturn(resultType);

        // When: call copy method on fnType
        FnType copiedFnType = fnType.copy(typeSystem, transform);

        // Then: verify copy method calls and result
        verify(paramType, times(1)).copy(typeSystem, transform);
        verify(resultType, times(1)).copy(typeSystem, transform);
        assertSame(fnType, copiedFnType);
    }

    @Test
    public void testCopy_WithChange() {
        // Given: mock copy method for paramType and resultType
        Type newParamType = mock(Type.class);
        Type newResultType = mock(Type.class);
        when(paramType.copy(any(TypeSystem.class), any(UnaryOperator.class))).thenReturn(newParamType);
        when(resultType.copy(any(TypeSystem.class), any(UnaryOperator.class))).thenReturn(newResultType);
        when(typeSystem.fnType(newParamType, newResultType)).thenReturn(mock(FnType.class));

        // When: call copy method on fnType
        FnType copiedFnType = fnType.copy(typeSystem, transform);

        // Then: verify copy method calls and result
        verify(paramType, times(1)).copy(typeSystem, transform);
        verify(resultType, times(1)).copy(typeSystem, transform);
        verify(typeSystem, times(1)).fnType(newParamType, newResultType);
        assertNotNull(copiedFnType);
        assertNotSame(fnType, copiedFnType);
    }

    @Test
    public void testCanCallArgOf_True() {
        // Given: mock specializes method for paramType
        when(paramType.specializes(any(Type.class))).thenReturn(true);

        // When: call canCallArgOf method on fnType
        boolean result = fnType.canCallArgOf(mock(Type.class));

        // Then: verify canCallArgOf method call and result
        verify(paramType, times(1)).specializes(any(Type.class));
        assertTrue(result);
    }

    @Test
    public void testCanCallArgOf_False() {
        // Given: mock specializes method for paramType
        when(paramType.specializes(any(Type.class))).thenReturn(false);

        // When: call canCallArgOf method on fnType
        boolean result = fnType.canCallArgOf(mock(Type.class));

        // Then: verify canCallArgOf method call and result
        verify(paramType, times(1)).specializes(any(Type.class));
        assertFalse(result);
    }
}
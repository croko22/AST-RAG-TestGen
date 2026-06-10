import net.hydromatic.morel.ast.Op;
import net.hydromatic.morel.type.MultiType;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import net.hydromatic.morel.type.TypeVisitor;
import net.hydromatic.morel.util.Static;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MultiTypeTest {

    @Mock
    private Type type1;

    @Mock
    private Type type2;

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private UnaryOperator<Type> transform;

    @Mock
    private TypeVisitor typeVisitor;

    @InjectMocks
    private MultiType multiType;

    @BeforeEach
    public void setup() {
        List<Type> types = new ArrayList<>();
        types.add(type1);
        types.add(type2);
        multiType = new MultiType(types);
    }

    @Test
    public void testKey() {
        // Given
        when(type1.key()).thenReturn("key1");
        when(type2.key()).thenReturn("key2");

        // When
        Object result = multiType.key();

        // Then
        assertNotNull(result);
        verify(type1, times(1)).key();
        verify(type2, times(1)).key();
    }

    @Test
    public void testOp() {
        // When
        Op result = multiType.op();

        // Then
        assertEquals(Op.MULTI_TYPE, result);
    }

    @Test
    public void testCopy() {
        // Given
        when(transform.apply(any())).thenReturn(type1);

        // When
        assertThrows(UnsupportedOperationException.class, () -> multiType.copy(typeSystem, transform));

        // Then
        verify(transform, never()).apply(any());
        verify(typeSystem, never()).getType(any());
    }

    @Test
    public void testAccept() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> multiType.accept(typeVisitor));

        // Then
        verify(typeVisitor, never()).visit(any());
    }
}
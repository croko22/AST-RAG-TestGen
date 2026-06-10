import net.hydromatic.morel.type.DummyType;
import net.hydromatic.morel.type.Keys;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeVisitor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DummyTypeTest {

    @Mock
    private TypeVisitor<?> typeVisitor;

    @Mock
    private UnaryOperator<Type> transform;

    @Test
    public void testKey() {
        // When: se ejecuta el metodo key
        Key key = DummyType.INSTANCE.key();

        // Then: se verifica el resultado
        assertEquals(Keys.dummy(), key);
    }

    @Test
    public void testOp() {
        // When: se ejecuta el metodo op
        Op op = DummyType.INSTANCE.op();

        // Then: se verifica el resultado
        assertEquals(Op.DUMMY_TYPE, op);
    }

    @Test
    public void testAccept() {
        // Given: un TypeVisitor
        when(typeVisitor.visit(any(DummyType.class))).thenReturn("resultado");

        // When: se ejecuta el metodo accept
        Object resultado = DummyType.INSTANCE.accept(typeVisitor);

        // Then: se verifica el resultado
        assertEquals("resultado", resultado);
        verify(typeVisitor, times(1)).visit(DummyType.INSTANCE);
    }

    @Test
    public void testCopy() {
        // When: se ejecuta el metodo copy
        DummyType copia = DummyType.INSTANCE.copy(null, transform);

        // Then: se verifica el resultado
        assertSame(DummyType.INSTANCE, copia);
        verify(transform, never()).apply(any(Type.class));
    }
}
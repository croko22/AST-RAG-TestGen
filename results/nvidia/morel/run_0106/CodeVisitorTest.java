import net.hydromatic.morel.eval.CodeVisitor;
import net.hydromatic.morel.eval.Describer;
import net.hydromatic.morel.eval.Detail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CodeVisitorTest {

    private CodeVisitor codeVisitor;

    @Mock
    private Consumer<Detail> consumer;

    @BeforeEach
    void setup() {
        codeVisitor = new CodeVisitor();
    }

    @Test
    void testRegister() {
        // When: se ejecuta el registro
        int result = codeVisitor.register("name", 1);

        // Then: se verifica el resultado
        assertEquals(0, result);
    }

    @Test
    void testStart() {
        // Given: un consumidor de detalles
        // When: se inicia la descripción
        Describer describer = codeVisitor.start("name", consumer);

        // Then: se verifica el resultado y la interacción con el consumidor
        assertSame(codeVisitor, describer);
        verify(consumer).accept(any(Detail.class));
    }
}
import net.hydromatic.morel.ast.Core;
import net.hydromatic.morel.compile.Tracers;
import net.hydromatic.morel.eval.Code;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TracersTest {

    @Mock
    private Core.Decl decl;

    @Mock
    private Code code;

    @Mock
    private Consumer<Core.Decl> consumer;

    @Mock
    private Consumer<Code> codeConsumer;

    @Mock
    private Consumer<Object> objectConsumer;

    @Mock
    private Consumer<List<? extends Throwable>> warningsConsumer;

    @Mock
    private Consumer<@Nullable Throwable> exceptionConsumer;

    @Mock
    private Consumer<CompileException> compileExceptionConsumer;

    @Mock
    private Consumer<TypeResolver.TypeException> typeExceptionConsumer;

    private Tracers.Tracer tracer;

    @BeforeEach
    void setup() {
        tracer = Tracers.empty();
    }

    @Test
    public void testEmptyTracer() {
        // Given: an empty tracer
        Tracers.Tracer emptyTracer = Tracers.empty();

        // When: calling methods on the empty tracer
        emptyTracer.onCore(1, decl);
        emptyTracer.onPlan(code);
        emptyTracer.onResult(new Object());
        emptyTracer.onWarnings(new ArrayList<>());
        emptyTracer.onException(null);
        emptyTracer.handleCompileException(null);
        emptyTracer.onTypeException();

        // Then: no exceptions are thrown
        verifyNoInteractions(consumer, codeConsumer, objectConsumer, warningsConsumer, exceptionConsumer, compileExceptionConsumer, typeExceptionConsumer);
    }

    @Test
    public void testWithOnCore() {
        // Given: a tracer with an onCore action
        Tracers.Tracer tracerWithOnCore = Tracers.withOnCore(tracer, 1, consumer);

        // When: calling onCore on the tracer
        tracerWithOnCore.onCore(1, decl);

        // Then: the onCore action is called
        verify(consumer, times(1)).accept(decl);
    }

    @Test
    public void testWithOnCore_WrongPass() {
        // Given: a tracer with an onCore action for a different pass
        Tracers.Tracer tracerWithOnCore = Tracers.withOnCore(tracer, 2, consumer);

        // When: calling onCore on the tracer with the wrong pass
        tracerWithOnCore.onCore(1, decl);

        // Then: the onCore action is not called
        verify(consumer, never()).accept(decl);
    }

    @Test
    public void testWithOnPlan() {
        // Given: a tracer with an onPlan action
        Tracers.Tracer tracerWithOnPlan = Tracers.withOnPlan(tracer, codeConsumer);

        // When: calling onPlan on the tracer
        tracerWithOnPlan.onPlan(code);

        // Then: the onPlan action is called
        verify(codeConsumer, times(1)).accept(code);
    }

    @Test
    public void testWithOnResult() {
        // Given: a tracer with an onResult action
        Tracers.Tracer tracerWithOnResult = Tracers.withOnResult(tracer, objectConsumer);

        // When: calling onResult on the tracer
        tracerWithOnResult.onResult(new Object());

        // Then: the onResult action is called
        verify(objectConsumer, times(1)).accept(any());
    }

    @Test
    public void testWithOnWarnings() {
        // Given: a tracer with an onWarnings action
        Tracers.Tracer tracerWithOnWarnings = Tracers.withOnWarnings(tracer, warningsConsumer);

        // When: calling onWarnings on the tracer
        tracerWithOnWarnings.onWarnings(new ArrayList<>());

        // Then: the onWarnings action is called
        verify(warningsConsumer, times(1)).accept(any());
    }

    @Test
    public void testWithOnException() {
        // Given: a tracer with an onException action
        Tracers.Tracer tracerWithOnException = Tracers.withOnException(tracer, exceptionConsumer);

        // When: calling onException on the tracer
        tracerWithOnException.onException(null);

        // Then: the onException action is called
        verify(exceptionConsumer, times(1)).accept(null);
    }

    @Test
    public void testWithOnCompileException() {
        // Given: a tracer with an onCompileException action
        Tracers.Tracer tracerWithOnCompileException = Tracers.withOnCompileException(tracer, compileExceptionConsumer);

        // When: calling handleCompileException on the tracer
        tracerWithOnCompileException.handleCompileException(null);

        // Then: the onCompileException action is called
        verify(compileExceptionConsumer, times(1)).accept(null);
    }

    @Test
    public void testWithOnTypeException() {
        // Given: a tracer with an onTypeException action
        Tracers.Tracer tracerWithOnTypeException = Tracers.withOnTypeException(tracer, typeExceptionConsumer);

        // When: calling onTypeException on the tracer
        tracerWithOnTypeException.onTypeException();

        // Then: the onTypeException action is called
        verify(typeExceptionConsumer, times(1)).accept(any());
    }
}
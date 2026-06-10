import net.hydromatic.morel.ast.Core;
import net.hydromatic.morel.compile.Tracer;
import net.hydromatic.morel.eval.Code;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TracerTest {

    @Mock
    private Tracer tracer;

    @Mock
    private Core.Decl decl;

    @Mock
    private Code code;

    @Mock
    private List<Throwable> warningList;

    @Mock
    private Throwable exception;

    @Mock
    private CompileException compileException;

    @Mock
    private TypeResolver.TypeException typeException;

    @BeforeEach
    public void setup() {
        // Initialize mocks if necessary
    }

    @Test
    public void testOnCore() {
        // Given
        int pass = 1;

        // When
        tracer.onCore(pass, decl);

        // Then
        verify(tracer, times(1)).onCore(pass, decl);
    }

    @Test
    public void testOnPlan() {
        // Given

        // When
        tracer.onPlan(code);

        // Then
        verify(tracer, times(1)).onPlan(code);
    }

    @Test
    public void testOnResult() {
        // Given
        Object result = new Object();

        // When
        tracer.onResult(result);

        // Then
        verify(tracer, times(1)).onResult(result);
    }

    @Test
    public void testOnWarnings() {
        // Given

        // When
        tracer.onWarnings(warningList);

        // Then
        verify(tracer, times(1)).onWarnings(warningList);
    }

    @Test
    public void testOnException() {
        // Given

        // When
        boolean handled = tracer.onException(exception);

        // Then
        verify(tracer, times(1)).onException(exception);
    }

    @Test
    public void testOnException_Null() {
        // Given

        // When
        boolean handled = tracer.onException(null);

        // Then
        verify(tracer, times(1)).onException(null);
    }

    @Test
    public void testOnTypeException() {
        // Given

        // When
        boolean handled = tracer.onTypeException(typeException);

        // Then
        verify(tracer, times(1)).onTypeException(typeException);
    }

    @Test
    public void testHandleCompileException() {
        // Given

        // When
        boolean handled = tracer.handleCompileException(compileException);

        // Then
        verify(tracer, times(1)).handleCompileException(compileException);
    }

    @Test
    public void testHandleCompileException_Null() {
        // Given

        // When
        boolean handled = tracer.handleCompileException(null);

        // Then
        verify(tracer, times(1)).handleCompileException(null);
    }

    @Test
    public void testOnCore_MultipleCalls() {
        // Given
        int pass1 = 1;
        int pass2 = 2;

        // When
        tracer.onCore(pass1, decl);
        tracer.onCore(pass2, decl);

        // Then
        verify(tracer, times(2)).onCore(anyInt(), any());
    }

    @Test
    public void testOnPlan_MultipleCalls() {
        // Given

        // When
        tracer.onPlan(code);
        tracer.onPlan(code);

        // Then
        verify(tracer, times(2)).onPlan(any());
    }

    @Test
    public void testOnResult_MultipleCalls() {
        // Given
        Object result1 = new Object();
        Object result2 = new Object();

        // When
        tracer.onResult(result1);
        tracer.onResult(result2);

        // Then
        verify(tracer, times(2)).onResult(any());
    }

    @Test
    public void testOnWarnings_MultipleCalls() {
        // Given
        List<Throwable> warningList1 = new ArrayList<>();
        List<Throwable> warningList2 = new ArrayList<>();

        // When
        tracer.onWarnings(warningList1);
        tracer.onWarnings(warningList2);

        // Then
        verify(tracer, times(2)).onWarnings(any());
    }

    @Test
    public void testOnException_MultipleCalls() {
        // Given
        Throwable exception1 = new Throwable();
        Throwable exception2 = new Throwable();

        // When
        tracer.onException(exception1);
        tracer.onException(exception2);

        // Then
        verify(tracer, times(2)).onException(any());
    }

    @Test
    public void testOnTypeException_MultipleCalls() {
        // Given
        TypeResolver.TypeException typeException1 = mock(TypeResolver.TypeException.class);
        TypeResolver.TypeException typeException2 = mock(TypeResolver.TypeException.class);

        // When
        tracer.onTypeException(typeException1);
        tracer.onTypeException(typeException2);

        // Then
        verify(tracer, times(2)).onTypeException(any());
    }

    @Test
    public void testHandleCompileException_MultipleCalls() {
        // Given
        CompileException compileException1 = mock(CompileException.class);
        CompileException compileException2 = mock(CompileException.class);

        // When
        tracer.handleCompileException(compileException1);
        tracer.handleCompileException(compileException2);

        // Then
        verify(tracer, times(2)).handleCompileException(any());
    }
}
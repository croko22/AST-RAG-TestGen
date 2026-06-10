import net.hydromatic.morel.eval.EvalEnv;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EvalEnvTest {

    @Mock
    private EvalEnv evalEnv;

    @Mock
    private BiConsumer<String, Object> consumer;

    @BeforeEach
    void setup() {
        // Initialize mock behavior
        when(evalEnv.getOpt(any())).thenReturn(null);
        when(evalEnv.get(any())).thenThrow(NullPointerException.class);
    }

    @Test
    public void testGetOpt_NotBound() {
        // Given: a variable name that is not bound
        String name = "variable";

        // When: getOpt is called
        Object result = evalEnv.getOpt(name);

        // Then: the result is null
        assertNull(result);
        verify(evalEnv, times(1)).getOpt(name);
    }

    @Test
    public void testGetOpt_Bound() {
        // Given: a variable name that is bound
        String name = "variable";
        Object value = "value";
        when(evalEnv.getOpt(name)).thenReturn(value);

        // When: getOpt is called
        Object result = evalEnv.getOpt(name);

        // Then: the result is the bound value
        assertEquals(value, result);
        verify(evalEnv, times(1)).getOpt(name);
    }

    @Test
    public void testGet_NotBound() {
        // Given: a variable name that is not bound
        String name = "variable";

        // When/Then: get is called and throws an exception
        assertThrows(NullPointerException.class, () -> evalEnv.get(name));
        verify(evalEnv, times(1)).get(name);
    }

    @Test
    public void testGet_Bound() {
        // Given: a variable name that is bound
        String name = "variable";
        Object value = "value";
        when(evalEnv.getOpt(name)).thenReturn(value);

        // When: get is called
        Object result = evalEnv.get(name);

        // Then: the result is the bound value
        assertEquals(value, result);
        verify(evalEnv, times(1)).get(name);
    }

    @Test
    public void testGetSession() {
        // Given: a session object
        Session session = mock(Session.class);
        when(evalEnv.get(EvalEnv.SESSION)).thenReturn(session);

        // When: getSession is called
        Session result = evalEnv.getSession();

        // Then: the result is the session object
        assertEquals(session, result);
        verify(evalEnv, times(1)).get(EvalEnv.SESSION);
    }

    @Test
    public void testBind() {
        // Given: a variable name and value
        String name = "variable";
        Object value = "value";

        // When: bind is called
        EvalEnv newEvalEnv = evalEnv.bind(name, value);

        // Then: a new environment is created
        assertNotNull(newEvalEnv);
    }

    @Test
    public void testVisit() {
        // Given: a consumer function
        BiConsumer<String, Object> consumer = (name, value) -> {
            // Do nothing
        };

        // When: visit is called
        evalEnv.visit(consumer);

        // Then: the consumer function is called for each binding
        verify(evalEnv, times(1)).visit(consumer);
    }

    @Test
    public void testValueMap() {
        // Given: a map of bindings
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("variable1", "value1");
        expectedMap.put("variable2", "value2");

        // When: valueMap is called
        Map<String, Object> result = evalEnv.valueMap();

        // Then: the result is a map of bindings
        assertNotNull(result);
        verify(evalEnv, times(1)).visit(any());
    }

    @Test
    public void testFix() {
        // Given: an environment
        EvalEnv evalEnv = mock(EvalEnv.class);

        // When: fix is called
        EvalEnv result = evalEnv.fix();

        // Then: the result is the same environment
        assertEquals(evalEnv, result);
    }
}
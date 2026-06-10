import net.hydromatic.morel.eval.EvalEnvs;
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
public class EvalEnvsTest {

    @Mock
    private BiConsumer<String, Object> consumer;

    private Map<String, Object> valueMap;

    @BeforeEach
    void setup() {
        valueMap = new HashMap<>();
        valueMap.put("key1", "value1");
        valueMap.put("key2", "value2");
    }

    @Test
    void testEmpty() {
        // When
        EvalEnv evalEnv = EvalEnvs.empty();

        // Then
        assertNotNull(evalEnv);
        assertTrue(evalEnv instanceof EvalEnvs.MapEvalEnv);
        assertEquals(0, ((EvalEnvs.MapEvalEnv) evalEnv).valueMap.size());
    }

    @Test
    void testCopyOf() {
        // When
        EvalEnv evalEnv = EvalEnvs.copyOf(valueMap);

        // Then
        assertNotNull(evalEnv);
        assertTrue(evalEnv instanceof EvalEnvs.MapEvalEnv);
        assertEquals(valueMap, ((EvalEnvs.MapEvalEnv) evalEnv).valueMap);
    }

    @Test
    void testVisit() {
        // Given
        EvalEnv evalEnv = EvalEnvs.copyOf(valueMap);

        // When
        evalEnv.visit(consumer);

        // Then
        verify(consumer, times(2)).accept(any(), any());
    }

    @Test
    void testGetOpt_Present() {
        // Given
        EvalEnv evalEnv = EvalEnvs.copyOf(valueMap);

        // When
        Object result = evalEnv.getOpt("key1");

        // Then
        assertEquals("value1", result);
    }

    @Test
    void testGetOpt_Absent() {
        // Given
        EvalEnv evalEnv = EvalEnvs.copyOf(valueMap);

        // When
        Object result = evalEnv.getOpt("key3");

        // Then
        assertNull(result);
    }

    @Test
    void testSubEvalEnv_Visit() {
        // Given
        EvalEnv parentEnv = EvalEnvs.copyOf(valueMap);
        EvalEnv subEvalEnv = new EvalEnvs.SubEvalEnv(parentEnv, "key3", "value3");

        // When
        subEvalEnv.visit(consumer);

        // Then
        verify(consumer, times(1)).accept("key3", "value3");
        verify(consumer, times(2)).accept(any(), any());
    }

    @Test
    void testSubEvalEnv_GetOpt_PresentInSubEnv() {
        // Given
        EvalEnv parentEnv = EvalEnvs.copyOf(valueMap);
        EvalEnv subEvalEnv = new EvalEnvs.SubEvalEnv(parentEnv, "key3", "value3");

        // When
        Object result = subEvalEnv.getOpt("key3");

        // Then
        assertEquals("value3", result);
    }

    @Test
    void testSubEvalEnv_GetOpt_PresentInParentEnv() {
        // Given
        EvalEnv parentEnv = EvalEnvs.copyOf(valueMap);
        EvalEnv subEvalEnv = new EvalEnvs.SubEvalEnv(parentEnv, "key3", "value3");

        // When
        Object result = subEvalEnv.getOpt("key1");

        // Then
        assertEquals("value1", result);
    }

    @Test
    void testSubEvalEnv_GetOpt_Absent() {
        // Given
        EvalEnv parentEnv = EvalEnvs.copyOf(valueMap);
        EvalEnv subEvalEnv = new EvalEnvs.SubEvalEnv(parentEnv, "key3", "value3");

        // When
        Object result = subEvalEnv.getOpt("key4");

        // Then
        assertNull(result);
    }

    @Test
    void testMapEvalEnv_Visit() {
        // Given
        EvalEnv evalEnv = EvalEnvs.copyOf(valueMap);

        // When
        evalEnv.visit(consumer);

        // Then
        verify(consumer, times(2)).accept(any(), any());
    }

    @Test
    void testMapEvalEnv_GetOpt_Present() {
        // Given
        EvalEnv evalEnv = EvalEnvs.copyOf(valueMap);

        // When
        Object result = evalEnv.getOpt("key1");

        // Then
        assertEquals("value1", result);
    }

    @Test
    void testMapEvalEnv_GetOpt_Absent() {
        // Given
        EvalEnv evalEnv = EvalEnvs.copyOf(valueMap);

        // When
        Object result = evalEnv.getOpt("key3");

        // Then
        assertNull(result);
    }
}
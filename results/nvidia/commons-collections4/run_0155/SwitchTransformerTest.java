import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.SwitchTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SwitchTransformerTest {

    @Mock
    private Predicate<Object> predicate1;

    @Mock
    private Predicate<Object> predicate2;

    @Mock
    private Transformer<Object, Object> transformer1;

    @Mock
    private Transformer<Object, Object> transformer2;

    @Mock
    private Transformer<Object, Object> defaultTransformer;

    private SwitchTransformer<Object, Object> switchTransformer;

    @BeforeEach
    public void setup() {
        switchTransformer = new SwitchTransformer<>(new Predicate[]{predicate1, predicate2}, new Transformer[]{transformer1, transformer2}, defaultTransformer);
    }

    @Test
    public void testSwitchTransformer_Predicate1ReturnsTrue() {
        // Given
        when(predicate1.test(any())).thenReturn(true);
        when(transformer1.transform(any())).thenReturn("Result1");

        // When
        Object result = switchTransformer.transform("Input");

        // Then
        assertEquals("Result1", result);
        verify(predicate1, times(1)).test(any());
        verify(transformer1, times(1)).transform(any());
        verify(predicate2, never()).test(any());
        verify(transformer2, never()).transform(any());
        verify(defaultTransformer, never()).transform(any());
    }

    @Test
    public void testSwitchTransformer_Predicate2ReturnsTrue() {
        // Given
        when(predicate1.test(any())).thenReturn(false);
        when(predicate2.test(any())).thenReturn(true);
        when(transformer2.transform(any())).thenReturn("Result2");

        // When
        Object result = switchTransformer.transform("Input");

        // Then
        assertEquals("Result2", result);
        verify(predicate1, times(1)).test(any());
        verify(predicate2, times(1)).test(any());
        verify(transformer2, times(1)).transform(any());
        verify(transformer1, never()).transform(any());
        verify(defaultTransformer, never()).transform(any());
    }

    @Test
    public void testSwitchTransformer_NoPredicateReturnsTrue() {
        // Given
        when(predicate1.test(any())).thenReturn(false);
        when(predicate2.test(any())).thenReturn(false);
        when(defaultTransformer.transform(any())).thenReturn("DefaultResult");

        // When
        Object result = switchTransformer.transform("Input");

        // Then
        assertEquals("DefaultResult", result);
        verify(predicate1, times(1)).test(any());
        verify(predicate2, times(1)).test(any());
        verify(defaultTransformer, times(1)).transform(any());
        verify(transformer1, never()).transform(any());
        verify(transformer2, never()).transform(any());
    }

    @Test
    public void testSwitchTransformer_MapConstructor() {
        // Given
        Map<Predicate<Object>, Transformer<Object, Object>> map = new HashMap<>();
        map.put(predicate1, transformer1);
        map.put(predicate2, transformer2);
        map.put(null, defaultTransformer);

        // When
        SwitchTransformer<Object, Object> switchTransformer = SwitchTransformer.switchTransformer(map);

        // Then
        assertNotNull(switchTransformer);
        assertEquals(defaultTransformer, switchTransformer.getDefaultTransformer());
    }

    @Test
    public void testSwitchTransformer_ArrayConstructor() {
        // Given
        Predicate<Object>[] predicates = new Predicate[]{predicate1, predicate2};
        Transformer<Object, Object>[] transformers = new Transformer[]{transformer1, transformer2};

        // When
        SwitchTransformer<Object, Object> switchTransformer = SwitchTransformer.switchTransformer(predicates, transformers, defaultTransformer);

        // Then
        assertNotNull(switchTransformer);
        assertEquals(defaultTransformer, switchTransformer.getDefaultTransformer());
    }

    @Test
    public void testGetDefaultTransformer() {
        // Given
        SwitchTransformer<Object, Object> switchTransformer = new SwitchTransformer<>(new Predicate[]{predicate1, predicate2}, new Transformer[]{transformer1, transformer2}, defaultTransformer);

        // When
        Transformer<Object, Object> defaultTransformer = switchTransformer.getDefaultTransformer();

        // Then
        assertEquals(defaultTransformer, defaultTransformer);
    }

    @Test
    public void testGetPredicates() {
        // Given
        SwitchTransformer<Object, Object> switchTransformer = new SwitchTransformer<>(new Predicate[]{predicate1, predicate2}, new Transformer[]{transformer1, transformer2}, defaultTransformer);

        // When
        Predicate<Object>[] predicates = switchTransformer.getPredicates();

        // Then
        assertNotNull(predicates);
        assertEquals(2, predicates.length);
        assertSame(predicate1, predicates[0]);
        assertSame(predicate2, predicates[1]);
    }

    @Test
    public void testGetTransformers() {
        // Given
        SwitchTransformer<Object, Object> switchTransformer = new SwitchTransformer<>(new Predicate[]{predicate1, predicate2}, new Transformer[]{transformer1, transformer2}, defaultTransformer);

        // When
        Transformer<Object, Object>[] transformers = switchTransformer.getTransformers();

        // Then
        assertNotNull(transformers);
        assertEquals(2, transformers.length);
        assertSame(transformer1, transformers[0]);
        assertSame(transformer2, transformers[1]);
    }
}
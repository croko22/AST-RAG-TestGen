import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.Transformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChainedTransformerTest {

    @Mock
    private Transformer<String, String> transformer1;

    @Mock
    private Transformer<String, String> transformer2;

    private ChainedTransformer<String> chainedTransformer;

    @BeforeEach
    void setup() {
        when(transformer1.apply(any())).thenReturn("Transformed by transformer1");
        when(transformer2.apply(any())).thenReturn("Transformed by transformer2");
    }

    @Test
    void testChainedTransformer_Collection() {
        // Given
        Collection<Transformer<String, String>> transformers = Arrays.asList(transformer1, transformer2);

        // When
        ChainedTransformer<String> chainedTransformer = ChainedTransformer.chainedTransformer(transformers);

        // Then
        assertNotNull(chainedTransformer);
        assertEquals("Transformed by transformer2", chainedTransformer.transform("Input"));
        verify(transformer1, times(1)).apply(any());
        verify(transformer2, times(1)).apply(any());
    }

    @Test
    void testChainedTransformer_VarArgs() {
        // Given
        Transformer<String, String> transformer1 = mock(Transformer.class);
        Transformer<String, String> transformer2 = mock(Transformer.class);
        when(transformer1.apply(any())).thenReturn("Transformed by transformer1");
        when(transformer2.apply(any())).thenReturn("Transformed by transformer2");

        // When
        ChainedTransformer<String> chainedTransformer = ChainedTransformer.chainedTransformer(transformer1, transformer2);

        // Then
        assertNotNull(chainedTransformer);
        assertEquals("Transformed by transformer2", chainedTransformer.transform("Input"));
        verify(transformer1, times(1)).apply(any());
        verify(transformer2, times(1)).apply(any());
    }

    @Test
    void testTransform() {
        // Given
        chainedTransformer = ChainedTransformer.chainedTransformer(transformer1, transformer2);

        // When
        String result = chainedTransformer.transform("Input");

        // Then
        assertEquals("Transformed by transformer2", result);
        verify(transformer1, times(1)).apply(any());
        verify(transformer2, times(1)).apply(any());
    }

    @Test
    void testGetTransformers() {
        // Given
        chainedTransformer = ChainedTransformer.chainedTransformer(transformer1, transformer2);

        // When
        Transformer<String, String>[] transformers = chainedTransformer.getTransformers();

        // Then
        assertNotNull(transformers);
        assertEquals(2, transformers.length);
        assertNotSame(transformer1, transformers[0]);
        assertNotSame(transformer2, transformers[1]);
    }

    @Test
    void testChainedTransformer_NullCollection() {
        // Given
        Collection<Transformer<String, String>> transformers = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> ChainedTransformer.chainedTransformer(transformers));
    }

    @Test
    void testChainedTransformer_NullTransformerInCollection() {
        // Given
        Collection<Transformer<String, String>> transformers = Arrays.asList(transformer1, null);

        // When and Then
        assertThrows(NullPointerException.class, () -> ChainedTransformer.chainedTransformer(transformers));
    }

    @Test
    void testChainedTransformer_NullTransformerInVarArgs() {
        // Given
        Transformer<String, String> transformer1 = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> ChainedTransformer.chainedTransformer(transformer1, transformer2));
    }
}
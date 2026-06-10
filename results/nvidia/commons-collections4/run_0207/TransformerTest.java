import org.apache.commons.collections4.Transformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransformerTest {

    @Mock
    private Transformer<String, Integer> transformerMock;

    @Mock
    private Function<String, Integer> functionMock;

    private Transformer<String, Integer> transformer;

    @BeforeEach
    void setup() {
        transformer = new Transformer<String, Integer>() {
            @Override
            public Integer transform(String input) {
                return input.length();
            }
        };
    }

    @Test
    public void testApply() {
        // Given
        String input = "Hello";

        // When
        Integer result = transformer.apply(input);

        // Then
        assertNotNull(result);
        assertEquals(input.length(), result);
    }

    @Test
    public void testTransform() {
        // Given
        String input = "Hello";

        // When
        Integer result = transformer.transform(input);

        // Then
        assertNotNull(result);
        assertEquals(input.length(), result);
    }

    @Test
    public void testApply_Mock() {
        // Given
        String input = "Hello";
        when(transformerMock.apply(input)).thenReturn(input.length());

        // When
        Integer result = transformerMock.apply(input);

        // Then
        assertNotNull(result);
        assertEquals(input.length(), result);
        verify(transformerMock, times(1)).apply(input);
    }

    @Test
    public void testTransform_Mock() {
        // Given
        String input = "Hello";
        when(transformerMock.transform(input)).thenReturn(input.length());

        // When
        Integer result = transformerMock.transform(input);

        // Then
        assertNotNull(result);
        assertEquals(input.length(), result);
        verify(transformerMock, times(1)).transform(input);
    }

    @Test
    public void testApply_Function() {
        // Given
        String input = "Hello";
        when(functionMock.apply(input)).thenReturn(input.length());

        // When
        Integer result = functionMock.apply(input);

        // Then
        assertNotNull(result);
        assertEquals(input.length(), result);
        verify(functionMock, times(1)).apply(input);
    }

    @Test
    public void testTransform_ClassCastException() {
        // Given
        Transformer<Object, Object> transformer = new Transformer<Object, Object>() {
            @Override
            public Object transform(Object input) {
                if (!(input instanceof String)) {
                    throw new ClassCastException("Input is not a String");
                }
                return ((String) input).length();
            }
        };

        // When / Then
        assertThrows(ClassCastException.class, () -> transformer.transform(123));
    }

    @Test
    public void testTransform_IllegalArgumentException() {
        // Given
        Transformer<String, Object> transformer = new Transformer<String, Object>() {
            @Override
            public Object transform(String input) {
                if (input == null || input.isEmpty()) {
                    throw new IllegalArgumentException("Input is empty or null");
                }
                return input.length();
            }
        };

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> transformer.transform(null));
        assertThrows(IllegalArgumentException.class, () -> transformer.transform(""));
    }
}
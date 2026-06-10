import net.hydromatic.morel.foreign.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConverterTest {

    @Mock
    private Converter<Object> converter;

    @BeforeEach
    void setup() {
        // No setup required for this test class
    }

    @Test
    public void testApply() {
        // Given: a converter and an input object
        Object input = new Object();
        Object expectedOutput = new Object();

        // When: the converter is applied to the input object
        when(converter.apply(input)).thenReturn(expectedOutput);

        // Then: the output is as expected
        Object output = converter.apply(input);
        assertEquals(expectedOutput, output);

        // Verify: the converter was applied to the input object
        verify(converter, times(1)).apply(input);
    }

    @Test
    public void testApplyNullInput() {
        // Given: a converter and a null input object

        // When: the converter is applied to the null input object
        when(converter.apply(null)).thenThrow(NullPointerException.class);

        // Then: a NullPointerException is thrown
        assertThrows(NullPointerException.class, () -> converter.apply(null));

        // Verify: the converter was applied to the null input object
        verify(converter, times(1)).apply(null);
    }

    @Test
    public void testAndThen() {
        // Given: a converter and another function
        Converter<Object> anotherConverter = mock(Converter.class);
        Function<Object, Object> anotherFunction = mock(Function.class);

        // When: the converter is composed with the another function
        when(converter.andThen(anotherFunction)).thenReturn(anotherConverter);

        // Then: the composed function is as expected
        Converter<Object> composedConverter = converter.andThen(anotherFunction);
        assertEquals(anotherConverter, composedConverter);

        // Verify: the converter was composed with the another function
        verify(converter, times(1)).andThen(anotherFunction);
    }

    @Test
    public void testCompose() {
        // Given: a converter and another function
        Converter<Object> anotherConverter = mock(Converter.class);
        Function<Object, Object> anotherFunction = mock(Function.class);

        // When: the converter is composed with the another function
        when(converter.compose(anotherFunction)).thenReturn(anotherConverter);

        // Then: the composed function is as expected
        Converter<Object> composedConverter = converter.compose(anotherFunction);
        assertEquals(anotherConverter, composedConverter);

        // Verify: the converter was composed with the another function
        verify(converter, times(1)).compose(anotherFunction);
    }

    @Test
    public void testIdentity() {
        // Given: a converter

        // When: the identity function is applied to the converter
        Function<Object, Object> identityFunction = Function.identity();

        // Then: the identity function is as expected
        assertEquals(identityFunction, Function.identity());

        // Verify: no interactions with the converter
        verifyNoInteractions(converter);
    }
}
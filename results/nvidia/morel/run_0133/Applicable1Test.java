import net.hydromatic.morel.eval.Applicable1;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class Applicable1Test {

    @Mock
    private Applicable1<String, String> applicable1Mock;

    @Test
    public void testApply() {
        // Given
        String argument = "testArgument";
        String expectedResult = "testResult";
        when(applicable1Mock.apply(argument)).thenReturn(expectedResult);

        // When
        String result = applicable1Mock.apply(argument);

        // Then
        assertEquals(expectedResult, result);
        verify(applicable1Mock, times(1)).apply(argument);
    }

    @Test
    public void testDescribe() {
        // Given
        Describer describer = mock(Describer.class);

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> applicable1Mock.describe(describer));
        verify(applicable1Mock, times(1)).describe(describer);
    }

    @Test
    public void testApplyNullArgument() {
        // Given
        String expectedResult = "testResult";
        when(applicable1Mock.apply(null)).thenReturn(expectedResult);

        // When
        String result = applicable1Mock.apply(null);

        // Then
        assertEquals(expectedResult, result);
        verify(applicable1Mock, times(1)).apply(null);
    }

    @Test
    public void testDescribeNullDescriber() {
        // Given
        Describer describer = null;

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> applicable1Mock.describe(describer));
        verify(applicable1Mock, times(1)).describe(describer);
    }
}
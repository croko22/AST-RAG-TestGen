import net.hydromatic.morel.eval.Applicable3;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class Applicable3Test {

    @Mock
    private Applicable3<String, String, String, String> applicable3Mock;

    @Test
    public void testApply() {
        // Given
        String a0 = "a0";
        String a1 = "a1";
        String a2 = "a2";
        String expectedResult = "expectedResult";
        when(applicable3Mock.apply(a0, a1, a2)).thenReturn(expectedResult);

        // When
        String result = applicable3Mock.apply(a0, a1, a2);

        // Then
        assertEquals(expectedResult, result);
        verify(applicable3Mock, times(1)).apply(a0, a1, a2);
    }

    @Test
    public void testCurry() {
        // Given
        Applicable3<String, String, String, String> applicable3 = new Applicable3<String, String, String, String>() {
            @Override
            public String apply(String a0, String a1, String a2) {
                return a0 + a1 + a2;
            }

            @Override
            public Applicable1<Applicable1<Applicable1<String, String>, String>, String> curry() {
                return a0 -> a1 -> a2 -> apply(a0, a1, a2);
            }
        };

        // When
        Applicable1<Applicable1<Applicable1<String, String>, String>, String> curried = applicable3.curry();

        // Then
        assertNotNull(curried);
        Applicable1<Applicable1<String, String>, String> curriedA0 = curried.apply("a0");
        assertNotNull(curriedA0);
        Applicable1<String, String> curriedA0A1 = curriedA0.apply("a1");
        assertNotNull(curriedA0A1);
        String result = curriedA0A1.apply("a2");
        assertEquals("a0a1a2", result);
    }

    @Test
    public void testCurry_Null() {
        // Given
        Applicable3<String, String, String, String> applicable3 = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> applicable3.curry());
    }

    @Test
    public void testApply_Null() {
        // Given
        Applicable3<String, String, String, String> applicable3 = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> applicable3.apply("a0", "a1", "a2"));
    }
}
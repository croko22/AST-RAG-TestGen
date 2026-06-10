import net.hydromatic.morel.eval.Applicable2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class Applicable2Test {

    @Mock
    private Applicable2<String, String, String> applicable2Mock;

    @Test
    public void testApply() {
        // Given
        String a0 = "arg0";
        String a1 = "arg1";
        String expectedResult = "result";

        when(applicable2Mock.apply(a0, a1)).thenReturn(expectedResult);

        // When
        String result = applicable2Mock.apply(a0, a1);

        // Then
        assertEquals(expectedResult, result);
        verify(applicable2Mock, times(1)).apply(a0, a1);
    }

    @Test
    public void testCurry() {
        // Given
        Applicable2<String, String, String> applicable2 = new Applicable2<String, String, String>() {
            @Override
            public String apply(String a0, String a1) {
                return a0 + a1;
            }

            @Override
            public Applicable1<Applicable1<String, String>, String> curry() {
                return a0 -> a1 -> a0 + a1;
            }
        };

        // When
        Applicable1<Applicable1<String, String>, String> curried = applicable2.curry();

        // Then
        assertNotNull(curried);
        Applicable1<String, String> curriedApp = curried.apply("a");
        assertNotNull(curriedApp);
        String result = curriedApp.apply("b");
        assertEquals("ab", result);
    }

    @Test
    public void testCurry_Applicable1Apply() {
        // Given
        Applicable2<String, String, String> applicable2 = new Applicable2<String, String, String>() {
            @Override
            public String apply(String a0, String a1) {
                return a0 + a1;
            }

            @Override
            public Applicable1<Applicable1<String, String>, String> curry() {
                return a0 -> a1 -> a0 + a1;
            }
        };

        // When
        Applicable1<Applicable1<String, String>, String> curried = applicable2.curry();
        Applicable1<String, String> curriedApp = curried.apply("a");
        String result = curriedApp.apply("b");

        // Then
        assertEquals("ab", result);
    }

    @Test
    public void testApply_NullArguments() {
        // Given
        Applicable2<String, String, String> applicable2 = mock(Applicable2.class);

        // When and Then
        assertThrows(NullPointerException.class, () -> applicable2.apply(null, "a"));
        assertThrows(NullPointerException.class, () -> applicable2.apply("a", null));
    }
}
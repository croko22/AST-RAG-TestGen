import net.hydromatic.morel.eval.Applicable4;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class Applicable4Test {

    @Mock
    private Applicable4<String, String, String, String, String> applicable4Mock;

    @Test
    public void testApply() {
        // Given
        String a0 = "a0";
        String a1 = "a1";
        String a2 = "a2";
        String a3 = "a3";
        String expectedResult = "expectedResult";

        when(applicable4Mock.apply(a0, a1, a2, a3)).thenReturn(expectedResult);

        // When
        String result = applicable4Mock.apply(a0, a1, a2, a3);

        // Then
        assertEquals(expectedResult, result);
        verify(applicable4Mock, times(1)).apply(a0, a1, a2, a3);
    }

    @Test
    public void testCurry() {
        // Given
        Applicable4<String, String, String, String, String> applicable4 = new Applicable4<String, String, String, String, String>() {
            @Override
            public String apply(String a0, String a1, String a2, String a3) {
                return a0 + a1 + a2 + a3;
            }

            @Override
            public Applicable1<Applicable1<Applicable1<Applicable1<String, String>, String>, String>, String> curry() {
                return a0 -> a1 -> a2 -> a3 -> apply(a0, a1, a2, a3);
            }
        };

        // When
        Applicable1<Applicable1<Applicable1<Applicable1<String, String>, String>, String>, String> curried = applicable4.curry();

        // Then
        assertNotNull(curried);
        assertEquals("a0a1a2a3", curried.apply("a0").apply("a1").apply("a2").apply("a3"));
    }

    @Test
    public void testCurry_Null() {
        // Given
        Applicable4<String, String, String, String, String> applicable4 = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> applicable4.curry());
    }

    @Test
    public void testApply_Null() {
        // Given
        Applicable4<String, String, String, String, String> applicable4 = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> applicable4.apply("a0", "a1", "a2", "a3"));
    }
}
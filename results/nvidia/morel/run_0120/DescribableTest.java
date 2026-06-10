import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.hydromatic.morel.eval.Describable;
import net.hydromatic.morel.eval.Describer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DescribableTest {

    @Mock
    private Describer describer;

    @Mock
    private Describable describable;

    @BeforeEach
    void setup() {
        // No setup needed for this test class
    }

    @Test
    public void testDescribe_Success() {
        // Given: a describable object
        when(describable.describe(any(Describer.class))).thenReturn(describer);

        // When: describe method is called
        Describer result = describable.describe(describer);

        // Then: verify the result and interactions with the mock
        assertNotNull(result);
        assertEquals(describer, result);
        verify(describable, times(1)).describe(any(Describer.class));
    }

    @Test
    public void testDescribe_NullDescriber() {
        // Given: a null describer
        Describer nullDescriber = null;

        // When / Then: expect a NullPointerException
        assertThrows(NullPointerException.class, () -> describable.describe(nullDescriber));
        verify(describable, never()).describe(any(Describer.class));
    }

    @Test
    public void testDescribe_NullDescribable() {
        // Given: a null describable
        Describable nullDescribable = null;

        // When / Then: expect a NullPointerException
        assertThrows(NullPointerException.class, () -> nullDescribable.describe(describer));
    }
}
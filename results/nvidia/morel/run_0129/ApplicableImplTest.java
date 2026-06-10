import net.hydromatic.morel.compile.BuiltIn;
import net.hydromatic.morel.eval.Applicable;
import net.hydromatic.morel.eval.ApplicableImpl;
import net.hydromatic.morel.eval.Describer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicableImplTest {

    @Mock
    private BuiltIn builtIn;

    @Mock
    private Describer describer;

    private ApplicableImpl applicableImpl;

    @BeforeEach
    void setup() {
        applicableImpl = new ApplicableImpl(builtIn) {
            // Anonymous subclass to allow instantiation of abstract class
        };
    }

    @Test
    void testToString_BuiltInMlNameStartsWithOp() {
        // Given: builtIn.mlName starts with "op "
        when(builtIn.mlName).thenReturn("op test");

        // When: toString is called
        String result = applicableImpl.toString();

        // Then: result is the substring of builtIn.mlName after "op "
        assertEquals("test", result);
        verify(builtIn).mlName;
    }

    @Test
    void testToString_BuiltInMlNameDoesNotStartWithOp() {
        // Given: builtIn.mlName does not start with "op "
        when(builtIn.mlName).thenReturn("test");
        when(builtIn.structure).thenReturn("structure");

        // When: toString is called
        String result = applicableImpl.toString();

        // Then: result is the concatenation of builtIn.structure and builtIn.mlName
        assertEquals("structure.test", result);
        verify(builtIn).mlName;
        verify(builtIn).structure;
    }

    @Test
    void testDescribe() {
        // Given: describer is not null
        when(describer.start(any(), any())).thenReturn(describer);

        // When: describe is called
        Describer result = applicableImpl.describe(describer);

        // Then: result is the same as describer
        assertNotNull(result);
        assertEquals(describer, result);
        verify(describer).start(any(), any());
    }
}
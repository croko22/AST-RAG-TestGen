import net.hydromatic.morel.type.NamedType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NamedTypeTest {

    @Mock
    private NamedType namedType;

    @BeforeEach
    void setup() {
        // Setup mock behavior
        when(namedType.name()).thenReturn("MockNamedType");
    }

    @Test
    public void testGetName_Success() {
        // Given: namedType is properly initialized
        // When: getName is called
        String name = namedType.name();
        // Then: verify the result
        assertEquals("MockNamedType", name);
        verify(namedType, times(1)).name();
    }

    @Test
    public void testGetName_Null() {
        // Given: namedType is not properly initialized
        when(namedType.name()).thenReturn(null);
        // When: getName is called
        String name = namedType.name();
        // Then: verify the result
        assertNull(name);
        verify(namedType, times(1)).name();
    }

    @Test
    public void testGetName_EmptyString() {
        // Given: namedType is not properly initialized
        when(namedType.name()).thenReturn("");
        // When: getName is called
        String name = namedType.name();
        // Then: verify the result
        assertEquals("", name);
        verify(namedType, times(1)).name();
    }
}
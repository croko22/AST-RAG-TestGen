import net.hydromatic.morel.eval.File;
import net.hydromatic.morel.type.TypeSystem;
import net.hydromatic.morel.type.TypedValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private TypedValue typedValue;

    private File file;

    @BeforeEach
    void setup() {
        file = new File() {
            @Override
            public File expand() {
                return this;
            }

            @Override
            public File discoverField(TypeSystem typeSystem, String fieldName) {
                return this;
            }
        };
    }

    @Test
    void testExpand() {
        // Given
        File expectedFile = file;

        // When
        File result = file.expand();

        // Then
        assertEquals(expectedFile, result);
    }

    @Test
    void testDiscoverField() {
        // Given
        String fieldName = "testField";
        File expectedFile = file;

        // When
        File result = file.discoverField(typeSystem, fieldName);

        // Then
        assertEquals(expectedFile, result);
        verify(typeSystem, never()).lookup();
    }

    @Test
    void testDiscoverField_TypeSystemLookup() {
        // Given
        String fieldName = "testField";
        File expectedFile = file;
        when(typeSystem.lookup(anyString())).thenReturn(null);

        // When
        File result = file.discoverField(typeSystem, fieldName);

        // Then
        assertEquals(expectedFile, result);
        verify(typeSystem, times(1)).lookup(fieldName);
    }
}
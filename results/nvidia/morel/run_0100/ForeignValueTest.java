import net.hydromatic.morel.foreign.ForeignValue;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ForeignValueTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private Type type;

    private ForeignValue foreignValue;

    @BeforeEach
    public void setup() {
        foreignValue = new ForeignValue() {
            @Override
            public Type type(TypeSystem typeSystem) {
                return type;
            }

            @Override
            public Object value() {
                return new Object();
            }
        };
    }

    @Test
    public void testType() {
        // Given
        when(typeSystem.apply(any(Type.class))).thenReturn(type);

        // When
        Type result = foreignValue.type(typeSystem);

        // Then
        assertEquals(type, result);
        verify(typeSystem, times(1)).apply(any(Type.class));
    }

    @Test
    public void testValue() {
        // Given
        Object expectedValue = new Object();

        // When
        Object result = foreignValue.value();

        // Then
        assertNotNull(result);
        assertNotEquals(expectedValue, result);
    }

    @Test
    public void testTypeNullTypeSystem() {
        // Given
        TypeSystem nullTypeSystem = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> foreignValue.type(nullTypeSystem));
    }

    @Test
    public void testValueNullForeignValue() {
        // Given
        ForeignValue nullForeignValue = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> nullForeignValue.value());
    }
}
import net.hydromatic.morel.compile.Nullable;
import net.hydromatic.morel.type.Key;
import net.hydromatic.morel.type.Op;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TypeTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private Op op;

    @Mock
    private Key key;

    @Mock
    private Type type;

    @BeforeEach
    void setup() {
        when(key.op).thenReturn(op);
        when(key.toType(typeSystem)).thenReturn(type);
    }

    @Test
    public void testToString() {
        // Given
        String expectedToString = "expectedToString";
        when(key.toString()).thenReturn(expectedToString);

        // When
        String actualToString = key.toString();

        // Then
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testToType() {
        // Given
        Type expectedType = type;
        when(key.toType(typeSystem)).thenReturn(expectedType);

        // When
        Type actualType = key.toType(typeSystem);

        // Then
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testSubstitute() {
        // Given
        Key expectedKey = key;
        when(key.substitute(any())).thenReturn(expectedKey);

        // When
        Key actualKey = key.substitute(List.of());

        // Then
        assertEquals(expectedKey, actualKey);
    }

    @Test
    public void testCopy() {
        // Given
        Key expectedKey = key;
        when(key.copy(any())).thenReturn(expectedKey);

        // When
        Key actualKey = key.copy(key -> key);

        // Then
        assertEquals(expectedKey, actualKey);
    }

    @Test
    public void testIsProgressive() {
        // Given
        boolean expectedIsProgressive = false;
        when(type.isProgressive()).thenReturn(expectedIsProgressive);

        // When
        boolean actualIsProgressive = type.isProgressive();

        // Then
        assertEquals(expectedIsProgressive, actualIsProgressive);
    }

    @Test
    public void testContainsProgressive() {
        // Given
        boolean expectedContainsProgressive = false;
        when(type.containsProgressive()).thenReturn(expectedContainsProgressive);

        // When
        boolean actualContainsProgressive = type.containsProgressive();

        // Then
        assertEquals(expectedContainsProgressive, actualContainsProgressive);
    }

    @Test
    public void testIsFinite() {
        // Given
        boolean expectedIsFinite = false;
        when(type.isFinite()).thenReturn(expectedIsFinite);

        // When
        boolean actualIsFinite = type.isFinite();

        // Then
        assertEquals(expectedIsFinite, actualIsFinite);
    }

    @Test
    public void testIsDiscrete() {
        // Given
        boolean expectedIsDiscrete = false;
        when(type.isDiscrete(any())).thenReturn(expectedIsDiscrete);

        // When
        boolean actualIsDiscrete = type.isDiscrete(typeSystem);

        // Then
        assertEquals(expectedIsDiscrete, actualIsDiscrete);
    }

    @Test
    public void testIsCollection() {
        // Given
        boolean expectedIsCollection = false;
        when(type.isCollection()).thenReturn(expectedIsCollection);

        // When
        boolean actualIsCollection = type.isCollection();

        // Then
        assertEquals(expectedIsCollection, actualIsCollection);
    }

    @Test
    public void testElementType() {
        // Given
        Type expectedElementType = type;
        when(type.elementType()).thenReturn(expectedElementType);

        // When
        Type actualElementType = type.elementType();

        // Then
        assertEquals(expectedElementType, actualElementType);
    }

    @Test
    public void testCanCallArgOf() {
        // Given
        boolean expectedCanCallArgOf = false;
        when(type.canCallArgOf(any())).thenReturn(expectedCanCallArgOf);

        // When
        boolean actualCanCallArgOf = type.canCallArgOf(type);

        // Then
        assertEquals(expectedCanCallArgOf, actualCanCallArgOf);
    }

    @Test
    public void testSpecializes() {
        // Given
        boolean expectedSpecializes = false;
        when(type.specializes(any())).thenReturn(expectedSpecializes);

        // When
        boolean actualSpecializes = type.specializes(type);

        // Then
        assertEquals(expectedSpecializes, actualSpecializes);
    }

    @Test
    public void testUnifyWith() {
        // Given
        Map<Integer, Type> expectedUnifyWith = Map.of();
        when(type.unifyWith(any())).thenReturn(expectedUnifyWith);

        // When
        Map<Integer, Type> actualUnifyWith = type.unifyWith(type);

        // Then
        assertEquals(expectedUnifyWith, actualUnifyWith);
    }

    @Test
    public void testContainsAlias() {
        // Given
        boolean expectedContainsAlias = false;
        when(type.containsAlias()).thenReturn(expectedContainsAlias);

        // When
        boolean actualContainsAlias = type.containsAlias();

        // Then
        assertEquals(expectedContainsAlias, actualContainsAlias);
    }
}
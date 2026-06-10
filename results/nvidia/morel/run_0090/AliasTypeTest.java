import net.hydromatic.morel.type.AliasType;
import net.hydromatic.morel.type.Key;
import net.hydromatic.morel.type.Op;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import net.hydromatic.morel.type.TypeVisitor;
import net.hydromatic.morel.util.Static;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AliasTypeTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private Type type;

    @Mock
    private TypeVisitor<String> typeVisitor;

    private AliasType aliasType;

    @BeforeEach
    void setup() {
        List<Type> arguments = new ArrayList<>();
        aliasType = new AliasType("alias", type, arguments);
    }

    @Test
    public void testKey() {
        // Given
        Key expectedKey = mock(Key.class);
        when(type.key()).thenReturn(expectedKey);

        // When
        Key key = aliasType.key();

        // Then
        assertEquals(expectedKey, key);
    }

    @Test
    public void testCopy_NoChange() {
        // Given
        when(typeSystem.getType(any())).thenReturn(type);
        UnaryOperator<Type> transform = t -> t;

        // When
        AliasType copiedAliasType = aliasType.copy(typeSystem, transform);

        // Then
        assertSame(aliasType, copiedAliasType);
    }

    @Test
    public void testCopy_WithChange() {
        // Given
        Type newType = mock(Type.class);
        when(newType.key()).thenReturn(mock(Key.class));
        when(typeSystem.getType(any())).thenReturn(newType);
        UnaryOperator<Type> transform = t -> newType;

        // When
        AliasType copiedAliasType = aliasType.copy(typeSystem, transform);

        // Then
        assertNotSame(aliasType, copiedAliasType);
    }

    @Test
    public void testArg_IndexInRange() {
        // Given
        List<Type> arguments = new ArrayList<>();
        arguments.add(type);
        aliasType = new AliasType("alias", type, arguments);

        // When
        Type arg = aliasType.arg(0);

        // Then
        assertEquals(type, arg);
    }

    @Test
    public void testArg_IndexOutOfRange() {
        // Given
        List<Type> arguments = new ArrayList<>();
        aliasType = new AliasType("alias", type, arguments);

        // When and Then
        assertThrows(IndexOutOfBoundsException.class, () -> aliasType.arg(0));
    }

    @Test
    public void testContainsAlias() {
        // When
        boolean containsAlias = aliasType.containsAlias();

        // Then
        assertTrue(containsAlias);
    }

    @Test
    public void testAccept() {
        // Given
        String expected = "visited";
        when(typeVisitor.visit(any())).thenReturn(expected);

        // When
        String result = aliasType.accept(typeVisitor);

        // Then
        assertEquals(expected, result);
    }
}
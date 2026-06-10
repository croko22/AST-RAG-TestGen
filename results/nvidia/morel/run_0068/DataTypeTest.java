package net.hydromatic.morel.type;

import net.hydromatic.morel.ast.Op;
import net.hydromatic.morel.compile.BuiltIn;
import net.hydromatic.morel.type.DataType;
import net.hydromatic.morel.type.Keys;
import net.hydromatic.morel.type.ParameterizedType;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import net.hydromatic.morel.type.TypeVisitor;
import net.hydromatic.morel.util.Pair;
import net.hydromatic.morel.util.Static;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataTypeTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private TypeVisitor<?> typeVisitor;

    private DataType dataType;

    @BeforeEach
    void setup() {
        List<Type> arguments = List.of(mock(Type.class), mock(Type.class));
        Map<String, Keys.Key> typeConstructors = Map.of("key1", mock(Keys.Key.class), "key2", mock(Keys.Key.class));
        dataType = new DataType("name", "moniker", arguments, typeConstructors);
    }

    @Test
    void testArg() {
        // Given
        int index = 0;

        // When
        Type result = dataType.arg(index);

        // Then
        assertNotNull(result);
        assertEquals(dataType.arguments.get(index), result);
    }

    @Test
    void testIsCollection() {
        // Given
        when(dataType.name).thenReturn(BuiltIn.Eqtype.BAG.mlName());

        // When
        boolean result = dataType.isCollection();

        // Then
        assertTrue(result);
    }

    @Test
    void testIsNotCollection() {
        // Given
        when(dataType.name).thenReturn("other");

        // When
        boolean result = dataType.isCollection();

        // Then
        assertFalse(result);
    }

    @Test
    void testElementType() {
        // Given
        when(dataType.name).thenReturn(BuiltIn.Eqtype.BAG.mlName());

        // When
        Type result = dataType.elementType();

        // Then
        assertNotNull(result);
        assertEquals(dataType.arguments.get(0), result);
    }

    @Test
    void testAccept() {
        // Given
        Object resultObject = new Object();

        // When
        Object result = dataType.accept(typeVisitor);

        // Then
        assertNotNull(result);
        verify(typeVisitor).visit(dataType);
    }

    @Test
    void testTypeConstructors() {
        // Given
        Map<String, Type> expected = Map.of("key1", mock(Type.class), "key2", mock(Type.class));

        // When
        Map<String, Type> result = dataType.typeConstructors(typeSystem);

        // Then
        assertNotNull(result);
        assertEquals(expected.size(), result.size());
    }

    @Test
    void testIsDiscrete() {
        // Given
        when(typeSystem.isDiscrete(any())).thenReturn(true);

        // When
        boolean result = dataType.isDiscrete(typeSystem);

        // Then
        assertTrue(result);
    }

    @Test
    void testIsNotDiscrete() {
        // Given
        when(typeSystem.isDiscrete(any())).thenReturn(false);

        // When
        boolean result = dataType.isDiscrete(typeSystem);

        // Then
        assertFalse(result);
    }

    @Test
    void testCopy() {
        // Given
        UnaryOperator<Type> transform = t -> t;

        // When
        DataType result = dataType.copy(typeSystem, transform);

        // Then
        assertNotNull(result);
        assertEquals(dataType, result);
    }

    @Test
    void testDescribe() {
        // Given
        StringBuilder buf = new StringBuilder();

        // When
        StringBuilder result = dataType.describe(buf);

        // Then
        assertNotNull(result);
        assertEquals(buf, result);
    }

    @Test
    void testSpecializes() {
        // Given
        Type type = mock(DataType.class);
        when(type.name).thenReturn(dataType.name);
        when(type.arguments).thenReturn(dataType.arguments);

        // When
        boolean result = dataType.specializes(type);

        // Then
        assertTrue(result);
    }

    @Test
    void testNotSpecializes() {
        // Given
        Type type = mock(DataType.class);
        when(type.name).thenReturn("other");

        // When
        boolean result = dataType.specializes(type);

        // Then
        assertFalse(result);
    }
}
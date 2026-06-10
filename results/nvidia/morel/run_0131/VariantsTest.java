import net.hydromatic.morel.eval.Variants;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VariantsTest {

    @Mock
    private TypeSystem typeSystem;

    @BeforeEach
    public void setup() {
        // Initialize typeSystem mock if needed
    }

    @Test
    public void testFromConstructor_Unit() {
        // Given
        String name = "UNIT";
        Object arg = null;
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.fromConstructor(name, arg, typeSystem);

        // Then
        assertEquals(Variants.Variant.unit(), result);
    }

    @Test
    public void testFromConstructor_Bool() {
        // Given
        String name = "BOOL";
        Object arg = true;
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.fromConstructor(name, arg, typeSystem);

        // Then
        assertEquals(Variants.Variant.ofBool(true), result);
    }

    @Test
    public void testFromConstructor_Int() {
        // Given
        String name = "INT";
        Object arg = 42;
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.fromConstructor(name, arg, typeSystem);

        // Then
        assertEquals(Variants.Variant.ofInt(42), result);
    }

    @Test
    public void testFromConstructor_Real() {
        // Given
        String name = "REAL";
        Object arg = 3.14f;
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.fromConstructor(name, arg, typeSystem);

        // Then
        assertEquals(Variants.Variant.ofReal(3.14f), result);
    }

    @Test
    public void testFromConstructor_Char() {
        // Given
        String name = "CHAR";
        Object arg = 'a';
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.fromConstructor(name, arg, typeSystem);

        // Then
        assertEquals(Variants.Variant.ofChar('a'), result);
    }

    @Test
    public void testFromConstructor_String() {
        // Given
        String name = "STRING";
        Object arg = "hello";
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.fromConstructor(name, arg, typeSystem);

        // Then
        assertEquals(Variants.Variant.ofString("hello"), result);
    }

    @Test
    public void testFromConstructor_List() {
        // Given
        String name = "LIST";
        Object arg = java.util.Arrays.asList(Variants.Variant.unit());
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.fromConstructor(name, arg, typeSystem);

        // Then
        assertEquals(Variants.Variant.ofVariantList(typeSystem, java.util.Arrays.asList(Variants.Variant.unit())), result);
    }

    @Test
    public void testFromConstructor_Bag() {
        // Given
        String name = "BAG";
        Object arg = java.util.Arrays.asList(Variants.Variant.unit());
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.fromConstructor(name, arg, typeSystem);

        // Then
        assertEquals(Variants.Variant.ofVariantBag(typeSystem, java.util.Arrays.asList(Variants.Variant.unit())), result);
    }

    @Test
    public void testFromConstructor_Vector() {
        // Given
        String name = "VECTOR";
        Object arg = java.util.Arrays.asList(Variants.Variant.unit());
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.fromConstructor(name, arg, typeSystem);

        // Then
        assertEquals(Variants.Variant.ofVariantVector(typeSystem, java.util.Arrays.asList(Variants.Variant.unit())), result);
    }

    @Test
    public void testFromConstructor_VariantNone() {
        // Given
        String name = "VARIANT_NONE";
        Object arg = null;
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.fromConstructor(name, arg, typeSystem);

        // Then
        assertEquals(Variants.Variant.ofNone(typeSystem, null), result);
    }

    @Test
    public void testFromConstructor_VariantSome() {
        // Given
        String name = "VARIANT_SOME";
        Object arg = Variants.Variant.unit();
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.fromConstructor(name, arg, typeSystem);

        // Then
        assertEquals(Variants.Variant.ofSome(typeSystem, Variants.Variant.unit()), result);
    }

    @Test
    public void testFromConstructor_Record() {
        // Given
        String name = "RECORD";
        Object arg = java.util.Arrays.asList(java.util.Arrays.asList("field1", Variants.Variant.unit()));
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.fromConstructor(name, arg, typeSystem);

        // Then
        assertEquals(Variants.Variant.ofRecord(typeSystem, net.hydromatic.morel.util.PairList.fromTransformed((java.util.List<java.util.List<Object>>) arg, (lists, consumer) -> consumer.accept((String) lists.get(0), (Variants.Variant) lists.get(1)))), result);
    }

    @Test
    public void testFromConstructor_Constant() {
        // Given
        String name = "CONSTANT";
        Object arg = "constant";
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.fromConstructor(name, arg, typeSystem);

        // Then
        assertEquals(Variants.Variant.ofConstant(typeSystem, "constant"), result);
    }

    @Test
    public void testFromConstructor_Construct() {
        // Given
        String name = "CONSTRUCT";
        Object arg = java.util.Arrays.asList("constructor", Variants.Variant.unit());
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.fromConstructor(name, arg, typeSystem);

        // Then
        assertEquals(Variants.Variant.ofConstructor(typeSystem, "constructor", Variants.Variant.unit()), result);
    }

    @Test
    public void testFromConstructor_Unknown() {
        // Given
        String name = "UNKNOWN";
        Object arg = null;
        when(typeSystem.lookup(any())).thenReturn(null);

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> Variants.fromConstructor(name, arg, typeSystem));
    }

    @Test
    public void testParse_Unit() {
        // Given
        String input = "()";
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.parse(input, typeSystem);

        // Then
        assertEquals(Variants.Variant.unit(), result);
    }

    @Test
    public void testParse_Bool() {
        // Given
        String input = "true";
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.parse(input, typeSystem);

        // Then
        assertEquals(Variants.Variant.ofBool(true), result);
    }

    @Test
    public void testParse_Int() {
        // Given
        String input = "42";
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.parse(input, typeSystem);

        // Then
        assertEquals(Variants.Variant.ofInt(42), result);
    }

    @Test
    public void testParse_Real() {
        // Given
        String input = "3.14";
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.parse(input, typeSystem);

        // Then
        assertEquals(Variants.Variant.ofReal(3.14f), result);
    }

    @Test
    public void testParse_Char() {
        // Given
        String input = "#\"a\"";
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.parse(input, typeSystem);

        // Then
        assertEquals(Variants.Variant.ofChar('a'), result);
    }

    @Test
    public void testParse_String() {
        // Given
        String input = "\"hello\"";
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.parse(input, typeSystem);

        // Then
        assertEquals(Variants.Variant.ofString("hello"), result);
    }

    @Test
    public void testParse_List() {
        // Given
        String input = "[(), true, 42, 3.14, #\"a\", \"hello\"]";
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.parse(input, typeSystem);

        // Then
        assertEquals(Variants.Variant.ofVariantList(typeSystem, java.util.Arrays.asList(Variants.Variant.unit(), Variants.Variant.ofBool(true), Variants.Variant.ofInt(42), Variants.Variant.ofReal(3.14f), Variants.Variant.ofChar('a'), Variants.Variant.ofString("hello"))), result);
    }

    @Test
    public void testParse_Bag() {
        // Given
        String input = "bag[(), true, 42, 3.14, #\"a\", \"hello\"]";
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.parse(input, typeSystem);

        // Then
        assertEquals(Variants.Variant.ofVariantBag(typeSystem, java.util.Arrays.asList(Variants.Variant.unit(), Variants.Variant.ofBool(true), Variants.Variant.ofInt(42), Variants.Variant.ofReal(3.14f), Variants.Variant.ofChar('a'), Variants.Variant.ofString("hello"))), result);
    }

    @Test
    public void testParse_Vector() {
        // Given
        String input = "#[(), true, 42, 3.14, #\"a\", \"hello\"]";
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.parse(input, typeSystem);

        // Then
        assertEquals(Variants.Variant.ofVariantVector(typeSystem, java.util.Arrays.asList(Variants.Variant.unit(), Variants.Variant.ofBool(true), Variants.Variant.ofInt(42), Variants.Variant.ofReal(3.14f), Variants.Variant.ofChar('a'), Variants.Variant.ofString("hello"))), result);
    }

    @Test
    public void testParse_Record() {
        // Given
        String input = "{field1 = (), field2 = true, field3 = 42, field4 = 3.14, field5 = #\"a\", field6 = \"hello\"}";
        when(typeSystem.lookup(any())).thenReturn(null);

        // When
        Variants.Variant result = Variants.parse(input, typeSystem);

        // Then
        assertEquals(Variants.Variant.ofRecord(typeSystem, net.hydromatic.morel.util.PairList.fromTransformed(java.util.Arrays.asList(java.util.Arrays.asList("field1", Variants.Variant.unit()), java.util.Arrays.asList("field2", Variants.Variant.ofBool(true)), java.util.Arrays.asList("field3", Variants.Variant.ofInt(42)), java.util.Arrays.asList("field4", Variants.Variant.ofReal(3.14f)), java.util.Arrays.asList("field5", Variants.Variant.ofChar('a')), java.util.Arrays.asList("field6", Variants.Variant.ofString("hello"))), (lists, consumer) -> consumer.accept((String) lists.get(0), (Variants.Variant) lists.get(1)))), result);
    }

    @Test
    public void testParse_Unknown() {
        // Given
        String input = "unknown";
        when(typeSystem.lookup(any())).thenReturn(null);

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> Variants.parse(input, typeSystem));
    }
}
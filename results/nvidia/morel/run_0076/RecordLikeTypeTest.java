import net.hydromatic.morel.type.RecordLikeType;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import net.hydromatic.morel.type.TypedValue;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecordLikeTypeTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private Type type1;

    @Mock
    private Type type2;

    private RecordLikeType recordLikeType;

    @BeforeEach
    void setup() {
        recordLikeType = new RecordLikeType() {
            @Override
            public SortedMap<String, Type> argNameTypes() {
                SortedMap<String, Type> map = new TreeMap<>();
                map.put("field1", type1);
                map.put("field2", type2);
                return map;
            }

            @Override
            public Type argType(int i) {
                if (i == 0) {
                    return type1;
                } else if (i == 1) {
                    return type2;
                } else {
                    throw new IndexOutOfBoundsException();
                }
            }
        };
    }

    @Test
    void testArgNameTypes() {
        // Given
        SortedMap<String, Type> expected = new TreeMap<>();
        expected.put("field1", type1);
        expected.put("field2", type2);

        // When
        SortedMap<String, Type> result = recordLikeType.argNameTypes();

        // Then
        assertEquals(expected, result);
    }

    @Test
    void testArgTypes() {
        // Given
        List<Type> expected = ImmutableList.of(type1, type2);

        // When
        List<Type> result = recordLikeType.argTypes();

        // Then
        assertEquals(expected, result);
    }

    @Test
    void testArgNames() {
        // Given
        List<String> expected = ImmutableList.of("field1", "field2");

        // When
        List<String> result = recordLikeType.argNames();

        // Then
        assertEquals(expected, result);
    }

    @Test
    void testArgTypeValidIndex() {
        // Given
        int index = 0;
        Type expected = type1;

        // When
        Type result = recordLikeType.argType(index);

        // Then
        assertEquals(expected, result);
    }

    @Test
    void testArgTypeInvalidIndex() {
        // Given
        int index = 2;

        // When and Then
        assertThrows(IndexOutOfBoundsException.class, () -> recordLikeType.argType(index));
    }

    @Test
    void testAsTypedValue() {
        // Given
        @Nullable TypedValue expected = null;

        // When
        @Nullable TypedValue result = recordLikeType.asTypedValue();

        // Then
        assertEquals(expected, result);
    }

    @Test
    void testIsDiscreteAllDiscrete() {
        // Given
        when(type1.isDiscrete(any())).thenReturn(true);
        when(type2.isDiscrete(any())).thenReturn(true);

        // When
        boolean result = recordLikeType.isDiscrete(typeSystem);

        // Then
        assertTrue(result);
    }

    @Test
    void testIsDiscreteNotAllDiscrete() {
        // Given
        when(type1.isDiscrete(any())).thenReturn(true);
        when(type2.isDiscrete(any())).thenReturn(false);

        // When
        boolean result = recordLikeType.isDiscrete(typeSystem);

        // Then
        assertFalse(result);
    }
}
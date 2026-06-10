import net.hydromatic.morel.type.RecordType;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import net.hydromatic.morel.type.TypeVisitor;
import net.hydromatic.morel.util.Pair;
import net.hydromatic.morel.util.PairList;
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
public class RecordTypeTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private TypeVisitor typeVisitor;

    private RecordType recordType;

    @BeforeEach
    void setup() {
        Map<String, Type> argNameTypes = new TreeMap<>();
        argNameTypes.put("field1", mock(Type.class));
        argNameTypes.put("field2", mock(Type.class));
        recordType = new RecordType(argNameTypes);
    }

    @Test
    void testArgNameTypes() {
        // Given
        Map<String, Type> expectedArgNameTypes = new TreeMap<>();
        expectedArgNameTypes.put("field1", mock(Type.class));
        expectedArgNameTypes.put("field2", mock(Type.class));

        // When
        SortedMap<String, Type> actualArgNameTypes = recordType.argNameTypes();

        // Then
        assertEquals(expectedArgNameTypes, actualArgNameTypes);
    }

    @Test
    void testArgType() {
        // Given
        Type expectedType = mock(Type.class);
        when(recordType.argNameTypes().values().iterator().next()).thenReturn(expectedType);

        // When
        Type actualType = recordType.argType(0);

        // Then
        assertEquals(expectedType, actualType);
    }

    @Test
    void testAccept() {
        // Given
        Object expectedResult = new Object();
        when(typeVisitor.visit(any(RecordType.class))).thenReturn(expectedResult);

        // When
        Object actualResult = recordType.accept(typeVisitor);

        // Then
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testKey() {
        // Given
        Object expectedResult = new Object();

        // When
        Object actualResult = recordType.key();

        // Then
        assertNotNull(actualResult);
    }

    @Test
    void testCopy() {
        // Given
        UnaryOperator<Type> transform = type -> type;
        RecordType expectedRecordType = recordType;

        // When
        RecordType actualRecordType = recordType.copy(typeSystem, transform);

        // Then
        assertEquals(expectedRecordType, actualRecordType);
    }

    @Test
    void testSpecializes() {
        // Given
        Type type = mock(Type.class);
        when(type instanceof RecordType).thenReturn(true);
        when(((RecordType) type).argNameTypes().size()).thenReturn(2);
        when(((RecordType) type).argNameTypes().keySet()).thenReturn(recordType.argNameTypes().keySet());

        // When
        boolean actualResult = recordType.specializes(type);

        // Then
        assertTrue(actualResult);
    }

    @Test
    void testMap() {
        // Given
        String name = "field1";
        Type v0 = mock(Type.class);
        Object[] entries = new Object[]{name, v0};

        // When
        SortedMap<String, Type> actualMap = RecordType.map(name, v0);

        // Then
        assertNotNull(actualMap);
        assertEquals(1, actualMap.size());
        assertTrue(actualMap.containsKey(name));
        assertEquals(v0, actualMap.get(name));
    }

    @Test
    void testMutableMap() {
        // Given

        // When
        NavigableMap<String, Type> actualMap = RecordType.mutableMap();

        // Then
        assertNotNull(actualMap);
    }

    @Test
    void testCompareNames() {
        // Given
        String o1 = "field1";
        String o2 = "field2";

        // When
        int actualResult = RecordType.compareNames(o1, o2);

        // Then
        assertTrue(actualResult < 0);
    }
}
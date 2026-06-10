import net.hydromatic.morel.type.AliasType;
import net.hydromatic.morel.type.DataType;
import net.hydromatic.morel.type.DummyType;
import net.hydromatic.morel.type.ForallType;
import net.hydromatic.morel.type.FnType;
import net.hydromatic.morel.type.ListType;
import net.hydromatic.morel.type.PrimitiveType;
import net.hydromatic.morel.type.RecordType;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeVar;
import net.hydromatic.morel.type.TupleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TypeVisitorTest {

    @Mock
    private TypeVar typeVar;

    @Mock
    private ListType listType;

    @Mock
    private FnType fnType;

    @Mock
    private TupleType tupleType;

    @Mock
    private RecordType recordType;

    @Mock
    private DataType dataType;

    @Mock
    private AliasType aliasType;

    @Mock
    private PrimitiveType primitiveType;

    @Mock
    private ForallType forallType;

    @Mock
    private DummyType dummyType;

    @Mock
    private Type elementType;

    @Mock
    private Type paramType;

    @Mock
    private Type resultType;

    private TypeVisitor<String> typeVisitor;

    @BeforeEach
    void setup() {
        typeVisitor = new TypeVisitor<String>() {
            @Override
            public String visit(TypeVar typeVar) {
                return "TypeVar";
            }

            @Override
            public String visit(ListType listType) {
                return "ListType";
            }

            @Override
            public String visit(FnType fnType) {
                return "FnType";
            }

            @Override
            public String visit(TupleType tupleType) {
                return "TupleType";
            }

            @Override
            public String visit(RecordType recordType) {
                return "RecordType";
            }

            @Override
            public String visit(DataType dataType) {
                return "DataType";
            }

            @Override
            public String visit(AliasType aliasType) {
                return "AliasType";
            }

            @Override
            public String visit(PrimitiveType primitiveType) {
                return "PrimitiveType";
            }

            @Override
            public String visit(ForallType forallType) {
                return "ForallType";
            }

            @Override
            public String visit(DummyType dummyType) {
                return "DummyType";
            }
        };
    }

    @Test
    void testVisit_TypeVar() {
        // When
        String result = typeVisitor.visit(typeVar);

        // Then
        assertEquals("TypeVar", result);
    }

    @Test
    void testVisit_ListType() {
        // Given
        when(listType.elementType).thenReturn(elementType);

        // When
        String result = typeVisitor.visit(listType);

        // Then
        assertEquals("ListType", result);
    }

    @Test
    void testVisit_FnType() {
        // Given
        when(fnType.paramType).thenReturn(paramType);
        when(fnType.resultType).thenReturn(resultType);

        // When
        String result = typeVisitor.visit(fnType);

        // Then
        assertEquals("FnType", result);
    }

    @Test
    void testVisit_TupleType() {
        // Given
        when(tupleType.argTypes).thenReturn(new Type[]{elementType});

        // When
        String result = typeVisitor.visit(tupleType);

        // Then
        assertEquals("TupleType", result);
    }

    @Test
    void testVisit_RecordType() {
        // Given
        Map<String, Type> argNameTypes = new HashMap<>();
        argNameTypes.put("field1", elementType);
        when(recordType.argNameTypes).thenReturn(argNameTypes);

        // When
        String result = typeVisitor.visit(recordType);

        // Then
        assertEquals("RecordType", result);
    }

    @Test
    void testVisit_DataType() {
        // Given
        when(dataType.parameterTypes).thenReturn(new Type[]{elementType});

        // When
        String result = typeVisitor.visit(dataType);

        // Then
        assertEquals("DataType", result);
    }

    @Test
    void testVisit_AliasType() {
        // Given
        when(aliasType.parameterTypes).thenReturn(new Type[]{elementType});

        // When
        String result = typeVisitor.visit(aliasType);

        // Then
        assertEquals("AliasType", result);
    }

    @Test
    void testVisit_PrimitiveType() {
        // When
        String result = typeVisitor.visit(primitiveType);

        // Then
        assertEquals("PrimitiveType", result);
    }

    @Test
    void testVisit_ForallType() {
        // Given
        when(forallType.type).thenReturn(elementType);

        // When
        String result = typeVisitor.visit(forallType);

        // Then
        assertEquals("ForallType", result);
    }

    @Test
    void testVisit_DummyType() {
        // When
        String result = typeVisitor.visit(dummyType);

        // Then
        assertEquals("DummyType", result);
    }
}
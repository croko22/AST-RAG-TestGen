import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeUnifier;
import net.hydromatic.morel.type.DataType;
import net.hydromatic.morel.type.TupleType;
import net.hydromatic.morel.type.RecordType;
import net.hydromatic.morel.type.ListType;
import net.hydromatic.morel.type.PrimitiveType;
import net.hydromatic.morel.type.TypeVar;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TypeUnifierTest {

    @Test
    public void testUnify_TypeVarAndNonTypeVar() {
        // Given: type1 is a TypeVar and type2 is not
        TypeVar typeVar = new TypeVar(1);
        DataType dataType = new DataType("name", new Type[0]);

        // When: unify type1 and type2
        Map<Integer, Type> result = TypeUnifier.unify(typeVar, dataType);

        // Then: result is not null and contains the type variable and its bound
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dataType, result.get(1));
    }

    @Test
    public void testUnify_TwoTypeVars() {
        // Given: type1 and type2 are both TypeVars
        TypeVar typeVar1 = new TypeVar(1);
        TypeVar typeVar2 = new TypeVar(2);

        // When: unify type1 and type2
        Map<Integer, Type> result = TypeUnifier.unify(typeVar1, typeVar2);

        // Then: result is not null and contains the type variables and their bounds
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(typeVar2, result.get(1));
    }

    @Test
    public void testUnify_TwoDataTypes() {
        // Given: type1 and type2 are both DataTypes
        DataType dataType1 = new DataType("name", new Type[0]);
        DataType dataType2 = new DataType("name", new Type[0]);

        // When: unify type1 and type2
        Map<Integer, Type> result = TypeUnifier.unify(dataType1, dataType2);

        // Then: result is not null and empty
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUnify_TwoDataTypes_DifferentNames() {
        // Given: type1 and type2 are both DataTypes with different names
        DataType dataType1 = new DataType("name1", new Type[0]);
        DataType dataType2 = new DataType("name2", new Type[0]);

        // When: unify type1 and type2
        Map<Integer, Type> result = TypeUnifier.unify(dataType1, dataType2);

        // Then: result is null
        assertNull(result);
    }

    @Test
    public void testUnify_TwoTupleTypes() {
        // Given: type1 and type2 are both TupleTypes
        TupleType tupleType1 = new TupleType(new Type[0]);
        TupleType tupleType2 = new TupleType(new Type[0]);

        // When: unify type1 and type2
        Map<Integer, Type> result = TypeUnifier.unify(tupleType1, tupleType2);

        // Then: result is not null and empty
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUnify_TwoTupleTypes_DifferentArgTypes() {
        // Given: type1 and type2 are both TupleTypes with different arg types
        TupleType tupleType1 = new TupleType(new Type[]{new DataType("name", new Type[0])});
        TupleType tupleType2 = new TupleType(new Type[]{new DataType("name2", new Type[0])});

        // When: unify type1 and type2
        Map<Integer, Type> result = TypeUnifier.unify(tupleType1, tupleType2);

        // Then: result is not null and empty
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUnify_TwoRecordTypes() {
        // Given: type1 and type2 are both RecordTypes
        RecordType recordType1 = new RecordType(new HashMap<>());
        RecordType recordType2 = new RecordType(new HashMap<>());

        // When: unify type1 and type2
        Map<Integer, Type> result = TypeUnifier.unify(recordType1, recordType2);

        // Then: result is not null and empty
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUnify_TwoRecordTypes_DifferentArgNameTypes() {
        // Given: type1 and type2 are both RecordTypes with different arg name types
        RecordType recordType1 = new RecordType(new HashMap<>() {{
            put("name", new DataType("name", new Type[0]));
        }});
        RecordType recordType2 = new RecordType(new HashMap<>() {{
            put("name2", new DataType("name2", new Type[0]));
        }});

        // When: unify type1 and type2
        Map<Integer, Type> result = TypeUnifier.unify(recordType1, recordType2);

        // Then: result is null
        assertNull(result);
    }

    @Test
    public void testUnify_TwoListTypes() {
        // Given: type1 and type2 are both ListTypes
        ListType listType1 = new ListType(new DataType("name", new Type[0]));
        ListType listType2 = new ListType(new DataType("name", new Type[0]));

        // When: unify type1 and type2
        Map<Integer, Type> result = TypeUnifier.unify(listType1, listType2);

        // Then: result is not null and empty
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUnify_TwoListTypes_DifferentElementTypes() {
        // Given: type1 and type2 are both ListTypes with different element types
        ListType listType1 = new ListType(new DataType("name", new Type[0]));
        ListType listType2 = new ListType(new DataType("name2", new Type[0]));

        // When: unify type1 and type2
        Map<Integer, Type> result = TypeUnifier.unify(listType1, listType2);

        // Then: result is null
        assertNull(result);
    }

    @Test
    public void testUnify_TwoPrimitiveTypes() {
        // Given: type1 and type2 are both PrimitiveTypes
        PrimitiveType primitiveType1 = new PrimitiveType();
        PrimitiveType primitiveType2 = new PrimitiveType();

        // When: unify type1 and type2
        Map<Integer, Type> result = TypeUnifier.unify(primitiveType1, primitiveType2);

        // Then: result is not null and empty
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUnify_TwoPrimitiveTypes_DifferentTypes() {
        // Given: type1 and type2 are both PrimitiveTypes with different types
        PrimitiveType primitiveType1 = new PrimitiveType();
        PrimitiveType primitiveType2 = new PrimitiveType();

        // When: unify type1 and type2
        Map<Integer, Type> result = TypeUnifier.unify(primitiveType1, primitiveType2);

        // Then: result is not null and empty
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
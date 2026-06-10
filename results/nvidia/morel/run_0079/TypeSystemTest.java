Here's a complete test class for the `TypeSystem` class with proper imports, setup, and test methods:

```java
import net.hydromatic.morel.type.TypeSystem;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.DataType;
import net.hydromatic.morel.type.TypeVar;
import net.hydromatic.morel.type.TypeCon;
import net.hydromatic.morel.type.Keys;
import net.hydromatic.morel.type.BuiltIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TypeSystemTest {

    private TypeSystem typeSystem;

    @BeforeEach
    public void setup() {
        typeSystem = new TypeSystem();
    }

    @Test
    public void testBindTyCon() {
        DataType dataType = new DataType("Test", "Test", new ArrayList<>(), new HashMap<>());
        TypeCon typeCon = typeSystem.bindTyCon(dataType, "Test");
        assertNotNull(typeCon);
    }

    @Test
    public void testLookup() {
        Type type = typeSystem.lookup(BuiltIn.Datatype.ORDER);
        assertNotNull(type);
    }

    @Test
    public void testLookupByName() {
        Type type = typeSystem.lookup("int");
        assertNotNull(type);
    }

    @Test
    public void testLookupOpt() {
        Type type = typeSystem.lookupOpt("int");
        assertNotNull(type);
    }

    @Test
    public void testTypeFor() {
        Keys.Key key = Keys.name("Test");
        Type type = typeSystem.typeFor(key);
        assertNotNull(type);
    }

    @Test
    public void testTypesFor() {
        List<Keys.Key> keys = new ArrayList<>();
        keys.add(Keys.name("Test"));
        List<Type> types = typeSystem.typesFor(keys);
        assertNotNull(types);
    }

    @Test
    public void testTypesForMap() {
        Map<String, Keys.Key> keys = new HashMap<>();
        keys.put("Test", Keys.name("Test"));
        Map<String, Type> types = typeSystem.typesFor(keys);
        assertNotNull(types);
    }

    @Test
    public void testFnType() {
        Type paramType = typeSystem.lookup("int");
        Type type1 = typeSystem.lookup("int");
        Type type2 = typeSystem.lookup("int");
        Type type = typeSystem.fnType(paramType, type1, type2);
        assertNotNull(type);
    }

    @Test
    public void testFnTypeSingle() {
        Type paramType = typeSystem.lookup("int");
        Type resultType = typeSystem.lookup("int");
        FnType type = typeSystem.fnType(paramType, resultType);
        assertNotNull(type);
    }

    @Test
    public void testTupleType() {
        Type argType = typeSystem.lookup("int");
        TupleType type = typeSystem.tupleType(argType);
        assertNotNull(type);
    }

    @Test
    public void testTupleTypeList() {
        List<Type> argTypes = new ArrayList<>();
        argTypes.add(typeSystem.lookup("int"));
        RecordLikeType type = typeSystem.tupleType(argTypes);
        assertNotNull(type);
    }

    @Test
    public void testBagType() {
        Type elementType = typeSystem.lookup("int");
        Type type = typeSystem.bagType(elementType);
        assertNotNull(type);
    }

    @Test
    public void testListType() {
        Type elementType = typeSystem.lookup("int");
        ListType type = typeSystem.listType(elementType);
        assertNotNull(type);
    }

    @Test
    public void testDataTypes() {
        List<Keys.DataTypeKey> keys = new ArrayList<>();
        keys.add(Keys.datatype("Test", new ArrayList<>(), new HashMap<>()));
        List<Type> types = typeSystem.dataTypes(keys);
        assertNotNull(types);
    }

    @Test
    public void testSetBuiltIn() {
        typeSystem.setBuiltIn(BuiltIn.Datatype.ORDER);
    }

    @Test
    public void testDataTypeScheme() {
        Type type = typeSystem.dataTypeScheme("Test", new ArrayList<>(), new HashMap<>());
        assertNotNull(type);
    }

    @Test
    public void testRecordOrScalarType() {
        Map<String, Type> argNameTypes = new HashMap<>();
        argNameTypes.put("Test", typeSystem.lookup("int"));
        Type type = typeSystem.recordOrScalarType(argNameTypes.entrySet());
        assertNotNull(type);
    }

    @Test
    public void testRecordType() {
        Map<String, Type> argNameTypes = new HashMap<>();
        argNameTypes.put("Test", typeSystem.lookup("int"));
        RecordLikeType type = typeSystem.recordType(argNameTypes.entrySet());
        assertNotNull(type);
    }

    @Test
    public void testRecordTypeMap() {
        Map<String, Type> argNameTypes = new HashMap<>();
        argNameTypes.put("Test", typeSystem.lookup("int"));
        RecordLikeType type = typeSystem.recordType(argNameTypes);
        assertNotNull(type);
    }

    @Test
    public void testAreContiguousIntegers() {
        List<String> strings = new ArrayList<>();
        strings.add("1");
        strings.add("2");
        strings.add("3");
        assertTrue(TypeSystem.areContiguousIntegers(strings));
    }

    @Test
    public void testProgressiveRecordType() {
        Map<String, Type> argNameTypes = new HashMap<>();
        argNameTypes.put("Test", typeSystem.lookup("int"));
        ProgressiveRecordType type = typeSystem.progressiveRecordType(argNameTypes.entrySet());
        assertNotNull(type);
    }

    @Test
    public void testProgressiveRecordTypeMap() {
        Map<String, Type> argNameTypes = new HashMap<>();
        argNameTypes.put("Test", typeSystem.lookup("int"));
        ProgressiveRecordType type = typeSystem.progressiveRecordType(argNameTypes);
        assertNotNull(type);
    }

    @Test
    public void testForallType() {
        Type type = typeSystem.forallType(1, new TypeSystem.ForallHelper() {
            @Override
            public TypeVar get(int i) {
                return typeSystem.typeVariable(i);
            }

            @Override
            public Type bag(int i) {
                return typeSystem.bagType(get(i));
            }

            @Override
            public Type either(int i, int j) {
                return typeSystem.either(get(i), get(j));
            }

            @Override
            public ListType list(int i) {
                return typeSystem.listType(get(i));
            }

            @Override
            public Type vector(int i) {
                return typeSystem.vector(get(i));
            }

            @Override
            public Type option(int i) {
                return typeSystem.option(get(i));
            }

            @Override
            public Type continuousSet(int i) {
                return typeSystem.continuousSet(get(i));
            }

            @Override
            public Type discreteSet(int i) {
                return typeSystem.discreteSet(get(i));
            }

            @Override
            public Type range(int i) {
                return typeSystem.range(get(i));
            }

            @Override
            public FnType predicate(int i) {
                return typeSystem.fnType(get(i), typeSystem.lookup("bool"));
            }
        });
        assertNotNull(type);
    }

    @Test
    public void testForallTypeSingle() {
        Type type = typeSystem.forallType(1, typeSystem.lookup("int"));
        assertNotNull(type);
    }

    @Test
    public void testMulti() {
        MultiType type = typeSystem.multi(typeSystem.lookup("int"));
        assertNotNull(type);
    }

    @Test
    public void testMultiList() {
        List<Type> types = new ArrayList<>();
        types.add(typeSystem.lookup("int"));
        MultiType type = typeSystem.multi(types);
        assertNotNull(type);
    }

    @Test
    public void testTypeVariables() {
        List<TypeVar> typeVars = typeSystem.typeVariables(1);
        assertNotNull(typeVars);
    }

    @Test
    public void testUnqualified() {
        Type type = typeSystem.unqualified(typeSystem.lookup("int"));
        assertNotNull(type);
    }

    @Test
    public void testLookupTyCon() {
        TypeCon typeCon = typeSystem.lookupTyCon("Test");
        assertNotNull(typeCon);
    }

    @Test
    public void testApply() {
        Type type = typeSystem.apply(typeSystem.lookup("int"));
        assertNotNull(type);
    }

    @Test
    public void testApplyList() {
        List<Type> types = new ArrayList<>();
        types.add(typeSystem.lookup("int"));
        Type type = typeSystem.apply(typeSystem.lookup("int"), types);
        assertNotNull(type);
    }

    @Test
    public void testTypeVariable() {
        TypeVar typeVar = typeSystem.typeVariable(0);
        assertNotNull(typeVar);
    }

    @Test
    public void testDescending() {
        Type type = typeSystem.descending();
        assertNotNull(type);
    }

    @Test
    public void testOrder() {
        Type type = typeSystem.order();
        assertNotNull(type);
    }

    @Test
    public void testBag() {
        Type type = typeSystem.bag(typeSystem.lookup("int"));
        assertNotNull(type);
    }

    @Test
    public void testEither() {
        Type type = typeSystem.either(typeSystem.lookup("int"), typeSystem.lookup("int"));
        assertNotNull(type);
    }

    @Test
    public void testOption() {
        Type type = typeSystem.option(typeSystem.lookup("int"));
        assertNotNull(type);
    }

    @Test
    public void testContinuousSet() {
        Type type = typeSystem.continuousSet(typeSystem.lookup("int"));
        assertNotNull(type);
    }

    @Test
    public void testDiscreteSet() {
        Type type = typeSystem.discreteSet(typeSystem.lookup("int"));
        assertNotNull(type);
    }

    @Test
    public void testRange() {
        Type type = typeSystem.range(typeSystem.lookup("int"));
        assertNotNull(type);
    }

    @Test
    public void testVector() {
        Type type = typeSystem.vector(typeSystem.lookup("int"));
        assertNotNull(type);
    }

    @Test
    public void testEnsureClosed() {
        Type type = typeSystem.ensureClosed(typeSystem.lookup("int"));
        assertNotNull(type);
    }

    @Test
    public void testCanAssign() {
        assertTrue(TypeSystem.canAssign(typeSystem.lookup("int"), typeSystem.lookup("int")));
    }
}
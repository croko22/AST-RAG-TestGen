import net.hydromatic.morel.compile.OutputMatcher;
import net.hydromatic.morel.type.DataType;
import net.hydromatic.morel.type.ListType;
import net.hydromatic.morel.type.RecordType;
import net.hydromatic.morel.type.TupleType;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OutputMatcherTest {

    @Mock
    private TypeSystem typeSystem;

    private OutputMatcher outputMatcher;

    @BeforeEach
    public void setup() {
        outputMatcher = new OutputMatcher(typeSystem);
    }

    @Test
    public void testEquivalent_SimpleTypes() {
        // Given
        Type type = new DataType() {
            @Override
            public Type arg(int i) {
                return null;
            }

            @Override
            public boolean isCollection() {
                return false;
            }

            @Override
            public Type elementType() {
                return null;
            }

            @Override
            public Map<String, Type> typeConstructors(TypeSystem typeSystem) {
                return null;
            }

            @Override
            public boolean isDiscrete(TypeSystem typeSystem) {
                return false;
            }

            @Override
            public DataType copy(TypeSystem typeSystem, UnaryOperator<Type> transform) {
                return null;
            }

            @Override
            public StringBuilder describe(StringBuilder buf) {
                return null;
            }

            @Override
            public boolean specializes(Type type) {
                return false;
            }
        };
        String actual = "1";
        String expected = "1";

        // When
        boolean result = outputMatcher.equivalent(type, actual, expected);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquivalent_ComplexTypes() {
        // Given
        Type type = new ListType() {
            @Override
            public Key key() {
                return null;
            }

            @Override
            public Type arg(int i) {
                return null;
            }

            @Override
            public boolean isCollection() {
                return true;
            }

            @Override
            public Type elementType() {
                return null;
            }

            @Override
            public <R> R accept(TypeVisitor<R> typeVisitor) {
                return null;
            }

            @Override
            public ListType copy(TypeSystem typeSystem, UnaryOperator<Type> transform) {
                return null;
            }

            @Override
            public boolean specializes(Type type) {
                return false;
            }
        };
        String actual = "[1, 2, 3]";
        String expected = "[1, 2, 3]";

        // When
        boolean result = outputMatcher.equivalent(type, actual, expected);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquivalent_DifferentTypes() {
        // Given
        Type type = new DataType() {
            @Override
            public Type arg(int i) {
                return null;
            }

            @Override
            public boolean isCollection() {
                return false;
            }

            @Override
            public Type elementType() {
                return null;
            }

            @Override
            public Map<String, Type> typeConstructors(TypeSystem typeSystem) {
                return null;
            }

            @Override
            public boolean isDiscrete(TypeSystem typeSystem) {
                return false;
            }

            @Override
            public DataType copy(TypeSystem typeSystem, UnaryOperator<Type> transform) {
                return null;
            }

            @Override
            public StringBuilder describe(StringBuilder buf) {
                return null;
            }

            @Override
            public boolean specializes(Type type) {
                return false;
            }
        };
        String actual = "1";
        String expected = "2";

        // When
        boolean result = outputMatcher.equivalent(type, actual, expected);

        // Then
        assertFalse(result);
    }

    @Test
    public void testCodeEqual_SimpleTypes() {
        // Given
        Type type = new DataType() {
            @Override
            public Type arg(int i) {
                return null;
            }

            @Override
            public boolean isCollection() {
                return false;
            }

            @Override
            public Type elementType() {
                return null;
            }

            @Override
            public Map<String, Type> typeConstructors(TypeSystem typeSystem) {
                return null;
            }

            @Override
            public boolean isDiscrete(TypeSystem typeSystem) {
                return false;
            }

            @Override
            public DataType copy(TypeSystem typeSystem, UnaryOperator<Type> transform) {
                return null;
            }

            @Override
            public StringBuilder describe(StringBuilder buf) {
                return null;
            }

            @Override
            public boolean specializes(Type type) {
                return false;
            }
        };
        String code0 = "1";
        String code1 = "1";

        // When
        boolean result = outputMatcher.codeEqual(type, code0, code1);

        // Then
        assertTrue(result);
    }

    @Test
    public void testCodeEqual_ComplexTypes() {
        // Given
        Type type = new ListType() {
            @Override
            public Key key() {
                return null;
            }

            @Override
            public Type arg(int i) {
                return null;
            }

            @Override
            public boolean isCollection() {
                return true;
            }

            @Override
            public Type elementType() {
                return null;
            }

            @Override
            public <R> R accept(TypeVisitor<R> typeVisitor) {
                return null;
            }

            @Override
            public ListType copy(TypeSystem typeSystem, UnaryOperator<Type> transform) {
                return null;
            }

            @Override
            public boolean specializes(Type type) {
                return false;
            }
        };
        String code0 = "[1, 2, 3]";
        String code1 = "[1, 2, 3]";

        // When
        boolean result = outputMatcher.codeEqual(type, code0, code1);

        // Then
        assertTrue(result);
    }

    @Test
    public void testCodeEqual_DifferentTypes() {
        // Given
        Type type = new DataType() {
            @Override
            public Type arg(int i) {
                return null;
            }

            @Override
            public boolean isCollection() {
                return false;
            }

            @Override
            public Type elementType() {
                return null;
            }

            @Override
            public Map<String, Type> typeConstructors(TypeSystem typeSystem) {
                return null;
            }

            @Override
            public boolean isDiscrete(TypeSystem typeSystem) {
                return false;
            }

            @Override
            public DataType copy(TypeSystem typeSystem, UnaryOperator<Type> transform) {
                return null;
            }

            @Override
            public StringBuilder describe(StringBuilder buf) {
                return null;
            }

            @Override
            public boolean specializes(Type type) {
                return false;
            }
        };
        String code0 = "1";
        String code1 = "2";

        // When
        boolean result = outputMatcher.codeEqual(type, code0, code1);

        // Then
        assertFalse(result);
    }
}
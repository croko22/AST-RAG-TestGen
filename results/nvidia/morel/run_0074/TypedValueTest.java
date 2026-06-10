import net.hydromatic.morel.type.TypedValue;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TypedValueTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private Type.Key typeKey;

    @Test
    public void testValueAs_SimpleCase() {
        // Given
        TypedValue typedValue = (TypedValue) Proxy.newProxyInstance(
                TypedValue.class.getClassLoader(),
                new Class[]{TypedValue.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("valueAs")) {
                        return "test";
                    } else {
                        throw new UnsupportedOperationException();
                    }
                });

        // When
        String result = typedValue.valueAs(String.class);

        // Then
        assertNotNull(result);
        assertEquals("test", result);
    }

    @Test
    public void testValueAs_InvalidClass() {
        // Given
        TypedValue typedValue = (TypedValue) Proxy.newProxyInstance(
                TypedValue.class.getClassLoader(),
                new Class[]{TypedValue.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("valueAs")) {
                        throw new ClassCastException("Invalid class");
                    } else {
                        throw new UnsupportedOperationException();
                    }
                });

        // When / Then
        assertThrows(ClassCastException.class, () -> typedValue.valueAs(String.class));
    }

    @Test
    public void testFieldValueAs_StringField() {
        // Given
        TypedValue typedValue = (TypedValue) Proxy.newProxyInstance(
                TypedValue.class.getClassLoader(),
                new Class[]{TypedValue.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("fieldValueAs")) {
                        throw new UnsupportedOperationException("not a record");
                    } else {
                        throw new UnsupportedOperationException();
                    }
                });

        // When / Then
        assertThrows(UnsupportedOperationException.class, () -> typedValue.fieldValueAs("field", String.class));
    }

    @Test
    public void testFieldValueAs_OrdinalField() {
        // Given
        TypedValue typedValue = (TypedValue) Proxy.newProxyInstance(
                TypedValue.class.getClassLoader(),
                new Class[]{TypedValue.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("fieldValueAs")) {
                        throw new UnsupportedOperationException("not a record");
                    } else {
                        throw new UnsupportedOperationException();
                    }
                });

        // When / Then
        assertThrows(UnsupportedOperationException.class, () -> typedValue.fieldValueAs(1, String.class));
    }

    @Test
    public void testTypeKey() {
        // Given
        TypedValue typedValue = (TypedValue) Proxy.newProxyInstance(
                TypedValue.class.getClassLoader(),
                new Class[]{TypedValue.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("typeKey")) {
                        return typeKey;
                    } else {
                        throw new UnsupportedOperationException();
                    }
                });

        // When
        Type.Key result = typedValue.typeKey();

        // Then
        assertNotNull(result);
        assertEquals(typeKey, result);
    }

    @Test
    public void testDiscoverField() {
        // Given
        TypedValue typedValue = (TypedValue) Proxy.newProxyInstance(
                TypedValue.class.getClassLoader(),
                new Class[]{TypedValue.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("discoverField")) {
                        return typedValue;
                    } else {
                        throw new UnsupportedOperationException();
                    }
                });

        // When
        TypedValue result = typedValue.discoverField(typeSystem, "field");

        // Then
        assertNotNull(result);
        assertEquals(typedValue, result);
    }
}
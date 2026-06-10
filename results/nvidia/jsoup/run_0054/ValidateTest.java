import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ValidateTest {

    @Test
    public void testNotNull_ObjectNotNull() {
        // Given
        Object obj = new Object();

        // When
        Validate.notNull(obj);

        // Then
        // No exception expected
    }

    @Test
    public void testNotNull_ObjectNull() {
        // Given
        Object obj = null;

        // When / Then
        assertThrows(ValidationException.class, () -> Validate.notNull(obj));
    }

    @Test
    public void testNotNullParam_ObjectNotNull() {
        // Given
        Object obj = new Object();
        String param = "param";

        // When
        Validate.notNullParam(obj, param);

        // Then
        // No exception expected
    }

    @Test
    public void testNotNullParam_ObjectNull() {
        // Given
        Object obj = null;
        String param = "param";

        // When / Then
        assertThrows(ValidationException.class, () -> Validate.notNullParam(obj, param));
    }

    @Test
    public void testNotNull_CustomMessage_ObjectNotNull() {
        // Given
        Object obj = new Object();
        String msg = "Custom message";

        // When
        Validate.notNull(obj, msg);

        // Then
        // No exception expected
    }

    @Test
    public void testNotNull_CustomMessage_ObjectNull() {
        // Given
        Object obj = null;
        String msg = "Custom message";

        // When / Then
        assertThrows(ValidationException.class, () -> Validate.notNull(obj, msg));
    }

    @Test
    public void testEnsureNotNull_ObjectNotNull() {
        // Given
        Object obj = new Object();

        // When
        Object result = Validate.ensureNotNull(obj);

        // Then
        assertNotNull(result);
        assertSame(obj, result);
    }

    @Test
    public void testEnsureNotNull_ObjectNull() {
        // Given
        Object obj = null;

        // When / Then
        assertThrows(ValidationException.class, () -> Validate.ensureNotNull(obj));
    }

    @Test
    public void testEnsureNotNull_CustomMessage_ObjectNotNull() {
        // Given
        Object obj = new Object();
        String msg = "Custom message";

        // When
        Object result = Validate.ensureNotNull(obj, msg);

        // Then
        assertNotNull(result);
        assertSame(obj, result);
    }

    @Test
    public void testEnsureNotNull_CustomMessage_ObjectNull() {
        // Given
        Object obj = null;
        String msg = "Custom message";

        // When / Then
        assertThrows(ValidationException.class, () -> Validate.ensureNotNull(obj, msg));
    }

    @Test
    public void testExpectNotNull_ObjectNotNull() {
        // Given
        Object obj = new Object();

        // When
        Object result = Validate.expectNotNull(obj);

        // Then
        assertNotNull(result);
        assertSame(obj, result);
    }

    @Test
    public void testExpectNotNull_ObjectNull() {
        // Given
        Object obj = null;

        // When / Then
        assertThrows(ValidationException.class, () -> Validate.expectNotNull(obj));
    }

    @Test
    public void testExpectNotNull_CustomMessage_ObjectNotNull() {
        // Given
        Object obj = new Object();
        String msg = "Custom message";

        // When
        Object result = Validate.expectNotNull(obj, msg);

        // Then
        assertNotNull(result);
        assertSame(obj, result);
    }

    @Test
    public void testExpectNotNull_CustomMessage_ObjectNull() {
        // Given
        Object obj = null;
        String msg = "Custom message";

        // When / Then
        assertThrows(ValidationException.class, () -> Validate.expectNotNull(obj, msg));
    }

    @Test
    public void testIsTrue_ValueTrue() {
        // Given
        boolean val = true;

        // When
        Validate.isTrue(val);

        // Then
        // No exception expected
    }

    @Test
    public void testIsTrue_ValueFalse() {
        // Given
        boolean val = false;

        // When / Then
        assertThrows(ValidationException.class, () -> Validate.isTrue(val));
    }

    @Test
    public void testIsTrue_CustomMessage_ValueTrue() {
        // Given
        boolean val = true;
        String msg = "Custom message";

        // When
        Validate.isTrue(val, msg);

        // Then
        // No exception expected
    }

    @Test
    public void testIsTrue_CustomMessage_ValueFalse() {
        // Given
        boolean val = false;
        String msg = "Custom message";

        // When / Then
        assertThrows(ValidationException.class, () -> Validate.isTrue(val, msg));
    }

    @Test
    public void testIsFalse_ValueFalse() {
        // Given
        boolean val = false;

        // When
        Validate.isFalse(val);

        // Then
        // No exception expected
    }

    @Test
    public void testIsFalse_ValueTrue() {
        // Given
        boolean val = true;

        // When / Then
        assertThrows(ValidationException.class, () -> Validate.isFalse(val));
    }

    @Test
    public void testIsFalse_CustomMessage_ValueFalse() {
        // Given
        boolean val = false;
        String msg = "Custom message";

        // When
        Validate.isFalse(val, msg);

        // Then
        // No exception expected
    }

    @Test
    public void testIsFalse_CustomMessage_ValueTrue() {
        // Given
        boolean val = true;
        String msg = "Custom message";

        // When / Then
        assertThrows(ValidationException.class, () -> Validate.isFalse(val, msg));
    }

    @Test
    public void testNoNullElements_ArrayWithoutNullElements() {
        // Given
        Object[] objects = {new Object(), new Object()};

        // When
        Validate.noNullElements(objects);

        // Then
        // No exception expected
    }

    @Test
    public void testNoNullElements_ArrayWithNullElements() {
        // Given
        Object[] objects = {new Object(), null};

        // When / Then
        assertThrows(ValidationException.class, () -> Validate.noNullElements(objects));
    }

    @Test
    public void testNoNullElements_CustomMessage_ArrayWithoutNullElements() {
        // Given
        Object[] objects = {new Object(), new Object()};
        String msg = "Custom message";

        // When
        Validate.noNullElements(objects, msg);

        // Then
        // No exception expected
    }

    @Test
    public void testNoNullElements_CustomMessage_ArrayWithNullElements() {
        // Given
        Object[] objects = {new Object(), null};
        String msg = "Custom message";

        // When / Then
        assertThrows(ValidationException.class, () -> Validate.noNullElements(objects, msg));
    }

    @Test
    public void testNotEmpty_StringNotEmpty() {
        // Given
        String string = "Hello";

        // When
        Validate.notEmpty(string);

        // Then
        // No exception expected
    }

    @Test
    public void testNotEmpty_StringEmpty() {
        // Given
        String string = "";

        // When / Then
        assertThrows(ValidationException.class, () -> Validate.notEmpty(string));
    }

    @Test
    public void testNotEmpty_StringNull() {
        // Given
        String string = null;

        // When / Then
        assertThrows(ValidationException.class, () -> Validate.notEmpty(string));
    }

    @Test
    public void testNotEmptyParam_StringNotEmpty() {
        // Given
        String string = "Hello";
        String param = "param";

        // When
        Validate.notEmptyParam(string, param);

        // Then
        // No exception expected
    }

    @Test
    public void testNotEmptyParam_StringEmpty() {
        // Given
        String string = "";
        String param = "param";

        // When / Then
        assertThrows(ValidationException.class, () -> Validate.notEmptyParam(string, param));
    }

    @Test
    public void testNotEmptyParam_StringNull() {
        // Given
        String string = null;
        String param = "param";

        // When / Then
        assertThrows(ValidationException.class, () -> Validate.notEmptyParam(string, param));
    }

    @Test
    public void testNotEmpty_CustomMessage_StringNotEmpty() {
        // Given
        String string = "Hello";
        String msg = "Custom message";

        // When
        Validate.notEmpty(string, msg);

        // Then
        // No exception expected
    }

    @Test
    public void testNotEmpty_CustomMessage_StringEmpty() {
        // Given
        String string = "";
        String msg = "Custom message";

        // When / Then
        assertThrows(ValidationException.class, () -> Validate.notEmpty(string, msg));
    }

    @Test
    public void testNotEmpty_CustomMessage_StringNull() {
        // Given
        String string = null;
        String msg = "Custom message";

        // When / Then
        assertThrows(ValidationException.class, () -> Validate.notEmpty(string, msg));
    }

    @Test
    public void testWtf() {
        // Given
        String msg = "WTF";

        // When / Then
        assertThrows(IllegalStateException.class, () -> Validate.wtf(msg));
    }

    @Test
    public void testFail() {
        // Given
        String msg = "Fail";

        // When / Then
        assertThrows(ValidationException.class, () -> Validate.fail(msg));
    }
}
import net.hydromatic.morel.eval.Describer;
import net.hydromatic.morel.eval.DescriberImpl;
import net.hydromatic.morel.eval.Detail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DescriberImplTest {

    private DescriberImpl describer;

    @BeforeEach
    public void setup() {
        describer = new DescriberImpl();
    }

    @Test
    public void testToString() {
        // Given: describer is empty
        // When: toString is called
        String result = describer.toString();
        // Then: result is empty string
        assertEquals("", result);
    }

    @Test
    public void testStart() {
        // Given: name and consumer
        String name = "test";
        Consumer<Detail> consumer = detail -> detail.arg("arg1", "value1");
        // When: start is called
        Describer result = describer.start(name, consumer);
        // Then: result is not null and toString contains name and arg
        assertNotNull(result);
        assertTrue(result.toString().contains(name));
        assertTrue(result.toString().contains("arg1"));
        assertTrue(result.toString().contains("value1"));
    }

    @Test
    public void testRegister() {
        // Given: name and i
        String name = "test";
        int i = 1;
        // When: register is called
        int result = describer.register(name, i);
        // Then: result is 0
        assertEquals(0, result);
    }

    @Test
    public void testRegister_Duplicate() {
        // Given: name and i
        String name = "test";
        int i = 1;
        describer.register(name, i);
        // When: register is called again with same i
        int result = describer.register(name, i);
        // Then: result is 0
        assertEquals(0, result);
    }

    @Test
    public void testRegister_Multiple() {
        // Given: name and i
        String name = "test";
        int i = 1;
        describer.register(name, i);
        // When: register is called again with different i
        int result = describer.register(name, 2);
        // Then: result is 1
        assertEquals(1, result);
    }

    @Test
    public void testArg() {
        // Given: name and value
        String name = "arg1";
        Object value = "value1";
        // When: arg is called
        Detail result = describer.start("test", detail -> detail.arg(name, value));
        // Then: result is not null and toString contains name and value
        assertNotNull(result);
        assertTrue(result.toString().contains(name));
        assertTrue(result.toString().contains(value.toString()));
    }

    @Test
    public void testArgs() {
        // Given: name and values
        String name = "args1";
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        // When: args is called
        Detail result = describer.start("test", detail -> detail.args(name, values));
        // Then: result is not null and toString contains name and values
        assertNotNull(result);
        assertTrue(result.toString().contains(name));
        assertTrue(result.toString().contains(values.get(0)));
        assertTrue(result.toString().contains(values.get(1)));
    }

    @Test
    public void testArg_Describable() {
        // Given: name and describable
        String name = "arg1";
        DescriberImpl describable = new DescriberImpl();
        describable.start("test", detail -> detail.arg("arg2", "value2"));
        // When: arg is called
        Detail result = describer.start("test", detail -> detail.arg(name, describable));
        // Then: result is not null and toString contains name and describable
        assertNotNull(result);
        assertTrue(result.toString().contains(name));
        assertTrue(result.toString().contains(describable.toString()));
    }
}
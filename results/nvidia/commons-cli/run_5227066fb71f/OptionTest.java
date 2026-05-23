import org.apache.commons.cli.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OptionTest {

    private Option option;

    @BeforeEach
    public void setup() {
        option = Option.builder("a").build();
    }

    @Test
    public void testBuilder() {
        // Given
        String optionName = "a";
        String description = "This is a test option";
        String longOption = "test-option";

        // When
        Option.Builder builder = Option.builder(optionName);
        builder.desc(description);
        builder.longOpt(longOption);
        Option option = builder.build();

        // Then
        assertEquals(optionName, option.getOpt());
        assertEquals(description, option.getDescription());
        assertEquals(longOption, option.getLongOpt());
    }

    @Test
    public void testArgName() {
        // Given
        String argName = "test-arg";

        // When
        Option.Builder builder = Option.builder("a");
        builder.argName(argName);
        Option option = builder.build();

        // Then
        assertEquals(argName, option.getArgName());
    }

    @Test
    public void testHasArg() {
        // Given
        boolean hasArg = true;

        // When
        Option.Builder builder = Option.builder("a");
        builder.hasArg(hasArg);
        Option option = builder.build();

        // Then
        assertEquals(hasArg, option.hasArg());
    }

    @Test
    public void testHasArgs() {
        // Given
        int numberOfArgs = 2;

        // When
        Option.Builder builder = Option.builder("a");
        builder.numberOfArgs(numberOfArgs);
        Option option = builder.build();

        // Then
        assertEquals(numberOfArgs, option.getArgs());
    }

    @Test
    public void testLongOpt() {
        // Given
        String longOpt = "test-long-opt";

        // When
        Option.Builder builder = Option.builder("a");
        builder.longOpt(longOpt);
        Option option = builder.build();

        // Then
        assertEquals(longOpt, option.getLongOpt());
    }

    @Test
    public void testOptionalArg() {
        // Given
        boolean isOptional = true;

        // When
        Option.Builder builder = Option.builder("a");
        builder.optionalArg(isOptional);
        Option option = builder.build();

        // Then
        assertEquals(isOptional, option.hasOptionalArg());
    }

    @Test
    public void testRequired() {
        // Given
        boolean required = true;

        // When
        Option.Builder builder = Option.builder("a");
        builder.required(required);
        Option option = builder.build();

        // Then
        assertEquals(required, option.isRequired());
    }

    @Test
    public void testType() {
        // Given
        Class<?> type = String.class;

        // When
        Option.Builder builder = Option.builder("a");
        builder.type(type);
        Option option = builder.build();

        // Then
        assertEquals(type, option.getType());
    }

    @Test
    public void testValueSeparator() {
        // Given
        char sep = '=';

        // When
        Option.Builder builder = Option.builder("a");
        builder.valueSeparator(sep);
        Option option = builder.build();

        // Then
        assertEquals(sep, option.getValueSeparator());
    }

    @Test
    public void testEquals() {
        // Given
        Option option1 = Option.builder("a").build();
        Option option2 = Option.builder("a").build();

        // When
        boolean equals = option1.equals(option2);

        // Then
        assertTrue(equals);
    }

    @Test
    public void testHashCode() {
        // Given
        Option option1 = Option.builder("a").build();
        Option option2 = Option.builder("a").build();

        // When
        int hashCode1 = option1.hashCode();
        int hashCode2 = option2.hashCode();

        // Then
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        // Given
        Option option = Option.builder("a").build();

        // When
        String toString = option.toString();

        // Then
        assertNotNull(toString);
    }

    @Test
    public void testGetArgName() {
        // Given
        String argName = "test-arg";
        Option option = Option.builder("a").argName(argName).build();

        // When
        String result = option.getArgName();

        // Then
        assertEquals(argName, result);
    }

    @Test
    public void testGetArgs() {
        // Given
        int numberOfArgs = 2;
        Option option = Option.builder("a").numberOfArgs(numberOfArgs).build();

        // When
        int result = option.getArgs();

        // Then
        assertEquals(numberOfArgs, result);
    }

    @Test
    public void testGetDescription() {
        // Given
        String description = "This is a test option";
        Option option = Option.builder("a").desc(description).build();

        // When
        String result = option.getDescription();

        // Then
        assertEquals(description, result);
    }

    @Test
    public void testGetId() {
        // Given
        Option option = Option.builder("a").build();

        // When
        int result = option.getId();

        // Then
        assertEquals('a', result);
    }

    @Test
    public void testGetLongOpt() {
        // Given
        String longOpt = "test-long-opt";
        Option option = Option.builder("a").longOpt(longOpt).build();

        // When
        String result = option.getLongOpt();

        // Then
        assertEquals(longOpt, result);
    }

    @Test
    public void testGetOpt() {
        // Given
        Option option = Option.builder("a").build();

        // When
        String result = option.getOpt();

        // Then
        assertEquals("a", result);
    }

    @Test
    public void testGetType() {
        // Given
        Class<?> type = String.class;
        Option option = Option.builder("a").type(type).build();

        // When
        Object result = option.getType();

        // Then
        assertEquals(type, result);
    }

    @Test
    public void testGetValue() {
        // Given
        String value = "test-value";
        Option option = Option.builder("a").build();
        option.addValue(value);

        // When
        String result = option.getValue();

        // Then
        assertEquals(value, result);
    }

    @Test
    public void testGetValueSeparator() {
        // Given
        char sep = '=';
        Option option = Option.builder("a").valueSeparator(sep).build();

        // When
        char result = option.getValueSeparator();

        // Then
        assertEquals(sep, result);
    }

    @Test
    public void testGetValuesList() {
        // Given
        String value1 = "test-value1";
        String value2 = "test-value2";
        Option option = Option.builder("a").build();
        option.addValue(value1);
        option.addValue(value2);

        // When
        List<String> result = option.getValuesList();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(value1));
        assertTrue(result.contains(value2));
    }

    @Test
    public void testHasArg() {
        // Given
        Option option = Option.builder("a").hasArg().build();

        // When
        boolean result = option.hasArg();

        // Then
        assertTrue(result);
    }

    @Test
    public void testHasArgName() {
        // Given
        String argName = "test-arg";
        Option option = Option.builder("a").argName(argName).build();

        // When
        boolean result = option.hasArgName();

        // Then
        assertTrue(result);
    }

    @Test
    public void testHasArgs() {
        // Given
        Option option = Option.builder("a").hasArgs().build();

        // When
        boolean result = option.hasArgs();

        // Then
        assertTrue(result);
    }

    @Test
    public void testHasLongOpt() {
        // Given
        String longOpt = "test-long-opt";
        Option option = Option.builder("a").longOpt(longOpt).build();

        // When
        boolean result = option.hasLongOpt();

        // Then
        assertTrue(result);
    }

    @Test
    public void testHasOptionalArg() {
        // Given
        Option option = Option.builder("a").optionalArg(true).build();

        // When
        boolean result = option.hasOptionalArg();

        // Then
        assertTrue(result);
    }

    @Test
    public void testHasValueSeparator() {
        // Given
        char sep = '=';
        Option option = Option.builder("a").valueSeparator(sep).build();

        // When
        boolean result = option.hasValueSeparator();

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsRequired() {
        // Given
        Option option = Option.builder("a").required().build();

        // When
        boolean result = option.isRequired();

        // Then
        assertTrue(result);
    }

    @Test
    public void testSetArgName() {
        // Given
        String argName = "test-arg";
        Option option = Option.builder("a").build();

        // When
        option.setArgName(argName);

        // Then
        assertEquals(argName, option.getArgName());
    }

    @Test
    public void testSetArgs() {
        // Given
        int numberOfArgs = 2;
        Option option = Option.builder("a").build();

        // When
        option.setArgs(numberOfArgs);

        // Then
        assertEquals(numberOfArgs, option.getArgs());
    }

    @Test
    public void testSetDescription() {
        // Given
        String description = "This is a test option";
        Option option = Option.builder("a").build();

        // When
        option.setDescription(description);

        // Then
        assertEquals(description, option.getDescription());
    }

    @Test
    public void testSetLongOpt() {
        // Given
        String longOpt = "test-long-opt";
        Option option = Option.builder("a").build();

        // When
        option.setLongOpt(longOpt);

        // Then
        assertEquals(longOpt, option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        // Given
        boolean isOptional = true;
        Option option = Option.builder("a").build();

        // When
        option.setOptionalArg(isOptional);

        // Then
        assertEquals(isOptional, option.hasOptionalArg());
    }

    @Test
    public void testSetRequired() {
        // Given
        boolean required = true;
        Option option = Option.builder("a").build();

        // When
        option.setRequired(required);

        // Then
        assertEquals(required, option.isRequired());
    }

    @Test
    public void testSetType() {
        // Given
        Class<?> type = String.class;
        Option option = Option.builder("a").build();

        // When
        option.setType(type);

        // Then
        assertEquals(type, option.getType());
    }

    @Test
    public void testSetValueSeparator() {
        // Given
        char sep = '=';
        Option option = Option.builder("a").build();

        // When
        option.setValueSeparator(sep);

        // Then
        assertEquals(sep, option.getValueSeparator());
    }
}
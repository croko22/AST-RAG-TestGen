import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OptionGroupTest {

    @Mock
    private Option option1;

    @Mock
    private Option option2;

    private OptionGroup optionGroup;

    @BeforeEach
    public void setup() {
        optionGroup = new OptionGroup();
        when(option1.getKey()).thenReturn("option1");
        when(option2.getKey()).thenReturn("option2");
    }

    @Test
    public void testAddOption() {
        // When
        OptionGroup result = optionGroup.addOption(option1);

        // Then
        assertSame(optionGroup, result);
        verify(option1, never()).getKey();
    }

    @Test
    public void testGetNames() {
        // Given
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        // When
        Collection<String> names = optionGroup.getNames();

        // Then
        assertEquals(2, names.size());
        assertTrue(names.contains("option1"));
        assertTrue(names.contains("option2"));
    }

    @Test
    public void testGetOptions() {
        // Given
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        // When
        Collection<Option> options = optionGroup.getOptions();

        // Then
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetSelected() {
        // Given
        optionGroup.setSelected(option1);

        // When
        String selected = optionGroup.getSelected();

        // Then
        assertEquals("option1", selected);
    }

    @Test
    public void testIsRequired() {
        // Given
        optionGroup.setRequired(true);

        // When
        boolean required = optionGroup.isRequired();

        // Then
        assertTrue(required);
    }

    @Test
    public void testSetRequired() {
        // Given
        optionGroup.setRequired(true);

        // When
        boolean required = optionGroup.isRequired();

        // Then
        assertTrue(required);
    }

    @Test
    public void testSetSelected() {
        // Given
        optionGroup.setSelected(option1);

        // When
        String selected = optionGroup.getSelected();

        // Then
        assertEquals("option1", selected);
    }

    @Test
    public void testSetSelected_AlreadySelected() {
        // Given
        optionGroup.setSelected(option1);

        // When / Then
        assertThrows(org.apache.commons.cli.AlreadySelectedException.class, () -> optionGroup.setSelected(option2));
    }

    @Test
    public void testSetSelected_Null() {
        // Given
        optionGroup.setSelected(option1);

        // When
        optionGroup.setSelected(null);

        // Then
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testToString() {
        // Given
        when(option1.getOpt()).thenReturn("o1");
        when(option1.getDescription()).thenReturn("desc1");
        when(option2.getLongOpt()).thenReturn("lo2");
        when(option2.getDescription()).thenReturn("desc2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        // When
        String toString = optionGroup.toString();

        // Then
        assertEquals("[--lo2 desc2, -o1 desc1]", toString);
    }
}
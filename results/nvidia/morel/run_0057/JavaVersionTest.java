import net.hydromatic.morel.util.JavaVersion;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JavaVersionTest {

    @Mock
    private Terminal terminal;

    private JavaVersion version;

    @BeforeEach
    public void setup() {
        // Initialize the version with some components
        version = JavaVersion.of(1, 8, 0);
    }

    @Test
    public void testOf() {
        // Test the of() method with varargs
        JavaVersion version = JavaVersion.of(1, 8, 0);
        assertNotNull(version);
        assertEquals(3, version.toString().split("\\.").length);
    }

    @Test
    public void testOfListComponent() {
        // Test the of() method with a list of components
        List<Integer> components = new ArrayList<>();
        components.add(1);
        components.add(8);
        components.add(0);
        JavaVersion version = JavaVersion.of(components);
        assertNotNull(version);
        assertEquals(3, version.toString().split("\\.").length);
    }

    @Test
    public void testBannerWithoutTerminal() {
        // Test the banner() method without a terminal
        String banner = JavaVersion.banner(null);
        assertNotNull(banner);
        assertNotEquals("", banner);
    }

    @Test
    public void testBannerWithTerminal() throws Exception {
        // Test the banner() method with a terminal
        when(terminal.getName()).thenReturn("Test Terminal");
        when(terminal.getType()).thenReturn("Test Type");
        String banner = JavaVersion.banner(terminal);
        assertNotNull(banner);
        assertNotEquals("", banner);
    }

    @Test
    public void testCompareTo() {
        // Test the compareTo() method
        JavaVersion version1 = JavaVersion.of(1, 8, 0);
        JavaVersion version2 = JavaVersion.of(1, 8, 1);
        assertEquals(-1, version1.compareTo(version2));
    }

    @Test
    public void testToString() {
        // Test the toString() method
        JavaVersion version = JavaVersion.of(1, 8, 0);
        String toString = version.toString();
        assertNotNull(toString);
        assertEquals("1.8.0", toString);
    }

    @Test
    public void testCurrentVersion() {
        // Test the CURRENT version
        JavaVersion currentVersion = JavaVersion.CURRENT;
        assertNotNull(currentVersion);
        assertNotEquals("", currentVersion.toString());
    }

    @Test
    public void testMorelVersion() {
        // Test the MOREL_VERSION
        JavaVersion morelVersion = JavaVersion.MOREL_VERSION;
        assertNotNull(morelVersion);
        assertEquals("0.8.0", morelVersion.toString());
    }
}
import net.hydromatic.morel.compile.NameGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NameGeneratorTest {

    private NameGenerator nameGenerator;

    @BeforeEach
    public void setup() {
        nameGenerator = new NameGenerator();
    }

    @Test
    public void testGet_Uniqueness() {
        // Given: a new NameGenerator instance
        // When: get() is called multiple times
        String name1 = nameGenerator.get();
        String name2 = nameGenerator.get();
        String name3 = nameGenerator.get();

        // Then: each name is unique
        assertNotEquals(name1, name2);
        assertNotEquals(name1, name3);
        assertNotEquals(name2, name3);
    }

    @Test
    public void testGetPrefixed_Uniqueness() {
        // Given: a new NameGenerator instance
        // When: getPrefixed() is called multiple times with the same prefix
        String name1 = nameGenerator.getPrefixed("v");
        String name2 = nameGenerator.getPrefixed("v");
        String name3 = nameGenerator.getPrefixed("v");

        // Then: each name is unique
        assertNotEquals(name1, name2);
        assertNotEquals(name1, name3);
        assertNotEquals(name2, name3);
    }

    @Test
    public void testGetPrefixed_DifferentPrefixes() {
        // Given: a new NameGenerator instance
        // When: getPrefixed() is called with different prefixes
        String name1 = nameGenerator.getPrefixed("v");
        String name2 = nameGenerator.getPrefixed("x");
        String name3 = nameGenerator.getPrefixed("y");

        // Then: each name has the correct prefix
        assertTrue(name1.startsWith("v"));
        assertTrue(name2.startsWith("x"));
        assertTrue(name3.startsWith("y"));
    }

    @Test
    public void testInc_NameNotUsedBefore() {
        // Given: a new NameGenerator instance and a name not used before
        String name = "v";

        // When: inc() is called
        int count = nameGenerator.inc(name);

        // Then: the count is 1
        assertEquals(1, count);
    }

    @Test
    public void testInc_NameUsedBefore() {
        // Given: a new NameGenerator instance and a name used before
        String name = "v";
        nameGenerator.inc(name);

        // When: inc() is called again
        int count = nameGenerator.inc(name);

        // Then: the count is 2
        assertEquals(2, count);
    }

    @Test
    public void testInc_MultipleNames() {
        // Given: a new NameGenerator instance and multiple names
        String name1 = "v";
        String name2 = "x";

        // When: inc() is called for each name
        nameGenerator.inc(name1);
        nameGenerator.inc(name2);

        // Then: each name has a count of 1
        assertEquals(1, nameGenerator.inc(name1));
        assertEquals(1, nameGenerator.inc(name2));
    }
}
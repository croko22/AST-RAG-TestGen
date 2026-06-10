import net.hydromatic.morel.ast.Pos;
import net.hydromatic.morel.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PosTest {

    @Test
    public void testOf() {
        // Given
        String ml = "test";
        String file = "file.txt";
        int startOffset = 0;
        int endOffset = 4;

        // When
        Pos pos = Pos.of(ml, file, startOffset, endOffset);

        // Then
        assertNotNull(pos);
        assertEquals(file, pos.file);
    }

    @Test
    public void testSplit() {
        // Given
        String s = "test#test";
        char delimiter = '#';
        String file = "file.txt";

        // When
        Pair<String, Pos> result = Pos.split(s, delimiter, file);

        // Then
        assertNotNull(result);
        assertEquals("testtest", result.getKey());
        assertNotNull(result.getValue());
        assertEquals(file, result.getValue().file);
    }

    @Test
    public void testHashCode() {
        // Given
        Pos pos1 = new Pos("file.txt", 1, 1, 1, 1);
        Pos pos2 = new Pos("file.txt", 1, 1, 1, 1);

        // When
        int hashCode1 = pos1.hashCode();
        int hashCode2 = pos2.hashCode();

        // Then
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        // Given
        Pos pos1 = new Pos("file.txt", 1, 1, 1, 1);
        Pos pos2 = new Pos("file.txt", 1, 1, 1, 1);

        // When
        boolean equals = pos1.equals(pos2);

        // Then
        assertTrue(equals);
    }

    @Test
    public void testToString() {
        // Given
        Pos pos = new Pos("file.txt", 1, 1, 1, 1);

        // When
        String toString = pos.toString();

        // Then
        assertNotNull(toString);
        assertEquals("file.txt:1.1", toString);
    }

    @Test
    public void testDescribeTo() {
        // Given
        Pos pos = new Pos("file.txt", 1, 1, 1, 1);
        StringBuilder buf = new StringBuilder();

        // When
        StringBuilder describeTo = pos.describeTo(buf);

        // Then
        assertNotNull(describeTo);
        assertEquals("file.txt:1.1", describeTo.toString());
    }

    @Test
    public void testSum() {
        // Given
        List<Pos> poses = new ArrayList<>();
        poses.add(new Pos("file.txt", 1, 1, 1, 1));
        poses.add(new Pos("file.txt", 1, 2, 1, 2));

        // When
        Pos sum = Pos.sum(poses);

        // Then
        assertNotNull(sum);
        assertEquals("file.txt", sum.file);
        assertEquals(1, sum.startLine);
        assertEquals(1, sum.startColumn);
        assertEquals(1, sum.endLine);
        assertEquals(2, sum.endColumn);
    }

    @Test
    public void testSumElements() {
        // Given
        List<String> elements = new ArrayList<>();
        elements.add("test1");
        elements.add("test2");
        Function<String, Pos> fn = element -> new Pos("file.txt", 1, 1, 1, 1);

        // When
        Pos sum = Pos.sum(elements, fn);

        // Then
        assertNotNull(sum);
        assertEquals("file.txt", sum.file);
        assertEquals(1, sum.startLine);
        assertEquals(1, sum.startColumn);
        assertEquals(1, sum.endLine);
        assertEquals(1, sum.endColumn);
    }

    @Test
    public void testSumNodes() {
        // Given
        List<AstNode> nodes = new ArrayList<>();
        nodes.add(new AstNode(new Pos("file.txt", 1, 1, 1, 1)));
        nodes.add(new AstNode(new Pos("file.txt", 1, 2, 1, 2)));

        // When
        Pos sum = Pos.sum(nodes);

        // Then
        assertNotNull(sum);
        assertEquals("file.txt", sum.file);
        assertEquals(1, sum.startLine);
        assertEquals(1, sum.startColumn);
        assertEquals(1, sum.endLine);
        assertEquals(2, sum.endColumn);
    }

    @Test
    public void testPlus() {
        // Given
        Pos pos1 = new Pos("file.txt", 1, 1, 1, 1);
        Pos pos2 = new Pos("file.txt", 1, 2, 1, 2);

        // When
        Pos plus = pos1.plus(pos2);

        // Then
        assertNotNull(plus);
        assertEquals("file.txt", plus.file);
        assertEquals(1, plus.startLine);
        assertEquals(1, plus.startColumn);
        assertEquals(1, plus.endLine);
        assertEquals(2, plus.endColumn);
    }

    @Test
    public void testPlusAll() {
        // Given
        List<Pos> poses = new ArrayList<>();
        poses.add(new Pos("file.txt", 1, 1, 1, 1));
        poses.add(new Pos("file.txt", 1, 2, 1, 2));

        // When
        Pos plusAll = Pos.sum(poses);

        // Then
        assertNotNull(plusAll);
        assertEquals("file.txt", plusAll.file);
        assertEquals(1, plusAll.startLine);
        assertEquals(1, plusAll.startColumn);
        assertEquals(1, plusAll.endLine);
        assertEquals(2, plusAll.endColumn);
    }

    @Test
    public void testCompare() {
        // Given
        Pos pos1 = new Pos("file.txt", 1, 1, 1, 1);
        Pos pos2 = new Pos("file.txt", 1, 2, 1, 2);

        // When
        int compare = Pos.compare(pos1, pos2);

        // Then
        assertEquals(-1, compare);
    }

    private static class AstNode {
        private final Pos pos;

        public AstNode(Pos pos) {
            this.pos = pos;
        }
    }
}
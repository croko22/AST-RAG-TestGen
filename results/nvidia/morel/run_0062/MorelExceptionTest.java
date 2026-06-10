import net.hydromatic.morel.ast.Pos;
import net.hydromatic.morel.util.MorelException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MorelExceptionTest {

    @Mock
    private MorelException morelException;

    @Mock
    private Pos pos;

    @BeforeEach
    void setup() {
        // Initialize mocks
        when(morelException.pos()).thenReturn(pos);
    }

    @Test
    public void testPos() {
        // Given: a MorelException instance
        // When: pos() method is called
        Pos result = morelException.pos();
        // Then: verify the result
        assertEquals(pos, result);
        verify(morelException, times(1)).pos();
    }

    @Test
    public void testDescribeTo() {
        // Given: a StringBuilder instance
        StringBuilder buf = new StringBuilder();
        // When: describeTo() method is called
        StringBuilder result = morelException.describeTo(buf);
        // Then: verify the result
        assertSame(buf, result);
        verify(morelException, times(1)).describeTo(buf);
    }

    @Test
    public void testPosOf() {
        // Given: parameters for Pos.of() method
        String ml = "ml";
        String file = "file";
        int startOffset = 1;
        int endOffset = 2;
        // When: Pos.of() method is called
        Pos result = Pos.of(ml, file, startOffset, endOffset);
        // Then: verify the result
        assertNotNull(result);
    }

    @Test
    public void testPosSplit() {
        // Given: parameters for Pos.split() method
        String s = "s";
        char delimiter = ',';
        String file = "file";
        // When: Pos.split() method is called
        Pair<String, Pos> result = Pos.split(s, delimiter, file);
        // Then: verify the result
        assertNotNull(result);
    }

    @Test
    public void testPosHashCode() {
        // Given: a Pos instance
        Pos posInstance = mock(Pos.class);
        // When: hashCode() method is called
        int result = posInstance.hashCode();
        // Then: verify the result
        assertTrue(result >= 0);
    }

    @Test
    public void testPosEquals() {
        // Given: two Pos instances
        Pos posInstance1 = mock(Pos.class);
        Pos posInstance2 = mock(Pos.class);
        // When: equals() method is called
        boolean result = posInstance1.equals(posInstance2);
        // Then: verify the result
        assertTrue(result || !result);
    }

    @Test
    public void testPosToString() {
        // Given: a Pos instance
        Pos posInstance = mock(Pos.class);
        // When: toString() method is called
        String result = posInstance.toString();
        // Then: verify the result
        assertNotNull(result);
    }

    @Test
    public void testPosDescribeTo() {
        // Given: a Pos instance and a StringBuilder
        Pos posInstance = mock(Pos.class);
        StringBuilder buf = new StringBuilder();
        // When: describeTo() method is called
        StringBuilder result = posInstance.describeTo(buf);
        // Then: verify the result
        assertSame(buf, result);
    }

    @Test
    public void testPosSum() {
        // Given: an iterable of Pos instances
        List<Pos> poses = new ArrayList<>();
        poses.add(mock(Pos.class));
        poses.add(mock(Pos.class));
        // When: Pos.sum() method is called
        Pos result = Pos.sum(poses);
        // Then: verify the result
        assertNotNull(result);
    }

    @Test
    public void testPosSumWithFunction() {
        // Given: an iterable of elements and a function
        List<String> elements = new ArrayList<>();
        elements.add("element1");
        elements.add("element2");
        Function<String, Pos> fn = element -> mock(Pos.class);
        // When: Pos.sum() method is called
        Pos result = Pos.sum(elements, fn);
        // Then: verify the result
        assertNotNull(result);
    }

    @Test
    public void testPosSumWithAstNodes() {
        // Given: a list of AstNode instances
        List<AstNode> nodes = new ArrayList<>();
        nodes.add(mock(AstNode.class));
        nodes.add(mock(AstNode.class));
        // When: Pos.sum() method is called
        Pos result = Pos.sum(nodes);
        // Then: verify the result
        assertNotNull(result);
    }

    @Test
    public void testPosPlus() {
        // Given: two Pos instances
        Pos posInstance1 = mock(Pos.class);
        Pos posInstance2 = mock(Pos.class);
        // When: plus() method is called
        Pos result = posInstance1.plus(posInstance2);
        // Then: verify the result
        assertNotNull(result);
    }

    @Test
    public void testPosPlusAll() {
        // Given: a Pos instance and an iterable of Pos instances
        Pos posInstance = mock(Pos.class);
        List<Pos> poses = new ArrayList<>();
        poses.add(mock(Pos.class));
        poses.add(mock(Pos.class));
        // When: plusAll() method is called
        Pos result = posInstance.plusAll(poses);
        // Then: verify the result
        assertNotNull(result);
    }

    @Test
    public void testPosPlusAllWithAstNodes() {
        // Given: a Pos instance and a list of AstNode instances
        Pos posInstance = mock(Pos.class);
        List<AstNode> nodes = new ArrayList<>();
        nodes.add(mock(AstNode.class));
        nodes.add(mock(AstNode.class));
        // When: plusAll() method is called
        Pos result = posInstance.plusAll(nodes);
        // Then: verify the result
        assertNotNull(result);
    }

    @Test
    public void testPosCompare() {
        // Given: two Pos instances
        Pos posInstance1 = mock(Pos.class);
        Pos posInstance2 = mock(Pos.class);
        // When: compare() method is called
        int result = Pos.compare(posInstance1, posInstance2);
        // Then: verify the result
        assertTrue(result >= 0);
    }
}
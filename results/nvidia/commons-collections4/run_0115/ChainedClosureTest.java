import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.functors.ChainedClosure;
import org.apache.commons.collections4.functors.NOPClosure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChainedClosureTest {

    @Mock
    private Closure<String> mockClosure1;

    @Mock
    private Closure<String> mockClosure2;

    private ChainedClosure<String> chainedClosure;

    @BeforeEach
    public void setup() {
        chainedClosure = new ChainedClosure<>(mockClosure1, mockClosure2);
    }

    @Test
    public void testChainedClosure_NoClosures() {
        Closure<String> chainedClosure = ChainedClosure.chainedClosure();
        assertNotNull(chainedClosure);
        assertTrue(chainedClosure instanceof NOPClosure);
    }

    @Test
    public void testChainedClosure_SingleClosure() {
        Closure<String> chainedClosure = ChainedClosure.chainedClosure(mockClosure1);
        assertNotNull(chainedClosure);
        assertSame(mockClosure1, chainedClosure);
    }

    @Test
    public void testChainedClosure_MultipleClosures() {
        Closure<String> chainedClosure = ChainedClosure.chainedClosure(mockClosure1, mockClosure2);
        assertNotNull(chainedClosure);
        assertNotSame(mockClosure1, chainedClosure);
        assertNotSame(mockClosure2, chainedClosure);
    }

    @Test
    public void testChainedClosure_Collection() {
        Collection<Closure<String>> closures = new ArrayList<>();
        closures.add(mockClosure1);
        closures.add(mockClosure2);
        Closure<String> chainedClosure = ChainedClosure.chainedClosure(closures);
        assertNotNull(chainedClosure);
        assertNotSame(mockClosure1, chainedClosure);
        assertNotSame(mockClosure2, chainedClosure);
    }

    @Test
    public void testExecute() {
        String input = "test";
        chainedClosure.execute(input);
        verify(mockClosure1, times(1)).accept(input);
        verify(mockClosure2, times(1)).accept(input);
    }

    @Test
    public void testGetClosures() {
        Closure<String>[] closures = chainedClosure.getClosures();
        assertNotNull(closures);
        assertEquals(2, closures.length);
        assertNotSame(mockClosure1, closures[0]);
        assertNotSame(mockClosure2, closures[1]);
    }

    @Test
    public void testChainedClosure_NullClosures() {
        assertThrows(NullPointerException.class, () -> ChainedClosure.chainedClosure((Closure<String>) null));
    }

    @Test
    public void testChainedClosure_NullCollection() {
        assertThrows(NullPointerException.class, () -> ChainedClosure.chainedClosure((Collection<Closure<String>>) null));
    }

    @Test
    public void testChainedClosure_EmptyCollection() {
        Collection<Closure<String>> closures = new ArrayList<>();
        Closure<String> chainedClosure = ChainedClosure.chainedClosure(closures);
        assertNotNull(chainedClosure);
        assertTrue(chainedClosure instanceof NOPClosure);
    }
}
import net.hydromatic.morel.ast.AstNode;
import net.hydromatic.morel.ast.Core;
import net.hydromatic.morel.compile.Analyzer;
import net.hydromatic.morel.compile.Analyzer.Analysis;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyzerTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private AstNode astNode;

    private Analyzer analyzer;

    @BeforeEach
    public void setup() {
        analyzer = new Analyzer(typeSystem, new Environment(), new HashMap<>(), new ArrayDeque<>());
    }

    @Test
    public void testAnalyze() {
        // Given
        when(typeSystem.lookup()).thenReturn(new Type());

        // When
        Analysis analysis = Analyzer.analyze(typeSystem, new Environment(), astNode);

        // Then
        assertNotNull(analysis);
        assertNotNull(analysis.map);
    }

    @Test
    public void testToString() {
        // When
        String toString = analyzer.toString();

        // Then
        assertNotNull(toString);
    }

    @Test
    public void testResult() {
        // Given
        Map<Core.NamedPat, Analyzer.MutableUse> map = new HashMap<>();
        map.put(new Core.NamedPat(), new Analyzer.MutableUse());

        // When
        Analysis analysis = new Analyzer(typeSystem, new Environment(), map, new ArrayDeque<>()).result();

        // Then
        assertNotNull(analysis);
        assertNotNull(analysis.map);
    }

    @Test
    public void testUse() {
        // Given
        Core.NamedPat namedPat = new Core.NamedPat();

        // When
        Analyzer.MutableUse mutableUse = analyzer.use(namedPat);

        // Then
        assertNotNull(mutableUse);
    }

    @Test
    public void testIsAtom() {
        // Given
        Core.Exp exp = new Core.Exp();

        // When
        boolean isAtom = Analyzer.isAtom(exp);

        // Then
        assertEquals(false, isAtom);
    }
}
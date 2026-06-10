import net.hydromatic.morel.ast.Core;
import net.hydromatic.morel.ast.FromBuilder;
import net.hydromatic.morel.compile.Expander;
import net.hydromatic.morel.type.Binding;
import net.hydromatic.morel.type.TypeSystem;
import net.hydromatic.morel.util.PairList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpanderTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private FromBuilder fromBuilder;

    @Mock
    private Core core;

    private Expander expander;

    @BeforeEach
    void setup() {
        expander = new Expander(new Expander.Generators.Cache(typeSystem, null), new ArrayList<>());
    }

    @Test
    void testExpandFrom() {
        // Given
        Core.From from = mock(Core.From.class);
        when(from.steps).thenReturn(new ArrayList<>());

        // When
        Core.From result = Expander.expandFrom(typeSystem, null, from);

        // Then
        assertEquals(from, result);
    }

    @Test
    void testExpandFrom2() {
        // Given
        Expander.Generators.Cache cache = new Expander.Generators.Cache(typeSystem, null);
        Expander.StepVarSet stepVarSet = mock(Expander.StepVarSet.class);

        // When
        Core.From result = Expander.expandFrom2(cache, null, stepVarSet);

        // Then
        assertNotNull(result);
    }

    @Test
    void testAddGeneratorScan() {
        // Given
        Core.NamedPat freePat = mock(Core.NamedPat.class);
        Map<Core.NamedPat, Expander.PatternState> patternState = new HashMap<>();
        Map<Core.NamedPat, Expander.Generator> generatorMap = new HashMap<>();
        Set<Core.NamedPat> allPats = new HashSet<>();
        Set<Core.NamedPat> allScanPats = new HashSet<>();

        // When
        Expander.addGeneratorScan(typeSystem, patternState, freePat, generatorMap, allPats, allScanPats, fromBuilder);

        // Then
        verify(fromBuilder, never()).scan(any(), any());
    }

    @Test
    void testRenamePatterns() {
        // Given
        Core.Pat pat = mock(Core.Pat.class);
        Map<Core.NamedPat, Core.IdPat> renameMap = new HashMap<>();

        // When
        Core.Pat result = Expander.renamePatterns(typeSystem, pat, renameMap);

        // Then
        assertEquals(pat, result);
    }

    @Test
    void testExpandSteps() {
        // Given
        List<Core.FromStep> steps = new ArrayList<>();

        // When
        Expander.expandSteps(steps, expander);

        // Then
        verifyNoInteractions(fromBuilder);
    }

    @Test
    void testImproveGenerators() {
        // Given
        Multimap<Core.NamedPat, Expander.Generator> generators = mock(Multimap.class);

        // When
        expander.improveGenerators(generators);

        // Then
        verifyNoInteractions(fromBuilder);
    }

    @Test
    void testPlusConstraint() {
        // Given
        Core.Exp constraint = mock(Core.Exp.class);

        // When
        Expander result = expander.plusConstraint(constraint);

        // Then
        assertNotNull(result);
    }

    @Test
    void testWithConstraints() {
        // Given
        List<Core.Exp> constraints = new ArrayList<>();

        // When
        Expander result = expander.withConstraints(constraints);

        // Then
        assertNotNull(result);
    }

    @Test
    void testGetEntries() {
        // Given
        Core.From from = mock(Core.From.class);

        // When
        PairList<Core.FromStep, Set<Core.NamedPat>> result = Expander.StepAnalyzer.getEntries(from, typeSystem);

        // Then
        assertNotNull(result);
    }
}
Here's a complete test class for the `FromBuilder` class with proper imports, setup, and test methods:

```java
import net.hydromatic.morel.ast.Core;
import net.hydromatic.morel.ast.CoreBuilder;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FromBuilderTest {

    @Mock
    private TypeSystem typeSystem;

    private FromBuilder fromBuilder;

    @BeforeEach
    public void setup() {
        fromBuilder = new FromBuilder(typeSystem, null);
    }

    @Test
    public void testClear() {
        // Given
        fromBuilder.scan(CoreBuilder.core().idPat("id"), CoreBuilder.core().intLiteral(1));

        // When
        fromBuilder.clear();

        // Then
        assertEquals(0, fromBuilder.steps.size());
        assertEquals(0, fromBuilder.bindings.size());
    }

    @Test
    public void testToString() {
        // Given
        fromBuilder.scan(CoreBuilder.core().idPat("id"), CoreBuilder.core().intLiteral(1));

        // When
        String result = fromBuilder.toString();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testScan() {
        // Given
        Core.Pat pat = CoreBuilder.core().idPat("id");
        Core.Exp exp = CoreBuilder.core().intLiteral(1);

        // When
        FromBuilder result = fromBuilder.scan(pat, exp);

        // Then
        assertEquals(fromBuilder, result);
        assertEquals(1, fromBuilder.steps.size());
    }

    @Test
    public void testAddAll() {
        // Given
        List<Core.FromStep> steps = new ArrayList<>();
        steps.add(CoreBuilder.core().scan(CoreBuilder.core().idPat("id"), CoreBuilder.core().intLiteral(1)));

        // When
        FromBuilder result = fromBuilder.addAll(steps);

        // Then
        assertEquals(fromBuilder, result);
        assertEquals(1, fromBuilder.steps.size());
    }

    @Test
    public void testWhere() {
        // Given
        Core.Exp condition = CoreBuilder.core().boolLiteral(true);

        // When
        FromBuilder result = fromBuilder.where(condition);

        // Then
        assertEquals(fromBuilder, result);
        assertEquals(1, fromBuilder.steps.size());
    }

    @Test
    public void testSkip() {
        // Given
        Core.Exp count = CoreBuilder.core().intLiteral(1);

        // When
        FromBuilder result = fromBuilder.skip(count);

        // Then
        assertEquals(fromBuilder, result);
        assertEquals(1, fromBuilder.steps.size());
    }

    @Test
    public void testTake() {
        // Given
        Core.Exp count = CoreBuilder.core().intLiteral(1);

        // When
        FromBuilder result = fromBuilder.take(count);

        // Then
        assertEquals(fromBuilder, result);
        assertEquals(1, fromBuilder.steps.size());
    }

    @Test
    public void testExcept() {
        // Given
        boolean distinct = true;
        List<Core.Exp> args = new ArrayList<>();
        args.add(CoreBuilder.core().intLiteral(1));

        // When
        FromBuilder result = fromBuilder.except(distinct, args);

        // Then
        assertEquals(fromBuilder, result);
        assertEquals(1, fromBuilder.steps.size());
    }

    @Test
    public void testIntersect() {
        // Given
        boolean distinct = true;
        List<Core.Exp> args = new ArrayList<>();
        args.add(CoreBuilder.core().intLiteral(1));

        // When
        FromBuilder result = fromBuilder.intersect(distinct, args);

        // Then
        assertEquals(fromBuilder, result);
        assertEquals(1, fromBuilder.steps.size());
    }

    @Test
    public void testUnion() {
        // Given
        boolean distinct = true;
        List<Core.Exp> args = new ArrayList<>();
        args.add(CoreBuilder.core().intLiteral(1));

        // When
        FromBuilder result = fromBuilder.union(distinct, args);

        // Then
        assertEquals(fromBuilder, result);
        assertEquals(1, fromBuilder.steps.size());
    }

    @Test
    public void testUnorder() {
        // When
        FromBuilder result = fromBuilder.unorder();

        // Then
        assertEquals(fromBuilder, result);
    }

    @Test
    public void testDistinct() {
        // When
        FromBuilder result = fromBuilder.distinct();

        // Then
        assertEquals(fromBuilder, result);
    }

    @Test
    public void testGroup() {
        // Given
        boolean atom = true;
        SortedMap<Core.IdPat, Core.Exp> groupExps = new java.util.TreeMap<>();
        groupExps.put(CoreBuilder.core().idPat("id"), CoreBuilder.core().intLiteral(1));
        SortedMap<Core.IdPat, Core.Aggregate> aggregates = new java.util.TreeMap<>();

        // When
        FromBuilder result = fromBuilder.group(atom, groupExps, aggregates);

        // Then
        assertEquals(fromBuilder, result);
        assertEquals(1, fromBuilder.steps.size());
    }

    @Test
    public void testOrder() {
        // Given
        Core.Exp exp = CoreBuilder.core().intLiteral(1);

        // When
        FromBuilder result = fromBuilder.order(exp);

        // Then
        assertEquals(fromBuilder, result);
        assertEquals(1, fromBuilder.steps.size());
    }

    @Test
    public void testYield() {
        // Given
        Core.Exp exp = CoreBuilder.core().intLiteral(1);

        // When
        FromBuilder result = fromBuilder.yield_(exp);

        // Then
        assertEquals(fromBuilder, result);
        assertEquals(1, fromBuilder.steps.size());
    }

    @Test
    public void testBuild() {
        // When
        Core.From result = fromBuilder.build();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testBuildSimplify() {
        // When
        Core.Exp result = fromBuilder.buildSimplify();

        // Then
        assertNotNull(result);
    }
}
```

This test class covers all the public methods of the `FromBuilder` class. Each test method tests a specific method of the `FromBuilder` class. The `@BeforeEach` method is used to set up the `FromBuilder` object before each test method. The `@Test` annotation is used to mark each test method. The `assert` methods are used to verify the expected behavior of the `FromBuilder` class. The `Mockito` framework is used to mock the `TypeSystem` object.
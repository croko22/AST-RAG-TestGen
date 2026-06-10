import net.hydromatic.morel.ast.Core;
import net.hydromatic.morel.compile.Environment;
import net.hydromatic.morel.type.Binding;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import net.hydromatic.morel.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.TriConsumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnvironmentTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private Core core;

    private Environment environment;

    @BeforeEach
    public void setup() {
        environment = new Environment() {
            @Override
            public void visit(Consumer<Binding> consumer) {
                // No-op
            }

            @Override
            public Binding getTop(String name) {
                return null;
            }

            @Override
            public Binding getOpt(String name) {
                return null;
            }

            @Override
            public Binding getOpt(Core.NamedPat id) {
                return null;
            }

            @Override
            public Pair<Binding, Environment> getOpt2(Core.NamedPat id) {
                return null;
            }

            @Override
            public void forEachAncestor(Consumer<Environment> consumer) {
                // No-op
            }

            @Override
            public Environment nearestAncestorNotObscuredBy(Set<Core.NamedPat> names) {
                return null;
            }

            @Override
            public int distance(int soFar, Core.NamedPat id) {
                return 0;
            }

            @Override
            public void collect(Core.NamedPat id, Consumer<Binding> consumer) {
                // No-op
            }
        };
    }

    @Test
    public void testAsString() {
        // Given
        when(environment.getValueMap(false)).thenReturn(new HashMap<>());

        // When
        String result = environment.asString();

        // Then
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    public void testGetTop() {
        // Given
        String name = "test";

        // When
        Binding result = environment.getTop(name);

        // Then
        assertNull(result);
    }

    @Test
    public void testGetOpt() {
        // Given
        String name = "test";

        // When
        Binding result = environment.getOpt(name);

        // Then
        assertNull(result);
    }

    @Test
    public void testGetOpt2() {
        // Given
        Core.NamedPat id = mock(Core.NamedPat.class);

        // When
        Pair<Binding, Environment> result = environment.getOpt2(id);

        // Then
        assertNull(result);
    }

    @Test
    public void testIsAncestorOf() {
        // Given
        Environment other = mock(Environment.class);

        // When
        boolean result = environment.isAncestorOf(other);

        // Then
        assertFalse(result);
    }

    @Test
    public void testCollect() {
        // Given
        Core.NamedPat id = mock(Core.NamedPat.class);
        Consumer<Binding> consumer = mock(Consumer.class);

        // When
        environment.collect(id, consumer);

        // Then
        verify(consumer, never()).accept(any());
    }

    @Test
    public void testBind() {
        // Given
        Core.IdPat id = mock(Core.IdPat.class);
        Object value = "test";

        // When
        Environment result = environment.bind(id, value);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testForEachType() {
        // Given
        TriConsumer<String, Binding.Kind, Type> consumer = mock(TriConsumer.class);

        // When
        environment.forEachType(typeSystem, consumer);

        // Then
        verify(consumer, never()).accept(any(), any(), any());
    }

    @Test
    public void testForEachValue() {
        // Given
        BiConsumer<String, Object> consumer = mock(BiConsumer.class);

        // When
        environment.forEachValue(consumer);

        // Then
        verify(consumer, never()).accept(any(), any());
    }

    @Test
    public void testGetValueMap() {
        // Given
        boolean skipOverloads = true;

        // When
        Map<String, Binding> result = environment.getValueMap(skipOverloads);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testBindAll() {
        // Given
        List<Binding> bindings = new ArrayList<>();

        // When
        Environment result = environment.bindAll(bindings);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testPlus() {
        // Given
        Environment env = mock(Environment.class);

        // When
        Environment result = environment.plus(env);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testRenumber() {
        // Given

        // When
        Environment result = environment.renumber();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testHasOverloaded() {
        // Given
        String name = "test";

        // When
        boolean result = environment.hasOverloaded(name);

        // Then
        assertFalse(result);
    }

    @Test
    public void testGetOverloads() {
        // Given
        Core.IdPat id = mock(Core.IdPat.class);

        // When
        List<Core.IdPat> result = environment.getOverloads(id);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
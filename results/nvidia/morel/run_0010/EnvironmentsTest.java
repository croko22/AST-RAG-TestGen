Here is a comprehensive test class for the `Environments` class:

```java
import net.hydromatic.morel.compile.Environments;
import net.hydromatic.morel.eval.Codes;
import net.hydromatic.morel.eval.EvalEnv;
import net.hydromatic.morel.eval.Session;
import net.hydromatic.morel.foreign.ForeignValue;
import net.hydromatic.morel.type.Binding;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnvironmentsTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private Session session;

    @Mock
    private ForeignValue foreignValue;

    @BeforeEach
    void setup() {
        // Initialize mocks
        when(typeSystem.nameGenerator()).thenReturn(new NameGenerator());
        when(foreignValue.type(any(TypeSystem.class))).thenReturn(new PrimitiveType());
    }

    @Test
    void testEmpty() {
        // Given
        Environment environment = Environments.empty();

        // When
        Environment result = environment;

        // Then
        assertNotNull(result);
    }

    @Test
    void testEnv() {
        // Given
        Map<String, ForeignValue> valueMap = Map.of("key", foreignValue);

        // When
        Environment environment = Environments.env(typeSystem, session, valueMap);

        // Then
        assertNotNull(environment);
    }

    @Test
    void testBind() {
        // Given
        Environment environment = Environments.empty();
        Binding binding = new Binding(new Core.NamedPat("name"), new Object());

        // When
        Environment result = Environments.bind(environment, List.of(binding));

        // Then
        assertNotNull(result);
    }

    @Test
    void testGetTop() {
        // Given
        Environment environment = Environments.empty();
        Binding binding = new Binding(new Core.NamedPat("name"), new Object());
        environment = Environments.bind(environment, List.of(binding));

        // When
        Binding result = environment.getTop("name");

        // Then
        assertNotNull(result);
    }

    @Test
    void testGetOpt() {
        // Given
        Environment environment = Environments.empty();
        Binding binding = new Binding(new Core.NamedPat("name"), new Object());
        environment = Environments.bind(environment, List.of(binding));

        // When
        Binding result = environment.getOpt(new Core.NamedPat("name"));

        // Then
        assertNotNull(result);
    }

    @Test
    void testCollect() {
        // Given
        Environment environment = Environments.empty();
        Binding binding = new Binding(new Core.NamedPat("name"), new Object());
        environment = Environments.bind(environment, List.of(binding));

        // When
        environment.collect(new Core.NamedPat("name"), binding1 -> {
            // Then
            assertNotNull(binding1);
        });
    }

    @Test
    void testNearestAncestorNotObscuredBy() {
        // Given
        Environment environment = Environments.empty();
        Binding binding = new Binding(new Core.NamedPat("name"), new Object());
        environment = Environments.bind(environment, List.of(binding));

        // When
        Environment result = environment.nearestAncestorNotObscuredBy(Set.of(new Core.NamedPat("name")));

        // Then
        assertNotNull(result);
    }

    @Test
    void testDistance() {
        // Given
        Environment environment = Environments.empty();
        Binding binding = new Binding(new Core.NamedPat("name"), new Object());
        environment = Environments.bind(environment, List.of(binding));

        // When
        int result = environment.distance(0, new Core.NamedPat("name"));

        // Then
        assertEquals(0, result);
    }
}
```

Note that this is not an exhaustive test suite, and you may need to add more test cases to cover all the scenarios. Additionally, you will need to implement the `NameGenerator` and `PrimitiveType` classes, as they are not provided in the original code.
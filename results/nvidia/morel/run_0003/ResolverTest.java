Here is a complete test class with proper imports, setup, and test methods for the `Resolver` class:
```java
import net.hydromatic.morel.compile.Resolver;
import net.hydromatic.morel.compile.Environment;
import net.hydromatic.morel.compile.Session;
import net.hydromatic.morel.compile.TypeMap;
import net.hydromatic.morel.ast.Ast;
import net.hydromatic.morel.ast.AstNode;
import net.hydromatic.morel.ast.Core;
import net.hydromatic.morel.ast.CoreBuilder;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ResolverTest {

    @Mock
    private TypeMap typeMap;

    @Mock
    private Environment env;

    @Mock
    private Session session;

    private Resolver resolver;

    @BeforeEach
    void setup() {
        resolver = Resolver.of(typeMap, env, session);
    }

    @Test
    void testOf() {
        // Given
        TypeMap typeMap = new TypeMap();
        Environment env = new Environment();
        Session session = new Session();

        // When
        Resolver resolver = Resolver.of(typeMap, env, session);

        // Then
        assertNotNull(resolver);
    }

    @Test
    void testWithEnv() {
        // Given
        Environment newEnv = new Environment();

        // When
        Resolver newResolver = resolver.withEnv(newEnv);

        // Then
        assertNotSame(resolver, newResolver);
        assertEquals(newEnv, newResolver.env);
    }

    @Test
    void testWithEnvBindings() {
        // Given
        Iterable<Binding> bindings = new ArrayList<>();

        // When
        Resolver newResolver = resolver.withEnv(bindings);

        // Then
        assertNotSame(resolver, newResolver);
        assertEquals(bindings, newResolver.env.bindings);
    }

    @Test
    void testSubsumes() {
        // Given
        Type actualType = new Type();
        Type expectedType = new Type();

        // When
        boolean result = Resolver.subsumes(actualType, expectedType);

        // Then
        assertTrue(result);
    }

    @Test
    void testToString() {
        // Given

        // When
        String result = resolver.toString();

        // Then
        assertNotNull(result);
    }

    @Test
    void testBindings() {
        // Given

        // When
        List<Binding> result = resolver.bindings();

        // Then
        assertNotNull(result);
    }
}
```
Note that this is just a starting point, and you will need to add more test methods to cover the entire `Resolver` class. Additionally, you may need to modify the test methods to better suit your specific use case.

Also, you will need to create mock implementations for the dependencies of the `Resolver` class, such as `TypeMap`, `Environment`, and `Session`. You can use a mocking library like Mockito to create these mock implementations.

You can also use `@Mock` annotation to create mock objects, and `@InjectMocks` to inject the mock objects into the `Resolver` class.

You can also use `@Test` annotation to mark the test methods, and `@BeforeEach` annotation to mark the setup method that will be executed before each test method.

You can also use `assert` statements to verify the expected behavior of the `Resolver` class.
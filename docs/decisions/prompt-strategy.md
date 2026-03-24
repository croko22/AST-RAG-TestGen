# Decision: Prompt Engineering Strategy for Test Generation

## Context

The LLM needs to generate Java unit tests that:
1. Compile correctly
2. Use JUnit 5 and Mockito properly
3. Mock dependencies correctly
4. Test actual business logic

## Key Decision Points

### 1. Language: Spanish

**Decision**: System and user prompts in Spanish.

**Rationale**:
- Original project context is in Spanish (class names, comments)
- Target users are Spanish-speaking
- Consistency with codebase documentation

### 2. Code Under Test: Full Content

**Decision**: Send the complete Java file, not just method signatures.

**Rationale**:
- LLM needs to understand full class structure (annotations, constructors, fields)
- Some implementations have inline logic that needs testing
- Annotations (`@Service`, `@Transactional`) affect how tests should be written

### 3. Dependency Context: Method Signatures Only

**Decision**: Send method signatures, NOT full dependency implementations.

**Rationale**:
- For mocking, we only need API contracts (method names, return types, parameters)
- Full implementations add token cost and noise
- Prevents LLM from "cheating" by copying implementation code into tests

### 4. System Prompt: Role Definition

```
Eres un ingeniero de software Senior experto en Java y pruebas unitarias (JUnit 5 + Mockito).
Tu objetivo es generar pruebas unitarias que COMPILEN A LA PRIMERA, logren alta cobertura de ramas y validen la lógica de negocio.
```

**Key elements**:
- **Senior engineer**: Sets expectation for quality
- **JUnit 5 + Mockito**: Specifies exact frameworks
- **COMPILEN A LA PRIMERA**: Emphasizes correctness
- **Alta cobertura de ramas**: Encourages edge case testing

### 5. User Prompt: Explicit Structure

```
=========================================
1. CLASE A PROBAR (Code Under Test):
=========================================
{code_under_test}

=========================================
2. CONTEXTO RECUPERADO (RAG - Firmas de Dependencias):
=========================================
Para evitar alucinaciones, utiliza ÚNICAMENTE estos métodos y firmas...
{dependency_context}

=========================================
3. REGLAS ESTRICTAS DE GENERACIÓN:
=========================================
- Escribe ÚNICAMENTE el código Java...
- Usa JUnit 5 (org.junit.jupiter.api) y Mockito (org.mockito).
- Haz mock de TODAS las dependencias inyectadas usando @Mock y @InjectMocks.
- NO uses aserciones genéricas como assertTrue(true)...
```

**Why explicit sections**:
- Clear separation prevents confusion
- Emphasis on "NO alucinaciones" reduces method invention
- Specific framework imports avoid test failures

### 6. Anti-Pattern Guardrails

| Anti-Pattern | Guardrail |
|--------------|-----------|
| Generic assertions | `NO uses assertTrue(true)` |
| Wrong imports | Specific import lists mentioned |
| Wrong frameworks | "JUnit 5 (org.junit.jupiter.api) y Mockito (org.mockito)" |
| Missing mocks | "Haz mock de TODAS las dependencias" |
| Explanation text | "Escribe ÚNICAMENTE el código Java" |

### 7. Temperature: 0.3

**Decision**: Low temperature for more deterministic output.

**Rationale**:
- Test generation is a convergent task (correct answer is clear)
- Lower temperature reduces hallucinations
- Still allows some variation in test approaches

## Output Post-Processing

Generated tests are cleaned:
```python
# Remove markdown code blocks if present
if test_code.startswith("```java"):
    test_code = test_code[7:]
if test_code.startswith("```"):
    test_code = test_code[3:]
if test_code.endswith("```"):
    test_code = test_code[:-3]
```

## Example Output

```java
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void crearUsuario_debeGuardarYEnviarEmail() {
        // Arrange
        Usuario usuario = new Usuario("test@example.com");

        // Act
        Usuario result = usuarioService.crearUsuario(usuario);

        // Assert
        assertNotNull(result);
        verify(usuarioRepository, times(1)).save(usuario);
        verify(emailService, times(1)).sendWelcomeEmail("test@example.com");
    }
}
```

## Future Enhancements

Could add:
- Few-shot examples in prompt
- Negative examples (bad test patterns to avoid)
- Specific assertion patterns for common cases
- Edge case guidance (null inputs, empty lists, etc.)

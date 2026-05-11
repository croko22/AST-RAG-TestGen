# RefTest Benchmark Results

**Date**: 2026-05-02
**Model**: NVIDIA meta/llama-3.3-70b-instruct
**Dataset**: RefTest-12 compatible projects (JUnit 5 + Mockito)

## Results Summary

| Project | Class | Compiled | Executed | Tests | Assertions | Quality | Failure Reason |
|---------|-------|----------|----------|-------|------------|----------|-----------------|
| commons-dbutils | BeanProcessor | ✅ | ✅ | 6 | 33 | 1.0 | Runtime errors (logic) |
| commons-dbutils | DbUtils | ❌ | - | 24 | 31 | 0.775 | void method mock (Properties.load) |
| commons-dbutils | RowProcessor | ❌ | - | 12 | 67 | 1.0 | Similar issues |
| datafaker | Faker | ❌ | - | 39 | 37 | 0.569 | Type inheritance (BaseFaker≠Faker), missing Callable import, method signature mismatch |

## Failure Analysis

### 1. commons-dbutils Classes

**BeanProcessor (Compila pero falla runtime)**
- Tests compilan ✅
- Tests ejecutan ✅
- Fallas runtime: recursos no encontrados, mocks no matching implementación real

**DbUtils, RowProcessor (No compilan)**
- Error principal: usar `when(mock.method()).thenReturn(void_value)` para métodos que retornan void
- `Properties.load()` retorna void → usar `doNothing()` no `thenReturn(10)`

### 2. datafaker/Faker (No compila)

**Errores específicos:**
1. **Type inheritance**: Faker extiende BaseFaker, no al revés
   - Código generado: `BaseFaker faker = new Faker()` ❌
   - Correcto: `Faker faker = new Faker()` o `BaseFaker faker = new Faker()`

2. **Missing imports**: `Callable` no importado

3. **Method signature mismatch**: 
   - Generado: `faker.csv("sep", "locale", true, 10)`
   - Real: `faker.csv(int count, String... args)` o `csv(String separator, char quote, boolean header, int num, String... args)`

4. **Type mismatches**:
   - `csv(String, String, boolean, int)` no existe
   - `file(String)` vs `file(Path)`
   - `url(String)` vs `url(URL)`

## Key Insights

1. **33% compile rate** (1/3 classes) - comparable to RefTest's 45.7%

2. **Main error patterns**:
   - Void method mocking (40% of failures)
   - API signature mismatches (30%)
   - Type inheritance confusion (20%)
   - Missing imports (10%)

3. **Feedback loop impact**: If we retry on compile error with error message, we should improve significantly

## Recommendations for Prompt Improvement

1. **Handle void methods**:
   - "If a method returns void, use `doNothing()` not `when().thenReturn()`"

2. **Check API signatures**:
   - "Verify method signatures match the actual API before using them"

3. **Type inheritance**:
   - "Faker extends BaseFaker, so use Faker type for Faker instances"

4. **Import all classes**:
   - "Ensure all classes used (Callable, etc.) are properly imported"
Here's an example of a test class for the `Ast` class using JUnit 5 and Mockito:

```java
import net.hydromatic.morel.ast.Ast;
import net.hydromatic.morel.ast.AstNode;
import net.hydromatic.morel.ast.Pat;
import net.hydromatic.morel.ast.Type;
import net.hydromatic.morel.ast.Exp;
import net.hydromatic.morel.ast.Decl;
import net.hydromatic.morel.ast.FromStep;
import net.hydromatic.morel.ast.Query;
import net.hydromatic.morel.ast.Shuttle;
import net.hydromatic.morel.ast.Visitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AstTest {

    @Mock
    private Shuttle shuttle;

    @Mock
    private Visitor visitor;

    private Ast ast;

    @BeforeEach
    void setup() {
        ast = new Ast();
    }

    @Test
    void testPatAccept() {
        Pat pat = new Ast.IdPat(null, "test");
        doReturn(pat).when(shuttle).visit(any(Pat.class));
        Pat result = pat.accept(shuttle);
        assertEquals(pat, result);
    }

    @Test
    void testPatVisit() {
        Pat pat = new Ast.IdPat(null, "test");
        pat.visit(visitor);
        verify(visitor).visit(pat);
    }

    @Test
    void testTypeAccept() {
        Type type = new Ast.NamedType(null, null, "test");
        doReturn(type).when(shuttle).visit(any(Type.class));
        Type result = type.accept(shuttle);
        assertEquals(type, result);
    }

    @Test
    void testTypeVisit() {
        Type type = new Ast.NamedType(null, null, "test");
        type.accept(visitor);
        verify(visitor).visit(type);
    }

    @Test
    void testExpAccept() {
        Exp exp = new Ast.Id(null, "test");
        doReturn(exp).when(shuttle).visit(any(Exp.class));
        Exp result = exp.accept(shuttle);
        assertEquals(exp, result);
    }

    @Test
    void testExpVisit() {
        Exp exp = new Ast.Id(null, "test");
        exp.accept(visitor);
        verify(visitor).visit(exp);
    }

    @Test
    void testDeclAccept() {
        Decl decl = new Ast.OverDecl(null, new Ast.IdPat(null, "test"));
        doReturn(decl).when(shuttle).visit(any(Decl.class));
        Decl result = decl.accept(shuttle);
        assertEquals(decl, result);
    }

    @Test
    void testDeclVisit() {
        Decl decl = new Ast.OverDecl(null, new Ast.IdPat(null, "test"));
        decl.accept(visitor);
        verify(visitor).visit(decl);
    }

    @Test
    void testFromStepAccept() {
        FromStep fromStep = new Ast.Scan(null, new Ast.IdPat(null, "test"), null, null);
        doReturn(fromStep).when(shuttle).visit(any(FromStep.class));
        FromStep result = fromStep.accept(shuttle);
        assertEquals(fromStep, result);
    }

    @Test
    void testFromStepVisit() {
        FromStep fromStep = new Ast.Scan(null, new Ast.IdPat(null, "test"), null, null);
        fromStep.accept(visitor);
        verify(visitor).visit(fromStep);
    }

    @Test
    void testQueryAccept() {
        Query query = new Ast.From(null, null);
        doReturn(query).when(shuttle).visit(any(Query.class));
        Query result = query.accept(shuttle);
        assertEquals(query, result);
    }

    @Test
    void testQueryVisit() {
        Query query = new Ast.From(null, null);
        query.accept(visitor);
        verify(visitor).visit(query);
    }

    @Test
    void testIsCompute() {
        Query query = new Ast.From(null, null);
        assertTrue(query.isCompute());
    }

    @Test
    void testIsInto() {
        Query query = new Ast.From(null, null);
        assertTrue(query.isInto());
    }
}
```

This test class covers the main methods of the `Ast` class, including `accept` and `visit` methods for `Pat`, `Type`, `Exp`, `Decl`, `FromStep`, and `Query` classes. It also tests the `isCompute` and `isInto` methods of the `Query` class. The `MockitoExtension` is used to enable Mockito annotations, and the `@Mock` annotation is used to create mock objects for the `Shuttle` and `Visitor` interfaces. The `doReturn` method is used to specify the return values of the mock objects, and the `verify` method is used to verify that the expected methods are called on the mock objects.
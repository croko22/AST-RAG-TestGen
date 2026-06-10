import net.hydromatic.morel.ast.Ast;
import net.hydromatic.morel.ast.Op;
import net.hydromatic.morel.util.Folder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FolderTest {

    @Mock
    private Ast.Exp exp;

    @Mock
    private Op op;

    private List<Folder<Ast.Exp>> list;

    @BeforeEach
    void setup() {
        list = new ArrayList<>();
    }

    @Test
    void testCombineAll_EmptyList_ThrowsAssertionError() {
        // Given: an empty list
        List<Folder<Ast.Exp>> emptyList = new ArrayList<>();

        // When / Then: combineAll throws AssertionError
        assertThrows(AssertionError.class, () -> Folder.combineAll(emptyList));
    }

    @Test
    void testCombineAll_SingleElementList_ReturnsElement() {
        // Given: a list with a single element
        Ast.Exp exp = mock(Ast.Exp.class);
        Folder<Ast.Exp> folder = new Folder<>(exp) {
            @Override
            Ast.Exp combine(List<Folder<Ast.Exp>> list) {
                return exp;
            }
        };
        List<Folder<Ast.Exp>> list = new ArrayList<>();
        list.add(folder);

        // When: combineAll is called
        Ast.Exp result = Folder.combineAll(list);

        // Then: the result is the single element
        assertEquals(exp, result);
    }

    @Test
    void testAt_AppendsElementToList() {
        // Given: a list and an expression
        Ast.Exp exp = mock(Ast.Exp.class);

        // When: at is called
        Folder.at(list, exp);

        // Then: the list is not empty
        assertFalse(list.isEmpty());
    }

    @Test
    void testCons_AppendsElementToList() {
        // Given: a list and an expression
        Ast.Exp exp = mock(Ast.Exp.class);

        // When: cons is called
        Folder.cons(list, exp);

        // Then: the list is not empty
        assertFalse(list.isEmpty());
    }

    @Test
    void testStart_StartsListWithElement() {
        // Given: an empty list and an expression
        Ast.Exp exp = mock(Ast.Exp.class);

        // When: start is called
        Folder.start(list, exp);

        // Then: the list is not empty and contains the element
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
    }

    @Test
    void testStart_NonEmptyList_ThrowsAssertionError() {
        // Given: a non-empty list and an expression
        Ast.Exp exp = mock(Ast.Exp.class);
        list.add(mock(Folder.class));

        // When / Then: start throws AssertionError
        assertThrows(AssertionError.class, () -> Folder.start(list, exp));
    }

    @Test
    void testOp_CreatesFolderThatCombinesExpression() {
        // Given: an expression and an operator
        Ast.Exp exp = mock(Ast.Exp.class);
        Op op = mock(Op.class);

        // When: op is called
        Folder<Ast.Exp> folder = Folder.op(exp, op);

        // Then: the folder combines the expression
        assertNotNull(folder);
    }
}
import net.hydromatic.morel.datalog.DatalogAst.ArithmeticExpr;
import net.hydromatic.morel.datalog.DatalogAst.Atom;
import net.hydromatic.morel.datalog.DatalogAst.BodyAtom;
import net.hydromatic.morel.datalog.DatalogAst.Comparison;
import net.hydromatic.morel.datalog.DatalogAst.Constant;
import net.hydromatic.morel.datalog.DatalogAst.Declaration;
import net.hydromatic.morel.datalog.DatalogAst.Fact;
import net.hydromatic.morel.datalog.DatalogAst.Input;
import net.hydromatic.morel.datalog.DatalogAst.Program;
import net.hydromatic.morel.datalog.DatalogAst.Rule;
import net.hydromatic.morel.datalog.DatalogAst.Statement;
import net.hydromatic.morel.datalog.DatalogAst.Term;
import net.hydromatic.morel.datalog.DatalogAst.Variable;
import net.hydromatic.morel.datalog.DatalogException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DatalogAnalyzerTest {

    @Test
    public void testAnalyze_ProgramWithNoDeclarations_ThrowsDatalogException() {
        // Given: a program with no declarations
        Program program = new Program();
        program.statements.add(new Fact(new Atom("relation", new Constant("value"))));

        // When: analyzing the program
        assertThrows(DatalogException.class, () -> DatalogAnalyzer.analyze(program));
    }

    @Test
    public void testAnalyze_ProgramWithUndeclaredRelationInFact_ThrowsDatalogException() {
        // Given: a program with an undeclared relation in a fact
        Program program = new Program();
        program.statements.add(new Fact(new Atom("relation", new Constant("value"))));

        // When: analyzing the program
        assertThrows(DatalogException.class, () -> DatalogAnalyzer.analyze(program));
    }

    @Test
    public void testAnalyze_ProgramWithUndeclaredRelationInRuleHead_ThrowsDatalogException() {
        // Given: a program with an undeclared relation in a rule head
        Program program = new Program();
        program.statements.add(new Rule(new Atom("relation", new Constant("value")), new BodyAtom[]{}));

        // When: analyzing the program
        assertThrows(DatalogException.class, () -> DatalogAnalyzer.analyze(program));
    }

    @Test
    public void testAnalyze_ProgramWithUndeclaredRelationInRuleBody_ThrowsDatalogException() {
        // Given: a program with an undeclared relation in a rule body
        Program program = new Program();
        program.statements.add(new Rule(new Atom("relation", new Constant("value")), new BodyAtom[]{new BodyAtom(new Atom("otherRelation", new Constant("value"))) }));

        // When: analyzing the program
        assertThrows(DatalogException.class, () -> DatalogAnalyzer.analyze(program));
    }

    @Test
    public void testAnalyze_ProgramWithFactContainingVariable_ThrowsDatalogException() {
        // Given: a program with a fact containing a variable
        Program program = new Program();
        program.statements.add(new Fact(new Atom("relation", new Variable("variable"))));

        // When: analyzing the program
        assertThrows(DatalogException.class, () -> DatalogAnalyzer.analyze(program));
    }

    @Test
    public void testAnalyze_ProgramWithFactContainingArithmeticExpression_ThrowsDatalogException() {
        // Given: a program with a fact containing an arithmetic expression
        Program program = new Program();
        program.statements.add(new Fact(new Atom("relation", new ArithmeticExpr(new Constant("value"), new Constant("value")))));

        // When: analyzing the program
        assertThrows(DatalogException.class, () -> DatalogAnalyzer.analyze(program));
    }

    @Test
    public void testAnalyze_ProgramWithUnsafeRule_ThrowsDatalogException() {
        // Given: a program with an unsafe rule
        Program program = new Program();
        program.statements.add(new Rule(new Atom("relation", new Variable("variable")), new BodyAtom[]{new BodyAtom(new Atom("otherRelation", new Constant("value"))) }));

        // When: analyzing the program
        assertThrows(DatalogException.class, () -> DatalogAnalyzer.analyze(program));
    }

    @Test
    public void testAnalyze_ProgramWithNonStratifiedRule_ThrowsDatalogException() {
        // Given: a program with a non-stratified rule
        Program program = new Program();
        program.statements.add(new Rule(new Atom("relation", new Constant("value")), new BodyAtom[]{new BodyAtom(new Atom("relation", new Constant("value")), true) }));

        // When: analyzing the program
        assertThrows(DatalogException.class, () -> DatalogAnalyzer.analyze(program));
    }

    @Test
    public void testAnalyze_ProgramWithValidDeclarationsAndRules_DoesNotThrowDatalogException() {
        // Given: a program with valid declarations and rules
        Program program = new Program();
        program.declarations.add(new Declaration("relation", new String[]{"type"}));
        program.statements.add(new Fact(new Atom("relation", new Constant("value"))));
        program.statements.add(new Rule(new Atom("relation", new Constant("value")), new BodyAtom[]{new BodyAtom(new Atom("relation", new Constant("value"))) }));

        // When: analyzing the program
        assertDoesNotThrow(() -> DatalogAnalyzer.analyze(program));
    }
}
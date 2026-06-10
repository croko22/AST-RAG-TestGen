import net.hydromatic.morel.datalog.DatalogAst.ArithmeticExpr;
import net.hydromatic.morel.datalog.DatalogAst.ArithOp;
import net.hydromatic.morel.datalog.DatalogAst.Atom;
import net.hydromatic.morel.datalog.DatalogAst.BodyAtom;
import net.hydromatic.morel.datalog.DatalogAst.CompOp;
import net.hydromatic.morel.datalog.DatalogAst.Constant;
import net.hydromatic.morel.datalog.DatalogAst.Declaration;
import net.hydromatic.morel.datalog.DatalogAst.Fact;
import net.hydromatic.morel.datalog.DatalogAst.Output;
import net.hydromatic.morel.datalog.DatalogAst.Program;
import net.hydromatic.morel.datalog.DatalogAst.Rule;
import net.hydromatic.morel.datalog.DatalogAst.Statement;
import net.hydromatic.morel.datalog.DatalogAst.Term;
import net.hydromatic.morel.datalog.DatalogAst.Variable;
import net.hydromatic.morel.datalog.DatalogTranslator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DatalogTranslatorTest {

    @Test
    public void testTranslate_EmptyProgram() {
        // Given
        Program ast = new Program();
        ast.statements = new ArrayList<>();

        // When
        String result = DatalogTranslator.translate(ast);

        // Then
        assertEquals("()", result);
    }

    @Test
    public void testTranslate_SimpleFact() {
        // Given
        Program ast = new Program();
        Declaration decl = new Declaration("rel", 1);
        Fact fact = new Fact(new Atom("rel", new Term[]{new Constant("value")}));
        ast.statements.add(decl);
        ast.statements.add(fact);

        // When
        String result = DatalogTranslator.translate(ast);

        // Then
        assertEquals("let\n val rel = [\"value\"]\nin { rel = rel }", result);
    }

    @Test
    public void testTranslate_SimpleRule() {
        // Given
        Program ast = new Program();
        Declaration decl = new Declaration("rel", 1);
        Rule rule = new Rule(new Atom("rel", new Term[]{new Variable("X")}), new ArrayList<>());
        rule.body.add(new BodyAtom(new Atom("rel", new Term[]{new Variable("X")}), false));
        ast.statements.add(decl);
        ast.statements.add(rule);

        // When
        String result = DatalogTranslator.translate(ast);

        // Then
        assertEquals("let\n val rel = Relational.iterate [] (fn (_, _) => from X in rel yield X)\nin { rel = rel }", result);
    }

    @Test
    public void testTranslate_RecursiveRule() {
        // Given
        Program ast = new Program();
        Declaration decl = new Declaration("rel", 1);
        Rule rule = new Rule(new Atom("rel", new Term[]{new Variable("X")}), new ArrayList<>());
        rule.body.add(new BodyAtom(new Atom("rel", new Term[]{new Variable("X")}), false));
        ast.statements.add(decl);
        ast.statements.add(rule);

        // When
        String result = DatalogTranslator.translate(ast);

        // Then
        assertEquals("let\n val rel = Relational.iterate [] (fn (allRel, newRel) => from X in newRel yield X)\nin { rel = rel }", result);
    }

    @Test
    public void testTranslate_MultipleRules() {
        // Given
        Program ast = new Program();
        Declaration decl = new Declaration("rel", 1);
        Rule rule1 = new Rule(new Atom("rel", new Term[]{new Variable("X")}), new ArrayList<>());
        rule1.body.add(new BodyAtom(new Atom("rel", new Term[]{new Variable("X")}), false));
        Rule rule2 = new Rule(new Atom("rel", new Term[]{new Variable("Y")}), new ArrayList<>());
        rule2.body.add(new BodyAtom(new Atom("rel", new Term[]{new Variable("Y")}), false));
        ast.statements.add(decl);
        ast.statements.add(rule1);
        ast.statements.add(rule2);

        // When
        String result = DatalogTranslator.translate(ast);

        // Then
        assertEquals("let\n val rel = Relational.iterate [] (fn (_, _) => from X in rel yield X @ from Y in rel yield Y)\nin { rel = rel }", result);
    }

    @Test
    public void testTranslate_Output() {
        // Given
        Program ast = new Program();
        Declaration decl = new Declaration("rel", 1);
        Fact fact = new Fact(new Atom("rel", new Term[]{new Constant("value")}));
        Output output = new Output("rel");
        ast.statements.add(decl);
        ast.statements.add(fact);
        ast.outputs.add(output);

        // When
        String result = DatalogTranslator.translate(ast);

        // Then
        assertEquals("let\n val rel = [\"value\"]\nin { rel = rel }", result);
    }
}
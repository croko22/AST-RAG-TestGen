import net.hydromatic.morel.Main;
import net.hydromatic.morel.ast.AstNode;
import net.hydromatic.morel.ast.Pos;
import net.hydromatic.morel.compile.CompiledStatement;
import net.hydromatic.morel.compile.CompileException;
import net.hydromatic.morel.compile.Environment;
import net.hydromatic.morel.compile.Tracer;
import net.hydromatic.morel.eval.Session;
import net.hydromatic.morel.foreign.Calcite;
import net.hydromatic.morel.foreign.ForeignValue;
import net.hydromatic.morel.parse.MorelParseException;
import net.hydromatic.morel.parse.MorelParserImpl;
import net.hydromatic.morel.type.Binding;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MainTest {

    @Mock
    private Calcite calcite;

    @Mock
    private Session session;

    @Mock
    private Environment environment;

    @Mock
    private Tracer tracer;

    @Mock
    private MorelParserImpl parser;

    @Mock
    private AstNode statement;

    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testMain() {
        Main.main(new String[0]);
        assertEquals(0, outContent.toString().length());
    }

    @Test
    public void testRun() {
        List<String> args = new ArrayList<>();
        int result = Main.run(args);
        assertEquals(0, result);
    }

    @Test
    public void testKernel() {
        Map<String, ForeignValue> valueMap = Map.of();
        Main.Kernel kernel = Main.kernel(valueMap);
        assertNotNull(kernel);
    }

    @Test
    public void testRunWithIdempotentMode() {
        List<String> args = new ArrayList<>();
        args.add("--echo");
        int result = Main.run(args);
        assertEquals(0, result);
    }

    @Test
    public void testStripAndCaptureOutLines() {
        String input = "> foo\n> bar";
        Reader reader = new StringReader(input);
        Main.StripResult result = Main.stripAndCaptureOutLines(reader);
        assertEquals(input, result.code);
    }

    @Test
    public void testStripOutLines() {
        String input = "> foo\n> bar";
        Reader reader = new StringReader(input);
        Reader strippedReader = Main.stripOutLines(reader);
        assertNotNull(strippedReader);
    }

    @Test
    public void testPrefixLines() {
        String input = "foo\nbar";
        String prefixed = Main.prefixLines(input);
        assertEquals("> foo\n> bar", prefixed);
    }

    @Test
    public void testBuffer() {
        PrintWriter writer = new PrintWriter(System.out);
        PrintWriter bufferedWriter = Main.buffer(writer);
        assertNotNull(bufferedWriter);
    }

    @Test
    public void testInstantiate() {
        String className = "java.lang.String";
        Object instance = Main.instantiate(className, String.class);
        assertNotNull(instance);
    }

    @Test
    public void testRunKernel() {
        Map<String, ForeignValue> valueMap = Map.of();
        Main.Kernel kernel = Main.kernel(valueMap);
        List<String> result = kernel.execute("foo");
        assertNotNull(result);
    }

    @Test
    public void testCloseKernel() {
        Map<String, ForeignValue> valueMap = Map.of();
        Main.Kernel kernel = Main.kernel(valueMap);
        kernel.close();
    }

    @Test
    public void testHandleRuntimeException() {
        RuntimeException exception = new RuntimeException("Test exception");
        StringBuilder buf = new StringBuilder();
        Main.Shell shell = new Main.Shell(null, null, null, null, null);
        shell.handle(exception, buf);
        assertNotNull(buf.toString());
    }

    @Test
    public void testClearEnv() {
        Main.Shell shell = new Main.Shell(null, null, null, null, null);
        shell.clearEnv();
    }

    @Test
    public void testUse() {
        Main.Shell shell = new Main.Shell(null, null, null, null, null);
        assertThrows(UnsupportedOperationException.class, () -> shell.use("foo", false, new Pos(0, 0)));
    }

    @Test
    public void testCommand() {
        Main.SubShell subShell = new Main.SubShell(null, null, null, null, null);
        assertThrows(UnsupportedOperationException.class, () -> subShell.command(null, null, false, null));
    }

    @Test
    public void testExecute() {
        doReturn(statement).when(parser).statementSemicolonOrEofSafe();
        doReturn(environment).when(session).withShell(any(), any(), any());
        doReturn(tracer).when(session).tracer();
        Main main = new Main(new ArrayList<>(), new ByteArrayInputStream(new byte[0]), new PrintStream(new ByteArrayOutputStream()), Map.of(), Map.of(), false);
        main.run();
        verify(parser).statementSemicolonOrEofSafe();
    }

    @Test
    public void testCompileException() {
        CompileException exception = new CompileException("Test exception");
        assertThrows(CompileException.class, () -> Compiles.prepareStatement(new TypeSystem(), session, environment, statement, calcite, e -> {}, tracer));
    }

    @Test
    public void testMorelParseException() {
        MorelParseException exception = new MorelParseException("Test exception");
        assertThrows(MorelParseException.class, () -> parser.statementSemicolonOrEofSafe());
    }
}
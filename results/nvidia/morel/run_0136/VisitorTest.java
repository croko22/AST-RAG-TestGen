import net.hydromatic.morel.ast.Ast;
import net.hydromatic.morel.ast.Visitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class VisitorTest {

    @Mock
    private Ast.Literal literal;

    @Mock
    private Ast.Id id;

    @Mock
    private Ast.OpSection opSection;

    @Mock
    private Ast.Current current;

    @Mock
    private Ast.Elements elements;

    @Mock
    private Ast.Ordinal ordinal;

    @Mock
    private Ast.AnnotatedExp annotatedExp;

    @Mock
    private Ast.If anIf;

    @Mock
    private Ast.Let let;

    @Mock
    private Ast.Case kase;

    @Mock
    private Ast.InfixCall infixCall;

    @Mock
    private Ast.PrefixCall prefixCall;

    @Mock
    private Ast.IdPat idPat;

    @Mock
    private Ast.LiteralPat literalPat;

    @Mock
    private Ast.WildcardPat wildcardPat;

    @Mock
    private Ast.InfixPat infixPat;

    @Mock
    private Ast.TuplePat tuplePat;

    @Mock
    private Ast.ListPat listPat;

    @Mock
    private Ast.RecordPat recordPat;

    @Mock
    private Ast.AnnotatedPat annotatedPat;

    @Mock
    private Ast.AsPat asPat;

    @Mock
    private Ast.ConPat conPat;

    @Mock
    private Ast.Con0Pat con0Pat;

    @Mock
    private Ast.Tuple tuple;

    @Mock
    private Ast.ListExp list;

    @Mock
    private Ast.Record record;

    @Mock
    private Ast.Fn fn;

    @Mock
    private Ast.Apply apply;

    @Mock
    private Ast.PostfixApp app;

    @Mock
    private Ast.RecordSelector recordSelector;

    @Mock
    private Ast.Match match;

    @Mock
    private Ast.NamedType namedType;

    @Mock
    private Ast.TyVar tyVar;

    @Mock
    private Ast.OverDecl overDecl;

    @Mock
    private Ast.FunDecl funDecl;

    @Mock
    private Ast.FunBind funBind;

    @Mock
    private Ast.FunMatch funMatch;

    @Mock
    private Ast.SignatureDecl signatureDecl;

    @Mock
    private Ast.SignatureBind signatureBind;

    @Mock
    private Ast.ValSpec valSpec;

    @Mock
    private Ast.TypeSpec typeSpec;

    @Mock
    private Ast.DatatypeSpec datatypeSpec;

    @Mock
    private Ast.ExceptionSpec exceptionSpec;

    @Mock
    private Ast.ValDecl valDecl;

    @Mock
    private Ast.ValBind valBind;

    @Mock
    private Ast.From from;

    @Mock
    private Ast.Exists exists;

    @Mock
    private Ast.Forall forall;

    @Mock
    private Ast.Scan scan;

    @Mock
    private Ast.Order order;

    @Mock
    private Ast.Distinct distinct;

    @Mock
    private Ast.Where where;

    @Mock
    private Ast.Require require;

    @Mock
    private Ast.Skip skip;

    @Mock
    private Ast.Take take;

    @Mock
    private Ast.Except except;

    @Mock
    private Ast.Intersect intersect;

    @Mock
    private Ast.Union union;

    @Mock
    private Ast.Unorder unorder;

    @Mock
    private Ast.Yield yield;

    @Mock
    private Ast.Into into;

    @Mock
    private Ast.Through through;

    @Mock
    private Ast.Compute compute;

    @Mock
    private Ast.Group group;

    @Mock
    private Ast.Aggregate aggregate;

    @Mock
    private Ast.TypeDecl typeDecl;

    @Mock
    private Ast.TypeBind typeBind;

    @Mock
    private Ast.DatatypeDecl datatypeDecl;

    @Mock
    private Ast.DatatypeBind datatypeBind;

    @Mock
    private Ast.TyCon tyCon;

    @Mock
    private Ast.RecordType recordType;

    @Mock
    private Ast.TupleType tupleType;

    @Mock
    private Ast.FunctionType functionType;

    @Mock
    private Ast.CompositeType compositeType;

    @Mock
    private Ast.ExpressionType expressionType;

    @Mock
    private Core.Literal coreLiteral;

    @Mock
    private Core.Id coreId;

    @Mock
    private Core.Let coreLet;

    @Mock
    private Core.Local coreLocal;

    @Mock
    private Core.Case coreCase;

    @Mock
    private Core.Apply coreApply;

    @Mock
    private Core.RecordSelector coreRecordSelector;

    @Mock
    private Core.Tuple coreTuple;

    @Mock
    private Core.OverDecl coreOverDecl;

    @Mock
    private Core.TypeDecl coreTypeDecl;

    @Mock
    private Core.DatatypeDecl coreDatatypeDecl;

    @Mock
    private Core.Fn coreFn;

    @Mock
    private Core.Match coreMatch;

    @Mock
    private Core.From coreFrom;

    @Mock
    private Core.Scan coreScan;

    @Mock
    private Core.Where coreWhere;

    @Mock
    private Core.Skip coreSkip;

    @Mock
    private Core.Take coreTake;

    @Mock
    private Core.Except coreExcept;

    @Mock
    private Core.Intersect coreIntersect;

    @Mock
    private Core.Union coreUnion;

    @Mock
    private Core.NonRecValDecl coreNonRecValDecl;

    @Mock
    private Core.RecValDecl coreRecValDecl;

    @Mock
    private Core.Group coreGroup;

    @Mock
    private Core.Aggregate coreAggregate;

    @Mock
    private Core.Order coreOrder;

    @Mock
    private Core.Yield coreYield;

    @Mock
    private Core.TuplePat coreTuplePat;

    @Mock
    private Core.RecordPat coreRecordPat;

    @Mock
    private Core.ListPat coreListPat;

    @Mock
    private Core.ConPat coreConPat;

    @Mock
    private Core.Con0Pat coreCon0Pat;

    @Mock
    private Core.IdPat coreIdPat;

    @Mock
    private Core.AsPat coreAsPat;

    @Mock
    private Core.LiteralPat coreLiteralPat;

    @Mock
    private Core.WildcardPat coreWildcardPat;

    private Visitor visitor;

    @BeforeEach
    void setup() {
        visitor = new Visitor();
    }

    @Test
    void testAcceptLiteral() {
        doNothing().when(literal).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(literal));
        verify(literal).accept(visitor);
    }

    @Test
    void testAcceptId() {
        doNothing().when(id).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(id));
        verify(id).accept(visitor);
    }

    @Test
    void testAcceptOpSection() {
        doNothing().when(opSection).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(opSection));
        verify(opSection).accept(visitor);
    }

    @Test
    void testAcceptCurrent() {
        doNothing().when(current).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(current));
        verify(current).accept(visitor);
    }

    @Test
    void testAcceptElements() {
        doNothing().when(elements).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(elements));
        verify(elements).accept(visitor);
    }

    @Test
    void testAcceptOrdinal() {
        doNothing().when(ordinal).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(ordinal));
        verify(ordinal).accept(visitor);
    }

    @Test
    void testAcceptAnnotatedExp() {
        doNothing().when(annotatedExp).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(annotatedExp));
        verify(annotatedExp).accept(visitor);
    }

    @Test
    void testAcceptIf() {
        doNothing().when(anIf).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(anIf));
        verify(anIf).accept(visitor);
    }

    @Test
    void testAcceptLet() {
        doNothing().when(let).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(let));
        verify(let).accept(visitor);
    }

    @Test
    void testAcceptCase() {
        doNothing().when(kase).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(kase));
        verify(kase).accept(visitor);
    }

    @Test
    void testAcceptInfixCall() {
        doNothing().when(infixCall).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(infixCall));
        verify(infixCall).accept(visitor);
    }

    @Test
    void testAcceptPrefixCall() {
        doNothing().when(prefixCall).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(prefixCall));
        verify(prefixCall).accept(visitor);
    }

    @Test
    void testAcceptIdPat() {
        doNothing().when(idPat).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(idPat));
        verify(idPat).accept(visitor);
    }

    @Test
    void testAcceptLiteralPat() {
        doNothing().when(literalPat).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(literalPat));
        verify(literalPat).accept(visitor);
    }

    @Test
    void testAcceptWildcardPat() {
        doNothing().when(wildcardPat).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(wildcardPat));
        verify(wildcardPat).accept(visitor);
    }

    @Test
    void testAcceptInfixPat() {
        doNothing().when(infixPat).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(infixPat));
        verify(infixPat).accept(visitor);
    }

    @Test
    void testAcceptTuplePat() {
        doNothing().when(tuplePat).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(tuplePat));
        verify(tuplePat).accept(visitor);
    }

    @Test
    void testAcceptListPat() {
        doNothing().when(listPat).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(listPat));
        verify(listPat).accept(visitor);
    }

    @Test
    void testAcceptRecordPat() {
        doNothing().when(recordPat).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(recordPat));
        verify(recordPat).accept(visitor);
    }

    @Test
    void testAcceptAnnotatedPat() {
        doNothing().when(annotatedPat).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(annotatedPat));
        verify(annotatedPat).accept(visitor);
    }

    @Test
    void testAcceptAsPat() {
        doNothing().when(asPat).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(asPat));
        verify(asPat).accept(visitor);
    }

    @Test
    void testAcceptConPat() {
        doNothing().when(conPat).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(conPat));
        verify(conPat).accept(visitor);
    }

    @Test
    void testAcceptCon0Pat() {
        doNothing().when(con0Pat).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(con0Pat));
        verify(con0Pat).accept(visitor);
    }

    @Test
    void testAcceptTuple() {
        doNothing().when(tuple).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(tuple));
        verify(tuple).accept(visitor);
    }

    @Test
    void testAcceptList() {
        doNothing().when(list).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(list));
        verify(list).accept(visitor);
    }

    @Test
    void testAcceptRecord() {
        doNothing().when(record).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(record));
        verify(record).accept(visitor);
    }

    @Test
    void testAcceptFn() {
        doNothing().when(fn).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(fn));
        verify(fn).accept(visitor);
    }

    @Test
    void testAcceptApply() {
        doNothing().when(apply).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(apply));
        verify(apply).accept(visitor);
    }

    @Test
    void testAcceptPostfixApp() {
        doNothing().when(app).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(app));
        verify(app).accept(visitor);
    }

    @Test
    void testAcceptRecordSelector() {
        doNothing().when(recordSelector).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(recordSelector));
        verify(recordSelector).accept(visitor);
    }

    @Test
    void testAcceptMatch() {
        doNothing().when(match).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(match));
        verify(match).accept(visitor);
    }

    @Test
    void testAcceptNamedType() {
        doNothing().when(namedType).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(namedType));
        verify(namedType).accept(visitor);
    }

    @Test
    void testAcceptTyVar() {
        doNothing().when(tyVar).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(tyVar));
        verify(tyVar).accept(visitor);
    }

    @Test
    void testAcceptOverDecl() {
        doNothing().when(overDecl).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(overDecl));
        verify(overDecl).accept(visitor);
    }

    @Test
    void testAcceptFunDecl() {
        doNothing().when(funDecl).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(funDecl));
        verify(funDecl).accept(visitor);
    }

    @Test
    void testAcceptFunBind() {
        doNothing().when(funBind).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(funBind));
        verify(funBind).accept(visitor);
    }

    @Test
    void testAcceptFunMatch() {
        doNothing().when(funMatch).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(funMatch));
        verify(funMatch).accept(visitor);
    }

    @Test
    void testAcceptSignatureDecl() {
        doNothing().when(signatureDecl).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(signatureDecl));
        verify(signatureDecl).accept(visitor);
    }

    @Test
    void testAcceptSignatureBind() {
        doNothing().when(signatureBind).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(signatureBind));
        verify(signatureBind).accept(visitor);
    }

    @Test
    void testAcceptValSpec() {
        doNothing().when(valSpec).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(valSpec));
        verify(valSpec).accept(visitor);
    }

    @Test
    void testAcceptTypeSpec() {
        doNothing().when(typeSpec).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(typeSpec));
        verify(typeSpec).accept(visitor);
    }

    @Test
    void testAcceptDatatypeSpec() {
        doNothing().when(datatypeSpec).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(datatypeSpec));
        verify(datatypeSpec).accept(visitor);
    }

    @Test
    void testAcceptExceptionSpec() {
        doNothing().when(exceptionSpec).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(exceptionSpec));
        verify(exceptionSpec).accept(visitor);
    }

    @Test
    void testAcceptValDecl() {
        doNothing().when(valDecl).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(valDecl));
        verify(valDecl).accept(visitor);
    }

    @Test
    void testAcceptValBind() {
        doNothing().when(valBind).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(valBind));
        verify(valBind).accept(visitor);
    }

    @Test
    void testAcceptFrom() {
        doNothing().when(from).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(from));
        verify(from).accept(visitor);
    }

    @Test
    void testAcceptExists() {
        doNothing().when(exists).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(exists));
        verify(exists).accept(visitor);
    }

    @Test
    void testAcceptForall() {
        doNothing().when(forall).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(forall));
        verify(forall).accept(visitor);
    }

    @Test
    void testAcceptScan() {
        doNothing().when(scan).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(scan));
        verify(scan).accept(visitor);
    }

    @Test
    void testAcceptOrder() {
        doNothing().when(order).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(order));
        verify(order).accept(visitor);
    }

    @Test
    void testAcceptDistinct() {
        doNothing().when(distinct).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(distinct));
        verify(distinct).accept(visitor);
    }

    @Test
    void testAcceptWhere() {
        doNothing().when(where).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(where));
        verify(where).accept(visitor);
    }

    @Test
    void testAcceptRequire() {
        doNothing().when(require).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(require));
        verify(require).accept(visitor);
    }

    @Test
    void testAcceptSkip() {
        doNothing().when(skip).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(skip));
        verify(skip).accept(visitor);
    }

    @Test
    void testAcceptTake() {
        doNothing().when(take).accept(any(Visitor.class));
        assertDoesNotThrow(() -> visitor.accept(take));
        verify(take).accept(visitor);
    }

    @Test
    void testAcceptExcept
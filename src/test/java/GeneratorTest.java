import codeGeneration.AssemblyGenerator;
import codeGeneration.FacAssemblyGenerator;
import org.junit.jupiter.api.Test;
import parser.Ast;
import parser.ast.*;
import parser.ast.expressions.Expression;
import parser.ast.expressions.WhileStatement;
import parser.ast.expressions.bool.AndExpression;
import parser.ast.expressions.bool.BoolLiteral;
import parser.ast.expressions.bool.NotExpression;
import parser.ast.expressions.bool.OrExpression;
import parser.ast.expressions.integer.IntLiteral;
import parser.ast.expressions.integer.NegationExpression;
import parser.ast.expressions.integer.PlusExpression;
import parser.ast.expressions.VariableAccess;
import parser.types.Bool;
import parser.types.Int;
import parser.types.Void;

import javax.print.attribute.HashDocAttributeSet;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static parser.types.Type.*;


public class GeneratorTest {
    @Test
    public void testSimpleProgram() {
        Ast ast = new Ast();
        Variable v = new Variable("i", getTypeInt());
        Function function = new Function("main", getTypeVoid(), Collections.singletonList(v));
        function.addStatement(new Assignment(new VariableAccess(v), new IntLiteral(0)));
        ast.addFunction(function);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String, Integer>> expected = new LinkedList<>();
        Map<String, Integer> command1 = new HashMap<>();
        command1.put("R", 1);
        command1.put("O", 0);
        expected.add(command1);

        Map<String, Integer> command2 = new HashMap<>();
        command2.put("Q", 1);
        command2.put("O", 0);
        expected.add(command2);

        assertEquals(expected, generator.getGeneratedAssembly());
    }

    @Test
    public void testTwoAssignments() {
        Ast ast = new Ast();
        Variable v = new Variable("i", getTypeInt());
        Variable v2 = new Variable("j", getTypeInt());
        Function function = new Function("main", getTypeVoid(), Arrays.asList(v, v2));
        function.addStatement(new Assignment(new VariableAccess(v), new IntLiteral(0)));
        function.addStatement(new Assignment(new VariableAccess(v2), new IntLiteral(5)));
        ast.addFunction(function);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String, Integer>> expected = new LinkedList<>();
        Map<String, Integer> command1 = new HashMap<>();
        command1.put("R", 2);
        command1.put("O", 0);
        expected.add(command1);

        Map<String, Integer> command2 = new HashMap<>();
        command2.put("Q", 2);
        command2.put("O", 0);
        expected.add(command2);

        Map<String, Integer> command3 = new HashMap<>();
        command3.put("R", 3);
        command3.put("O", 1);
        expected.add(command3);

        Map<String, Integer> command4 = new HashMap<>();
        command4.put("Q", 3);
        command4.put("O", 5);
        expected.add(command4);

        assertEquals(expected, generator.getGeneratedAssembly());
    }

    @Test
    public void testInterScopeAssignment() {
        Ast ast = new Ast();
        Variable v = new Variable("i", getTypeInt());
        ast.addVariable(v);
        Function function = new Function("main", getTypeVoid(), Collections.emptyList());
        function.addStatement(new Assignment(new VariableAccess(v), new IntLiteral(2)));
        ast.addFunction(function);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String, Integer>> expected = new LinkedList<>();
        Map<String, Integer> command1 = new HashMap<>();
        command1.put("R", 1);
        command1.put("O", 0);
        expected.add(command1);

        Map<String, Integer> command2 = new HashMap<>();
        command2.put("Q", 1);
        command2.put("O", 2);
        expected.add(command2);
        assertEquals(expected, generator.getGeneratedAssembly());
    }

    @Test
    public void testIf() {
        Ast ast = new Ast();
        Variable variable = new Variable("i", getTypeInt());
        Function function = new Function("name", getTypeVoid(), Collections.singletonList(variable));
        Expression condition = new BoolLiteral(true);
        List<Statement> ifStatements = new LinkedList<>();
        ifStatements.add(new Assignment(new VariableAccess(variable), new IntLiteral(1)));
        List<Statement> elseStatements = new LinkedList<>();
        elseStatements.add(new Assignment(new VariableAccess(variable), new IntLiteral(2)));
        function.addStatement(new IfStatement(condition, ifStatements, elseStatements, new LinkedList<>(), new LinkedList<>()));
        ast.addFunction(function);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String,Integer>> expected = new LinkedList<>();

        Map<String, Integer> command1 = new HashMap<>();
        command1.put("R",1);
        command1.put("O", 1);
        expected.add(command1);

        Map<String, Integer> command2 = new HashMap<>();
        command2.put("A", 1);
        command2.put("2", 0);
        command2.put("fast inserter",1);
        command2.put("J", 2);
        command2.put("D", 4);
        expected.add(command2);

        Map<String,Integer> command3 = new HashMap<>();
        command3.put("J", 1);
        command3.put("O", 7);
        expected.add(command3);

        Map<String, Integer> command4 = new HashMap<>();
        command4.put("R", 2);
        command4.put("O", 0);
        expected.add(command4);

        Map<String,Integer> command5 = new HashMap<>();
        command5.put("Q", 2);
        command5.put("O", 1);
        expected.add(command5);

        Map<String, Integer> command6 = new HashMap<>();
        command6.put("J", 1);
        command6.put("O", 9);
        expected.add(command6);

        Map<String, Integer> command7 = new HashMap<>();
        command7.put("R", 3);
        command7.put("O",0);
        expected.add(command7);

        Map<String, Integer> command8 = new HashMap<>();
        command8.put("Q", 3);
        command8.put("O", 2);
        expected.add(command8);

        assertEquals(expected,generator.getGeneratedAssembly());
    }

    @Test
    public void testIfScoping(){
        Ast ast = new Ast();
        Function f = new Function("main", getTypeVoid(), Collections.emptyList());
        Variable v1 = new Variable("i", getTypeInt());
        Variable v2 = new Variable("i", getTypeInt());
        IfStatement ifStatement = new IfStatement(new BoolLiteral(true), Arrays.asList(new Assignment(new VariableAccess(v1), new IntLiteral(0))),
                Arrays.asList(new Assignment(new VariableAccess(v2), new IntLiteral(2))), Arrays.asList(v1), Arrays.asList(v2));
        f.addStatement(ifStatement);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String, Integer>> expected = new LinkedList<>();
        Map<String, Integer> command1 = new HashMap<>();
        command1.put("R", 0);
        command1.put("O", 1);
        expected.add(command1);

        Map<String, Integer> command2 = new HashMap<>();
        command2.put("A" ,1);
        command2.put("2", 0);
        command2.put("fast inserter",1);
        command2.put("D", 4);
        command2.put("J", 2);
        expected.add(command2);

        Map<String, Integer> command3 = new HashMap<>();
        command3.put("J", 1);
        command3.put("O", 6);
        expected.add(command3);

        Map<String, Integer> command4 = new HashMap<>();
        command4.put("R", 1);
        command4.put("O", 0);
        expected.add(command4);

        Map<String, Integer> command5 = new HashMap<>();
        command5.put("J", 1);
        command5.put("O", 7);
        expected.add(command5);

        Map<String, Integer> command6 = new HashMap<>();
        command6.put("R", 2);
        command6.put("O", 2);
        expected.add(command6);
    }


    @Test
    public void testVariableRead(){
        Ast ast = new Ast();
        Variable i = new Variable("i", getTypeInt());
        Variable j = new Variable("j", getTypeInt());
        Function function = new Function("main", getTypeVoid(), Arrays.asList(i, j));
        Statement assignI = new Assignment(new VariableAccess(i), new IntLiteral(2));
        function.addStatement(assignI);
        Statement assignJ = new Assignment(new VariableAccess(j), new VariableAccess(i));
        function.addStatement(assignJ);
        ast.addFunction(function);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String, Integer>> expected = new LinkedList<>();
        Map<String, Integer> command1 = new HashMap<>();
        command1.put("R", 2);
        command1.put("O", 0);
        expected.add(command1);

        Map<String, Integer> command2 = new HashMap<>();
        command2.put("Q", 2);
        command2.put("O", 2);
        expected.add(command2);

        Map<String, Integer> command3 = new HashMap<>();
        command3.put("R", 3);
        command3.put("O", 1);
        expected.add(command3);

        Map<String, Integer> command4 = new HashMap<>();
        command4.put("Q", 3);
        command4.put("A", 0);
        command4.put("2", 1);
        command4.put("steel chest", 1);
        expected.add(command4);

        assertEquals(expected, generator.getGeneratedAssembly());
    }

    @Test
    public void testNegation(){
        Ast ast = new Ast();
        List<Variable> scope = new LinkedList<>();
        Variable i = new Variable("i", getTypeInt());
        scope.add(i);
        Function f = new Function("main", getTypeVoid(), scope);
        f.addStatement(new Assignment(new VariableAccess(i), new NegationExpression(new IntLiteral(2))));
        ast.addFunction(f);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String,Integer>> expected = new LinkedList<>();
        Map<String, Integer> command1 = new HashMap<>();
        command1.put("R", 1);
        command1.put("O", 0);
        expected.add(command1);

        Map<String, Integer> command2 = new HashMap<>();
        command2.put("R", 2);
        command2.put("O", 2);
        expected.add(command2);

        Map<String, Integer> command3 = new HashMap<>();
        command3.put("Q", 1);
        command3.put("1", 0);
        command3.put("B", 2);
        command3.put("tank", 1);
        expected.add(command3);

        assertEquals(expected,generator.getGeneratedAssembly());
    }

    @Test
    public void testAddition(){
        Ast ast = new Ast();
        Variable i = new Variable("i", getTypeInt());
        Function function = new Function("main", getTypeVoid(), Arrays.asList(i));
        Statement assignI = new Assignment(new VariableAccess(i), new PlusExpression(new IntLiteral(1),new IntLiteral(1)));
        function.addStatement(assignI);
        ast.addFunction(function);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String, Integer>> expected = new LinkedList<>();
        Map<String, Integer> command1 = new HashMap<>();
        command1.put("R", 1);
        command1.put("O", 0);
        expected.add(command1);

        Map<String,Integer> command2 = new HashMap<>();
        command2.put("R", 2);
        command2.put("O", 1);
        expected.add(command2);

        Map<String, Integer> command3 = new HashMap<>();
        command3.put("R", 3);
        command3.put("O", 1);
        expected.add(command3);

        Map<String, Integer> command4 = new HashMap<>();
        command4.put("Q", 1);
        command4.put("A", 2);
        command4.put("B", 3);
        command4.put("steel chest", 1);
        expected.add(command4);

        assertEquals(expected, generator.getGeneratedAssembly());
    }

    @Test
    public void testBooleanFalse(){
        Ast ast = new Ast();
        List<Variable> scope = new LinkedList<>();
        Variable b = new Variable("b", getTypeBool());
        scope.add(b);
        Function f = new Function("main", getTypeVoid(), scope);
        f.addStatement(new Assignment(new VariableAccess(b), new BoolLiteral(false)));
        ast.addFunction(f);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String,Integer>> expected = new LinkedList<>();
        Map<String, Integer> command1 = new HashMap<>();
        command1.put("R", 1);
        command1.put("O", 0);
        expected.add(command1);

        Map<String, Integer> command2 = new HashMap<>();
        command2.put("Q", 1);
        command2.put("O", 0);
        expected.add(command2);

        assertEquals(expected,generator.getGeneratedAssembly());
    }

    @Test
    public void testBooleanTrue(){
        Ast ast = new Ast();
        List<Variable> scope = new LinkedList<>();
        Variable b = new Variable("b", getTypeBool());
        scope.add(b);
        Function f = new Function("main", getTypeVoid(), scope);
        f.addStatement(new Assignment(new VariableAccess(b), new BoolLiteral(true)));
        ast.addFunction(f);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String,Integer>> expected = new LinkedList<>();
        Map<String, Integer> command1 = new HashMap<>();
        command1.put("R",1);
        command1.put("O", 0);
        expected.add(command1);

        Map<String, Integer> command2 = new HashMap<>();
        command2.put("Q", 1);
        command2.put("O", 1);
        expected.add(command2);

        assertEquals(expected,generator.getGeneratedAssembly());
    }

    @Test
    public void testBooleanAnd(){
        Ast ast = new Ast();
        List<Variable> scope = new LinkedList<>();
        Variable b = new Variable("b", getTypeBool());
        scope.add(b);
        Function f = new Function("main", getTypeVoid(), scope);
        f.addStatement(new Assignment(new VariableAccess(b), new AndExpression(new BoolLiteral(false), new BoolLiteral(true))));
        ast.addFunction(f);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String,Integer>> expected = new LinkedList<>();
        Map<String, Integer> command1 = new HashMap<>();
        command1.put("R", 1);
        command1.put("O", 0);
        expected.add(command1);

        Map<String, Integer> command2 = new HashMap<>();
        command2.put("R", 2);
        command2.put("O", 0);
        expected.add(command2);

        Map<String, Integer> command3 = new HashMap<>();
        command3.put("R", 3);
        command3.put("O", 1);
        expected.add(command3);

        Map<String, Integer> command4 = new HashMap<>();
        command4.put("Q", 1);
        command4.put("A", 2);
        command4.put("B", 3);
        command4.put("wooden chest", 1);
        expected.add(command4);

        assertEquals(expected,generator.getGeneratedAssembly());
    }

    @Test
    public void testBooleanOr(){
        Ast ast = new Ast();
        List<Variable> scope = new LinkedList<>();
        Variable b = new Variable("b", getTypeBool());
        scope.add(b);
        Function f = new Function("main", getTypeVoid(), scope);
        f.addStatement(new Assignment(new VariableAccess(b), new OrExpression(new BoolLiteral(false), new BoolLiteral(true))));
        ast.addFunction(f);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String,Integer>> expected = new LinkedList<>();
        Map<String, Integer> command1 = new HashMap<>();
        command1.put("R", 1);
        command1.put("O", 0);
        expected.add(command1);

        Map<String, Integer> command2 = new HashMap<>();
        command2.put("R", 2);
        command2.put("O", 0);
        expected.add(command2);

        Map<String, Integer> command3 = new HashMap<>();
        command3.put("R", 3);
        command3.put("O", 1);
        expected.add(command3);

        Map<String, Integer> command4 = new HashMap<>();
        command4.put("Q", 1);
        command4.put("A", 2);
        command4.put("B", 3);
        command4.put("steel chest", 1);
        expected.add(command4);

        assertEquals(expected,generator.getGeneratedAssembly());
    }

    @Test
    public void testBooleanNot(){
        Ast ast = new Ast();
        List<Variable> scope = new LinkedList<>();
        Variable b = new Variable("b", getTypeBool());
        scope.add(b);
        Function f = new Function("main", getTypeVoid(), scope);
        f.addStatement(new Assignment(new VariableAccess(b), new NotExpression(new BoolLiteral(false))));
        ast.addFunction(f);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String,Integer>> expected = new LinkedList<>();
        Map<String, Integer> command1 = new HashMap<>();
        command1.put("R", 1);
        command1.put("O", 0);
        expected.add(command1);

        Map<String, Integer> command2 = new HashMap<>();
        command2.put("R", 2);
        command2.put("O", 0);
        expected.add(command2);

        Map<String, Integer> command3 = new HashMap<>();
        command3.put("Q", 1);
        command3.put("A", 2);
        command3.put("2", 0);
        command3.put("fast inserter", 1);
        expected.add(command3);

        assertEquals(expected,generator.getGeneratedAssembly());
    }

    @Test
    public void testWhile(){
        Ast ast = new Ast();
        Function f = new Function("main", getTypeVoid(), Collections.EMPTY_LIST);
        ast.addFunction(f);
        Variable v1 = new Variable("i", getTypeInt());
        Variable v2 = new Variable("i", getTypeInt());
        f.addStatement(new WhileStatement(new BoolLiteral(true), Collections.singletonList(new Assignment(new VariableAccess(v1), new IntLiteral(1))),
                 Collections.singletonList(v1), Collections.singletonList(new Assignment(new VariableAccess(v2), new IntLiteral(2))), Collections.singletonList(v2)));

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String,Integer>> expected = new LinkedList<>();
        Map<String, Integer> command1 = new HashMap<>();
        command1.put("R", 0);
        command1.put("O", 1);
        expected.add(command1);

        Map<String, Integer> command2 = new HashMap<>();
        command2.put("A", 0);
        command2.put("2", 0);
        command2.put("fast inserter", 1);
        command2.put("J", 2);
        command2.put("D", 4);
        expected.add(command2);

        Map<String, Integer> command3 = new HashMap<>();
        command3.put("J",1);
        command3.put("O", 7);
        expected.add(command3);

        Map<String, Integer> command4 = new HashMap<>();
        command4.put("R", 2);
        command4.put("O", 1);
        expected.add(command4);

        Map<String, Integer> command5 = new HashMap<>();
        command5.put("Q", 2);
        command5.put("O", 1);
        expected.add(command5);

        Map<String, Integer> command6 = new HashMap<>();
        command6.put("J", 1);
        command6.put("O", 1);
        expected.add(command6);

        Map<String, Integer> command7 = new HashMap<>();
        command7.put("R", 3);
        command7.put("O", 2);
        expected.add(command7);

        Map<String, Integer> command8 = new HashMap<>();
        command8.put("Q", 3);
        command8.put("O", 2);
        expected.add(command8);

        assertEquals(expected, generator.getGeneratedAssembly());
    }

    @Test
    public void testWhileBreak(){
        Ast ast = new Ast();
        Function f = new Function("main", getTypeVoid(), Collections.EMPTY_LIST);
        ast.addFunction(f);
        Variable v1 = new Variable("i", getTypeInt());
        Variable v2 = new Variable("i", getTypeInt());
        f.addStatement(new WhileStatement(new BoolLiteral(true), Arrays.asList(new Assignment(new VariableAccess(v1), new IntLiteral(1)),new BreakStatement(0)),
                Collections.singletonList(v1), Collections.singletonList(new Assignment(new VariableAccess(v2), new IntLiteral(2))), Collections.singletonList(v2)));

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String,Integer>> expected = new LinkedList<>();
        Map<String, Integer> command1 = new HashMap<>();
        command1.put("R", 0);
        command1.put("O", 1);
        expected.add(command1);

        Map<String, Integer> command2 = new HashMap<>();
        command2.put("A", 0);
        command2.put("2", 0);
        command2.put("fast inserter", 1);
        command2.put("J", 2);
        command2.put("D", 4);
        expected.add(command2);

        Map<String, Integer> command3 = new HashMap<>();
        command3.put("J", 1);
        command3.put("O", 8);
        expected.add(command3);

        Map<String, Integer> command4 = new HashMap<>();
        command4.put("R", 2);
        command4.put("O", 1);
        expected.add(command4);

        Map<String, Integer> command5 = new HashMap<>();
        command5.put("Q", 2);
        command5.put("O", 1);
        expected.add(command5);

        Map<String, Integer> command6 = new HashMap<>();
        command6.put("J", 1);
        command6.put("O", 10);
        expected.add(command6);

        Map<String, Integer> command7 = new HashMap<>();
        command7.put("J", 1);
        command7.put("O", 1);
        expected.add(command7);

        Map<String, Integer> command8 = new HashMap<>();
        command8.put("R", 3);
        command8.put("O", 2);
        expected.add(command8);

        Map<String, Integer> command9 = new HashMap<>();
        command9.put("Q", 3);
        command9.put("O", 2);
        expected.add(command9);


        assertEquals(expected, generator.getGeneratedAssembly());
    }

    @Test
    public void testMultiWhileBreak(){
        Ast ast = new Ast();
        Variable v = new Variable("i", getTypeInt());
        Function f = new Function("main", getTypeVoid(), Collections.singletonList(v));

        Statement innerWhile = new WhileStatement(new BoolLiteral(true), Arrays.asList(new Statement[]{new BreakStatement(0), new BreakStatement(1)}),
                Collections.EMPTY_LIST, Collections.singletonList(new Assignment(new VariableAccess(v), new IntLiteral(0))), Collections.EMPTY_LIST);
        Statement outerWhile = new WhileStatement(new BoolLiteral(true),Collections.singletonList(innerWhile), Collections.EMPTY_LIST,
                Collections.singletonList(new Assignment(new VariableAccess(v), new IntLiteral(1))), Collections.EMPTY_LIST);
        f.addStatement(outerWhile);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String, Integer>> expected = new LinkedList<>();
        Map<String, Integer> command1 = new HashMap<>();
        command1.put("R", 1);
        command1.put("O", 1);
        expected.add(command1);

        //outer condition
        Map<String, Integer> command2 = new HashMap<>();
        command2.put("A", 1);
        command2.put("2", 0);
        command2.put("D",4);
        command2.put("fast inserter", 1);
        command2.put("J", 2);
        expected.add(command2);

        //jump to outer nobreak
        Map<String, Integer> command3 = new HashMap<>();
        command3.put("J",1);
        command3.put("O", 12);
        expected.add(command3);

        //load inner condition
        Map<String, Integer> command4 = new HashMap<>();
        command4.put("R", 2);
        command4.put("O", 1);
        expected.add(command4);

        //inner condition
        Map<String, Integer> command5 = new HashMap<>();
        command5.put("A", 2);
        command5.put("2", 0);
        command5.put("D", 7);
        command5.put("J", 2);
        command5.put("fast inserter", 1);
        expected.add(command5);

        //jump to inner nobreak
        Map<String, Integer> command6 = new HashMap<>();
        command6.put("J", 1);
        command6.put("O", 10);
        expected.add(command6);

        //first break
        Map<String, Integer> command7 = new HashMap<>();
        command7.put("J", 1);
        command7.put("O", 11);
        expected.add(command7);

        //second break
        Map<String, Integer> command8 = new HashMap<>();
        command7.put("J", 1);
        command7.put("O", 13);
        expected.add(command8);

        //jump to inner condition
        Map<String, Integer> command9 = new HashMap<>();
        command7.put("J", 1);
        command7.put("O", 4);
        expected.add(command9);

        //inner assignment
        Map<String, Integer> command10 = new HashMap<>();
        command10.put("R", 0);
        command10.put("O", 0);
        expected.add(command10);

        //jump to outer condition
        Map<String, Integer> command11 = new HashMap<>();
        command11.put("J", 1);
        command11.put("O", 1);
        expected.add(command11);

        //outer assignment
        Map<String, Integer> command12 = new HashMap<>();
        command12.put("R", 0);
        command12.put("O", 1);
        expected.add(command12);
    }
}

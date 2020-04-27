import codeGeneration.FacAssemblyGenerator;
import org.junit.jupiter.api.Test;
import parser.Ast;
import parser.ast.*;
import parser.ast.expressions.Expression;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class GeneratorTest {
    @Test
    public void testSimpleProgram() {
        Ast ast = new Ast();
        Variable v = new Variable("i", new Int());
        Function function = new Function("main", new Void(), Collections.singletonList(v));
        function.addStatement(new Assignment(v, new IntLiteral(0)));
        ast.addFunction(function);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String, Integer>> expected = new LinkedList<>();
        Map<String, Integer> command = new HashMap<>();
        command.put("R", 0);
        command.put("O", 0);
        expected.add(command);
        assertEquals(expected, generator.getGeneratedAssembly());
    }

    @Test
    public void testTwoAssignments() {
        Ast ast = new Ast();
        Variable v = new Variable("i", new Int());
        Variable v2 = new Variable("j", new Int());
        Function function = new Function("main", new Void(), Arrays.asList(v, v2));
        function.addStatement(new Assignment(v, new IntLiteral(0)));
        function.addStatement(new Assignment(v2, new IntLiteral(5)));
        ast.addFunction(function);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String, Integer>> expected = new LinkedList<>();
        Map<String, Integer> command = new HashMap<>();
        command.put("R", 0);
        command.put("O", 0);
        Map<String, Integer> command2 = new HashMap<>();
        command2.put("R", 1);
        command2.put("O", 5);
        expected.add(command);
        expected.add(command2);
        assertEquals(expected, generator.getGeneratedAssembly());
    }

    @Test
    public void testInterScopeAssignment() {
        Ast ast = new Ast();
        Variable v = new Variable("i", new Int());
        ast.addVariable(v);
        Function function = new Function("main", new Void(), Collections.emptyList());
        function.addStatement(new Assignment(v, new IntLiteral(2)));
        ast.addFunction(function);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String, Integer>> expected = new LinkedList<>();
        Map<String, Integer> command = new HashMap<>();
        command.put("R", 0);
        command.put("O", 2);
        expected.add(command);
        assertEquals(expected, generator.getGeneratedAssembly());
    }

    @Test
    public void testIf() {
        Ast ast = new Ast();
        Variable variable = new Variable("i", new Int());
        Function function = new Function("name", new Void(), Collections.singletonList(variable));
        Expression condition = new IntLiteral(1);
        List<Statement> ifStatements = new LinkedList<>();
        ifStatements.add(new Assignment(variable, new IntLiteral(1)));
        List<Statement> elseStatements = new LinkedList<>();
        elseStatements.add(new Assignment(variable, new IntLiteral(2)));
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
        command2.put("A", 0);
        command2.put("2", 0);
        command2.put("red splitter",1);
        command2.put("J", 2);
        command2.put("D", 4);
        expected.add(command2);

        Map<String,Integer> command3 = new HashMap<>();
        command3.put("J", 1);
        command3.put("O", 6);
        expected.add(command3);

        Map<String,Integer> command4 = new HashMap<>();
        command4.put("R", 0);
        command4.put("O", 1);
        expected.add(command4);

        Map<String, Integer> command5 = new HashMap<>();
        command5.put("J", 1);
        command5.put("O", 7);
        expected.add(command5);

        Map<String, Integer> command6 = new HashMap<>();
        command6.put("R", 0);
        command6.put("O", 2);
        expected.add(command6);

        assertEquals(expected,generator.getGeneratedAssembly());
    }

    @Test
    public void testVariableRead(){
        Ast ast = new Ast();
        Variable i = new Variable("i", new Int());
        Variable j = new Variable("j", new Int());
        Function function = new Function("main", new Void(), Arrays.asList(i, j));
        Statement assignI = new Assignment(i, new IntLiteral(2));
        function.addStatement(assignI);
        Statement assignJ = new Assignment(j, new VariableAccess(i));
        function.addStatement(assignJ);
        ast.addFunction(function);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String, Integer>> expected = new LinkedList<>();
        Map<String, Integer> command1 = new HashMap<>();
        command1.put("R", 0);
        command1.put("O", 2);
        expected.add(command1);

        Map<String, Integer> command2 = new HashMap<>();
        command2.put("R", 1);
        command2.put("A", 0);
        command2.put("steel chest", 1);
        expected.add(command2);

        assertEquals(expected, generator.getGeneratedAssembly());
    }

    @Test
    public void testNegation(){
        Ast ast = new Ast();
        List<Variable> scope = new LinkedList<>();
        Variable i = new Variable("i", new Int());
        scope.add(i);
        Function f = new Function("main", new Void(), scope);
        f.addStatement(new Assignment(i, new NegationExpression(new IntLiteral(2))));
        ast.addFunction(f);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String,Integer>> expected = new LinkedList<>();
        Map<String, Integer> command1 = new HashMap<>();
        command1.put("R", 1);
        command1.put("O", 2);
        expected.add(command1);

        Map<String, Integer> command2 = new HashMap<>();
        command2.put("R", 0);
        command2.put("1", 0);
        command2.put("B", 1);
        command2.put("tank", 1);
        expected.add(command2);

        assertEquals(expected,generator.getGeneratedAssembly());
    }

    @Test
    public void testAddition(){
        Ast ast = new Ast();
        Variable i = new Variable("i", new Int());
        Function function = new Function("main", new Void(), Arrays.asList(i));
        Statement assignI = new Assignment(i, new PlusExpression(new IntLiteral(1),new IntLiteral(1)));
        function.addStatement(assignI);
        ast.addFunction(function);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String, Integer>> expected = new LinkedList<>();
        Map<String, Integer> command1 = new HashMap<>();
        command1.put("R", 1);
        command1.put("O", 1);
        expected.add(command1);

        Map<String,Integer> command2 = new HashMap<>();
        command2.put("R", 2);
        command2.put("O", 1);
        expected.add(command2);

        Map<String, Integer> command3 = new HashMap<>();
        command3.put("R", 0);
        command3.put("A", 1);
        command3.put("B", 2);
        command3.put("steel chest", 1);
        expected.add(command3);

        assertEquals(expected, generator.getGeneratedAssembly());
    }

    @Test
    public void testBooleanFalse(){
        Ast ast = new Ast();
        List<Variable> scope = new LinkedList<>();
        Variable b = new Variable("b", new Bool());
        scope.add(b);
        Function f = new Function("main", new Void(), scope);
        f.addStatement(new Assignment(b, new BoolLiteral(false)));
        ast.addFunction(f);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String,Integer>> expected = new LinkedList<>();
        Map<String, Integer> command1 = new HashMap<>();
        command1.put("R", 0);
        command1.put("O", 0);
        expected.add(command1);

        assertEquals(expected,generator.getGeneratedAssembly());
    }

    @Test
    public void testBooleanTrue(){
        Ast ast = new Ast();
        List<Variable> scope = new LinkedList<>();
        Variable b = new Variable("b", new Bool());
        scope.add(b);
        Function f = new Function("main", new Void(), scope);
        f.addStatement(new Assignment(b, new BoolLiteral(true)));
        ast.addFunction(f);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String,Integer>> expected = new LinkedList<>();
        Map<String, Integer> command1 = new HashMap<>();
        command1.put("R", 0);
        command1.put("O", 1);
        expected.add(command1);

        assertEquals(expected,generator.getGeneratedAssembly());
    }

    @Test
    public void testBooleanAnd(){
        Ast ast = new Ast();
        List<Variable> scope = new LinkedList<>();
        Variable b = new Variable("b", new Bool());
        scope.add(b);
        Function f = new Function("main", new Void(), scope);
        f.addStatement(new Assignment(b, new AndExpression(new BoolLiteral(false), new BoolLiteral(true))));
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
        command2.put("O", 1);
        expected.add(command2);

        Map<String, Integer> command3 = new HashMap<>();
        command3.put("R", 0);
        command3.put("A", 1);
        command3.put("B", 2);
        command3.put("wooden chest", 1);
        expected.add(command3);

        assertEquals(expected,generator.getGeneratedAssembly());
    }

    @Test
    public void testBooleanOr(){
        Ast ast = new Ast();
        List<Variable> scope = new LinkedList<>();
        Variable b = new Variable("b", new Bool());
        scope.add(b);
        Function f = new Function("main", new Void(), scope);
        f.addStatement(new Assignment(b, new OrExpression(new BoolLiteral(false), new BoolLiteral(true))));
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
        command2.put("O", 1);
        expected.add(command2);

        Map<String, Integer> command3 = new HashMap<>();
        command3.put("R", 0);
        command3.put("A", 1);
        command3.put("B", 2);
        command3.put("steel chest", 1);
        expected.add(command3);

        assertEquals(expected,generator.getGeneratedAssembly());
    }

    @Test
    public void testBooleanNot(){
        Ast ast = new Ast();
        List<Variable> scope = new LinkedList<>();
        Variable b = new Variable("b", new Bool());
        scope.add(b);
        Function f = new Function("main", new Void(), scope);
        f.addStatement(new Assignment(b, new NotExpression(new BoolLiteral(false))));
        ast.addFunction(f);

        FacAssemblyGenerator generator = new FacAssemblyGenerator(0);
        ast.generateAssembly(generator);

        List<Map<String,Integer>> expected = new LinkedList<>();
        Map<String, Integer> command1 = new HashMap<>();
        command1.put("R", 1);
        command1.put("O", 0);
        expected.add(command1);

        Map<String, Integer> command2 = new HashMap<>();
        command2.put("R", 0);
        command2.put("A", 1);
        command2.put("2", 0);
        command2.put("fast inserter", 1);
        expected.add(command2);

        assertEquals(expected,generator.getGeneratedAssembly());
    }
}

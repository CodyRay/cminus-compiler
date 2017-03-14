package io.github.haroldhues;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import io.github.haroldhues.SyntaxTree.*;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class ParserTest {
	@Test
	public void testBasicProgram() throws CompileErrorException {
		StringSource testSource = new StringSource("void main( void ) { }");

		RootNode expected = new RootNode(new ArrayList<DeclarationNode>(
			Arrays.asList(new DeclarationNode(new TypeSpecifierNode(TypeSpecifierNode.Type.Void), "main", new ArrayList<ParameterDeclarationNode>(), new CompoundStatementNode(
				new ArrayList<DeclarationNode>(), 
				new ArrayList<StatementNode>()
			)))
		));
		
		Scanner testScanner = new Scanner(testSource);
		Parser parser = new Parser(testScanner);
		RootNode ast = parser.parse((e) -> {});

		assertThat(ast, is(expected));
	}
	
	@Test
	public void testFactor() throws CompileErrorException {
		StringSource testSource = new StringSource("( array[1] + fun(1, x, y) )");
		Scanner testScanner = new Scanner(testSource);
		Parser parser = new Parser(testScanner);
		ExpressionNode factor = ExpressionNode.parseFactorNode(parser, (e) -> {});
		
		ExpressionNode expectedFactor = new NestedExpressionNode(new BinaryExpressionNode(
			new VariableExpressionNode("array", new LiteralExpressionNode(1)), 
			new Token(TokenType.Add), 
			new CallExpressionNode("fun", new ArrayList<ExpressionNode>(Arrays.asList(
				new LiteralExpressionNode(1),
				new VariableExpressionNode("x"),
				new VariableExpressionNode("y")
			)))
		));
		
		assertThat(factor, is(expectedFactor));
	}
	
	@Test
	public void testSampleProgram() throws CompileErrorException {
		StringSource testSource = new StringSource(sampleProgram);
		Scanner testScanner = new Scanner(testSource);
		Parser parser = new Parser(testScanner);
		RootNode ast = parser.parse((e) -> {});
		String text = ast.toString();
		StringSource testSource2 = new StringSource(text);
		Scanner testScanner2 = new Scanner(testSource2);
		Parser parser2 = new Parser(testScanner2);
		RootNode ast2 = parser2.parse((e) -> {});
		assertThat(ast, is(ast2));
	}
	
	@Test
	public void testSampleProgram2() throws CompileErrorException {
		StringSource testSource = new StringSource(sampleProgram2);
		Scanner testScanner = new Scanner(testSource);
		Parser parser = new Parser(testScanner);
		RootNode ast = parser.parse((e) -> {});
		String text = ast.toString();
		StringSource testSource2 = new StringSource(text);
		Scanner testScanner2 = new Scanner(testSource2);
		Parser parser2 = new Parser(testScanner2);
		RootNode ast2 = parser2.parse((e) -> {});
		assertThat(ast, is(ast2));
	}
	
	@Test
	public void testEquality() throws CompileErrorException {
		TypeSpecifierNode type1 = new TypeSpecifierNode(TypeSpecifierNode.Type.Void);

		TypeSpecifierNode type2 = new TypeSpecifierNode(TypeSpecifierNode.Type.Void);

		assertThat(type1, is(type2));
			
		DeclarationNode declaration1 = new DeclarationNode(new TypeSpecifierNode(TypeSpecifierNode.Type.Void), "main", new ArrayList<ParameterDeclarationNode>(), new CompoundStatementNode(
			new ArrayList<DeclarationNode>(), 
			new ArrayList<StatementNode>()
		));

		DeclarationNode declaration2 = new DeclarationNode(new TypeSpecifierNode(TypeSpecifierNode.Type.Void), "main", new ArrayList<ParameterDeclarationNode>(), new CompoundStatementNode(
				new ArrayList<DeclarationNode>(), 
				new ArrayList<StatementNode>()
			));

		assertThat(declaration1, is(declaration2));
		
		RootNode expected1 = new RootNode(new ArrayList<DeclarationNode>(
			Arrays.asList(new DeclarationNode(new TypeSpecifierNode(TypeSpecifierNode.Type.Void), "main", new ArrayList<ParameterDeclarationNode>(), new CompoundStatementNode(
				new ArrayList<DeclarationNode>(), 
				new ArrayList<StatementNode>()
			)))
		));

		RootNode expected2 = new RootNode(new ArrayList<DeclarationNode>(
			Arrays.asList(new DeclarationNode(new TypeSpecifierNode(TypeSpecifierNode.Type.Void), "main", new ArrayList<ParameterDeclarationNode>(), new CompoundStatementNode(
				new ArrayList<DeclarationNode>(), 
				new ArrayList<StatementNode>()
			)))
		));

		assertThat(expected1, is(expected2));
	}
	
	@Test
	public void testCallExpression() throws CompileErrorException {
		StringSource testSource = new StringSource("myFunction(expr, 1, 2, nestedCall(), (1+2), 5 + 2)");
		Scanner testScanner = new Scanner(testSource);
		Parser parser = new Parser(testScanner);
		
		ExpressionNode node = ExpressionNode.parse(parser, (n) -> {});
		
		ExpressionNode expected = new CallExpressionNode("myFunction", new ArrayList<ExpressionNode>(Arrays.asList(
			new VariableExpressionNode("expr"),
			new LiteralExpressionNode(1),
			new LiteralExpressionNode(2),
			new CallExpressionNode("nestedCall", new ArrayList<ExpressionNode>()),
			new NestedExpressionNode(new BinaryExpressionNode(new LiteralExpressionNode(1), new Token(TokenType.Add), new LiteralExpressionNode(2))),
			new BinaryExpressionNode(new LiteralExpressionNode(5), new Token(TokenType.Add), new LiteralExpressionNode(2))
		)));
		
		assertThat(node, is(expected));
	}
	
	public static String sampleProgram = "int x; int y;\r\n" + 
			"int z[10];\r\n" + 
			"\r\n" + 
			"void f( void ) {\r\n" + 
			"    int i;\r\n" + 
			"    i = 0;\r\n" + 
			"    while( i < 10 ) {\r\n" + 
			"        z[ i ] = 100 + 2 * i;\r\n" + 
			"        i = i + 1;\r\n" + 
			"    }\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"void g( void ) {\r\n" + 
			"    int i;\r\n" + 
			"    i = 0;\r\n" + 
			"    while( i < 10 ) {\r\n" + 
			"        write z[ i ];\r\n" + 
			"        i = i + 1;\r\n" + 
			"    }\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"void main( void ) {\r\n" + 
			"    f();\r\n" + 
			"    g();\r\n" + 
			"}";
	public static String sampleProgram2 = "int x;\r\n" + 
			"\r\n" + 
			"void a( void ) {\r\n" + 
			"    int a;\r\n" + 
			"    int b;\r\n" + 
			"    int c;\r\n" + 
			"    int d[10];\r\n" + 
			"    int e[20];\r\n" + 
			"\r\n" + 
			"    c = 8;\r\n" + 
			"    {\r\n" + 
			"        int nested;\r\n" + 
			"        \r\n" + 
			"        read nested;\r\n" + 
			"\r\n" + 
			"        if (nested >= 3) {\r\n" + 
			"            return;\r\n" + 
			"        }\r\n" + 
			"        else {\r\n" + 
			"            read b;\r\n" + 
			"        }\r\n" + 
			"    }\r\n" + 
			"\r\n" + 
			"    read a;\r\n" + 
			"\r\n" + 
			"    while(a == 0) {\r\n" + 
			"        write a;\r\n" + 
			"        read(a);\r\n" + 
			"        read d[1 + 1];\r\n" + 
			"    }\r\n" + 
			"\r\n" + 
			"    if(a != 2) {\r\n" + 
			"        a = b = c;\r\n" + 
			"    }\r\n" + 
			"\r\n" + 
			"    if ((((b(d, e) + 20) - 7) / 3) + 3 == 10) {\r\n" + 
			"        return;\r\n" + 
			"    }\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"int y[10];\r\n" + 
			"\r\n" + 
			"int b( int arga[], int argb[] ) {\r\n" + 
			"    return 10;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"void main( void ) {\r\n" + 
			"    a();\r\n" + 
			"}";
}

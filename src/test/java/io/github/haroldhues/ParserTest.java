package io.github.haroldhues;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import io.github.haroldhues.Tokens.*;
import io.github.haroldhues.SyntaxTree.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class ParserTest {
	@Test
	public void testBasicProgram() throws Exception {
		StringSource testSource = new StringSource("void main( void ) { }");

		ProgramSyntaxNode expected = new ProgramSyntaxNode(new ArrayList<DeclarationSyntaxNode>(
			Arrays.asList(new DeclarationSyntaxNode(new TypeSpecifierNode(TypeSpecifierNode.Type.Void), "main", new ParamsNode(new ArrayList<ParamNode>()), new CompoundStatementNode(
				new ArrayList<DeclarationSyntaxNode>(), 
				new ArrayList<StatementNode>()
			)))
		));
		
		Scanner testScanner = new Scanner(testSource);
		Parser parser = new Parser(testScanner);
		ProgramSyntaxNode ast = parser.parse((e) -> {});

		assertThat(ast, is(expected));
	}
	
	@Test
	public void testSampleProgram() throws Exception {
		StringSource testSource = new StringSource(sampleProgram);
		Scanner testScanner = new Scanner(testSource);
		Parser parser = new Parser(testScanner);
		ProgramSyntaxNode ast = parser.parse((e) -> {});
		String text = ast.toString();
		StringSource testSource2 = new StringSource(text);
		Scanner testScanner2 = new Scanner(testSource2);
		Parser parser2 = new Parser(testScanner2);
		ProgramSyntaxNode ast2 = parser2.parse((e) -> {});
		assertThat(ast, is(ast2));
	}
	
	@Test
	public void testEquality() throws Exception {
		TypeSpecifierNode type1 = new TypeSpecifierNode(TypeSpecifierNode.Type.Void);

		TypeSpecifierNode type2 = new TypeSpecifierNode(TypeSpecifierNode.Type.Void);

		assertThat(type1, is(type2));
			
		DeclarationSyntaxNode declaration1 = new DeclarationSyntaxNode(new TypeSpecifierNode(TypeSpecifierNode.Type.Void), "main", new ParamsNode(new ArrayList<ParamNode>()), new CompoundStatementNode(
			new ArrayList<DeclarationSyntaxNode>(), 
			new ArrayList<StatementNode>()
		));

		DeclarationSyntaxNode declaration2 = new DeclarationSyntaxNode(new TypeSpecifierNode(TypeSpecifierNode.Type.Void), "main", new ParamsNode(new ArrayList<ParamNode>()), new CompoundStatementNode(
				new ArrayList<DeclarationSyntaxNode>(), 
				new ArrayList<StatementNode>()
			));

		assertThat(declaration1, is(declaration2));
		
		ProgramSyntaxNode expected1 = new ProgramSyntaxNode(new ArrayList<DeclarationSyntaxNode>(
			Arrays.asList(new DeclarationSyntaxNode(new TypeSpecifierNode(TypeSpecifierNode.Type.Void), "main", new ParamsNode(new ArrayList<ParamNode>()), new CompoundStatementNode(
				new ArrayList<DeclarationSyntaxNode>(), 
				new ArrayList<StatementNode>()
			)))
		));

		ProgramSyntaxNode expected2 = new ProgramSyntaxNode(new ArrayList<DeclarationSyntaxNode>(
			Arrays.asList(new DeclarationSyntaxNode(new TypeSpecifierNode(TypeSpecifierNode.Type.Void), "main", new ParamsNode(new ArrayList<ParamNode>()), new CompoundStatementNode(
				new ArrayList<DeclarationSyntaxNode>(), 
				new ArrayList<StatementNode>()
			)))
		));

		assertThat(expected1, is(expected2));
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
}

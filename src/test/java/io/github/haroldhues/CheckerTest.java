package io.github.haroldhues;

import org.junit.Test;

import io.github.haroldhues.SyntaxTree.*;
import static org.junit.Assert.*;

public class CheckerTest {
	
	@Test
	public void testSampleProgram() throws CompileErrorException {
		StringSource testSource = new StringSource(sampleProgram);
		Scanner testScanner = new Scanner(testSource);
		Parser parser = new Parser(testScanner);
		RootNode ast = parser.parse();
        Checker checker = new Checker();
        ast.visit(checker);
        assertNotNull(ast.symbolTable);
	}
	
	@Test
	public void testSampleProgram2() throws CompileErrorException {
		StringSource testSource = new StringSource(sampleProgram2);
		Scanner testScanner = new Scanner(testSource);
		Parser parser = new Parser(testScanner);
		RootNode ast = parser.parse();
        Checker checker = new Checker();
        ast.visit(checker);
        assertNotNull(ast.symbolTable);
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

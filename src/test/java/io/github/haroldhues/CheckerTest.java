package io.github.haroldhues;

import org.junit.Test;

import io.github.haroldhues.SyntaxTree.*;
import static org.junit.Assert.*;

public class CheckerTest {
	
	public RootNode testASampleProgram(String program) throws CompileErrorException {
		StringSource testSource = new StringSource(program);
		Scanner testScanner = new Scanner(testSource);
		Parser parser = new Parser(testScanner);
		RootNode ast = parser.parse();
        Checker checker = new Checker(ast);
		checker.check();
        assertNotNull(ast.symbolTable);
		return ast;
	}

	@Test
	public void testPrintSymbolRecordTable() throws CompileErrorException {
		RootNode ast = testASampleProgram(sampleProgram5);
		String allSymbols = ast.symbolTable.getRecordedSymbols();
		assertEquals(
		    "{ identifier: f, type: Function, location: 1:1, level: 0, rename: a, returnType: Void, parameters: [{ identifier: a, isArray: true }, { identifier: b, isArray: true }, { identifier: count, isArray: false }] }\n" + 
			"{ identifier: a, type: ArrayVariable, location: 1:9, level: 1, rename: b, size: null }\n" + 
			"{ identifier: b, type: ArrayVariable, location: 1:18, level: 1, rename: c, size: null }\n" + 
			"{ identifier: count, type: Variable, location: 1:27, level: 1, rename: d }\n" + 
			"{ identifier: i, type: Variable, location: 2:5, level: 1, rename: e }\n" + 
			"{ identifier: main, type: Function, location: 17:1, level: 0, rename: f, returnType: Void, parameters: [] }\n" + 
			"{ identifier: b, type: ArrayVariable, location: 18:5, level: 1, rename: g, size: 5 }\n" + 
			"{ identifier: c, type: ArrayVariable, location: 18:15, level: 1, rename: h, size: 5 }\n" + 
			"{ identifier: i, type: Variable, location: 19:5, level: 1, rename: i }\n", allSymbols);
	}
	
	@Test
	public void testSampleProgram() throws CompileErrorException {
		testASampleProgram(sampleProgram);
	}

	@Test
	public void testSampleProgram2() throws CompileErrorException {
		testASampleProgram(sampleProgram2);
	}

	@Test
	public void testSampleProgram3() throws CompileErrorException {
		testASampleProgram(sampleProgram3);
	}

	@Test
	public void testSampleProgram4() throws CompileErrorException {
		testASampleProgram(sampleProgram4);
	}

	@Test
	public void testSampleProgram5() throws CompileErrorException {
		testASampleProgram(sampleProgram5);
	}

	@Test
	public void testSampleProgram6() throws CompileErrorException {
		testASampleProgram(sampleProgram6);
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
	public static String sampleProgram2 = "\r\n" + 
			"int x[5];\r\n" + 
			"int y[15];\r\n" + 
			"int z[10];\r\n" + 
			"\r\n" + 
			"void f( int a[], int b[], int c[] ) {\r\n" + 
			"\r\n" + 
			"    a[ 2 ] = 111;\r\n" + 
			"    b[ 9 ] = 222;\r\n" + 
			"    c[ 4 ] = 333;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"void main( void ) {\r\n" + 
			"    f(x, y, z);\r\n" + 
			"    write x[ 2 ];\r\n" + 
			"    write y[ 9 ];\r\n" + 
			"    write z[ 4 ];\r\n" + 
			"\r\n" + 
			"}";
	public static String sampleProgram3 = "void main( void ) {\r\n" + 
			"    int x;\r\n" + 
			"    int y;\r\n" + 
			"    int z;\r\n" + 
			"\r\n" + 
			"    read x;\r\n" + 
			"    read y;\r\n" + 
			"    read z;\r\n" + 
			"\r\n" + 
			"    if( x < y ) {\r\n" + 
			"        if ( x < z ) {\r\n" + 
			"            if ( y < z ) {\r\n" + 
			"                write x;\r\n" + 
			"                write y;\r\n" + 
			"                write z;\r\n" + 
			"            }\r\n" + 
			"            else {\r\n" + 
			"                write x;\r\n" + 
			"                write z;\r\n" + 
			"                write y;\r\n" + 
			"            }\r\n" + 
			"        }\r\n" + 
			"        else {\r\n" + 
			"            write z;\r\n" + 
			"            write x;\r\n" + 
			"            write y;\r\n" + 
			"        }\r\n" + 
			"    }\r\n" + 
			"    else {\r\n" + 
			"        if( x < z ) {\r\n" + 
			"            write y;\r\n" + 
			"            write x;\r\n" + 
			"            write z;\r\n" + 
			"        }\r\n" + 
			"        else {\r\n" + 
			"            if( y < z ) {\r\n" + 
			"                write y;\r\n" + 
			"                write z;\r\n" + 
			"                write x;\r\n" + 
			"            }\r\n" + 
			"            else {\r\n" + 
			"                write z;\r\n" + 
			"                write y;\r\n" + 
			"                write x;\r\n" + 
			"            }\r\n" + 
			"        }\r\n" + 
			"    }\r\n" + 
			"}";
	public static String sampleProgram4 = "void main( void ) {\r\n" + 
			"    int x;\r\n" + 
			"    int y;\r\n" + 
			"\r\n" + 
			"    read x;\r\n" + 
			"\r\n" + 
			"    while( x != 1 ) {\r\n" + 
			"        write x;\r\n" + 
			"        y = x / 2;\r\n" + 
			"\r\n" + 
			"        if( 2 * y == x ) \r\n" + 
			"            x = x /2;\r\n" + 
			"\r\n" + 
			"        else\r\n" + 
			"            x = 3 * x + 1;\r\n" + 
			"    }\r\n" + 
			"}";
	public static String sampleProgram5 = "void f( int a[], int b[], int count ) {\r\n" + 
			"    int i;\r\n" + 
			"    i = 0;\r\n" + 
			"\r\n" + 
			"    if( count < 4 ) {\r\n" + 
			"        count = count + 1;\r\n" + 
			"        f( a, b, count );\r\n" + 
			"    }\r\n" + 
			"    else {\r\n" + 
			"        while( i < 5 ) {\r\n" + 
			"            write( a[ i ] + b[ i ] );\r\n" + 
			"            i = i + 1;\r\n" + 
			"        }\r\n" + 
			"    }\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"void main( void ) {\r\n" + 
			"    int b[5]; int c[5];\r\n" + 
			"    int i;\r\n" + 
			"\r\n" + 
			"    i = 0;\r\n" + 
			"    while( i < 5 ) {\r\n" + 
			"        b[ i ] = 7 + i * 10;\r\n" + 
			"        c[ i ] = i + 2;\r\n" + 
			"        i = i + 1;\r\n" + 
			"    }\r\n" + 
			"\r\n" + 
			"    f( b, c, 0 );\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"}";
	public static String sampleProgram6 = "\r\n" + 
			"int f( int a, int b, int c, int d, int e) {\r\n" + 
			"    \r\n" + 
			"    write a;\r\n" + 
			"    write b;\r\n" + 
			"    write c;\r\n" + 
			"    write d;\r\n" + 
			"    write e;\r\n" + 
			"\r\n" + 
			"    return (a + b + c + d + e);\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"void main( void ) {\r\n" + 
			"    \r\n" + 
			"\r\n" + 
			"    write f( 1, 2, 3, 4, 5);\r\n" + 
			"}";
}

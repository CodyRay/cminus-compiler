package io.github.haroldhues;

import org.junit.Test;

import io.github.haroldhues.SyntaxTree.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;

public class InterpreterTest {
	
	public RootNode compileProgram(String program) throws CompileErrorException {
		StringSource testSource = new StringSource(program);
		Scanner testScanner = new Scanner(testSource);
		Parser parser = new Parser(testScanner);
		RootNode ast = parser.parse();
		Checker.check(ast);
        assertNotNull(ast.symbolTable);
		return ast;
	}
	
	public void testProgram(String program, List<Integer> input, List<Integer> output) throws CompileErrorException {
		RootNode rootNode = compileProgram(program);
		TestInputOutput io = new TestInputOutput(input);
		Interpreter.run(rootNode, io);
		assertThat(io.getOutput(), is(output));
		assertTrue(io.outputConsumed());
	}
	
	@Test
	public void testProgram1() throws CompileErrorException {
		List<Integer> input = Arrays.asList();
		List<Integer> output = Arrays.asList(
			100,
			102,
			104,
			106,
			108,
			110,
			112,
			114,
			116,
			118
		);
		testProgram(sampleProgram, input, output);
	}
	
	@Test
	public void testProgram2() throws CompileErrorException {
		List<Integer> input = Arrays.asList();
		List<Integer> output = Arrays.asList(
			111,
			222,
			333
		);
		testProgram(sampleProgram2, input, output);
	}
	
	@Test
	public void testProgram3() throws CompileErrorException {
		List<Integer> input = Arrays.asList(
			200,
			100,
			300
		);
		List<Integer> output = Arrays.asList(
			100,
			200,
			300
		);
		testProgram(sampleProgram3, input, output);
	}
	
	@Test
	public void testProgram4() throws CompileErrorException {
		List<Integer> input = Arrays.asList(
			20
		);
		List<Integer> output = Arrays.asList(
			20,
			10,
			5,
			16,
			8,
			4,
			2
		);
		testProgram(sampleProgram4, input, output);
	}
	
	@Test
	public void testProgram5() throws CompileErrorException {
		List<Integer> input = Arrays.asList();
		List<Integer> output = Arrays.asList(
			9,
			20,
			31,
			42,
			53
		);
		testProgram(sampleProgram5, input, output);
	}
	
	@Test
	public void testProgram6() throws CompileErrorException {
		List<Integer> input = Arrays.asList();
		List<Integer> output = Arrays.asList(
			1,
			2,
			3,
			4,
			5,
			15
		);
		testProgram(sampleProgram6, input, output);
	}
	
	public class TestInputOutput implements InputOutput {
		private List<Integer> input;
		private List<Integer> output = new ArrayList<Integer>();
		
		public TestInputOutput(List<Integer> input) {
			this.input = new LinkedList<Integer>(input);
		}
		
		public int read() {
			if(input.size() > 0) {
				int out = input.get(0);
				input.remove(0);
				return out;
			} else {
				fail("Ran out of input");
				throw new UnsupportedOperationException();
			}
		}

		public void write(int out) {
			output.add(out);
		}
		
		public List<Integer> getOutput() {
			return output;
		}
		
		public boolean outputConsumed() {
			return input.size() == 0;
		}
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

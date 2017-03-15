package io.github.haroldhues;

import org.junit.Test;

import io.github.haroldhues.SyntaxTree.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

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
	
	public void testError(String... strs) {
		String program = strs[strs.length - 1];
		try {
			testASampleProgram(program);
			fail();
		} catch (CompileErrorException ex) {
			for(int x = 0; x < strs.length - 1; x++) {
				assertThat(ex.getMessage(), containsString(strs[x]));				
			}
		}
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
	
	@Test
	public void testFunctionWithinFunctionError() {
		testError("Functions cannot be declared inside of functions",
				"void main( void ) {\r\n" + 
				"    int y;\r\n" + 
				"    int x( int z ) {\r\n" + 
				"        return z;\r\n" + 
				"    }\r\n" + 
				"    y = x(1);\r\n" + 
				"}");
	}
	
	@Test
	public void testMainDeclarationError() {
		String errorMessage = "The last declaration in a program must be a function declaration";
		testError(errorMessage,
				"int main( void ) {\r\n" + 
				"    return 1;\r\n" + 
				"}");
		testError(errorMessage,
				"void main( int x ) {\r\n" + 
				"    return;\r\n" + 
				"}");
		testError(errorMessage,
				"void do( void ) {\r\n" + 
				"    return;\r\n" + 
				"}");
		testError(errorMessage,
				"");
		testError(errorMessage,
				"void main( void ) {\r\n" + 
				"    return;\r\n" + 
				"}\r\n" + 
				"void do( void ) {\r\n" + 
				"    return;\r\n" + 
				"}");
	}
	
	@Test
	public void testVoidVariableError() {
		String errorMessage = "cannot be declared with a void type";
		testError(errorMessage,
				"void main( void ) {\r\n" + 
				"    void variable;\r\n" + 
				"}");
		testError(errorMessage,
				"void variable;\r\n" + 
				"\r\n" + 
				"void main( void ) {\r\n" + 
				"    return;\r\n" + 
				"}");
		testError(errorMessage,
				"void main( void ) {\r\n" + 
				"    void variable[10];\r\n" + 
				"}");
		testError(errorMessage,
				"void variable[10];\r\n" + 
				"\r\n" + 
				"void main( void ) {\r\n" + 
				"    return;\r\n" + 
				"}");
	}
	
	@Test
	public void testArraySizeZeroError() {
		String errorMessage = "array size cannot be zero";
		testError(errorMessage,
				"void main( void ) {\r\n" + 
				"    int variable[0];\r\n" + 
				"}");
		testError(errorMessage,
				"int variable[0];\r\n" + 
				"void main( void ) {\r\n" + 
				"    return;\r\n" + 
				"}");
	}
	
	@Test
	public void testAllPathsMustReturnError() throws CompileErrorException {
		testASampleProgram( // Test that it doesn't throw error for this case
				"int f( void ) {\r\n" + 
				"    if(1) {\r\n" + 
				"        return 1;\r\n" + 
				"    } else {\r\n" + 
				"        return 0;\r\n" + 
				"    }\r\n" + 
				"}\r\n" + 
				"void main( void ) { }");
		String errorMessage1 = "All paths";
		String errorMessage2 = "must return an integer";
		testError(errorMessage1, errorMessage2,
				"int f( void ) {\r\n" + 
				"}\r\n" + 
				"void main( void ) { }");
		testError(errorMessage1, errorMessage2,
				"int f( void ) {\r\n" + 
				"    if(1) {\r\n" + 
				"        return 1;\r\n" + 
				"    }\r\n" + 
				"}\r\n" + 
				"void main( void ) { }");
	}
	
	@Test
	public void testWriteIntegersOnlyError() throws CompileErrorException {
		testASampleProgram( // Test that it doesn't throw error for this case
				"int x;\r\n" + 
				"void main( void ) { \r\n" + 
				"    write x;\r\n" + 
				"}");
		String errorMessage = "Only integers can be used with 'write'";
		testError(errorMessage,
				"void f ( void ) { }\r\n" + 
				"void main( void ) { \r\n" + 
				"    write f();\r\n" + 
				"}");
		testError(errorMessage,
				"int x[10];\r\n" + 
				"void main( void ) { \r\n" + 
				"    write x;\r\n" + 
				"}");
	}
	
	@Test
	public void testReadIntegerAssignmentOnlyError() throws CompileErrorException {
		testASampleProgram( // Test that it doesn't throw error for this case
				"int x;\r\n" + 
				"void main( void ) { \r\n" + 
				"    read x;\r\n" + 
				"}");
		testASampleProgram( // Test that it doesn't throw error for this case
				"int x;\r\n" + 
				"void main( void ) { \r\n" + 
				"    read(x);\r\n" + 
				"}");
		testASampleProgram( // Test that it doesn't throw error for this case
				"int x[10];\r\n" + 
				"void main( void ) { \r\n" + 
				"    read(x[2]);\r\n" + 
				"}");
		String errorMessage = "The variable for a read statement must accept assignment of an integer";
		testError(errorMessage,
				"int x[10];\r\n" + 
				"void main( void ) { \r\n" + 
				"    read x;\r\n" + 
				"}");
	}
	
	@Test
	public void testAllReturnStatementsMustReturnValueError() throws CompileErrorException {
		testASampleProgram( // Test that it doesn't throw error for this case
				"int f( void ) {\r\n" + 
				"    if(1) {\r\n" + 
				"        return 1;\r\n" + 
				"    } else {\r\n" + 
				"        return 0;\r\n" + 
				"    }\r\n" + 
				"}\r\n" + 
				"void main( void ) { }");
		String errorMessage1 = "All return statements";
		String errorMessage2 = "must return integers";
		testError(errorMessage1, "myFunction", errorMessage2,
				"int myFunction( void ) {\r\n" + 
				"    return;\r\n" + 
				"}\r\n" + 
				"void main( void ) { }");
		testError(errorMessage1, "myFunction", errorMessage2,
				"int myFunction( void ) {\r\n" + 
				"    if(1) {\r\n" + 
				"        return 1;\r\n" + 
				"    } else {\r\n" + 
				"        return;\r\n" + 
				"    }\r\n" + 
				"}\r\n" + 
				"void main( void ) { }");
	}
	
	@Test
	public void testReturnStatementsCannotReturnValueError() throws CompileErrorException {
		testASampleProgram( // Test that it doesn't throw error for this case
				"void f( void ) {\r\n" + 
				"    if(1) {\r\n" + 
				"        return;\r\n" + 
				"    } else {\r\n" + 
				"        return;\r\n" + 
				"    }\r\n" + 
				"}\r\n" + 
				"void main( void ) { }");
		String errorMessage1 = "Return statements";
		String errorMessage2 = "cannot return values";
		testError(errorMessage1, "myFunction", errorMessage2,
				"void myFunction( void ) {\r\n" + 
				"    return 2;\r\n" + 
				"}\r\n" + 
				"void main( void ) { }");
		testError(errorMessage1, "myFunction", errorMessage2,
				"void myFunction( void ) {\r\n" + 
				"    if(1) {\r\n" + 
				"        return 1;\r\n" + 
				"    } else {\r\n" + 
				"        return;\r\n" + 
				"    }\r\n" + 
				"}\r\n" + 
				"void main( void ) { }");
	}
	
	@Test
	public void testConditionForWhileLoopMustBeIntegerError() {
		String errorMessage = "The condition for a while loop must be an integer";
		testError(errorMessage,
				"void f( void ) {}\r\n" + 
				"void main( void ) {\r\n" + 
				"    while(f()) {\r\n" + 
				"        return;\r\n" + 
				"    }\r\n" + 
				"}");
		testError(errorMessage,
				"void main( void ) {\r\n" + 
				"    int x[10];\r\n" + 
				"    while(x) {\r\n" + 
				"        return;\r\n" + 
				"    }\r\n" + 
				"}");
	}
	
	@Test
	public void testConditionForIfMustBeIntegerError() {
		String errorMessage = "The condition for a if statement must be an integer";
		testError(errorMessage,
				"void f( void ) {}\r\n" + 
				"void main( void ) {\r\n" + 
				"    if(f()) {\r\n" + 
				"        return;\r\n" + 
				"    }\r\n" + 
				"}");
		testError(errorMessage,
				"void main( void ) {\r\n" + 
				"    int x[10];\r\n" + 
				"    if(x) {\r\n" + 
				"        return;\r\n" + 
				"    }\r\n" + 
				"}");
	}
	
	@Test
	public void testNonIntegerAssignmentError() {
		String errorMessage = "Only integers can be assigned to variables";
		testError(errorMessage,
				"void main( void ) {\r\n" + 
				"    int x[10];\r\n" + 
				"    int y[10];\r\n" + 
				"    x = y;\r\n" + 
				"}");
		testError(errorMessage,
				"void main( void ) {\r\n" + 
				"    int x[10];\r\n" + 
				"    int y[10];\r\n" + 
				"    x[10] = y;\r\n" + 
				"}");
		testError(errorMessage,
				"void y( void ) { }\r\n" + 
				"\r\n" + 
				"void main( void ) {\r\n" + 
				"    int x[10];\r\n" + 
				"    x[10] = y();\r\n" + 
				"}");
	}
	
	@Test
	public void testNonIntegerLeftHandAssignmentError() {
		String errorMessage = "Only simple variables or subscripted arrays variables can be the left hand of an expression";
		testError(errorMessage,
				"void main( void ) {\r\n" + 
				"    int y[10];\r\n" + 
				"    y = 1;\r\n" + 
				"}");
	}
	
	@Test
	public void testCannotUseFunctionAsVariableError() {
		String errorMessage1 = "Cannot use function";
		String errorMessage2 = "as a variable";
		testError(errorMessage1, errorMessage2,
				"void f( void ) { }\r\n" + 
				"void main( void ) {\r\n" + 
				"    (f + 1);\r\n" + 
				"}");
		testError(errorMessage1, errorMessage2,
				"void f( void ) { }\r\n" + 
				"void main( void ) {\r\n" + 
				"    f = 0;\r\n" + 
				"}");
	}
	
	@Test
	public void testCannotUseVariableAsFunctionError() {
		String errorMessage1 = "Cannot invoke the variable";
		String errorMessage2 = "as a function";
		testError(errorMessage1, errorMessage2,
				"void main( void ) {\r\n" + 
				"    int x[10];\r\n" + 
				"    x();\r\n" + 
				"}");
		testError(errorMessage1, errorMessage2,
				"void main( void ) {\r\n" + 
				"    int x;\r\n" + 
				"    x();\r\n" + 
				"}");
	}
	
	@Test
	public void testArrayNotationOnVariableError() {
		String errorMessage = "Array notation (i.e., d[x]) can only be used for array variables";
		testError(errorMessage,
				"void main( void ) {\r\n" + 
				"    int x;\r\n" + 
				"    x[0];\r\n" + 
				"}");
	}
	
	@Test
	public void testArrayNotationVoidError() {
		String errorMessage = "Array variables must be indexed by integers";
		testError(errorMessage,
				"void f( void ) { }\r\n" + 
				"\r\n" + 
				"void main( void ) {\r\n" + 
				"    int x[10];\r\n" + 
				"    x[f()];\r\n" + 
				"}");
	}
	
	@Test
	public void testMissmatchedArgumentCountError() {
		// Function, 'My Function' received 2 arguments, but 0 were expected
		String errorMessage1 = "Function";
		String errorMessage2 = "received";
		String errorMessage3 = "arguments, but";
		String errorMessage4 = "were expected";
		testError(errorMessage1, "myFunction", errorMessage2, "0", errorMessage3, "1", errorMessage4,
				"void myFunction(int z) { }\r\n" + 
				"void main( void ) {\r\n" + 
				"    myFunction();\r\n" + 
				"}");
		testError(errorMessage1, "myFunction", errorMessage2, "2", errorMessage3, "0", errorMessage4,
				"void myFunction( void ) { }\r\n" + 
				"void main( void ) {\r\n" + 
				"    myFunction(1, 2);\r\n" + 
				"}");
	}
	
	@Test
	public void testMissmatchedArgumentTypeError() {
		// Function, 'My Function' received 2 arguments, but 0 were expected
		testError("Argument", "received an expression that evaluated to an integer, but an integer array was expected",
				"void myFunction( int x[], int y ) { }\r\n" + 
				"void main( void ) {\r\n" + 
				"    int z[1];\r\n" + 
				"    myFunction(1, z);\r\n" + 
				"}");
		testError("Argument", "received an expression that evaluated to an integer array, but an integer was expected",
				"void myFunction( int x[], int y ) { }\r\n" + 
				"void main( void ) {\r\n" + 
				"    int z[1];\r\n" + 
				"    myFunction(z, z);\r\n" + 
				"}");
		testError("Argument", "received an expression that evaluated to void",
				"void myFunction( int x[], int y ) { }\r\n" + 
				"void main( void ) {\r\n" + 
				"    int z[1];\r\n" + 
				"    myFunction(z, myFunction(z, 1));\r\n" + 
				"}");
	}
	
	@Test
	public void testIntegerOnBothSidesOfExpressionError() {
		String errorMessage = "Both sides of an add expression must result in integers";
		testError(errorMessage,
				"void foo( void ) { }\r\n" + 
				"\r\n" + 
				"void main( void ) {\r\n" + 
				"    (1 + foo());\r\n" + 
				"}");
		testError(errorMessage,
				"void foo( void ) { }\r\n" + 
				"\r\n" + 
				"void main( void ) {\r\n" + 
				"    (foo() + 1);\r\n" + 
				"}");
	}
	
	@Test
	public void testNotDefinedError() {
		String errorMessage = "'foo' has not been defined";
		testError(errorMessage,
				"void main( void ) {\r\n" + 
				"    foo();\r\n" + 
				"}");
		testError(errorMessage,
				"void main( void ) {\r\n" + 
				"    foo + 1;\r\n" + 
				"}");
	}
	
	@Test
	public void testAlreadyDefinedError() {
		String errorMessage = "'foo' has already been declared in this scope";
		testError(errorMessage,
				"void function(int foo, int y) {\r\n" + 
				"    int foo[10];\r\n" + 
				"}\r\n" + 
				"void main( void ) { }");
		testError(errorMessage,
				"void function(int x, int y) {\r\n" + 
				"    int foo[10];\r\n" + 
				"    int foo;\r\n" + 
				"}\r\n" + 
				"void main( void ) { }");
	}
	
	@Test
	public void testLeftHandOfAssigmentMustBeVariableError() {
		String errorMessage = "The left hand of an assignment must be a variable reference";
		testError(errorMessage,
				"void main( void ) {\r\n" + 
				"    1 = (3 + 1);\r\n" + 
				"}");
		testError(errorMessage,
				"void f( void ) { }\r\n" + 
				"void main( void ) {\r\n" + 
				"    f() = 5;\r\n" + 
				"}");
		testError(errorMessage,
				"void main( void ) {\r\n" + 
				"    (1 + 2) = 4;\r\n" + 
				"}");
		testError(errorMessage,
				"void main( void ) {\r\n" + 
				"    1 + 2 = 4;\r\n" + 
				"}");
	}
	
	// Note: Actually a parse error, but more convenient to test here
	@Test
	public void testExpectedFactorError() {
		String errorMessage = "Expected an integer, '(', or an identifier but instead found '*'";
		testError(errorMessage,
				"void main( void ) {\r\n" + 
				"    (1 <= *);\r\n" + 
				"}");
	}
	
	// Note: Actually a parse error, but more convenient to test here
	@Test
	public void testExpectedTypeError() {
		String errorMessage = "Expected 'int' or 'void' but instead found 'main'";
		testError(errorMessage, "main( void ) { }");
	}
	
	// Note: Actually a parse error, but more convenient to test here
	@Test
	public void testExpectedSemicolonBracketOrParenthesisError() {
		String errorMessage = "Expected ';', '[', or '(' but instead found 'void'";
		testError(errorMessage, 
				"int x\r\n" + 
				"void main( void ) {}");
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

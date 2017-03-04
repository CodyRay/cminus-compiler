package io.github.haroldhues;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import io.github.haroldhues.Tokens.IdentifierToken;
import io.github.haroldhues.Tokens.IntegerLiteralToken;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class ScannerTest {
	@Test
	public void testFunctionDeclaration() throws CompileErrorException {
		StringSource testSource = new StringSource("void nothing( void ) { }");
		Scanner testScanner = new Scanner(testSource);
		List<Token> result = testScanner.toList();
		assertThat(result, is(Arrays.asList(new Token[] {
			new Token(TokenType.Void),
			new IdentifierToken("nothing"),
			new Token(TokenType.LeftParenthesis),
			new Token(TokenType.Void),
			new Token(TokenType.RightParenthesis),
			new Token(TokenType.LeftBrace),
			new Token(TokenType.RightBrace),
			new Token(TokenType.Eof),
		})));
	}

	@Test
	public void testSymbols1() throws CompileErrorException {
		StringSource testSource = new StringSource("( ) { } < <= > !=");
		Scanner testScanner = new Scanner(testSource);
		List<Token> result = testScanner.toList();
		assertThat(result, is(Arrays.asList(new Token[] {
			new Token(TokenType.LeftParenthesis),
			new Token(TokenType.RightParenthesis),
			new Token(TokenType.LeftBrace),
			new Token(TokenType.RightBrace),
			new Token(TokenType.LessThan),
			new Token(TokenType.LessThanOrEqual),
			new Token(TokenType.GreaterThan),
			new Token(TokenType.NotEqual), 
			new Token(TokenType.Eof),
		})));
	}

	@Test
	public void testSymbols2() throws CompileErrorException {
		StringSource testSource = new StringSource("()[]{}+*-/;,==><>=<=!=");
		Scanner testScanner = new Scanner(testSource);
		List<Token> result = testScanner.toList();
		assertThat(result, is(Arrays.asList(new Token[] {
			new Token(TokenType.LeftParenthesis),
			new Token(TokenType.RightParenthesis),
			new Token(TokenType.LeftBracket),
			new Token(TokenType.RightBracket),
			new Token(TokenType.LeftBrace),
			new Token(TokenType.RightBrace),
			new Token(TokenType.Add),
			new Token(TokenType.Multiply),
			new Token(TokenType.Subtract),
			new Token(TokenType.Divide),
			new Token(TokenType.Semicolon), 
			new Token(TokenType.Comma),
			new Token(TokenType.Equal),
			new Token(TokenType.GreaterThan),
			new Token(TokenType.LessThan), 
			new Token(TokenType.GreaterThanOrEqual),
			new Token(TokenType.LessThanOrEqual),
			new Token(TokenType.NotEqual),
			new Token(TokenType.Eof),
		})));
	}

	@Test
	public void testSymbols3() throws CompileErrorException {
		StringSource testSource = new StringSource("-*");
		Scanner testScanner = new Scanner(testSource);
		List<Token> result = testScanner.toList();
		assertThat(result, is(Arrays.asList(new Token[] {
			new Token(TokenType.Subtract),
			new Token(TokenType.Multiply),
			new Token(TokenType.Eof),
		})));
	}
	
	@Test
	public void testLineAndColumn() throws CompileErrorException {
		StringSource testSource = new StringSource("void \r\n nothing \r\n ( \r\n void \r\n ) { }");
		Scanner testScanner = new Scanner(testSource);
		List<Token> result = testScanner.toList();
		Token brace = result.get(result.size() - 2);
		assertThat(brace.getLine(), is(5));
		assertThat(brace.getColumn(), is(6));
	}

	@Test
	public void testSymbolsAndIdentifiers() throws CompileErrorException {
		StringSource testSource = new StringSource("()write[read]void{ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz}+*-/42;myid,==foo>bar<int>=<=!=");
		Scanner testScanner = new Scanner(testSource);
		List<Token> result = testScanner.toList();
		assertThat(result, is(Arrays.asList(new Token[] {
			new Token(TokenType.LeftParenthesis),
			new Token(TokenType.RightParenthesis),
			new Token(TokenType.Write),
			new Token(TokenType.LeftBracket),
			new Token(TokenType.Read),
			new Token(TokenType.RightBracket),
			new Token(TokenType.Void),
			new Token(TokenType.LeftBrace),
			new IdentifierToken("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"),
			new Token(TokenType.RightBrace),
			new Token(TokenType.Add),
			new Token(TokenType.Multiply),
			new Token(TokenType.Subtract),
			new Token(TokenType.Divide),
			new IntegerLiteralToken(42),
			new Token(TokenType.Semicolon), 
			new IdentifierToken("myid"),
			new Token(TokenType.Comma),
			new Token(TokenType.Equal),
			new IdentifierToken("foo"),
			new Token(TokenType.GreaterThan),
			new IdentifierToken("bar"),
			new Token(TokenType.LessThan), 
			new Token(TokenType.Int),
			new Token(TokenType.GreaterThanOrEqual),
			new Token(TokenType.LessThanOrEqual),
			new Token(TokenType.NotEqual),
			new Token(TokenType.Eof),
		})));
	}
	
	@Test
	public void testLongComment() throws CompileErrorException {
		StringSource testSource = new StringSource("()/*write[read]void{firstid}+*-/42;myid,==foo>bar<int>=<=*/!=");
		Scanner testScanner = new Scanner(testSource);
		List<Token> result = testScanner.toList();
		assertThat(result, is(Arrays.asList(new Token[] {
			new Token(TokenType.LeftParenthesis),
			new Token(TokenType.RightParenthesis),
			// Comment Removed
			new Token(TokenType.NotEqual),
			new Token(TokenType.Eof),
		})));
	}
	
	@Test
	public void testLongCommentStars() throws CompileErrorException {
		StringSource testSource = new StringSource("/* / *** / *** / **/!=");
		Scanner testScanner = new Scanner(testSource);
		List<Token> result = testScanner.toList();
		assertThat(result, is(Arrays.asList(new Token[] {
			// Comment Removed
			new Token(TokenType.NotEqual),
			new Token(TokenType.Eof),
		})));
	}
	
	@Test
	public void testInlineComment() throws CompileErrorException {
		StringSource testSource = new StringSource("()///*write[read]void{firstid}+*-/42;myid,==foo>bar<int>=<=*/!=\n()");
		Scanner testScanner = new Scanner(testSource);
		List<Token> result = testScanner.toList();
		assertThat(result, is(Arrays.asList(new Token[] {
			new Token(TokenType.LeftParenthesis),
			new Token(TokenType.RightParenthesis),
			// Comment Removed
			new Token(TokenType.LeftParenthesis),
			new Token(TokenType.RightParenthesis),
			new Token(TokenType.Eof),
		})));
	}
	
	@Test
	public void testUnexpectedInputError() {
		try {
			StringSource testSource = new StringSource("!(invalid)");
			Scanner testScanner = new Scanner(testSource);
			testScanner.toList();
			fail("Exception should be thrown");
		} catch (Exception ex) {
			assertThat(ex.getMessage(), CoreMatchers.containsString("Unexpected character"));
		}
	}
}

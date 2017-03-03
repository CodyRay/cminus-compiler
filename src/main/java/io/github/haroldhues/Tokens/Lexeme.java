package io.github.haroldhues.Tokens;

public class Lexeme {
    public final TokenType type;
    public final String text;
    public Lexeme(TokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    public static final Lexeme[] reservedKeywords = new Lexeme[] {
        new Lexeme(TokenType.Read, "read"),
        new Lexeme(TokenType.Write, "write"),
        new Lexeme(TokenType.If, "if"),
        new Lexeme(TokenType.Else, "else"),
        new Lexeme(TokenType.Int, "int"),
        new Lexeme(TokenType.Return, "return"),
        new Lexeme(TokenType.Void, "void"),
        new Lexeme(TokenType.While, "while"),
    };

    public static final Lexeme[] symbols = new Lexeme[] {
        new Lexeme(TokenType.Add, "+"),
        new Lexeme(TokenType.Subtract, "-"),
        new Lexeme(TokenType.Multiply, "*"),
        new Lexeme(TokenType.Divide, "/"),
        new Lexeme(TokenType.LessThan, "<"),
        new Lexeme(TokenType.LessThanOrEqual, "<="),
        new Lexeme(TokenType.GreaterThan, ">"),
        new Lexeme(TokenType.GreaterThanOrEqual, ">="),
        new Lexeme(TokenType.Equal, "=="),
        new Lexeme(TokenType.NotEqual, "!="),
        new Lexeme(TokenType.Assign, "="),
        new Lexeme(TokenType.Semicolon, ";"),
        new Lexeme(TokenType.Comma, ","),
        new Lexeme(TokenType.LeftParenthesis, "("),
        new Lexeme(TokenType.RightParenthesis, ")"),
        new Lexeme(TokenType.LeftBracket, "["),
        new Lexeme(TokenType.RightBracket, "]"),
        new Lexeme(TokenType.LeftBrace, "{"),
        new Lexeme(TokenType.RightBrace, "}"),
    };
}

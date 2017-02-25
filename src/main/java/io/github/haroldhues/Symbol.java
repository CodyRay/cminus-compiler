package io.github.haroldhues;

public class Symbol {
    public final TokenType type;
    public final String text;
    public Symbol(TokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    public static final Symbol[] symbols = new Symbol[] {
        new Symbol(TokenType.Add, "+"),
        new Symbol(TokenType.Subtract, "-"),
        new Symbol(TokenType.Multiply, "*"),
        new Symbol(TokenType.Divide, "/"),
        new Symbol(TokenType.LessThan, "<"),
        new Symbol(TokenType.LessThanOrEqual, "<="),
        new Symbol(TokenType.GreaterThan, ">"),
        new Symbol(TokenType.GreaterThanOrEqual, ">="),
        new Symbol(TokenType.Equal, "=="),
        new Symbol(TokenType.NotEqual, "!="),
        new Symbol(TokenType.Assign, "="),
        new Symbol(TokenType.Semicolon, ";"),
        new Symbol(TokenType.Comma, ","),
        new Symbol(TokenType.LeftParenthesis, "("),
        new Symbol(TokenType.RightParenthesis, ")"),
        new Symbol(TokenType.LeftBracket, "["),
        new Symbol(TokenType.RightBracket, "]"),
        new Symbol(TokenType.LeftBrace, "{"),
        new Symbol(TokenType.RightBrace, "}"),
    };
}
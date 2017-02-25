package io.github.haroldhues;

public class ReservedKeyword {
    public final TokenType type;
    public final String text;
    public ReservedKeyword(TokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    public static final ReservedKeyword[] reservedKeywords = new ReservedKeyword[] {
        new ReservedKeyword(TokenType.Read, "read"),
        new ReservedKeyword(TokenType.Write, "write"),
        new ReservedKeyword(TokenType.If, "if"),
        new ReservedKeyword(TokenType.Else, "else"),
        new ReservedKeyword(TokenType.Int, "int"),
        new ReservedKeyword(TokenType.Return, "return"),
        new ReservedKeyword(TokenType.Void, "void"),
        new ReservedKeyword(TokenType.While, "while"),
    };
}

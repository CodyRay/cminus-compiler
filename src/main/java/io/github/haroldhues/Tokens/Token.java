package io.github.haroldhues.Tokens;

import java.util.stream.Stream;

import io.github.haroldhues.Location;


import java.util.HashMap;
import java.util.stream.Collectors;


public class Token 
{
    public TokenType type;
    private Location location;

    private static final HashMap<String, TokenType> tokenTypeMap = new HashMap<String, TokenType>();
    private static final HashMap<TokenType, String> lexemeMap = new HashMap<TokenType, String>();
    
    public Token(TokenType type) {
        this.type = type;
    }

    public Token(String text) {
        buildTypeMap();
        this.type = tokenTypeMap.get(text);
    }
    public Token(TokenType type, Location location) {
        this.type = type;
        this.location = location;
    }

    public Token(String text, Location location) {
        buildTypeMap();
        this.type = tokenTypeMap.get(text);
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public int getLength() {
        return toString().length() - 1;
    }

    private static void buildTypeMap() {
        if (tokenTypeMap.size() == 0) {
            for(Lexeme lex: Stream.concat(Stream.of(Lexeme.reservedKeywords), Stream.of(Lexeme.symbols)).collect(Collectors.toList())) {
                tokenTypeMap.put(lex.text, lex.type);
                lexemeMap.put(lex.type, lex.text);
            }
        }
    }

    public boolean isCompareOperator() {
        switch(type) {
            case LessThanOrEqual:
            case LessThan:
            case GreaterThanOrEqual:
            case GreaterThan:
            case Equal:
            case NotEqual:
                return true;
            default:
                return false;
        }
    }

    public boolean isMultiplyOrDivideOperator() {
        switch(type) {
            case Multiply:
            case Divide:
                return true;
            default:
                return false;
        }
    }

    public boolean isAddOrSubtractOperator() {
        switch(type) {
            case Add:
            case Subtract:
                return true;
            default:
                return false;
        }
    }

    public String toString() {
        buildTypeMap();
        return lexemeMap.get(type) + " ";
    }
    
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof Token)) {
            return false;
        }
         
        Token that = (Token) other;
 
        return this.type == that.type;
    }
}
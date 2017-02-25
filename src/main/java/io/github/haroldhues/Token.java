package io.github.haroldhues;

import java.util.stream.Stream;
import java.util.HashMap;
import java.util.stream.Collectors;


public class Token 
{
    public TokenType type;

    private static final HashMap<String, TokenType> tokenTypeMap = new HashMap<String, TokenType>();
    
    public Token(TokenType type) {
        this.type = type;
    }

    public Token(String text) {
        if (tokenTypeMap.size() == 0) {
            tokenTypeMap.putAll(
                Stream
                    .of(ReservedKeyword.reservedKeywords)
                    .collect(Collectors.toMap(keyword -> keyword.text, keyword -> keyword.type)));
            tokenTypeMap.putAll(
                Stream
                    .of(Symbol.symbols)
                    .collect(Collectors.toMap(symbol -> symbol.text, symbol -> symbol.type)));
        }
        this.type = tokenTypeMap.get(text);
    }
}
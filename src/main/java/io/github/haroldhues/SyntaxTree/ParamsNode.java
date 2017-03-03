package io.github.haroldhues.SyntaxTree;

import java.util.List;
import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.TokenType;

import java.util.ArrayList;





public class ParamsNode extends SyntaxTreeNode {
    public List<ParamNode> parameters = new ArrayList<ParamNode>();

    public ParamsNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        if(!parser.parseTokenIf(TokenType.Void)) {
            parameters.add(new ParamNode(parser, visitor));
            while(parser.parseTokenIf(TokenType.Comma)) {
                parameters.add(new ParamNode(parser, visitor));
            }
        }
        visitor.accept(this);
    }
}
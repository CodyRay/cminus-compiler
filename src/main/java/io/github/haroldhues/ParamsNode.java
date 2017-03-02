package io.github.haroldhues;

import java.util.List;
import java.util.ArrayList;



public class ParamsNode extends SyntaxTree {
    public List<ParamNode> parameters = new ArrayList<ParamNode>();

    public ParamsNode(Parser parser) throws Exception {
        if(!parser.parseTokenIf(TokenType.Void)) {
            parameters.add(new ParamNode(parser));
            while(parser.parseTokenIf(TokenType.Comma)) {
                parameters.add(new ParamNode(parser));
            }
        }
        parser.visit(this);
    }
}
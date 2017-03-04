package io.github.haroldhues.SyntaxTree;

import java.util.List;
import java.util.function.Consumer;

import io.github.haroldhues.*;
import io.github.haroldhues.Tokens.*;

import java.util.ArrayList;

public class ParamsNode extends SyntaxTreeNode {
    public List<ParamNode> parameters = new ArrayList<ParamNode>();

    public ParamsNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        if(!parser.parseTokenIf(TokenType.Void)) {
            do {
                parameters.add(new ParamNode(parser, visitor));
            }
            while(parser.parseTokenIf(TokenType.Comma));
        }
        visitor.accept(this);
    }

    public ParamsNode(List<ParamNode> parameters) {
        this.parameters = parameters;
    }
    
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(parameters.size() > 0) {
            String delimiter = "";
            for(ParamNode param : parameters) {
                builder.append(delimiter);
                builder.append(param);
                delimiter = ", ";
            }
        } else {
            builder.append(new Token(TokenType.Void));
        }
        return builder.toString();
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof ParamsNode)) {
            return false;
        }
         
        ParamsNode that = (ParamsNode) other;
 
        return this.parameters.equals(that.parameters);
    }
}
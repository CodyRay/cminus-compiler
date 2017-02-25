package io.github.haroldhues;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Scanner extends Enumerable<Token>
{
    public Enumerable<Character> source;
    public Enumerable<Character>.Item nextChar;
    public int lineNumber;

    public Scanner(Enumerable<Character> source) {
        this.source = source;
        lineNumber = 1;
        nextChar = source.next();
    }

    private void moveNext() {
        if (nextChar.getStatus() != Status.Value) {
            throw new UnsupportedOperationException("Source is in an error state or is out of input");
        }
        if (nextChar.getValue() == '\n') {
            lineNumber++;
        }
        nextChar = source.next();
    }

    private boolean nextIs(char... symbols) {
        char token = nextChar.getStatus() != Status.Value ? nextChar.getValue() : '\n';
        return Arrays.asList(symbols).contains(token);
    }

    private boolean nextIsDigit() {
        char token = nextChar.getStatus() != Status.Value ? nextChar.getValue() : '\n';
        return (int)'0' <= (int)token && (int)token <= (int)'9';
    }

    private boolean nextIsLetter() {
        char token = nextChar.getStatus() != Status.Value ? nextChar.getValue() : '\n';
        return ((int)'A' <= (int)token && (int)token <= (int)'Z') || ((int)'a' <= (int)token && (int)token <= (int)'z');
    }

    private String join(List<Character> list) {
        StringBuilder builder = new StringBuilder(list.size());
        for(Character c: list) {
            builder.append(c);
        }
        return builder.toString();
    }

    public enum State {
        Initial,
        In1,
        In2,
        In3,
        In4,
        In5,
        In6,
        In7,
        In8,
        In9,
        EndComment,
        AcceptLiteral,
        AcceptSymbol,
        AcceptIdentifier
    }

    public Item next() {
        try {
            State state = State.Initial;
            List<Character> tokenText = new ArrayList<Character>();
            boolean lastTokenSeen = false;
            while(nextChar.getStatus() != Status.Error && !lastTokenSeen) {
                state = nextState(state);

                if (nextChar.getStatus() == Status.End) {
                    lastTokenSeen = true;
                } else if (state == State.Initial) {
                    moveNext(); // If we are in initial that means we should discard the content before the next token
                } else if (state != State.AcceptLiteral && state != State.AcceptIdentifier && state != State.AcceptSymbol) {
                    // All other states (including EndComment) must consume input on the traversal to them
                    tokenText.add(nextChar.getValue());
                    moveNext();
                }
                
                if (state != State.EndComment && state != State.AcceptIdentifier && state != State.AcceptLiteral && state != State.AcceptSymbol) {
                    continue; // Continue until we reach an acceptance state
                }
                
                String text = join(tokenText);
                if (state == State.EndComment) {
                    tokenText.clear();
                    state = State.Initial;
                    continue; // We just exited a comment, start over
                }

                return 
                    state == State.AcceptIdentifier ?
                        new Item(new IdentifierToken(text)) :
                    state == State.AcceptLiteral ?
                        new Item(new IntegerLiteralToken(Integer.parseInt(text))) :
                    new Item(new Token(text));
            }

            if (nextChar.getStatus() == Status.Error) {
                return new Item(nextChar.getError());
            } else if (nextChar.getStatus() == Status.End && state != State.Initial) {
                return new Item(new CompileError("Unexpected End of Input")); // End
            } else {
                return new Item(); // We must be at State.Initial and the end of input
            }
        } catch (Exception ex) {
            return new Item(new CompileError(ex.getMessage()));
        }
    }

    private State nextState(State state) throws Exception {
        switch(state) {
            case Initial:
                return
                    nextIs('+', '-', '*', ';', ',', '(', ')', '[', ']', '{', '}') ?
                        State.In6 :
                    nextIs('<', '>', '=') ?
                        State.In3 :
                    nextIsDigit() ?
                        State.In1 :
                    nextIsLetter() ?
                        State.In2 :
                    nextIs('!') ?
                        State.In5 :
                    nextIs('\\') ?
                        State.In6 : 
                    State.Initial; // Loop
            case In1:
                return 
                    nextIsDigit() ?
                        State.In1 :
                    State.AcceptLiteral;
            case In2:
                return 
                    nextIsLetter() ?
                        State.In2 : // Loop
                    State.AcceptIdentifier;
            case In3:
                return 
                    nextIs('=') ?
                        State.In4 :
                    State.AcceptSymbol;
            case In4:
                return State.AcceptSymbol;
            case In5:
                if (!nextIs('=')) {
                    throw new Exception("Unexpected character, expected '='");
                }
                return State.In4;
            case In6:
                return 
                    nextIs('*') ?
                        State.In8 :
                    nextIs('!') ?
                        State.In9 :
                    State.AcceptSymbol;
            case In7:
                return 
                    nextIs('*') ?
                        State.In7 : // Loop
                    nextIs('/') ?
                        State.EndComment :
                    State.In8;
            case In8:
                return
                    nextIs('*') ?
                        State.In7 :
                    State.In8; // Loop
            case In9:
                return
                    nextIs('\n') ?
                        State.EndComment :
                    State.In9;
            default:
                throw new UnsupportedOperationException("Invalid State");
        }
    }
}

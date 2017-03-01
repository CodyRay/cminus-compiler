package io.github.haroldhues;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.Arrays;

public class Scanner extends Enumerable<Token>
{
    public Enumerable<Character> source;
    public Enumerable<Character>.Item currentChar;
    public int lineNumber;

    public Scanner(Enumerable<Character> source) {
        this.source = source;
        lineNumber = 1;
        currentChar = source.next();
    }

    private void moveNext() {
        if (currentChar.getStatus() != Status.Value) {
            throw new UnsupportedOperationException("Source is in an error state or is out of input");
        }
        if (currentChar.getValue() == '\n') {
            lineNumber++;
        }
        currentChar = source.next();
    }

    private boolean currentIs(Character... symbols) {
    	Character token = currentChar.getStatus() == Status.Value ? currentChar.getValue() : '\n';
        return Arrays.asList(symbols).contains(token);
    }

    private boolean currentIsDigit() {
        char token = currentChar.getStatus() == Status.Value ? currentChar.getValue() : '\n';
        return (int)'0' <= (int)token && (int)token <= (int)'9';
    }

    private boolean currentIsLetter() {
        char token = currentChar.getStatus() == Status.Value ? currentChar.getValue() : '\n';
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
            boolean finalCharacterSeen = false;
            while(!finalCharacterSeen) {
                if (currentChar.getStatus() == Status.Error) {
                    return new Item(currentChar.getError());
                }
                // Step 1: Move to next state
                state = nextState(state);

                // Step 2: Move the source input stream and accumulate tokens based on the resulting state 
                if (currentChar.getStatus() == Status.End) {
                    finalCharacterSeen = true;
                } else if (state == State.Initial) {
                    moveNext(); // If we are in initial that means we should discard the content before the next token
                } else if (state != State.AcceptLiteral && state != State.AcceptIdentifier && state != State.AcceptSymbol) {
                    // All other states (including EndComment) must consume input on the traversal to them
                    tokenText.add(currentChar.getValue());
                    moveNext();
                }
                
                // Step 3: Continue Running the loop until we are in an accept state (or the end comment state)
                if (state != State.EndComment && state != State.AcceptIdentifier && state != State.AcceptLiteral && state != State.AcceptSymbol) {
                    continue; // Continue until we reach an acceptance state
                }
                
                String text = join(tokenText);
                // Step 3 Also: Return to the start state and discard the accumulated tokens if we are at the end of a comment
                if (state == State.EndComment) {
                    tokenText.clear();
                    state = State.Initial;
                    continue; // We just exited a comment, start over
                }

                // Step 4: Emit a token based on the specific acceptance state
                return new Item(identifyToken(state, text));
            }

            if (currentChar.getStatus() == Status.End && state != State.Initial) {
                return new Item(new CompileError("Unexpected End of Input")); // End
            } else {
                return new Item(); // We must be at State.Initial and the end of input
            }
        } catch (Exception ex) {
            return new Item(new CompileError(ex.getMessage()));
        }
    }

    private static Token identifyToken(State state, String text) {
    	for (Lexeme lex : Lexeme.reservedKeywords) {
    		if (lex.text.equals(text)) {
    			return new Token(lex.type);
    		}
    	}
    	
        return 
            state == State.AcceptIdentifier ?
                new IdentifierToken(text) :
            state == State.AcceptLiteral ?
                new IntegerLiteralToken(Integer.parseInt(text)) :
            new Token(text);
    }

    private State nextState(State state) throws Exception {
        switch(state) {
            case Initial:
                return
                    currentIs('+', '-', '*', ';', ',', '(', ')', '[', ']', '{', '}') ?
                        State.In4 :
                    currentIs('<', '>', '=') ?
                        State.In3 :
                    currentIsDigit() ?
                        State.In1 :
                    currentIsLetter() ?
                        State.In2 :
                    currentIs('!') ?
                        State.In5 :
                    currentIs('/') ?
                        State.In6 : 
                    State.Initial; // Loop
            case In1:
                return 
                    currentIsDigit() ?
                        State.In1 : // Loop
                    State.AcceptLiteral;
            case In2:
                return 
                    currentIsLetter() ?
                        State.In2 : // Loop
                    State.AcceptIdentifier;
            case In3:
                return 
                    currentIs('=') ?
                        State.In4 :
                    State.AcceptSymbol;
            case In4:
                return State.AcceptSymbol;
            case In5:
                if (!currentIs('=')) {
                    throw new Exception("Unexpected character, expected '='");
                }
                return State.In4;
            case In6:
                return 
                    currentIs('*') ?
                        State.In8 :
                    currentIs('/') ?
                        State.In9 :
                    State.AcceptSymbol;
            case In7:
                return 
                    currentIs('*') ?
                        State.In7 : // Loop
                    currentIs('/') ?
                        State.EndComment :
                    State.In8;
            case In8:
                return
                    currentIs('*') ?
                        State.In7 :
                    State.In8; // Loop
            case In9:
                return
                    currentIs('\n') ?
                        State.EndComment :
                    State.In9;
            default:
                throw new UnsupportedOperationException("Invalid State");
        }
    }
}

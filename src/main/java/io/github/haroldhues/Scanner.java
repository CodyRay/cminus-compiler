package io.github.haroldhues;

import java.util.List;

import io.github.haroldhues.Tokens.*;

import java.util.ArrayList;
import java.util.Arrays;



public class Scanner extends Enumerable<Token>
{
    public Enumerable<Character> source;
    public Character currentChar;
    public int lineNumber;
    public int columnNumber;
    public boolean complete = false;

    public Scanner(Enumerable<Character> source) throws CompileErrorException {
        this.source = source;
        lineNumber = 1;
        columnNumber = 1;
        currentChar = source.next();
    }

    private void moveNext() throws CompileErrorException {
        columnNumber++;
        if (currentChar == '\n') {
            lineNumber++;
            columnNumber = 1;
        }
        currentChar = source.next();
    }

    private boolean currentIs(Character... symbols) {
    	Character token = currentChar != null ? currentChar : '\n';
        return Arrays.asList(symbols).contains(token);
    }

    private boolean currentIsDigit() {
    	Character token = currentChar != null ? currentChar : '\n';
        return (int)'0' <= (int)token && (int)token <= (int)'9';
    }

    private boolean currentIsLetter() {
    	Character token = currentChar != null ? currentChar : '\n';
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

    public Token next() throws CompileErrorException {
        if (complete) {
            return null;
        }
        State state = State.Initial;
        List<Character> tokenText = new ArrayList<Character>();
        boolean finalCharacterSeen = false;
        while(!finalCharacterSeen) {
            // Step 1: Move to next state
            state = nextState(state);

            // Step 2: Move the source input stream and accumulate tokens based on the resulting state 
            if (currentChar == null) {
                finalCharacterSeen = true;
            } else if (state == State.Initial) {
                moveNext(); // If we are in initial that means we should discard the content before the next token
            } else if (state != State.AcceptLiteral && state != State.AcceptIdentifier && state != State.AcceptSymbol) {
                // All other states (including EndComment) must consume input on the traversal to them
                tokenText.add(currentChar);
                moveNext();
            }
            
            // Step 3: Continue Running the loop until we are in an accept state (or the end comment state)
            if (state != State.EndComment && state != State.AcceptIdentifier && state != State.AcceptLiteral && state != State.AcceptSymbol) {
                continue; // Continue until we reach an acceptance state
            }

            String text = join(tokenText);
            int column = columnNumber - text.length();
            // Step 3 Also: Return to the start state and discard the accumulated tokens if we are at the end of a comment
            if (state == State.EndComment) {
                tokenText.clear();
                state = State.Initial;
                continue; // We just exited a comment, start over
            }

            // Step 4: Emit a token based on the specific acceptance state
            return identifyToken(state, text, lineNumber, column);
        }

        if (currentChar == null && state != State.Initial) {
            throw new CompileErrorException("Unexpected End of Input", lineNumber, columnNumber); // End
        } else {
            complete = true;
            return new Token(TokenType.Eof, lineNumber, columnNumber); // We must be at State.Initial and the end of input
        }
    }

    private static Token identifyToken(State state, String text, int line, int column) {
    	for (Lexeme lex : Lexeme.reservedKeywords) {
    		if (lex.text.equals(text)) {
    			return new Token(lex.type, line, column);
    		}
    	}
    	
        return 
            state == State.AcceptIdentifier ?
                new IdentifierToken(text, line, column) :
            state == State.AcceptLiteral ?
                new IntegerLiteralToken(Integer.parseInt(text), line, column) :
            new Token(text, line, column);
    }

    private State nextState(State state) throws CompileErrorException {
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
                    throw new CompileErrorException("Unexpected character, expected '='", lineNumber, columnNumber);
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

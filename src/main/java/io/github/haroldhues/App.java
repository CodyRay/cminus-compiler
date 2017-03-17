package io.github.haroldhues;

import java.io.*;

import io.github.haroldhues.SyntaxTree.*;
import io.github.haroldhues.Tokens.*;

public class App 
{
    public static void main( String[] args )
    {
        String fileName = args[0];
        try {
            if (!fileName.matches(".*\\.cm")) {
                throw new CompileErrorException("The provided file (" + fileName + ") name does not appear to be a *.cm file, please rename it");
            }
            Enumerable<Character> file = new FileScanner(fileName);
            Enumerable<Token> tokens = new Scanner(file);
            Parser parser = new Parser(tokens);
            RootNode node = parser.parse();
            Checker.check(node);
            Interpreter.run(node, new IO());
        } catch (FileNotFoundException ex) {
            System.out.println("The file (" + fileName + ") doesn't seem to exist!: " + ex.getMessage());
        } catch (CompileErrorException ex) {
            System.out.println("Compile Error: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Unexpected Error: " + ex.getMessage());
        }
    }

    public static class FileScanner extends Enumerable<Character> {
        FileReader reader = null;

        public FileScanner(String fileName) throws FileNotFoundException {
            reader = new FileReader(fileName);
        }

        public Character next() {
            try {
                int c = reader.read();
                if(c != -1) {
                    return (char) c;
                } else {
                    return null;
                }
            } catch (IOException ex) {
                return null;
            }
        }
    }

    public static class IO implements InputOutput {
        public int read() {
            System.out.print("> ");
            String text = System.console().readLine();
            Integer in = null;
            while(in == null) {
                try {
                    in = Integer.parseInt(text);
                } catch (NumberFormatException ex) {
                    System.out.println("Try Again...");
                }
            }
            return in;
        }

        public void write(int out) {
            System.out.println(out);
        }
    }
}

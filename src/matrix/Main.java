package matrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Entry point for real matrix user interface
 * @author ParkerHall
 *
 */
public class Main {
    
    /**
     * Read user input and respond accordingly
     * Program terminates on empty input
     * @param args unused
     * @throws IOException if error occurs during opening of BufferedReader
     */
    public static void main(String[] args) throws IOException {
        final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        MatrixParser parser = new MatrixParser();
        
        while (true) {
            System.out.print("> ");
            String input = in.readLine();
            
            if (input.equals("")) {
                return; // exit the program
            }
            
            try {
                final String output = parser.parseString(input);
                System.out.println(output);
            } catch (MatrixFormatException mfe) {
                System.err.println(mfe.getMessage());
            } catch (NumberFormatException nfe) {
                System.err.println(nfe.getMessage());
            } catch (IllegalArgumentException iae) {
                System.err.println(iae.getMessage());
            }
        }
    }
    
}

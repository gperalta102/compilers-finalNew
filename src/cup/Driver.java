package cup;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.PrintWriter;

public class Driver {

    public static void main (String[] args) throws IOException {
        System.err.close();
        
        // Reading input part.
		 Scanner in = new Scanner(System.in);
		 String fileName="";
     	while (true) {
			System.out.println("** THE INPUT MUST BE IN THE \"\\inputs\" FOLDER. **");
			System.out.print("> Please name the input file: ");
			fileName = in.nextLine();
		   
		 
			// Lexing part.
			ToyLexer tLexer = new ToyLexer("inputs/" + fileName);
			int[] tokens = tLexer.getTokenList();
			
			PrintWriter out = new PrintWriter("outputs/lexer/" + fileName);
			out.print(tLexer.tokensToString(tokens));
			out.close();
			
			// Parsing part.
			try {
				Parser p = new Parser(new TScanner("outputs/lexer/" + fileName));
				p.parse();
			} catch (FileNotFoundException e) {
				System.out.println("Unable to find file.");
			} catch (Exception e) {
				System.out.println("\n[reject] from catch");
			}
		}
	}
}
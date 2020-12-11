package cup.example;


import java.io.FileNotFoundException;

public class Driver {
	//need to combine with prev driver
    public static void main (String[] args) {
        System.err.close();
        try {
        	ToyLexer l = new ToyLexer("input.txt");
        	ToyLexer.printIntArray(l.getTokenList());
        	Parser p = new Parser(l);
            p.parse();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to find file.");
        } catch (Exception e) {
            System.out.println("\n[reject] from catch");
        }
    }
}
package cup.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ToyLexer
{


	public static final String[] TOKEN_NAMES = {"boolean", "break", "class", "double", "else", // keywords 0-20
												"extends", "for", "if", "implements", "int", 
												"interface", "new", "newarray", "null", "println", 
												"readln", "return", "string", "this", "void", "while", 
												"plus", "minus", "multiplication", // operators/punctuation 21-44
												"division", "mod", "less", "lessequal", "greater", 
												"greaterequal", "equal", "notequal", "and", "or", "not", 
												"assignop", "semicolon", "comma", "period", "leftparen", 
												"rightparen", "leftbracket", "rightbracket", "leftbrace", 
												"rightbrace", 
												"intconstant", "doubleconstant", // constants 45-48
												"stringconstant", "booleanconstant",
												"id" }; // just "identifier" 49
	
	public static final String[] KEYWORDS = {"boolean", "break", "class", "double", "else",
											 "extends", "false", "for", "if", "implements",
											 "int", "interface", "new", "newarray", "null", "println", "readln",
											 "return", "string", "this", "true", "void", "while"};
	
	public static final String[] OPERATORS = {"+", "-", "*", "/", "%", "<", "<=", ">", ">=", "==",
											  "!=", "&&", "||", "!", "=", ";", ",", ".", "(", ")",
											  "[", "]", "{", "}"};
	
	private int[] tokenList;
	
	public ToyLexer(String fileName) throws IOException
	{
		// okay how i'm imagining this in my head:
		// input takes in the entire file and reads it character by character until it finds a full
		// "word" (which i guess is what the trie table is for), and then finds the equivalent token
		// to turn it into a token and put it in an array of tokens until the entire thing is evaluated
		
		// 1. generate the trie by slapping in all the keywords
		// 2. read in the file character by character
		// 3. print all the integers in the array as their token names
		
		// 1. generate the trie
		Trie symbolTable = new Trie();
		int[] tokenList = new int[500]; // arbitrary number
		int tokenCount = 0;
		
		for (int i = 0; i < KEYWORDS.length; i++)
		{
			symbolTable.insert(KEYWORDS[i]); 
		}
		
		// 2. read in the file step by step
		File f = new File(fileName);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		int buffer = 10; // this is in the weird case where i eat an operator or character and need to get it back
		
		int a = 0;
		while ((a = br.read()) != -1)
		{
			// each iteration of THIS while loop is for each token, the "c" being read right now is the
			// first character of the token, or whitespace somehow lmao
			char character1 = (char) a;
			// jump to each of the alternative routes:
			// letters, numbers, strings, comments, punctuation, or whitespace
			
			if (Character.isLetter(character1))
			{
				// letters route, can lead to one of the keywords or identifier
				String fullName = "" + character1;
				boolean guaranteedIdentifier = false;
				
				int c = 0;
				br.mark(buffer);
				while ((c = br.read()) != -1)
				{
					char character = (char) c;
					if (Character.isDigit(character) || character == '_')
					{
						guaranteedIdentifier = true;
					}
					else if (Character.isWhitespace(character))
					{
						break;
					}
					else if (!Character.isLetterOrDigit(character))
					{
						br.reset(); // the point of the buffer, so you can actually get to reading this character
						break;
					}
					
					fullName += character;
					br.mark(buffer);
				}
				
				if (symbolTable.search(fullName))
				{
					// if this returns true, then it's either a keyword or an identifier already in the table
					if (guaranteedIdentifier)
					{
						tokenList[tokenCount] = 49;
						tokenCount++;
					}
					else
					{
						boolean found = false;
						for (int i = 0; i < KEYWORDS.length; i++) // i didn't want to have to do this
						{
							if (fullName.equals(KEYWORDS[i]))
							{
								if (fullName.equals("true") || fullName.equals("false"))
								{
									// these two are special cases in that they are just boolean constants
									tokenList[tokenCount] = 48;
									tokenCount++;
								}
								else
								{
									if (i >= 20) // "true" is #20 on keyword list, if you passed true, you also passed false
									{
										tokenList[tokenCount] = i - 2;
										tokenCount++;
									}
									else if (i >= 6) // "false" is #6 on keyword list
									{
										tokenList[tokenCount] = i - 1;
										tokenCount++;
									}
									else
									{
										tokenList[tokenCount] = i; // it's before "false" so is treated as normal
										tokenCount++;
									}
								}
								found = true;
								break;
							}
						}
						
						if (!found)
						{
							// it was not found, but since we know it's IN the list already, we don't need to add it again
							// so let's just call it as it is, and add it to the token list
							tokenList[tokenCount] = 49;
							tokenCount++;
						}
					}
				}
				else
				{
					// if it returned false, then it's not in the table, meaning it can't be a keyword
					// (they're all already in there) therefore add it to the table!
					tokenList[tokenCount] = 49;
					tokenCount++;
				}
				continue;
			}
			
			if (Character.isDigit(character1))
			{
				boolean confirmedInt = false;
				boolean confirmedDouble = false;
				int c = 0;
				br.mark(buffer);
				if ((c = br.read()) != -1)
				{
					char character2 = (char) c;
					if (character2 == '0') // testing the 0 case JUST for hexadecimality
					{
						br.mark(buffer);
						if ((c = br.read()) != -1)
						{
							character2 = (char) c;
							if (character2 == 'x')
							{
								confirmedInt = true;
							}
						}
						else
						{
							br.reset(); // in case the number was actually just "0" and the next character is '+' or something
						}
					}
					while ((c = br.read()) != -1)
					{
						character2 = (char) c;
						if (character2 == '.')
						{
							confirmedDouble = true;
						}
						else if (!Character.isLetterOrDigit(character2))
						{
							br.reset();
							break; // we out, except for the case of . because it's a decimal
						}
						br.mark(buffer);
					}
				}
				
				if (confirmedInt)
				{
					tokenList[tokenCount] = 45;
					tokenCount++;
				}
				else if (confirmedDouble)
				{
					tokenList[tokenCount] = 46;
					tokenCount++;
				}
				else
				{
					tokenList[tokenCount] = 45;
					tokenCount++;
				}
				continue;
			}
			
			if (character1 == '\"')
			{
				// string route, leads to a string that ends with another "
				// i did the comment route before this one, and they're pretty much identical
				int c = 0;
				br.mark(buffer);
				if ((c = br.read()) != -1)
				{
					char character2 = (char) c;
					if (character2 == '\"')
					{
						break;
					}
					tokenList[tokenCount] = 47;
					tokenCount++;
				}
				continue;
			}
			if (character1 == '/')
			{
				// comment route, either goes into single line or multi line comment, please note
				int c = 0;
				br.mark(buffer);
				if ((c = br.read()) != -1)
				{
					char character2 = (char) c;
					if (character2 == '/')
					{
						// single line! read until you find a new line.
						int d = 0;
						while ((d = br.read()) != -1)
						{
							char character3 = (char) d;
							if (character3 == '\n')
							{
								break;
							}
						}
						continue;
					}
					else if (character2 == '*')
					{
						// multiple lines! read until you find a * AND a / after it
						int d = 0;
						while ((d = br.read()) != -1)
						{
							char character3 = (char) d;
							int e = 0;
							if (character3 == '*' && (e  = br.read()) != -1)
							{
								if ((char) e == '/')
								{
									break; // GO GO GO ESCAPE
								}
							}
						}
						continue;
					}
					else
					{
						br.reset(); // oops i accidentally ate the number needed for division
					}
				}
				
			}
			
			if (Character.isWhitespace(character1))
			{
				continue; // ignore this character lmao
			}
			
			if (!Character.isLetterOrDigit(character1))
			{
				// not a character or a digit, must be a punctuation or operator
				String fullOperator = "" + character1;
				
				br.mark(buffer); // to go back if the second character isn't actually another symbol
				int c = 0;
				if ((c = br.read()) != -1)
				{
					// this wouldn't happen if the previous character was the last one
					char character2 = (char) c;
					if (!Character.isLetterOrDigit(character2) && !Character.isWhitespace(character2))
					{
						// if it's not a letter or digit, then it's a special character, else ignore
						// also not including whitespace which is not a letter or digit as well
						fullOperator += character2;
					}
					else
					{
						// have to go back due to possibly overwriting something we really shouldn't
						br.reset();
					}
				}
				for (int i = 0; i < OPERATORS.length; i++)
				{
					if (fullOperator.equals(OPERATORS[i]))
					{
						tokenList[tokenCount] = i + 21; // because the operator tokens start at 21
						tokenCount++;
						break; // no need for the loop anymore
					}
				}
				continue;
			}
		}
		
		//printIntArray(tokenCount, tokenList);
		//System.out.println("\n" + symbolTable);
		
		this.tokenList = new int[tokenCount];
		for (int i = 0; i < tokenCount; i++)
		{
			this.tokenList[i] = tokenList[i];
		}
		
		br.close();
		fr.close();
	}
	
	public int[] getTokenList()
	{
		return tokenList;
	}
	
	public static void printIntArray(int[] array)
	{
		for (int i = 0; i < array.length; i++)
		{
			//System.out.print(array[i] + " ");
			System.out.print(TOKEN_NAMES[array[i]] + " ");
		}
		System.out.println();
	}
	
	/*public static void main(String[] args) throws IOException
	{
		Scanner in = new Scanner(System.in);
		System.out.print("input: ");
		String fileName = in.nextLine();
		
		toy_lexer(fileName);
		in.close();
	}*/
	
}

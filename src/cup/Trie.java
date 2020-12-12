package cup;

public class Trie
{
	public static final int SWITCH_COUNT = 52; 		// all keywords and user-defined identifiers MUST
													// start with [A-Z][a-z]
	public static final int MAX_TRANSITION = 600; 	// arbitrarily decided number
	public static final char[] ALL_STARTING_SYMBOLS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	public static final char END_SYMBOL = '@'; // if this symbol, then that's the end of the token
	// there was probably a better way to do that which i just did but it didn't occur to me at the time of writing this
	
	private int[] switch_; // the first character in a given token, it's supposed to be named "switch" but that's a keyword, so there's an underscore
	private char[] symbol; // the symbols associated with each switch, which corresponds to a number
	private int[] next;	  // points to the next possible word if it doesn't match this current one
	private int nextAvailableSymbol; // points to the next open spot in symbol
	
	public Trie()
	{
		// initializing the three arrays that make up the trie-o, haha, get it?
		switch_ = new int[SWITCH_COUNT];
		for (int i = 0; i < SWITCH_COUNT; i++)
		{
			switch_[i] = -1; // -1 means there's nothing in there
		}
		
		symbol = new char[MAX_TRANSITION];
		next = new int[MAX_TRANSITION];
		for (int i = 0; i < MAX_TRANSITION; i++)
		{
			next[i] = -1; // same as before, nothing is there
		}
	}
	
	// inserts a token into the trie, under the brash assumption that it's not already there
	public void insert(String token)
	{
		//System.out.println("-> adding " + token); // DEBUG LINE
		int currentIndex = findEquivalentIndex(token.charAt(0)); // wrote a method that converts the character to the index 
		if (switch_[currentIndex] == -1) // if the value at that character is empty, then set it to the next available symbol 
		{
			switch_[currentIndex] = nextAvailableSymbol;
			currentIndex = nextAvailableSymbol; // reusing currentIndex to traverse through the symbol array, starting at the first available open index
		}
		else
		{
			currentIndex = switch_[currentIndex]; // reusing currentIndex to traverse through the symbol array, starting from the index specified in switch
		}
		
		int currentCharacter = 1;
		
		while (currentCharacter < token.length())
		{
			// if the current index being checked is at the very end of the existing characters,
			// just start adding the rest of the characters to the thing
			if (currentIndex == nextAvailableSymbol)
			{
				symbol[currentIndex] = token.charAt(currentCharacter);
				//System.out.println("adding " + token.charAt(currentCharacter) + " at symbol[" + currentIndex + "]"); // DEBUG LINE
				currentCharacter++;
				currentIndex++;
				nextAvailableSymbol++;
			}
			else
			{
				// if the symbol at the current index is equal to the current character of the token:
				if (symbol[currentIndex] == token.charAt(currentCharacter))
				{
					// advance current index, because so far the string matches
					currentCharacter++;
					currentIndex++;
				}
				else
				{
					// check if an alternate path exists in "next"
					if (next[currentIndex] != -1)
					{
						// jump to the "next" symbol
						currentIndex = next[currentIndex];
					}
					else
					{
						// if it doesn't exist, create one, then move on to the rest of the string
						//System.out.println("adding " + nextAvailableSymbol + " to next[" + currentIndex + "]") // DEBUG LINE
						next[currentIndex] = nextAvailableSymbol;
						currentIndex = nextAvailableSymbol;
					}
				}
			}
		}
		
		// to show that it is the end of the thing
		symbol[nextAvailableSymbol] = END_SYMBOL;
		//System.out.println("adding @ at symbol[" + nextAvailableSymbol + "]"); // DEBUG LINE
		nextAvailableSymbol++;
	}
	
	public boolean search(String token)
	{
		//System.out.println("-> searching for " + token); // DEBUG LINE
		int currentIndex = findEquivalentIndex(token.charAt(0)); // wrote a method that converts the character to the index 
		if (switch_[currentIndex] == -1) // if the value at that character is empty, then return false because there's clearly nothing that could exist there
		{
			return false;
		}
		else
		{
			currentIndex = switch_[currentIndex]; // reusing currentIndex to traverse through the symbol array, starting from the index specified in switch
		}
		
		int currentCharacter = 1;
		
		while (currentCharacter < token.length())
		{
			//System.out.println("checking character: " + token.charAt(currentCharacter) + " at position " + currentIndex); // DEBUG LINE
			// if the symbol at the current index is equal to the current character of the token:
			if (symbol[currentIndex] == token.charAt(currentCharacter))
			{
				// advance current index, because so far the string matches
				currentCharacter++;
				currentIndex++;
			}
			else
			{
				// check if an alternate path exists in "next"
				if (next[currentIndex] != -1)
				{
					// jump to the "next" symbol
					currentIndex = next[currentIndex];
				}
				else
				{
					// if it doesn't exist, explode and return false
					return false;
				}
			}
		}
		// the entire token exists so far, but it could be part of another one (i.e: the and these)
		// so if the next token is an "@", then that truly means the end of the token
		return (symbol[currentIndex] == '@' ? true : false);
	}
	
	@Override
	public String toString()
	{
		String[] lines = new String[6 + (nextAvailableSymbol / 20) + 1];
		lines[0] = formattedLine_CharVer("        ", ALL_STARTING_SYMBOLS, 0, 20);
		lines[1] = formattedLine_IntVer("switch: ", switch_, 0, 20) + "\n";
		lines[2] = formattedLine_CharVer("        ", ALL_STARTING_SYMBOLS, 20, 40);
		lines[3] = formattedLine_IntVer("switch: ", switch_, 20, 40) + "\n";
		lines[4] = formattedLine_CharVer("        ", ALL_STARTING_SYMBOLS, 40, SWITCH_COUNT);
		lines[5] = formattedLine_IntVer("switch: ", switch_, 40, SWITCH_COUNT) + "\n";
		
		int[] numbers = new int[nextAvailableSymbol];
		for (int i = 0; i < nextAvailableSymbol; i++)
		{
			numbers[i] = i;
		}
		
		for (int i = 0; i < lines.length - 6; i++)
		{
			int min = i * 20;
			int max = Math.min((i + 1) * 20, nextAvailableSymbol);
			lines[i + 6] = formattedLine_IntVer("       ", numbers, min, max);
			lines[i + 6] += "\n";
			lines[i + 6] += formattedLine_CharVer("symbol: ", symbol, min, max);
			lines[i + 6] += "\n";
			lines[i + 6] += formattedLine_IntVer("next:   ", next, min, max); 
			lines[i + 6] += "\n";
		}
		
		String output = "";
		for (int i = 0; i < lines.length; i++)
		{
			output += lines[i] + "\n";
		}
		return output;
	}
	
	private String formattedLine_CharVer(String header, char[] array, int start, int end)
	{
		if (start < end)
		{
			String line = header;
			for (int i = start; i < end; i++)
			{
				int remainingCharacters = 5;
				String valueToAdd = "" + array[i];
				remainingCharacters -= valueToAdd.length();
				for (int j = 0; j < remainingCharacters; j++)
				{
					valueToAdd += " ";
				}
				
				line += valueToAdd;
			}
			return line;
		}
		return "";
	}
	
	// RUNS THE ASSUMPTION THAT THE "HEADER" IS 8 CHARACTERS, NO MORE NO LESS
	private String formattedLine_IntVer(String header, int[] array, int start, int end)
	{
		if (start < end)
		{
			String line = header;
			for (int i = start; i < end; i++)
			{
				int remainingCharacters = 5;
				String valueToAdd = "" + array[i];
				remainingCharacters -= valueToAdd.length();
				for (int j = 0; j < remainingCharacters; j++)
				{
					valueToAdd += " ";
				}
				
				line += valueToAdd;
			}
			return line;
		}
		return "";
	}
	
	private int findEquivalentIndex(char c)
	{
		for (int i = 0; i < ALL_STARTING_SYMBOLS.length; i++)
		{
			if (ALL_STARTING_SYMBOLS[i] == c)
			{
				return i;
			}
		}
		return -1;
	}
}

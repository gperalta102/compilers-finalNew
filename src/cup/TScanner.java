package cup;

import java_cup.runtime.*;

import java.util.LinkedList;
import java.util.Queue;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TScanner implements java_cup.runtime.Scanner{

	String [] tokens;
	Queue<Symbol> out;
	boolean shift = false;
    public TScanner (String fileName) throws FileNotFoundException {
		out = new LinkedList<Symbol>();
	
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = null;
		try {
		 while ((line = br.readLine()) != null) {
			String[] t = line.split(" ");
			tokens = new String[t.length];
			for(int i = 0; i < t.length; i++){
				out.add(new Symbol(toInteger(t[i])));	
			}
		 }
		 out.add(new Symbol(sym.EOF));
		}catch(IOException e) {
			
		}
    }
	
	public static void init() throws java.io.IOException        {}
	
	public String toString(int n) {
		switch(n) {
		case sym._IF:
			return "if";
		case sym._INT:
			return "int";
		case sym._VOID:
			return "void";
		case sym.ADDOP:
			return "add";
		case sym.AND:
			return "and";
		case sym.ASSIGN:
			return "assign";
		case sym.BOOLCONST:
			return "boolconst";
		case sym.BOOLN:
			return "boolean";
		case sym.BRK:
			return "break";
		case sym.CLS:
			return "class";
		case sym.COMMA:
			return "comma";
		case sym.DBL:
			return "double";
		case sym.DBLCONST:
			return "doubleconst";
		case sym.DIV:
			return "divide";
		case sym.DOT:
			return "period";
		case sym.ELS:
			return "else";
		case sym.EOF:
			return "EOF";
		case sym.EQL:
			return "equal";
		case sym.error:
			return "error";
		case sym.EXTNDS:
			return "extends";
		case sym.FR:
			return "for";
		case sym.GRT:
			return "greater";
		case sym.GRTEQ:
			return "greaterequal";
		case sym.ID:
			return "id";
		case sym.IMPL:
			return "implements";
		case sym.INTCONST:
			return "intconst";
		case sym.INTRFC:
			return "interface";
		case sym.LESS:
			return "less";
		case sym.LESSEQ:
			return "lessequal";
		case sym.LFTBRACE:
			return "leftbrace";
		case sym.LFTBRACKET:
			return "leftbracket";
		case sym.LFTPRN:
			return "leftparen";
		case sym.MOD:
			return "mod";
		case sym.MULOP:
			return "multiplication";
		case sym.NEQ:
			return "notequal";
		case sym.NEWAR:
			return "newarray";
		case sym.NOT:
			return "not";
		case sym.NUL:
			return "null";
		case sym.NW:
			return "new";
		case sym.OR:
			return "or";
		case sym.PRNTLN:
			return "println";
		case sym.READLN:
			return "readline";
		case sym.RTBRACE:
			return "rightbrace";
		case sym.RTBRC:
			return "rightbracket";
		case sym.RTPRN:
			return "rightparen";
		case sym.RTRN:
			return "return";
		case sym.SEMICOL:
			return "semicol";
		case sym.STRING:
			return "string";
		case sym.STRINGCONST:
			return "stringconst";
		case sym.SUBOP:
			return "minus";
		case sym.WHLE:
			return "while";
		}
		return "INVALID";
	}
	
	public int toInteger(String s){
		switch(s) {
			case "doubleconst":
				return sym.DBLCONST;
			case "intconst":
				return sym.INTCONST;
			case "add":
				return sym.ADDOP;
			case "multiplication":
				return sym.MULOP;
			case "minus":
				return sym.SUBOP;
			case "semicol":
				return sym.SEMICOL;
			case "comma":
				return sym.COMMA;
			case "boolean":
				return sym.BOOLN;
			case "else":
				return sym.ELS;
			case "implements":
				return sym.IMPL;
			case "newarray":
				return sym.NEWAR;
			case "return":
				return sym.RTRN;
			case "mod":
				return sym.MOD;
			case "greaterequal":
				return sym.GRTEQ;
			case "lessequal":
				return sym.LESSEQ;
			case "greatherthan":
				return sym.GRT;
			case "and":
				return sym.AND;
			case "rightbracket":
				return sym.RTBRC;
			case "rightparen":
				return sym.RTPRN;
			case "booleanconst":
				return sym.BOOLCONST;
			case "int":
				return sym._INT;
			case "period":
				return sym.DOT;
			case "leftbracket":
				return sym.LFTBRACKET;
			case "or":
				return sym.OR;
			case "break":
				return sym.BRK;
			case "extends":
				return sym.EXTNDS;
			case "null":
				return sym.NUL;
			case "string":
				return sym.STRING;
			case "stringconst":
				return sym.STRINGCONST;
			case "less":
				return sym.LESS;
			case "notequal":
				return sym.NEQ;
			case "equal":
				return sym.EQL;
			case "assign":
				return sym.ASSIGN;
			case "leftparen":
				return sym.LFTPRN;
			case "leftbrace":
				return sym.LFTBRACE;
			case "rightbrace":
				return sym.RTBRACE;
			case "if":
				return sym._IF;
			case "new":
				return sym.NW;
			case "readline":
				return sym.READLN;
			case "while":
				return sym.WHLE;
			case "divide":
				return sym.DIV;
			case "not":
				return sym.NOT;
			case "double":
				return sym.DBL;
			case "class":
				return sym.CLS;
			case "for":
				return sym.FR;
			case "interface":
				return sym.INTRFC;
			case "println":
				return sym.PRNTLN;
			case "void":
				return sym._VOID;
			case "id":
				return sym.ID;
						
			
		}
		return 0;
	}
	

    @Override
    public Symbol next_token () throws Exception {
    	if(out.isEmpty()) {
            return null;
        }
        Symbol s = out.remove();
        if(shift) {
            System.out.print("[shift]\n" + toString(s.sym) + " ");
        } else {
           System.out.print(toString(s.sym) + " ");
        }
        shift = true;
        return s;
    }
}
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class Main{

	public static Stack<String[]> stacks = new Stack<String[]>();
	public static HashMap<String, Integer> registers = new HashMap<String, Integer>();
	
	public static void main(String[] args){
	
		for(int i=1; i<=32; i++){
			String reg = "R" + i;
			registers.put(reg, 100);
		}
		
		Stack<String[]> stacks = Parser.reader();
		
		for (String[] e : stacks){
			for (String s : e){
				System.out.println(s);
			}
			System.out.println();
		}
		
		for (String[] e : stacks){
			String inst = e[0];
			String op1 = e[1];
			String op2 = e[2];
			
			if(Pattern.matches("LOAD", inst)){//If LOAD
				registers.replace(op1, Integer.parseInt(op2));
			}
		}
		
		for(int i=1; i<=32; i++){
			String reg = "R" + i;
			System.out.println(reg + ": " + registers.get(reg));
		}
		
	}
	
}
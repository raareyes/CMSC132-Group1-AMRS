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
		
		int of = 0;

		for (String[] e : stacks){
			String inst = e[0];
			String op1 = e[1];
			String op2 = e[2];

			if(Pattern.matches("LOAD", inst)){//If LOAD
				int o2 = Integer.parseInt(op2);
				if (o2 > 99){
					registers.replace(op1, 99);
					of = 1;
					System.out.println(op1 + "\t\t of: " + of);
				}else {
					registers.replace(op1, o2);
					of = 0;
					System.out.println(op1 + "\t\t of: " + of);
				}
			}

			if(Pattern.matches("ADD", inst)){//If ADD
				int o1 = registers.get(op1);
				int o2 = registers.get(op2);

				o1 = o1 + o2;

				if (o1 > 99){
					registers.replace(op1, 99);
					of = 1;
					System.out.println(op1 + "\t\t of: " + of);
				}else {
					registers.replace(op1, o1);
					of = 0;
					System.out.println(op1 + "\t\t of: " + of);
				}	
			}

			if(Pattern.matches("SUB", inst)){//If SUB
				int o1 = registers.get(op1);
				int o2 = registers.get(op2);

				o1 = o1 - o2;

				if (o1 > 99){
					registers.replace(op1, 99);
					of = 1;
					System.out.println(op1 + "\t\t of: " + of);
				}else {
					registers.replace(op1, o1);
					of = 0;
					System.out.println(op1 + "\t\t of: " + of);
				}	
			}

			if(Pattern.matches("CMP", inst)){//If CMP
				int o1 = registers.get(op1);
				int o2 = registers.get(op2);
				int zf;

				o1 = o1 - o2;

				if (o1 == 0){
					zf = 1;
					System.out.println("\t\t zf: " + zf);
				}else {
					zf = 0;
					System.out.println("\t\t zf: " + zf);
				}	
			}

		}
		
		for(int i=1; i<=32; i++){
			String reg = "R" + i;
			
			System.out.println(reg + ": " + registers.get(reg));

		}
		
	}
	
}
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class Main{

	public static Stack<String[]> stacks = new Stack<String[]>();
	public static HashMap<String, Integer> registers = new HashMap<String, Integer>();
	
	public static void main(String[] args){
	
		for(int i=1; i<=32; i++){
			String reg = "R" + i;
			registers.put(reg, null);
		}
		registers.put("ZF",0);
		registers.put("OF",0);
		registers.put("NF",0);
		registers.put("MAR",0);		
		registers.put("MBR",0);
		registers.put("PC",0);		 
		 
		Stack<String[]> stacks = Parser.reader();
		
		for (String[] e : stacks){
			for (String s : e){
				System.out.println(s);
			}
			System.out.println();
		}
		
		int of = 0;

		for (String[] e : stacks){	//FETCH
			String inst = e[0];
			String op1 = e[1];
			String op2 = e[2];
			registers.put("PC",registers.get("PC")+1);

			//decode
			if(Pattern.matches("LOAD", inst)){//If LOAD
				int o2 = Integer.parseInt(op2);
				//execute
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

			else if(Pattern.matches("ADD", inst)){//If ADD
				if (registers.get(op1) == null || registers.get(op2) == null){
					System.out.println("Error");
					System.exit(0);
				}
				int o1 = registers.get(op1);
				int o2 = registers.get(op2);
				//execute

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

			else if(Pattern.matches("SUB", inst)){//If SUB
				if (registers.get(op1) == null || registers.get(op2) == null){
					System.out.println("Error");
					System.exit(0);
				}
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

			else if(Pattern.matches("CMP", inst)){//If CMP
				if (registers.get(op1) == null || registers.get(op2) == null){
					System.out.println("Error");
					System.exit(0);
				}
				int o1 = registers.get(op1);
				int o2 = registers.get(op2);
				int zf;
				int nf;
				o1 = o1 - o2;

				if (o1 == 0){
					zf = 1;
					System.out.println("\t\t zf: " + zf);
				}else {
					zf = 0;
					System.out.println("\t\t zf: " + zf);
				}
				if (o1 < 0){
					nf = 1;
					System.out.println("\t\t nf: " + nf);
				}else {
					nf = 0;
					System.out.println("\t\t nf: " + nf);
				}
				registers.replace("ZF",zf);
				registers.replace("NF",nf);
			}

		}
		
		for(int i=1; i<=32; i++){
			String reg = "R" + i;
			
			System.out.println(reg + ": " + registers.get(reg));

		}
		System.out.println("ZF: "+registers.get("ZF"));
		System.out.println("OF: "+registers.get("OF"));
		System.out.println("NF: "+registers.get("NF"));
		System.out.println("MAR: "+registers.get("MAR"));		
		System.out.println("MBR: "+registers.get("MBR"));
		System.out.println("PC: "+registers.get("PC"));	
	}
	
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Stack;
import java.util.regex.Pattern;

public class Parser{
	private static Stack<String[]> stacks = new Stack<String[]>();

	public static Stack<String[]> reader(){
		try{
			BufferedReader br = new BufferedReader(new FileReader("input.txt"));		//file reader for the input file
			String currentLine;

			while((currentLine = br.readLine()) != null) {								//the whole process of initialization of value by reading through the file
				if(currentLine.isEmpty()){
					continue;
				}
				String[] data = currentLine.split("\\s+");									//splits by character
				String[] inst = new String[3];

				for(String str:data){
					switch(str) {
					case "LOAD": System.out.println("LOAD");
								inst[0] = str;
								break;
							
					case "ADD": System.out.println("ADD");	
								inst[0] = str;
								break;
								
					case "SUB": System.out.println("SUB");	
								inst[0] = str;
								break;
								
					case "CMP": System.out.println("CMP");
								inst[0] = str;
								break;
								
					default: if(Pattern.matches("(.*)(,)(.*)", str)) {									//the character passed the regex test
								String[] operands = str.split(",");
								System.out.println("operand 1: " + operands[0] + "\noperand 2: " + operands[1]);
		
								if ((Object)inst[0] == null) {
									continue;
								} else {
									switch(inst[0]) {
									case "LOAD": if (Pattern.matches("^R\\d{1,2}",operands[0]) && Pattern.matches("-?\\d(\\d)?",operands[1])){						//immediate parser
													System.out.println("Valid");
												} else {
													System.out.println("Overflow"); 
													//System.exit(0);
												}
												inst[1] = operands[0];
												inst[2] = operands[1];
												stacks.push(inst);
												System.out.println("\n");
												break;
												
									case "ADD": validate(operands[0], operands[1], inst);
												break;
												
									case "SUB": validate(operands[0], operands[1], inst);
												break;
											
									case "CMP": validate(operands[0], operands[1], inst);
												break;
									}
								}
							} else {
								System.out.println("Invalid Instruction");
								System.exit(0);
							}
								
							
							/*
							if (Pattern.matches("\\d(\\d)?",operands[0]))						//immediate parser
								System.out.println("Immediate");
							else if (Pattern.matches("^R\\d{1,2}",operands[0]))
								System.out.println("Register");
							else
								System.out.println("Invalid operand");
							inst[2] = operands[1];
							if (Pattern.matches("\\d(\\d)?",operands[1]))						//immediate parser
								System.out.println("Immediate");
							else if (Pattern.matches("^R\\d{1,2}",operands[1]))
								System.out.println("Register");
							else
								System.out.println("Invalid operand");
						}	*/					

					} //switch
			} //for
		} //while
			br.close();	
					
		} catch(Exception e) {
			System.out.println(e);
		}
		
		return stacks;
	} //reader
	
	public static void validate(String operand1, String operand2, String[] inst) {
		if (Pattern.matches("^R\\d{1,2}",operand1) && Pattern.matches("^R\\d{1,2}",operand2)){						//immediate parser
			System.out.println("Valid");
		} else {
			System.out.println("Overflow"); 
			//System.exit(0);
		}
		inst[1] = operand1;
		inst[2] = operand2;
		stacks.push(inst);
		System.out.println("\n");		
	}
} //class
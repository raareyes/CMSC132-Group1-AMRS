import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class Parser{
	private static Stack<String[]> stacks = new Stack<String[]>();

	public static Stack<String[]> reader(){
		try{
			BufferedReader br = new BufferedReader(new FileReader("input.txt"));		//file reader for the input file
			String currentLine;

			while((currentLine = br.readLine()) != null){								//the whole process of initialization of value by reading through the file
				
				if(currentLine.isEmpty()){
					continue;
				}
				String[] data = currentLine.split(" ");									//splits by character
				String[] inst = new String[3];

				for(String str:data){
					if(Pattern.matches("LOAD", str)){									//the character passed the regex test
						System.out.println("LOAD");
						inst[0] = str;
					}

					else if(Pattern.matches("ADD", str)){									//the character passed the regex test
						System.out.println("ADD");	
						inst[0] = str;
					}

					else if(Pattern.matches("SUB", str)){									//the character passed the regex test
						System.out.println("SUB");
						inst[0] = str;
					}

					else if(Pattern.matches("CMP", str)){									//the character passed the regex test
						System.out.println("CMP");
						inst[0] = str;
					}

					else if(Pattern.matches("(.*)(,)(.*)", str)){									//the character passed the regex test
						String[] operands = str.split(",");
						System.out.println("operand 1: " + operands[0] + "\noperand 2: " + operands[1]);
						inst[1] = operands[0];
						inst[2] = operands[1];

						if(Pattern.matches("LOAD", inst[0])){
							if (Pattern.matches("^R\\d{1,2}",operands[0]) && Pattern.matches("-?\\d(\\d)?",operands[1])){						//immediate parser
								System.out.println("Valid");
							}
							else{
								System.out.println("Overflow"); 
								//System.exit(0);
							}
							System.out.println("\n");
							
						}

						if(Pattern.matches("ADD", inst[0])){
							if (Pattern.matches("^R\\d{1,2}",operands[0]) && Pattern.matches("^R\\d{1,2}",operands[1])){						//immediate parser
								System.out.println("Valid");
							}
							else{
								System.out.println("Overflow"); 
								//System.exit(0);
							}
							System.out.println("\n");
							
						}

						if(Pattern.matches("SUB", inst[0])){
							if (Pattern.matches("^R\\d{1,2}",operands[0]) && Pattern.matches("^R\\d{1,2}",operands[1])){						//immediate parser
								System.out.println("Valid");
							}
							else{
								System.out.println("Overflow"); 
								//System.exit(0);
							}
							System.out.println("\n");
							
						}

						if(Pattern.matches("CMP", inst[0])){
							if (Pattern.matches("^R\\d{1,2}",operands[0]) && Pattern.matches("^R\\d{1,2}",operands[1])){						//immediate parser
								System.out.println("Valid");
							}
							else{
								System.out.println("Overflow"); 
								//System.exit(0);
							}
							System.out.println("\n");
							
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
						*/					}

				}
				stacks.push(inst);
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
		
		return stacks;
	}
}
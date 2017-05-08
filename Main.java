
import java.util.HashMap;
import java.util.Stack;

public class Main{

	public static Stack<String[]> stacks = new Stack<String[]>();
	public static HashMap<String, Integer> registers = new HashMap<String, Integer>();
	public final static int MAX = 99, MIN = -99;
	public final static int LOAD = 0, ADD = 1, SUB = 2, CMP = 3;
	
	private static String inst, op1, op2;
	
	public static void main(String[] args){
		
		initRegisters();	 
		
		Stack<String[]> stacks = Parser.reader();
		
		for (String[] e : stacks){
			for (String s : e){
				System.out.println(s);
			}
			System.out.println();
		}
		
		SpecialRegisters sRegisters = new SpecialRegisters(stacks);
		sRegisters.initSRegisters();
		
		//cycle loop
		int[] values;
		int result;
		for (String[] e : stacks){
			System.out.println("\nInstruction: " + sRegisters.getPC());
			
			fetch(sRegisters);
			values = decode();
			result = execute(values, sRegisters);
			System.out.println(result);
			memoryAccess();
			writeBack(result, values[0]);
			
			sRegisters.printSRegisters();
			sRegisters.resetFlags();
			sRegisters.incPC();

		}
		//print registers
		System.out.println("");
		for(int i=1; i<=32; i++){
			String reg = "R" + i;
			
			System.out.println(reg + ": " + registers.get(reg));

		}
	}
	
	
	public static void initRegisters() {
		for(int i=1; i<=32; i++){
			String reg = "R" + i;
			registers.put(reg, null);
		}
	}
	
	public static void fetch(SpecialRegisters sRegisters) {
		sRegisters.setMAR(); //get address from PC to MAR
		String[] data = sRegisters.loadMBR(); //load instruction to MBR
		inst = data[0];
		op1 = data[1];
		op2 = data[2];
		System.out.println("Fetch: OK");
	}
	
	public static int[] decode() {
		int[] values = new int[3];
		
		switch(inst) {
		case "LOAD": values[0] = LOAD;
					 values[1] = LOAD;
					 values[2] = Integer.parseInt(op2);
					 //System.out.println("Load");
					 break;
					 
		case "ADD": values[0] = ADD;
					values[1] = registers.get(op1);
					values[2] = registers.get(op2);
					//System.out.println("Add");
					break;
					
		case "SUB": values[0] = SUB;
					values[1] = registers.get(op1);
					values[2] = registers.get(op2);
					break;
					
		case "CMP": values[0] = CMP;
					values[1] = registers.get(op1);
					values[2] = registers.get(op2);
					break;
		}
		
		System.out.println("Decode: OK");
		return values;
	}
	
	public static int execute(int[] values, SpecialRegisters sRegisters) {
		int result = 0;
		int opcode = values[0];
		int operand1 = values[1];
		int operand2 = values[2];
		
		if ((Object) operand1 == null || (Object) operand2 == null){
			System.out.println("Error: null operand");
			System.exit(0);
		}
		
		switch(opcode) {
		case LOAD:  result = operand2;
					result = checkOverflow(op1, result, sRegisters);
					break;
					
		case ADD:	result = operand1 + operand2;
					System.out.println(result + " " + operand1 + " " + operand2);
					result = checkOverflow(op1, result, sRegisters);
					break;
			
		case SUB: 	result = operand1 - operand2;
					result = checkOverflow(op1, result, sRegisters);
					break;
					
		case CMP:	result = operand1 - operand2;
					if (result == 0) sRegisters.setZF();			//check for zero flag
					else if (result < 0) sRegisters.setNF();				//check for negative flag
					break;
		}
		System.out.println("Execute: OK");
		return result;
	}
	
	public static void memoryAccess() {
		System.out.println("Memory Access: OK");
	}
	
	public static void writeBack(int result, int opcode) {
		if(opcode != 3) registers.replace(op1, result);
		System.out.println("Write Back: OK");
	}
	
	public static int checkOverflow(String op1, int result, SpecialRegisters sRegisters) {
		if (result > MAX){
			sRegisters.setOF();
			return 99;
		} else if (result < MIN) {
			sRegisters.setOF();
			return -99;
		}  else {
			return result;
		}
	}
}
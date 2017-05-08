
import java.util.HashMap;
import java.util.Stack;
import java.util.ArrayList;

public class Main{

	public static Stack<String[]> stacks = new Stack<String[]>();
	public static HashMap<String, Integer> registers = new HashMap<String, Integer>();
	public final static int MAX = 99, MIN = -99;
	public final static int LOAD = 0, ADD = 1, SUB = 2, CMP = 3;
	public static boolean[] stages = {false, false, false, false, false};
	private static ArrayList<Cycle> cycles = new ArrayList<Cycle>();
	private static int clockCycles = 0;
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
			cycles.add(new Cycle(sRegisters));
		}


		do{
		System.out.println("clockCycles: " + clockCycles);
			for (boolean t : stages){
				System.out.println(t);
			}
			System.out.println("\nInstruction: " + sRegisters.getPC());

			
			for (Cycle c : cycles){
				if (c.run()){
					cycles.remove(c);
				}
			}



			/*fetch(sRegisters);
			values = decode();
			result = execute(values, sRegisters);
			System.out.println(result);
			memoryAccess();
			writeBack(result, values[0]);*/
			clockCycles += 1;
			sRegisters.printSRegisters();
			sRegisters.resetFlags();
			sRegisters.incPC();

		}while (!(cycles.isEmpty()));
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
	
}
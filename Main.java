
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
	public static int doneCycles = 0;
	private static int clockCycles = 0;
	private static String inst, op1, op2;
	private static int stalls = 0;
	public static boolean stalled = false;
	
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

			/*
				Cycle cycler = new Cycle(sRegisters);
				cycler.fetch();
				cycles.add(cycler);
				clockCycles ++;
				sRegisters.incPC();*/
		do{
		System.out.println("\n==========================\n  || CLOCKCYCLES: " + clockCycles + " ||\n==========================\n");
			/*for (boolean t : stages){
				System.out.println(t);
			}*/
			//System.out.println("\nInstruction: " + sRegisters.getPC());

			
			for (Cycle c : cycles){
					//System.out.println("==========================\n");

				c.run();
					//System.out.println("==========================\n");
			}

			if (clockCycles < stacks.size()){
				Cycle cycle = new Cycle(sRegisters);
				cycle.fetch();
				cycles.add(cycle);
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
			if (stalled)
				stalls ++;
			stalled = false;

		}while (doneCycles < (cycles.size() + stalls + 1));
		//print registers
		System.out.println("\n");
		for(int i=1; i<=32; i++){
			String reg = "R" + i;
			
			System.out.println(reg + ": " + registers.get(reg));

		}
		System.out.println("\nStalls: " + stalls + "\n");
	}
	
	
	public static void initRegisters() {
		for(int i=1; i<=32; i++){
			String reg = "R" + i;
			registers.put(reg, null);
		}
	}
	
}
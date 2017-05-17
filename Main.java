
import java.util.HashMap;
import java.util.Stack;
import java.util.ArrayList;

public class Main{

	public static HashMap<String, Integer> registers = new HashMap<String, Integer>();
	public static ArrayList<String[]> dependencyList = new ArrayList<String[]>();
	public static boolean[] stages = {false, false, false, false, false};
	public static int doneCycles = 0;

	public final static int LOAD = 0, ADD = 1, SUB = 2, CMP = 3;
	public final static int MAX = 99, MIN = -99;

	public static ArrayList<Cycle> cycles = new ArrayList<Cycle>();
	public static boolean stalled = false;
	public static SpecialRegisters sRegisters;

	private static int clockCycles = 0;
	private static int stalls = 0;


	public static void main(String[] args){

		initRegisters();

		Stack<String[]> stacks = Parser.reader();

		for (String[] e : stacks){
			for (String s : e){
				System.out.println(s);
			}
			System.out.println();
		}

		sRegisters = new SpecialRegisters(stacks);
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
			System.out.println("\n==========================\nCLOCK CYCLE: " + (clockCycles));
			/*for (boolean t : stages){
				System.out.println(t);
			}*/

			for (Cycle c : cycles){
					System.out.println("**************************\n\tINSTRUCTION: " + c.id);
				c.run();
					System.out.println("**************************\n");
			}

			if (clockCycles < stacks.size()){
				Cycle cycle = new Cycle(clockCycles);
				cycle.fetch();
				cycle.generateDependencyList();
				cycles.add(cycle);
				sRegisters.incPC();
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

			if (stalled)
				stalls ++;
			stalled = false;
			System.out.println("\n==========================");
		}while (doneCycles < cycles.size());
		//print registers
		printRegisters();
		printDependencies();
		System.out.println("Stalls: " + stalls);
	}

	public static void initRegisters() {
		for(int i=1; i<=32; i++){
			String reg = "R" + i;
			registers.put(reg, null);
		}
	}

	public static void printRegisters() {
		System.out.println("");
		for(int i=1; i<=32; i++){
			String reg = "R" + i;
			if(registers.get(reg) != null) System.out.println(reg + ": " + registers.get(reg));
		}
	}

	public static void printDependencies() {
		if(dependencyList.isEmpty()) {
			System.out.println("No dependencies!");
		} else {
			for(String[] s : dependencyList) {
				System.out.println(s[0] + " & " + s[1] + " | " + s[2] + " | " + s[3]);
			}
		}
	}

}

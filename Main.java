
import java.util.HashMap;
import java.util.Stack;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.*;

public class Main{

	public static HashMap<String, Integer> registers = new HashMap<String, Integer>();
	public static ArrayList<String[]> dependencyList = new ArrayList<String[]>();
	public static ArrayList<ArrayList<Character>> pipeLinePrint = new ArrayList<ArrayList<Character>>();
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

		JFrame frame = new JFrame("CMSC 132");
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(1200,375));
		Container framepane = frame.getContentPane();

		JTextArea cycs = new JTextArea();
		cycs.setEditable(false);
		JScrollPane cycpane = new JScrollPane(cycs);
		cycpane.setPreferredSize(new Dimension (400, 300));
		
		JTextArea regs = new JTextArea();
		regs.setEditable(false);
		JScrollPane regpane = new JScrollPane(regs);
		regpane.setPreferredSize(new Dimension (400, 300));
		
		JTextArea stats = new JTextArea();
		stats.setEditable(false);
		JScrollPane statspane = new JScrollPane(stats);
		statspane.setPreferredSize(new Dimension (400, 300));

		framepane.add(cycpane, BorderLayout.WEST);
		framepane.add(regpane, BorderLayout.CENTER);
		framepane.add(statspane, BorderLayout.EAST);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		//cycle loop
		int[] values;
		int result;
		do{

			System.out.println("\n==========================\nCLOCK CYCLE: " + (clockCycles));


			for (Cycle c : cycles){
					System.out.println("**************************\n\tINSTRUCTION: " + c.id);
				c.run();
					System.out.println("**************************\n");
			}

			if (clockCycles < stacks.size()){
				Cycle cycle = new Cycle(clockCycles);
				cycle.fetch();
				ArrayList<Character> temp = new ArrayList<Character>();
				temp.add('F');
				pipeLinePrint.add(temp);
				cycle.generateDependencyList();
				cycles.add(cycle);
				sRegisters.incPC();
			}


			clockCycles += 1;

			sRegisters.printSRegisters();
			sRegisters.resetFlags();

			if (stalled)
				stalls ++;
			stalled = false;
			System.out.println("\n==========================");

			cycs.setText(null);
			regs.setText(null);
			stats.setText(null);
			printPipeline(cycs);
			printRegs(regs);
			printStats(stats);
			try{
				Thread.sleep(1000);
			}
			catch(Exception e){}
		}while (doneCycles < cycles.size());

		stats.setText(null);
		printStats(stats);

		double cpi = ((clockCycles-1)*1.0) / stacks.size();
		stats.append("\nCPI: \n" + cpi );

		//print registers
		printRegisters();
		printDependencies();
		System.out.println("Stalls: " + stalls);

		for (int j =0;j<pipeLinePrint.size();j++){
			System.out.println("");
			for (int i=0;i<j*2;i++)
				System.out.print(" ");
			for (Character c : pipeLinePrint.get(j)){
				System.out.print(c + " ");
			}
		}
		System.out.println("");


	}

	public static void printPipeline(JTextArea cycs) {
		cycs.setTabSize(2);
		cycs.append("Pipeline:\n\n");
		for (int j =0;j<pipeLinePrint.size();j++){
			cycs.append("\n");
			for (int i=0;i<j;i++)
				cycs.append("\t");
			for (Character c : pipeLinePrint.get(j)){
				cycs.append(c + "\t");
			}
		}
	}

	public static void printRegs(JTextArea stats) {
		stats.append("Registers\n\n");
		for(int i=1; i<=32; i++){
			String reg = "R" + i;
			stats.append(reg + ": " + registers.get(reg) + "\n");
		}
	}

	public static void printStats(JTextArea stats) {
		stats.append("Statistics\n\n");
		stats.append("Execution: \n");
		stats.append(clockCycles-1 + " Cycles\n");
		stats.append(pipeLinePrint.size() + " Instructions\n");
		stats.append(stalls + " Stalls \n");
		stats.append("\nDependencies: \n");
		if(dependencyList.isEmpty()) {
			stats.append("No dependencies!\n");
		} else {
			for(String[] s : dependencyList) {
				stats.append(s[0] + " & " + s[1] + " | " + s[2] + " | " + s[3] + "\n");
			}
		}
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

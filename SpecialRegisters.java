import java.util.Stack;

public class SpecialRegisters {
	private Stack<String[]> stacks = new Stack<String[]>();
	private int PC;
	private int MAR;
	private String[] MBR;
	private int OF;
	private int ZF;
	private int NF;

	public SpecialRegisters(Stack<String[]> stacks) {
		this.stacks = stacks;
	}

	public void initSRegisters() {
		PC = 0;
		MBR = new String[3];
		OF = 0;
		ZF = 0;
		NF = 0;
	}

	public void setMAR() {
		MAR = PC;
	}

	public String[] loadMBR() {
		MBR = stacks.get(MAR);
		return MBR;
	}

	public int getPC() {
		return PC;
	}

	public void incPC() {
		PC++;
	}

	public void setOF() {
		OF++;
	}

	public void setZF() {
		ZF++;
	}

	public void setNF() {
		NF++;
	}

	public void resetFlags() {
		OF = 0;
		ZF = 0;
		NF = 0;
	}

	public void printSRegisters() {
		System.out.println("\n\tPC: " + PC);
		System.out.println("\tMAR: " + MAR);
		System.out.println("\tMBR: " + MBR[0] + " " + MBR[1] + "," + MBR[2]);
		System.out.println("\tOF: " + OF);
		System.out.println("\tZF: " + ZF);
		System.out.println("\tNF: " + NF);
	}


}

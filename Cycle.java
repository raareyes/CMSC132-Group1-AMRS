
import java.util.Stack;
public class Cycle {
	public final static char FETCH = 'F';
	public final static char DECODE = 'D';
	public final static char EXECUTE = 'E';
	public final static char MEMACCESS = 'M';
	public final static char WRITEBACK = 'W';
	public final int id;

	private String inst, op1, op2;
	private int[] values;
	private int result;
	private boolean isFinished = false;

	public char stage;


	public Cycle (int id){
		this.stage = FETCH;
		this.id = id;
	}

	public void fetch() {
		Main.sRegisters.setMAR(); //get address from PC to MAR
		String[] data = Main.sRegisters.loadMBR(); //load instruction to MBR
		this.inst = data[0];
		this.op1 = data[1];
		this.op2 = data[2];
		System.out.println("**************************\n\tINSTRUCTION: " + id);
		System.out.println("Stage: " + this.stage);
		System.out.println("Opcode: " + this.inst);
		System.out.println("Operand 1: " + this.op1);
		System.out.println("Operand 2: " + this.op2);
		System.out.println("\nFetch: OK");

		System.out.println("**************************");

		this.stage = DECODE;
		//create new process thread
	}

	public void stall(){
		System.out.println("\nStalling...");
		Main.stalled = true;

	}

	public void run() {
		if(stage != 'Q') {
			System.out.println("\nStage: " + this.stage);
			System.out.println("Opcode: " + this.inst);
			System.out.println("Operand 1: " + this.op1);
			System.out.println("Operand 2: " + this.op2);
		}

        switch (this.stage){
        	case FETCH:
        		if (Main.stages[0]){
        			stall();
        		}
        		else{
        			Main.stages[0] = true;
        			fetch();
        		}
        	break;
        	case DECODE:
        		if (Main.stages[1]  || checkDependency()){
        			stall();
        		}
				else{
					Main.stages[0] = false;
        			Main.stages[1] = true;
					decode();
				}
        	break;
        	case EXECUTE:
        		if (Main.stages[2]){
        			stall();
        		}
        		else{
					Main.stages[1] = false;
        			Main.stages[2] = true;
        			execute();
        		}
        	break;
        	case MEMACCESS:
        		if (Main.stages[3]){
        			stall();
        		}
        		else{
					Main.stages[2] = false;
        			Main.stages[3] = true;
        			memoryAccess();
        		}
        	break;
        	case WRITEBACK:
        		if (Main.stages[4]){
        			stall();
        		}
        		else{
					Main.stages[3] = false;
        			Main.stages[4] = true;
        			writeBack(this.values[0]);
        		}
        	break;
        	case 'R':
				Main.stages[4] = false;
        		Main.doneCycles ++;
        		this.stage = 'Q';
        		isFinished = true;
        	break;
        	default:
        		break;
        }

        //System.out.println("\nStage: " + this.stage);
		System.out.println("\tInstruction: " + this.inst);
		System.out.println("\tOp1: " + this.op1);
		System.out.println("\tOp2: " + this.op2);
    }

	public void decode() {
		this.values = new int[3];
		switch(inst) {
		case "LOAD": values[0] = Main.LOAD;
					 values[1] = Main.LOAD;
					 values[2] = Integer.parseInt(op2);
					 //System.out.println("Load");
					 break;

		case "ADD":
					if (Main.registers.get(op1) == null || Main.registers.get(op2) == null){
						stall();

						Main.stages[1] = false;
						return;
					}
					values[0] = Main.ADD;
					values[1] = Main.registers.get(op1);
					values[2] = Main.registers.get(op2);
					//System.out.println("Add");
					break;

		case "SUB":
					if (Main.registers.get(op1) == null || Main.registers.get(op2) == null){
						stall();

						Main.stages[1] = false;
						return;
					}
					values[0] = Main.SUB;
					values[1] = Main.registers.get(op1);
					values[2] = Main.registers.get(op2);
					break;

		case "CMP":
					if (Main.registers.get(op1) == null || Main.registers.get(op2) == null){
						stall();

						Main.stages[1] = false;
						return;
					}
					values[0] = Main.CMP;
					values[1] = Main.registers.get(op1);
					values[2] = Main.registers.get(op2);
					break;
		}

		System.out.println("\nDecode: OK");
		this.stage = EXECUTE;

	}

	public void execute() {
		this.result = 0;
		int opcode = values[0];
		int operand1 = values[1];
		int operand2 = values[2];

		if ((Object) operand1 == null || (Object) operand2 == null){
			System.out.println("Error: null operand");
			System.exit(0);
		}

		switch(opcode) {
		case Main.LOAD:  this.result = operand2;
					this.result = checkOverflow(op1, this.result);
					break;

		case Main.ADD:	this.result = operand1 + operand2;
					this.result = checkOverflow(op1, this.result);
					break;

		case Main.SUB: 	this.result = operand1 - operand2;
					this.result = checkOverflow(op1, this.result);
					break;

		case Main.CMP:	this.result = operand1 - operand2;
					if (this.result == 0) Main.sRegisters.setZF();			//check for zero flag
					else if (this.result < 0) Main.sRegisters.setNF();				//check for negative flag
					break;
		}
		System.out.println("\nExecute: OK");
		this.stage = MEMACCESS;
	}

	public void memoryAccess() {
		System.out.println("\nMemory Access: OK");
		this.stage = WRITEBACK;
	}

	public void writeBack(int opcode) {
		if(opcode != 3) Main.registers.replace(op1, this.result);
		this.stage = 'R';
		System.out.println("\nWrite Back: OK");
	}


	public int checkOverflow(String op1, int result) {
		if (result > Main.MAX){
			Main.sRegisters.setOF();
			return 99;
		} else if (result < Main.MIN) {
			Main.sRegisters.setOF();
			return -99;
		}  else {
			return result;
		}
	}
	public void generateDependencyList() {
		String dStore[] = new String[4];
		int i = 0;
		for(Cycle c : Main.cycles) {
			if(c.id == id) {
				return;
			} else {
				if(c.op1.equals(this.op1)) { //WAW
					dStore[0] = Integer.toString(id);
					dStore[1] = Integer.toString(i);
					dStore[2] = op1;
					dStore[3] = "WAW";
					if(!Main.dependencyList.contains(dStore)) {
						Main.dependencyList.add(dStore);
					}
					System.out.println("Dependency Added");
				} else if(c.op2.equals(this.op1)) { //WAR
					dStore[0] = Integer.toString(id);
					dStore[1] = Integer.toString(i);
					dStore[2] = op1;
					dStore[3] = "WAR";
					Main.dependencyList.add(dStore);
					if(!Main.dependencyList.contains(dStore)) {
						Main.dependencyList.add(dStore);
					}
				} else if(c.op1.equals(this.op2)) { //RAW
					dStore[0] = Integer.toString(id);
					dStore[1] = Integer.toString(i);
					dStore[2] = op2;
					dStore[3] = "RAW";
					if(!Main.dependencyList.contains(dStore)) {
						Main.dependencyList.add(dStore);
					}
					System.out.println("Dependency Added");
				}
			}
			i++;
		}
	}

	public boolean checkDependency() {
		for (String[] d : Main.dependencyList) {
			int dependencyId;

			if(id == Integer.parseInt(d[0])) {
				dependencyId = Integer.parseInt(d[1]);
			} //else if(id == Integer.parseInt(d[1])) {
				//dependencyId = Integer.parseInt(d[0]);
			//}
			 else {
				dependencyId = -1;
			}

			if(dependencyId >= 0)  {
    			for(Cycle c : Main.cycles) {
					if(c.id == dependencyId) {
						if(!c.isFinished || c.stage == 'F' || c.stage == 'D' || c.stage == 'E' || c.stage == 'M' || c.stage == 'W') {
							return  true;
						}
					}
				}
			}

		}
		return false;
	}

}

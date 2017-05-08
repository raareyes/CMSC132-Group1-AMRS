
import java.util.Stack;
public class Cycle {
	public static char stage;
	final static char FETCH = 'F';
	final static char DECODE = 'D';
	final static char EXECUTE = 'E';
	final static char MEMACCESS = 'M';
	final static char WRITEBACK = 'W';
	private SpecialRegisters sRegisters;
	private String inst, op1, op2;
	private int[] values;
	private int result;


	public Cycle (SpecialRegisters sRegisters){
		this.stage = FETCH;
		this.sRegisters = sRegisters;
	}

	public void fetch() {
		this.sRegisters.setMAR(); //get address from PC to MAR
		String[] data = this.sRegisters.loadMBR(); //load instruction to MBR
		this.inst = data[0];
		this.op1 = data[1];
		this.op2 = data[2];
		System.out.println("Fetch: OK");
		this.stage = DECODE;
		//create new process thread
	}

	public boolean run() {
		System.out.println(this.stage);
		System.out.println(this.inst);
		System.out.println(this.op1);
		System.out.println(this.op2);
        switch (this.stage){
        	case FETCH:
        		if (Main.stages[0]){
        			//stall();
        			return false;
        		}
        		else{
        			Main.stages[0] = true;
        			fetch();
					return false;
        		}
        	case DECODE:
        		if (Main.stages[1]){
        			//stall();
        			return false;
        		}
				else{
					Main.stages[0] = false;
        			Main.stages[1] = true;
					decode();
					return false;
				}
        	case EXECUTE:
        		if (Main.stages[2]){
        			//stall();
        			return false;
        		}
        		else{
					Main.stages[1] = false;
        			Main.stages[2] = true;
        			execute();
        			return false;
        		}
        	case MEMACCESS:
        		if (Main.stages[3]){
        			//stall();
        			return false;
        		}
        		else{
					Main.stages[2] = false;
        			Main.stages[3] = true;
        			memoryAccess();
        			return false;
        		}
        	case WRITEBACK:
        		if (Main.stages[4]){
        			//stall();
        			return false;
        		}
        		else{
					Main.stages[3] = false;
        			Main.stages[4] = true;
        			writeBack(this.values[0]);
        			return false;
        		}
        	default:
				Main.stages[4] = false;
        		return true;
        }
    }

	public void decode() {
		this.values = new int[3];
		
		switch(inst) {
		case "LOAD": values[0] = Main.LOAD;
					 values[1] = Main.LOAD;
					 values[2] = Integer.parseInt(op2);
					 //System.out.println("Load");
					 break;
					 
		case "ADD": values[0] = Main.ADD;
					values[1] = Main.registers.get(op1);
					values[2] = Main.registers.get(op2);
					//System.out.println("Add");
					break;
					
		case "SUB": values[0] = Main.SUB;
					values[1] = Main.registers.get(op1);
					values[2] = Main.registers.get(op2);
					break;
					
		case "CMP": values[0] = Main.CMP;
					values[1] = Main.registers.get(op1);
					values[2] = Main.registers.get(op2);
					break;
		}
		
		System.out.println("Decode: OK");
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
					this.result = checkOverflow(op1, this.result, this.sRegisters);
					break;
					
		case Main.ADD:	this.result = operand1 + operand2;
					System.out.println(this.result + " " + operand1 + " " + operand2);
					this.result = checkOverflow(op1, this.result, this.sRegisters);
					break;
			
		case Main.SUB: 	this.result = operand1 - operand2;
					this.result = checkOverflow(op1, this.result, this.sRegisters);
					break;
					
		case Main.CMP:	this.result = operand1 - operand2;
					if (this.result == 0) this.sRegisters.setZF();			//check for zero flag
					else if (this.result < 0) this.sRegisters.setNF();				//check for negative flag
					break;
		}
		System.out.println("Execute: OK");
		this.stage = MEMACCESS;
	}
	
	public void memoryAccess() {
		System.out.println("Memory Access: OK");
		this.stage = WRITEBACK;
	}
	
	public void writeBack(int opcode) {
		if(opcode != 3) Main.registers.replace(op1, this.result);
		System.out.println("Write Back: OK");
	}


	public int checkOverflow(String op1, int result, SpecialRegisters sRegisters) {
		if (result > Main.MAX){
			sRegisters.setOF();
			return 99;
		} else if (result < Main.MIN) {
			sRegisters.setOF();
			return -99;
		}  else {
			return result;
		}
	}
}
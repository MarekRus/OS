package masina;

import java.util.ArrayList;


public class Proc {

	
	public static String convIntToHexStr(int x, int length) {
		String word = Integer.toHexString(x);
		while(word.length() < length) {
			word = "0" + word;
		}
		return word;
	}
	
	
	public static int convHexStrToInt(String word) {
		return (int)Long.parseLong(word, 16);
	}
	
	
	public static ArrayList<String> errorMessages;
	
	static {
		errorMessages = new ArrayList(12);
		errorMessages.add(0, "Jokios klaidos neivyko");
		errorMessages.add(1, "Neatpazinta komanda");
		errorMessages.add(2, "Buvo bandyta irasyti reiksme i steka, kai stekas pilnas");
		errorMessages.add(3, "Buvo bandyta nuskaityti komanda, kai IC uzeina uz komandu segmenta (CS)");
		errorMessages.add(4, "IC registre neteisingas adresas.");
		errorMessages.add(5, "Pritruko atminties.");
		errorMessages.add(6, "Programos pabaigoje nera komandos HALT.");
		errorMessages.add(7, "Neteisingas steko dydis.");
		errorMessages.add(8, "Buvo pabandyta nuskaityti is ir/arba sumazinti steko virsune, kai stekas tuscias.");
		errorMessages.add(9, "Per mazai zodziu steke operacijai atlikti.");
		errorMessages.add(10, "Iskvietus komanda PADNUM steko virsuneje esantis zodis nera skaicius arba jis yra per didelis.");
		errorMessages.add(11, "Dalyba is nulio.");
	}
	
	
	
	private int[] registers;
	
	static final int IC = 0;
	static final int R = 1;
	static final int PTR = 2;
	static final int MODE = 3;
	static final int SF = 4;
	static final int SP = 5;
	static final int CS = 6;
	static final int DS = 7;
	static final int SS = 8;
	static final int PI = 9;
	static final int SI = 10;
	static final int TI = 11;
	
	
	public static void printMemBlock(String[] memory) {
		
		int ic = convHexStrToInt(memory[IC]);
		int cs = convHexStrToInt(memory[CS]);
		
		int currentBlock = (ic / 255) + cs;	
		
		for(int i = 0; i < 32; i++) {
			
			for(int g = 0; g < 8; g++) {
				System.out.print(memory[currentBlock*256 + i*8 + g] + " ");
			}
			System.out.println();
			
		}
		
	}
	
	
	public static void printInfo(String[] memory) {
		
		int ic = convHexStrToInt(memory[IC]);
		int cs = convHexStrToInt(memory[CS]);
		
		System.out.println("------------------------------------------------");
		
		System.out.print("Dabartine komanda: ");
		System.out.print(memory[cs*256 + ic]);
		System.out.print(" Sekanti komanda: ");
		System.out.println(memory[cs*256 + ic + 1]);
		
		System.out.println("------------------------------------------------");
		
		
	}
	
	
	public static String[] run(String[] memory) {
		
		int ic = convHexStrToInt(memory[IC]);
		int cs = convHexStrToInt(memory[CS]);
		
		String currentCommand = memory[cs*256 + ic];
		
		while(!currentCommand.equals("HALT0000")){
			memory = step(memory);
			ic = convHexStrToInt(memory[IC]);
			currentCommand = memory[cs*256 + ic];
			
			//System.out.println("Current Command: " + currentCommand);
		}
		
		ic = convHexStrToInt(memory[IC]) + 1;
		memory[IC] = convIntToHexStr(ic,8);
		
		return memory;
		
	}
	
	
	
	
	
	public static String[] step(String[] memory) {
		
		int ic = convHexStrToInt(memory[IC]);
		int cs = convHexStrToInt(memory[CS]);
		int ds = convHexStrToInt(memory[DS]);
		int ss = convHexStrToInt(memory[SS]);
		int sp = convHexStrToInt(memory[SP]);
		int r = convHexStrToInt(memory[R]);
		
		String command = memory[cs*256 + ic];
		
		//System.out.println(command);
		
		
		if(command.substring(0, 3).equals("ADD")) {
			
			int sum = convHexStrToInt(memory[ss*256 + sp]) + convHexStrToInt(memory[ss*256 + sp + 1]);
			memory[ss*256 + sp] = convIntToHexStr(sum,8);
			
			ic++;
			memory[IC] = convIntToHexStr(ic, 8);
			
		} else if(command.substring(0, 3).equals("SUB")) {
			
			int sub = convHexStrToInt(memory[ss*256 + sp + 1]) - convHexStrToInt(memory[ss*256 + sp]);
			memory[ss*256 + sp] = convIntToHexStr(sub,8);
			
			ic++;
			memory[IC] = convIntToHexStr(ic, 8);
			
		} else if(command.substring(0, 3).equals("DIV")) {
			
			int div = convHexStrToInt(memory[ss*256 + sp + 1]) / convHexStrToInt(memory[ss*256 + sp]);
			memory[ss*256 + sp] = convIntToHexStr(div,8);
			
			ic++;
			memory[IC] = convIntToHexStr(ic, 8);
			
		} else if(command.substring(0, 3).equals("MUL")) {
			
			int mul = convHexStrToInt(memory[ss*256 + sp + 1]) * convHexStrToInt(memory[ss*256 + sp]);
			memory[ss*256 + sp] = convIntToHexStr(mul,8);
			
			ic++;
			memory[IC] = convIntToHexStr(ic, 8);
			
		} else if(command.substring(0, 2).equals("KR")) {
				
			int address = convHexStrToInt(command.substring(2,8));
			int temp = convHexStrToInt(memory[ds*256 + address]);
			memory[R] = convIntToHexStr(temp,8);
			
			ic++;
			memory[IC] = convIntToHexStr(ic, 8);
			
		} else if(command.substring(0, 2).equals("SR")) {
			
			int address = convHexStrToInt(command.substring(2,8));
			int temp = convHexStrToInt(memory[R]);
			memory[ds*256 + address] = convIntToHexStr(temp,8);
			
			ic++;
			memory[IC] = convIntToHexStr(ic, 8);
			
		} else if(command.substring(0, 4).equals("PUSH")) {
			
			sp = sp - 1;
			memory[SP] = convIntToHexStr(sp, 8);
			memory[ss*256 + sp] = convIntToHexStr(r, 8);	
			
			ic++;
			memory[IC] = convIntToHexStr(ic, 8);
			
		} else if(command.substring(0, 3).equals("POP")) {
			
			r = convHexStrToInt(memory[ss*256 + sp]);
			memory[R] = convIntToHexStr(r, 8);
			sp = sp + 1;
			memory[SP] = convIntToHexStr(sp, 8);
			
			ic++;
			memory[IC] = convIntToHexStr(ic, 8);
			
		} else if(command.substring(0, 3).equals("CMP")) {
			
			
			int tempR = convHexStrToInt(memory[R]);
			int tempStack = convHexStrToInt(memory[ss*256 + sp]);
			int tempSF = convHexStrToInt(memory[SF]);
			
			int result = tempR - tempStack;
			
			if(result == 0) {
				tempSF = 10;
			} else if(result < 0) {
				tempSF = 1;
			} else if(result > 0) {
				tempSF = 0;
			}
			
			memory[SF] = convIntToHexStr(tempSF,8);
			
			ic++;
			memory[IC] = convIntToHexStr(ic, 8);
			
		} else if(command.substring(0, 2).equals("PD")) {
			
			int temp = convHexStrToInt(memory[R]);
			System.out.println(memory[R]);
			
			ic++;
			memory[IC] = convIntToHexStr(ic, 8);
			
		} else if(command.substring(0, 2).equals("RD")) {
			
			int temp = convHexStrToInt(command.substring(2,8));	
			memory[R] = convIntToHexStr(temp,8);	
			//System.out.println(temp);
			
			ic++;
			memory[IC] = convIntToHexStr(ic, 8);
	
		} else if(command.substring(0, 2).equals("JM")) {
			
			int address = convHexStrToInt(command.substring(2,8));
			memory[IC] = convIntToHexStr(address,8);
			
		} else if(command.substring(0, 2).equals("JZ")) {
			
			int address = convHexStrToInt(command.substring(2,8));
			int tempSF = convHexStrToInt(memory[SF]);
			
			if(tempSF/10 == 1) {
				memory[IC] = convIntToHexStr(address,8);
			}
			
		} else if(command.substring(0, 2).equals("JN")) {
			
			int address = convHexStrToInt(command.substring(2,8));
			int tempSF = convHexStrToInt(memory[SF]);
			
			if(tempSF/10 == 0) {
				memory[IC] = convIntToHexStr(address,8);
			}
			
		} else if(command.substring(0, 2).equals("JB")) {
			
			int address = convHexStrToInt(command.substring(2,8));
			int tempSF = convHexStrToInt(memory[SF]);
			
			if((tempSF/10 == 0) && (tempSF%10 == 1)) {
				memory[IC] = convIntToHexStr(address,8);
			}
			
			
		} else if(command.substring(0, 2).equals("JA")) {
			
			int address = convHexStrToInt(command.substring(2,8));
			int tempSF = convHexStrToInt(memory[SF]);
			
			if((tempSF/10 == 0) && (tempSF%10 == 0)) {
				memory[IC] = convIntToHexStr(address,8);
			}
			
		} 
		
		return memory;
		
	}
	
	
	
	
	
	
	
}

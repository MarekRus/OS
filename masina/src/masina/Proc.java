package masina;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
	
	
	
	public static void incrTimer(String[] supervisorMem, String[] memory) {
		int ti = convHexStrToInt(supervisorMem[TI]);
		ti++;
		if(ti > 2) {
			ti = 0;
			test(supervisorMem, memory);
		}
		supervisorMem[TI] = convIntToHexStr(ti,8);
	}
	
	
	
	public static void test(String[] supervisorMem, String[] memory) {
		
		printWord(memory, supervisorMem, 9);
		
		//System.out.println("Ivyko pertraukimas!!!");
	}
	
	
	
	public static void printMemBlock(String[] memory, String[] supervisorMem) {
		
		int ic = convHexStrToInt(supervisorMem[IC]);
		int cs = convHexStrToInt(supervisorMem[CS]);
		
		int currentBlock = (ic / 255) + cs;	
		
		for(int i = 0; i < 32; i++) {
			for(int g = 0; g < 8; g++) {
				System.out.print(memory[currentBlock*256 + i*8 + g] + " ");
			}
			System.out.println();
		}
	}
	
	
	
	public static void initDS(String[] memory, String[] supervisorMem) {
		
		int ds = convHexStrToInt(supervisorMem[DS]);
		
		memory[ds * 256] = "Welcome0";
		memory[ds * 256 + 1] = "Iveskite";
		memory[ds * 256 + 2] = " komanda";
		memory[ds * 256 + 3] = "Programa";
		memory[ds * 256 + 4] = " sekming";
		memory[ds * 256 + 5] = "ai ikrau";
		memory[ds * 256 + 6] = "ta000000";
		memory[ds * 256 + 7] = "Rezultat";
		memory[ds * 256 + 8] = "as: 0000";
		memory[ds * 256 + 9] = "Ivyko pe";
		memory[ds * 256 + 10] = "rtraukim";
		memory[ds * 256 + 11] = "as!!!000";
	}
	
	
	
	public static void printWord(String[] memory, String[] supervisorMem, int i) {
		
		int ds = convHexStrToInt(supervisorMem[DS]);
		
		if(i == 0) {
			System.out.println(memory[ds * 256].substring(0, 7));
		} else if(i == 1) {
			System.out.print(memory[ds * 256 + 1]);
			System.out.println(memory[ds * 256 + 2]);
		} else if(i == 3) {
			System.out.print(memory[ds * 256 + 3]);
			System.out.print(memory[ds * 256 + 4]);
			System.out.print(memory[ds * 256 + 5]);
			System.out.println(memory[ds * 256 + 6].substring(0, 2));
		} else if(i == 7) {
			System.out.print(memory[ds * 256 + 7]);
			System.out.println(memory[ds * 256 + 8].substring(0, 4));
		} else if(i == 9) {
			System.out.print(memory[ds * 256 + 9]);
			System.out.print(memory[ds * 256 + 10]);
			System.out.println(memory[ds * 256 + 11].substring(0, 5));
		}
	}
	
	
	
	public static void printDSBlock(String[] memory, String[] supervisorMem, int blockNr) {
		
		int ds = convHexStrToInt(supervisorMem[DS]);	
		int currentBlock = ds;	
		
		for(int i = 0; i < 32; i++) {	
			for(int g = 0; g < 8; g++) {
				System.out.print(memory[currentBlock*256 + i*8 + g] + " ");
			}
			System.out.println();	
		}	
	}
	
	
	
	public static void printInfo(String[] memory, String[] supervisorMem) {
		
		int ic = convHexStrToInt(supervisorMem[IC]);
		int cs = convHexStrToInt(supervisorMem[CS]);
		
		System.out.println("-------------------------------------------------------------------------");
		System.out.print("IC: " + supervisorMem[IC] + " | " + "R: " + supervisorMem[R] + " | " + "PTR: " + supervisorMem[PTR] + " | " + "SP: " + supervisorMem[SP] 
				+ " | " + "TI: " + supervisorMem[TI]);
		System.out.println();
		System.out.print("Dabartine komanda: ");
		System.out.print(memory[cs*256 + ic]);
		System.out.print(" Sekanti komanda: ");
		System.out.println(memory[cs*256 + ic + 1]);
		
		System.out.println("-------------------------------------------------------------------------");
	}
	
	
	
	public static void run(String[] memory, String[] supervisorMem) {
		
		int ic = convHexStrToInt(supervisorMem[IC]);
		int cs = convHexStrToInt(supervisorMem[CS]);
		
		String currentCommand = memory[cs*256 + ic];
		
		while(!currentCommand.equals("HALT0000")){
			step(memory, supervisorMem);
			ic = convHexStrToInt(supervisorMem[IC]);
			currentCommand = memory[cs*256 + ic];
		}
		
		ic = convHexStrToInt(supervisorMem[IC]) + 1;
		supervisorMem[IC] = convIntToHexStr(ic,8);
	}
	
	
	
	public static void step(String[] memory, String[] supervisorMem) {
		
		int ic = convHexStrToInt(supervisorMem[IC]);
		int cs = convHexStrToInt(supervisorMem[CS]);
		int ds = convHexStrToInt(supervisorMem[DS]);
		int ss = convHexStrToInt(supervisorMem[SS]);
		int sp = convHexStrToInt(supervisorMem[SP]);
		int r = convHexStrToInt(supervisorMem[R]);
		
		String command = memory[cs*256 + ic];
		
		if(command.substring(0, 3).equals("ADD")) {
			
			int sum = convHexStrToInt(memory[ss*256 + sp]) + convHexStrToInt(memory[ss*256 + sp + 1]);
			memory[ss*256 + sp] = convIntToHexStr(sum,8);
			
			incrTimer(supervisorMem, memory);
			
			ic++;
			supervisorMem[IC] = convIntToHexStr(ic, 8);
			
		} else if(command.substring(0, 3).equals("SUB")) {
			
			int sub = convHexStrToInt(memory[ss*256 + sp + 1]) - convHexStrToInt(memory[ss*256 + sp]);
			memory[ss*256 + sp] = convIntToHexStr(sub,8);
			
			incrTimer(supervisorMem, memory);
			
			ic++;
			supervisorMem[IC] = convIntToHexStr(ic, 8);
			
		} else if(command.substring(0, 3).equals("DIV")) {
			
			int div = convHexStrToInt(memory[ss*256 + sp + 1]) / convHexStrToInt(memory[ss*256 + sp]);
			memory[ss*256 + sp] = convIntToHexStr(div,8);
			
			incrTimer(supervisorMem, memory);
			
			ic++;
			supervisorMem[IC] = convIntToHexStr(ic, 8);
			
		} else if(command.substring(0, 3).equals("MUL")) {
			
			int mul = convHexStrToInt(memory[ss*256 + sp + 1]) * convHexStrToInt(memory[ss*256 + sp]);
			memory[ss*256 + sp] = convIntToHexStr(mul,8);
			
			incrTimer(supervisorMem, memory);
			
			ic++;
			supervisorMem[IC] = convIntToHexStr(ic, 8);
			
		} else if(command.substring(0, 2).equals("KR")) {
				
			int address = convHexStrToInt(command.substring(2,8));
			int temp = convHexStrToInt(memory[ds*256 + address]);
			supervisorMem[R] = convIntToHexStr(temp,8);
			
			incrTimer(supervisorMem, memory);
			
			ic++;
			supervisorMem[IC] = convIntToHexStr(ic, 8);
			
		} else if(command.substring(0, 2).equals("SR")) {
			
			int address = convHexStrToInt(command.substring(2,8));
			int temp = convHexStrToInt(supervisorMem[R]);
			memory[ds*256 + address] = convIntToHexStr(temp,8);
			
			incrTimer(supervisorMem, memory);
			
			ic++;
			supervisorMem[IC] = convIntToHexStr(ic, 8);
			
		} else if(command.substring(0, 4).equals("PUSH")) {
			
			sp = sp - 1;
			supervisorMem[SP] = convIntToHexStr(sp, 8);
			memory[ss*256 + sp] = convIntToHexStr(r, 8);	
			
			incrTimer(supervisorMem, memory);
			
			ic++;
			supervisorMem[IC] = convIntToHexStr(ic, 8);
			
		} else if(command.substring(0, 3).equals("POP")) {
			
			r = convHexStrToInt(memory[ss*256 + sp]);
			supervisorMem[R] = convIntToHexStr(r, 8);
			sp = sp + 1;
			supervisorMem[SP] = convIntToHexStr(sp, 8);
			
			incrTimer(supervisorMem, memory);
			
			ic++;
			supervisorMem[IC] = convIntToHexStr(ic, 8);
			
		} else if(command.substring(0, 3).equals("CMP")) {
			
			
			int tempR = convHexStrToInt(supervisorMem[R]);
			int tempStack = convHexStrToInt(memory[ss*256 + sp]);
			int tempSF = convHexStrToInt(supervisorMem[SF]);
			
			int result = tempR - tempStack;
			
			if(result == 0) {
				tempSF = 10;
			} else if(result < 0) {
				tempSF = 1;
			} else if(result > 0) {
				tempSF = 0;
			}
			
			supervisorMem[SF] = convIntToHexStr(tempSF,8);
			
			incrTimer(supervisorMem, memory);
			
			ic++;
			supervisorMem[IC] = convIntToHexStr(ic, 8);
			
		} else if(command.substring(0, 2).equals("PD")) {
			
			int temp = convHexStrToInt(supervisorMem[R]);
			
			Proc.printWord(memory, supervisorMem, 7);
			
			System.out.println(supervisorMem[R]);
			
			incrTimer(supervisorMem, memory);
			
			ic++;
			supervisorMem[IC] = convIntToHexStr(ic, 8);
			
		} else if(command.substring(0, 2).equals("RD")) {
			
			int temp = convHexStrToInt(command.substring(2,8));	
			supervisorMem[R] = convIntToHexStr(temp,8);	
			
			incrTimer(supervisorMem, memory);
			
			ic++;
			supervisorMem[IC] = convIntToHexStr(ic, 8);
	
		} else if(command.substring(0, 2).equals("JM")) {
			
			int address = convHexStrToInt(command.substring(2,8));
			supervisorMem[IC] = convIntToHexStr(address,8);
			
			incrTimer(supervisorMem, memory);
			
		} else if(command.substring(0, 2).equals("JZ")) {
			
			int address = convHexStrToInt(command.substring(2,8));
			int tempSF = convHexStrToInt(supervisorMem[SF]);
			
			if(tempSF/10 == 1) {
				supervisorMem[IC] = convIntToHexStr(address,8);
			}
			
			incrTimer(supervisorMem, memory);
			
		} else if(command.substring(0, 2).equals("JN")) {
			
			int address = convHexStrToInt(command.substring(2,8));
			int tempSF = convHexStrToInt(supervisorMem[SF]);
			
			if(tempSF/10 == 0) {
				supervisorMem[IC] = convIntToHexStr(address,8);
			}
			
			incrTimer(supervisorMem, memory);
			
		} else if(command.substring(0, 2).equals("JB")) {
			
			int address = convHexStrToInt(command.substring(2,8));
			int tempSF = convHexStrToInt(supervisorMem[SF]);
			
			if((tempSF/10 == 0) && (tempSF%10 == 1)) {
				supervisorMem[IC] = convIntToHexStr(address,8);
			}
			
			incrTimer(supervisorMem, memory);
			
		} else if(command.substring(0, 2).equals("JA")) {
			
			int address = convHexStrToInt(command.substring(2,8));
			int tempSF = convHexStrToInt(supervisorMem[SF]);
			
			if((tempSF/10 == 0) && (tempSF%10 == 0)) {
				supervisorMem[IC] = convIntToHexStr(address,8);
			}
			
			incrTimer(supervisorMem, memory);
			
		} else if(command.substring(0, 4).equals("HALT")) {
			ic++;
			supervisorMem[IC] = convIntToHexStr(ic, 8);
		}
		
	}
	
}

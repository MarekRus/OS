package masina;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class RMasina {
	
	
	
	public static int convHexStrToInt(String word) {
		return (int)Long.parseLong(word, 16);
	}
	
	
	
	public static String convIntToHexStr(int x, int length) {
		String word = Integer.toHexString(x);
		while(word.length() < length) {
			word = "0" + word;
		}
		return word;
	}
	
	
	
	public static void translate(Memory memory, String[] vMemory, String[] supervisorMem) {
		
		int ptr = convHexStrToInt( supervisorMem[Proc.PTR] );
		
		for(int i = 0; i < 256; i++) {
		
			int rAddress = convHexStrToInt( memory.getWord(ptr*256 + i).substring(0, 4) );
			int vAddress = convHexStrToInt( memory.getWord(ptr*256 + i).substring(4, 8) );
			
			for(int g = 0; g < 256; g++) {
				memory.setWord(rAddress + g, vMemory[vAddress + g]);
			}
			
		}	
	}
	
	

	public static void main(String[] args) {
		
/*
 * Atminties inicializavimas		
 */
		Memory mem = new Memory();
		

/*
 * Sudaromas list'as is visu prieinamu realios atminties bloku
 */
		ArrayList availableBlocks = new ArrayList();
		
		for(int i = 0; i < 1024; i ++) {
			availableBlocks.add(i,i);
		}
		
/*
 * Atsitiktinis blokas yra priskiriamas puslapiavimui
 */	
		Random rand = new Random();
		
		int ptr = rand.nextInt(availableBlocks.size() - 0 ) + 0;
		
		availableBlocks.remove(ptr);
		
/*
 * Atsitiktiniai 256 blokai yra priskiriami virtualiai masinai
 */
		for(int i = 0; i < 256; i++) {
			int vAddr = rand.nextInt(availableBlocks.size() - 0 ) + 0;
			mem.setWord(256*ptr + i,  convIntToHexStr(i,4) + convIntToHexStr((int)availableBlocks.get(vAddr),4));
			availableBlocks.remove(vAddr);
		}
		
		
		//for(int i = 0; i < availableBlocks.size(); i++) {
		//	System.out.println(availableBlocks.get(i));
		//}
		
/*
 * Supervizorine atmintis yra 12 zodziu, kur kiekvienas zodis yra registras:
 * supervisorMem[0] = IC
 * supervisorMem[1] = R
 * supervisorMem[2] = PTR
 * supervisorMem[3] = MODE
 * supervisorMem[4] = SF
 * supervisorMem[5] = SP
 * supervisorMem[6] = CS
 * supervisorMem[7] = DS
 * supervisorMem[8] = SS
 * supervisorMem[9] = PI
 * supervisorMem[10] = SI
 * supervisorMem[11] = TI
 * 
 */		
		String[] supervisorMem = new String[12];
		
		supervisorMem[0] = "00000000";
		supervisorMem[1] = "00000000";
		supervisorMem[2] = convIntToHexStr(ptr, 8);
		supervisorMem[3] = "00000000";
		supervisorMem[4] = "00000000";
		supervisorMem[5] = convIntToHexStr(21503, 8);
		supervisorMem[6] = convIntToHexStr(0, 8);
		supervisorMem[7] = convIntToHexStr(86, 8);
		supervisorMem[8] = convIntToHexStr(170, 8);
		supervisorMem[9] = "00000000";
		supervisorMem[10] = "00000000";
		supervisorMem[11] = "00000000";
		

		
		//Puslapiavimas
		//for(int i = 0; i < 256; i++) {
		//	System.out.println(mem.getWord(ptr*256+i));
		//}

		
		
		Proc proc = new Proc();
		VMasina vm = new VMasina();
		
		String[] vMemory = vm.memory;
		
		int icTemp = 0;
		Scanner s = new Scanner(System.in);
		
		
		
/*
 * I duomenu segmenta yra irasomi zodziai		
 */
		proc.initDS(vMemory, supervisorMem);
		proc.printWord(vMemory, supervisorMem, 0);
		
		
		
		
/*
 * Paprastos komandos yra irasomos i atminti, bet nevykdant ju.
 * Komanda STEP ivykdo viena komanda adresu (CS*256 + IC)
 * Komanda RUN vykdo komandas is adreso (CS*256 + IC), iki pirmo sutikto HALT
 * Komanda LOAD uz'loadina komandas is txt failo i atminti
 * Komanda BPRINT pavaizduoja CS atminties bloka, kur yra IC 		
 */
		while(true) {
			
			proc.printWord(vMemory, supervisorMem, 1);
			
			String command = s.nextLine();
			
			if( (command.length() > 3) && ( command.substring(0, 4).equals("STEP") ) ) {
				
				proc.printInfo(vMemory, supervisorMem);
				
				proc.step(vMemory, supervisorMem);
				
			} else if( (command.length() > 2) && ( command.substring(0, 3).equals("RUN") ) ) {
				
				proc.run(vMemory, supervisorMem);
				
				
			} else if( (command.length() > 5) && ( command.substring(0, 6).equals("BPRINT") ) ) {
				
				proc.printMemBlock(vMemory, supervisorMem);
				
			}else if( (command.length() > 6) && ( command.substring(0, 7).equals("DSPRINT") ) ) {
			
				proc.printDSBlock(vMemory, supervisorMem, 0);
				
			}else if( ( command.length() > 4) && ( command.substring(0,4).equals("LOAD") ) ) {
			
				String programName = command.substring(4, command.length());
				
				icTemp = vm.load(vMemory, programName, supervisorMem, icTemp);
				
				proc.printWord(vMemory, supervisorMem, 3);
			
			} else {
				
				icTemp = vm.komanda(supervisorMem, icTemp, command);
				
			}
			
			translate(mem, vMemory, supervisorMem);
			
		}
		
	}

}

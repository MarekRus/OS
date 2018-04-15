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

	public static void main(String[] args) {
		
		Memory mem = new Memory();
		
		ArrayList availableBlocks = new ArrayList();
		
		for(int i = 0; i < 1024; i ++) {
			availableBlocks.add(i,i);
		}
		
		Random rand = new Random();
		
		
		
		int ptr = rand.nextInt(availableBlocks.size() - 0 ) + 0;
		
		availableBlocks.remove(ptr);
		
		for(int i = 0; i < 256; i++) {
			int vAddr = rand.nextInt(availableBlocks.size() - 0 ) + 0;
			mem.setWord(256*ptr + i,  convIntToHexStr(i,4) + convIntToHexStr((int)availableBlocks.get(vAddr),4));
			availableBlocks.remove(vAddr);
		}
		
		
		
		//Puslapiavimas
//		for(int i = 0; i < 256; i++) {
//			System.out.println(mem.getWord(ptr*256+i));
//		}

		
		
		
		Proc proc = new Proc();
		VMasina vm = new VMasina(ptr, mem, proc);
		
		String[] memory = new String[65536];
		
		int icTemp = 0;
		Scanner s = new Scanner(System.in);
		
		
		
		
		while(true) {
			
			String command = s.nextLine();
			
			
			if( (command.length() > 3) && ( command.substring(0, 4).equals("STEP") ) ) {
				
				proc.printInfo(memory);
				
				memory = proc.step(memory);
				
			} else if( (command.length() > 2) && ( command.substring(0, 3).equals("RUN") ) ) {
				
				memory = proc.run(memory);
				
			} else if( (command.length() > 5) && ( command.substring(0, 6).equals("BPRINT") ) ) {
				
				proc.printMemBlock(memory);
				
			} else {
				
				memory = vm.komanda(proc, icTemp, command);
				icTemp++;
				
			}
			
			
		}
		
		
		
		
		
	}
	

}

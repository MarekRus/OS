package masina;

import java.util.ArrayList;
import java.util.Random;

public class RMasina {
	
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
		
		
		
		
		
	}
	

}

package masina;

public class Memory {

	private String[] memory;
	
	public Memory() {
		System.out.println("Initializing memory...");
		
		this.memory = new String[262144];
		
		for(int i = 0; i < 262144; i++) {
			this.memory[i] = "00000000";
		}
		
		System.out.println("Done");
	}
	
	
	public String getWord(int address) {
		
		if((address >= 0) && (address < 262144)) {
			return this.memory[address];
		} else {
			return "00000000";
		}
		
	}
	
	
	public void setWord(int address, String word) {
		
		if((address >= 0) && (address < 262144)) {
			if(word.length() > 8)
				word = word.substring(0, 8);
			while(word.length() < 8)
				word = "0" + word;

			this.memory[address] = word;
		} 
		
	}
	
	
	
	
}

package masina;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class VMasina {

	
	
	public static  String[] memory;
	
	
	
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
	

	
	VMasina(){
		
		this.memory = new String[65536];
		
		for(int i = 0; i < 65536; i++) {
			memory[i] = "00000000";
		}
		
	}
	
	
	
	public static int komanda(String[] supervisorMem, int icTemp, String command) {
		
		int CSint = convHexStrToInt(supervisorMem[Proc.CS]);
		
		if( (command.length() > 2) && ( command.substring(0, 3).equals("ADD") ) ) {
			
			memory[CSint*256 + icTemp] = "ADD00000";
			icTemp++;
			
		} else if( (command.length() > 2) && ( command.substring(0, 3).equals("SUB") ) ) {
			
			memory[CSint*256 + icTemp] = "SUB00000";
			icTemp++;
			
		} else if( (command.length() > 2) && ( command.substring(0, 3).equals("MUL") ) ) {
			
			memory[CSint*256 + icTemp] = "MUL00000";
			icTemp++;
			
		} else if( (command.length() > 2) && ( command.substring(0, 3).equals("DIV") ) ) {
			
			memory[CSint*256 + icTemp] = "DIV00000";
			icTemp++;
			
		} else if( (command.length() > 2) && ( command.substring(0, 2).equals("KR") ) ) {
			
			memory[CSint*256 + icTemp] = command.substring(0, 8);
			icTemp++;
			
		} else if( (command.length() > 2) && ( command.substring(0, 2).equals("SR") ) ) {
			
			memory[CSint*256 + icTemp] = command.substring(0, 8);
			icTemp++;
			
		} else if( (command.length() > 3) && ( command.substring(0, 4).equals("PUSH") ) ) {
			
			memory[CSint*256 + icTemp] = "PUSH0000";
			icTemp++;
			
		} else if( (command.length() > 2) && ( command.substring(0, 3).equals("POP") ) ) {
			
			memory[CSint*256 + icTemp] = "POP00000";
			icTemp++;
			
		} else if( (command.length() > 2) && ( command.substring(0, 3).equals("CMP") ) ) {
			
			memory[CSint*256 + icTemp] = "CMP00000";
			icTemp++;
			
		} else if( (command.length() > 1) && ( command.substring(0, 2).equals("PD") ) ) {
			
			memory[CSint*256 + icTemp] = "PD000000";
			icTemp++;
			
		} else if( (command.length() > 2) && ( command.substring(0, 2).equals("RD") ) ) {
			
			memory[CSint*256 + icTemp] = command.substring(0, 8);
			icTemp++;
			
		} else if( (command.length() > 2) && ( command.substring(0, 2).equals("JM") ) ) {
			
			memory[CSint*256 + icTemp] = command.substring(0, 8);
			icTemp++;
			
		} else if( (command.length() > 2) && ( command.substring(0, 2).equals("JZ") ) ) {
			
			memory[CSint*256 + icTemp] = command.substring(0, 8);
			icTemp++;
			
		} else if( (command.length() > 2) && ( command.substring(0, 2).equals("JN") ) ) {
			
			memory[CSint*256 + icTemp] = command.substring(0, 8);
			icTemp++;
			
		} else if( (command.length() > 2) && ( command.substring(0, 2).equals("JB") ) ) {
			
			memory[CSint*256 + icTemp] = command.substring(0, 8);
			icTemp++;
			
		} else if( (command.length() > 2) && ( command.substring(0, 2).equals("JA") ) ) {
			
			memory[CSint*256 + icTemp] = command.substring(0, 8);
			icTemp++;
			
		} else if( (command.length() > 3) && ( command.substring(0, 4).equals("HALT") ) ) {
			
			memory[CSint*256 + icTemp] = "HALT0000";
			icTemp++;
			
		}	
		
		return icTemp;
		
	}
	
	
	
	public static int load(String[] memory, String programName, String[] supervisorMem, int icTemp) {
		
		int cs = convHexStrToInt(supervisorMem[Proc.CS]);
	
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(
					"ExternalMem.txt"));
			String line = reader.readLine();
			
			
			if(line.equals(programName)) {
				while(!line.equals("HALT")) {
					line = reader.readLine();
					//System.out.println(line);
					icTemp = komanda(supervisorMem, icTemp, line);
				}
				
				return icTemp;
			}
				
			while (line != null) {
				line = reader.readLine();
				
				if(line.equals(programName)) {
					while(!line.equals("HALT")) {
						line = reader.readLine();
						//System.out.println(line);
						icTemp = komanda(supervisorMem, icTemp, line);		
					}
				break;
				}
				
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return icTemp;
	}
	
	
	
	
	
}

import java.io.*;

/********************************************************************
 * Cipher
 * @author andersonic
 * 
 * Provide methods to encipher and decipher Strings and files based
 * on a code-phrase.
 *******************************************************************/

public class Cipher{
	private static final int ASCII_MIN = 32;
	private static final int ASCII_MAX = 127;
	
	public static char encodeChar(char toSwitch, char code) {
	    int base = (int)toSwitch - ASCII_MIN;
	    int offset = (int)code - ASCII_MIN;

	    int newChar = ((base + offset) % (ASCII_MAX - ASCII_MIN)) + ASCII_MIN;

	    return (char)newChar;
	}

	public static char decodeChar(char toSwitch, char code) {
		int base = (int)toSwitch - ASCII_MIN;
	    int offset = (int)code - ASCII_MIN;

	    int newChar = base - offset;

	    if(newChar < 0) {
	        newChar = ASCII_MAX + newChar;
	    } else {
	        newChar += ASCII_MIN;
	    }

	    return (char) newChar;
	}

	public static String encodeString(String payload, String code) {
	    char[] newString = new char[payload.length()];
	    for(int i = 0; i < payload.length(); i++) {
	        if(payload.charAt(i) >= ASCII_MAX || payload.charAt(i) < ASCII_MIN) {
	            newString[i] = payload.charAt(i);
	        } else {
	            newString[i] = encodeChar(payload.charAt(i), code.charAt(i % code.length()));
	        }
	    }
	    
	    return new String(newString);
	}

	public static String decodeString(String payload, String code) {
	    char[] newString = new char[payload.length()];
	    for(int i = 0; i < payload.length(); i++) {
	        if(payload.charAt(i) < ASCII_MIN || payload.charAt(i) >= ASCII_MAX) {
	            newString[i] = payload.charAt(i);
	        } else {
	            newString[i] = decodeChar(payload.charAt(i), code.charAt(i % code.length()));
	        }
	    }
	    return new String(newString);
	}
	
	public static void encodeToFile(String filename, String payload, String code){
		try {
            FileWriter fileWriter = new FileWriter(filename);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            
            bufferedWriter.write(encodeString(payload, code));
            bufferedWriter.newLine();

            bufferedWriter.close();
            fileWriter.close();
        } catch(IOException ex) {
            System.out.println("Error writing to file.");
        }
	}
	
	public static void encodeFile(String filename, String code) {
		try {
			String payload = readFile(filename);
            FileWriter fileWriter = new FileWriter(filename);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            
            bufferedWriter.write(encodeString(payload, code));
            bufferedWriter.newLine();

            bufferedWriter.close();
            fileWriter.close();
        } catch(IOException ex) {
            System.out.println("Error writing to file.");
        }
	}
	
	public static String decodeFile(String filename, String code) {
		return decodeString(readFile(filename), code);
	}
	
	public static String readFile(String filename) {
		String fileContents = "";
		String line = null;
		try {
            FileReader fileReader = new FileReader(filename);
            
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) 
            {
                fileContents += line + "\n";
            }   

            bufferedReader.close();  
            fileReader.close();
        } catch(FileNotFoundException ex) {
            System.out.println("Unable to open file");                
        } catch(IOException ex) {
            System.out.println("Error reading file.");                  
        }
		return fileContents;
	}
}

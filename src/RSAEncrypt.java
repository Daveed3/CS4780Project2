import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/*
	You will write a Java program to encrypt an arbitrary file. Note that the RSA algorithm specifies 
	how to encrypt a single number ( < n). To encrypt a file, it is sufficient to break up the file into blocks 
	so that each block can be treated as a number and encrypted with the RSA algorithm. In this homework,  you 
	will use a block size of 3 bytes. To treat each block as a number, simply use 00 - 26 encoding scheme (26 for space). 
	
	After encrypting the block, the encrypted number has to be written out to the output file. Use a space to 
	separate the encrypted numbers.
	
	For example. Suppose the input file consists of the following letters (in order) : this is an example.
	To generate the first encrypted number, pick up the first block consisting of three letters : t, h and i. Then 
	transform the block to an integer 190708 and encrypt it.
	 
	The encryption algorithm should be runnable from a file named “RSAEncrypt.java” (that is, this file should have 
	the main method). The program will be called with a plaintext file and a key file one each on a line. For this 
	part you should use pub_txt to encrypt the message.
	
	The program will be invoked as :
	
	c:\> java RSAEncrypt test.txt  pub_key.txt
	where test.txt (Links to an external site.) is the name of the file to be encrypted.
	The encrypted file that the program generates is a plaintext file named test.enc
	Note: In order for the scheme to work, make sure that n  is bigger than 262626.
 */
// a = 97, A = 65 (tho just use a .toLowerCase to subtract 97)
// z = 122

// run as java RSAEncrypt test.txt pub_key.txt


public class RSAEncrypt {
	static int blockSize = 6;
	static String fileName = "test.enc";
	
	public static void encryption(String keyFile, String p) {
		String keyy = readFile(keyFile);
		keyy = keyy.replace("n", " n");
		String[] keySplit = keyy.split(" ");
		ArrayList<String> pSplit = new ArrayList<String>();
		for (int i=0; i<p.length()/blockSize; i++) {
			pSplit.add(p.substring(i*blockSize, (i*blockSize)+blockSize));
		}
			
		int keyInt = Integer.parseInt(keySplit[2]);
		BigInteger pInt;
		BigInteger c;
		BigInteger n = new BigInteger(keySplit[5]);
		int nPad = keySplit[5].length();
		
		
		try {
			File file = new File(fileName);
			file.createNewFile();
			
			FileWriter writer = new FileWriter(fileName);
			String totalString = "";
			for (int i=0; i<pSplit.size();i++) {
				pInt = new BigInteger(pSplit.get(i));
				c = pInt.pow(keyInt).mod(n);
				
				int curPad = 0;
				while (curPad + (""+c.intValue()).length() != nPad) {
					totalString += "0";
					curPad++;
				}
				totalString += c.intValue();
			}
			
			writer.write(totalString);
			writer.close();
			System.out.println("Encrypted key " + fileName + " created");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	// Helper function to read file and return it as a String
	public static String readFile(String fileName) {
		String msg = "";
		try {
			File file = new File(fileName);
			Scanner reader = new Scanner(file);
			while (reader.hasNextLine()) {
				msg += reader.nextLine();
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	// Helper method to turn the string into its corresponding number (0-25) + special chars
	public static String messageToBlocks(String msg) {
		String msgInt = "";
		int max = 29;
		for (int letter=0; letter<msg.length(); letter++) {
			int num = (int) msg.charAt(letter) - 97;
			// Space key
			if (num == -65) {
				num = max-3;
			} else if (num == -51) {
				num = max-2;
			} else if (num == -53) {
				num = max-1;
			} else if (num == -87) {
				num = max;
			}
			if (num >=10 && num <= max) {
				msgInt += num;
			} else if (num >=0 && num <= 9) {
				msgInt += ("0" + num);
			}
		}
		// Padding if message isn't a multiple of 3
		while ((msgInt.length()% (blockSize/2)) != 0) {
			msgInt += "00";
		}
		return msgInt;
	}
	
	// Helper function for printing out String arrays
	public static void print(String[] array) {
		for (int i=0; i<array.length; i++) {
			System.out.print(array[i] + " " );
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		String p = messageToBlocks(readFile(args[0]).toLowerCase());
		encryption(args[1], p);
	}

}

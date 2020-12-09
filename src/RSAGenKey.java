import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Random;

//RSAGenKey.java : Generates a private key

/*
 * Your program, “RSAGenKey.java”, either takes the key size as input or  take input as p, q, and e.
 If  only one argument k is given, the program randomly picks p and q in k bits and generates a key pair.  
 For example:
c:\> java  RSAGenKey 12
Two files will be created.: pub_key.txt and pri_key.txt.  
pub_key.txt contains a public key in the following format:
e = 8311
n = 31005883
pri_key.txt contains the corresponding private key in the following format:
d = 11296191
n = 31005883
 
If the program takes p, q, and e as the input (java RSAGenKey p q e), it should generate the 
corresponding private key. The key pair should also be saved in two files as described above. For example
c:\> java RSA 6551 4733 8311
The same files pub_key.txt and pri_key.txt should be created.
 */

// run it as "java RSAGenKey <p> <q> <e>" or "java RSAGenKey <length>"

public class RSAGenKey {

	public static void calcAndKeys(BigInteger p, BigInteger q, BigInteger e) {	
		String[] publicKeyStrs = {"pub_key.txt", "en"};
		String[] privateKeyStrs = {"pri_key.txt", "dn"};
		
		// calculate n
		BigInteger n = p.multiply(q);
		
		// calculate phi(n)
		BigInteger phi = (p.subtract(new BigInteger("1"))).multiply(q.subtract(new BigInteger("1")));
		
		// calculate d
		BigInteger d = calcInverse(e, phi).mod(phi);
		
		// Key pairs
		BigInteger[] en = {e, n};
		BigInteger[] dn = {d, n};
		
		// publish public key KU={e,n}
		createFile(publicKeyStrs[0], publicKeyStrs[1], en);
		
		// publish private key KR={d,n}
		createFile(privateKeyStrs[0], privateKeyStrs[1], dn);
	}
	
	public static int findRandPrime(int lengthOfPrime) {
		String number = "";
		
		// Base case
		if (lengthOfPrime == 1) {
			int[] possible = {3, 5, 7, 9};
			
			return possible[(int)(Math.random() * (4))];
		}
		
		for (int i=0; i<lengthOfPrime; i++) {
			if (i==0) {
				number += (int)(Math.random() * 8)+1;
			} else {
				number += (int)(Math.random() * 9);
			}
		}
		
		int num = Integer.parseInt(number);
		while (!isPrime(num)) {
			num++;
		}
		
		return num;
	}
	
	public static int findFirstPrime(int lengthOfPrime) {
		String number = "1";
		
		// Base case
		if (lengthOfPrime == 1) {
			return 3;
		}
		
		// Get number to start at the however long it needs to be 
		// i.e. length 3 = 100, length 5 = 10,000, length 9 = 100,000,000
		for (int i=0; i<lengthOfPrime-1; i++) {
			number += "0";
		}
		
		// Start at this num
		int num = Integer.parseInt(number);
		while (!isPrime(num)) {
			num++;
		}
		
		// Returns first instance of prime number with given length
		return num;
	}
	
	// Helper function to determine if is a prime number
	public static boolean isPrime(int num) {
		for (int i=2; i<num-1; i++) {
			if (num%i == 0) {
				return false;
			}
		}
		return true;
	}
	
	public static BigInteger calcInverse(BigInteger num, BigInteger mod) {
		// find gcd(mod, num) = 1
		
		// Use Extended Euclidean Algorithm 
		
		// Initialize vars
		BigInteger q, r1, r2, r, t1, t2, t;
		r1 = mod;
		r2 = num;
		t1 = new BigInteger("0");
		t2 = new BigInteger("1");
		
		// Do Extended Euclidean algorithm calculations
		while (r2.compareTo(new BigInteger("0")) == 1) {
			// Short calculations
			q = r1.divide(r2);
			r = r1.mod(r2);
			t = t1.subtract(q.multiply(t2));
			
			// Tested to see if rows were good
//			printEEARow(q, r1, r2, r, t1, t2, t);
			
			// Transfer over variables
			r1 = r2;
			r2 = r;
			t1 = t2;
			t2 = t;
		}
		
		if (t1.compareTo(new BigInteger("0")) == -1) {
			t1 = t1.add(mod);
		}
		
		return t1;
	}
	
	// Helper function for calcInverse
	public static void printEEARow(int q, int r1, int r2, int r, int t1, int t2, int t) {
		System.out.println("q\t\tr1\t\tr2\t\tr\t\tt1\t\tt2\t\tt");
		System.out.println(q + "\t\t" + r1 + "\t\t" + r2 + "\t\t" + r + "\t\t" + t1 + "\t\t" + t2 + "\t\t" + t );
		System.out.println();
	}
	
	public static void createFile(String fileName, String keyType, BigInteger[] key) {
		try {
			File file = new File(fileName);
			file.createNewFile();
			
			FileWriter writer = new FileWriter(fileName);
			writer.write(keyType.charAt(0) + " = " + key[0].intValue() + 
					"\n" + keyType.charAt(1) + " = " + key[1].intValue());
			writer.close();
			System.out.println("File " + fileName + " created");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Helper function to debug arrays by printing them out for viewing
	public static void print(String[] array) {
		for (int i=0; i<array.length;i++) {
			System.out.print(array[i] + " " );
		}
		
	}
	
	public static void main(String[] args) {		
		if (args.length == 1) {
			// Do function where it took in only 1 value k
			int bit = 0;
			try {
				bit = Integer.parseInt(args[0])/3;
				if (bit == 0) {
					System.err.println("Make sure the length of the key you want is a multiple of 3");
				}
			} catch (Exception e) {
				System.err.println("Make sure the length of the key you want is a multiple of 3");
			}
			
			// Do args[0] / 3 for a p, a q, an e
			BigInteger p = BigInteger.probablePrime(bit, new Random());
			BigInteger q = BigInteger.probablePrime(bit, new Random());
			BigInteger e = BigInteger.probablePrime(bit, new Random());
						
			calcAndKeys(p, q, e);
		} else if (args.length == 3) {
			// Do function where it took in 3 values p, q, and e
			BigInteger p = new BigInteger(args[0]);
			BigInteger q = new BigInteger(args[1]);
			BigInteger e = new BigInteger(args[2]);

			calcAndKeys(p, q, e);
		} else {
			System.err.println("Incorrect amount of parameters. Please enter either 1 or 3 parameter values");
		}
	}
	
	
}

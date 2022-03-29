package crypto;

import static crypto.Helper.cleanString;
import static crypto.Helper.stringToBytes;
import static crypto.Helper.bytesToString;

import static crypto.Encrypt.encrypt;
import static crypto.Encrypt.generatePad;
import static crypto.Encrypt.caesar;
import static crypto.Encrypt.vigenere;
import static crypto.Encrypt.xor;
import static crypto.Encrypt.oneTimePad;
import static crypto.Encrypt.cbc;

import java.util.Arrays;
import java.util.Scanner;

/*
 * Part 1: Encode (with note that one can reuse the functions to decode)
 * Part 2: bruteForceDecode (caesar, xor) and CBCDecode
 * Part 3: frequency analysis and key-length search
 * Bonus: CBC with encryption, shell
 */


public class test {
	
	
	//---------------------------MAIN---------------------------
	public static void main(String args[]) {
		
		Scanner scanner = new Scanner (System.in);
		System.out.print("Message encryption or decryption ? (enter Encryption=0, Decryption=1)");
		int choice = scanner.nextInt();
		if(choice==0) { //programme pour coder un message
			System.out.print("Enter a message you'd like to encrypt : ");
			String message = scanner.nextLine();
			String cleanText = cleanString(message);
			System.out.println("Original input sanitized : " + cleanText);
			byte[] plainText = stringToBytes(message);
			System.out.println("Original input in byte array : " + Arrays.toString(plainText));
			
			System.out.print("Choose the encryption method (Caesar=0, Vigenere=1, XOR=2, One-Time pad=3, CBC=4) : ");
			int type = scanner.nextInt(); //type de chiffrement choisi
	
			byte key; //cl� sous forme de byte pour les m�thodes caesar et xor
			String keyword; //cl� sous forme de cha�ne de caract�res pour la m�thode vigenere
			byte[] pad; //cl� pour les m�thodes otp et cbc
			int size; //d�signe la taille du pad
			
			byte[] cipher = new byte [plainText.length];
			switch (type) {
			case 0 :
				System.out.print("Enter key : ");
				key = scanner.nextByte();
				cipher = caesar(plainText,key);
				break;
			case 1 :
				System.out.println("Enter keyword : ");
				keyword = scanner.nextLine();
				byte[] keyArr = stringToBytes(keyword);
				cipher = vigenere(plainText,keyArr);
				break;
			case 2 :
				System.out.println("Enter key : ");
				key = scanner.nextByte();
				cipher = xor(plainText,key);
				break;
			case 3 :
				size = (stringToBytes(message)).length;
				pad = generatePad(size);
				cipher = oneTimePad(plainText,pad);
				break;
			case 4 :
				System.out.print("Enter key size : ");
				size = scanner.nextInt();
				pad = generatePad(size);
				cipher = cbc(plainText,pad);
				break;
			}
			scanner.close();
			String cipherText = bytesToString(cipher);
			System.out.println("Encrypted message : " + cipherText);
		}
		
	}
	
	
	//Run the Encoding and Decoding using the caesar pattern 
	public static void testCaesar(byte[] string , byte key) {
		//Encoding
		byte[] result = Encrypt.caesar(string, key);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);
		
		//Decoding with key
		String sD = bytesToString(Encrypt.caesar(result, (byte) (-key)));
		System.out.println("Decoded knowing the key : " + sD);
		
		//Decoding without key
		byte[][] bruteForceResult = Decrypt.caesarBruteForce(result);
		String sDA = Decrypt.arrayToString(bruteForceResult);
		Helper.writeStringToFile(sDA, "bruteForceCaesar.txt");
		
		byte decodingKey = Decrypt.caesarWithFrequencies(result);
		String sFD = bytesToString(Encrypt.caesar(result, decodingKey));
		System.out.println("Decoded without knowing the key : " + sFD);
	}

}

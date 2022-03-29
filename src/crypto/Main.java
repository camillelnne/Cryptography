package crypto;

import static crypto.Helper.cleanString;
import static crypto.Helper.stringToBytes;
import static crypto.Helper.bytesToString;

import static crypto.Encrypt.*;
import static crypto.Decrypt.*;

import java.util.Arrays;
import java.util.Scanner;

/*
 * Part 1: Encode (with note that one can reuse the functions to decode)
 * Part 2: bruteForceDecode (caesar, xor) and CBCDecode
 * Part 3: frequency analysis and key-length search
 * Bonus: CBC with encryption, shell
 */


public class Main {
	
	
	//---------------------------MAIN---------------------------
	public static void main(String args[]) {
		Scanner scanner = new Scanner (System.in);
		boolean tryagain = true; 
		while (tryagain) {
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
				String cipherText = bytesToString(cipher);
				System.out.println("Encrypted message : " + cipherText);
			}else { //programme pour d�coder
				System.out.print("Enter a message you'd like to decrypt : ");
				String message = scanner.nextLine();
				String cleanText = cleanString(message);
				System.out.println("Original input sanitized : " + cleanText);
				
				System.out.print("Choose the encryption method (0 = Caesar, 1 = Vigenere, 2 = XOR) : ");
				int type = scanner.nextInt(); //type de chiffrement choisi
				String decrypted = breakCipher(cleanText, type);
				System.out.println("Decrypted message : " + decrypted);
			}
			System.out.print("Do you want to try again ? (yes=true or no=false) ");
			tryagain=scanner.nextBoolean();
		}
		scanner.close();
	}
	

}

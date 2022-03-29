package crypto;

import java.util.Arrays;
import java.util.Scanner;
import static crypto.Helper.cleanString;
import static crypto.Helper.stringToBytes;
import static crypto.Helper.bytesToString;

import static crypto.Encrypt.caesar;

public class cesartest {
	public static void main(String[] args) {
		Scanner scanner = new Scanner (System.in);
		System.out.print("Enter a message you'd like to encrypt : ");
		String message = scanner.nextLine(); 
		byte[] plainText = stringToBytes(message); //converti le texte en bits
		System.out.println(Arrays.toString(plainText));
		
		System.out.print("Enter key (between -128 and 127) : "); //entrer la clé de cryptage
		byte key = scanner.nextByte();
		scanner.close();
		
		byte[] cipher = caesar(plainText, key);
		
		System.out.println(Arrays.toString(cipher));
		String cipherText = bytesToString(cipher);
		System.out.println(cipherText);
	}
}
package crypto;

import static crypto.Helper.bytesToString;
import static crypto.Helper.stringToBytes;

import static crypto.Encrypt.encrypt;
import static crypto.Encrypt.generatePad;

import java.util.Scanner;

public class testEncrypt {
	public static void main(String[] args) {
		Scanner scanner = new Scanner (System.in);
		System.out.print("Enter message : " );
		String message = scanner.nextLine();
		System.out.print("Choose the encryption method (Caesar=0, Vigenere=1, XOR=2, One-Time pad=3, CBC=4) : ");
		int type = scanner.nextInt();

		String key = null;
		int size;

		switch (type) {
		case 0 :
		case 1 :
		case 2 :
			System.out.println("Enter key : ");
			key = scanner.nextLine();
			break;
		case 3 :
			size = stringToBytes(message).length;
			System.out.print(size);
			byte[] pad = new byte[size];
			pad = generatePad(size);
			key = bytesToString(pad);
			break;
		case 4 :
			System.out.print("Enter key size : ");
			size = scanner.nextInt();
			pad = new byte[size];
			pad = generatePad(size);
			key = bytesToString(pad);
			break;
		}
		String cipherText = encrypt(message, key, type);
		System.out.println(cipherText);
	}
}

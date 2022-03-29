package crypto;

import static crypto.Helper.stringToBytes;
import static crypto.Helper.bytesToString;

import java.util.Scanner;

public class xorBruteForce {
	public static void main(String[] args) {
		Scanner scanner = new Scanner (System.in);
		System.out.print("Enter encoded message : ");
		String cipherText = scanner.nextLine();
		scanner.close();
		byte[] cipher = stringToBytes(cipherText);
		byte[][] bruteForce = new byte[256][cipher.length];
		for (int key=0; key<256; ++key) {
			for (int j=0; j<cipher.length; ++j) {
				if (bruteForce[key][j]==32) {
					bruteForce[key][j] = cipher[j];
				}else {
				bruteForce[key][j] = (byte) (cipher[j]^key);
				}
			}
		}
		for (int key=0; key<256; ++key) {
			String message = bytesToString(bruteForce[key]);
			System.out.println("For key = "+ (byte) key + ", decoded message : "+ message);
			Helper.writeStringToFile(message, "bruteForce.txt");
		}
	}
}

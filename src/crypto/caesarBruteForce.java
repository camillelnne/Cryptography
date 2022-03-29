package crypto;

import static crypto.Helper.stringToBytes;
import static crypto.Helper.bytesToString;

import java.util.Scanner;

public class caesarBruteForce {
	public static void main(String[] args) {
		Scanner scanner = new Scanner (System.in);
		System.out.print("Enter encoded message : ");
		String cipherText = scanner.nextLine();
		scanner.close();
		byte[] cipher = {-128, 32, -119, 124, 120, -125, -125, -112, 32, 127, -122, -121, 124, 32, -117, 127, -128, -118, 32, -114, -122, -119, -126, 32, -128, 32, -114, -128, -125, -125, 32, -127, -116, -118, -117, 124, 32, -117, 120, -126, 124, 32, -117, 127, 124, 32, 120, -119, -119, 120, -112, 32, -123, -122, -117, 32, -117, 127, 124, 32, -124, 124, -118, -118, 120, 126, 124};
		byte[][] bruteForce = new byte[256][cipher.length];
		for (int key=0; key<256; ++key) {
			for (int j=0; j<cipher.length; ++j) {
				if (cipher[j]==32) {
					bruteForce[key][j] = cipher[j];
				}else {
				bruteForce[key][j] = (byte) (cipher[j]-key);
				}
			}
		}
		String message;
		for (int key=0; key<256; ++key) {
			message = bytesToString(bruteForce[key]);
			System.out.println("For key = "+ (byte) key + ", decoded message : "+ message);
			Helper.writeStringToFile(message, "bruteForce.txt");
		}
	}
}

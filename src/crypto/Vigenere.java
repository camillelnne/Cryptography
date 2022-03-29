package crypto;

import static crypto.Helper.bytesToString;
import static crypto.Helper.stringToBytes;

import java.util.Scanner;
import java.util.Arrays;

public class Vigenere {
	public static void main(String[] args) {
		Scanner scanner = new Scanner (System.in);
		System.out.print("Enter a message you'd like to encrypt : ");
		String message = scanner.nextLine();
		byte[] plainText = stringToBytes(message);
		System.out.println(Arrays.toString(plainText));
		System.out.print("Enter key : ");
		String key = scanner.nextLine();
		byte[] keyword = {50, -10, 100};
		scanner.close();
		
		byte[] cipher = new byte[plainText.length];
		if (plainText.length<=keyword.length) { //si le mot cl� est plus long que le message, on va le tronquer, si les tailles sont �gales la cl� tronqu�e sera �gale � la cl�
			byte[]keyshort = new byte [plainText.length];
			for (int i=0; i<plainText.length; ++i) {
				keyshort[i]=keyword[i];
				if (plainText[i]==32) {
					cipher[i]=plainText[i];
				}else {
					cipher[i]=(byte) (plainText[i]+keyshort[i]);
				}
			} //remarque : on peut aussi regrouper dans cette boucle conditionnelle les deux cas "taille de la cl� inf�rieure ou �gale au message"
		}else if (plainText.length>keyword.length) {
			int modulo = plainText.length%keyword.length;
			for (int i = 0; i<plainText.length-modulo; i+=keyword.length) { 
				for (int j = 0; j<keyword.length; ++j) {
					if (plainText[i+j]==32) {
						cipher[i+j]=plainText[i+j];
					}else {
						cipher[i+j]=(byte) (plainText[i+j]+keyword[j]);
					}
				}
			}
			byte[] keymodulo = new byte[modulo]; //on tronque la cl� � la taille du reste du message
			for (int k = 0; k<modulo; ++k) {
				keymodulo[k]= keyword[k];
			}
			for (int i = plainText.length-modulo; i<plainText.length; ++i){ //boucle pour chiffrer le reste du message
				for (int j = 0; j<modulo; ++j) {
					if (plainText[i+j]==32) {
						cipher[i+j]=plainText[i+j];
					}else {
						cipher[i+j]=(byte) (plainText[i+j]+keyword[j]);
					}
				}
			}
			
		}
	    System.out.println(Arrays.toString(cipher));
		String cipherText = bytesToString(cipher);
		System.out.println(cipherText);
	}
}

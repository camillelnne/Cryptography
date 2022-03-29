package crypto;

import java.util.Arrays;
import java.util.Scanner;
import static crypto.Helper.stringToBytes;
import static crypto.Helper.bytesToString;

public class XOR {
	public static void main(String[] args) {
		Scanner scanner = new Scanner (System.in);
		System.out.print("Enter a message you'd like to encrypt : ");
		String message = scanner.nextLine();
		byte[] plainText = stringToBytes(message); //converti le texte en bits
		
		System.out.print("Enter key : "); //entrer la cl� de cryptage
		byte key = scanner.nextByte();
		scanner.close();
		
		byte[] cypherArr = new byte[plainText.length]; //on cr�e un tableau pour le message en bits apr�s op�ration XOR
		for (int i = 0; i<plainText.length; ++i) {
			if (plainText[i]!=32) {
				cypherArr[i] = (byte) (plainText[i]^key); //l'op�ration XOR donne des entiers qu'on convertit en bits pour les affecter au tableau
			}
			else {
				cypherArr[i] = plainText[i]; //si la valeur est 32 cela correspond � un espace, donc on ne le chiffre pas
			}
		}
		System.out.println(Arrays.toString(cypherArr));
		String cypherText = bytesToString(cypherArr);
		System.out.println(cypherText);
	}
}
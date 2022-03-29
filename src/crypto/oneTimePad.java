package crypto;

import static crypto.Helper.bytesToString;
import static crypto.Helper.cleanString;
import static crypto.Helper.stringToBytes;

import java.util.Arrays;
import java.util.Scanner;

public class oneTimePad {
	public static void main(String[] args) {
		Scanner scanner = new Scanner (System.in);
		System.out.print("Enter a message you'd like to encrypt : "); //entrer le message � coder
		String message = scanner.nextLine();
		scanner.close();
		String cleanText = cleanString(message); // remplace la ponctuation par des espaces
		byte[] plainText = stringToBytes(cleanText); //converti le texte en tableau de bits
	    byte[] pad = {1, 2, 3, 4, 5, 6};
		byte[] cypherArr = new byte[plainText.length]; //on cr�e un tableau pour le message en bits apr�s op�ration XOR
		for (int i = 0; i<plainText.length; ++i) { //boucle pour effectuer xor sur le message
				cypherArr[i] = (byte) (plainText[i]^pad[i]); //l'op�ration XOR donne des entiers qu'on convertit en bits pour les affecter au tableau
		}
		System.out.println(Arrays.toString(cypherArr));
		String cypherText = bytesToString(cypherArr);
		System.out.println(cypherText);
	}
}
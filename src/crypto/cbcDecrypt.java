package crypto;

import static crypto.Helper.stringToBytes;
import static crypto.Helper.bytesToString;

import java.util.Arrays;
import java.util.Scanner;

public class cbcDecrypt {
	public static void main(String[] args) {
		Scanner scanner = new Scanner (System.in);
		System.out.print("Enter a message you'd like to decrypt : "); //entrer le message � coder
		String message = scanner.nextLine();
		byte[] cipher = {99, 109, 109, 13, 8, 77, 99, 125, 36, 23, 93, 71, 118, 48};
		System.out.println(Arrays.toString(cipher));
		scanner.close();
	    byte[] iv = {1, 2, 3};
	    int size = iv.length;
		byte[] plainText = new byte[cipher.length]; //boucle pour la partie du message divisible par la taille du pad
		int modulo = cipher.length%size;
		for (int i = 0; i<cipher.length-modulo; i+=size)  {
				for (int j = 0; j<size; ++j) {
					plainText[i+j]=(byte) (cipher[i+j]^iv[j]);
					iv[j]=cipher[i+j];
				}
		}
		if (modulo!=0) { //si la taille du message n'est pas un multiple de la taille de cl�
			byte[] ivmodulo = new byte[modulo]; //on cr�e un tableau de byte de la taille du reste non cod� du message, auquel on va affecter le d�bute de la cl� cipher
			for (int k = 0; k<modulo; ++k) {
				ivmodulo[k]= cipher[k+(plainText.length-modulo-size)];
			}
			for (int i = cipher.length-modulo; i<cipher.length; ++i){ //boucle pour la partie du message inf�rieure � la taille du pad
					plainText[i]=(byte) (cipher[i]^ivmodulo[i-(cipher.length-modulo)]);
			}
		}
		System.out.println(Arrays.toString(plainText));
		String cipherText = bytesToString(plainText);
		System.out.print(cipherText);
		
	}
}
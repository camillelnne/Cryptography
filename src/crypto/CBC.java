package crypto;

import static crypto.Helper.stringToBytes;
import static crypto.Helper.bytesToString;

import static crypto.Encrypt.generatePad;
import java.util.Arrays;
import java.util.Scanner;

public class CBC {
	public static void main(String[] args) { //99, 109, 109, 67, 12, 29, 51, -27, 105, 90, -111, 73, 63, -27, 105, 93, -118, 7, 51, -17, 39, 93, -102, 78, 23
		Scanner scanner = new Scanner (System.in);
		System.out.print("Enter a message you'd like to encrypt : "); //entrer le message � coder
		String message = scanner.nextLine();
		byte[] plainText = stringToBytes(message); //converti le texte en tableau de bits
		System.out.println(Arrays.toString(plainText));
		System.out.print("Enter key size : ");
		int size = scanner.nextInt();
		scanner.close();
	    byte[] iv = {1, 2, 3};
	    System.out.println("Random key : " +Arrays.toString(iv));
	    size = iv.length;
		byte[] cipher = new byte[plainText.length]; //boucle pour la partie du message divisible par la taille du pad
		int modulo = (plainText.length)%size;
		for (int i = 0; i<plainText.length-modulo; i+=size)  {
				for (int j = 0; j<size; ++j) {
						cipher[i+j]=(byte) (plainText[i+j]^iv[j]);
						iv[j]=cipher[i+j];
				}
		}
		if (modulo!=0) { //si la taille du message n'est pas un multiple de la taille de cl�
			byte[] ivmodulo = new byte[modulo]; //on cr�e un tableau de byte de la taille du reste non cod� du message, auquel on va affecter le d�bute de la cl� cipher
			for (int k = 0; k<modulo; ++k) {
				ivmodulo[k]= cipher[k+(plainText.length-modulo-size)]; //on affecte a ivmodulo une partie de taille modulo du dernier cipher obtenu
			}
			System.out.println(Arrays.toString(ivmodulo));
			for (int i = plainText.length-modulo; i<plainText.length; ++i){ //boucle pour la partie du message inf�rieure � la taille du pad//on parcourt la cl� de taille = modulo
					cipher[i]=(byte) (plainText[i]^ivmodulo[i-(plainText.length-modulo)]);
			}
		}
		System.out.println(Arrays.toString(cipher));
		String cipherText = bytesToString(cipher);
		System.out.print(cipherText);
		
	}
}